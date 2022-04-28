package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.UUID;

@Setter
@Getter
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    @Column(name = "phone_number")
    private String phone;
    @Column(name = "password_hash")
    private String passwordHash;
    @OneToOne(mappedBy = "user")
    @ToString.Exclude
    private Questionnaire questionnaire;
}
