package com.manager;

import com.manager.tasks.*;
import javafx.application.Platform;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.concurrent.ScheduledService;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Callback;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.Optional;
import java.util.function.Predicate;

public class MainMenu {
    @FXML
    private BorderPane mainBorderPane;
    @FXML
    private ListView<Task> taskListView;

    private DateTimeFormatter formatter;

    private FilteredList<Task> filteredList;

    private Predicate<Task> wantTodaysItems;

    private SortedList<Task> sortedList;

    public void initialize() {
        formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

        wantTodaysItems = new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                if (task != null) {
                    return task.isTaskThatDay(LocalDate.now());
                }
                return false;
            }
        };
        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), wantTodaysItems);
        sortedList = new SortedList<Task>(filteredList,
                new Comparator<Task>() {
                    @Override
                    public int compare(Task o1, Task o2) {
                        if (o1.isTaskInThatMoment(LocalDateTime.now()) && o2.isTaskInThatMoment(LocalDateTime.now())) {
                            if (o1.getStartingTime().isBefore(o2.getStartingTime())) {
                                return -1;
                            } else if (o1.getStartingTime().isAfter(o2.getStartingTime())) {
                                return 1;
                            } else {
                                return 0;
                            }
                        } else if (o1.isTaskInThatMoment(LocalDateTime.now())) {
                            return -1;
                        } else if (o2.isTaskInThatMoment(LocalDateTime.now())) {
                            return 1;
                        } else {
                            return o1.getTaskName().compareTo(o2.getTaskName());
                        }
                    }
                });

        taskListView.setItems(sortedList);
        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        taskListView.getSelectionModel().selectFirst();

        taskListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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
                            if (task.getTaskState()) {
                                setFont(Font.font("Times New Roman bold", Font.getDefault().getSize() + 4));
                                setTextFill(Color.RED); // if happening, paint red
                            } else {
                                setFont(Font.font("Times New Roman", Font.getDefault().getSize() + 2));
                                setTextFill(Color.BLACK); // otherwise paint black, as if painted red earlier
                                                        //it must be set back to black after it finishes
                            }
                        }
                    }
                };

                return cell;
            }
        });


        ScheduledService<Boolean> updateList = new ScheduledService<Boolean>() {
            @Override
            protected javafx.concurrent.Task<Boolean> createTask() {
                return new javafx.concurrent.Task<Boolean>() {

                    @Override
                    protected Boolean call() throws Exception {
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                for(Task task : TaskList.getInstance().getTaskList()) {
                                    task.refreshTask();
                                    if(task.isNow.getValue() && !task.didPopUp) {
                                        task.didPopUp = true;
                                        showTaskPopup(task);
                                    }
                                    if(!task.isNow.getValue() && task.didPopUp) {
                                        task.didPopUp = false;
                                    }
                                }
                            }
                        });
                        return true;
                    }
                };
            }
        };

        updateList.setPeriod(Duration.seconds(5));
        updateList.start();
    }

    private void showTaskPopup(Task task) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Task happening!");
        dialog.setHeaderText("The following task is happening right now: " + task.getTaskName());
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.show();
    }
    @FXML
    public void openDateSelector() {
        Dialog<ButtonType> dateSelector = new Dialog<>();
        dateSelector.initOwner(mainBorderPane.getScene().getWindow());
        dateSelector.setTitle("Date selector");
        dateSelector.setHeaderText("Use this dialog to show tasks at specific date");

        DatePicker picker = new DatePicker();
        ListView list = new ListView();

        FilteredList<Task> filtered = new FilteredList(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                if (task != null) {
                    return task.isTaskThatDay(LocalDate.now());
                }
                return false;
            }
        });

        SortedList sorted = new SortedList(filtered, new Comparator<Task>() {
            @Override
            public int compare(Task o1, Task o2) {
                if (o1.getStartingTime().isBefore(o2.getStartingTime())) {
                    return -1;
                } else if (o1.getStartingTime().isAfter(o2.getStartingTime())) {
                    return 1;
                } else {
                    return o1.getTaskName().compareTo(o2.getTaskName());
                }
            }
        });

        list.setItems(sorted);
        picker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                filtered.setPredicate(new Predicate<Task>() {
                    @Override
                    public boolean test(Task task) {
                        if (task != null && picker.getValue() != null) {
                            return task.isTaskThatDay(picker.getValue());
                        }
                        return false;
                    }
                });
            }
        });

        list.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        GridPane pane = new GridPane();
        pane.addColumn(0, picker);
        pane.addColumn(1, list);
        dateSelector.getDialogPane().setContent(pane);

        dateSelector.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dateSelector.showAndWait();
    }

    @FXML
    public void openTaskManager() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initOwner(mainBorderPane.getScene().getWindow());
        dialog.setTitle("Task manager");
        dialog.setHeaderText("Use this dialog to manage your tasks");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("taskManager.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = dialog.showAndWait();
    }

    @FXML
    public void handleShowTaskDetails() {
        Dialog<ButtonType> showDetails = new Dialog<>();
        showDetails.initOwner(mainBorderPane.getScene().getWindow());
        showDetails.setTitle("Task details");
        showDetails.setHeaderText("Details of the selected task");

        Task task = taskListView.getSelectionModel().getSelectedItem();
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
    public void openWeeklyTaskCalendar() {
        Dialog<ButtonType> weeklyCalendar = new Dialog<>();
        weeklyCalendar.initOwner(mainBorderPane.getScene().getWindow());
        weeklyCalendar.setTitle("Weekly calendar");
        weeklyCalendar.setHeaderText("Your weekly tasks are as follows");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("weeklyTaskCalendar.fxml"));
        try {
            weeklyCalendar.getDialogPane().setContent(fxmlLoader.load());
        } catch (Exception e) {
            System.out.println("Couldn't load the dialog");
            e.printStackTrace();
            return;
        }

        weeklyCalendar.getDialogPane().getButtonTypes().add(ButtonType.OK);
        Optional<ButtonType> result = weeklyCalendar.showAndWait();
    }


    @FXML
    public void exitProgram() {
        Platform.exit();
    }

}
