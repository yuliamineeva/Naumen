package ru.naumen.javakids.model.lectures;

import ru.naumen.javakids.model.AbstractLecture;
import ru.naumen.javakids.model.Status;

public class Lecture3 extends AbstractLecture {
    public Lecture3() {
        id = 3;
        topic = "Объявление переменных";
        content = "Содержимое лекции 3";
        Status status = Status.NOT_STARTED;
    }
}
