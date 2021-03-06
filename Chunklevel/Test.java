import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
public class Test extends JFrame {
  JPopupMenu menu = new JPopupMenu("Popup");
  class MyLabel extends JButton {
    public MyLabel(String text) {
      super(text);
      addMouseListener(new PopupTriggerListener());
    }

    class PopupTriggerListener extends MouseAdapter {
      public void mousePressed(MouseEvent ev) {
        if (ev.isPopupTrigger()) {
          menu.show(ev.getComponent(), ev.getX(), ev.getY());
        }
      }

      public void mouseReleased(MouseEvent ev) {
        if (ev.isPopupTrigger()) {
          menu.show(ev.getComponent(), ev.getX(), ev.getY());
        }
      }

      public void mouseClicked(MouseEvent ev) {
      }
    }
  }

  JButton label = new MyLabel("right-click");
JButton label1 = new MyLabel("right-clicks");

  public Test() {
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    JMenuItem item = new JMenuItem("Test1");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Menu item Test1");
      }
    });
    menu.add(item);

    item = new JMenuItem("Test2");
    item.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        System.out.println("Menu item Test2");
      }
    });
    menu.add(item);

    add(label);
 	add(label1);
    setSize(300, 100);
  }

  public static void main(String[] args) {
    new Test().setVisible(true);
  }
}
