package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class PeriodoDeFacturacionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static PeriodoDeFacturacion getPeriodoDeFacturacionSample1() {
        return new PeriodoDeFacturacion().id(1L);
    }

    public static PeriodoDeFacturacion getPeriodoDeFacturacionSample2() {
        return new PeriodoDeFacturacion().id(2L);
    }

    public static PeriodoDeFacturacion getPeriodoDeFacturacionRandomSampleGenerator() {
        return new PeriodoDeFacturacion().id(longCount.incrementAndGet());
    }
}
