package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;


public class DailyTask extends Task {

    public DailyTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime) {
        super(taskName, additionalInfo, startingTime, endingTime);
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        return "Daily task";
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        return true;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.DAILY_TASK;
    }
}
