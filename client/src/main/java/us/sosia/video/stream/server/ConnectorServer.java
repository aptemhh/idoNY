package us.sosia.video.stream.server;


import io.netty.buffer.ByteBuf;

import io.netty.channel.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.server.listners.*;
import us.sosia.video.stream.server.models.*;

import javax.xml.bind.JAXBException;
import java.io.*;
import java.net.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 02.01.17.
 */
public class ConnectorServer extends MessageListners {
    protected static ConnectorServer connectorServer = new ConnectorServer();
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
    protected final Bootstrap clientBootstrap;
    protected Channel clientChannel;
    EventLoopGroup bossGroup;

    private ConnectorServer() {
        super();
        this.clientBootstrap = new Bootstrap();
        setting();
        addListners();
    }

    private void setting() {
        bossGroup = new NioEventLoopGroup();
        this.clientBootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();


                        pipeline.addLast("framer2", new StringEncoder());

                        pipeline.addLast("framer5", new ByteToMessageDecoder() {

                            public Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                try {
                                    String keyClient = new String(byteBuf.array());
                                    byteBuf.clear();
                                    byteBuf.writeZero(byteBuf.capacity());
                                    byteBuf.clear();
                                    keyClient = keyClient.substring(0, keyClient.indexOf(0));
                                    if (keyClient.length() < 5) return null;
                                    logger.info("Пришло сообщение :\n---------{}------------", keyClient);

                                    Message message = null;
                                    Message message2;

                                    message = (Message) JAXB.unmarshal(new StringReader(keyClient), Message.class);
                                    message2 = (Message) JAXB.unmarshal(new StringReader(keyClient), Message.class, Class.forName(message.getType()));
                                    submitLisners(message2, channelHandlerContext);

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }


                        });
                    }
                });
    }

    private void addListners() {
        addListner(ConnectTListner.class, new ConnectTListner());
        addListner(CreateTListner.class, new CreateTListner());
        addListner(SettingTListner.class, new SettingTListner());
        addListner(AutorisationListner.class, new AutorisationListner());
    }

    public static ConnectorServer getInstate() {
        return connectorServer;
    }

    public void connect() throws Exception {
        if (clientChannel != null && clientChannel.isOpen()) return;
        logger.info("going to connect to stream server :{}", Person.ip + ":" + Person.portS);
        clientBootstrap.remoteAddress(Person.ip, Person.portS);
        try {
            if (clientChannel == null)
                clientChannel = clientBootstrap.connect().sync().channel();
            else {

            }
        } catch (InterruptedException e) {
            logger.info(e.getMessage());
        } catch (Exception e) {
            throw e;
        }


    }


    public void stop() {
        if (clientChannel.isOpen()) {
            clientChannel.close().awaitUninterruptibly();
            bossGroup.shutdown();
        }


    }

    public synchronized boolean write(Object mess) {

        if (clientChannel != null) {

            try {
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(stringWriter, mess, mess.getClass(), ((Message) mess).getData().getClass());
                ChannelFuture future = null;
                future = clientChannel.write(stringWriter.getBuffer().toString());
                //logger.info("Отправлено :\n--------{}-------", stringWriter.getBuffer().toString());
                return true;
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


}
