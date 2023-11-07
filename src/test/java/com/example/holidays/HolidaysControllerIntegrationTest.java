package com.example.holidays;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = HolidaysApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HolidaysControllerIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private HolidaysService holidaysService;

    @Test
    public void testCalculateHolidaysIntegration() {
        String colleagueName = "John";
        int totalAllowance = 25;
        String fileUrl = "/Users/davidcarlin/development/git/holidays/eb3a9c16-9435-42ce-a024-92ed076b5c26.ics";

        // Perform an HTTP POST request to the /calculate endpoint
        String result = restTemplate.postForObject(
                "http://localhost:" + port + "/calculate?colleagueName={name}&totalAllowance={allowance}&fileUrl={url}",
                null, String.class, colleagueName, totalAllowance, fileUrl);

        // Calculate expected result using the HolidaysService
        String expected = holidaysService.calculateHolidays(colleagueName, totalAllowance, fileUrl);

        // Compare the actual result with the expected result
        assertThat(result).isEqualTo(expected);
    }
}

