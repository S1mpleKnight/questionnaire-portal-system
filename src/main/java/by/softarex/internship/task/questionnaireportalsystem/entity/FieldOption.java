package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;

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
public class FieldOption {
    @Id
    @GeneratedValue
    private Long id;
    private String value;
    @ManyToOne
    @JoinColumn(name = "field_id")
    private Field field;
}
