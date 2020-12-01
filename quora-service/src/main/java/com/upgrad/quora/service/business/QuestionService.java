package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity) {
       return questionDao.createQuestion(questionEntity);
    }
    public List<QuestionEntity> getAllQuestions() {
        return questionDao.getAllQuestions();
    }

    public List<QuestionEntity> getAllQuestionsByUser(final Integer user_id) {
        return questionDao.getAllQuestionsByUser(user_id);
    }
    @Transactional(propagation = Propagation.REQUIRED)
    public Integer editQuestionContent(String content, String questionId) {
        return questionDao.editQuestionContent(content, questionId);
    }

    public QuestionEntity deleteQuestion(QuestionEntity questionEntity) {
        return questionDao.deleteQuestion(questionEntity);
    }

}
