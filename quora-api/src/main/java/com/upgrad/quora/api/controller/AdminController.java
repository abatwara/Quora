package com.upgrad.quora.api.controller;

import com.upgrad.quora.api.model.UserDeleteResponse;
import com.upgrad.quora.service.business.AdminBusinessService;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class AdminController {

    @Autowired
    private AdminBusinessService adminBusinessService;

    /**
     * Controller method to delete the user with mapping '/admin/user/{userId}'
     *
     * @param userUuid      - userid of the user who has to be deleted
     * @param authorization - authrization header of the logged in user
     * @return
     * @throws UserNotFoundException
     * @throws AuthenticationFailedException
     * @throws AuthorizationFailedException
     */
    @RequestMapping(method = RequestMethod.DELETE, path = "/admin/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public ResponseEntity<UserDeleteResponse> deleteUser(@PathVariable("userId") final String userUuid,
                                                         @RequestHeader("authorization") final String authorization)
            throws UserNotFoundException, AuthenticationFailedException, AuthorizationFailedException {
        final UserEntity userEntity = adminBusinessService.deleteUser(userUuid, authorization);
        UserDeleteResponse userDeleteResponse = new UserDeleteResponse();
        userDeleteResponse.id(userEntity.getUuid()).setStatus("USER SUCCESSFULLY DELETED");
        return new ResponseEntity<UserDeleteResponse>(userDeleteResponse, HttpStatus.OK);
    }
}
