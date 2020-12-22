package ru.otus.work5.shell;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.shell.CommandNotCurrentlyAvailable;
import org.springframework.shell.Shell;
import org.springframework.test.annotation.DirtiesContext;
import ru.otus.work5.configs.AppProps;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Тест команд shell ")
@SpringBootTest
class ApplicationEventsCommandsTest {

    @Autowired
    private Shell shell;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private AppProps props;

    private static final String DEFAULT_FIRST_NAME = "Vasya";
    private static final String DEFAULT_LAST_NAME = "Pupkin";
    private static final String COMMAND_LOGIN = "login --first_name " + DEFAULT_FIRST_NAME + " --last_name " + DEFAULT_LAST_NAME;
    private static final String COMMAND_RESULTS = "r";

    @DisplayName(" должен возвращать локализованное приветствие")
    @Test
    void shouldReturnExpectedGreetingAfterLoginCommandEvaluated() {
        String res = (String) shell.evaluate(() -> COMMAND_LOGIN);
        assertThat(res).isEqualTo(messageSource.getMessage("ru.otus.hello",
                new Object[]{DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME}, props.getLocale()));
    }

    @DisplayName(" должен возвращать CommandNotCurrentlyAvailable при попытке получения результата до прохождения теста")
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.BEFORE_METHOD)
    @Test
    void shouldReturnExpectedMessageAndFirePublishMethodAfterPublishCommandEvaluated() {
        shell.evaluate(() -> COMMAND_LOGIN);
        Object res = shell.evaluate(() -> COMMAND_RESULTS);
        assertThat(res).isInstanceOf(CommandNotCurrentlyAvailable.class);
    }
}