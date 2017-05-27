package io.github.mojtab23.diaries.model.diary;

import com.jfoenix.controls.JFXListCell;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

/**
 * Created by mojtab23 on 5/13/2017.
 */
public class DiaryListCell extends JFXListCell<Diary> {


    @Override
    public void updateItem(Diary item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {

            HBox container = new HBox();
            container.setMouseTransparent(true);
            container.getChildren().add(new Label(item.getText()));
            container.getChildren().add(new Label(item.getTimestamp() + ""));
            setGraphic(container);

        }

    }
}
