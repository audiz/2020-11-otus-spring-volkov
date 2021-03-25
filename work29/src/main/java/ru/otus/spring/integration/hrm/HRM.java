package ru.otus.spring.integration.hrm;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import ru.otus.spring.integration.domain.HrInfo;


@MessagingGateway
public interface HRM {
    @Gateway(requestChannel = "hrmChannel", replyChannel = "backChannel")
    HrInfo process(Integer hrm);
}
