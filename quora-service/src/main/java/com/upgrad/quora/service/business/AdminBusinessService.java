package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;

import com.upgrad.quora.service.entity.UserAuthTokenEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import com.upgrad.quora.service.exception.AuthorizationFailedException;
import com.upgrad.quora.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdminBusinessService {
    @Autowired
    private UserDao userDao;
    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    public UserEntity deleteUser(final String userUuid, final String authorizationToken) throws UserNotFoundException,
            AuthenticationFailedException, AuthorizationFailedException {
        UserAuthTokenEntity userAuthTokenEntity = userDao.getUserAuthToken(authorizationToken);

        //Integer deleteUserId =
        UserEntity deleteUserEntity = userDao.getUser(userUuid);
        if(userAuthTokenEntity==null) {
            throw new AuthenticationFailedException("ATHR-001", "User has not signed in");
        }
        if(deleteUserEntity==null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        if(userAuthTokenEntity.getLogoutAt()!=null)
            throw new AuthorizationFailedException("ATHR-002","User is signed out");
        UserEntity userEntity  = userDao.getUser(userAuthTokenEntity.getUuid());
        if (!userEntity.getRole().equalsIgnoreCase("admin")) {
            throw new AuthorizationFailedException("ATHR-003","Unauthorized Access, Entered user is not an admin");
        }

//        userDao.deleteUserAuthToken(deleteUserEntity.getId());
        userDao.deleteUser(deleteUserEntity.getId());

        return deleteUserEntity;
    }
}
