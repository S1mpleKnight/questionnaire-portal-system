package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "responses")
public class Response {
    @Id
    @GeneratedValue
    private UUID id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Response response)) return false;
        if (!Objects.equals(id, response.id)) return false;
        if (!Objects.equals(questionnaire, response.questionnaire))
            return false;
        if (!Objects.equals(value, response.value)) return false;
        return Objects.equals(field, response.field);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (questionnaire != null ? questionnaire.hashCode() : 0);
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}
