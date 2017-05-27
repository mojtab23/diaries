package io.github.mojtab23.diaries;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;

/**
 * Created by mojtab23 on 5/4/2017.
 */

@Component
public class LoginView extends GridPane {



    public LoginView() {

        setHgap(24);
        setVgap(24);
        setAlignment(Pos.CENTER);
        getStyleClass().add("dark-pane");

        add(buildTextField(), 1, 1);
        add(buildPasswordField(), 1, 2);
        add(buildLoginButton(), 1, 3);

    }

    @NotNull
    private JFXButton buildLoginButton() {
        final JFXButton login = new JFXButton("Login");
        login.setButtonType(JFXButton.ButtonType.RAISED);
        login.setRipplerFill(Color.DARKSLATEGREY);
//        login.setStyle(INPUT_CSS);
        login.setPrefHeight(36);
        login.setOnAction(event -> {
            setVisible(false);
        });
        return login;
    }

    private JFXTextField buildTextField() {
        JFXTextField field = new JFXTextField();

        field.setLabelFloat(true);
//        field.promptTextProperty().bind(field.heightProperty().asString());
        field.setPromptText("User Name");
        field.setPrefHeight(36);
        return field;
    }

    private JFXPasswordField buildPasswordField() {
        JFXPasswordField field = new JFXPasswordField();
        field.setLabelFloat(true);
        field.setPromptText("Password");
        return field;
    }

}
