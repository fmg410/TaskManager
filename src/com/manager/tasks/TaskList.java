package com.manager.tasks;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Iterator;

public class TaskList { //a singleton used to control tasks in programme
    private static TaskList instance = new TaskList();

    private ObservableList<Task> taskList;

    public static TaskList getInstance() {
        return instance;
    }

    private TaskList() {
        taskList = FXCollections.observableArrayList(Task.extractor());
    }

    public ObservableList<Task> getTaskList() {
        return taskList;
    }

    public void addTask(Task task) {
        this.taskList.add(task);
    }

    public void deleteTask(Task task) {
        taskList.remove(task);
    }

    public void loadTasks() throws IOException {

        Path path = null;
        BufferedReader br = null;
        String input;


        for(TaskTypes type : TaskTypes.values()) {
            switch(type) {
                case DAILY_TASK:
                    path = Paths.get("DailyTasks.txt");
                    break;
                case EVERY_CERTAIN_DAYS_TASK:
                    path = Paths.get("EveryCertainDaysTasks.txt");
                    break;
                case MONTHLY_TASK:
                    path = Paths.get("MonthlyTasks.txt");
                    break;
                case SPECIFIC_DATA_TASK:
                    path = Paths.get("SpecificDataTasks.txt");
                    break;
                case WEEKLY_TASK:
                    path = Paths.get("WeeklyTasks.txt");
                    break;
                case YEARLY_TASK:
                    path = Paths.get("YearlyTasks.txt");
                    break;
            }
            if(path != null) {
                if(Files.exists(path)) {
                    br = Files.newBufferedReader(path);
                }
            }

            if(br != null) {

                try {
                    while ((input = br.readLine()) != null) {
                        Task task = null;
                        String[] itemPieces = input.split("\t");

                        String taskName = itemPieces[0];
                        String additionalInfo = itemPieces[1];
                        String startingTime = itemPieces[2];
                        String endingTime = itemPieces[3];
                        LocalTime startTime = LocalTime.parse(startingTime, Task.timeFormatter);
                        LocalTime endTime = LocalTime.parse(endingTime, Task.timeFormatter);

                        int everyCertainDays;
                        LocalDate everyCertainNextOccurrence;
                        int monthlyDayOfMonth;
                        LocalDate specificDataDate;
                        boolean[] weeklyWeek = new boolean[7];
                        LocalDate yearlyDate;

                        switch (type) {
                            case DAILY_TASK:
                                task = new DailyTask(taskName, additionalInfo, startTime, endTime);
                                break;
                            case EVERY_CERTAIN_DAYS_TASK:
                                everyCertainDays = Integer.parseInt(itemPieces[4]);
                                everyCertainNextOccurrence = LocalDate.parse(itemPieces[5], Task.dateFormatter);
                                task = new EveryCertainDaysTask(taskName, additionalInfo, startTime, endTime, everyCertainDays, everyCertainNextOccurrence);
                                break;
                            case MONTHLY_TASK:
                                monthlyDayOfMonth = Integer.parseInt(itemPieces[4]);
                                task = new MonthlyTask(taskName, additionalInfo, startTime, endTime, monthlyDayOfMonth);
                                break;
                            case SPECIFIC_DATA_TASK:
                                specificDataDate = LocalDate.parse(itemPieces[4], Task.dateFormatter);
                                task = new SpecificDataTask(taskName, additionalInfo, startTime, endTime, specificDataDate);
                                break;
                            case WEEKLY_TASK:
                                for (int i = 0; i < 7; i++) {
                                    weeklyWeek[i] = Boolean.parseBoolean(itemPieces[4 + i]);
                                }
                                task = new WeeklyTask(taskName, additionalInfo, startTime, endTime, weeklyWeek);
                                break;
                            case YEARLY_TASK:
                                yearlyDate = LocalDate.parse(itemPieces[4], Task.dateFormatter);
                                task = new YearlyTask(taskName, additionalInfo, startTime, endTime, yearlyDate);
                                break;
                        }

                        taskList.add(task);
                    }
                } catch(Exception e) {
                    e.printStackTrace();
                } finally {
                    if (br != null) {
                        br.close();
                    }
                }
            }

        }

    }


