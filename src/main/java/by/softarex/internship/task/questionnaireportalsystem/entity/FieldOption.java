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

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name="field_options")
public class FieldOption {
    @Id
    @GeneratedValue
    private Long id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FieldOption that)) return false;
        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(value, that.value)) return false;
        return Objects.equals(field, that.field);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (field != null ? field.hashCode() : 0);
        return result;
    }
}
