package ar.edu.um.isa.oncall.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class TarifaDeServicioTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + 2L * Integer.MAX_VALUE);

    public static TarifaDeServicio getTarifaDeServicioSample1() {
        return new TarifaDeServicio().id(1L);
    }

    public static TarifaDeServicio getTarifaDeServicioSample2() {
        return new TarifaDeServicio().id(2L);
    }

    public static TarifaDeServicio getTarifaDeServicioRandomSampleGenerator() {
        return new TarifaDeServicio().id(longCount.incrementAndGet());
    }
}
