package io.extact.sample.testcontainer.persistence;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import io.extact.sample.testcontainer.Person;
import io.extact.sample.testcontainer.PersonRepository;

@Repository
@Profile("memory")
public class PersonMemoryRepository implements PersonRepository {

    private Map<Integer, Person> personMap = new ConcurrentHashMap<>();

    @Override
    public Person findById(int id) {
        return personMap.get(id);
    }

    @Override
    public void save(Person person) {
        personMap.put(person.getId(), person);
    }
}
