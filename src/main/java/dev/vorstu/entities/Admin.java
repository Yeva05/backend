package dev.vorstu.entities;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="admins")
@Data
@Getter
@Setter
public class Admin {
    //fields
    private String fio;
    private String phoneNumber;
    private String email;
    private String password;
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long adminId;
}
