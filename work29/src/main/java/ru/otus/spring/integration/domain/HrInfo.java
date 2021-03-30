package ru.otus.spring.integration.domain;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class HrInfo {
    private static final int MAX_HRM = 190;

    private Integer hr;
    private Integer prevHr;

    private Integer maxHr;
    private Integer minHr;
    private Integer avgHr;

    private Float zone1prc;
    private Float zone2prc;
    private Float zone3prc;
    private Float zone4prc;
    private Float zone5prc;

    public HrInfo(Integer hr, Integer prevHr) {
        this.hr = hr;
        this.prevHr = prevHr;
    }

    public int getZone() {
        if(hr < MAX_HRM *.6) {
            return 1;
        }
        if(hr < MAX_HRM *.7) {
            return 2;
        }
        if(hr < MAX_HRM *.8) {
            return 3;
        }
        if(hr < MAX_HRM *.9) {
            return 4;
        }
        return 5;
    }

    @Override
    public String toString() {
        return "HrInfo{" +
                "hr=" + hr +
                ", maxHr=" + maxHr +
                ", minHr=" + minHr +
                ", avgHr=" + avgHr +
                ", zone1prc=" + zone1prc +
                ", zone2prc=" + zone2prc +
                ", zone3prc=" + zone3prc +
                ", zone4prc=" + zone4prc +
                ", zone5prc=" + zone5prc +
                '}';
    }
}
