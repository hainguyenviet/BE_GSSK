package com.gssk.gssk.dto;

import com.gssk.gssk.model.Illness;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RelativeDTO {
    private String rid;
    private String relation;
    private String name;
    private String gender;
    private String age;
    private List<String> illnessName;
}
