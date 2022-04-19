package com.gssk.gssk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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


}
