package com.upgrad.quora.api.controller;
import com.upgrad.quora.api.model.*;
import com.upgrad.quora.service.business.QuestionService;
import com.upgrad.quora.service.entity.QuestionEntity;
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

    @RequestMapping(method = RequestMethod.POST, path = "/question/create", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionResponse> createQuestion (final QuestionRequest questionRequest) {
        final QuestionEntity questionEntity = new QuestionEntity();
        questionEntity.setUuid(UUID.randomUUID().toString());
        questionEntity.setContent(questionRequest.getContent());
        questionEntity.setUser_id(1024);
        questionEntity.setDate(new Date());
        final QuestionEntity createdQuestionEntity = questionService.createQuestion(questionEntity);
        QuestionResponse questionResponse = new QuestionResponse().id(createdQuestionEntity.getUuid()).status("Created");
        return new ResponseEntity<QuestionResponse>(questionResponse, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestions () {
        final ArrayList<Object> result = new ArrayList();
        final List<QuestionEntity> questionEntity = questionService.getAllQuestions();
        for (QuestionEntity text : questionEntity) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            result.add(questionDetailsResponse.content(text.getContent()).id(text.getUuid()));
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.GET, path = "/question/all/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<QuestionDetailsResponse> getAllQuestionsByUser (@PathVariable("userId") Integer userId) {
        final ArrayList<QuestionDetailsResponse> result = new ArrayList();
        final List<QuestionEntity> questionEntity = questionService.getAllQuestionsByUser(userId);
        for (QuestionEntity text : questionEntity) {
            QuestionDetailsResponse questionDetailsResponse = new QuestionDetailsResponse();
            result.add(questionDetailsResponse.content(text.getContent()).id(text.getUuid()));
        }
        return new ResponseEntity(result, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.PUT, path = "/question/edit/{questionId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
        public ResponseEntity<QuestionEditResponse> editQuestionContent (@PathVariable("questionId") String questionId, @RequestBody QuestionEditRequest questionEditRequest ) {
        Integer updateCount = questionService.editQuestionContent(questionEditRequest.getContent(), questionId);
        if(updateCount > 0){
            QuestionEditResponse questionEditResponse = new QuestionEditResponse().id(questionId).status("UPDATED");
            return new ResponseEntity(questionEditResponse, HttpStatus.OK);
        }
        else{
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

}
