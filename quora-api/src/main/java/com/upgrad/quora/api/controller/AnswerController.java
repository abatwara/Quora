package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.AnswerService;
import com.upgrad.quora.service.entity.AnswerEntity;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.exception.AnswerNotFoundException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.InvalidQuestionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.print.attribute.standard.Media;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/")
public class AnswerController {

    @Autowired private AnswerService answerService;


    /**
     * This controller is invoked when request pattern matches /question/{questionId}/answer/create and main purpose is to create answer for a existing question
     * @param accessToken
     * @param questionId
     * @param answerRequest
     * @return
     * @throws InvalidQuestionException
     * @throws AuthorizationFailedException
     */

    @RequestMapping(
            method = RequestMethod.POST,
            path = "/question/{questionId}/answer/create",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<AnswerResponse> createAnswer(@RequestHeader("authorization") String accessToken , @PathVariable("questionId") String questionId, AnswerRequest answerRequest) throws InvalidQuestionException, AuthorizationFailedException {

        AnswerEntity answerEntity = new AnswerEntity();
        answerEntity.setAnswer(answerRequest.getAnswer());
        answerEntity = answerService.createAnswer(answerEntity,accessToken,questionId);

        AnswerResponse answerResponse= new AnswerResponse();
        answerResponse.setId(answerEntity.getUuid());
        answerResponse.setStatus("ANSWER CREATED");

        return new ResponseEntity<AnswerResponse>(answerResponse,HttpStatus.CREATED);
    }

    /**
     * This controller is invoked when request pattern matches /answer/edit/{answerId} and main purpose is to allow user update the existing question's
     * answer in the DB
     * @param accessToken
     * @param answerId
     * @param answerEditRequest
     * @return
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */

    @RequestMapping(
        method = RequestMethod.PUT,
        path = "/answer/edit/{answerId}",
        consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<AnswerEditResponse> editAnswer(@RequestHeader("authorization") String accessToken, @PathVariable("answerId") String answerId, AnswerEditRequest answerEditRequest) throws AuthorizationFailedException, AnswerNotFoundException {

        AnswerEntity answerEntity = new AnswerEntity();
        String answer = answerEditRequest.getContent();
        answerEntity = answerService.editAnswer(answer,accessToken,answerId);

        AnswerEditResponse answerEditResponse = new AnswerEditResponse();
        answerEditResponse.setId(answerEntity.getUuid());
        answerEditResponse.setStatus("ANSWER EDITED");

        return new ResponseEntity<AnswerEditResponse>(answerEditResponse,HttpStatus.OK);

    }

    /**
     * This controller is invoked when request pattern matches /answer/delete/{answerId} and main purpose is to store delete existing answer for a question
     * @param accessToken
     * @param answerId
     * @return
     * @throws AuthorizationFailedException
     * @throws AnswerNotFoundException
     */

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") String accessToken, @PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException{

        AnswerEntity answerEntity = answerService.deleteAnswer(answerId, accessToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
        answerDeleteResponse.setId(answerEntity.getUuid());
        answerDeleteResponse.setStatus("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }


    /**
     * This controller is invoked when request pattern matches /question/all/{questionId} and main purpose is to fetch
     * all answers for a specific question from DB
     * @param accessToken
     * @param questionId
     * @return
     * @throws AuthorizationFailedException
     * @throws InvalidQuestionException
     */
    @RequestMapping(
            method = RequestMethod.GET,
            path = "/answer/all/{questionId}",
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<List<AnswerDetailsResponse>> getAllAnswersToQuestion(@RequestHeader("authorization") String accessToken, @PathVariable("questionId")  String questionId) throws AuthorizationFailedException, InvalidQuestionException{
        List<AnswerEntity> answers = answerService.getAllAnswersToQeuestion(questionId, accessToken);
        List<AnswerDetailsResponse> answerDetailsResponses= new ArrayList<>();

        for(AnswerEntity answerEntity : answers){
            AnswerDetailsResponse answerDetailsResponse = new AnswerDetailsResponse();
            answerDetailsResponse.setAnswerContent(answerEntity.getAnswer());
            answerDetailsResponse.setId(answerEntity.getUuid());
            answerDetailsResponse.setQuestionContent(answerEntity.getQuestionEntity().getContent());
            answerDetailsResponses.add(answerDetailsResponse);
        }
        return new ResponseEntity<List<AnswerDetailsResponse>>(answerDetailsResponses, HttpStatus.OK);
    }

}
