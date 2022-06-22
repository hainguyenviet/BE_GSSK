package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_person")
        //uniqueConstraints = {@UniqueConstraint(name = "cmnd", columnNames = "id_card"), @UniqueConstraint(name = "email", columnNames = "email"), @UniqueConstraint(name = "sdt", columnNames = "phone_number")})
public class Person implements Serializable {
    static final long serialVersionUID = 5L;
    @Id
    @GeneratedValue(generator = "identity")
    @Column(name = "id")
    private Long id;
    @NotNull
    @Column(name="first_name")
    private String firstName;
    @NotNull
    @Column(name="last_name")
    private String lastName;
    @Column(name = "birthday")
    private Date dateOfBirth;
    @NotNull
    @Column(name = "id_card")
    private String idCard;
    @Column(name = "email")
    private String email;
    @NotNull
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
}
