import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class Geometry {


    public static void main(String[] args) {

        Geometry obj = new Geometry();
        obj.run(args);

    }


    public void run(String[] args) {
        JFrame frame = new JFrame();

        Display d = new Display();

        frame.add(d);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1300, 700);
        frame.setVisible(true);
        frame.setResizable(false);

        d.setLayout(new FlowLayout());

    }
}
