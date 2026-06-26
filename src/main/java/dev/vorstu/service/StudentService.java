package dev.vorstu.service;

import dev.vorstu.dto.Student;
import dev.vorstu.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentService {
    private final StudentRepository studentRepository;

    @Autowired
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository=studentRepository;
    }

    public Student createStudent(String fio, String group, String phoneNumber) {
        Student student=new Student(fio, group, phoneNumber);
        return studentRepository.save(student);
    }

    public Student updateStudent(Long id, String fio,  String group, String phoneNumber) {
        Student student=studentRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
        student.setFio(fio);
        student.setGroup(group);
        student.setPhoneNumber(phoneNumber);
        return studentRepository.save(student);
    }

    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public List<Student> getAllStudents(){
         return studentRepository.findAll();
    }

    public Optional<Student> getStudentById(Long id) {
        return studentRepository.findById(id);
    }

}
