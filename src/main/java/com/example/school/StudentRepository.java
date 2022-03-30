package com.example.school;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, UUID> {

    Page<Student> findAllByFirstNameAndSurname(String firstName, String surname, Pageable pageable);

    Page<Student> findAllByFirstName(String firstName, Pageable pageable);

    Page<Student> findAllBySurname(String surname, Pageable pageable);
}
