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

@Getter
@Setter
@Entity
@Table(name="field_options")
@ToString
@NoArgsConstructor
public class FieldOption {
    @Id
    @GeneratedValue
    @ToString.Exclude
    private Long id;
    private String value;
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;

    public FieldOption(String value) {
        this.value = value;
    }
}
