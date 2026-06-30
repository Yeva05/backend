package dev.vorstu.service;

import dev.vorstu.dto.StudentRequest;
import dev.vorstu.dto.StudentResponse;
import dev.vorstu.entities.Student;
import dev.vorstu.mappers.StudentMapper;
import dev.vorstu.repositories.StudentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;

    public StudentResponse createStudent(StudentRequest studentRequest) {
        Student student=studentMapper.toStudent(studentRequest);
        Student saved = studentRepository.save(student);
        return studentMapper.toStudentResponse(saved);
    }

    public StudentResponse updateStudent(Long id, StudentRequest request) {
        Student existing=studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        studentMapper.updateEntity(request, existing);
        Student updated =studentRepository.save(existing);
        return studentMapper.toStudentResponse(updated);
    }


    public void deleteStudent(Long id) {
        studentRepository.deleteById(id);
    }

    public Page<StudentResponse> getAllStudents(){
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id"));
        Page<Student> page = studentRepository.findAll(pageable);
         return page.map(studentMapper::toStudentResponse);
    }

    public StudentResponse getStudentById(Long id) {
        Student student= studentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Student not found"));
        return studentMapper.toStudentResponse(student);

    }

}
