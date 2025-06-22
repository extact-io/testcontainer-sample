package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * 一番原始的な使い方
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PersonRepositoryStep1Test {

    // database名, username, passwordなどはPostgreSQLContainer内でデフォルトが設定されている
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");

    /* PostgreSQLContainerの構成は↓と同じ
    static GenericContainer<?> postgres = new GenericContainer<>("postgres:16-alpine")
            .withExposedPorts(5432)
            .withEnv("POSTGRES_USER", "test")
            .withEnv("POSTGRES_PASSWORD", "test")
            .withEnv("POSTGRES_DB", "test")
            .waitingFor(
               new LogMessageWaitStrategy()
                   .withRegEx(".*database system is ready to accept connections.*\\s")
                   .withTimes(2).withStartupTimeout(Duration.of(60L, ChronoUnit.SECONDS)));
    */

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }

    @Autowired
    private PersonRepository repository;

    @Test
    void testRepository() {

        Person person = repository.findById(1);
        assertThat(person.getId()).isEqualTo(1);
        assertThat(person.getName()).isEqualTo("名前1");

        Person newPerson = new Person(4, "名前4");
        repository.save(newPerson);

        Person savedPerson = repository.findById(4);
        assertThat(savedPerson.getId()).isEqualTo(4);
        assertThat(savedPerson.getName()).isEqualTo("名前4");
    }
}
