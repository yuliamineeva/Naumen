package ru.javakids.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnswerDto implements Serializable {
  private static final long serialVersionUID = 1;

  List<Answer> answers;
  Long lectureId;
}
