package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_health_record")
public class HealthRecord {

    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotNull
    @Column(name = "is_twin")
    private Boolean isTwin;

    @NotNull
    @Column(name = "is_adopted")
    private Boolean isAdopted;

    @NotNull
    @Column(name = "height")
    private Integer height;

    @NotNull
    @Column(name = "weight")
    private Integer weight;

    @NotNull
    @Column(name = "is_parent_relative")
    private Boolean isParentRelative;

    @Column(name = "first_period_age")
    private Integer firstPeriodAge;

    @Column(name = "birth_control")
    private Integer birthControl;

    @Column(name = "pregnant_time")
    private Integer pregnantTime;

    @Column(name = "first_born_age")
    private Integer firstBornAge;

    @NotNull
    @Column(name = "is_smoke")
    private Boolean isSmoke;

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
