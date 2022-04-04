package ru.naumen.javakids.model;

import com.sun.istack.NotNull;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "userlectures_table")
public class UserLecture {

    public UserLecture() {
    }

    @Embeddable
    public static class Id implements Serializable {

        @Column(name = "user_id")
        protected Long userId;

        @Column(name = "lecture_id")
        protected Long lectureId;

        public Id() {
        }

        public Id(Long userId, Long lectureId) {
            this.userId = userId;
            this.lectureId = lectureId;
        }

        public boolean equals(Object o) {
            if (o != null && o instanceof Id) {
                Id that = (Id) o;
                return this.userId.equals(that.userId)
                        && this.lectureId.equals(that.lectureId);
            }
            return false;
        }

        public int hashCode() {
            return userId.hashCode() + lectureId.hashCode();
        }
    }

    @EmbeddedId
    protected Id id = new Id();

    //    @Column(name = "status", updatable = false)
    @Column(name = "status")
    @NotNull
    @Enumerated(EnumType.STRING)
    protected Status status;

    @ManyToOne
    @JoinColumn(
            name = "user_id",
            insertable = false, updatable = false)
    protected User user;

    @ManyToOne
    @JoinColumn(
            name = "lecture_id",
            insertable = false, updatable = false)
    protected Lecture lecture;

    public UserLecture(User user, Lecture lecture, Status status) {
        this.user = user;
        this.lecture = lecture;
        this.status = status;

        this.id.userId = user.getId();
        this.id.lectureId = lecture.getId();
        user.getUserLectures().add(this);
        lecture.getUserLectures().add(this);
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Id getId() {
        return id;
    }
}
