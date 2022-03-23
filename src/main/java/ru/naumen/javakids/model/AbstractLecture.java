package ru.naumen.javakids.model;

public class AbstractLecture implements Lecture {
    protected int id;
    protected String topic;
    protected String content;
    Status status;

    @Override
    public int getId() {
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
        return Status.NOT_STARTED;
    }
}
