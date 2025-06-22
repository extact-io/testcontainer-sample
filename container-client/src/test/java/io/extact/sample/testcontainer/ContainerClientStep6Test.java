package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import io.extact.sample.testcontainer.service.UrlConnectableContainer;

/**
 * Step5をオレオレServiceConnectionにした例
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("service-connection")
class ContainerClientStep6Test {

    @Autowired
    private ContainerClient client;

    @Configuration(proxyBeanMethods = false)
    @Import(ClientApplication.class)
    static class TestConfig {
        @Bean
        @ServiceConnection
        UrlConnectableContainer appContainer() {
            return new UrlConnectableContainer("container-app:latest")
                    .withExposedPorts(8080);
        }
    }

    @Test
    void testClient() {

        PersonDto person = client.get(1);
        assertThat(person).isEqualTo(new PersonDto(1, "名前1"));

        PersonDto newPerson = new PersonDto(4, "名前4");
        client.save(newPerson);

        PersonDto savedPerson = client.get(4);
        assertThat(savedPerson).isEqualTo(newPerson);
    }
}
