package dev.vorstu.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="admins")
@Data
@Getter
@Setter
public class Group {
    private String name;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToMany(mappedBy = "groups")
    private List<Teacher> teachers = new ArrayList<>();
}
