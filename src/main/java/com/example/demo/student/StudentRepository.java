package com.example.demo.student;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    // SELECT * FROM student WHERE email = ?;
    @Query("SELECT s FROM Student s WHERE s.email = ?1")
    Optional<Student> findStudentByEmail(String email);

    // SELECT * FROM students WHERE name LIKE 'prefix%';
    Optional<List<Student>> findByNameStartingWith(String prefix);

    // SELECT * FROM students WHERE email LIKE 'prefix%';
    Optional<List<Student>> findByEmailStartingWith(String prefix);

    @Query("SELECT s FROM Student s WHERE s.dob > :dob")
    Optional<List<Student>> findByBirthDateAfter(@Param("dob") LocalDate dob);

    @Query("SELECT s FROM Student s WHERE s.dob < :dob")
    Optional<List<Student>> findByBirthDateBefore(@Param("dob") LocalDate dob);
}
