package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/")
public class QuestionController {

    @Autowired
    private QuestionService questionService;


    /**
     *
     * This controller is invoked when request pattern matches /question/create and main purpose is to store new questions to the database
     * @param questionRequest - Model of question
     * @param authorization
     * @return - JSON response containing created question id and http status
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion (final QuestionRequest questionRequest, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        final QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity , authorization);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("Created");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }

    /**
     * This controller is invoked when request pattern matches /question/all and main purpose is to fetch all questions from the database
     * @param authorization
     * @return JSON response of all the questions array
     * @throws AuthorizationFailedException
     */

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions (@RequestHeader("authorization") final String authorization) throws AuthorizationFailedException {
        final ArrayList<QuestionDetailsResponse> result = new ArrayList();
        final List<QuestionEntity> questionEntity = questionService.getAllQuestions(authorization);
        for (QuestionEntity text : questionEntity) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            result.add(questionDetailsResponse.content(text.getContent()).id(text.getUuid()));
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * This controller is invoked when request pattern matches /question/all/{userId} and main purpose is to fetch all questions posted by
     * a specific user
     * @param userId  - user ID whose question needs to be fetched
     * @param authorization
     * @return  JSON response of question array
     * @throws AuthorizationFailedException
     * @throws UserNotFoundException
     */

    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser (@PathVariable("userId") String userId, @RequestHeader("authorization") final String authorization) throws AuthorizationFailedException, UserNotFoundException {
        final ArrayList<QuestionDetailsResponse> result = new ArrayList();
        final List<QuestionEntity> questionEntity = questionService.getAllQuestionsByUser(userId, authorization);
        for (QuestionEntity text : questionEntity) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            result.add(questionDetailsResponse.content(text.getContent()).id(text.getUuid()));
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    /**
     * This controller is invoked when request pattern matches /question/edit/{questionId} and this allows user to update the question
     * @param questionId
     * @param questionEditRequest
     * @param authorization
     * @return updated status of edited question
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */


    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<QuestionEditResponse> editQuestionContent (@PathVariable("questionId") String questionId, QuestionEditRequest questionEditRequest, @RequestHeader("authorization") final String authorization ) throws AuthorizationFailedException, InvalidQuestionException {
        QuestionEntity questionEntity = questionService.editQuestionContent(questionEditRequest.getContent(), questionId, authorization);
        QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionEntity.getUuid()).status("QUESTION EDITED");
        return new ResponseEntity<QuestionEditResponse>(questionEditResponse, HttpStatus.OK);
    }

    /**
     * This controller is invoked when request pattern matches /question/delete/{questionId} and this allows user to delete existing question
     * @param questionId
     * @param authorization
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */

    @RequestMapping(method = RequestMethod.DELETE, path = "/question/delete/{questionId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDeleteResponse> deleteQuestion (@PathVariable("questionId") String questionId, @RequestHeader("authorization") final String authorization  ) throws AuthorizationFailedException, InvalidQuestionException {

        QuestionEntity questionEntity = questionService.deleteQuestion(questionId, authorization);
        QuestionDeleteResponse questionDeleteResponse = new QuestionDeleteResponse();
        questionDeleteResponse.setId(questionEntity.getUuid());
        questionDeleteResponse.setStatus("QUESTION DELETED");
        return new ResponseEntity<QuestionDeleteResponse>(questionDeleteResponse, HttpStatus.OK);
    }

}
