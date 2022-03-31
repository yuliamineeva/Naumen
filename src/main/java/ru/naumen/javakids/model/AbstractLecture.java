package ru.naumen.javakids.model;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class AbstractLecture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    protected String topic;
    protected String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    protected Status status;

    public Long getId() {
        return id;
    }

    public String getTopic() {
        return topic;
    }

    public String getContent() {
        return content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
