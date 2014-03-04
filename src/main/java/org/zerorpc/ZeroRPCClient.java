package org.zerorpc;

import org.msgpack.MessagePack;
import org.msgpack.type.Value;
import org.zeromq.ZMQ;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

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

    public <T> T invoke(String command, T type, String... args) throws IOException {
        final MessagePack packer = new MessagePack();
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        packer.write(out, new Command(command, args));

        this.socket.send(out.toByteArray(), 0);
        byte[] response = this.socket.recv(0);
        T result = packer.read(response, type);
        return result;
    }
}
