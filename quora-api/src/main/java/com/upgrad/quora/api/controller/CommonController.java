package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDetailsResponse;
import com.upgrad.quora.service.business.CommonBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class CommonController {

    @Autowired
    private CommonBusinessService commonBusinessService;

    @RequestMapping(method = RequestMethod.GET, path = "/userprofile/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDetailsResponse> getUser(@PathVariable("id") final String userUuid,
                                                       @RequestHeader("authorization") final String authorization) throws UserNotFoundException, AuthenticationFailedException {
        final UserEntity userEntity = commonBusinessService.getUser(userUuid, authorization);
        UserDetailsResponse userDetailsResponse = new UserDetailsResponse()
                .firstName(userEntity.getFirstName())
                .lastName(userEntity.getLastName())
                .userName(userEntity.getUserName())
                .country(userEntity.getCountry())
                .aboutMe(userEntity.getAboutme())
                .emailAddress(userEntity.getEmail())
                .dob(userEntity.getDob())
                .contactNumber(userEntity.getContactNumber());
        return new ResponseEntity<UserDetailsResponse>(userDetailsResponse, HttpStatus.OK);
    }
    }
