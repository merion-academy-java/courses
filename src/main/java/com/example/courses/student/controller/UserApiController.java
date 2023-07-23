package com.example.courses.student.controller;

import com.example.courses.student.dto.request.EditStudentRequest;
import com.example.courses.student.dto.request.RegistrationRequest;
import com.example.courses.student.dto.response.StudentResponse;
import com.example.courses.student.entity.StudentEntity;
import com.example.courses.base.exceptions.BadRequestException;
import com.example.courses.student.exceptions.StudentAlreadyExistException;
import com.example.courses.student.exceptions.StudentNotFoundException;
import com.example.courses.student.repository.StudentRepository;
import com.example.courses.student.routes.StudentRoutes;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class UserApiController {

    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    @Value("${init.email}")
    private String initUser;
    @Value("${init.password}")
    private String initPassword;

    @GetMapping(StudentRoutes.INIT)
    public StudentResponse init() {
        Optional<StudentEntity> checkUser = studentRepository.findByEmail(initUser);
        StudentEntity student;
        if (checkUser.isEmpty()) {
            student = StudentEntity.builder()
                    .firstName("Default")
                    .lastName("Default")
                    .email(initUser)
                    .password(passwordEncoder.encode(initPassword))
                    .build();
            student = studentRepository.save(student);
        } else {
            student = checkUser.get();
        }

        return StudentResponse.of(student);
    }

    @PostMapping(StudentRoutes.REGISTRATION)
    public StudentResponse registration(@RequestBody RegistrationRequest request) throws BadRequestException, StudentAlreadyExistException {
        request.validate();

        Optional<StudentEntity> check = studentRepository.findByEmail(request.getEmail());
        if(check.isPresent()) throw new StudentAlreadyExistException();

        StudentEntity student = StudentEntity.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        student = studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @PutMapping(StudentRoutes.EDIT)
    public StudentResponse edit(Principal principal, @RequestBody EditStudentRequest request) {
        StudentEntity student = studentRepository
                .findByEmail(principal.getName())
                .orElseThrow(StudentNotFoundException::new);
        student.setFirstName(request.getFirstName());
        student.setLastName(request.getLastName());

        student = studentRepository.save(student);
        return StudentResponse.of(student);
    }

    @GetMapping(StudentRoutes.BY_ID)
    public StudentResponse byId(@PathVariable Long id) {
        return studentRepository
                .findById(id)
                .map(StudentResponse::of)
                .orElseThrow(StudentNotFoundException::new);
    }

    @GetMapping(StudentRoutes.SEARCH)
    public List<StudentResponse> search(
            @RequestParam(defaultValue = "") String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);

        ExampleMatcher ignoringExampleMatcher = ExampleMatcher.matchingAny()
                .withMatcher("lastName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
                .withMatcher("firstName", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());

        Example<StudentEntity> example = Example.of(
                StudentEntity.builder().lastName(query).firstName(query).build(),
                ignoringExampleMatcher);

        return studentRepository
                .findAll(example, pageable)
                .stream()
                .map(StudentResponse::of)
                .collect(Collectors.toList());
    }

}
