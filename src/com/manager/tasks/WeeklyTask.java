package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class WeeklyTask extends Task {

    private boolean[] week;

    public WeeklyTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime, boolean[] week) {
        super(taskName, additionalInfo, startingTime, endingTime);
        this.week = week;
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        StringBuilder str = new StringBuilder("Weekly task\n");
        str.append("Monday: " + (this.week[0] ? "yes" : "no"));
        str.append("\nTuesday: " + (this.week[1] ? "yes" : "no"));
        str.append("\nWednesday: " + (this.week[2] ? "yes" : "no"));
        str.append("\nThursday: " + (this.week[3] ? "yes" : "no"));
        str.append("\nFriday: " + (this.week[4] ? "yes" : "no"));
        str.append("\nSaturday: " + (this.week[5] ? "yes" : "no"));
        str.append("\nSunday: " + (this.week[6] ? "yes" : "no"));
        return str.toString();
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        if(date != null) {
            return week[date.getDayOfWeek().getValue() - 1];
        }
        return false;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.WEEKLY_TASK;
    }

    public boolean[] getWeek() {
        return week;
    }

    public void setWeek(boolean[] week) {
        this.week = week;
    }
}
