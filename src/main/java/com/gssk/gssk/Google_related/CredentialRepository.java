package com.gssk.gssk.Google_related;

import com.warrenstrange.googleauth.ICredentialRepository;


import org.springframework.stereotype.Component;

import java.util.*;

@Component

public class CredentialRepository implements ICredentialRepository {

    private user_info userInfo;

    public void setUserInfo(user_info user_info)
    {
        userInfo=user_info;
    }


    @Override
    public String getSecretKey(String s) {
             userInfo.setUsername(s);

            return userInfo.getSecretKey();
    }

    @Override

    public void saveUserCredentials(String username, String secretKey, int validationCode, List<Integer> scratchCodes) {
        userInfo.setUsername(username);
        userInfo.setSecretKey(secretKey);
        userInfo.setScratchCodes(scratchCodes);
        userInfo.setValidationCode(validationCode);

    }

}