    public void storeTasks() throws IOException { // TODO alternatywa do roznych plikow to zrobienie w kazdej klasie task override abstrakcyjnej metody w Task ktora by zwracala stream bajtow ktory dalej by mozna podac bezposrednio do pliku
// TODO z takim podejsciem zrobic przycisk z kopia zapasowa do subfolderu
        BufferedWriter dailyTaskWriter = Files.newBufferedWriter(Paths.get("DailyTasks.txt"));
        BufferedWriter everyCertainDaysTaskWriter = Files.newBufferedWriter(Paths.get("EveryCertainDaysTasks.txt"));
        BufferedWriter monthlyTaskWriter = Files.newBufferedWriter(Paths.get("MonthlyTasks.txt"));
        BufferedWriter specificDataTaskWriter = Files.newBufferedWriter(Paths.get("SpecificDataTasks.txt"));
        BufferedWriter weeklyTaskWriter = Files.newBufferedWriter(Paths.get("WeeklyTasks.txt"));
        BufferedWriter yearlyTaskWriter = Files.newBufferedWriter(Paths.get("YearlyTasks.txt"));

        try {
            Iterator<Task> iter = taskList.iterator();
            while (iter.hasNext()) {
                Task task = iter.next();

                switch(task.getTaskType()) {
                    case DAILY_TASK:
                        dailyTaskWriter.write((String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter))));
                        dailyTaskWriter.newLine();
                        break;
                    case EVERY_CERTAIN_DAYS_TASK:
                        everyCertainDaysTaskWriter.write(String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter)));
                        everyCertainDaysTaskWriter.write(String.format("%s\t%s\t",
                                ((EveryCertainDaysTask) task).getAmountOfDaysBreak(), ((EveryCertainDaysTask) task).getNextTaskOccurrence().format(Task.dateFormatter)));
                        everyCertainDaysTaskWriter.newLine();
                        break;
                    case MONTHLY_TASK:
                        monthlyTaskWriter.write(String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter)));
                        monthlyTaskWriter.write(String.format("%s\t", ((MonthlyTask) task).getDayOfMonth()));
                        monthlyTaskWriter.newLine();
                        break;
                    case SPECIFIC_DATA_TASK:
                        specificDataTaskWriter.write(String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter)));
                        specificDataTaskWriter.write(String.format("%s\t", ((SpecificDataTask) task).getTaskDate().format(Task.dateFormatter)));
                        specificDataTaskWriter.newLine();
                        break;
                    case WEEKLY_TASK:
                        weeklyTaskWriter.write(String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter)));
                        for(int i = 0; i < 7; i++) {
                            weeklyTaskWriter.write(String.format("%b\t", ((WeeklyTask) task).getWeek()[i]));
                        }
                        weeklyTaskWriter.newLine();
                        break;
                    case YEARLY_TASK:
                        yearlyTaskWriter.write(String.format("%s\t%s\t%s\t%s\t",
                                task.getTaskName(), task.getAdditionalInfo(), task.getStartingTime().format(Task.timeFormatter), task.getEndingTime().format(Task.timeFormatter)));
                        yearlyTaskWriter.write(String.format("%s\t", ((YearlyTask) task).getTaskDate().format(Task.dateFormatter)));
                        yearlyTaskWriter.newLine();
                        break;
                }

            }

        } finally {
            if (dailyTaskWriter != null) {
                dailyTaskWriter.close();
            }
            if (everyCertainDaysTaskWriter != null) {
                everyCertainDaysTaskWriter.close();
            }
            if (monthlyTaskWriter != null) {
                monthlyTaskWriter.close();
            }
            if (specificDataTaskWriter != null) {
                specificDataTaskWriter.close();
            }
            if (weeklyTaskWriter != null) {
                weeklyTaskWriter.close();
            }
            if (yearlyTaskWriter != null) {
                yearlyTaskWriter.close();
            }
        }
    }

}
