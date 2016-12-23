package us.sosia.video.stream.forms;

import us.sosia.video.stream.agent.InterfaceImp;
import us.sosia.video.stream.agent.InterfaceProgramm;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Created by idony on 24.12.16.
 */
public class form extends JFrame{
    private JPanel panel1;
    private JButton fghfghfghButton;
    private JButton button6456tyutyutyutuButton;
    private InterfaceImp imp=new InterfaceImp();

    public form(){
        setContentPane(panel1);
        fghfghfghButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                imp.startSer();
            }
        });
        button6456tyutyutyutuButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                imp.startCl();
            }
        });
    }

    public static void main(String[] bud) throws ClassNotFoundException, UnsupportedLookAndFeelException, InstantiationException, IllegalAccessException {
        UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
        form form=new form();
        form.pack();
        form.setVisible(true);
        form.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
