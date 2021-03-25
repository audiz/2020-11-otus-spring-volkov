package ru.otus.spring.integration.hrm;

import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.HrInfo;

@Slf4j
@Service
public class HrmService {
    private Integer prevValue;

    public HrInfo process(Integer hrmValue) throws Exception {
        if(prevValue == null) {
            prevValue = hrmValue;
        }
        val hrm = new HrInfo(hrmValue, prevValue);
        prevValue = hrmValue;
        return hrm;
    }

    public HrInfo filter(HrInfo hrmInfo) throws Exception {
        if(hrmInfo.getPrevHr() == null) {
            prevValue = null;
        }
        return hrmInfo;
    }

}
