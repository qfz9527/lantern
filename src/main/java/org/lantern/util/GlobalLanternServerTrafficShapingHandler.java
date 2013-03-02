package org.lantern.util;

import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.handler.traffic.GlobalTrafficShapingHandler;
import org.jboss.netty.util.Timer;
import org.lantern.LanternClientConstants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class GlobalLanternServerTrafficShapingHandler 
    extends GlobalTrafficShapingHandler {

    private final Logger log = LoggerFactory.getLogger(getClass());
    
    private final AtomicInteger connectedChannels = new AtomicInteger(0);

    @Inject
    public GlobalLanternServerTrafficShapingHandler(final Timer timer) {
        super(timer, LanternClientConstants.SYNC_INTERVAL_SECONDS * 1000);
    }

    @Override
    public void channelOpen(final ChannelHandlerContext ctx, 
        final ChannelStateEvent e) throws Exception {
        this.connectedChannels.incrementAndGet();
    }
    
    @Override
    public void channelClosed(final ChannelHandlerContext ctx, 
        final ChannelStateEvent e) throws Exception {
        this.connectedChannels.decrementAndGet();
    }
    
    public boolean isConnected() {
        return connectedChannels.get() > 0;
    }

    public int getNumSockets() {
        return connectedChannels.get();
    }

}
