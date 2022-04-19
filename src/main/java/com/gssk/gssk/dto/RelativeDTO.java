package com.gssk.gssk.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeDTO {
    private String rid;
    private String relation;
    private String name;
    private String gender;
    private String age;
}
