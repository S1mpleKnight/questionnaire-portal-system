package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.QuestionnaireResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface QuestionnaireResponseRepository extends JpaRepository<QuestionnaireResponse, UUID> {
    List<QuestionnaireResponse> findAllByField(Field field);

    Boolean existsByAnswerId(UUID answerId);

    @Query(
            value = "SELECT * FROM responses r WHERE r.questionnaire_id = ?1 ORDER BY answer_id, creation_date DESC, position ASC",
            nativeQuery = true
    )
    List<QuestionnaireResponse> findAllSorted(UUID questionnaireId);

    Page<QuestionnaireResponse> findAllByQuestionnaireOrderByAnswerId(Questionnaire questionnaire, Pageable pageable);
}
