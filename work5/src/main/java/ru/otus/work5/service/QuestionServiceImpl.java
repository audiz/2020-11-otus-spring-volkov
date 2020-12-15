package ru.otus.work5.service;

import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import ru.otus.work5.configs.AppProps;
import ru.otus.work5.dao.QuestionDao;
import ru.otus.work5.domain.Answer;
import ru.otus.work5.domain.Question;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class QuestionServiceImpl implements QuestionService {

    private static final String ANSWER_SEPARATOR = "\\s*,\\s*";
    private final QuestionDao dao;
    private final InputService inputService;
    private int questionNumber;
    private List<Question> answeredList = new ArrayList<>();
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
    public void restart() {
        questionNumber= 0;
        answeredList = new ArrayList<>();
        dao.restart();
    }

    @Override
    public Pair<Question, String> getQuestion() {
        Question question;
        StringBuilder sb = new StringBuilder();
        if ((question = dao.getNextQuestion()) != null) {
            question.setNumber(++questionNumber);
            sb.append((question.getNumber()) + ") " + question.getQuestion()).append("\n");
            if(question.getAnswers().size() != 1) {
                for(Answer answer: question.getAnswers()) {
                    sb.append("\t" + answer.getNumber() + " - " + answer.getAnswer() +  " - " + answer.isCorrect()).append("\n");;
                }
            }
            return new Pair<>(question, sb.toString());
        }
        return null;
    }

    public boolean isAnsweredOk(Question question, String str) {
        if(question.getAnswers().size() <= 1) {
            compareAnswers(str, question.getAnswers());
            answeredList.add(question);
            return true;
        } else {
            List<String> items = Arrays.asList(str.split(ANSWER_SEPARATOR));
            List<String> list = question.getAnswers().stream().map(answer -> String.valueOf(answer.getNumber())).collect(Collectors.toList());
            boolean result = list.containsAll(items);
            if(result) {
                compareAnswers(str, question.getAnswers());
                answeredList.add(question);
            }
            return result;
        }
    }
    @Override
    public String getMessage(String bundle, @Nullable Object[] var) {
        return messageSource.getMessage(bundle, var, props.getLocale());
    }

    @Override
    public String prepareResults() {
        double prc = answeredList.stream().mapToDouble(Question::getCorrectPercents).average().orElse(Double.NaN);
        return messageSource.getMessage("ru.otus.correct_answer", new Double[]{prc}, props.getLocale());
    }

    @Override
    public boolean isAnswered() {
        return answeredList.size() > 0;
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
