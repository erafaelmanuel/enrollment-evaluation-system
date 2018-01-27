package io.ermdev.ees.legacy.controller;

import io.ermdev.ees.legacy.dao.UserDetailDao;
import io.ermdev.ees.legacy.dao.impl.UserDetailDaoImpl;
import io.ermdev.ees.legacy.model.UserDetail;
import io.ermdev.ees.legacy.stage.UserInputStage;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

public class UserInputController implements Initializable {

    private final UserDetailDao userDetailDao = new UserDetailDaoImpl();

    @FXML
    private JFXComboBox<String> cbType;

    @FXML
    private JFXTextField txUsername;

    @FXML
    private JFXPasswordField txPassword;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        cbType.getItems().add("TEACHER");
        cbType.getItems().add("DEAN");
        cbType.getSelectionModel().select(0);
    }

    @FXML
    protected void onClickSave(ActionEvent event) {
        if(!(txUsername.getText().trim().isEmpty() && txPassword.getText().trim().isEmpty()) &&
                (txPassword.getText().trim().matches("^[0-9a-zA-Z]+$")
                        && txUsername.getText().trim().matches("^[0-9a-zA-Z]+$"))) {

            for(UserDetail user : userDetailDao.getUserDetailList()) {
                if(user.getUsername().trim().equals(txUsername.getText().trim())) {
                    new Thread(()->
                            JOptionPane.showMessageDialog(null, "Username is already used."))
                    .start();
                    return;
                }
            }
            UserDetail userDetail = new UserDetail();
            userDetail.setUsername(txUsername.getText().trim());
            userDetail.setPassword(txPassword.getText().trim());
            userDetail.setUserType(cbType.getSelectionModel().getSelectedItem()
                    .equalsIgnoreCase("TEACHER") ? "user/teacher":"user/dean");
            userDetail.setActivated(true);
            userDetail.setRegistrationDate(new Date().toString());
            userDetailDao.addUserDetail(userDetail);

            UserInputStage userInputStage = (UserInputStage) ((Node) event.getSource()).getScene().getWindow();
            userInputStage.callBack();
        } else {
            new Thread(()-> JOptionPane.showMessageDialog(null, "Invalid input"))
                    .start();
        }
    }

    @FXML
    protected void onClickCancel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }


}