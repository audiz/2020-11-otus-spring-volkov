package ru.otus.work5.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.otus.work5.domain.Answer;
import ru.otus.work5.domain.Question;
import ru.otus.work5.service.DataReader;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class QuestionDaoImpl implements QuestionDao {

    private final String CSV_SEPARATOR;
    private final DataReader dataReader;

    @Autowired
    public QuestionDaoImpl(DataReader dataReader, @Value("${ru.otus.questionSeparator}") String csvSeparator) {
        CSV_SEPARATOR = csvSeparator;
        this.dataReader = dataReader;
    }

    @Override
    public void restart() {
        dataReader.restart();
    }

    @Override
    public Question getNextQuestion() {
        String line = dataReader.readLine();
        if (line != null) {
            String[] split = line.split(CSV_SEPARATOR);
            List<Answer> answers = IntStream.range(1, split.length)
                    .mapToObj(index -> {
                        if(index % 2 == 0) {
                            return null;
                        }
                        int i = index / 2 + 1;
                        return new Answer(split[index], i, split[(index + 1)]);
                    })
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
            if(split[0].length() > 0) {
                return new Question(split[0], answers);
            } else {
                return null;
            }
        }
        return null;
    }
}
