package by.softarex.internship.task.questionnaireportalsystem.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Questionnaire portal system API")
                                .version("1.0.0")
                                .contact(
                                        new Contact()
                                                .email("s1mpleknight@mail.ru")
                                                .url("https://vk.com/vane4kaa")
                                                .name("Zelezinsky Ivan")
                                )
                );
    }
}
