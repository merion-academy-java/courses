package com.example.courses.student.dto.response;

import com.example.courses.student.entity.StudentEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class StudentResponse {
    protected Long id;
    protected String firstName;
    protected String lastName;
    protected String email;

    public static StudentResponse of(StudentEntity entity) {
        return StudentResponse.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .email(entity.getEmail())
                .build();
    }

}
