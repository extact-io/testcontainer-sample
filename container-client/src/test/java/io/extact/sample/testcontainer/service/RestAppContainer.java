package io.extact.sample.testcontainer.service;

import org.testcontainers.containers.GenericContainer;

import lombok.NonNull;

public class RestAppContainer extends GenericContainer<RestAppContainer> {

    public RestAppContainer(@NonNull String dockerImageName) {
        super(dockerImageName);
    }
}
