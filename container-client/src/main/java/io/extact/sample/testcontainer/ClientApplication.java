package io.extact.sample.testcontainer;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import io.extact.sample.testcontainer.details.PropertiesUrlConnectionDetails;
import io.extact.sample.testcontainer.details.UrlConnectionDetails;

@SpringBootApplication
@EnableConfigurationProperties
public class ClientApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder()
                .sources(ClientApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }

    @Bean
    @ConfigurationProperties("client.target")
    @Profile("properties")
    PropertiesUrlConnectionDetails propertiesUrlConnectionDetails() {
        return new PropertiesUrlConnectionDetails();
    }

    @Bean
    ContainerClient containerClientByServiceConnection(UrlConnectionDetails details) {

        RestClient restClient = RestClient.builder()
                .baseUrl(details.getUrl())
                .build();

        RestClientAdapter adapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(adapter)
                .build();

        return factory.createClient(ContainerClient.class);
    }
}
