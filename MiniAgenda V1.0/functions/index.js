/**
 * Import function triggers from their respective submodules:
 *
 * const {onCall} = require("firebase-functions/v2/https");
 * const {onDocumentWritten} = require("firebase-functions/v2/firestore");
 *
 * See a full list of supported triggers at https://firebase.google.com/docs/functions
 */

const {onRequest} = require("firebase-functions/v2/https");
const logger = require("firebase-functions/logger");

// Create and deploy your first functions
// https://firebase.google.com/docs/functions/get-started

// exports.helloWorld = onRequest((request, response) => {
//   logger.info("Hello logs!", {structuredData: true});
//   response.send("Hello from Firebase!");
// });

const admin = require("firebase-admin");

admin.initializeApp();

// Listen for new data in the Realtime Database
exports.sendNotificationOnNewData = functions.database.ref("/persons/{person_id}")
    .onCreate(async (snapshot, context) => {
        // Fetch the newly added data
        const newData = snapshot.val();

        // Notification payload
        const payload = {
            notification: {
                title: "New Data Added!",
                body: `New data: ${newData}`, // Customize your notification message here
            },
        };

        try {
            // Get all the FCM tokens of users who should receive the notification
            const tokensSnapshot = await admin.database().ref("/users").once("value");
            const tokens = [];
            tokensSnapshot.forEach((childSnapshot) => {
                const token = childSnapshot.val().fcmToken;
                if (token) {
                    tokens.push(token);
                }
            });

            // Send FCM notification
            const response = await admin.messaging().sendToDevice(tokens, payload);
            console.log("Notification sent successfully:", response);
        } catch (error) {
            console.error("Error sending notification:", error);
        }
    });