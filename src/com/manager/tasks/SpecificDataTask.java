package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class SpecificDataTask extends Task {

    private LocalDate taskDate;

    public SpecificDataTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime, LocalDate taskDate) {
        super(taskName, additionalInfo, startingTime, endingTime);
        this.taskDate = taskDate;
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        return "Specific data task\nData: " + this.taskDate.format(super.dateFormatter);
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        if(date != null) {
            return date.equals(taskDate);
        }
        return false;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.SPECIFIC_DATA_TASK;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
}
