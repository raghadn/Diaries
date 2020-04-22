package com.example.diaries;

public class Diaries {

    public String day,details,month,subject,year,uid,date;

    public Diaries(){

    }

    public Diaries(String day, String month, String year, String subject, String details) {
        this.day = day;
        this.month = month;
        this.year = year;
        this.subject = subject;
        this.details = details;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
