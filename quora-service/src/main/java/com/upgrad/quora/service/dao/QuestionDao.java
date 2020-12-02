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
        try{
            final List allQuestions = entityManager.createNamedQuery("questionAll", QuestionEntity.class).getResultList();
            return allQuestions;

        }
        catch (NoResultException e){
            return null;
        }
    }

    public List<QuestionEntity> getAllQuestionsByUser(final String user_id) {
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("user_id", user_id).getResultList();
        }
        catch (NoResultException e){
            return null;
        }
    }

    public QuestionEntity editQuestionContent(QuestionEntity questionEntity) {
         entityManager.merge(questionEntity);
         return questionEntity;
    }

    public QuestionEntity deleteQuestion (String questionId) {
        QuestionEntity questionEntity = getQuestionById(questionId);
        if(questionEntity != null){
            entityManager.remove(questionEntity);
        }
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
