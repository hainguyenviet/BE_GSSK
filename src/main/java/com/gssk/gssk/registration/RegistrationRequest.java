package com.gssk.gssk.registration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RegistrationRequest {
    private final String fullName;
    private final String email;
    private final String password;
}
