package io.extact.sample.testcontainer.service;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

import io.extact.sample.testcontainer.details.RestAppConnectionDetails;


class RestAppContainerConnectionDetailsFactory
    extends ContainerConnectionDetailsFactory<RestAppContainer, RestAppConnectionDetails> {

    @Override
    protected RestAppContainerConnectionDetails getContainerConnectionDetails(
            ContainerConnectionSource<RestAppContainer> source) {

        return new RestAppContainerConnectionDetails(source);
    }

    private static final class RestAppContainerConnectionDetails
        extends ContainerConnectionDetails<RestAppContainer> implements RestAppConnectionDetails {

        protected RestAppContainerConnectionDetails(ContainerConnectionSource<RestAppContainer> source) {
            super(source);
        }

        @Override
        public String getUrl() {
            String host = getContainer().getHost();
            int port = getContainer().getFirstMappedPort();
            return "http://%s:%s".formatted(host, port);
        }
    }
}
