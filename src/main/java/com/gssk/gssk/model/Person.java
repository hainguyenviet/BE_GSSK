package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import com.fasterxml.jackson.annotation.JsonFormat;

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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
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
}
