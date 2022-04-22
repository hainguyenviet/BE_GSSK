package com.gssk.gssk.model;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
public class Doctor extends Account{

    @NotNull
    @Column(name="name")
    private String name;

    @NotNull
    @Column(name = "gender")
    private String gender;

    @Column(name = "age")
    private int age;

    @NotNull
    @Column(name = "hospital")
    private String hospital; //benh vien noi lam viec

    @ElementCollection
    @Column(name = "evaluates")
    private List<String> evaluates;
}
