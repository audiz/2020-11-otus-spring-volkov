package ru.otus.spring.integration.hrm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Component;
import ru.otus.spring.integration.domain.HrInfo;

@Slf4j
@RequiredArgsConstructor
@Component
public class HrMeter {

    private final HrGenerator hrGenerator;

    private final HRM hrm;

    public void start() {

        while (true) {
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            val hr = hrGenerator.getHrm();
            log.info("got raw hr: {}", hr);
            HrInfo hrInfo = hrm.process(hr);
            log.info("transformed object: {}", hrInfo);
        }
    }

}
