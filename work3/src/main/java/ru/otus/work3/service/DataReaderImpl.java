package ru.otus.work3.service;

import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.work3.configs.AppProps;

import java.io.IOException;
import java.util.Iterator;

@Service("dataReader")
public class DataReaderImpl implements DataReader {

    private Iterator<String> iterator;

    public DataReaderImpl(MessageSource messageSource, AppProps props) {
        try {
            String csvFile = messageSource.getMessage("ru.otus.questionFile", null, props.getLocale());
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
