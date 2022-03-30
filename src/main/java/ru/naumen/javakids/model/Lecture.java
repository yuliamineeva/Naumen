package ru.naumen.javakids.model;

public interface Lecture {
    Long getId();
    String getTopic();
    String getContent();
    Status getStatus();
    void setStatus(Status status);
}
