package dev.vorstu;
import dev.vorstu.models.entities.Student;
import dev.vorstu.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class Initializer {
    @Autowired
    private StudentRepository studentRepository;

    /*
    public void initial() {
        if (studentRepository.count()==0) {
            studentRepository.save(new Student("fio", "def_group", "+79"));
            studentRepository.save(new Student("fio2", "some_group", "+78"));
        }
    }
     */
}
