package com.erm.project.ees.stage;

import com.erm.project.ees.controller.StudentInputController;
import com.erm.project.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class StudentInputStage extends Stage {

    private OnItemAddLister onItemAddLister;
    private StudentInputController controller;

    public StudentInputStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/student_input.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 590, 390);

            controller = loader.getController();

            initModality(Modality.APPLICATION_MODAL);
            setResizable(false);
            setScene(scene);
            setOnCloseRequest(e -> controller.dispose());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setOnItemAddLister(OnItemAddLister onItemAddLister) {
        this.onItemAddLister = onItemAddLister;
    }

    public void callBack() {
        onItemAddLister.onAddStudent();
        Platform.runLater(() -> close());
    }

    public StudentInputController getController() {
        return controller;
    }

    @FunctionalInterface
    public interface OnItemAddLister {
        void onAddStudent();
    }
}
