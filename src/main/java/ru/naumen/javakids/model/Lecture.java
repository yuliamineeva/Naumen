package ru.naumen.javakids.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "lectures")
public class Lecture extends AbstractLecture {

    public Lecture() {
    }

    public Lecture(String topic, String content) {
        this.topic = topic;
        this.content = content;
        this.setStatus(Status.NOT_STARTED);
    }
}
