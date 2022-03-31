package ru.naumen.javakids.model;

public interface Lecture {
    Long getId();
    String getTopic();
    String getContent();
    Status getStatus();
    void setTopic(String topic);
    void setContent(String content);
    void setStatus(Status status);
}
