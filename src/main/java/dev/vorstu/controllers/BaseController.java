package dev.vorstu.controllers;

import dev.vorstu.dto.Student;
import dev.vorstu.service.StudentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

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
    public ResponseEntity<Student> getStudentById(@PathVariable Long id) {
        return studentService.getStudentById(id).map(student -> new ResponseEntity<>(student, HttpStatus.OK)).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/students")
    public List<Student> getAllStudents(){
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
