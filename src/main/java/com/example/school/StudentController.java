package com.example.school;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentRepository studentRepository;

    public StudentController(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @GetMapping
    Page<Student> findAllByFirstNameAndSurname(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String surname,
            @RequestParam(required = false, defaultValue = "0") int page,
            @RequestParam(required = false, defaultValue = "20") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") Sort.Direction sortOrder) {

        Pageable pageable;

        if (sortBy == null) {
            pageable = PageRequest.of(page, size);
        } else {
            Sort sort = Sort.by(sortBy);
            pageable = PageRequest.of(page, size, sortOrder.equals(Sort.Direction.DESC) ? sort.descending() : sort.ascending());
        }

        Page<Student> result;
        if (firstName == null && surname == null) {
            result = studentRepository.findAll(pageable);
        } else if (surname == null) {
            result = studentRepository.findAllByFirstName(firstName, pageable);
        } else if (firstName == null) {
            result = studentRepository.findAllBySurname(surname, pageable);
        } else {
            result = studentRepository.findAllByFirstNameAndSurname(firstName, surname, pageable);
        }
        return result;
    }

    @PutMapping
    public Student addStudent(@RequestBody CreateStudentDto createStudentDto){
        Student student = new Student();
        student.setId(UUID.randomUUID());
        student.setFirstName(createStudentDto.getFirstName());
        student.setSurname(createStudentDto.getSurname());
        student.setAge(createStudentDto.getAge());
        student.setFieldOfStudy(createStudentDto.getFieldOfStudy());
        student.setEmail(createStudentDto.getEmail());

        return  studentRepository.save(student);
    }
}
