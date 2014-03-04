package org.zerorpc;

import org.msgpack.annotation.Message;

/**
 * Created by jamescarr on 3/4/14.
 */
@Message
public class Command {
    private String methodName;
    private String[] args;

    public Command() {}
    public Command(String methodName, String... args) {
        this.methodName = methodName;
        this.args = args;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String[] getArgs() {
        return args;
    }

    public void setArgs(String[] args) {
        this.args = args;
    }


}
