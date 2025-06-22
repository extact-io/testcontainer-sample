package io.extact.sample.testcontainer.details;

import org.springframework.boot.autoconfigure.service.connection.ConnectionDetails;

public interface UrlConnectionDetails extends ConnectionDetails {

    String getUrl();
}
