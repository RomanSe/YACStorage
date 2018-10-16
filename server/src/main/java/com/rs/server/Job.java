package com.rs.server;

import com.rs.common.Context;
import com.rs.common.messages.Command;
import io.netty.channel.socket.SocketChannel;

public class Job {

    private Context context;
    private Command command;
    private SocketChannel channel;
    private volatile boolean free;

    public void setFree(boolean free) {
        this.free = free;
    }

    public boolean isFree() {
        return free;
    }

    public static Job getInstance(SocketChannel channel, Context context) {
        return new Job(channel, context);  //TODO организовать pool
    }

    public Job(SocketChannel channel, Context context) {
        this.context = context;
        this.channel = channel;
        free = true;
    }

    public Command getCommand() {
        return command;
    }

    public void setCommand(Command command) {
        this.command = command;
    }

    public void execute() {
        channel.writeAndFlush(command.process(context));
    }
}
