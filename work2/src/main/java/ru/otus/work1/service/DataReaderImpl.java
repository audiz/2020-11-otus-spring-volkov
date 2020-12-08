package ru.otus.work1.service;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Iterator;

@Service("dataReader")
public class DataReaderImpl implements DataReader {

    private Iterator<String> iterator;

    public DataReaderImpl(@Value("${ru.otus.questionFile}") String csvFile) {
        try {
            iterator = IOUtils.readLines(getClass().getResourceAsStream(csvFile), "UTF-8").iterator();
        } catch (IOException e) {

            e.printStackTrace();
        }
    }

    @Override
    public String readLine() {
        if(iterator == null || !iterator.hasNext()) {
            return null;
        }
        return iterator.next();
    }
}
