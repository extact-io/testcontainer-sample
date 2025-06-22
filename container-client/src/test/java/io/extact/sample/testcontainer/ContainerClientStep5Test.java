package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.GenericContainer;

/**
 * 普通にGenericContainerでコンテナを起動し、DynamicPropertyRegistrarで動的にURLを設定する例
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@ActiveProfiles("properties")
class ContainerClientStep5Test {

    @Autowired
    private ContainerClient client;

    @Configuration(proxyBeanMethods = false)
    @Import(ClientApplication.class)
    static class TestConfig {

        @Bean
        GenericContainer<?> appContainer() {
            return new GenericContainer<>("container-app:latest")
                    .withExposedPorts(8080);
        }

        @Bean
        DynamicPropertyRegistrar targetUrlRegistrar(GenericContainer<?> appContainer, Environment env) {
            String destination = "http://" + appContainer.getHost() + ":" + appContainer.getFirstMappedPort();
            return registry -> registry.add("client.target.url", () -> destination);
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
