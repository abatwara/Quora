package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired private AnswerDao answerDao;
    @Autowired private UserDao userDao;
    @Autowired private QuestionDao questionDao;

    public AnswerEntity createAnswer(AnswerEntity answerEntity, String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        //User authentication verification
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }

        //Question ID validation.
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);

        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
//        answerEntity.setUserEntity(user);
        return answerDao.createAnswer(answerEntity);
    }

    public AnswerEntity editAnswer(String answer, String accessToken, String answerId) throws AuthorizationFailedException, AnswerNotFoundException {

        //User authentication verification
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }

        //answer ID verification
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity=answerDao.getAnswerById(answerId);
        if (answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if(!answerEntity.getUserEntity().getUuid().equals(userAuthTokenEntity.getUuid())){
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can edit the answer");
        }

        answerEntity.setAnswer(answer);
        answerDao.editAnswer(answerEntity);

        return answerDao.editAnswer(answerEntity);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AnswerEntity deleteAnswer(String answerId, String accessToken) throws AuthorizationFailedException, AnswerNotFoundException{

        //User authentication verification
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if (userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        } else if (userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to edit an answer");
        }

        //answer ID verification
        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity=answerDao.getAnswerById(answerId);
        if (answerEntity==null){
            throw new AnswerNotFoundException("ANS-001","Entered answer uuid does not exist");
        }
        if (answerEntity.getUserEntity().getRole().equals("admin") || answerEntity.getUserEntity().getUuid().equals(userAuthTokenEntity.getUuid())){
        return answerDao.deleteAnswer(answerId);
        }else{
            throw new AuthorizationFailedException("ATHR-003","Only the answer owner can delete the answer");
        }
    }

    public List<AnswerEntity> getAllAnswersToQeuestion(String questionId, String accessToken) throws  AuthorizationFailedException, InvalidQuestionException{
        //User authentication verification
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(accessToken);
        if(userAuthTokenEntity ==null){
            throw new AuthorizationFailedException("ATHR-001","User has not signed in");
        }else if(userAuthTokenEntity.getLogoutAt()!=null){
            throw new AuthorizationFailedException("ATHR-002","User is signed out.Sign in first to post an answer");
        }

        //Question ID validation.
        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "The question entered is invalid");
        }

        return answerDao.getAllAnswersToQuestion(questionId);
    }
}
