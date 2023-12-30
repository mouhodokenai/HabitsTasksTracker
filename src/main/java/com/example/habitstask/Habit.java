package com.example.habitstask;

import javafx.scene.control.Button;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Habit {
    private int id;
    private String title;
    private int periodicity;
    private LocalDate date;
    private boolean done;
    private Button button;
    private String makeDone;
    private long howDays;


    public Habit(int id, String title, int periodicity, LocalDate date, boolean done) {
        this.id = id;
        this.title = title;
        this.periodicity = periodicity;
        this.date = date;
        this.done = done;
    }

    public Habit() {
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

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int periodicity) {
        this.periodicity = periodicity;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public void makeDone(boolean done) {

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

    public void getDaysBetweenDates(LocalDate startDate) {
        LocalDate currentDate = LocalDate.now();
        howDays = ChronoUnit.DAYS.between(startDate, currentDate);
    }

    public long getHowDays() {
        return howDays;
    }

    public void setHowDays(long howDays) {
        this.howDays = howDays;
    }
}
