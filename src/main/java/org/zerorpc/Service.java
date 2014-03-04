package org.zerorpc;

import org.msgpack.MessagePack;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamescarr on 3/3/14.
 */
public class Service<T> implements Runnable {
    private T wrappedService;
    private Context context;
    private Socket socket;

    public Service(T wrappedService) {
        this.wrappedService = wrappedService;
    }

    public void bind(String address) {
        this.context = ZMQ.context(1);

        this.socket = context.socket(ZMQ.REP);
        this.socket.bind(address);

    }
    private Method getMethod(Command command) throws NoSuchMethodException {
        if(command.getArgs() != null && command.getArgs().length > 0) {
            return this.wrappedService.getClass().getMethod(command.getMethodName(), convert(command.getArgs()));
        } else {
            return this.wrappedService.getClass().getMethod(command.getMethodName());
        }
    }
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            System.out.println("Waiting for messages");
            try {
                byte[] bytes = this.socket.recv();
                final MessagePack packer = new MessagePack();
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                final Command command = packer.createUnpacker(new ByteArrayInputStream(bytes)).read(new Command());

                final Object result = getMethod(command).invoke(this.wrappedService, command.getArgs());
                packer.write(out, result);
                this.socket.send(out.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
        this.socket.close();
        this.context.term();
    }

    private Class[] convert(String[] args) {
        Class[] classes = new Class[args.length];
        int i = 0;
        for(String arg : args) {
            classes[i++] = arg.getClass();
        }

        return classes;
    }
}
