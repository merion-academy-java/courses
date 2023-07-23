package com.example.courses.course.controller;

import com.example.courses.course.dto.request.CreateCourseRequest;
import com.example.courses.course.dto.request.EditCourseRequest;
import com.example.courses.course.dto.response.CourseResponse;
import com.example.courses.course.entity.CourseEntity;
import com.example.courses.course.exception.CourseNotFoundException;
import com.example.courses.course.repository.CourseRepository;
import com.example.courses.course.routes.CourseRoutes;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@AllArgsConstructor
public class CourseApiController {
    private final CourseRepository courseRepository;

    @PostMapping(CourseRoutes.CREATE)
    public CourseResponse create(@RequestBody CreateCourseRequest request) {
        CourseEntity entity = courseRepository.save(request.entity());
        return CourseResponse.of(entity);
    }

    @GetMapping(CourseRoutes.BY_ID)
    public CourseResponse findById(@PathVariable Long id)  {
        CourseEntity entity = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);
        return CourseResponse.of(entity);
    }

    @PutMapping(CourseRoutes.BY_ID)
    public CourseResponse edit(@PathVariable Long id, @RequestBody EditCourseRequest request) {
        CourseEntity entity = courseRepository.findById(id).orElseThrow(CourseNotFoundException::new);

        entity.setTitle(request.getTitle());
        entity.setDescription(request.getDescription());

        entity = courseRepository.save(entity);

        return CourseResponse.of(entity);
    }

    @DeleteMapping(CourseRoutes.BY_ID)
    public String delete(@PathVariable Long id) {
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
