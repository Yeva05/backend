package dev.vorstu.repositories;
import dev.vorstu.dto.Student;
import org.springframework.data.repository.CrudRepository;

public interface StudentRepository extends CrudRepository<Student, Long> {
    @Override
    <S extends Student> S save (S student);

    @Override
    Iterable<Student> findAll();

    @Override
    void deleteById(Long id);
}
