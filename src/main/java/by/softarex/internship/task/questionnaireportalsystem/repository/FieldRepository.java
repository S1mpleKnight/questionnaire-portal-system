package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findAllByQuestionnaire_IdOrderByPositionAsc(UUID questionnaireId);

    Page<Field> findAllByQuestionnaire_IdOrderByPositionAsc(UUID questionnaireId, Pageable pageable);

    Integer countAllByQuestionnaire(Questionnaire questionnaire);
}
