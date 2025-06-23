package io.extact.sample.testcontainer.details;

import lombok.Data;

@Data
public class PropertiesRestAppConnectionDetails implements RestAppConnectionDetails {

    private String connectUrl;
}
