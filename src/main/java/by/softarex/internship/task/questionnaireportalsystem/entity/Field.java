package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "fields")
public class Field extends UuidEntity{
    private boolean isActive;
    private boolean required;
    private String label;
    private Integer position;
    private FieldType fieldType;
    @OneToMany(mappedBy = "field")
    private List<QuestionnaireResponse> questionnaireResponses;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "field")
    private Set<FieldOption> options;
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    private Questionnaire questionnaire;
}
