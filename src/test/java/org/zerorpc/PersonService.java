package org.zerorpc;

import org.zerorpc.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jamescarr on 3/3/14.
 */
public class PersonService {
    private List<Person> people = new ArrayList<Person>();
    private AtomicInteger idProvider = new AtomicInteger(1);

    public int add(String name, String email) {
        Person p = new Person();
        p.setEmail(email);
        p.setName(name);
        p.setId(idProvider.addAndGet(1));

        people.add(p);
        System.out.println("Adding a person");
        return p.getId();
    }

    public Person get(String id) {
        for (Person p : this.people) {
            if (p.getId() == Integer.parseInt(id)) {
                return p;
            }
        }
        return null;
    }
}
