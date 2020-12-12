package ru.otus.work3.service;

import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.work3.configs.AppProps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service("dataReader")
public class DataReaderImpl implements DataReader {

    private Iterator<String> iterator;

    public DataReaderImpl(MessageSource messageSource, AppProps props) {
        InputStream stream = null;
        try {
            String csvFile = messageSource.getMessage("ru.otus.questionFile", null, props.getLocale());
            stream = getClass().getResourceAsStream(csvFile);
            iterator = IOUtils.readLines(stream, "UTF-8").iterator();
            stream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

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
