package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.*;
import java.util.List;

@Repository
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    public QuestionEntity createQuestion (QuestionEntity questionEntity) {
       entityManager.persist(questionEntity);
       return questionEntity;
    }

    public List<QuestionEntity> getAllQuestions () {
        final List allQuestions = entityManager.createNamedQuery("questionAll", QuestionEntity.class).getResultList();
        return allQuestions;
    }

    public List<QuestionEntity> getAllQuestionsByUser(final Integer user_id) {
        return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("user_id", user_id).getResultList();

    }

    public Integer editQuestionContent(String content, String uuid) {
        Query query = entityManager.createQuery("Update QuestionEntity q SET q.content = :content where q.uuid = :uuid");
        query.setParameter("uuid", uuid);
        query.setParameter("content", content);
        return query.executeUpdate();
    }

    public QuestionEntity deleteQuestion (QuestionEntity questionEntity) {
        return questionEntity;
    }

    public QuestionEntity getQuestionById(String questionID){
        try {
            return entityManager.createNamedQuery("getQuestionById", QuestionEntity.class).setParameter("uuid",questionID).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
