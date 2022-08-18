package com.gssk.gssk.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.text.DateFormat;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_person")
public class Person {
    @Id
    @GeneratedValue(generator = "identity")
    @Column(name = "id")
    private Long id;

    @Column(name = "app_id")
    private String appID;

    @Column(name="first_name")
    private String firstName;

    @Column(name="last_name")
    private String lastName;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    @Column(name = "birthday")
    private Date dateOfBirth;

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "email")
    private String email;

    @Column(name= "phone_number")
    private String phoneNumber;

    @Column(name= "gender")
    private String gender;

    @OneToOne(targetEntity = HealthRecord.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pID", referencedColumnName = "id")
    private HealthRecord healthRecord;

    @OneToMany(targetEntity = Relative.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id", referencedColumnName = "id")
    private List<Relative> relativeList;

    @Column(name= "username")
    private String username;


//    @OneToOne(targetEntity = AppUser.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private AppUser appUser_id;

}
