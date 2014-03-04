package org.zerorpc;

import org.msgpack.MessagePack;
import org.msgpack.packer.Packer;
import org.msgpack.type.IntegerValue;
import org.msgpack.type.Value;
import org.msgpack.type.ValueFactory;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import static org.msgpack.type.ValueFactory.*;
/**
 * Created by jamescarr on 3/3/14.
 */
public class ZeroRPCClient {
    private ZMQ.Context context;
    private ZMQ.Socket socket;

    public void connect(String address) {
        this.context = ZMQ.context(1);
        this.socket = context.socket(ZMQ.REQ);
        this.socket.connect(address);

    }

    public <T> T invoke(String command, T type, Object... args) throws IOException {
        final MessagePack packer = new MessagePack();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        Value[] values = new Value[args.length];
        int i = 0;
        for(Object arg : args) {

            if(arg instanceof Integer) {
                values[i++] = createIntegerValue(((Integer) arg));
            } else if (arg instanceof String) {
                values[i++] = ValueFactory.createRawValue((String)arg);
            }

        }
        packer.write(out, new Command(command, values));

        this.socket.send(out.toByteArray(), 0);
        byte[] response = this.socket.recv(0);
        T result = packer.read(response, type);
        return result;
    }
}
