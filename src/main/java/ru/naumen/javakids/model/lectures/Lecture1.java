package ru.naumen.javakids.model.lectures;

import ru.naumen.javakids.model.AbstractLecture;
import ru.naumen.javakids.model.Status;

public class Lecture1 extends AbstractLecture {
    public Lecture1() {
        id = 1L;
        topic = "Знакомство с Java";
        content = "Содержимое лекции 1";
        Status status = Status.NOT_STARTED;
    }
}
