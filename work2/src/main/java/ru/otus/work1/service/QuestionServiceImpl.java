package ru.otus.work1.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.otus.work1.dao.QuestionDao;
import ru.otus.work1.domain.Answer;
import ru.otus.work1.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String ANSWER_SEPARATOR = "\\s*,\\s*";
    private final QuestionDao dao;
    private final InputService inputService;
    private int questionNumber;
    private List<Question> answeredList;

    @Autowired
    public QuestionServiceImpl(QuestionDao dao, InputService inputService) {
        this.dao = dao;
        this.inputService = inputService;
    }

    @Override
    public void doQuestions() {
        askFio();
        makeQuestions();
        prepareResults();

    }

    private void askFio() {
        String firstName = inputService.getInput("What is your First Name?");
        String lastName = inputService.getInput("What is your Last Name?");
        System.out.println("Hello " + firstName + " " + lastName + "!");
    }

    private void makeQuestions(){
        answeredList = new ArrayList<>();
        System.out.println("Please answer next questions: (use comma for multiple choice answers)");

        Question question;
        while ((question = dao.getNextQuestion()) != null) {
            question.setNumber(++questionNumber);
            System.out.println((question.getNumber()) + ") " + question.getQuestion());
            if(question.getAnswers().size() == 1) {
                String answer = inputService.getInput("Type your answer: ");
                compareAnswers(answer, question.getAnswers());
            } else {
                for(Answer answer: question.getAnswers()) {
                    System.out.println("\t" + answer.getNumber() + " - " + answer.getAnswer() +  " - " + answer.isCorrect());
                }
                String answer = inputService.getInput("Your answer is: ");
                compareAnswers(answer, question.getAnswers());
            }
            answeredList.add(question);
        }
    }

    private void prepareResults() {
        double prc = answeredList.stream().mapToDouble(Question::getCorrectPercents).average().orElse(Double.NaN);
        System.out.println("Correct answers is " + prc + "%");
    }

    private void compareAnswers(String str, List<Answer> answerList) {
        List<String> items = Arrays.asList(str.split(ANSWER_SEPARATOR));

        answerList.stream().filter(Answer::isCorrect).forEach(answer -> {
            if(answer.isUserInput() && answer.getCorrectStr().equalsIgnoreCase(str)) {
                answer.setAnswered(true);
            }
            else if(!answer.isUserInput() && items.contains(String.valueOf(answer.getNumber()))) {
                answer.setAnswered(true);
            }
        });
    }
}
