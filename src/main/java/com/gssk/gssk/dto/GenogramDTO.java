package com.gssk.gssk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenogramDTO {
    private String key;
    private String n;
    private String s;
    private String m;
    private String f;
    private String ux;
    private String vir;
    private List<String> a;
    private String listID;
}
