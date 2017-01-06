package org.idony;

import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.channel.group.*;
import io.netty.bootstrap.*;
import io.netty.channel.local.LocalChannel;


import io.netty.channel.socket.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.channel.socket.oio.OioServerSocketChannel;
import io.netty.channel.socket.oio.OioSocketChannel;
import io.netty.handler.codec.*;
import io.netty.handler.codec.bytes.ByteArrayDecoder;

import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.util.CharsetUtil;
import org.idony.listners.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBException;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.InetSocketAddress;
import java.net.NetworkInterface;
import java.net.SocketAddress;

import java.net.SocketException;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executors;

/**
 * Created by idony on 02.01.17.
 */
public class Server extends MessageListners{
    protected final static Logger logger = LoggerFactory.getLogger(Server.class);
    protected ServerBootstrap b;
Integer i=0;
    public Server() throws SocketException {

        EventLoopGroup bossGroup = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        b = new ServerBootstrap();
        b.group(bossGroup, workerGroup);
        b.channel(NioServerSocketChannel.class);
        b.childHandler(new ChannelInitializer<NioSocketChannel>() {
            public void initChannel(NioSocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                pipeline.addLast("framer2", new StringEncoder());

                pipeline.addLast("framer", new ByteToMessageDecoder() {
                    int o;

                    public Object decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf) throws Exception {
                        String keyClient = new String(byteBuf.array());
                        byteBuf.clear();
                        byteBuf.writeZero(byteBuf.capacity());
                        byteBuf.clear();
                        keyClient = keyClient.substring(0, keyClient.indexOf(0));
                        if (keyClient.length() <5) return null;
                        logger.info("Пришло сообщение :\n----------{}-----------", keyClient);
                        System.out.println(keyClient + "\n" +
                                "----------________-----------");
                        for (String mes : keyClient.split("</message>\n")) {
                            mes += "</message>\n";
                            Message message = null;
                            Message message2;
                            try {
                                message = (Message) JAXB.unmarshal(new StringReader(mes), Message.class);
                                message2 = (Message) JAXB.unmarshal(new StringReader(mes), Message.class, Class.forName(message.getType()));
                                if (channelHandlerContext.channel().isOpen()) {
                                    submitLisners(message2, channelHandlerContext);
                                }
                            } catch (JAXBException e) {
                                e.printStackTrace();
                                return null;
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                                return null;
                            }


                        }
                        return null;
                    }

                }).addLast("framer3", new ChannelHandler() {
                    public void beforeAdd(ChannelHandlerContext channelHandlerContext) throws Exception {
                        logger.info("Подключился :{}", channelHandlerContext.channel().remoteAddress());
                    }

                    public void afterAdd(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    public void beforeRemove(ChannelHandlerContext channelHandlerContext) throws Exception {
                        logger.info("Отключился :{}", channelHandlerContext.channel().remoteAddress());
                    }

                    public void afterRemove(ChannelHandlerContext channelHandlerContext) throws Exception {

                    }

                    public void exceptionCaught(ChannelHandlerContext channelHandlerContext, Throwable throwable) throws Exception {

                    }

                    public void userEventTriggered(ChannelHandlerContext channelHandlerContext, Object o) throws Exception {

                    }

                });
            }
        });


    }

    public void start(Integer port) throws InterruptedException, SocketException {
        b.localAddress(NetworkInterface.getNetworkInterfaces().nextElement().getInetAddresses().nextElement().getCanonicalHostName(), port);
        Channel ch = null;
        try {
            ch = b.bind().sync().channel();
            ch.closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        logger.info("start server :{}",ch.localAddress());
    }

    public void stop() {
        logger.info("server is stoping");
    }

    public static void main(String[] bud) throws SocketException, InterruptedException {
        Server server = new Server();
        server.addListner(new ConnectTListner());
        server.addListner(new CreateTListner());
        server.addListner(new SettingTListner());
        server.addListner(new SettingTOListner());
        server.addListner(new AutorisationListner());

        server.start(20001);
    }
}
