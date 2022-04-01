package ru.naumen.javakids.model;

import javax.persistence.*;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String topic;
    private String content;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    public Lecture() {
    }

    public Lecture(String topic, String content) {
        this.topic = topic;
        this.content = content;
      //  this.setStatus(Status.NOT_STARTED);
    }

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
