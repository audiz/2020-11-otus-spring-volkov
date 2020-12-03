package ru.otus.work1.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

@Service("dataReader")
public class DataReaderImpl implements DataReader {
    private final BufferedReader reader;

    public DataReaderImpl(@Value("${ru.otus.questionFile}") String csvFile) {
        reader = new BufferedReader(new InputStreamReader(getClass().getResourceAsStream(csvFile), StandardCharsets.UTF_8));
    }

    @Override
    public String readLine() {
        try {
            return reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
