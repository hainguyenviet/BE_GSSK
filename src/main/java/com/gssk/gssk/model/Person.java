package com.gssk.gssk.model;

import lombok.Data;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_person")
public class Person {
    @Id
    @GeneratedValue(generator = "identity")
    @Column(name = "id")
    private Long id;

    @Column(name="first_name", nullable = false)
    private String firstName;

    @Column(name="last_name", nullable = false)
    private String lastName;

    @Column(name = "birthday", nullable = false)
    private Date dateOfBirth;

    @Column(name = "id_card", nullable = false)
    private String idCard;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name= "phone_number", nullable = false)
    private String phoneNumber;

    @Column(name= "gender", nullable = false)
    private String gender;

    @OneToOne(targetEntity = HealthRecord.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "pID", referencedColumnName = "id")
    private HealthRecord healthRecord;

    @OneToMany(targetEntity = Relative.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_id", referencedColumnName = "id")
    private List<Relative> relativeList;

    @Column(name= "user_id")
    private String userId;

//    @OneToOne(targetEntity = AppUser.class, cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
//    private AppUser appUser_id;

}
