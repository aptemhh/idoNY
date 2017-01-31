package us.sosia.video.stream.forms;

import javax.swing.*;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by idony on 06.01.17.
 * Менеджер форм
 */
public class FormManager {
    static Map<Class, JFrame> classJFrameMap = new HashMap<Class, JFrame>();
    static List<JFrame> jFrameVisible = new LinkedList<JFrame>();

    /**
     * Добавить форму
     *
     * @param aClass класс формы
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static void add(Class aClass) throws IllegalAccessException, InstantiationException {
        JFrame jFrame = (JFrame) aClass.newInstance();
        jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        classJFrameMap.put(aClass, jFrame);
    }

    /**
     * Показать форму
     *
     * @param aClass класс формы
     */
    public static void show(Class aClass) {
        JFrame jFrame = classJFrameMap.get(aClass);
        jFrameVisible.add(jFrame);
        jFrame.setVisible(true);
        jFrame.pack();
    }

    /**
     * Скрыть все формы
     */
    public static void hideAll() {
        for (JFrame jFrame : jFrameVisible) {
            jFrame.setVisible(false);
        }
    }

}
