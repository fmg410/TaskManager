package com.manager;

import com.manager.tasks.Task;
import com.manager.tasks.TaskList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.function.Predicate;

public class WeeklyTaskCalendar {
    @FXML
    private ListView mondayTasks;
    @FXML
    private ListView tuesdayTasks;
    @FXML
    private ListView wednesdayTasks;
    @FXML
    private ListView thursdayTasks;
    @FXML
    private ListView fridayTasks;
    @FXML
    private ListView saturdayTasks;
    @FXML
    private ListView sundayTasks;

    private FilteredList<Task> filteredList;

    private SortedList<Task> sortedList;

    private Comparator comparator = new Comparator<Task>() {
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
    };

    public void initialize() {

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(0);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        mondayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(1);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        tuesdayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(2);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        wednesdayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(3);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        thursdayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(4);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        fridayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(5);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        saturdayTasks.setItems(sortedList);

        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), new Predicate<Task>() {
            @Override
            public boolean test(Task task) {
                LocalDate date = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1).plusDays(6);
                return task.isTaskThatDay(date);
            }
        });
        sortedList = new SortedList<Task>(filteredList, comparator);
        sundayTasks.setItems(sortedList);

        mondayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        tuesdayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        wednesdayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        thursdayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        fridayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        saturdayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

        sundayTasks.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
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

//        tableViewWeekly.getColumns().get(0).setCellFactory(new Callback<TableColumn<Task, ?>, TableCell<Task, ?>>() {
//            @Override
//            public TableCell<Task, ?> call(TableColumn<Task, ?> taskTableColumn) {
//                TableCell<Task, ?> cell = new TableCell<>(){
//                    @Override
//                    protected void updateItem(Object o, boolean b) {
//                        super.updateItem(o, b);
//                        if (b) {
//                            setText("ddd");
//                        } else {
//                        }
//                    }
//                };
//                return cell;
//            }
//        });


//        filteredList = new FilteredList<Task>(TaskList.getInstance().getTaskList(), wantTodaysItems);
//        SortedList<Task> sortedList = new SortedList<Task>(filteredList,
//                new Comparator<Task>() {
//                    @Override
//                    public int compare(Task o1, Task o2) {
//                        if (o1.isTaskInThatMoment(LocalDateTime.now()) && o2.isTaskInThatMoment(LocalDateTime.now())) {
//                            if (o1.getStartingTime().isBefore(o2.getStartingTime())) {
//                                return -1;
//                            } else if (o1.getStartingTime().isAfter(o2.getStartingTime())) {
//                                return 1;
//                            } else {
//                                return 0;
//                            }
//                        } else if (o1.isTaskInThatMoment(LocalDateTime.now())) {
//                            return -1;
//                        } else if (o2.isTaskInThatMoment(LocalDateTime.now())) {
//                            return 1;
//                        } else {
//                            return o1.getTaskName().compareTo(o2.getTaskName());
//                        }
//                    }
//                });
//
//        taskListView.setItems(sortedList);
//        taskListView.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//        taskListView.getSelectionModel().selectFirst();
//
//        taskListView.setCellFactory(new Callback<ListView<Task>, ListCell<Task>>() {
//            @Override
//            public ListCell<Task> call(ListView<Task> TaskListView) {
//                ListCell<Task> cell = new ListCell<Task>() {
//
//                    @Override
//                    protected void updateItem(Task task, boolean b) {
//                        super.updateItem(task, b);
//                        if (b) {
//                            setText(null);
//                        } else {
//                            setText(task.getTaskName());
//                            if (task.isTaskInThatMoment(LocalDateTime.now())) {
//                                setTextFill(Color.RED);
//                            }
//                        }
//                    }
//                };
//
//                return cell;
//            }
//        });
    }

}
