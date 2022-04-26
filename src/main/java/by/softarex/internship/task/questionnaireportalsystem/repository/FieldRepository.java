package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldRepository extends JpaRepository<Field, UUID> {
    List<Field> findAllByQuestionnaire_Id(UUID questionnaireId);
}
