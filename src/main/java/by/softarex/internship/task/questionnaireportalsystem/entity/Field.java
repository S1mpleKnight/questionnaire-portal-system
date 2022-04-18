package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
public class Field {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "is_active")
    private boolean isActive;
    private boolean required;
    private String label;
    private FieldType fieldType;
    @OneToMany(mappedBy = "field")
    @ToString.Exclude
    private List<Response> responses;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "field")
    @ToString.Exclude
    private Set<FieldOption> options;
    @ManyToOne
    @JoinColumn(name = "questionnaire_id")
    @ToString.Exclude
    private Questionnaire questionnaire;
}
