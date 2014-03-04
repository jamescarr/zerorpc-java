package org.zerorpc;

import org.msgpack.annotation.Message;
import org.msgpack.type.Value;

/**
 * Created by jamescarr on 3/4/14.
 */
@Message
public class Command {
    private String methodName;
    private Value[] args;

    public Command() {}
    public Command(String methodName, Value... args) {
        this.methodName = methodName;
        this.args = args;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Value[] getArgs() {
        return args;
    }

    public void setArgs(Value[] args) {
        this.args = args;
    }


}
