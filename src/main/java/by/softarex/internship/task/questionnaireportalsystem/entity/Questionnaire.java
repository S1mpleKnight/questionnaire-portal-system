package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "questionnaires")
public class Questionnaire {
    @Id
    @GeneratedValue
    private UUID id;
    @OneToOne(mappedBy = "questionnaire")
    @ToString.Exclude
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire")
    @ToString.Exclude
    private Set<Field> fields;
    @OneToMany(mappedBy = "questionnaire")
    @ToString.Exclude
    private List<Response> responses;
}
