package dev.vorstu.controllers;

import dev.vorstu.dto.Student;
import dev.vorstu.repositories.StudentRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("api/base")
public class BaseController {

    private Long counter=0L;

    private Long generateId(){
        return counter++;
    }

    private final StudentRepository studentRepository;

    public BaseController(StudentRepository studentRepository) {
        this.studentRepository=studentRepository;
    }

    @GetMapping("check")
    public String greetJava(){
        return "Hello world " + new Date();
    }

    //Новые методы для postgres

    @GetMapping("/students")
    public List<Student> getAllStudents() {
        return (List<Student>) studentRepository.findAll();
    }

    @PostMapping(value="students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student createStudent (@RequestBody Student student) {
        return studentRepository.save(student);
    }

    @PutMapping(value="students", produces = MediaType.APPLICATION_JSON_VALUE)
    public Student changeStudent(@RequestBody Student changingStudent) {
        return studentRepository.save(changingStudent);
    }

    @DeleteMapping(value="students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Long deleteStudent(@PathVariable("id") Long id) {
        studentRepository.deleteById(id);
        return id;
    }
}
