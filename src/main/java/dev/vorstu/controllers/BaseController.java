package dev.vorstu.controllers;

import dev.vorstu.service.StudentService;
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

}
