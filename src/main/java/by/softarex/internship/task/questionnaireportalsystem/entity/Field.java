package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;

@ToString
@Getter
@Setter
@Entity
@Table(name = "fields")
public class Field extends UuidEntity{
    @Column(name = "is_active")
    private boolean isActive;
    private boolean required;
    private String label;
    private Integer position;
    private FieldType fieldType;
    @OneToMany(mappedBy = "field")
    @ToString.Exclude
    private List<QuestionnaireResponse> questionnaireResponses;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "field")
    @ToString.Exclude
    private Set<FieldOption> options;
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    @ToString.Exclude
    private Questionnaire questionnaire;
}
