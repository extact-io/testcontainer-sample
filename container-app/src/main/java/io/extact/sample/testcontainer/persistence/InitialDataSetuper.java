package io.extact.sample.testcontainer.persistence;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import io.extact.sample.testcontainer.Person;
import io.extact.sample.testcontainer.PersonRepository;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class InitialDataSetuper implements CommandLineRunner {

    private final PersonRepository repository;

    //@Transactional ← repositoryで定義
    @Override
    public void run(String... args) throws Exception {

        Person p1 = new Person(1, "名前1");
        Person p2 = new Person(2, "名前2");
        Person p3 = new Person(3, "名前3");

        repository.save(p1);
        repository.save(p2);
        repository.save(p3);
    }
}
