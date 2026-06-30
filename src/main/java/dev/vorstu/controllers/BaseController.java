package dev.vorstu.controllers;

import dev.vorstu.dto.StudentRequest;
import dev.vorstu.dto.StudentResponse;
import dev.vorstu.entities.Student;
import dev.vorstu.service.StudentService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("api/base")
public class BaseController {

    private final StudentService studentService;

    public BaseController(StudentService studentService) {
        this.studentService=studentService;
    }

    @GetMapping("check")
    public String greetJava(){
        return "Hello world " + new Date();
    }

    //Новые методы для postgres

    @PostMapping(value="students", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> createStudent (@RequestBody StudentRequest request) {
        StudentResponse response = studentService.createStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/students")
    public Page<StudentResponse> getAllStudents(){
        return studentService.getAllStudents();
    }

    @Transactional
    @PutMapping(value="/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> updateStudent(@PathVariable Long id, @RequestBody StudentRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(id, request));
    }


    @DeleteMapping(value="students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
