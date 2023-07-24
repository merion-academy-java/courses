package com.example.courses.lesson.dto.response;

import com.example.courses.course.dto.response.CourseResponse;
import com.example.courses.lesson.entity.LessonEntity;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class LessonResponse {
    protected Long id;
    protected String title;
    protected String description;
    protected Long courseId;

    public static LessonResponse of(LessonEntity lesson) {
        return LessonResponse.builder()
                .id(lesson.getId())
                .title(lesson.getTitle())
                .description(lesson.getDescription())
                .courseId(lesson.getCourseId())
                .build();
    }
}
