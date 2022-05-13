package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User extends UuidEntity{
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private String passwordHash;
    @OneToOne(mappedBy = "user")
    private Questionnaire questionnaire;
}
