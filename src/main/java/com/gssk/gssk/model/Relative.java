package com.gssk.gssk.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_relative")
public class Relative {

    @Id
    @GeneratedValue(generator = "identity")
    @Column(name = "relativeId")
    private Long rid;

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
    @Convert(converter = ListToStringConverter.class)
    private List<String> illnessName;


    @Column(name = "age_detected")
    @Convert(converter = ListToStringConverter.class)
    private List<String> age_detected;

    @Column(name = "death_age")
    private int death_age = -1;

    @Column(name = "death_cause")
    private String deathCause;
}
