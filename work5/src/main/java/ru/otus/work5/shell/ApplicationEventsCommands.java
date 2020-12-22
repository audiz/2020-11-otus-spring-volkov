package ru.otus.work5.shell;

import org.javatuples.Pair;
import org.jline.reader.LineReader;
import org.springframework.context.annotation.Lazy;
import org.springframework.shell.Availability;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellMethodAvailability;
import org.springframework.shell.standard.ShellOption;
import ru.otus.work5.domain.Question;
import ru.otus.work5.service.QuestionService;

@ShellComponent
public class ApplicationEventsCommands {

    private String firstName;
    private String lastName;

    private final LineReader reader;
    private final QuestionService questionService;

    public ApplicationEventsCommands(QuestionService questionService, @Lazy LineReader reader) {
        this.questionService = questionService;
        this.reader = reader;
    }

    public String ask(String question) {
        return this.reader.readLine("\n" + question + "\n > ");
    }

    @ShellMethod(value = "Login command", key = {"l", "login"})
    public String login(@ShellOption("--first_name") String firstName, @ShellOption("--last_name") String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
        return questionService.getMessage("ru.otus.hello", new Object[]{firstName, lastName});
    }

    @ShellMethod(value = "Questions command", key = { "q", "questions" })
    @ShellMethodAvailability("isQuestionCommandsAvailable")
    public void questions() {
        questionService.restart();
        Pair<Question, String> pair;
        while((pair = questionService.getQuestion()) != null ) {
            boolean success = false;
            Question question = pair.getValue0();
            String str = pair.getValue1();
            do {
                String input = this.ask(str);
                if(questionService.isAnsweredOk(question, input)) {
                    success = true;
                } else {
                    System.out.println(questionService.getMessage("ru.otus.input_valid", null));
                }
            } while (!success);
        }
    }

    @ShellMethod(value = "Result command", key = {"r", "result"})
    @ShellMethodAvailability("isResultCommandAvailable")
    public String result() {
        return questionService.prepareResults();
    }

    private Availability isResultCommandAvailable() {
        if(isQuestionCommandsAvailable().isAvailable()){
            return !questionService.isAnswered() ? Availability.unavailable(questionService.getMessage("ru.otus.answer_questions", null)) : Availability.available();
        }
        return isQuestionCommandsAvailable();
    }

    private Availability isQuestionCommandsAvailable() {
        return lastName == null ? Availability.unavailable(questionService.getMessage("ru.otus.login_msg", null)): Availability.available();
    }
}
