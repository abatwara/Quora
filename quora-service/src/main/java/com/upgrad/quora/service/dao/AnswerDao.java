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

    public AnswerEntity createAnswer(AnswerEntity answerEntity){
        entityManager.persist(answerEntity);
        return answerEntity;
    }

    public AnswerEntity editAnswer(AnswerEntity answerEntity){
        entityManager.merge(answerEntity);
        return answerEntity;
    }

    public AnswerEntity getAnswerById(String answerId){
        try {
            return entityManager.createNamedQuery("getAnswerById",AnswerEntity.class).setParameter("uuid", answerId).getSingleResult();
        }catch (NoResultException e){
            return null;
        }
    }

    public AnswerEntity deleteAnswer(String answerId){
        AnswerEntity deleteAnswer = getAnswerById(answerId);
        if (deleteAnswer!=null){
            entityManager.remove(deleteAnswer);
        }
        return deleteAnswer;
    }

    public List<AnswerEntity> getAllAnswersToQuestion(String questionId){
        return entityManager.createNamedQuery("getAllAnswersToQuestion", AnswerEntity.class).setParameter("uuid", questionId).getResultList();
    }

}
