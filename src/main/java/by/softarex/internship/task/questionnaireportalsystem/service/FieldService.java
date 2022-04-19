package by.softarex.internship.task.questionnaireportalsystem.service;

import by.softarex.internship.task.questionnaireportalsystem.repository.FieldRepository;
import by.softarex.internship.task.questionnaireportalsystem.util.EntityMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service
@Scope("singleton")
@AllArgsConstructor
public class FieldService {
    private final FieldRepository fieldRepository;
    private final EntityMapper mapper;

}
