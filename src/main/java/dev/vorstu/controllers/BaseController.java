package dev.vorstu.controllers;

import dev.vorstu.dto.StudentResponse;
import dev.vorstu.entities.Student;
import dev.vorstu.service.StudentService;
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
    public ResponseEntity<Student> createStudent (@RequestBody Student student) {
        Student savedStudent=studentService.createStudent(student.getFio(), student.getGroup(), student.getPhoneNumber());
        return new ResponseEntity<>(savedStudent, HttpStatus.CREATED);
    }

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.getStudentById(id));
    }

    @GetMapping("/students")
    public Page<StudentResponse> getAllStudents(){
        return studentService.getAllStudents();
    }

    @PutMapping(value="/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Student> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        Student updatedStudent=studentService.updateStudent(id, student.getFio(), student.getGroup(), student.getPhoneNumber());
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }


    @DeleteMapping(value="students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
