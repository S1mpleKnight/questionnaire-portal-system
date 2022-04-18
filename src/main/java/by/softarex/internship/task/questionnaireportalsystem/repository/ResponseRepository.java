package by.softarex.internship.task.questionnaireportalsystem.repository;

import by.softarex.internship.task.questionnaireportalsystem.entity.Response;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ResponseRepository extends JpaRepository<Response, UUID> {
}
