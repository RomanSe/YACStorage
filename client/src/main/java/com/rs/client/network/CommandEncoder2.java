package com.rs.client.network;

import com.rs.common.model.message.AuthMsg;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.util.ReferenceCountUtil;

public class CommandEncoder2 extends ChannelOutboundHandlerAdapter {


    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        System.out.println(getClass());
        System.out.println(((AuthMsg) msg).getLogin());
        try {
            ctx.write(msg, promise);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void flush(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }
}
