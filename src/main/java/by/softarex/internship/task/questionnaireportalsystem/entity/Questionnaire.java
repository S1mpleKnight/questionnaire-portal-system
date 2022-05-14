package by.softarex.internship.task.questionnaireportalsystem.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "questionnaires")
public class Questionnaire extends UuidEntity{
    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire")
    private List<Field> fields;
    @OneToMany(mappedBy = "questionnaire")
    private List<QuestionnaireResponse> questionnaireReponses;
}
