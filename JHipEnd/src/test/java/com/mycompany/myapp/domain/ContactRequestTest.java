package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ContactRequestTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ContactRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ContactRequest.class);
        ContactRequest contactRequest1 = getContactRequestSample1();
        ContactRequest contactRequest2 = new ContactRequest();
        assertThat(contactRequest1).isNotEqualTo(contactRequest2);

        contactRequest2.setId(contactRequest1.getId());
        assertThat(contactRequest1).isEqualTo(contactRequest2);

        contactRequest2 = getContactRequestSample2();
        assertThat(contactRequest1).isNotEqualTo(contactRequest2);
    }
}
