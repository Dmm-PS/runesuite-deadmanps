package wind.net;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.DefaultChannelPipeline;
import org.jboss.netty.handler.timeout.ReadTimeoutHandler;
import org.jboss.netty.util.Timer;

import wind.net.login.RS2Encoder;
import wind.net.login.RS2LoginProtocol;

public class PipelineFactory implements ChannelPipelineFactory {

	private final Timer timer;

	public PipelineFactory(Timer timer) {
		this.timer = timer;
	}

	@Override
	public ChannelPipeline getPipeline() throws Exception {
		final ChannelPipeline pipeline = new DefaultChannelPipeline();
		pipeline.addLast("timeout", new ReadTimeoutHandler(timer, 10));
		pipeline.addLast("encoder", new RS2Encoder());
		pipeline.addLast("decoder", new RS2LoginProtocol());
		pipeline.addLast("handler", new ChannelHandler());
		return pipeline;
	}

}
