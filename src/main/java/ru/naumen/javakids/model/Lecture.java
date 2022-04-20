package ru.naumen.javakids.model;

import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "lectures")
public class Lecture implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String topic;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    private String content;

    @OneToMany(mappedBy = "lecture")
    protected Set<UserLecture> userLectures  = new HashSet<>();

    public Lecture() {
    }

    public Lecture(String topic, String content) {
        this.topic = topic;
        this.content = content;
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

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Set<UserLecture> getUserLectures() {
        return userLectures;
    }

    public void setUserLectures(Set<UserLecture> userLectures) {
        this.userLectures = userLectures;
    }

}
