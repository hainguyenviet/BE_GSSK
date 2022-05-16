package com.gssk.gssk.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.*;

@Data
@Entity
@Table(name = "tbl_account", uniqueConstraints = {@UniqueConstraint(name = "email_acc", columnNames = "email")})
public class Account {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid")
    @Column(name = "id")
    @Schema(accessMode = Schema.AccessMode.READ_ONLY)
    private String id;

    @NotNull
    @Column(name="email")
    private String email;

    @NotNull
    @Column(name="password")
    private String password;

    @NotNull
    @Column(name="fullname")
    private String fullname;
}
