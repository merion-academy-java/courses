package com.example.courses.course.repository;

import com.example.courses.course.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository  extends JpaRepository<CourseEntity, Long> {
}
