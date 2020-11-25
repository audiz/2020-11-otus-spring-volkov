package ru.otus.work1;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.work1.domain.Answer;
import ru.otus.work1.domain.Question;
import ru.otus.work1.service.QuestionService;


public class Main {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("/spring-context.xml");
        QuestionService service = context.getBean(QuestionService.class);

        Question question;
        while ((question = service.getNextQuestion()) != null) {
            System.out.println((question.getNumber()) + ") " + question.getQuestion());
            for(Answer answer: question.getAnswers()) {
                System.out.println("\t" + answer.getNumber() + " - " + answer.getAnswer());
            }
        }
    }
}
