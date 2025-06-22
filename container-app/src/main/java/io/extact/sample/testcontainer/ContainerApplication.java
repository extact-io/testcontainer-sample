package io.extact.sample.testcontainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@SpringBootApplication
public class ContainerApplication {

	public static void main(String[] args) {
		SpringApplication.run(ContainerApplication.class, args);
	}

	@Configuration(proxyBeanMethods = false)
	@EnableAutoConfiguration(exclude = {
	        DataSourceAutoConfiguration.class,
	        HibernateJpaAutoConfiguration.class,
	        JpaRepositoriesAutoConfiguration.class
	})
	@Profile("memory")
	static class MemoryProfileConfig {
	}
}
