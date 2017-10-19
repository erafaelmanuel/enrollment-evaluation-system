package io.erm.ees.stage;

import io.erm.ees.controller.EvaluationAddController;
import io.erm.ees.util.ResourceHelper;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class EvaluationAddStage extends Stage {

    private EvaluationAddController controller;
    private OnCloseListener listener;

    public EvaluationAddStage() {
        try {
            FXMLLoader loader = new FXMLLoader(ResourceHelper.resourceWithBasePath("fxml/evaluation_add.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root, 940, 600);
            initModality(Modality.APPLICATION_MODAL);
            setScene(scene);
            setResizable(false);

            controller = loader.getController();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void setListener(OnCloseListener listener) {
        this.listener = listener;
    }

    public EvaluationAddController getController() {
        return controller;
    }

    public void setClose() {
        if(listener != null)
            listener.onClose();
    }

    @FunctionalInterface
    public interface OnCloseListener {
        void onClose();
    }
}