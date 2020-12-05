package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.QuestionDao;
import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class QuestionService {

    @Autowired
    private QuestionDao questionDao;

    @Autowired
    private UserDao userDao;

    /**
     * This service method interacts with DAO class to check to if the user is authorized or not , then it fetches the user id for the logged in
     * user and uses the passed questionEntity to add model properties and calls the DAO method to pass the created entity to the DB.
     * @param questionEntity
     * @param authorizationToken
     * @return
     * @throws AuthorizationFailedException
     */


    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity createQuestion(QuestionEntity questionEntity, final String authorizationToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to post a question");
        }
        UserEntity userId = userDao.getUser(userAuthTokenEntity.getUuid());
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setDate(ZonedDateTime.now());
        questionEntity.setUserEntity(userId);
        return questionDao.createQuestion(questionEntity);
    }

    /**
     * This method takes auth token, fetches user authorization token and based on that handles authorization failures,
     * Also interacts with DAO method to fetch all questions from database.
     * @param authorizationToken
     * @return
     * @throws AuthorizationFailedException - When user has signed out, or not signed in to perform operation
     */
    public List<QuestionEntity> getAllQuestions(final String authorizationToken) throws AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }
        return questionDao.getAllQuestions();
    }

    /**
     *This method takes auth token and user ID and fetches user authorization token and based on that handles authorization failures,
     * Also interacts with DAO method to fetch all questions by that specific user id which was passed as path parameter
     * @param user_id
     * @param authorizationToken
     * @return
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */
    public List<QuestionEntity> getAllQuestionsByUser(final String user_id, final String authorizationToken) throws AuthorizationFailedException, UserNotFoundException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        UserEntity userEntity = userDao.getUser(user_id);

        if(userEntity == null ){
            throw  new UserNotFoundException("USR-001", "User with entered uuid whose question details are to be seen does not exist");
        }
        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions posted by a specific user");
        }


        return questionDao.getAllQuestionsByUser(user_id);
    }

    /**
     * This method takes the content of the question which needs to be updated, question Id and authorization token , validates user's identity
     * checks if the question exists in the DB and then call DAO method to update the question content
     * @param content
     * @param questionId
     * @param authorizationToken
     * @return
     * @throws AuthorizationFailedException  - When user has signed out, or not signed in or logged in user is not the owner
     * @throws InvalidQuestionException  - when question id passed to the request does not exist in the DB
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity editQuestionContent(String content, String questionId, final String authorizationToken) throws AuthorizationFailedException, InvalidQuestionException {

        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);
        if (questionEntity != null) {
            if (!questionEntity.getUserEntity().getUuid().equals(userAuthTokenEntity.getUuid())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the question owner can edit the question");
            }
            questionEntity.setContent(content);
            return questionDao.editQuestionContent(questionEntity);
        } else {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

    }

    /**
     * This method takes question id and auth token as parameters, validates user identity and if user's role is admin or user is owner of the question
     * it calls the DAO method to delete the question from DB.
     * @param questionId
     * @param authorizationToken
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @Transactional(propagation = Propagation.REQUIRED)
    public QuestionEntity deleteQuestion(String questionId, final String authorizationToken) throws AuthorizationFailedException,InvalidQuestionException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        if(userAuthTokenEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }
        else if(userAuthTokenEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get all questions");
        }

        QuestionEntity questionEntity = questionDao.getQuestionById(questionId);

        if(questionEntity == null) {
            throw new InvalidQuestionException("QUES-001", "Entered question uuid does not exist");
        }

       if(userAuthTokenEntity.getUser().getRole().equalsIgnoreCase("admin") || userAuthTokenEntity.getUuid().equals(questionEntity.getUserEntity().getUuid())) {
            return questionDao.deleteQuestion(questionId);
        }
        throw new AuthorizationFailedException("ATHR-003", "Only the question owner or admin can delete the question");
    }

}
