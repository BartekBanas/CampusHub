package entity;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "rating", schema = "dziennik")
public class RatingEntity extends AbstractEntity {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;
    @Basic
    @Column(name = "subject")
    private String subject;
    @Basic
    @Column(name = "date")
    private String date;
    @Basic
    @Column(name = "comment")
    private String comment;
    @Basic
    @Column(name = "grade")
    private int grade;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RatingEntity that = (RatingEntity) o;
        return id == that.id && grade == that.grade && Objects.equals(subject, that.subject) && Objects.equals(date, that.date) && Objects.equals(comment, that.comment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subject, date, comment, grade);
    }
}
