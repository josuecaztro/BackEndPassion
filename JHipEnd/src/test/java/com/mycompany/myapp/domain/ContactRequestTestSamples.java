package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicLong;

public class ContactRequestTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static ContactRequest getContactRequestSample1() {
        return new ContactRequest()
            .id(1L)
            .messageBody("messageBody1")
            .firstName("firstName1")
            .lastName("lastName1")
            .phoneNumber("phoneNumber1");
    }

    public static ContactRequest getContactRequestSample2() {
        return new ContactRequest()
            .id(2L)
            .messageBody("messageBody2")
            .firstName("firstName2")
            .lastName("lastName2")
            .phoneNumber("phoneNumber2");
    }

    public static ContactRequest getContactRequestRandomSampleGenerator() {
        return new ContactRequest()
            .id(longCount.incrementAndGet())
            .messageBody(UUID.randomUUID().toString())
            .firstName(UUID.randomUUID().toString())
            .lastName(UUID.randomUUID().toString())
            .phoneNumber(UUID.randomUUID().toString());
    }
}
