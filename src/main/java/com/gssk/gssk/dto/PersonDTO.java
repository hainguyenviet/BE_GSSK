package com.gssk.gssk.dto;

import com.gssk.gssk.model.Person;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
    private String id;
    private String firstName;
    private String lastName;
    private Date birthDay;
    private String idCard;
    private String email;
    private String phoneNumber;
    private String gender;

    private Person person;
}
