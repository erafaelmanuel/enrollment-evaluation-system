package io.ermdev.ees.legacy.controller;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import io.ermdev.ees.legacy.dao.*;
import io.ermdev.ees.legacy.dao.impl.DirtyDaoImpl;
import io.ermdev.ees.legacy.dao.impl.SectionDaoImpl;
import io.ermdev.ees.legacy.dao.impl.UserDetailDaoImpl;
import io.ermdev.ees.legacy.helper.DbFactory;
import io.ermdev.ees.legacy.model.Course;
import io.ermdev.ees.legacy.model.Student;
import io.ermdev.ees.legacy.model.Subject;
import io.ermdev.ees.legacy.model.UserDetail;
import io.ermdev.ees.legacy.model.v2.AcademicYear;
import io.ermdev.ees.legacy.stage.*;
import io.ermdev.ees.legacy.stage.window.AboutWindow;
import io.ermdev.ees.legacy.stage.window.PopOnExitWindow;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class AdminWindowController implements Initializable, StudentInputStage.OnItemAddLister,
        CurriculumStage.OnItemAddLister, SubjectInputStage.OnItemAddLister, UserInputStage.OnItemAddLister,
        AcademicYearInputStage.OnItemAddLister {

    @FXML
    private Button bnAdd;

    @FXML
    private MenuBar menuBar;

    @FXML
    private TableView<Object> tblData;

    @FXML
    private Label lbTitle;

    @FXML
    private MenuItem miInputGrade;

    @FXML
    private MenuItem miSpecial;

    @FXML
    private MenuItem miUpdate;

    @FXML
    private MenuItem miDelete;

    @FXML
    private MenuItem miActivate;

    @FXML
    private MenuItem miDeactivate;

    @FXML
    private MenuItem miOpen;

    @FXML
    private MenuItem miClose;

    @FXML
    private JFXButton bnSearch;

    @FXML
    private JFXTextField txSearch;

    @FXML
    private TableView<Object> tblSubData;

    private static final ObservableList<Object> OBSERVABLE_LIST = FXCollections.observableArrayList();

    private static final int NO_TABLE = 0;
    private static final int TABLE_STUDENT = 1;
    private static final int TABLE_SUBJECT = 2;
    private static final int TABLE_COURSE = 3;
    private static final int TABLE_USER = 4;
    private static final int TABLE_ACADEMIC_YEAR = 5;


    private int mCurrent = TABLE_STUDENT;

    private final CourseStage courseStage = new CourseStage();
    private final StudentInputStage studentInputStage = new StudentInputStage();
    private final SubjectInputStage subjectInputStage = new SubjectInputStage();
    private final SpecialCurriculumStage specialCurriculumStage = new SpecialCurriculumStage();
    private final UserInputStage userInputStage = new UserInputStage();
    private final AcademicYearInputStage academicYearInputStage = new AcademicYearInputStage();

    private final List<Student> STUDENT_LIST = new ArrayList<>();
    private final List<Course> COURSE_LIST = new ArrayList<>();
    private final List<Subject> SUBJECT_LIST = new ArrayList<>();
    private final List<Subject> SUBJECT_PREREQUISITE_LIST = new ArrayList<>();
    private final List<AcademicYear> ACADEMIC_YEAR_LIST = new ArrayList<>();
    private final List<UserDetail> USER_LIST = new ArrayList<>();

    private final CourseDao courseDao = DbFactory.courseFactory();
    private final AcademicYearDao academicYearDao = DbFactory.academicYearFactory();
    private final StudentDao studentDao = DbFactory.studentFactory();
    private final SubjectDao subjectDao = DbFactory.subjectFactory();
    private final DirtyDao dirtyDao = new DirtyDaoImpl();
    private final UserDetailDao userDetailDao = new UserDetailDaoImpl();
    private final CreditSubjectDao creditSubjectDao = DbFactory.creditSubjectFactory();
    private final SectionDao sectionDao = new SectionDaoImpl();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tblData.setItems(OBSERVABLE_LIST);
        tblData.setStyle("-fx-table-cell-border-color: transparent;");

        clear();
        loadStudent();

        lbTitle.setText("Student List");

        miSpecial.setVisible(false);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        miOpen.setVisible(false);
        miClose.setVisible(false);

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setPlaceholder(new Label("No Subject Prerequisite"));
        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        studentInputStage.setOnItemAddLister(this);
        courseStage.getCurriculumStage().setOnItemAddLister(this);
        subjectInputStage.setOnItemAddLister(this);
        userInputStage.setOnItemAddLister(this);
        academicYearInputStage.setOnItemAddLister(this);
    }

    @FXML
    protected void onClickSearch() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
            case TABLE_ACADEMIC_YEAR:
                loadAcademicYear();
                break;
        }
    }

    @FXML
    protected void onActionSearch() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
            case TABLE_ACADEMIC_YEAR:
                loadAcademicYear();
                break;
        }
    }

    @FXML
    protected void onSearchPressed() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
            case TABLE_ACADEMIC_YEAR:
                loadAcademicYear();
                break;
        }
    }

    @FXML
    protected void onSearchReleased() {
        clear();
        switch (mCurrent) {
            case TABLE_STUDENT:
                loadStudent();
                break;
            case TABLE_COURSE:
                loadCourse();
                break;
            case TABLE_SUBJECT:
                loadSubject(subjectDao.getSubjectListBySearch(txSearch.getText().trim()));
                break;
            case TABLE_USER:
                loadUser();
                break;
            case TABLE_ACADEMIC_YEAR:
                loadAcademicYear();
                break;
        }
    }

    @FXML
    protected void onClickRefresh() {
        clear();
        new Thread(() -> {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Platform.runLater(() -> {
                switch (mCurrent) {
                    case TABLE_STUDENT:
                        loadStudent();
                        break;
                    case TABLE_COURSE:
                        loadCourse();
                        break;
                    case TABLE_SUBJECT:
                        loadSubject();
                        break;
                    case TABLE_USER:
                        loadUser();
                        break;
                    case TABLE_ACADEMIC_YEAR:
                        loadAcademicYear();
                        break;
                }
            });
        }).start();
    }

    @FXML
    protected void onClickItem() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        switch (mCurrent) {
            case TABLE_STUDENT:
                break;
            case TABLE_COURSE:
                break;
            case TABLE_SUBJECT:
                if(index > -1) {
                    subClear();
                    loadSubjectPrerequisite(dirtyDao.getPrerequisiteBySujectId(SUBJECT_LIST.get(index).getId()));
                }
                break;
            case TABLE_USER:
                break;
            case TABLE_ACADEMIC_YEAR:
                break;
        }
    }

    @FXML
    protected void onPressedItem() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        switch (mCurrent) {
            case TABLE_STUDENT:
                break;
            case TABLE_COURSE:
                break;
            case TABLE_SUBJECT:
                if(index > -1) {
                    subClear();
                    loadSubjectPrerequisite(dirtyDao.getPrerequisiteBySujectId(SUBJECT_LIST.get(index).getId()));
                }
                break;
            case TABLE_USER:
                break;
            case TABLE_ACADEMIC_YEAR:
                break;
        }
    }

    @FXML
    protected void onClickExit() {
        Stage stage = (Stage) menuBar.getScene().getWindow();
        if (PopOnExitWindow.display("Are you sure you want to exit?"))
            stage.close();
    }

    @FXML
    protected void onClickAdd() {
        switch (mCurrent) {
            case TABLE_STUDENT:
                studentInputStage.showAndWait();
                break;
            case TABLE_COURSE:
                courseStage.showAndWait();
                break;
            case TABLE_SUBJECT:
                subjectInputStage.showAndWait();
                break;
            case TABLE_USER:
                userInputStage.showAndWait();
                break;
            case TABLE_ACADEMIC_YEAR:
                new Thread(() -> {
                    Platform.runLater(academicYearInputStage::showAndWait);
                    academicYearInputStage.getController().listener();
                }).start();
                break;
            default:
                courseStage.showAndWait();
                break;
        }
    }

    @FXML
    protected void onClickActivate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            UserDetail userDetail = USER_LIST.get(index);
            if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                userDetail.setActivated(true);
                userDetailDao.updateUserDetailById(userDetail.getId(), userDetail);

                clear();
                loadUser();
            } else {
                new Thread(()-> JOptionPane.showMessageDialog(null,
                        "You don't have permission to update/delete this account"))
                        .start();
            }
        }
    }

    @FXML
    protected void onClickDeactivate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            UserDetail userDetail = USER_LIST.get(index);
            if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                userDetail.setActivated(false);
                userDetailDao.updateUserDetailById(userDetail.getId(), userDetail);

                clear();
                loadUser();
            } else {
                new Thread(()-> JOptionPane.showMessageDialog(null,
                        "You don't have permission to update/delete this account"))
                        .start();
            }
        }
    }

    @FXML
    protected void onClickSpecial() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Platform.runLater(() -> specialCurriculumStage.showAndWait());
            specialCurriculumStage.getController().courseProperty(COURSE_LIST.get(index));
        }

    }

    @FXML
    protected void onClickDelete() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            switch (mCurrent) {
                case TABLE_STUDENT:
                    studentDao.deleteStudentById(STUDENT_LIST.get(index).getId());
                    clear();
                    loadStudent();
                    break;
                case TABLE_COURSE:
                    courseDao.deleteCourseById(COURSE_LIST.get(index).getId());
                    clear();
                    loadCourse();
                    break;
                case TABLE_SUBJECT:
                    subjectDao.deleteSubjectById(SUBJECT_LIST.get(index).getId());
                    clear();
                    loadSubject();
                    break;
                case TABLE_USER:
                    UserDetail userDetail = USER_LIST.get(index);
                    if(!userDetail.getUserType().getType().equalsIgnoreCase("admin/admin")) {
                        userDetail.setActivated(true);
                        userDetailDao.deleteUserDetailById(userDetail.getId());

                        clear();
                        loadUser();
                    } else {
                        new Thread(()-> JOptionPane.showMessageDialog(null,
                                "You don't have permission to update/delete this account"))
                                .start();
                    }
                    break;
                case TABLE_ACADEMIC_YEAR:
                    AcademicYear tempAcademicYear = ACADEMIC_YEAR_LIST.get(index);
                    academicYearDao.deleteAcademicYear(tempAcademicYear.getCode(), tempAcademicYear.getCourseId(),
                            tempAcademicYear.getSemester());
                    clear();
                    loadAcademicYear();
                    break;
            }
        }
    }

    @FXML
    protected void onClickUpdate() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            switch (mCurrent) {
                case TABLE_STUDENT:
                    Student student = STUDENT_LIST.get(index);
                    Platform.runLater(() -> studentInputStage.showAndWait());
                    studentInputStage.getController().listen(student);
                    break;
                case TABLE_SUBJECT:
                    new Thread(() -> {
                        Subject subject = SUBJECT_LIST.get(index);
                        Platform.runLater(() -> subjectInputStage.showAndWait());
                        subjectInputStage.getController().listen(subject);
                    }).start();
                    break;
                case TABLE_COURSE:
                    Course course = COURSE_LIST.get(index);
                    Platform.runLater(() -> courseStage.showAndWait());
                    courseStage.getController().listen(course);
                    break;
            }
        }
    }

    @FXML
    protected void onClickOpen() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            AcademicYear academicYear = ACADEMIC_YEAR_LIST.get(index);

            if(academicYear.isStatus()) return;

            academicYearDao.statusClose(academicYear.getCourseId());
            academicYearDao.statusOpen(academicYear.getCode(), academicYear.getCourseId(), academicYear.getSemester());
            creditSubjectDao.setSubject(academicYear.getCourseId());
            clear();
            loadAcademicYear();
        }
    }

    @FXML
    protected void onClickClose() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if(index > -1) {
            AcademicYear academicYear = ACADEMIC_YEAR_LIST.get(index);

            if(!academicYear.isStatus()) return;

            academicYearDao.statusClose(academicYear.getCode(), academicYear.getCourseId(), academicYear.getSemester());
            creditSubjectDao.setSubject(academicYear.getCourseId());
            clear();
            loadAcademicYear();
        }
    }
    @FXML
    protected void onClickInputGrade() {
        final int index = tblData.getSelectionModel().getSelectedIndex();
        if (index > -1) {
            Student student = STUDENT_LIST.get(index);
            StudentGradeInputStage studentGradeInputStage = new StudentGradeInputStage();
            Platform.runLater(() -> studentGradeInputStage.showAndWait());
            studentGradeInputStage.getController().listening(student);
        }
    }

    @FXML
    protected void onClickAcademicYear() {
        mCurrent = TABLE_ACADEMIC_YEAR;

        clear();
        loadAcademicYear();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(false);

        miUpdate.setVisible(false);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Academic Year List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        miOpen.setVisible(true);
        miClose.setVisible(true);
    }

    @FXML
    protected void onClickSubject() {
        mCurrent = TABLE_SUBJECT;

        clear();
        loadSubject();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(false);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Subject List");

        bnSearch.setVisible(true);
        txSearch.setVisible(true);

        tblSubData.setVisible(true);
        tblSubData.setPrefWidth(400);

        loadSubjectPrerequisite(new ArrayList<>());

        miOpen.setVisible(false);
        miClose.setVisible(false);
    }

    @FXML
    protected void onClickStudent() {
        mCurrent = TABLE_STUDENT;

        clear();
        loadStudent();

        miInputGrade.setVisible(true);
        miSpecial.setVisible(false);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Student List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        miOpen.setVisible(false);
        miClose.setVisible(false);
    }

    @FXML
    protected void onClickCourse() {
        mCurrent = TABLE_COURSE;

        clear();
        loadCourse();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(true);

        miUpdate.setVisible(true);
        miDelete.setVisible(true);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        lbTitle.setText("Course List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        miOpen.setVisible(false);
        miClose.setVisible(false);
    }

    @FXML
    protected void onClickUser() {
        mCurrent = TABLE_USER;

        clear();
        loadUser();

        miInputGrade.setVisible(false);
        miSpecial.setVisible(false);

        miUpdate.setVisible(false);
        miDelete.setVisible(true);

        miActivate.setVisible(true);
        miDeactivate.setVisible(true);

        lbTitle.setText("User List");

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        miOpen.setVisible(false);
        miClose.setVisible(false);
    }
    @FXML
    protected void onClickSignout() {
        AdminStage stage = (AdminStage) menuBar.getScene().getWindow();
        stage.close();
        stage.callBack();
    }

    private void loadStudent() {
        TableColumn<Object, String> stStudentNumber = new TableColumn<>("Student Number");
        stStudentNumber.setCellValueFactory(new PropertyValueFactory<>("studentNumber"));
        stStudentNumber.setPrefWidth(150);
        stStudentNumber.setSortable(false);
        stStudentNumber.setResizable(false);

        TableColumn<Object, String> stFirstName = new TableColumn<>("FirstName");
        stFirstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        stFirstName.setPrefWidth(175);
        stFirstName.setSortable(false);
        stFirstName.setResizable(false);

        TableColumn<Object, String> stLastName = new TableColumn<>("LastName");
        stLastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        stLastName.setPrefWidth(175);
        stLastName.setSortable(false);
        stLastName.setResizable(false);

        TableColumn<Object, String> stMiddleName = new TableColumn<>("MiddleName");
        stMiddleName.setCellValueFactory(new PropertyValueFactory<>("middleName"));
        stMiddleName.setPrefWidth(175);
        stMiddleName.setSortable(false);
        stMiddleName.setResizable(false);

        TableColumn<Object, String> stAge = new TableColumn<>("Age");
        stAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        stAge.setPrefWidth(120);
        stAge.setSortable(false);
        stAge.setResizable(false);

        TableColumn<Object, String> stGender = new TableColumn<>("Gender");
        stGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        stGender.setPrefWidth(120);
        stGender.setSortable(false);
        stGender.setResizable(false);

        TableColumn<Object, String> stContact = new TableColumn<>("Contact");
        stContact.setCellValueFactory(new PropertyValueFactory<>("displayContactNumber"));
        stContact.setPrefWidth(125);
        stContact.setSortable(false);
        stContact.setResizable(false);

        TableColumn<Object, String> stCourse = new TableColumn<>("Course");
        stCourse.setCellValueFactory(new PropertyValueFactory<>("displayCourse"));
        stCourse.setPrefWidth(125);
        stCourse.setSortable(false);
        stCourse.setResizable(false);

        TableColumn<Object, String> stSection = new TableColumn<>("Year / Section");
        stSection.setCellValueFactory(new PropertyValueFactory<>("displaySection"));
        stSection.setPrefWidth(125);
        stSection.setSortable(false);
        stSection.setResizable(false);

        tblData.getColumns().add(stStudentNumber);
        tblData.getColumns().add(stFirstName);
        tblData.getColumns().add(stLastName);
        tblData.getColumns().add(stMiddleName);
        tblData.getColumns().add(stAge);
        tblData.getColumns().add(stGender);
        tblData.getColumns().add(stContact);
        tblData.getColumns().add(stCourse);
        tblData.getColumns().add(stSection);

        STUDENT_LIST.clear();
        for (Student student : studentDao.getStudentList()) {
            student.setCourseDao(courseDao);
            student.setSectionDao(sectionDao);
            tblData.getItems().add(student);
            STUDENT_LIST.add(student);
        }
    }

    private void loadAcademicYear() {
        TableColumn<Object, String> ayId = new TableColumn<>("Id");
        ayId.setCellValueFactory(new PropertyValueFactory<>("id"));
        ayId.setPrefWidth(180);
        ayId.setSortable(false);
        ayId.setResizable(false);

        TableColumn<Object, String> ayCode = new TableColumn<>("Code");
        ayCode.setCellValueFactory(new PropertyValueFactory<>("code"));
        ayCode.setPrefWidth(180);
        ayCode.setSortable(false);
        ayCode.setResizable(false);

        TableColumn<Object, String> ayYear = new TableColumn<>("Year");
        ayYear.setCellValueFactory(new PropertyValueFactory<>("name"));
        ayYear.setPrefWidth(180);
        ayYear.setSortable(false);
        ayYear.setResizable(false);

        TableColumn<Object, String> aySem = new TableColumn<>("Semester");
        aySem.setCellValueFactory(new PropertyValueFactory<>("semester"));
        aySem.setPrefWidth(180);
        aySem.setSortable(false);
        aySem.setResizable(false);

        TableColumn<Object, String> ayStatus = new TableColumn<>("Status");
        ayStatus.setCellValueFactory(new PropertyValueFactory<>("displayStatus"));
        ayStatus.setPrefWidth(180);
        ayStatus.setSortable(false);
        ayStatus.setResizable(false);

        TableColumn<Object, String> ayCourse = new TableColumn<>("Course");
        ayCourse.setCellValueFactory(new PropertyValueFactory<>("displayCourse"));
        ayCourse.setPrefWidth(180);
        ayCourse.setSortable(false);
        ayCourse.setResizable(false);

        TableColumn<Object, String> ayStudents = new TableColumn<>("Total Student");
        ayStudents.setCellValueFactory(new PropertyValueFactory<>("students"));
        ayStudents.setPrefWidth(120);
        ayStudents.setSortable(false);
        ayStudents.setResizable(false);

        tblData.getColumns().add(ayId);
        tblData.getColumns().add(ayCode);
        tblData.getColumns().add(ayYear);
        tblData.getColumns().add(aySem);
        tblData.getColumns().add(ayStatus);
        tblData.getColumns().add(ayCourse);
        tblData.getColumns().add(ayStudents);

        ACADEMIC_YEAR_LIST.clear();
        for (AcademicYear academicYear : academicYearDao.getAcademicYearList()) {
            academicYear.getDisplayStatus();
            academicYear.getDisplayCourse();
            tblData.getItems().add(academicYear);
            ACADEMIC_YEAR_LIST.add(academicYear);
        }
    }


    private void loadSubject() {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(200);
        suId.setSortable(false);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(200);
        suName.setSortable(false);

        TableColumn<Object, String> suDesc = new TableColumn<>("Description");
        suDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        suDesc.setPrefWidth(200);
        suDesc.setSortable(false);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(200);
        suUnit.setSortable(false);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        SUBJECT_LIST.clear();
        for (Subject subject : subjectDao.getSubjectList()) {
            tblData.getItems().add(subject);
            SUBJECT_LIST.add(subject);
        }
    }

    private void loadSubject(List<Subject> subjectList) {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(200);
        suId.setSortable(false);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(200);
        suName.setSortable(false);

        TableColumn<Object, String> suDesc = new TableColumn<>("Description");
        suDesc.setCellValueFactory(new PropertyValueFactory<>("desc"));
        suDesc.setPrefWidth(200);
        suDesc.setSortable(false);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(200);
        suUnit.setSortable(false);

        tblData.getColumns().add(suId);
        tblData.getColumns().add(suName);
        tblData.getColumns().add(suDesc);
        tblData.getColumns().add(suUnit);

        SUBJECT_LIST.clear();
        for (Subject subject : subjectList) {
            tblData.getItems().add(subject);
            SUBJECT_LIST.add(subject);
        }
    }

    private void loadSubjectPrerequisite(List<Subject> subjectList) {
        TableColumn<Object, String> suId = new TableColumn<>("Id");
        suId.setCellValueFactory(new PropertyValueFactory<>("id"));
        suId.setPrefWidth(100);
        suId.setResizable(false);
        suId.setSortable(false);

        TableColumn<Object, String> suName = new TableColumn<>("Name");
        suName.setCellValueFactory(new PropertyValueFactory<>("name"));
        suName.setPrefWidth(190);
        suName.setResizable(false);
        suName.setSortable(false);

        TableColumn<Object, String> suUnit = new TableColumn<>("Unit");
        suUnit.setCellValueFactory(new PropertyValueFactory<>("unitDisplay"));
        suUnit.setPrefWidth(80);
        suUnit.setResizable(false);
        suUnit.setSortable(false);

        tblSubData.getColumns().add(suId);
        tblSubData.getColumns().add(suName);
        tblSubData.getColumns().add(suUnit);

        SUBJECT_PREREQUISITE_LIST.clear();
        for (Subject subject : subjectList) {
            tblSubData.getItems().add(subject);
            SUBJECT_PREREQUISITE_LIST.add(subject);
        }
    }

    private void loadCourse() {
        TableColumn<Object, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        idCol.setPrefWidth(200);
        idCol.setSortable(false);

        TableColumn<Object, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(200);
        nameCol.setSortable(false);

        TableColumn<Object, String> descCol = new TableColumn<>("Description");
        descCol.setCellValueFactory(new PropertyValueFactory<>("desc"));
        descCol.setPrefWidth(400);
        descCol.setSortable(false);

        TableColumn<Object, String> tYearCol = new TableColumn<>("Number of Year");
        tYearCol.setCellValueFactory(new PropertyValueFactory<>("totalYear"));
        tYearCol.setPrefWidth(200);
        tYearCol.setSortable(false);

        TableColumn<Object, String> tSemCol = new TableColumn<>("Number of Semester");
        tSemCol.setCellValueFactory(new PropertyValueFactory<>("totalSemester"));
        tSemCol.setPrefWidth(200);
        tSemCol.setSortable(false);

        tblData.getColumns().add(idCol);
        tblData.getColumns().add(nameCol);
        tblData.getColumns().add(descCol);
        tblData.getColumns().add(tYearCol);
        tblData.getColumns().add(tSemCol);

        COURSE_LIST.clear();
        for (Course course : courseDao.getCourseList()) {
            tblData.getItems().add(course);
            COURSE_LIST.add(course);
        }
    }

    private void loadUser() {
        TableColumn<Object, Long> uId = new TableColumn<>("ID");
        uId.setCellValueFactory(new PropertyValueFactory<>("id"));
        uId.setPrefWidth(200);
        uId.setSortable(false);

        TableColumn<Object, String> uUser = new TableColumn<>("Username");
        uUser.setCellValueFactory(new PropertyValueFactory<>("username"));
        uUser.setPrefWidth(200);
        uUser.setSortable(false);

        TableColumn<Object, String> uPass = new TableColumn<>("Password");
        uPass.setCellValueFactory(new PropertyValueFactory<>("password"));
        uPass.setPrefWidth(200);
        uPass.setSortable(false);

        TableColumn<Object, String> uType = new TableColumn<>("Type");
        uType.setCellValueFactory(new PropertyValueFactory<>("userType"));
        uType.setPrefWidth(200);
        uType.setSortable(false);

        TableColumn<Object, String> uIA = new TableColumn<>("Activated");
        uIA.setCellValueFactory(new PropertyValueFactory<>("activated"));
        uIA.setPrefWidth(200);
        uIA.setSortable(false);

        TableColumn<Object, String> uDate = new TableColumn<>("Registration Date");
        uDate.setCellValueFactory(new PropertyValueFactory<>("registrationDate"));
        uDate.setPrefWidth(200);
        uDate.setSortable(false);

        tblData.getColumns().add(uId);
        tblData.getColumns().add(uUser);
        tblData.getColumns().add(uPass);
        tblData.getColumns().add(uType);
        tblData.getColumns().add(uIA);
        tblData.getColumns().add(uDate);

        USER_LIST.clear();
        for (UserDetail userDetail : userDetailDao.getUserDetailList()) {
            userDetail.setActivated(userDetail.isActivated() ? "YES":"NO");
            tblData.getItems().add(userDetail);
            USER_LIST.add(userDetail);
        }
    }

    private void clear() {
        tblData.getColumns().clear();
        tblData.getItems().clear();
    }

    private void subClear() {
        tblSubData.getColumns().clear();
        tblSubData.getItems().clear();
    }


    @Override
    public void onAddStudent() {
        clear();
        loadStudent();
    }

    @Override
    public void onAddCourse() {
        clear();
        loadCourse();
    }

    @Override
    public void onAddSubject() {
        clear();
        loadSubject();
    }

    @Override
    public void onAddUser() {
        clear();
        loadUser();
    }

    @Override
    public void onAddAcademicYear() {
        clear();
        loadAcademicYear();
    }

    public void listener() {
        clear();
        loadStudent();

        lbTitle.setText("Student List");

        miSpecial.setVisible(false);

        miActivate.setVisible(false);
        miDeactivate.setVisible(false);

        miOpen.setVisible(false);
        miClose.setVisible(false);

        bnSearch.setVisible(false);
        txSearch.setVisible(false);

        tblSubData.setPlaceholder(new Label("No Subject Prerequisite"));
        tblSubData.setVisible(false);
        tblSubData.setPrefWidth(0);

        studentInputStage.setOnItemAddLister(this);
        courseStage.getCurriculumStage().setOnItemAddLister(this);
        subjectInputStage.setOnItemAddLister(this);
        userInputStage.setOnItemAddLister(this);
        academicYearInputStage.setOnItemAddLister(this);
    }

    @FXML
    protected void onClickAbout() {
        AboutWindow.display();
    }
}