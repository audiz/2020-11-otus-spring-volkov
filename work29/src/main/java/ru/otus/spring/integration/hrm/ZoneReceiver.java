package ru.otus.spring.integration.hrm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.SubscribableChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import ru.otus.spring.integration.domain.HrInfo;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Slf4j
@Component
public class ZoneReceiver {

    private final SubscribableChannel zone1Channel;
    private final SubscribableChannel zone2Channel;
    private final SubscribableChannel zone3Channel;
    private final SubscribableChannel zone4Channel;
    private final SubscribableChannel zone5Channel;
    private final SubscribableChannel direct1Channel;

    private Integer maxHr;
    private Integer minHr;
    private int ARRAY_SIZE = 120;
    private int arrPosition = 0;
    private int[] avgHr = new int[ARRAY_SIZE];

    @PostConstruct
    public void init() {
        zone1Channel.subscribe(message -> {
            log.info("\n--------- 1 -----------\n {}", message);
        });

        zone2Channel.subscribe(message -> {
            log.info("\n--------- 2 -----------\n {}", message);
        });

        zone3Channel.subscribe(message -> {
            log.info("\n--------- 3 -----------\n {}",message);
        });

        zone4Channel.subscribe(message -> {
            log.info("\n--------- 4 -----------\n {}", message);
        });

        zone5Channel.subscribe(message -> {
            log.info("\n--------- 5 -----------\n {}", message);
        });

        direct1Channel.subscribe(message -> {
            MessageChannel replyChannel = (MessageChannel)message.getHeaders().getReplyChannel();
            log.info("\n--------- direct -----------\n {}", message);
            HrInfo payload = (HrInfo) message.getPayload();

            handle(payload);

            payload.setMaxHr(maxHr);
            payload.setMinHr(minHr);
            payload.setAvgHr(calcAvg());

            Message<HrInfo> infoMessage = MessageBuilder.withPayload(payload).build();
            replyChannel.send(infoMessage);
        });
    }

    private void handle(HrInfo hr) {
        arrPosition++;
        if(arrPosition >= ARRAY_SIZE) {
            arrPosition = 0;
        }
        avgHr[arrPosition] = hr.getHr();

        if(maxHr == null || hr.getHr() > maxHr) {
            maxHr = hr.getHr();
        }
        if(minHr == null || hr.getHr() < minHr) {
            minHr = hr.getHr();
        }
    }

    private int calcAvg() {
        return (int) Stream.of(Arrays.stream(avgHr)).flatMap(IntStream::boxed).filter(integer -> integer != 0).mapToInt(Integer::intValue).average().getAsDouble();
    }
}
