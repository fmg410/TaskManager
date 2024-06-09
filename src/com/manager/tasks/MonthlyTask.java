package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class MonthlyTask extends Task {

    private int dayOfMonth;

    public MonthlyTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime, int dayOfMonth) {
        super(taskName, additionalInfo, startingTime, endingTime);
        this.dayOfMonth = dayOfMonth;
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        return "Monthly task\nDay of month: " + this.dayOfMonth;
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        if(date != null) {
            return date.getDayOfMonth() == this.dayOfMonth;
        }
        return false;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.MONTHLY_TASK;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}
