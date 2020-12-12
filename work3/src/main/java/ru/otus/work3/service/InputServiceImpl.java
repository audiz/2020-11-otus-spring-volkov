package ru.otus.work3.service;

import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import ru.otus.work3.configs.AppProps;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@Service("inputService")
public class InputServiceImpl implements InputService {
    private final MessageSource messageSource;
    private final AppProps props;

    public InputServiceImpl(MessageSource messageSource, AppProps props) {
        this.messageSource = messageSource;
        this.props = props;
    }

    @Override
    public String getInput(String info) {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        try {

            System.out.println(messageSource.getMessage("ru.otus.getFirstName", null, props.getLocale()));
            return br.readLine().trim();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
