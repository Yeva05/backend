package dev.vorstu.controllers;
import dev.vorstu.models.dto.*;
import dev.vorstu.models.dto.admin.AdminRequest;
import dev.vorstu.models.dto.admin.AdminResponse;
import dev.vorstu.models.dto.group.GroupRequest;
import dev.vorstu.models.dto.group.GroupResponse;
import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.dto.teacher.TeacherRequest;
import dev.vorstu.models.dto.teacher.TeacherResponse;
import dev.vorstu.models.entities.User;
import dev.vorstu.services.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.AccessDeniedException;


//TODO CRUD for user minus create,


@AllArgsConstructor
@RestController
@RequestMapping("/api/admins")
public class AdminController {

    private final AdminService adminService;
    private final TeacherService teacherService;
    private final StudentService studentService;
    private final GroupService groupService;
    private final UserService userService;

    //CRUD for Users

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser (@RequestBody SignUpRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //CRUD for students

    @GetMapping("/students/{id}")
    public ResponseEntity<StudentResponse> getStudentById(@AuthenticationPrincipal User currentUser, @PathVariable Long id) throws AccessDeniedException {
        return ResponseEntity.ok(studentService.getStudentById(currentUser, id));
    }

    @GetMapping("/students")
    public Page<StudentResponse> getAllStudents(@AuthenticationPrincipal User currentUser,
                                                @PageableDefault(size = 10, sort = "id") Pageable pageable){
        return studentService.getAllStudents(currentUser, pageable);
    }

    @Transactional
    @PutMapping(value="/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<StudentResponse> updateStudent(@AuthenticationPrincipal User currentUser, @PathVariable Long id, @RequestBody StudentRequest request) throws AccessDeniedException {
        return ResponseEntity.ok(studentService.updateStudent(currentUser, id, request));
    }


    @DeleteMapping(value="/students/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteStudent(@PathVariable("id") Long id) {
        studentService.deleteStudent(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    //CRUD for teachers

    @GetMapping("/teachers/{id}")
    public ResponseEntity<TeacherResponse> getTeacherById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.getTeacherById(id));
    }

    @GetMapping("/teachers")
    public Page<TeacherResponse> getAllTeachers(){
        return teacherService.getAllTeachers();
    }

    @Transactional
    @PutMapping(value="/teachers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherResponse> updateTeacher(@PathVariable Long id, @RequestBody TeacherRequest request) {
        return ResponseEntity.ok(teacherService.updateTeacher(id, request));
    }

    @DeleteMapping(value="/teachers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteTeacher(@PathVariable("id") Long id) {
        teacherService.deleteTeacher(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @Transactional
    @PutMapping(value="/{teacherId}/groups/{groupId}", produces =MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TeacherResponse> assignTeacherToGroup(@PathVariable Long teacherId, @PathVariable Long groupId) {
        return ResponseEntity.ok(teacherService.assignTeacherToGroup(groupId, teacherId));
    }

    //CRUD for groups

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(value="/groups", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupResponse> createGroup (@RequestBody GroupRequest request) {
        GroupResponse response = groupService.createGroup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/groups/{id}")
    public ResponseEntity<GroupResponse> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(groupService.getGroupById(id));
    }

    @GetMapping("/groups")
    public Page<GroupResponse> getAllGroups(){
        return groupService.getAllGroups();
    }

    @Transactional
    @PutMapping(value="/groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<GroupResponse> updateGroup(@PathVariable Long id, @RequestBody GroupRequest request) {
        return ResponseEntity.ok(groupService.updateGroup(id, request));
    }

    @DeleteMapping(value="/groups/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        groupService.deleteGroup(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    //CRUD for admins

    @GetMapping("/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @GetMapping
    public Page<AdminResponse> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @Transactional
    @PutMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Long id, @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.updateAdmin(id, request));
    }

    @DeleteMapping(value="/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
