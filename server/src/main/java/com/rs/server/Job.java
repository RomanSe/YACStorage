package com.rs.server;

import com.rs.common.messages.*;
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

    public void setCommand(Command command) {
        this.command = command;
    }

    public void execute() {
        if (command instanceof GetFileCommand)
            channel.writeAndFlush(CommandProcessor.process((GetFileCommand) command, context));
        else if (command instanceof SaveFileCommand)
            channel.writeAndFlush(CommandProcessor.process((SaveFileCommand) command, context));
        else if (command instanceof SignInCommand)
            channel.writeAndFlush(CommandProcessor.process((SignInCommand) command, context));
        else if (command instanceof LoginCommand)
            channel.writeAndFlush(CommandProcessor.process((LoginCommand) command, context));
        else if (command instanceof MoveCommand)
            channel.writeAndFlush(CommandProcessor.process((MoveCommand) command, context));
        else if (command instanceof GetDirectoryCommand)
            channel.writeAndFlush(CommandProcessor.process((GetDirectoryCommand) command, context));
        else if (command instanceof DeleteFileCommand)
            channel.writeAndFlush(CommandProcessor.process((DeleteFileCommand) command, context));
    }

}
