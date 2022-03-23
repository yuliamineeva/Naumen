package ru.naumen.javakids.model;

public class AddLecture extends AbstractLecture {

    public AddLecture() {
    }

    public AddLecture(String topic, String content) {
        this.topic = topic;
        this.content = content;
    }
}
