package com.gssk.gssk.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_relative")
public class Relative extends Person {

    @ManyToOne
    @JoinColumn(name = "pid")
    private Person pid;
    
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "relativeId")
    private String rid;

    @Column(name="relation")
    private String relation;

    @Column(name="name")
    private String name;
}
