package com.manager.tasks;

import javafx.beans.Observable;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.util.Callback;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public abstract class Task {

    private String taskName;
    private String additionalInfo;
    private LocalTime startingTime;
    private LocalTime endingTime;

    public BooleanProperty isNow;
    public Boolean didPopUp;

    public static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public static DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

    public Task(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime) {
        this.isNow = new SimpleBooleanProperty(false);
        this.taskName = taskName;
        this.additionalInfo = additionalInfo;
        this.didPopUp = false;

        if(endingTime != null && startingTime != null) {
            this.startingTime = startingTime;
            this.endingTime = endingTime;

            if(endingTime.isBefore(startingTime)) { //ending time earlier than starting time
                this.endingTime = LocalTime.from(startingTime);
            }
        } else {
            this.startingTime = LocalTime.now();
            this.endingTime = LocalTime.now();
        }
    }

    public abstract TaskTypes getTaskType();

    public abstract String taskTypeInfo();

    public abstract boolean isTaskThatDay(LocalDate date);

    public boolean isTaskInThatMoment(LocalDateTime date) {
        if (date != null && isTaskThatDay(date.toLocalDate())) {
            if (getEndingTime() != null && getStartingTime() != null) {
                LocalTime time = date.toLocalTime();
                if((time.isAfter(startingTime) && time.isBefore(endingTime.plusSeconds(57))) || time.equals(startingTime) || time.equals(endingTime)){
                    return true;
                }
            }
        }
        return false;
    }

    public void refreshTask() {
        LocalDateTime date = LocalDateTime.now();
        if (isTaskThatDay(date.toLocalDate())) {
            if (getEndingTime() != null && getStartingTime() != null) {
                LocalTime time = date.toLocalTime();
                if ((time.isAfter(startingTime) && time.isBefore(endingTime.plusSeconds(57))) || time.equals(startingTime) || time.equals(endingTime)) {
                    this.isNow.setValue(true);
                    return;
                }
            }
        }
        this.isNow.setValue(false);
    }

    public boolean getTaskState() {
        return this.isNow.getValue();
    }

    public static Callback<Task, Observable[]> extractor() {
        return new Callback<Task, Observable[]>() {
            @Override
            public Observable[] call(Task task) {
                return new Observable[]{task.isNow};
            }
        };
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getAdditionalInfo() {
        return additionalInfo;
    }

    public void setAdditionalInfo(String additionalInfo) {
        this.additionalInfo = additionalInfo;
    }

    public LocalTime getStartingTime() {
        return startingTime;
    }

    public void setStartingTime(LocalTime startingTime) {
        this.startingTime = startingTime;
    }

    public LocalTime getEndingTime() {
        return endingTime;
    }

    public void setEndingTime(LocalTime endingTime) {
        this.endingTime = endingTime;
    }
}
