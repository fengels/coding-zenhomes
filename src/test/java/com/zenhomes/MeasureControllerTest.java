package com.zenhomes;

import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;

import static org.junit.Assert.*;
import static org.springframework.http.HttpStatus.OK;

/**
 * Integration Tests
 */
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MeasureControllerTest {

    @LocalServerPort
    int port;

    TestRestTemplate restTemplate = new TestRestTemplate();

    HttpHeaders headers = new HttpHeaders();

    @Test
    public void putCounter7() {

        final String body = "{\"counter_id\":\"7\",\"amount\":\"123.123\"}";

        HttpEntity<String> entity = new HttpEntity<String>(body, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/counter"),
                HttpMethod.POST, entity, String.class);

        assertEquals(OK, response.getStatusCode());
    }

    @Test
    public void getCounter1() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/counter?id=1"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\"id\": \"1\",\"village_name\": \"Villarriba\"}";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    @Test
    public void consumptionReport() {

        HttpEntity<String> entity = new HttpEntity<String>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                createURLWithPort("/consumption_report?duration=24h"),
                HttpMethod.GET, entity, String.class);

        String expected = "{\n" +
                "    \"villages\": [\n" +
                "        {\n" +
                "            \"village_name\": \"Villarriba\",\n" +
                "            \"consumption\": 12345.123\n" +
                "        },\n" +
                "        {\n" +
                "            \"village_name\": \"Villabajo\",\n" +
                "            \"consumption\": 23456.123\n" +
                "        }\n" +
                "    ]\n" +
                "}   ";

        JSONAssert.assertEquals(expected, response.getBody(), false);
    }

    private String createURLWithPort(String uri) {
        return "http://localhost:" + port + uri;
    }
}