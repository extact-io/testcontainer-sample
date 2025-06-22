package io.extact.sample.testcontainer;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/persons")
public interface ContainerClient {

    @GetExchange("/{id}")
    PersonDto get(@PathVariable int id);

    @PostExchange
    void save(@RequestBody PersonDto dto);
}
