package com.gssk.gssk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GenogramDTO {
    private String id;
    private String name;
    private String sex;
    private String m_key;
    private String f_key;
    private String wife;
    private String husband;
    private List<String> attb;
}