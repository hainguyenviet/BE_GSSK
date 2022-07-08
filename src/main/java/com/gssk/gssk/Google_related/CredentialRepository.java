package com.gssk.gssk.Google_related;

import com.warrenstrange.googleauth.ICredentialRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CredentialRepository implements ICredentialRepository {

    private user_info userInfo;

    @Override
    public String getSecretKey(String s) {
        return userInfo.getSecretKey();
    }

    @Override
    //update the table
    public void saveUserCredentials(String s, String s1, int i, List<Integer> list) {

    }


}

