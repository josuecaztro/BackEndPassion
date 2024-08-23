package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class PrayerRequestFormTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static PrayerRequestForm getPrayerRequestFormSample1() {
        return new PrayerRequestForm()
            .id(1L)
            .description("description1")
            .timeFrame("timeFrame1")
            .firstName("firstName1")
            .lastName("lastName1")
            .phoneNumber("phoneNumber1");
    }

    public static PrayerRequestForm getPrayerRequestFormSample2() {
        return new PrayerRequestForm()
            .id(2L)
            .description("description2")
            .timeFrame("timeFrame2")
            .firstName("firstName2")
            .lastName("lastName2")
            .phoneNumber("phoneNumber2");
    }

    public static PrayerRequestForm getPrayerRequestFormRandomSampleGenerator() {
        return new PrayerRequestForm()
            .id(longCount.incrementAndGet())
            .description(UUID.randomUUID().toString())
            .timeFrame(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
