package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistrar;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Spring Bootとのインテグレーション機能を使う
 * ContainerをBeanとして登録するとSpring Bootがライフサイクルの管理をしてくれる
 * このため、@Containerを使う必要がなくなるので@Containerを検出してそれに対していstart/stopを行う@Testcontainersも
 * 不要となる
 * ただし、staticフィールドでコンテナインスタンスを持ちたい場合は@Containerでやる必要がある
 * ----
 * この機能と有効にするにはspring-boot-testcontainersを依存に含める。
 * Startableインタフェースを持っているインスタンスに対してはSpringはBeanの初期化のタイミング(BeanPostProcessor#postProcessAfterInitialization)で
 * startメソッドを呼び出す
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PersonRepositoryStep3Test {

    @TestConfiguration(proxyBeanMethods = false)
    @Import(ContainerApplication.class)
    static class TestConfig {

        @Bean
        PostgreSQLContainer<?> postgreSQLContainer() {
            return new PostgreSQLContainer<>("postgres:16-alpine");
        }

        @Bean
        DynamicPropertyRegistrar targetUrlRegistrar(PostgreSQLContainer<?> postgres) {
            return registry -> {
                registry.add("spring.datasource.url", postgres::getJdbcUrl);
                registry.add("spring.datasource.username", postgres::getUsername);
                registry.add("spring.datasource.password", postgres::getPassword);
            };
        }
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
