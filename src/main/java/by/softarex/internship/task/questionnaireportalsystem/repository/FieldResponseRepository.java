package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.Questionnaire;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldResponseRepository extends JpaRepository<FieldResponse, UUID> {
    List<FieldResponse> findAllByField(Field field);

    Boolean existsByAnswerId(UUID answerId);

    @Query(
            value = "SELECT * FROM responses r WHERE r.questionnaire_id = ?1 ORDER BY answer_id, creation_date DESC, position ASC",
            nativeQuery = true
    )
    List<FieldResponse> findAllSorted(UUID questionnaireId);

    Page<FieldResponse> findAllByQuestionnaireOrderByAnswerId(Questionnaire questionnaire, Pageable pageable);
}
