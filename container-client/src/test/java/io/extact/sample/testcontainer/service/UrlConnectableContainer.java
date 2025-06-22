package io.extact.sample.testcontainer.service;

import org.testcontainers.containers.GenericContainer;

import lombok.NonNull;

public class UrlConnectableContainer extends GenericContainer<UrlConnectableContainer> {

    public UrlConnectableContainer(@NonNull String dockerImageName) {
        super(dockerImageName);
    }
}
