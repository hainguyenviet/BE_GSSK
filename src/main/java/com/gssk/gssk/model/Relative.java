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

    @Column(name = "id_card")
    private String idCard;

    @Column(name = "age")
    private int age;

    @Column(name = "family_order")
    private String familyOrder;

    @Column(name = "is_dead")
    private String isDead;

    @Column(name = "death_age")
    private int death_age = -1;

    @Column(name = "death_cause")
    private String deathCause;

    @OneToMany(targetEntity = Illness.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "relativeId", referencedColumnName = "relativeId")
    private List<Illness> illnessRelative;

}
