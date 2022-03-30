package ru.naumen.javakids.model.lectures;

import ru.naumen.javakids.model.AbstractLecture;
import ru.naumen.javakids.model.Status;

public class Lecture2 extends AbstractLecture {
    public Lecture2() {
        id = 2L;
        topic = "Структура Java приложения";
        content = "Содержимое лекции 2";
        status = Status.NOT_STARTED;
    }
}
