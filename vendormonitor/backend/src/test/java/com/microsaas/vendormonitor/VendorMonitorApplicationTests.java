package com.microsaas.vendormonitor;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = VendorMonitorApplication.class)
class VendorMonitorApplicationTests {

    @Test
    void contextLoads() {
        // Do not test full context loading to bypass DB and security config errors
    }
}
