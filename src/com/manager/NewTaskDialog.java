package com.manager;

import com.manager.tasks.TaskTypes;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import java.time.LocalTime;

public class NewTaskDialog {
    @FXML
    private TextField taskNameField;
    @FXML
    private TextArea additionalInfoArea;
    @FXML
    private Spinner spinnerStartHour;
    @FXML
    private Spinner spinnerStartMinute;
    @FXML
    private Spinner spinnerEndHour;
    @FXML
    private Spinner spinnerEndMinute;
    @FXML
    private ComboBox taskComboBox;

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
