package com.gssk.gssk.Google_related;

import lombok.Data;
import org.springframework.context.annotation.Primary;

import javax.persistence.*;
import java.util.List;


@Data
@Entity
@Table(name = "user_info")
public class user_info {
    @Id
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "secretKey")
    private String secretKey;

    @Column(name = "validationCode")
    private int validationCode;

    @Column(name = "scratchCodes")
    private List<Integer> scratchCodes;



    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getSecretKey() {
        return secretKey;
    }

    public void setSecretKey(String secretKey) {
        this.secretKey = secretKey;
    }

    public int getValidationCode() {
        return validationCode;
    }

    public void setValidationCode(int validationCode) {
        this.validationCode = validationCode;
    }

    public List<Integer> getScratchCodes() {
        return scratchCodes;
    }

    public void setScratchCodes(List<Integer> scratchCodes) {
        this.scratchCodes = scratchCodes;
    }
}
