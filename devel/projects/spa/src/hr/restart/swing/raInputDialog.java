package hr.restart.swing;

import java.awt.BorderLayout;
import java.awt.Container;

import javax.swing.JPanel;


public class raInputDialog extends raOptionDialog {
  public boolean show(Container parent, JPanel content, String title) {
    JPanel main = new JPanel(new BorderLayout());
    main.add(content);
    main.add(okp, BorderLayout.SOUTH);
    return super.show(parent, main, title);
  }
  protected boolean checkOk() {
    return super.checkOk();
  }
}
