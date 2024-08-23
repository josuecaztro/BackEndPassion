package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.PrayerRequestFormTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PrayerRequestFormTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PrayerRequestForm.class);
        PrayerRequestForm prayerRequestForm1 = getPrayerRequestFormSample1();
        PrayerRequestForm prayerRequestForm2 = new PrayerRequestForm();
        assertThat(prayerRequestForm1).isNotEqualTo(prayerRequestForm2);

        prayerRequestForm2.setId(prayerRequestForm1.getId());
        assertThat(prayerRequestForm1).isEqualTo(prayerRequestForm2);

        prayerRequestForm2 = getPrayerRequestFormSample2();
        assertThat(prayerRequestForm1).isNotEqualTo(prayerRequestForm2);
    }
}
