package io.erm.ees.stage;

import io.erm.ees.dao.conn.DBManager;
import io.erm.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Logger;

public class ConfigurationStage extends Stage {

    private Logger logger = Logger.getLogger(ConfigurationStage.class.getSimpleName());

    private DBManager dbManager;
    private boolean status;

    private OnFinishListener onFinishListener;

    public ConfigurationStage(DBManager dbManager) {
        this.dbManager = dbManager;
        try {
            URL url = ResourceHelper.resourceWithBasePath("fxml/config.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 365, 345);

            this.setTitle("UserLibrary configuration");
            this.setResizable(false);
            this.setScene(scene);
            this.setOnCloseRequest((e) -> {
                onFinishListener.onFinish(dbManager, status);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    public ConfigurationStage(DBManager dbManager, OnFinishListener onFinishListener) {
        this.dbManager = dbManager;
        this.onFinishListener = onFinishListener;
        try {
            URL url = ResourceHelper.resourceWithBasePath("fxml/config.fxml");
            Parent root = FXMLLoader.load(url);
            Scene scene = new Scene(root, 365, 345);

            this.setTitle("UserLibrary configuration");
            this.setResizable(false);
            this.setScene(scene);
            this.setOnCloseRequest((e) -> {
                onFinishListener.onFinish(dbManager, status);
            });
        } catch (IOException e) {
            logger.info("IOException");
        }
    }

    public DBManager getDbManager() {
        return dbManager;
    }

    public void setOnFinishListener(OnFinishListener onFinishListener) {
        this.onFinishListener = onFinishListener;
    }

    public void callBack(DBManager dbManager, boolean status) {
        this.dbManager = dbManager;
        this.status = status;
        Platform.runLater(() -> {
            this.close();
        });
        onFinishListener.onFinish(dbManager, status);
    }

    @FunctionalInterface
    public interface OnFinishListener {
        void onFinish(DBManager dbManager, boolean status);
    }
}