package io.extact.sample.testcontainer;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;

/**
 * Spring Bootとのインテグレーション機能を使う
 * ポイントは@ServiceConnectionを使ってスッキリさせる
 */
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
public class PersonRepositoryStep4Test {

    @TestConfiguration(proxyBeanMethods = false)
    @Import(ContainerApplication.class)
    static class TestConfig {

        /**
         * Containerから取得した接続情報を設定ファイルに設定するのではなく
         * その接続情報を司る接続情報オブジェクトにバインドする。
         * 今までのはContainerオブジェクトから接続情報取得→設定ファイルに設定→接続情報オブジェクトにバインド
         * という流れになっていたが、@ServiceConnectinでContainerから接続情報の取得→オブジェクトへのバインドを
         * 仲介してくれるため設定ファイルへの設定を経由する必要がなくなった
         */
        @Bean
        @ServiceConnection
        PostgreSQLContainer<?> postgreSQLContainer() {
            return new PostgreSQLContainer<>("postgres:16-alpine");
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
