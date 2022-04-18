package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(mappedBy = "questionnaire")
    @ToString.Exclude
    private User user;
    @OneToMany(mappedBy = "questionnaire")
    @ToString.Exclude
    private Set<Field> fields;
    @OneToMany(mappedBy = "questionnaire")
    @ToString.Exclude
    private List<Response> responses;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Questionnaire that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
