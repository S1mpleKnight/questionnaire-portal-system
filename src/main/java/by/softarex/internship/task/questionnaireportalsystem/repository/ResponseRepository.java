package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ResponseRepository extends JpaRepository<Response, UUID> {
    List<Response> findAllByField(Field field);

    Boolean existsByAnswerId(UUID answerId);

    List<Response> findAllByQuestionnaireOrderByAnswerId(Questionnaire questionnaire);
}
