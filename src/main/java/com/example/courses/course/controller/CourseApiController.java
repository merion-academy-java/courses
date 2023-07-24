package com.example.courses.course.controller;

import com.example.courses.course.dto.request.CreateCourseRequest;
import com.example.courses.course.dto.request.EditCourseRequest;
import com.example.courses.course.dto.response.CourseFullResponse;
import com.example.courses.course.dto.response.CourseResponse;
import com.example.courses.course.entity.CourseEntity;
import com.example.courses.course.exception.CourseNotFoundException;
import com.example.courses.course.repository.CourseRepository;
import com.example.courses.course.routes.CourseRoutes;
import com.example.courses.lesson.entity.LessonEntity;
import com.example.courses.lesson.repository.LessonRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class CourseApiController {
    private final CourseRepository courseRepository;
    private final LessonRepository lessonRepository;

    @PostMapping(CourseRoutes.CREATE)
    public CourseFullResponse create(@RequestBody CreateCourseRequest request) {
        CourseEntity entity = courseRepository.save(request.entity());
        return CourseFullResponse.of(entity, new ArrayList<>());
    }

    @GetMapping(CourseRoutes.BY_ID)
    public CourseFullResponse findById(@PathVariable Long id) {
        CourseEntity entity = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        List<LessonEntity> lessons = lessonRepository.findByCourseId(id);
        return CourseFullResponse.of(entity, lessons);
    }

    @PutMapping(CourseRoutes.BY_ID)
    public CourseFullResponse edit(@PathVariable Long id, @RequestBody EditCourseRequest request) {
        CourseEntity entity = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());

        entity = courseRepository.save(entity);

        List<LessonEntity> lessons = lessonRepository.findByCourseId(id);
        return CourseFullResponse.of(entity, lessons);
    }

    @DeleteMapping(CourseRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
        List<LessonEntity> lessons = lessonRepository.findByCourseId(id);
        for(LessonEntity lesson : lessons) lessonRepository.deleteById(lesson.getId());

        courseRepository.deleteById(id);
        return HttpStatus.OK.name();
    }

    @GetMapping(CourseRoutes.SEARCH)
    public List<CourseResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("title", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("description", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<CourseEntity> example = Example.of(
                CourseEntity.builder().title(query).title(query).build(),
                ignoringExampleMatcher);

        return courseRepository
                .findAll(example, pageable)
                .stream()
                .map(CourseResponse::of)
                .collect(Collectors.toList());
    }


}
