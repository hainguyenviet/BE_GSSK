package com.gssk.gssk.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gssk.gssk.service.RelativeService;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "tbl_genogram", uniqueConstraints = {@UniqueConstraint(name = "wife", columnNames = "wife"), @UniqueConstraint(name = "husband", columnNames = "husband")})
public class Genogram {

    @Id
    @Column(name = "id")
    private Long key;
    @Column(name="name")
    private String n;
    @Column(name="sex")
    private String s;
    @Column(name="motherKey")
    private Long m;
    @Column(name="fatherKey")
    private Long f;
    @Column(name="wife")
    private Long ux;
    @Column(name="husband")
    private Long vir;
    @Column(name="attributes")
    @Convert(converter = ListToStringConverter.class)
    private List<String> a;
    @Column(name="list_id")
    @JsonIgnore
    private String listID;

    public Genogram() {

    }

    public Genogram(Long key, String n, String s, Long m, Long f, Long ux, Long vir, List<String> a){
        this.key=key;
        this.n=n;
        this.s=s;
        this.m=m;
        this.f=f;
        this.ux=ux;
        this.vir=vir;
        this.a=a;
    }

}
