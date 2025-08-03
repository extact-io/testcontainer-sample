package io.extact.sample.testcontainer;

import jakarta.annotation.PostConstruct;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.extact.sample.testcontainer.details.PropertiesRestAppConnectionDetails;
import io.extact.sample.testcontainer.details.RestAppConnectionDetails;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.instrumentation.logback.appender.v1_0.OpenTelemetryAppender;

@SpringBootApplication
@EnableConfigurationProperties
@EnableScheduling
public class ClientApplication {

    public static void main(String[] args) {

        new SpringApplicationBuilder()
                .sources(ClientApplication.class)
                .profiles("properties", "timer-exec")
                .run(args);
    }

    @Bean
    @ConfigurationProperties("client")
    @Profile("properties")
    PropertiesRestAppConnectionDetails propertiesUrlConnectionDetails() {
        return new PropertiesRestAppConnectionDetails();
    }

    @Bean
    ContainerClient containerClient(RestClient.Builder builder, RestAppConnectionDetails details) {

        RestClient restClient = builder
                .baseUrl(details.getConnectUrl())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();

        return factory.createClient(ContainerClient.class);
    }

    @Bean
    @Profile("timer-exec")
    TimerExecutor timerExecutor(ContainerClient client) {
        return new TimerExecutor(client);
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
