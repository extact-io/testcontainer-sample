package io.extact.sample.testcontainer.service;

import org.springframework.boot.testcontainers.service.connection.ContainerConnectionDetailsFactory;
import org.springframework.boot.testcontainers.service.connection.ContainerConnectionSource;

import io.extact.sample.testcontainer.details.UrlConnectionDetails;


class UrlConnectableContainerConnectionDetailsFactory
    extends ContainerConnectionDetailsFactory<UrlConnectableContainer, UrlConnectionDetails> {

    @Override
    protected UrlContainerConnectionDetails getContainerConnectionDetails(
            ContainerConnectionSource<UrlConnectableContainer> source) {

        return new UrlContainerConnectionDetails(source);
    }

    private static final class UrlContainerConnectionDetails
        extends ContainerConnectionDetails<UrlConnectableContainer> implements UrlConnectionDetails {

        protected UrlContainerConnectionDetails(ContainerConnectionSource<UrlConnectableContainer> source) {
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
