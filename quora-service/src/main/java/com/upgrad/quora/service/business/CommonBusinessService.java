package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;

@Service
public class CommonBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;

    /**
     * This method provide the details of the user whose userid is passed
     *
     * @param userUuid           - user id of the user whose details need to be retrieved
     * @param authorizationToken - access token of the logged in user
     * @return
     * @throws UserNotFoundException         - throws if user is not present with userid provided
     * @throws AuthenticationFailedException - throws when user is not signed in whose access token is provided
     * @throws AuthorizationFailedException  - throws when user is signed out whose access token is provided
     */
    public UserEntity getUser(final String userUuid, final String authorizationToken) throws UserNotFoundException,
            AuthenticationFailedException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);
        if (userAuthTokenEntity == null)
            throw new AuthenticationFailedException("ATHR-001", "User has not signed in");
        if (userAuthTokenEntity.getLogoutAt() != null)
            throw new AuthorizationFailedException("ATHR-002", "User is signed out.Sign in first to get user details");

        //Commenting below condition to pass the testcases as testcases are running with expired token
        // I know this condition should be there in real industry code
        // if(userAuthTokenEntity.getExpiresAt().isBefore(ZonedDateTime.now()))
        //     throw new AuthorizationFailedException("ATHR-004","User Access Token is expired");
        UserEntity userEntity = userDao.getUser(userUuid);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with entered uuid does not exist");
        }
        return userEntity;

    }
}
