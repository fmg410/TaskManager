package com.manager;

import com.manager.tasks.*;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;

public class TaskManager {
    @FXML
    private DialogPane taskManagerDialogPane;
    @FXML
    private ListView<Task> allTaskListView;

    public void initialize() {
        SortedList<Task> sortedList = new SortedList<Task>(TaskList.getInstance().getTaskList(),
                new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                            return o1.getTaskName().compareTo(o2.getTaskName());
                    }
                });

        allTaskListView.setItems(sortedList);
        allTaskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        allTaskListView.getSelectionModel().selectFirst();

        allTaskListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
            @Override
            public ListCell<Task> call(ListView<Task> TaskListView) {
                ListCell<Task> cell = new ListCell<Task>() {

                    @Override
                    protected void updateItem(Task task, boolean b) {
                        super.updateItem(task, b);
                        if (b) {
                            setText(null);
                        } else {
                            setText(task.getTaskName());
                        }
                    }
                };

                return cell;
            }
        });

    }

    @FXML
    public void showNewTaskDialog() {
        Dialog<ButtonType> newTaskDialog = new Dialog<>();
        newTaskDialog.initOwner(taskManagerDialogPane.getScene().getWindow());
        newTaskDialog.setTitle("Add new Task");
        newTaskDialog.setHeaderText("Use this dialog to create a new Task");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newTaskDialog.fxml"));
        try {
            newTaskDialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        newTaskDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        newTaskDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = newTaskDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            NewTaskDialog controller = fxmlLoader.getController();

            Dialog<ButtonType> taskTypeDialog = new Dialog<>();
            taskTypeDialog.initOwner(taskManagerDialogPane.getScene().getWindow());
            taskTypeDialog.setTitle("Input task type information");
            HBox box = new HBox(10);


            DatePicker picker = new DatePicker();
            Spinner spinnerEvery = new Spinner(0, 365, 0);
            CheckBox monday = new CheckBox("Monday");
            CheckBox tuesday = new CheckBox("Tuesday");
            CheckBox wednesday = new CheckBox("Wednesday");
            CheckBox thursday = new CheckBox("Thursday");
            CheckBox friday = new CheckBox("Friday");
            CheckBox saturday = new CheckBox("Saturday");
            CheckBox sunday = new CheckBox("Sunday");
            Spinner spinnerDayOfMonth = new Spinner(0, 31, 15);
            Spinner spinnerYearlyDayOfMonth = new Spinner(0, 31, 15);
            Spinner spinnerYearlyMonth = new Spinner(1, 12, 6);
            DatePicker specificPicker = new DatePicker();

            switch (controller.getSelectedTask()) {
                case DAILY_TASK:
                    break;
                case EVERY_CERTAIN_DAYS_TASK:
                    taskTypeDialog.setHeaderText("Choose a date of first occurrence, then select the number of days before new repetition");
                    box.getChildren().addAll(picker, spinnerEvery);
                    break;
                case WEEKLY_TASK:
                    taskTypeDialog.setHeaderText("Choose days of week in which the task should occur");
                    box.getChildren().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
                    break;
                case MONTHLY_TASK:
                    taskTypeDialog.setHeaderText("Select day of month");
                    box.getChildren().add(spinnerDayOfMonth);
                    break;
                case YEARLY_TASK:
                    taskTypeDialog.setHeaderText("Select month of year and its day");
                    box.getChildren().addAll(spinnerYearlyMonth, spinnerYearlyDayOfMonth);
                    break;
                case SPECIFIC_DATA_TASK:
                    taskTypeDialog.setHeaderText("Select data");
                    box.getChildren().add(specificPicker);
                    break;
                default:
                    break;
            }

            taskTypeDialog.getDialogPane().setContent(box);

            taskTypeDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            taskTypeDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Task newTask = null;

            if (controller.getSelectedTask() == TaskTypes.DAILY_TASK) {
                newTask = new DailyTask(controller.getTaskName(), controller.getAdditionalInfo(), controller.getStartingTime(), controller.getEndingTime());
            } else {
                Optional<ButtonType> resultType = taskTypeDialog.showAndWait();

                if (resultType.isPresent() && resultType.get() == ButtonType.OK) {

                    switch (controller.getSelectedTask()) {
                        case DAILY_TASK:
                            break;
                        case EVERY_CERTAIN_DAYS_TASK:
                            newTask = new EveryCertainDaysTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(), Integer.parseInt(spinnerEvery.getEditor().getText()), picker.getValue());
                            break;
                        case WEEKLY_TASK:
                            boolean[] week = {false, false, false, false, false, false, false};
                            week[0] = monday.isSelected();
                            week[1] = tuesday.isSelected();
                            week[2] = wednesday.isSelected();
                            week[3] = thursday.isSelected();
                            week[4] = friday.isSelected();
                            week[5] = saturday.isSelected();
                            week[6] = sunday.isSelected();
                            newTask = new WeeklyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(), week);
                            break;
                        case MONTHLY_TASK:
                            newTask = new MonthlyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    Integer.parseInt(spinnerDayOfMonth.getEditor().getText()));

                            break;
                        case YEARLY_TASK:
                            newTask = new YearlyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    LocalDate.of(2000, Integer.parseInt(spinnerYearlyMonth.getEditor().getText()),
                                            Integer.parseInt(spinnerYearlyDayOfMonth.getEditor().getText())));
                            break;
                        case SPECIFIC_DATA_TASK:
                            newTask = new SpecificDataTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    specificPicker.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
            if (newTask != null) {
                TaskList.getInstance().addTask(newTask);
            }
        }
    }

    @FXML
    public void showEditTaskDialog() {
        Dialog<ButtonType> editTaskDialog = new Dialog<>();
        editTaskDialog.initOwner(taskManagerDialogPane.getScene().getWindow());
        editTaskDialog.setTitle("Edit Task");
        editTaskDialog.setHeaderText("Use this dialog to edit a Task");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("editTaskDialog.fxml"));
        try {
            editTaskDialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        EditTaskDialog controller = fxmlLoader.getController();
        if(allTaskListView.getSelectionModel().getSelectedItem() == null) { // TODO zamiast tego blokowac przycisk edit gdy lista jest pusta
            return;
        }
        controller.setCurrentTask(allTaskListView.getSelectionModel().getSelectedItem());

        editTaskDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        editTaskDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

        Optional<ButtonType> result = editTaskDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            Dialog<ButtonType> taskTypeDialog = new Dialog<>();
            taskTypeDialog.initOwner(taskManagerDialogPane.getScene().getWindow());
            taskTypeDialog.setTitle("Input task type information");
            HBox box = new HBox(10);


            DatePicker picker = new DatePicker();
            Spinner spinnerEvery = new Spinner(0, 365, 0);
            CheckBox monday = new CheckBox("Monday");
            CheckBox tuesday = new CheckBox("Tuesday");
            CheckBox wednesday = new CheckBox("Wednesday");
            CheckBox thursday = new CheckBox("Thursday");
            CheckBox friday = new CheckBox("Friday");
            CheckBox saturday = new CheckBox("Saturday");
            CheckBox sunday = new CheckBox("Sunday");
            Spinner spinnerDayOfMonth = new Spinner(0, 31, 15);
            Spinner spinnerYearlyDayOfMonth = new Spinner(0, 31, 15);
            Spinner spinnerYearlyMonth = new Spinner(1, 12, 6);
            DatePicker specificPicker = new DatePicker();

            switch (controller.getSelectedTask()) {
                case DAILY_TASK:
                    break;
                case EVERY_CERTAIN_DAYS_TASK:
                    if(controller.getCurrentTask().getTaskType() == controller.getSelectedTask()) {
                        picker.setChronology(((EveryCertainDaysTask) controller.getCurrentTask()).getNextTaskOccurrence().getChronology());
                        spinnerEvery.getValueFactory().setValue(((EveryCertainDaysTask) controller.getCurrentTask()).getAmountOfDaysBreak());
                    }
                    taskTypeDialog.setHeaderText("Choose a date of first occurrence, then select the number of days before new repetition");
                    box.getChildren().addAll(picker, spinnerEvery);
                    break;
                case WEEKLY_TASK:
                    if(controller.getCurrentTask().getTaskType() == controller.getSelectedTask()) {
                        monday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[0]);
                        tuesday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[1]);
                        wednesday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[2]);
                        thursday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[3]);
                        friday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[4]);
                        saturday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[5]);
                        sunday.setSelected(((WeeklyTask) controller.getCurrentTask()).getWeek()[6]);
                    }
                    taskTypeDialog.setHeaderText("Choose days of week in which the task should occur");
                    box.getChildren().addAll(monday, tuesday, wednesday, thursday, friday, saturday, sunday);
                    break;
                case MONTHLY_TASK:
                    if(controller.getCurrentTask().getTaskType() == controller.getSelectedTask()) {
                        spinnerDayOfMonth.getValueFactory().setValue(((MonthlyTask) controller.getCurrentTask()).getDayOfMonth());
                    }
                    taskTypeDialog.setHeaderText("Select day of month");
                    box.getChildren().add(spinnerDayOfMonth);
                    break;
                case YEARLY_TASK:
                    if(controller.getCurrentTask().getTaskType() == controller.getSelectedTask()) {
                        spinnerYearlyMonth.getValueFactory().setValue(((YearlyTask) controller.getCurrentTask()).getTaskDate().getMonthValue());
                        spinnerYearlyDayOfMonth.getValueFactory().setValue(((YearlyTask) controller.getCurrentTask()).getTaskDate().getDayOfMonth());
                    }
                    taskTypeDialog.setHeaderText("Select month of year and its day");
                    box.getChildren().addAll(spinnerYearlyMonth, spinnerYearlyDayOfMonth);
                    break;
                case SPECIFIC_DATA_TASK:
                    if(controller.getCurrentTask().getTaskType() == controller.getSelectedTask()) {
                        specificPicker.setChronology(((SpecificDataTask) controller.getCurrentTask()).getTaskDate().getChronology());
                    }
                    taskTypeDialog.setHeaderText("Select data");
                    box.getChildren().add(specificPicker);
                    break;
                default:
                    break;
            }

            taskTypeDialog.getDialogPane().setContent(box);

            taskTypeDialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
            taskTypeDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
            Task newTask = null;

            if (controller.getSelectedTask() == TaskTypes.DAILY_TASK) {
                newTask = new DailyTask(controller.getTaskName(), controller.getAdditionalInfo(), controller.getStartingTime(), controller.getEndingTime());
            } else {
                Optional<ButtonType> resultType = taskTypeDialog.showAndWait();

                if (resultType.isPresent() && resultType.get() == ButtonType.OK) {

                    switch (controller.getSelectedTask()) {
                        case DAILY_TASK:
                            break;
                        case EVERY_CERTAIN_DAYS_TASK:
                            newTask = new EveryCertainDaysTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(), Integer.parseInt(spinnerEvery.getEditor().getText()), picker.getValue());
                            break;
                        case WEEKLY_TASK:
                            boolean[] week = {false, false, false, false, false, false, false};
                            week[0] = monday.isSelected();
                            week[1] = tuesday.isSelected();
                            week[2] = wednesday.isSelected();
                            week[3] = thursday.isSelected();
                            week[4] = friday.isSelected();
                            week[5] = saturday.isSelected();
                            week[6] = sunday.isSelected();
                            newTask = new WeeklyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(), week);
                            break;
                        case MONTHLY_TASK:
                            newTask = new MonthlyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    Integer.parseInt(spinnerDayOfMonth.getEditor().getText()));

                            break;
                        case YEARLY_TASK:
                            newTask = new YearlyTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    LocalDate.of(2000, Integer.parseInt(spinnerYearlyMonth.getEditor().getText()),
                                            Integer.parseInt(spinnerYearlyDayOfMonth.getEditor().getText())));
                            break;
                        case SPECIFIC_DATA_TASK:
                            newTask = new SpecificDataTask(controller.getTaskName(), controller.getAdditionalInfo(),
                                    controller.getStartingTime(), controller.getEndingTime(),
                                    specificPicker.getValue());
                            break;
                        default:
                            break;
                    }
                }
            }
            if (newTask != null) {
                TaskList.getInstance().addTask(newTask);
                TaskList.getInstance().deleteTask(controller.getCurrentTask());
            }
        }
    }

    @FXML
    public void handleShowTaskDetails() {
        Dialog<ButtonType> showDetails = new Dialog<>();
        showDetails.initOwner(taskManagerDialogPane.getScene().getWindow());
        showDetails.setTitle("Task details");
        showDetails.setHeaderText("Details of the selected task");

        Task task = allTaskListView.getSelectionModel().getSelectedItem();
        if (task != null) {

            TextField taskNameField = new TextField();
            taskNameField.setText(task.getTaskName());

            TextArea additionalInfoArea = new TextArea();
            additionalInfoArea.setText(task.getAdditionalInfo());

            Label labelStart = new Label();
            labelStart.setText("Start of the task at: " + task.getStartingTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            Label labelEnd = new Label();
            labelEnd.setText("End of the task at: " + task.getEndingTime().format(DateTimeFormatter.ofPattern("HH:mm")));

            TextArea taskTypeInfo = new TextArea();
            taskTypeInfo.setText(task.taskTypeInfo());

            VBox box = new VBox(10, taskNameField, additionalInfoArea, labelStart, labelEnd, taskTypeInfo);

            showDetails.getDialogPane().setContent(box);

            showDetails.getDialogPane().getButtonTypes().add(ButtonType.OK);
            Optional<ButtonType> result = showDetails.showAndWait();
        }
    }

    @FXML
    public void removeTask() {
        Dialog<ButtonType> showDetails = new Dialog<>(); //creation of new dialog
        showDetails.initOwner(taskManagerDialogPane.getScene().getWindow());
        showDetails.setTitle("Remove task");
        Task task = allTaskListView.getSelectionModel().getSelectedItem(); //getting a specific task from a task list
        if(task != null) {
            showDetails.setHeaderText("Are you sure, you want to delete the following task: " + task.getTaskName());

            DialogPane pane = new DialogPane();
            showDetails.getDialogPane().setContent(pane);

            showDetails.getDialogPane().getButtonTypes().add(ButtonType.OK); //adding predefined buttons
            showDetails.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);

            Optional<ButtonType> result = showDetails.showAndWait(); //dialog show

            if (result.isPresent() && result.get() == ButtonType.OK) {
                TaskList.getInstance().deleteTask(task); //remove only if user pressed OK button
            }
        }
    }






}
