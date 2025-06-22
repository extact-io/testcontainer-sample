package io.extact.sample.testcontainer.persistence;

import jakarta.transaction.Transactional;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

import io.extact.sample.testcontainer.Person;
import io.extact.sample.testcontainer.PersonRepository;
import lombok.RequiredArgsConstructor;

@Repository
@Transactional
@RequiredArgsConstructor
@Profile("jpa")
public class PersonJpaRepository implements PersonRepository {

    private final JpaRepositoryDelegator delegator;

    @Override
    public Person findById(int id) {
        return delegator.findById(id).orElse(null);
    }

    @Override
    public void save(Person person) {
        delegator.save(person);
    }

}
