package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_illness")
public class Illness {

    @Id
    @GeneratedValue(generator="identity")
    @Column(name = "id")
    private Long id;
    @Column(name="code")
    private String code;
    @Column(name="name")
    private String name;
    @Column(name="age_detected")
    private int age_detected;
}
