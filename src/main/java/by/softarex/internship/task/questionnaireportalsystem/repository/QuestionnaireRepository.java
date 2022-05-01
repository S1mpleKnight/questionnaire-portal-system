package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuestionnaireRepository extends CrudRepository<Questionnaire, UUID> {
    Optional<Questionnaire> findByUser_Email(String email);

    Optional<Questionnaire> findByUser_Id(UUID uuid);
}
