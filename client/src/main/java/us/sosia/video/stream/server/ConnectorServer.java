package us.sosia.video.stream.server;


import io.netty.buffer.ByteBuf;

import io.netty.channel.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;

import io.netty.util.concurrent.AbstractEventExecutorGroup;

import io.netty.channel.socket.SocketChannel;

import io.netty.channel.socket.nio.NioEventLoopGroup;

import io.netty.channel.socket.nio.NioSocketChannel;

import io.netty.handler.codec.ByteToMessageDecoder;
import io.netty.handler.codec.string.StringEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.agent.IStreamClientAgent;
import us.sosia.video.stream.agent.StreamClientAgent;
import us.sosia.video.stream.handler.*;
import us.sosia.video.stream.server.models.ConnectTO;
import us.sosia.video.stream.server.models.CreateT;
import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBException;
import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeoutException;

/**
 * Created by idony on 02.01.17.
 */
public class ConnectorServer extends MessageListners{
    protected final static Logger logger = LoggerFactory.getLogger(StreamClientAgent.class);
    protected final Bootstrap clientBootstrap;
    protected Channel clientChannel;
    EventLoopGroup bossGroup;

    public ConnectorServer() {
        super();
        this.clientBootstrap = new Bootstrap();

        bossGroup = new NioEventLoopGroup();
        this.clientBootstrap.group(bossGroup)
                .channel(NioSocketChannel.class)
                .handler(new ChannelInitializer<SocketChannel>() {
                    public void initChannel(SocketChannel socketChannel) throws Exception {
                        ChannelPipeline pipeline = socketChannel.pipeline();


                        pipeline.addLast( "framer2", new StringEncoder());

                        pipeline.addLast( "framer5", new ByteToMessageDecoder()
                        {

                            public Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                                String keyClient = new String(byteBuf.array());
                                byteBuf.clear();
                                keyClient=keyClient.substring(0,keyClient.indexOf(0));
                                if(keyClient.length()==0)return null;
                                logger.info("Пришло сообщение :\n---------{}------------",keyClient);

                                Message message = null;
                                Message message2;
                                try {
                                    message = (Message) JAXB.unmarshal(new StringReader(keyClient), Message.class);
                                    message2 = (Message) JAXB.unmarshal(new StringReader(keyClient), Message.class, Class.forName(message.getType()));
                                    submitLisners(message2);
                                } catch (JAXBException e) {
                                    e.printStackTrace();
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }

                                return null;
                            }


                        });
                    }
                });


    }


    public void connect(String ip,Integer port) {
        logger.info("going to connect to stream server :{}", ip+":"+port);
        clientBootstrap.remoteAddress(ip,port);
        try {
            clientChannel= clientBootstrap.connect().sync().channel();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }


    public void stop() {


            bossGroup.shutdown();

            clientChannel.close().awaitUninterruptibly();

    }

    public synchronized boolean write(Object mess,Class... classes) {

        if (clientChannel != null) {

            try {
                StringWriter stringWriter = new StringWriter();
                JAXB.marshal(stringWriter, mess, classes);
                ChannelFuture future=null;
                future = clientChannel.write(stringWriter.getBuffer().toString());
                logger.info("Отправлено :\n--------{}-------", stringWriter.getBuffer().toString());
                return true;
            } catch (JAXBException e) {
                e.printStackTrace();
            }
        }
        return false;
    }


    public static void main(String[] bud) throws SocketException {

        ConnectorServer connectorServer = new ConnectorServer();

        connectorServer.connect(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), 20001);

        ConnectTListner connectTListner=new ConnectTListner();
        connectorServer.addListner(connectTListner);
        try {
            Thread.sleep(1000);
            connectTListner.BisnessLogic(connectorServer);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        connectorServer.stop();

    }
}
