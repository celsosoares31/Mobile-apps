package com.example.control_gastos;

public class Expense {
    private final long id;
    private final String date;
    private final String time;
    private final String location;
    private final String item;
    private final double amount;
    private final String photo;

    public Expense(long id, String date, String time, String location, String item, double amount, String photo) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.location = location;
        this.item = item;
        this.amount = amount;
        this.photo = photo;
    }

    public long getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }

    public String getItem() {
        return item;
    }

    public double getAmount() {
        return amount;
    }

    public String getPhoto() {
        return photo;
    }
}
