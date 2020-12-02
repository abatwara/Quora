package com.upgrad.quora.service.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;

@Entity
@Table(name = "question", schema = "public")
@NamedQueries(
        {
                @NamedQuery(name = "questionAll", query = "select q from QuestionEntity q"),
                @NamedQuery(name = "getQuestionById", query = "select q from QuestionEntity q where q.uuid=:uuid"),
                @NamedQuery(name = "questionByUserId", query = "select q from QuestionEntity q where q.userEntity.uuid= :user_id")
        }
)
public class QuestionEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    @Column(name = "UUID")
    @Size(max = 200)
    @NotNull
    private String uuid;

    @Column(name = "CONTENT")
    @NotNull
    @Size(max = 500)
    private String content;

    @Column(name = "DATE")
    private Date date;

    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

}
