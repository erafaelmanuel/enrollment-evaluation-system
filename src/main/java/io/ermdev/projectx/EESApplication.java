package io.ermdev.projectx;

import io.ermdev.projectx.data.repository.RoleRepository;
import io.ermdev.projectx.data.repository.UserRepository;
import io.ermdev.projectx.event.LoginEvent;
import io.ermdev.projectx.listener.LoginListener;
import io.ermdev.projectx.ui.login.LoginStage;
import io.ermdev.projectx.ui.teacher.TeacherStage;
import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories("io.ermdev.ees.data")
public class EESApplication extends Application implements LoginListener {

    private Stage primaryStage;
    private ApplicationContext applicationContext;

    @Bean
    public CommandLineRunner commandLineRunner(UserRepository userRepository, RoleRepository roleRepository) {
        return args -> {
        };
    }

    public static void main(String args[]) {
        launch();
    }

    @Override
    public void init() throws Exception {
        applicationContext = SpringApplication.run(EESApplication.class);
    }

    public void start(Stage primaryStage) throws Exception {
        this.primaryStage = primaryStage;
        LoginStage loginStage = new LoginStage(applicationContext, this);
        loginStage.show();
    }

    @Override
    public void onLoginSuccess(LoginEvent loginEvent) {
        TeacherStage teacherStage = new TeacherStage(applicationContext, this);
        teacherStage.show();
    }
}
