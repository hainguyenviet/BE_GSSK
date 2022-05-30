package com.gssk.gssk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenogramDTO {
    private Long key;
    private String n;
    private String s;
    private Long m;
    private Long f;
    private Long ux;
    private Long vir;
    private List<String> a;
    private String listID;
}
