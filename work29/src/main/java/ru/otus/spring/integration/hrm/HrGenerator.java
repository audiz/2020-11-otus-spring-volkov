package ru.otus.spring.integration.hrm;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class HrGenerator {

    private final Integer[] HRM = {0, 0, 0, 0, 1, 1, 1, 2, 2, 3, 3};
    private int nowHrm = 120;
    private boolean hrmUpStrategy = true;
    private boolean hasNoise = false;

    public int getHrm() {

        // generate noise
        if(!hasNoise && random(0, 10) == 10 ) {
            log.warn("random getHrm");
            hasNoise = true;
            return nowHrm + 20;
        }
        hasNoise = false;

        //  return nowHrm++;
        int minHrm = 90;
        int maxHrm = 190;
        int nHrm = HRM[RandomUtils.nextInt(0, HRM.length)];
        int from = (-1*nHrm);

        if(from != 0) {
            if(hrmUpStrategy) {
                from++;
            } else {
                from--;
            }
        }

        nowHrm += random(from, nHrm);
        if(nowHrm > maxHrm) {
            nowHrm = maxHrm;
            hrmUpStrategy = false;
        }
        if(nowHrm < minHrm) {
            nowHrm = minHrm;
            hrmUpStrategy = true;
        }
        return nowHrm;
    }

    private int random(int from, int to) {
        return new Random().nextInt((to - from) + 1) + from;
    }
}
