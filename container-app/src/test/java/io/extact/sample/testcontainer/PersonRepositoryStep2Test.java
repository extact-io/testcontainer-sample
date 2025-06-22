package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Testcontainersが持つJUnitとのインテグレーション機能を使った場合
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
@Testcontainers
public class PersonRepositoryStep2Test {

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:16-alpine");


    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);
    }

    /*
     * @Testcontainersがフィールドに付けられた@Containerを認識しコンテナをstartし、
     * staticフィールドであればすべてのテストメソッド実行後にstopしてくれる。
     * つまり、Containerのライフサイクルを管理しなくてよくなる
     */
    /*
    /*
    @BeforeAll
    static void startContainer() {
        postgres.start();
    }

    @AfterAll
    static void stopContainer() {
        postgres.stop();
    }
    */

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
