package com.gssk.gssk.Google_related;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
class user_info {
    private String username;
    private String secretKey;
    private int validationCode;
    private List<Integer> scratchCodes;
}