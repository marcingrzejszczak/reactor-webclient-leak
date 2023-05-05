package eu.tasgroup.poc;

import io.micrometer.context.ContextSnapshot;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;

public final class TracingChannelInboundHandler implements ChannelInboundHandler {
	static final String NAME = "tracingChannelInboundHandler";

	static final TracingChannelInboundHandler INSTANCE = new TracingChannelInboundHandler();

	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelActive();
		}
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelInactive();
		}
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelRead(msg);
		}
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelReadComplete();
		}
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelRegistered();
		}
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelUnregistered();
		}
	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireChannelWritabilityChanged();
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireExceptionCaught(cause);
		}
	}

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) {
		//noop
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) {
		//noop
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) {
		try (ContextSnapshot.Scope scope = ContextSnapshot.setAllThreadLocalsFrom(ctx.channel())) {
			ctx.fireUserEventTriggered(evt);
		}
	}
}
