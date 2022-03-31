package ru.naumen.javakids.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public class AbstractLecture implements Lecture, Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String topic;
    protected String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected Status status;

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getTopic() {
        return topic;
    }

    @Override
    public String getContent() {
        return content;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void setTopic(String topic) {
        this.topic = topic;
    }

    @Override
    public void setContent(String content) {
        this.content = content;
    }
}
