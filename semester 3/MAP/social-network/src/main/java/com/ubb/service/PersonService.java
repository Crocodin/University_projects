package com.ubb.service;

import com.ubb.config.Config;
import com.ubb.domain.person.Person;
import com.ubb.repo.database.PersonDBRepo;

public class PersonService extends DBService<Person> {

    public PersonService() {
        super(
                new PersonDBRepo(
                        Config.getProperties().getProperty("DB_URL"),
                        Config.getProperties().getProperty("DB_USER"),
                        Config.getProperties().getProperty("DB_PASSWORD")
                ),
                Person::fromString
        );
    }
}
