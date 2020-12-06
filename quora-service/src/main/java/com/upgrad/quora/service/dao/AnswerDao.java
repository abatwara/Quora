package com.upgrad.quora.service.dao;

import com.upgrad.quora.service.entity.AnswerEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class AnswerDao {

    @PersistenceContext private EntityManager entityManager;

    /**
     * This method accepts a new answer entity and creates a new record in the database
     * @param answerEntity
     * @return
     */
    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    /**
     * This method updates the answer record in the database
     * @param answerEntity
     * @return
     */
    public AnswerEntity editAnswer(AnswerEntity answerEntity){
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    /**
     * This method queries DB using answerId parameter and fetches answer record
     * @param answerId
     * @return
     */

    public AnswerEntity getAnswerById(String answerId){
        try {
            return entityManager.createNamedQuery("getAnswerById",AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    /**
     * This method deletes existing answer record in the database if the answer id exists
     * @param answerId
     * @return
     */
    public AnswerEntity deleteAnswer(String answerId){
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer!=null){
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }

    /**
     * This method queries the DB using question Id and fetches all answers for a specific question
     * @param questionId
     * @return
     */
    public List<AnswerEntity> getAllAnswersToQuestion(String questionId){
        return entityManager.createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
    }

}
