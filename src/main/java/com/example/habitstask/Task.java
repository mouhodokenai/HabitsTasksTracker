package com.example.habitstask;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.util.Date;

public class Task {
    private int id;
    private String title;
    private String dateS;
    private LocalDate dateF;
    private Button button;
    private String warning;
    private boolean done;

    public Task(int id, String title, String dateS, LocalDate dateF, boolean done) {
        this.id = id;
        this.title = title;
        this.dateS = dateS;
        this.dateF = dateF;
        this.done = done;
    }

    public Task(int id, String title, String dateS, boolean done) {
        this.id = id;
        this.title = title;
        this.dateS = dateS;
        this.done = false;
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

    public String getDateS() {
        return dateS;
    }

    public void setDateS(String dateS) {
        this.dateS = dateS;
    }

    public LocalDate getDateF() {
        return dateF;
    }

    public void setDateF(LocalDate doneF) {
        this.dateF = doneF;
    }

    public String getWarning() {
        return warning;
    }

    public void setMakeDone(boolean done) {
        if (done){
            warning = "✔";
        } else {
            warning = " ";
        }
    }

    public void setWarning(Date dateF) {
        Date currentDate = new Date(); // Получаем текущую дату
        // Сравниваем переданную дату с текущей
        if (dateF.before(currentDate)) {
            warning = "!";
        } else {
            warning = "";
        }
    }


    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
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
}
