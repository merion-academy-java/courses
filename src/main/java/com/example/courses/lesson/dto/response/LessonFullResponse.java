package com.example.courses.lesson.dto.response;

import com.example.courses.course.dto.response.CourseResponse;
import com.example.courses.course.entity.CourseEntity;
import com.example.courses.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonFullResponse {
    protected Long id;
    protected String title;
    protected String description;
    protected Long courseId;
    protected CourseResponse course;

    public static LessonFullResponse of(LessonEntity lesson, CourseEntity course) {
        return LessonFullResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .courseId(lesson.getCourseId())
                .course(course != null ? CourseResponse.of(course) : null)
                .build();
    }
}
