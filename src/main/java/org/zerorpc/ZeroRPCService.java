package org.zerorpc;

import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by jamescarr on 3/3/14.
 */
public class ZeroRPCService<T> implements Runnable {
    private T wrappedService;
    private Context context;
    private Socket socket;

    public ZeroRPCService(T wrappedService) {
        this.wrappedService = wrappedService;
    }

    public void bind(String address) {
        this.context = ZMQ.context(1);

        this.socket = context.socket(ZMQ.REP);
        this.socket.bind(address);

    }
    private Object invoke(Command command) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if(command.getArgs() != null && command.getArgs().length > 0) {
            Pair pair = convert(command.getArgs());
            Method m = this.wrappedService.getClass().getMethod(command.getMethodName(), pair.getTypes());
            return m.invoke(this.wrappedService, pair.getParameters());
        } else {
            Method m = this.wrappedService.getClass().getMethod(command.getMethodName());
            return m.invoke(this.wrappedService);
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

                packer.write(out, invoke(command));
                this.socket.send(out.toByteArray());
            } catch (Exception e) {
                e.printStackTrace();
                break;
            }

        }
        this.socket.close();
        this.context.term();
    }

    private Pair convert(Value[] args) {
        Pair pair = new Pair(args.length);

        for(Value arg : args) {
            if(arg.isIntegerValue()) {
                pair.addValue(arg.asIntegerValue().intValue());
            } else if (arg.isRawValue()) {
                pair.addValue(arg.asRawValue().getString());
            }

        }

        return pair;
    }
}

class Pair {
    private final Class<?>[] clazz;
    private final Object[] values;
    private int index = 0;

    public Pair(final int length){
        clazz = new Class<?>[length];
        values = new Object[length];
    }
    public void addValue(Object value){
        clazz[index] = value.getClass();
        values[index++] = value;

    }

    public Class<?>[] getTypes() {
        return clazz;
    }

    public Object[] getParameters() {
        return values;
    }

}


