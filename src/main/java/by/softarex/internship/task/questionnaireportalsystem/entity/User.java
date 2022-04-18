package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Setter
@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue
    private UUID id;
    private String firstname;
    private String lastname;
    private String email;
    private String login;
    @Column(name = "password_hash")
    private String password;
    @OneToOne
    @JoinColumn(name = "questionnaire_id", referencedColumnName = "id")
    @ToString.Exclude
    private Questionnaire questionnaire;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!Objects.equals(id, user.id)) return false;
        if (!Objects.equals(firstname, user.firstname)) return false;
        if (!Objects.equals(lastname, user.lastname)) return false;
        if (!Objects.equals(email, user.email)) return false;
        if (!Objects.equals(login, user.login)) return false;
        return Objects.equals(password, user.password);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (login != null ? login.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        return result;
    }
}
