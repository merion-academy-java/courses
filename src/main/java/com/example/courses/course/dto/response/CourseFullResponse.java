package com.example.courses.course.dto.response;

import com.example.courses.course.entity.CourseEntity;
import com.example.courses.lesson.dto.response.LessonResponse;
import com.example.courses.lesson.entity.LessonEntity;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@SuperBuilder
public class CourseFullResponse extends CourseEntity {
    private List<LessonResponse> lessons = new ArrayList<>();

    public static CourseFullResponse of(CourseEntity entity, List<LessonEntity> lessons) {
        return CourseFullResponse.builder()
                .id(entity.getId())
                .title(entity.getTitle())
                .description(entity.getDescription())
                .lessons(lessons.stream().map(LessonResponse::of).collect(Collectors.toList()))
                .build();
    }
}
