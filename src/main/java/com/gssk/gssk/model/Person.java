package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_person")
public class Person {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Column(name="first_name")
    private String firstName;
    @Column(name="last_name")
    private String lastName;
    @Column(name = "birthday")
    private Date birthDay;
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "email")
    private String email;
    @Column(name= "phone_number")
    private String phoneNumber;
    @Column(name= "gender")
    private String gender;

    @OneToMany
    private List<Relative> relativeList;

    @OneToOne
    private HealthRecord healthRecord;




}
