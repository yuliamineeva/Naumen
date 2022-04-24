package ru.naumen.javakids.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
@Table(name = "grade")
public class Grade {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private Integer value;
}
