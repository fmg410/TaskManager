package com.manager;

import com.manager.tasks.TaskList;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("mainMenu.fxml"));
        primaryStage.setTitle("Task planner");
        Scene mainScene = new Scene(root, 1000, 600);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        TaskList.getInstance().storeTasks();
    }

    @Override
    public void init() throws Exception {
        TaskList.getInstance().loadTasks();
    }
}
