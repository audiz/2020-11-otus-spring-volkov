package ru.otus.work5.service;

import lombok.RequiredArgsConstructor;
import org.apache.commons.io.IOUtils;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.work5.configs.AppProps;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

@Service("dataReader")
public class DataReaderImpl implements DataReader {

    private Iterator<String> iterator;
    private final MessageSource messageSource;
    private final AppProps props;

    public DataReaderImpl(MessageSource messageSource, AppProps props) {
        this.messageSource = messageSource;
        this.props = props;
        restart();
    }

    @Override
    public void restart() {
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
