package ru.naumen.javakids.model;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "lectures")
public class AddLecture extends AbstractLecture {

    public AddLecture() {
    }

    public AddLecture(String topic, String content) {
        this.topic = topic;
        this.content = content;
        this.getStatus();
    }
}
