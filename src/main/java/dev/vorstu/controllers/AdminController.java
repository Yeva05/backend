package dev.vorstu.controllers;
import dev.vorstu.models.dto.*;
import dev.vorstu.models.dto.admin.AdminRequest;
import dev.vorstu.models.dto.admin.AdminResponse;
import dev.vorstu.models.dto.auth.AuthResponse;
import dev.vorstu.models.dto.group.GroupRequest;
import dev.vorstu.models.dto.group.GroupResponse;
import dev.vorstu.models.dto.student.StudentRequest;
import dev.vorstu.models.dto.student.StudentResponse;
import dev.vorstu.models.dto.teacher.TeacherRequest;
import dev.vorstu.models.dto.teacher.TeacherResponse;
import dev.vorstu.service.*;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

//TODO assign teacher to group method, CRUD for user minus create, ADD PERMISSION ANNOTATIONS


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

    @PostMapping(value="/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserResponse> createUser (@RequestBody SignUpRequest request) {
        UserResponse response = userService.createUser(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    //CRUD for students

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

    //CRUD for groups

    @PostMapping(value="groups", produces = MediaType.APPLICATION_JSON_VALUE)
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

    @PostMapping(value="assignations")

    //CRUD for admins

    @GetMapping("/admins/{id}")
    public ResponseEntity<AdminResponse> getAdminById(@PathVariable Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @GetMapping("/admins")
    public Page<AdminResponse> getAllAdmins(){
        return adminService.getAllAdmins();
    }

    @Transactional
    @PutMapping(value="/admins/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AdminResponse> updateAdmin(@PathVariable Long id, @RequestBody AdminRequest request) {
        return ResponseEntity.ok(adminService.updateAdmin(id, request));
    }

    @DeleteMapping(value="/admins/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> deleteAdmin(@PathVariable("id") Long id) {
        adminService.deleteAdmin(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
