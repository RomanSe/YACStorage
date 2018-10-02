package com.rs.client.network;

import com.rs.common.model.ResponseCode;
import com.rs.common.model.messages.LoginCommand;
import com.rs.common.model.messages.ResponseMsg;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.CombinedChannelDuplexHandler;
import io.netty.util.ReferenceCountUtil;

import java.net.SocketAddress;
import java.security.NoSuchAlgorithmException;

public class CommandHandler extends ChannelDuplexHandler {

    private ChannelHandlerContext ctx;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        try {
            if (msg == null)
                return;
            System.out.println(msg.getClass());
            System.out.println(((ResponseMsg) msg).getResponseCode());
            //ctx.fireChannelRead(msg);
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) throws Exception {
        this.ctx = ctx;
        super.connect(ctx, remoteAddress, localAddress, promise);
    }

    public ResponseMsg login(String username, String password) {
        System.out.println("login");
        try {
            ctx.writeAndFlush(new LoginCommand(username, password));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

}
