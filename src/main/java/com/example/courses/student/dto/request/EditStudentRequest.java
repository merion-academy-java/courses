package com.example.courses.student.dto.request;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class EditStudentRequest {
    private String lastName;
    private String firstName;
}
