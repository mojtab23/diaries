package io.github.mojtab23.diaries;

import com.ibm.icu.text.DateFormat;
import com.ibm.icu.util.Calendar;
import com.ibm.icu.util.ULocale;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTimePicker;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIcon;
import de.jensd.fx.glyphs.materialdesignicons.MaterialDesignIconView;
import io.github.mojtab23.diaries.model.diary.Diary;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.NodeOrientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Paint;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Date;
import java.time.*;
import java.util.List;

/**
 * Created by mojtab23 on 5/12/2017.
 */

@Component
public class DiariesView extends GridPane {
    private static final Logger LOGGER = LoggerFactory.getLogger(DiariesView.class);
    private final static String DEFAULT_COLOR = "#F1C360";
    private final ObservableList<Diary> diaries = FXCollections.observableArrayList();
    private final DiaryRepository repository;
    private final Diary newDiary = new Diary();
    private final BooleanProperty isEdited = new SimpleBooleanProperty(false);
    private final ChangeListener<LocalTime> timeListener = (observable, oldValue, newValue) -> {
        if (!isEdited.get()) {
            final LocalDate localDate = LocalDateTime.ofInstant(newDiary.getTimestamp(), ZoneId.systemDefault()).toLocalDate();
            newDiary.setTimestamp(LocalDateTime.of(localDate, newValue).atZone(ZoneId.systemDefault()).toInstant());
        }
    };
    private final ChangeListener<LocalDate> localDateListener = (observable, oldValue, newValue) -> {
        if (!isEdited.get()) {
            final LocalTime localTime = LocalDateTime.ofInstant(newDiary.getTimestamp(), ZoneId.systemDefault()).toLocalTime();
            newDiary.setTimestamp(LocalDateTime.of(newValue, localTime).atZone(ZoneId.systemDefault()).toInstant());
        }

    };
    private final ChangeListener<String> textListener = (observable, oldValue, newValue) -> {
        if (!isEdited.get()) {
            newDiary.setText(newValue);
        }
    };
    private JFXDatePicker datePicker;
    private JFXTimePicker timePicker;
    private JFXTextArea textArea;


    @Autowired
    public DiariesView(DiaryRepository repository) {
        super();
        this.repository = repository;
    }

    @PostConstruct
    public void init() {

        setPadding(new Insets(24));
        setHgap(24);
        setVgap(24);
        setAlignment(Pos.CENTER);
        getStyleClass().add("dark-pane");

        //todo externalize...
        final String resourcePath = "avatar.jpg";
        final ClassPathResource imageResource = new ClassPathResource(resourcePath);
        final InputStream imageInputStream;
        try {
            imageInputStream = imageResource.getInputStream();
            if (imageInputStream != null)
                setBackground(new Background(new BackgroundImage(new Image(imageInputStream),
                        BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER,
                        BackgroundSize.DEFAULT)));
        } catch (IOException ignored) {
            LOGGER.error("Error loading resource: {} ", resourcePath);
        }
        getLatestDiaries(-1, -1);
        add(buildDiariesView(), 0, 0);

        add(buildDiaryEditor(), 1, 0);

    }

    private TableView<Diary> buildDiariesView() {

        final TableView<Diary> diaryTableView = new TableView<>(diaries);
        final TableColumn<Diary, Long> diaryTimestampColumn = new TableColumn<>("Time");
        diaryTimestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        final TableColumn<Diary, String> diaryTextColumn = new TableColumn<>("Text");
        diaryTextColumn.setCellValueFactory(new PropertyValueFactory<>("text"));

        diaryTableView.getColumns().addAll(diaryTimestampColumn, diaryTextColumn);
        diaryTableView.setPrefWidth(400);

        diaryTableView.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> editDiaryDetails(newValue));


        return diaryTableView;
    }

    private void editDiaryDetails(Diary diary) {

        if (diary != null) {
            isEdited.set(true);

            final LocalDateTime localDateTime = LocalDateTime.ofInstant(diary.getTimestamp(), ZoneId.systemDefault());
            datePicker.setValue(localDateTime.toLocalDate());
            timePicker.setValue(localDateTime.toLocalTime());
            textArea.setText(diary.getText());
        }


    }

    private void editNewDiary() {
        isEdited.set(false);
        final LocalDateTime localDateTime = LocalDateTime.ofInstant(newDiary.getTimestamp(), ZoneId.systemDefault());
        datePicker.setValue(localDateTime.toLocalDate());
        timePicker.setValue(localDateTime.toLocalTime());
        textArea.setText(newDiary.getText());

    }


    private GridPane buildDiaryEditor() {

        final GridPane pane = new GridPane();
        pane.getStyleClass().add("diary-editor");
        pane.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);


        final StringBinding stringBinding = Bindings.when(isEdited).then(MaterialDesignIcon.TABLE_EDIT.name()).
                otherwise(MaterialDesignIcon.NEW_BOX.name());


        MaterialDesignIconView materialDesignIconView = new MaterialDesignIconView(MaterialDesignIcon.NEW_BOX);
        materialDesignIconView.glyphNameProperty().bind(stringBinding);//todo set change icon for edit and new mode `table-edit` icon
        materialDesignIconView.setStyleClass("new-icon");
