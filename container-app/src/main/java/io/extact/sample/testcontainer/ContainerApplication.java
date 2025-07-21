package io.extact.sample.testcontainer;

import jakarta.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.jpa.JpaRepositoriesAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;

@SpringBootApplication
public class ContainerApplication {

//    static {
//        System.setProperty("http.proxyHost", "127.0.0.1");
//        System.setProperty("http.proxyPort", "8888");
//    }

    public static void main(String[] args) {
        SpringApplication.run(ContainerApplication.class, args);
    }

    @Configuration(proxyBeanMethods = false)
    @EnableAutoConfiguration(exclude = {
            DataSourceAutoConfiguration.class,
            HibernateJpaAutoConfiguration.class,
            JpaRepositoriesAutoConfiguration.class})
    @Profile("memory")
    static class MemoryProfileConfig {
    }

    @Bean
    OtlpGrpcSpanExporter otlpHttpSpanExporter(@Value("${tracing.url}") String url) {
        return OtlpGrpcSpanExporter.builder().setEndpoint(url).build();
    }

    @Bean
    OpenTelemetryAppenderInitializer openTelemetryAppenderInitializer(OpenTelemetry openTelemetry) {
        return new OpenTelemetryAppenderInitializer(openTelemetry);
    }

    static class OpenTelemetryAppenderInitializer {

        private final OpenTelemetry openTelemetry;

        OpenTelemetryAppenderInitializer(OpenTelemetry openTelemetry) {
            this.openTelemetry = openTelemetry;
        }

        @PostConstruct
        void init() {
            OpenTelemetryAppender.install(this.openTelemetry);
        }

    }
}
