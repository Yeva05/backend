package dev.vorstu.entities;

import jakarta.persistence.*;

@Entity
@Table(name="students")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    private String fio;

    @Column (name="group_of_students")
    @ManyToOne
    @JoinColumn(name = "group_id", nullable = false)
    private Group group;

    private String phoneNumber;

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;



    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio=fio;
    }

    public Group getGroup(){
        return group;
    }
    public void setGroup(Group group){
        this.group=group;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber=phoneNumber;
    }







}
