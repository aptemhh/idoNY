package us.sosia.video.stream;

import us.sosia.video.stream.forms.Autorisation;
import us.sosia.video.stream.forms.FormManager;
import us.sosia.video.stream.forms.form;

import javax.swing.*;

/**
 * Created by idony on 06.01.17.
 */
public class main {
    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        FormManager.add(Autorisation.class);
        FormManager.add(form.class);
        FormManager.show(Autorisation.class);


    }
}
