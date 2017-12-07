package io.ermdev.ees.controller;

import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import io.ermdev.ees.dao.CourseDao;
import io.ermdev.ees.dao.SubjectDao;
import io.ermdev.ees.helper.DbFactory;
import io.ermdev.ees.model.Student;
import io.ermdev.ees.util.ResourceHelper;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AdvisingFormController implements Initializable {

    @FXML
    private JFXTreeTableView<io.ermdev.ees.model.recursive.Subject> tblSubject;

    @FXML
    private Label lbSN;

    @FXML
    private Label lbCourse;

    @FXML
    private Label lbSemester;

    @FXML
    private Label lbFN;

    @FXML
    private Label lbUnit;

    @FXML
    private Label lbDate;

    @FXML
    private ImageView imgCCS;

    @FXML
    private ImageView imgDHVTSU;

    private final ObservableList<io.ermdev.ees.model.recursive.Subject> SUBJECT_LIST = FXCollections.observableArrayList();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initTable();

        Image logo = new Image(ResourceHelper.resourceWithBasePath("image/ccslogo.png").toString());
        Image logo2 = new Image(ResourceHelper.resourceWithBasePath("image/logodhvtsu.jpg").toString());
        imgCCS.setImage(logo);
        imgDHVTSU.setImage(logo2);
    }

    private void initTable() {
        for (io.ermdev.ees.model.Subject s : subjectDao.getSubjectList())
            SUBJECT_LIST.add(new io.ermdev.ees.model.recursive.Subject(s.getId(), s.getName(), s.getDesc(), s.getUnit()));

        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        Platform.runLater(() -> {
            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Long> idCol = new JFXTreeTableColumn<>("ID");
            idCol.setResizable(false);
            idCol.setPrefWidth(80);
            idCol.setCellValueFactory(param -> param.getValue().getValue().idProperty().asObject());
            idCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> nameCol = new JFXTreeTableColumn<>("Subject");
            nameCol.setResizable(false);
            nameCol.setPrefWidth(130);
            nameCol.setCellValueFactory(param -> param.getValue().getValue().nameProperty());
            nameCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, String> descCol = new JFXTreeTableColumn<>("Description");
            descCol.setResizable(false);
            descCol.setPrefWidth(210);
            descCol.setCellValueFactory(param -> param.getValue().getValue().descProperty());
            descCol.setSortable(false);

            JFXTreeTableColumn<io.ermdev.ees.model.recursive.Subject, Integer> unitCol = new JFXTreeTableColumn<>("Unit");
            unitCol.setResizable(false);
            unitCol.setPrefWidth(80);
            unitCol.setCellValueFactory(param -> param.getValue().getValue().unitProperty().asObject());
            unitCol.setSortable(false);

            tblSubject.getColumns().add(idCol);
            tblSubject.getColumns().add(nameCol);
            tblSubject.getColumns().add(descCol);
            tblSubject.getColumns().add(unitCol);

            tblSubject.getSelectionModel().select(0);

            tblSubject.setRoot(root);
            tblSubject.setShowRoot(false);
        });
    }

    public void listener(Student student, List<io.ermdev.ees.model.recursive.Subject> subjectList) {

        lbSN.setText(student.getStudentNumber() + "");
        lbCourse.setText(courseDao.getCourseById(student.getCourseId()).getName());
        lbFN.setText(String.format("%s %s. %s", student.getFirstName().toUpperCase(),
                student.getMiddleName().substring(0, 1).toUpperCase(),
                student.getLastName().toUpperCase()));
        int tUnit = 0;
        for (io.ermdev.ees.model.recursive.Subject s : subjectList)
            tUnit += s.getUnit();

        Calendar calendar = Calendar.getInstance();

        lbUnit.setText(tUnit + "");

        lbDate.setText(String.format(Locale.ENGLISH, "%d/%d/%d", calendar.get(Calendar.MONTH) + 1,
                calendar.get(Calendar.DAY_OF_MONTH), calendar.get(Calendar.YEAR)));

        SUBJECT_LIST.clear();

        SUBJECT_LIST.addAll(subjectList);

        TreeItem<io.ermdev.ees.model.recursive.Subject> root = new RecursiveTreeItem<>(SUBJECT_LIST,
                RecursiveTreeObject::getChildren);

        tblSubject.setRoot(root);
        tblSubject.setShowRoot(false);
    }


    @FXML
    protected void onClickPrint(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }
}