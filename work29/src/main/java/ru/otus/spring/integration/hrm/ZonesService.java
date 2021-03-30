package ru.otus.spring.integration.hrm;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.otus.spring.integration.domain.HrInfo;

@Slf4j
@Service
public class ZonesService {

    private int total;
    private int zone1;
    private int zone2;
    private int zone3;
    private int zone4;
    private int zone5;

    public HrInfo zone1(HrInfo hrmInfo) throws Exception {
        total++;
        zone1++;
        return calcZones(hrmInfo);
    }

    public HrInfo zone2(HrInfo hrmInfo) throws Exception {
        total++;
        zone2++;
        return calcZones(hrmInfo);
    }

    public HrInfo zone3(HrInfo hrmInfo) throws Exception {
        total++;
        zone3++;
        return calcZones(hrmInfo);
    }

    public HrInfo zone4(HrInfo hrmInfo) throws Exception {
        total++;
        zone4++;
        return calcZones(hrmInfo);
    }

    public HrInfo zone5(HrInfo hrmInfo) throws Exception {
        total++;
        zone5++;
        return calcZones(hrmInfo);
    }

    private HrInfo calcZones(HrInfo hrmInfo) {
        hrmInfo.setZone1prc(((float)zone1 / total) * 100);
        hrmInfo.setZone2prc(((float)zone2 / total) * 100);
        hrmInfo.setZone3prc(((float)zone3 / total) * 100);
        hrmInfo.setZone4prc(((float)zone4 / total) * 100);
        hrmInfo.setZone5prc(((float)zone5 / total) * 100);
        return hrmInfo;
    }
}
