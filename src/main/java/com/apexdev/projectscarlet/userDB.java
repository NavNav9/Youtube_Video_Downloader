package com.apexdev.projectscarlet;

import javafx.beans.property.SimpleStringProperty;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Locale;

public class userDB {
    //So making a DB like MariaDB or mysql :)

//Basically get the First Name (username) and set the private string so it can send it back
    private SimpleStringProperty firstName;

    public userDB(String firstName) {
        this.firstName = new SimpleStringProperty(firstName);
    }

    public SimpleStringProperty firstNameProperty() {
        return firstName;
    }


    public void setFirstName(String firstName){
        this.firstName.set(firstName);
    }



}
