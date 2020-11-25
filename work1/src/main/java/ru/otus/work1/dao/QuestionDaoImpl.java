package ru.otus.work1.dao;

import ru.otus.work1.domain.Answer;
import ru.otus.work1.domain.Question;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class QuestionDaoImpl implements QuestionDao {

    private BufferedReader reader;
    private final String CSV_SEPARATOR;
    private final String SCV_FILE;

    public QuestionDaoImpl(String csvFile, String csvSeparator) {
        SCV_FILE = csvFile;
        CSV_SEPARATOR = csvSeparator;
        reset();
    }

    @Override
    public Question getNextQuestion() {
        try{
            String line = reader.readLine();
            if (line != null) {
                String[] split = line.split(CSV_SEPARATOR);
                List<Answer> answers =IntStream.range(1, split.length)
                        .mapToObj(index -> new Answer(split[index], index))
                        .collect(Collectors.toList());
                if(split[0].length() > 0) {
                    return new Question(split[0], answers);
                } else {
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void reset() {
        InputStream is = getClass().getResourceAsStream(SCV_FILE);
        InputStreamReader streamReader = new InputStreamReader(is, StandardCharsets.UTF_8);
        reader = new BufferedReader(streamReader);
    }
}
