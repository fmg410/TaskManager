package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class YearlyTask extends Task {

    LocalDate taskDate;

    public YearlyTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime, LocalDate taskDate) {
        super(taskName, additionalInfo, startingTime, endingTime);
        this.taskDate = taskDate;
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        return "YearlyTask\nMonth: " + this.taskDate.getMonthValue() + "Day: " + this.taskDate.getDayOfMonth();
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        if(date != null) {
            return date.getDayOfMonth() == taskDate.getDayOfMonth() && taskDate.getMonthValue() == LocalDate.now().getMonthValue();
        }
        return false;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.YEARLY_TASK;
    }

    public LocalDate getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(LocalDate taskDate) {
        this.taskDate = taskDate;
    }
}
