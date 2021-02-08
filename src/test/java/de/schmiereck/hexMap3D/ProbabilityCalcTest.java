package de.schmiereck.hexMap3D;


import java.util.concurrent.atomic.AtomicInteger;

import de.schmiereck.hexMap3D.utils.ProbabilityCalc;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class ProbabilityCalcTest {

    @org.junit.jupiter.api.Test
    void calc0() {
        // arrange
        final String expected1 =
        //       012345678901     012345678901     0123456          012345678901     012345678901     012345678901     012345678901
                "............" + "............" + "............" + "............" + "............" + "............" + "............" + "............" + "............" + "............";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 0);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    @org.junit.jupiter.api.Test
    void calc100() {
        // arrange
        final String expected1 =
                //       012345678901     234567890123456
                //       012345678901     012345678901     012345678901     012345678901
                "111111111111" + "111111111111" + "111111111111" + "111111111111" + "111111111111" +
                        "111111111111" + "111111111111" + "111111111111" + "111111111111" + "111111111111";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 100);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    @org.junit.jupiter.api.Test
    void calc50() {
        // arrange
        final String expected1 = "1.1.";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 50);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    @org.junit.jupiter.api.Test
    void calc20() {
        // arrange
        //                        012345678901234567890123
        final String expected1 = "1.....1.....1.....1.....";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 20);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    @org.junit.jupiter.api.Test
    void calc10() {
        // arrange
        //                        012345678901234567890123
        final String expected1 = "1...........1...........";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 10);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    @org.junit.jupiter.api.Test
    void calc40() {
        // arrange
        // 40%:	4,8=40%*12		8,333=100/12 	33,333=8,333*4	41,666=8,333*5%	6,666=2*3,333	6,666=4*1,666
        // 0: 480%=40%*12		4=480%/100%	3=12/4	400%=4*100%	80%=480%-400%
        // 1: 560%=40%*12+80%	5=560%/100%	2=12/5	500%=5*100%	60%=480%-500%+80
        // 2: 540%=40%*12+60%	5=540%/100%	2=12/5	500%=5*100%	40%=480%-500%+60%
        // 3: 520%=40%*12+40%	5=520%/100%	2=12/5	500%=5*100%	20%=480%-500%+40%
        // 4: 500%=40%*12+20%	5=500%/100%	2=12/5	500%=5*100%	0%=480%-500%+20%
        // 5: 480%=40%*12+0%	4=480%/100%	3=12/4	400%=4*100%	80%=480%-400%
        final String expected1 =
                //       012345678901     234567890123456
                //       012345678901     012345678901     012345678901     012345678901
                "1..1..1..1.." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." +
                        "1..1..1..1.." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." +
                        "1..1..1..1.." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." +
                        "1..1..1..1.." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1." + "1.1.1.1.1.1.";
        final ProbabilityCalc probabilityCalc = new ProbabilityCalc(12, 100, 40);

        // assert
        assertProbability(expected1, probabilityCalc);
    }

    private void assertProbability(final String expected1, final ProbabilityCalc probabilityCalc) {
        final AtomicInteger pos = new AtomicInteger();
        expected1.chars().forEach(c -> {
            final boolean b = probabilityCalc.getExecute();
            if (c == '1') assertTrue(b, String.format("On position \"%s\" (rangePos:%d, posInRange:%d).", pos, probabilityCalc.rangePos, probabilityCalc.posInRange)); else assertFalse(b, String.format("On position \"%s\" (rangePos:%d, posInRange:%d).", pos, probabilityCalc.rangePos, probabilityCalc.posInRange));
            probabilityCalc.calcNextTick();
            pos.getAndIncrement();
        });
    }
}
