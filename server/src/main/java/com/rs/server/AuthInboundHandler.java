package com.rs.server;

import com.rs.common.model.message.AuthMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class AuthInboundHandler extends ChannelInboundHandlerAdapter {



    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        System.out.println("channelRead");
        try {
            if (msg == null)
                return;
            System.out.println(msg.getClass());
            if (msg instanceof AuthMsg) {
                System.out.println("Client text message: " + ((AuthMsg) msg).getLogin());
                AuthMsg answer = new AuthMsg("Hello Client!","lkjlkj");
                ctx.write(answer);
            } else {
                System.out.printf("Server received wrong object!");
                return;
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }
}
