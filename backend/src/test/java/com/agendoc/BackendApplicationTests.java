package com.agendoc;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "agendoc.security.jwt.issuer=agendoc-test",
        "agendoc.security.jwt.secret=agendoc-test-secret-key-with-at-least-32-characters",
        "agendoc.security.jwt.expiration-seconds=3600"
})
class BackendApplicationTests {

    @Test
    void contextLoads() {
    }
}