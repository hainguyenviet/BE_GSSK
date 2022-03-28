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
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;
    @Column(name="code")
    private String code;
    @Column(name="name")
    private String name;
    @ManyToOne
    @JoinColumn(name="hid")
    private HealthRecord hid;
    @Column(name="afflictions")
    private String Afflictions;
    //OM = only males, OF =only females, DG= diagonal gender, NS= no sparing, UN= Unknown
    @Column(name="rate")
    private float rate;
    //I don't know if I should add type to know illness that conflict to each other in genetics
    //This addition might help me to do genome tree more easily(affliction)

}
