package io.extact.sample.testcontainer.persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import io.extact.sample.testcontainer.Person;

public interface JpaRepositoryDelegator extends JpaRepository<Person, Integer> {
}
