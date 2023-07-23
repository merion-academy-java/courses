package com.example.courses.course.dto.request;

import com.example.courses.course.entity.CourseEntity;
import lombok.Data;

@Data
public class EditCourseRequest {
    private Long id;
    private String title;
    private String description;
}
