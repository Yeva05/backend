package dev.vorstu.models.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name="students")
@Data
public class Student {
    public Student(){};

    public Student(String fio, Group group, String phoneNumber) {
        this.fio=fio;
        this.group=group;
        this.phoneNumber=phoneNumber;
    }

    public Student(Long id, String fio, Group group, String phoneNumber) {
        this(fio, group, phoneNumber);
        this.id=id;
    }

    private String fio;

    @ManyToOne
    @JoinColumn(name = "group_id", nullable = true)
    private Group group;

    private String phoneNumber;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    Long getGroup(Student student) {
        return student.group.getId();
    }


}
