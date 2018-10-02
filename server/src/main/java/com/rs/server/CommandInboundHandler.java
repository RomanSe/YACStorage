package com.rs.server;

import com.rs.common.model.messages.LoginCommand;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class CommandInboundHandler extends ChannelInboundHandlerAdapter {

    private UserContext userContext = new UserContext();


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            if (msg instanceof LoginCommand) {
                System.out.println("Client text message: " + ((LoginCommand) msg).getLogin());
                ctx.write(CommandProcessor.process(userContext, (LoginCommand) msg));
            } else {
                System.out.printf("Server received wrong object!");
                return;
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
