package us.sosia.video.stream.server;

import us.sosia.video.stream.server.models.Message;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.*;

/**
 * Created by idony on 01.01.17.
 * xml->object
 * object->xml
 */

public class JAXB {

    /**
     * object->xml
     * @param out выходной xml
     * @param inputStream объект
     * @param classes классы объекста
     * @throws JAXBException
     */
    public static void marshal(Writer out, Object inputStream, Class... classes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();
        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxbMarshaller.marshal(inputStream, out);
    }

    /**
     * xml->object
     * @param inputStream xml
     * @param classes классы объектов
     * @return результирующий объект
     * @throws JAXBException
     */
    public static Object unmarshal(Reader inputStream, Class... classes) throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(classes);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        return jaxbUnmarshaller.unmarshal(inputStream);
    }
}
