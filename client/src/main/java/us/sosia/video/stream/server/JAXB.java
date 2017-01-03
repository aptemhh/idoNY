package us.sosia.video.stream.server;

import us.sosia.video.stream.server.models.ConnectT;
import us.sosia.video.stream.server.models.CreateT;
import us.sosia.video.stream.server.models.Data;
import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created by idony on 01.01.17.
 */

public class JAXB {

    public static void marshal(Writer out, Object inputStream, Class... classes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        jaxbMarshaller.marshal(inputStream, out);

    }

    public static Object unmarshal(Reader inputStream, Class... classes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(inputStream);
    }

    public static void main(String[] array) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IOException {

        Message mess = new Message();
        mess.setType(CreateT.class.getName());
        CreateT createT = (CreateT) Class.forName(CreateT.class.getName()).newInstance();
        createT.setIp("1..2.3.3");
        mess.setLogin("login1");
        mess.setPass("pass1");
        mess.setData(createT);
        StringWriter outputStream = new StringWriter();

//
//        try {
////            marshal(outputStream, mess, Message.class, CreateT.class);
//            System.out.println(outputStream.getBuffer());
////            marshal(outputStream, mess, Message.class, ConnectT.class);
//            System.out.println(outputStream.getBuffer());
//            Message message=(Message)unmarshal(new StringReader(outputStream.getBuffer().toString()), Message.class);
//            Message message2=(Message)unmarshal(new StringReader(outputStream.getBuffer().toString()), Message.class,Class.forName(message.getType()));
//
//        } catch (JAXBException e) {
//            e.printStackTrace();
//        }

    }
}
