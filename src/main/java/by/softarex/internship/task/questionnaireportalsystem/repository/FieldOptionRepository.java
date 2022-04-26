package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Field;
import by.softarex.internship.task.questionnaireportalsystem.entity.FieldOption;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface FieldOptionRepository extends CrudRepository<FieldOption, UUID> {
    List<FieldOption> findAllByField(Field field);
}
