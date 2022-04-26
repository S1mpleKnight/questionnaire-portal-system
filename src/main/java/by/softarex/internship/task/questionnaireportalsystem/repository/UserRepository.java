package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserRepository extends CrudRepository<User, UUID> {
    Boolean existsByEmail(String email);

    User findByEmail(String email);
}
