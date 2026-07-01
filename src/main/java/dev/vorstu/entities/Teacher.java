package dev.vorstu.entities;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name="teachers")
@Data
public class Teacher {
    //fields
    private String fio;
    private String phoneNumber;
    private String email;
    private String password;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToMany
    @JoinTable(
            name = "teacher_group", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "teacher_id"), // Ключ этой таблицы (Student)
            inverseJoinColumns = @JoinColumn(name = "group_id") // Ключ чужой таблицы (Course)
    )
    private List<Group> groups = new ArrayList<>();
}
