package com.gssk.gssk.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_health_record")
public class HealthRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;


    @Column(name = "is_twin")
    private String isTwin;


    @Column(name = "is_adopted")
    private String isAdopted;


    @Column(name = "height")
    private Integer height;


    @Column(name = "weight")
    private Integer weight;

    @Column(name = "first_period_age")
    private Integer firstPeriodAge;

    @Column(name = "birth_control")
    private Integer birthControl;

    @Column(name = "pregnant_time")
    private Integer pregnantTime;

    @Column(name = "first_born_age")
    private Integer firstBornAge;


    @Column(name = "is_smoke")
    private String isSmoke = "false";

    @Column(name = "smoke_time")
    private Integer smokeTime;

    @Column(name = "give_up_smoke_age")
    private String giveUpSmokeAge;

    @Column(name = "wine_volume")
    private Integer wineVolume;

    @Column(name = "work_out_volume")
    private Integer workOutVolume;

    @Column(name = "work_out_type")
    private String workOutType;

    @OneToMany(targetEntity = Illness.class, cascade = CascadeType.ALL)
    @JoinColumn(name = "healthID", referencedColumnName = "id")
    private List<Illness> illnessList;
}