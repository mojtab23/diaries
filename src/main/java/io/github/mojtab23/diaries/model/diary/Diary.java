package io.github.mojtab23.diaries.model.diary;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import jetbrains.exodus.entitystore.EntityId;

import java.time.Instant;

/**
 * Created by mojtab23 on 5/5/2017.
 */
public class Diary implements Cloneable {

    public static final String ENTITY_TYPE = "Diary";

    private final ObjectProperty<Instant> timestamp;
    //    private final StringProperty title;
    private final StringProperty text;
    private EntityId id;

    public Diary() {
        this.text = new SimpleStringProperty();
        timestamp = new SimpleObjectProperty<>(Instant.now());
//        title = new SimpleStringProperty();
    }

    public Diary(String text, Instant time) {
        this.text = new SimpleStringProperty(text);
        timestamp = new SimpleObjectProperty<>(time);

    }

    public Diary(String text, Instant time, EntityId id) {
        this.text = new SimpleStringProperty(text);
        timestamp = new SimpleObjectProperty<>(time);
        this.id = id;
    }

    public Diary(SimpleObjectProperty<Instant> timestamp, StringProperty text, EntityId id) {
        this.timestamp = timestamp;
        this.text = text;
        this.id = id;
    }

    public EntityId getId() {
        return id;
    }

    public void setId(EntityId id) {
        this.id = id;
    }

    public Instant getTimestamp() {
        return timestamp.get();
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp.set(timestamp);
    }

    public ObjectProperty<Instant> timestampProperty() {
        return timestamp;
    }

    public String getText() {
        return text.get();
    }

    public void setText(String text) {
        this.text.set(text);
    }

    public StringProperty textProperty() {
        return text;
    }

    @Override
    public String toString() {
        return "Diary{" +
                "id=" + id +
                ", timestamp=" + timestamp +
                ", text=" + text +
                '}';
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}
