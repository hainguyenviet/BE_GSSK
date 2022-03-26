package com.gssk.gssk.model;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "tbl_relative")
public class Relative extends Person {

    @ManyToOne
    @JoinColumn(name = "pid")
    private Person pid;

    @Column(name="relation")
    private String relation;

    @Column(name="name")
    private String name;
}
