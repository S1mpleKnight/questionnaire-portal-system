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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "questionnaires")
public class Questionnaire extends UuidEntity{
    @OneToOne
    @ToString.Exclude
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "questionnaire")
    @ToString.Exclude
    private List<Field> fields;
    @OneToMany(mappedBy = "questionnaire")
    @ToString.Exclude
    private List<QuestionnaireResponse> questionnaireResponses;
}
