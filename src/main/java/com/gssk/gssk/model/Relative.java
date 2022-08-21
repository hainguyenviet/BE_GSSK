package com.gssk.gssk.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tbl_relative")
public class Relative {

    @Id
    @GeneratedValue(generator = "identity")
    @Column(name = "relativeId")
    private Long rid;


    @Column(name="relation")
    private String relation;


    @Column(name="name")
    private String name;


    @Column(name = "gender")
    private String gender;


    @Column(name = "age")
    private int age;

    @Column(name = "family_order")
    private String familyOrder;

    @Column(name = "family_order_other")
    private String familyOrderOther;

    @Column(name = "is_dead")
    private String isDead;

    @Column(name = "death_age")
    private int death_age = -1;

    @Column(name = "death_cause")
    private String deathCause;

    @OneToMany(targetEntity = Illness.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "relativeId", referencedColumnName = "relativeId")
    private List<Illness> illnessRelative;

    public Relative(String relation, String name, String gender){
        this.relation = relation;
        this.name = name;
        this.gender = gender;
    }

}
