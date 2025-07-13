package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import java.io.IOException;
import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.ComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;

/**
 * ComposeContainerを利用して複数コンテナを起動する例
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("properties")
class ContainerClientStep7Test {

    @Autowired
    private ContainerClient client;

    @Configuration(proxyBeanMethods = false)
    @Import(ClientApplication.class)
    static class TestConfig {

        @Bean
        ComposeContainer composeContainer() throws IOException {
            return new ComposeContainer(new ClassPathResource("compose.yml").getFile())
                    .withExposedService("container-app-1", 8080)
                    .waitingFor("container-app-1",
                            Wait.forLogMessage(".*Started ContainerApplication in.*", 1)
                                    .withStartupTimeout(Duration.ofSeconds(10)));
        }

        @Bean
        DynamicPropertyRegistrar targetUrlRegistrar(ComposeContainer container) {
            String host = container.getServiceHost("container-app-1", 8080);
            int port = container.getServicePort("container-app-1", 8080);
            String destination = "http://" + host + ":" + port;
            return registry -> registry.add("client.connect-url", () -> destination);
        }
    }

    @Test
    void testClient() throws InterruptedException {

        PersonDto person = client.get(1);
        assertThat(person).isEqualTo(new PersonDto(1, "名前1"));

        PersonDto newPerson = new PersonDto(4, "名前4");
        client.save(newPerson);

        PersonDto savedPerson = client.get(4);
        assertThat(savedPerson).isEqualTo(newPerson);
    }
}
