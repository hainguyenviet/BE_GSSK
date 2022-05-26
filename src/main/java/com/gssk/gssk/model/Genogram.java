package com.gssk.gssk.model;

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
    @Column(name = "key")
    private String key;
    @Column(name="name")
    private String n;
    @Column(name="sex")
    private String s;
    @Column(name="motherKey")
    private String m;
    @Column(name="fatherKey")
    private String f;
    @Column(name="wife")
    private String ux;
    @Column(name="husband")
    private String vir;
    @Column(name="attributes")
    @Convert(converter = ListToStringConverter.class)
    private List<String> a;
    @Column(name="list_id")
    private String listID;

    public Genogram() {

    }

}