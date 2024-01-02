package com.example.demo.student;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "api/v1/student")
public class StudentController {

    public final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public List<Student> getStudents(
            @RequestParam(required = false) Long id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email,
            @RequestParam(required = false) String dobAfter,
            @RequestParam(required = false) String dobBefore
    ) {
        if (id != null && name == null && email == null && dobAfter == null && dobBefore == null) {
            return studentService.getStudentById(id);
        }
        if (id == null && name != null && email == null && dobAfter == null && dobBefore == null) {
            return studentService.getStudentsByNameStartingWith(name);
        }
        if (id == null && name == null && email != null && dobAfter == null && dobBefore == null) {
            return studentService.getStudentsByEmailStartingWith(email);
        }
        if (id == null && name == null && email == null && dobAfter != null && dobBefore == null) {
            LocalDate parsedDobAfter = LocalDate.parse(dobAfter);
            return studentService.getStudentsByDobAfter(parsedDobAfter);
        }
        if (id == null && name == null && email == null && dobAfter == null && dobBefore != null) {
            LocalDate parsedDobBefore = LocalDate.parse(dobBefore);
            return studentService.getStudentsByDobBefore(parsedDobBefore);
        }
        if (id == null && name == null && email == null && dobAfter != null && dobBefore != null) {
            LocalDate parsedDobBefore = LocalDate.parse(dobBefore);
            LocalDate parsedDobAfter = LocalDate.parse(dobAfter);
            List<Student> listDobBefore = studentService.getStudentsByDobBefore(parsedDobBefore);
            List<Student> listDobAfter = studentService.getStudentsByDobAfter(parsedDobAfter);

            return listDobBefore.stream()
                    .filter(listDobAfter::contains)
                    .collect(Collectors.toList());
        }
        if (id == null && name == null && email == null && dobAfter == null && dobBefore == null) {
            return studentService.getStudents();
        }
        throw new IllegalArgumentException("this combination of arguments is prohibited");
    }

    @PostMapping
    public void registerNewStudent(@RequestBody Student student) {
        studentService.addNewStudent(student);
    }

    @DeleteMapping(path = "{studentId}")
    public void deleteStudent(@PathVariable("studentId") Long studentId) {
        studentService.deleteStudent(studentId);
    }

    @PutMapping(path = "{studentId}")
    public void updateStudent(
            @PathVariable("studentId") Long studentId,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String email) {
        studentService.updateStudent(studentId, name, email);
    }
}
