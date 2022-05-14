package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name="field_options")
@NoArgsConstructor
public class FieldOption extends UuidEntity{
    private String value;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    public FieldOption(String value) {
        this.value = value;
    }
}
