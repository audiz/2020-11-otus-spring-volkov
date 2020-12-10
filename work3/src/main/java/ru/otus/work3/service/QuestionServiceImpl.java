package ru.otus.work3.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.work3.configs.AppProps;
import ru.otus.work3.dao.QuestionDao;
import ru.otus.work3.domain.Answer;
import ru.otus.work3.domain.Question;

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
    private final MessageSource messageSource;
    private final AppProps props;

    @Autowired
    public QuestionServiceImpl(QuestionDao dao, InputService inputService, MessageSource messageSource, AppProps props) {
        this.dao = dao;
        this.inputService = inputService;
        this.messageSource = messageSource;
        this.props = props;
    }

    @Override
    public void doQuestions() {
        askFio();
        makeQuestions();
        prepareResults();
    }

    private void askFio() {
        String firstName = inputService.getInput(messageSource.getMessage("ru.otus.getFirstName", null, props.getLocale()));
        String lastName = inputService.getInput(messageSource.getMessage("ru.otus.getLastName", null, props.getLocale()));
        System.out.println(messageSource.getMessage("ru.otus.hello", new String[]{firstName, lastName}, props.getLocale()));
    }

    private void makeQuestions(){
        answeredList = new ArrayList<>();
        System.out.println(messageSource.getMessage("ru.otus.answer_questions", null, props.getLocale()));

        Question question;
        while ((question = dao.getNextQuestion()) != null) {
            question.setNumber(++questionNumber);
            System.out.println((question.getNumber()) + ") " + question.getQuestion());
            if(question.getAnswers().size() == 1) {
                String answer = inputService.getInput(messageSource.getMessage("ru.otus.type_answer", null, props.getLocale()));
                compareAnswers(answer, question.getAnswers());
            } else {
                for(Answer answer: question.getAnswers()) {
                    System.out.println("\t" + answer.getNumber() + " - " + answer.getAnswer() +  " - " + answer.isCorrect());
                }
                String answer = inputService.getInput(messageSource.getMessage("ru.otus.your_answer", null, props.getLocale()));
                compareAnswers(answer, question.getAnswers());
            }
            answeredList.add(question);
        }
    }

    private void prepareResults() {
        double prc = answeredList.stream().mapToDouble(Question::getCorrectPercents).average().orElse(Double.NaN);
        System.out.println(messageSource.getMessage("ru.otus.correct_answer", new Double[]{prc}, props.getLocale()));
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
