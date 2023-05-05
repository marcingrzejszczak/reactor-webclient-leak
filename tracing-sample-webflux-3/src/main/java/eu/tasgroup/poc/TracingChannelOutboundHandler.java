package eu.tasgroup.poc;

import io.micrometer.context.ContextSnapshot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandler;
import io.netty.channel.ChannelPromise;

import java.net.SocketAddress;

public final class TracingChannelOutboundHandler implements ChannelOutboundHandler {
	static final String NAME = "tracingChannelOutboundHandler";

	static final TracingChannelOutboundHandler INSTANCE = new TracingChannelOutboundHandler();

	@Override
	public void bind(ChannelHandlerContext ctx, SocketAddress localAddress, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.bind(localAddress, promise);
		}
	}

	@Override
	public void close(ChannelHandlerContext ctx, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.close(promise);
		}
	}

	@Override
	public void connect(ChannelHandlerContext ctx, SocketAddress remoteAddress, SocketAddress localAddress, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.connect(remoteAddress, localAddress, promise);
		}
	}

	@Override
	public void deregister(ChannelHandlerContext ctx, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.deregister(promise);
		}
	}

	@Override
	public void disconnect(ChannelHandlerContext ctx, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.disconnect(promise);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireExceptionCaught(cause);
		}
	}

	@Override
	public void flush(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.flush();
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		//noop
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		//noop
	}

	@Override
	public void read(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.read();
		}
	}

	@Override
	public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.write(msg, promise);
		}
	}
}