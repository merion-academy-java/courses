package com.example.courses.course.dto.response;

import com.example.courses.course.entity.CourseEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CourseResponse extends CourseEntity {
    public static CourseResponse of(CourseEntity entity){
        return CourseResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .build();
    }
}
