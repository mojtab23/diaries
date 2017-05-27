package io.github.mojtab23.diaries;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Created by mojtab23 on 5/4/2017.
 */

public class DiariesApp extends Application {

    private static final Logger LOGGER = LoggerFactory.getLogger(DiariesApp.class);


    private static AnnotationConfigApplicationContext context;
    private Scene scene;
    private String styleUrl = this.getClass().getResource("/style.css").toExternalForm();


    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void init() throws Exception {
//        System.out.println("in init...");
        context = new AnnotationConfigApplicationContext(SpringConfig.class);

        LOGGER.info("after init...");
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        LOGGER.info("App started...");
        StackPane root = new StackPane();
        scene = new Scene(root, 800, 600);
        reloadCSS();

        root.getChildren().add(context.getBean(DiariesView.class));
        root.getChildren().add(context.getBean(LoginView.class));


        primaryStage.setScene(scene);
        primaryStage.show();
    }

    @Override
    public void stop() throws Exception {
        context.close();
    }

    private void reloadCSS() {
        scene.getStylesheets().clear();
        scene.getStylesheets().add(styleUrl);

    }

}
