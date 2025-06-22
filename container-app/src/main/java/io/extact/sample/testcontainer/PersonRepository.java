package io.extact.sample.testcontainer;

public interface PersonRepository {

    Person findById(int id);

    void save(Person person);
}
