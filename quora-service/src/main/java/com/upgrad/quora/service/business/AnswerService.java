package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.AnswerDao;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AnswerService {

    @Autowired private AnswerDao answerDao;

    public AnswerEntity createAnswer(AnswerEntity answerEntity, String accessToken, String questionId) throws AuthorizationFailedException, InvalidQuestionException {

        //After Endpoint 1 we need to use the token authentication methods to authenticate token.

        //Question ID validation.

        answerEntity.setUuid(UUID.randomUUID().toString());
        answerEntity.setDate(ZonedDateTime.now());
//        answerEntity.setUserEntity(user);
        return answerDao.createAnswer(answerEntity);
    }
}
