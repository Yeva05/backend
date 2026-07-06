package dev.vorstu.repositories;
import dev.vorstu.models.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Page<Student> findAll(Pageable pageable);

    Page<Student> findByGroupIdIn(List<Long> groupIds, Pageable pageable);

    Page<Student> findByGroupId(Long groupId, Pageable pageable);
    Page<Student> findByIdIn(List<Long> ids, Pageable pageable);
}
