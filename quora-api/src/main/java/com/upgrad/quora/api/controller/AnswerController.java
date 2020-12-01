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

    @RequestMapping(
            method = RequestMethod.DELETE,
            path = "/answer/delete/{answerId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE
    )
    public ResponseEntity<AnswerDeleteResponse> deleteAnswer(@RequestHeader("authorization") String accessToken, @PathVariable("answerId") String answerId) throws AuthorizationFailedException, AnswerNotFoundException{

        AnswerEntity answerEntity = answerService.deleteAnswer(answerId, accessToken);
        AnswerDeleteResponse answerDeleteResponse = new AnswerDeleteResponse();
        answerDeleteResponse.setId(answerEntity.getUuid());
        answerDeleteResponse.setStatus("ANSWER DELETED");

        return new ResponseEntity<AnswerDeleteResponse>(answerDeleteResponse, HttpStatus.OK);
    }

    @RequestMapping(
            method = RequestMethod.GET,
            path = "answer/all/{questionId}",
            consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
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
