package com.rs.server;

import com.rs.common.messages.Command;
import com.rs.common.messages.LoginCommand;
import com.rs.common.messages.SaveFileCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class CommandInboundHandler extends ChannelInboundHandlerAdapter {

    private CommandProcessor commandProcessor = new CommandProcessor();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            if (msg instanceof LoginCommand) {
                ctx.write(commandProcessor.process((LoginCommand) msg));
            }
            if (msg instanceof SaveFileCommand) {
                ctx.write(commandProcessor.process((SaveFileCommand) msg));
            }

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        //ctx.flush();
        ctx.close();
    }

}
