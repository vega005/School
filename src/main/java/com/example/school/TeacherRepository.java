package com.example.school;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherRepository extends PagingAndSortingRepository<Teacher, UUID> {

    Page<Teacher> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

    Page<Teacher> findAllByFirstName(String firstName, Pageable pageable);

    Page<Teacher> findAllBySurname(String surname, Pageable pageable);

    List<Teacher> findAllByStudentsId(UUID studentId);
}
