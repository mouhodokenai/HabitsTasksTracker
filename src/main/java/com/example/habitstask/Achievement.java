package com.example.habitstask;

import javafx.scene.control.Button;

public class Achievement {
    private int id;
    private String title;
    private String date;
    private boolean done;
    private Button button;
    private String makeDone;

    public Achievement(int id, String title, String date, boolean done) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.done = done;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    @Override
    public String toString() {
        return title;
    }

    public Button getButton() {
        return button;
    }

    public String getMakeDone() {
        return makeDone;
    }

    public void setMakeDone(boolean done) {
        if (done){
            makeDone = "✔";
        } else {
            makeDone = " ";
        }
    }

    public void setButton(Button button) {
        this.button = button;
    }
}
