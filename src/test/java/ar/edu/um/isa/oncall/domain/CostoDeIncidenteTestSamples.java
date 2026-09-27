package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class CostoDeIncidenteTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + 2 * Short.MAX_VALUE);

    public static CostoDeIncidente getCostoDeIncidenteSample1() {
        return new CostoDeIncidente().id(1L).minutosCaidos(1);
    }

    public static CostoDeIncidente getCostoDeIncidenteSample2() {
        return new CostoDeIncidente().id(2L).minutosCaidos(2);
    }

    public static CostoDeIncidente getCostoDeIncidenteRandomSampleGenerator() {
        return new CostoDeIncidente().id(longCount.incrementAndGet()).minutosCaidos(intCount.incrementAndGet());
    }
}