//        materialDesignIconView.setSize("4em");
        pane.add(materialDesignIconView, 0, 0);

        final JFXButton newButton = new JFXButton("جدید");
        newButton.setOnAction(event -> editNewDiary());
        pane.add(newButton, 1, 0);


        final JFXButton clearButton = new JFXButton("پاک کردن");
        clearButton.setOnAction(event -> clearNewDiary());
        pane.add(clearButton, 2, 0);



        final Label timeLabel = new Label("ساعت");
        timePicker = new JFXTimePicker(LocalTime.now());
        timePicker.setIs24HourView(true);
        timePicker.setPrefWidth(300);
        timePicker.setDefaultColor(Paint.valueOf(DEFAULT_COLOR));
        pane.add(timeLabel, 0, 2, 1, 1);
        pane.add(timePicker, 1, 2, 2, 1);


        buildDatePicker(pane);

        final Label textLabel = new Label("متن");
        textArea = new JFXTextArea();
        textArea.setPrefWidth(300);
        pane.add(textLabel, 0, 3, 1, 1);
        pane.add(textArea, 1, 3, 2, 1);


        final JFXButton saveButton = new JFXButton("ثبت");
        saveButton.setOnAction(event -> saveChanges());
        pane.add(saveButton, 1, 4);
        final JFXButton removeButton = new JFXButton("حذف");
        pane.add(removeButton, 2, 4);


        datePicker.valueProperty().addListener(localDateListener);
        timePicker.valueProperty().addListener(timeListener);
        textArea.textProperty().addListener(textListener);


        return pane;
    }

    private void buildDatePicker(GridPane pane) {


        final Label dateLabel = new Label("روز");
        datePicker = new JFXDatePicker(LocalDate.now());
        datePicker.setDefaultColor(Paint.valueOf(DEFAULT_COLOR));
        datePicker.setPrefWidth(200);


        final ULocale locale = new ULocale("fa_IR@calendar=persian");
        final Calendar calendar = Calendar.getInstance(locale);
        final DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, locale);

        final Label persianDateLabel = new Label(toPersianString(datePicker.getValue(), calendar, df));
        persianDateLabel.setPrefWidth(100);
        persianDateLabel.getStyleClass().add("persian-date-label");

        datePicker.valueProperty().addListener(new ChangeListener<LocalDate>() {
            private boolean changing;

            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                if (!changing) {
                    try {
                        changing = true;
                        final String format = toPersianString(newValue, calendar, df);
                        persianDateLabel.textProperty().set(format);
                    } finally {
                        changing = false;
                    }
                }
            }
        });


        pane.add(dateLabel, 0, 1);
        pane.add(datePicker, 1, 1);
        pane.add(persianDateLabel, 2, 1);
    }

    @NotNull
    private String toPersianString(LocalDate newValue, Calendar calendar, DateFormat df) {
        final LocalDateTime localDateTime = newValue.atTime(timePicker.getValue());
        final ZonedDateTime zonedDateTime = localDateTime.atZone(ZoneId.systemDefault());
        final java.util.Date date = Date.from(zonedDateTime.toInstant());
        calendar.setTime(date);
        return df.format(calendar);
    }

    private void saveChanges() {

        if (!isEdited.get()) {
            try {
                final Diary clone = (Diary) newDiary.clone();
                repository.saveDiary(clone);
                diaries.add(clone);
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
        }
    }

    private void clearNewDiary() {
        newDiary.setTimestamp(Instant.now());
        newDiary.setText("");
        editNewDiary();
    }


//    private JFXListCell<Diary> diaryListCellFactory(ListView<Diary> param) {
//        return new DiaryListCell();
//    }

    private void getLatestDiaries(long limitTime, long limitCount) {
// TODO: 5/13/2017 implement ...

        final List<Diary> diariesList = repository.readAllDiaries();

        diaries.clear();
        diaries.addAll(diariesList);
    }

}
