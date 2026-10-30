package uk.gov.service.payments.commons.testing.pact.consumers;

import au.com.dius.pact.consumer.junit.PactVerification;
import org.apache.hc.client5.http.fluent.Request;
import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class PayPactPublishingTest {

    @Rule
    public PayPactProviderRule weatherService = new PayPactProviderRule("weather-service", this);

    @Rule
    public PayPactProviderRule orderService = new PayPactProviderRule("order-service", this);

    @Test
    @PactVerification({"weather-service", "order-service"})
    @Pacts(pacts = {"consumer-weather-service"})
    @Pacts(pacts = {"consumer-order-service"}, publish = false)
    public void specifyIfPactShouldBeWritten() throws IOException {
        Request.get(weatherService.getUrl() + "/weather/London").execute();
        Request.get(orderService.getUrl() + "/orders").execute();
    }

    @AfterClass
    public static void after() {
        assertTrue(new File(Paths.get("", "target", "pacts", "consumer-weather-service.json").toAbsolutePath().toString()).exists());
        assertFalse(new File(Paths.get("", "target", "pacts", "consumer-order-service.json").toAbsolutePath().toString()).exists());
    }
}
