package dev.vorstu.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="teachers")
@Data
public class Teacher {
    //fields
    private String fio;
    private String phoneNumber;
    private String subject;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @ManyToMany
    @JoinTable(
            name = "teacher_group", // Имя промежуточной таблицы
            joinColumns = @JoinColumn(name = "teacher_id"), // Ключ этой таблицы (Student)
            inverseJoinColumns = @JoinColumn(name = "group_id") // Ключ чужой таблицы (Course)
    )
    private List<Group> groups = new ArrayList<>();
}
