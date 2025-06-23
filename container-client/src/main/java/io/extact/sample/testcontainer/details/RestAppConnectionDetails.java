package io.extact.sample.testcontainer.details;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

public interface RestAppConnectionDetails extends ConnectionDetails {

    String getConnectUrl();
}
