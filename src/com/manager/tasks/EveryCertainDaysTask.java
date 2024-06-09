package com.manager.tasks;

import java.time.LocalDate;
import java.time.LocalTime;

public class EveryCertainDaysTask extends Task {

    private int amountOfDaysBreak;
    private LocalDate nextTaskOccurrence;

    public EveryCertainDaysTask(String taskName, String additionalInfo, LocalTime startingTime, LocalTime endingTime, int amountOfDaysBreak, LocalDate nextTaskOccurrence) {
        super(taskName, additionalInfo, startingTime, endingTime);
        this.amountOfDaysBreak = amountOfDaysBreak;
        this.nextTaskOccurrence = nextTaskOccurrence;
        refreshTask();
    }

    @Override
    public String taskTypeInfo() {
        return ("Every certain days task:\n" + "Amount of days between occurrence: " + this.amountOfDaysBreak +
                "\nNext task occurrence: " + this.nextTaskOccurrence.format(super.dateFormatter));
    }

    @Override
    public boolean isTaskThatDay(LocalDate date) {
        while(nextTaskOccurrence.isBefore(LocalDate.now())) {
            nextTaskOccurrence = nextTaskOccurrence.plusDays(amountOfDaysBreak);
        }
        if(date != null) {
            LocalDate temp = LocalDate.from(nextTaskOccurrence);
            if(date.isBefore(temp)) {
                while(date.isBefore(temp)) {
                    temp = temp.minusDays(amountOfDaysBreak);
                }
                return date.equals(temp);
            } else if(date.isAfter(temp)) {
                while(date.isAfter(temp)) {
                    temp = temp.plusDays(amountOfDaysBreak);
                }
                return date.equals(temp);
            } else {
                return date.equals(temp);
            }
        }
        return false;
    }

    @Override
    public TaskTypes getTaskType() {
        return TaskTypes.EVERY_CERTAIN_DAYS_TASK;
    }

    public int getAmountOfDaysBreak() {
        return amountOfDaysBreak;
    }

    public void setAmountOfDaysBreak(int amountOfDaysBreak) {
        this.amountOfDaysBreak = amountOfDaysBreak;
    }

    public LocalDate getNextTaskOccurrence() {
        return nextTaskOccurrence;
    }

    public void setNextTaskOccurrence(LocalDate nextTaskOccurrence) {
        this.nextTaskOccurrence = nextTaskOccurrence;
    }
}
