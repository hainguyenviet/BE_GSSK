package com.gssk.gssk.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Data
@Entity
@Table(name = "tbl_relative")
public class Relative {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "relativeId")
    private String rid;

    @NotNull
    @Column(name="relation")
    private String relation;

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @Column(name = "illness_name")
    private String illnessName;

    @Column(name = "age_detected")
    private int age_detected;

    @Column(name = "death_age")
    private int death_age;

    @Column(name = "death_cause")
    private String deathCause;
}
