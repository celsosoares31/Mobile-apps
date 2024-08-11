package com.example.gestaoprojecto;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Recebe os dados do Intent
        long projectId = intent.getLongExtra("projectId", -1);
        String projectName = intent.getStringExtra("projectName");

        // Cria o NotificationManager
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        // Cria o Builder para a notificação
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "projectChannel")
                .setSmallIcon(R.drawable.ic_notification)  // Ícone pequeno da notificação
                .setContentTitle("Project Deadline Approaching")  // Título da notificação
                .setContentText("Project " + projectName + " is nearing its deadline.")  // Texto da notificação
                .setPriority(NotificationCompat.PRIORITY_HIGH);  // Prioridade da notificação

        // Notifica o NotificationManager com o ID do projeto
        notificationManager.notify((int) projectId, builder.build());
    }
}
