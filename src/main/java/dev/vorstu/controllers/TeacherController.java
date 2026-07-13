package dev.vorstu.controllers;

import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.entities.User;
import dev.vorstu.services.StudentService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;

@AllArgsConstructor
@RestController
@RequestMapping("api/teachers")
public class TeacherController {
    private final StudentService studentService;

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@AuthenticationPrincipal User currentUser,@PathVariable Long id) throws AccessDeniedException {
        return ResponseEntity.ok(studentService.getStudentById(currentUser, id));
    }

    @GetMapping
    public Page<StudentResponse> getAllStudents(@AuthenticationPrincipal User currentUser,
                                                @PageableDefault(size = 10, sort = "id") Pageable pageable){
        return studentService.getAllStudents(currentUser, pageable);
    }

    @Transactional
    @PutMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> updateStudent(@AuthenticationPrincipal User currentUser,@PathVariable Long id, @RequestBody StudentRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(studentService.updateStudent(currentUser, id, request));
    }
}
