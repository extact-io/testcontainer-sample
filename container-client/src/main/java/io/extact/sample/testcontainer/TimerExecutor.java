package io.extact.sample.testcontainer;

import org.springframework.scheduling.annotation.Scheduled;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TimerExecutor {

    private int id = 10;

    private final ContainerClient client;

    @Scheduled(fixedRateString = "15s")
    void execute() {
        client.save(new PersonDto(id, "%sな名前".formatted(id)));
        PersonDto created = client.get(id);
        id++;
    }
}
