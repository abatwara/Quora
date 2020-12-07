package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.stereotype.Repository;
import javax.persistence.EntityManager;
import javax.persistence.NamedQuery;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Repository
@Transactional
public class QuestionDao {

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Stores the new question entity in the database by calling persist method
     * @param questionEntity
     * @return
     */
    public QuestionEntity createQuestion (QuestionEntity questionEntity) {
           entityManager.persist(questionEntity);
           return questionEntity;
    }

    /**
     * Using named query , fetches all questions from the database as a list
     * @return
     */
    public List<QuestionEntity> getAllQuestions () {
        try{
            final List allQuestions = entityManager.createNamedQuery("questionAll", QuestionEntity.class).getResultList();
            return allQuestions;

        }
        catch (NoResultException e){
            return null;
        }
    }

    /**
     * This method accepts user id as a parameter and queries the database to fetch all the questions posted by that specific user
     * @param user_id
     * @return
     */
    public List<QuestionEntity> getAllQuestionsByUser(final String user_id) {
        try {
            return entityManager.createNamedQuery("questionByUserId", QuestionEntity.class).setParameter("user_id", user_id).getResultList();
        }
        catch (NoResultException e){
            return null;
        }
    }

    /**
     * This method receives updated question entity as parameter and call merge method to update the existing record in the DB
     * @param questionEntity
     * @return
     */

    public QuestionEntity editQuestionContent(QuestionEntity questionEntity) {
         entityManager.merge(questionEntity);
         return questionEntity;
    }

    /**
     * This method receives questionId as parameter and if the question exists in the database it invokes the remove method passes the question
     * entity to be deleted
     * @param questionId
     * @return
     */

    public QuestionEntity deleteQuestion (String questionId) {
        QuestionEntity questionEntity = getQuestionById(questionId);
        if(questionEntity != null){
            entityManager.remove(questionEntity);
        }
        return questionEntity;
    }

    /**
     * This method accepts question Id as parameter and check in the database if the question exists in the DB
     * @param questionID
     * @return
     */

    public QuestionEntity getQuestionById(String questionID){
        try {
            return entityManager.createNamedQuery("getQuestionById", QuestionEntity.class).setParameter("uuid",questionID).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }
}
