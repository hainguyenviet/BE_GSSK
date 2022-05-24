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
    private String name;
    @Column(name="sex")
    private String sex;
    @Column(name="motherKey")
    private String m_key;
    @Column(name="fatherKey")
    private String f_key;
    @Column(name="wife")
    private String wife;
    @Column(name="husband")
    private String husband;
    @Column(name="attributes")
    @ElementCollection
    private List<String> attb;
    @Column(name="list_id")
    private String listID;

    public Genogram() {

    }

}