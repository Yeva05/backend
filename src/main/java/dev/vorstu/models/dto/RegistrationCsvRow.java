package dev.vorstu.models.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;


@JsonPropertyOrder({"userName", "email", "role", "groupName"})
public class RegistrationCsvRow {
    @JsonProperty("userName")
    private String userName;
    @JsonProperty("email")
    private String email;

    @JsonProperty("role")
    private String role;

    @JsonProperty("fio")
    private String fio;

    @JsonProperty("phoneNumber")
    private String phoneNumber;

    @JsonProperty("groupId")
    private String groupId;

    @JsonProperty("subject")
    private String subject;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFio() {
        return fio;
    }

    public void setFio(String fio) {
        this.fio = fio;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }
}

