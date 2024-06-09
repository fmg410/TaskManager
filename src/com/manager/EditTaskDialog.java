package com.manager;

import com.manager.tasks.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalTime;

public class EditTaskDialog {
    @FXML
    private TextField taskNameField;
    @FXML
    private TextArea additionalInfoArea;
    @FXML
    private Spinner<Integer> spinnerStartHour;
    @FXML
    private Spinner<Integer> spinnerStartMinute;
    @FXML
    private Spinner<Integer> spinnerEndHour;
    @FXML
    private Spinner<Integer> spinnerEndMinute;
    @FXML
    private ComboBox taskComboBox;

    private Task currentTask = null;

    public void setCurrentTask(Task task) {
        currentTask = task;
        taskNameField.setText(currentTask.getTaskName());
        additionalInfoArea.setText(currentTask.getAdditionalInfo());
        spinnerStartHour.getValueFactory().setValue(currentTask.getStartingTime().getHour());
        spinnerStartMinute.getValueFactory().setValue(currentTask.getStartingTime().getMinute());
        spinnerEndHour.getValueFactory().setValue(currentTask.getEndingTime().getHour());
        spinnerEndMinute.getValueFactory().setValue(currentTask.getEndingTime().getMinute());
        switch(task.getTaskType()) {
            case DAILY_TASK:
                taskComboBox.getSelectionModel().select(0);
                break;
            case EVERY_CERTAIN_DAYS_TASK:
                taskComboBox.getSelectionModel().select(1);
                break;
            case WEEKLY_TASK:
                taskComboBox.getSelectionModel().select(2);
                break;
            case MONTHLY_TASK:
                taskComboBox.getSelectionModel().select(3);
                break;
            case SPECIFIC_DATA_TASK:
                taskComboBox.getSelectionModel().select(4);
                break;
            case YEARLY_TASK:
                taskComboBox.getSelectionModel().select(5);
                break;
            default:
                taskComboBox.getSelectionModel().select(0);
                break;
        }
    }

    public Task getCurrentTask() {
        return this.currentTask;
    }

    public TaskTypes getSelectedTask() {
        switch (taskComboBox.getSelectionModel().getSelectedIndex()) {
            case 0:
                return TaskTypes.DAILY_TASK;
            case 1:
                return TaskTypes.EVERY_CERTAIN_DAYS_TASK;
            case 2:
                return TaskTypes.WEEKLY_TASK;
            case 3:
                return TaskTypes.MONTHLY_TASK;
            case 4:
                return TaskTypes.SPECIFIC_DATA_TASK;
            case 5:
                return TaskTypes.YEARLY_TASK;
            default:
                return TaskTypes.DAILY_TASK;
        }
    }

    public String getTaskName() {
        if(taskNameField.getText().trim().isEmpty()) {
            return "Empty";
        } else {
            return taskNameField.getText().replaceAll("\t", " ").replaceAll("\n", " ");
        }
    }

    public String getAdditionalInfo() {
        if(additionalInfoArea.getText().trim().isEmpty()) {
            return "Empty";
        } else {
            return additionalInfoArea.getText().replaceAll("\t", " ").replaceAll("\n", " ");
        }
    }

    public LocalTime getStartingTime() {
        LocalTime startingTime = LocalTime.of(Integer.parseInt(spinnerStartHour.getEditor().getText()),
                Integer.parseInt(spinnerStartMinute.getEditor().getText()));
        return startingTime;
    }

    public LocalTime getEndingTime() {
        LocalTime endingTime = LocalTime.of(Integer.parseInt(spinnerEndHour.getEditor().getText()),
                Integer.parseInt(spinnerEndMinute.getEditor().getText()));
        return endingTime;
    }

}
