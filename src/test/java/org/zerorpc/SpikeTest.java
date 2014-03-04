package org.zerorpc;

import org.junit.Test;
import org.msgpack.MessagePack;
import org.zerorpc.Person;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by jamescarr on 3/3/14.
 */
public class SpikeTest {

    @Test
    public void shouldDoXThings() throws IOException {

        Person person = new Person();
        person.setName("James Carr");
        person.setEmail("james@zapier.com");
        person.setTwitter("@jamescarr");

        MessagePack msgpack = new MessagePack();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        msgpack.write(out, person);

        Person p = new Person();
        msgpack.read(out.toByteArray(), p);

        System.out.println(p.getEmail());

    }

    @Test
    public void shouldDoThisFromHighLevel() throws IOException {
        Service service = new Service<PersonService>(new PersonService());
        service.bind("tcp://0.0.0.0:4242");
        new Thread(service).start();

        ZeroRPCClient client = new ZeroRPCClient();
        client.connect("tcp://127.0.0.1:4242");

        Integer id = client.invoke("add", 0, "James Carr", "james@zapier.com");

        System.out.printf("Resulting id is %s", id);
        Person p = client.invoke("get", new Person(), id.toString());
        System.out.println(p.getEmail());
    }

}
