package com.example.demo.student;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getStudents() {
        return studentRepository.findAll();
    }

    public void addNewStudent(Student student) {
        Optional<Student> studentOptional = studentRepository.findStudentByEmail(student.getEmail());
        if (studentOptional.isPresent()) {
            throw new IllegalStateException("email taken");
        }
        studentRepository.save(student);
    }

    public void deleteStudent(Long studentId) {
        boolean exists = studentRepository.existsById(studentId);
        if (exists) {
            studentRepository.deleteById(studentId);
        } else {
            throw new IllegalStateException("student with id " + studentId + " does not exist");
        }
    }
    @Transactional
    public void updateStudent(Long studentId, String name, String email) {
        Student student = studentRepository
                .findById(studentId)
                .orElseThrow(() -> new IllegalStateException("student with " + studentId + " does not exist"));

        if (name != null && !name.isEmpty() && !Objects.equals(student.getName(), name)) {
            student.setName(name);
        }

        if (email != null && !email.isEmpty() && !Objects.equals(student.getEmail(), email)) {
            // email should not be already taken
            Optional<Student> studentOptional = studentRepository.findStudentByEmail(email);
            if (studentOptional.isPresent()) {
                throw new IllegalStateException("email taken");
            }
            student.setEmail(email);
        }
    }

    public List<Student> getStudentById(Long id) {
        Student student = studentRepository
                .findById(id)
                .orElseThrow(() -> new IllegalStateException("student with " + id + " does not exist"));
        return List.of(student);
    }

    public List<Student> getStudentsByNameStartingWith(String prefix) {
        return studentRepository
                .findByNameStartingWith(prefix)
                .orElseThrow(() -> new IllegalStateException(
                        "students with names starting with " + prefix + " do not exist"
                ));
    }

    public List<Student> getStudentsByEmailStartingWith(String prefix) {
        return studentRepository
                .findByEmailStartingWith(prefix)
                .orElseThrow(() -> new IllegalStateException(
                        "students with emails starting with " + prefix + " do not exist"
                ));
    }

    public List<Student> getStudentsByDobAfter(LocalDate dobAfter) {
        return studentRepository
                .findByBirthDateAfter(dobAfter)
                .orElseThrow(() -> new IllegalStateException(
                        "students with date of birth after " + dobAfter + " do not exist"
                ));
    }

    public List<Student> getStudentsByDobBefore(LocalDate dobBefore) {
        return studentRepository
                .findByBirthDateBefore(dobBefore)
                .orElseThrow(() -> new IllegalStateException(
                        "students with date of birth before " + dobBefore + " do not exist"
                ));
    }

}
