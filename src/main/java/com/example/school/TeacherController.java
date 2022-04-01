package com.example.school;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/teachers")
public class TeacherController {

    private final TeacherRepository teacherRepository;
    private final StudentRepository studentRepository;

    public TeacherController(TeacherRepository teacherRepository, StudentRepository studentRepository) {
        this.teacherRepository = teacherRepository;
        this.studentRepository = studentRepository;
    }

    @GetMapping
    public Page<Teacher> findAllByFirstNameAndSurname(
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
            pageable = PageRequest.of(
                    page,
                    size,
                    sortOrder.equals(Sort.Direction.DESC) ? sort.descending() : sort.ascending());
        }
        Page<Teacher> result;
        if (firstName == null && surname == null) {
            result = teacherRepository.findAll(pageable);
        } else if (surname == null) {
            result = teacherRepository.findAllByFirstName(firstName, pageable);
        } else if (firstName == null) {
            result = teacherRepository.findAllBySurname(surname, pageable);
        } else {
            result = teacherRepository.findAllByFirstNameAndSurname(firstName, surname, pageable);
        }

        return result;
    }

    @PostMapping
    public Teacher addTeacher(@RequestBody CreateTeacherDto teacherDto) {
        Teacher teacher = new Teacher();
        teacher.setId(UUID.randomUUID());
        teacher.setFirstName(teacherDto.getFirstName());
        teacher.setSurname(teacherDto.getSurname());
        teacher.setAge(teacherDto.getAge());
        teacher.setSubject(teacherDto.getSubject());
        teacher.setEmail(teacherDto.getEmail());

        return teacherRepository.save(teacher);
    }

    @GetMapping("/{id}/studentAssignments")
    public List<Student> getStudentsByTeacher(@PathVariable("id") UUID teacherId) {
        return teacherRepository.findById(teacherId).map(Teacher::getStudents).get();

    }

    @PutMapping("/{id}/studentAssignments")
    public void assignStudents(@PathVariable("id") UUID teacherId, @RequestBody List<UUID> studentIds) {

        Optional<Teacher> teacherOpt = teacherRepository.findById(teacherId);

        if(teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            teacher.setStudents(
                    studentIds.stream()
                        .map(studentRepository::findById)
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .collect(Collectors.toList())
            );
            teacherRepository.save(teacher);
        }
    }

    @DeleteMapping("/{id}")
    public void deleteTeacher(@PathVariable("id") UUID teacherId) {
        teacherRepository.deleteById(teacherId);
    }
}
