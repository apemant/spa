package hr.restart.help;

import java.awt.Container;

import javax.swing.JEditorPane;
import javax.swing.JPanel;

import hr.restart.swing.JraScrollPane;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.raComboBox;


public class raSendMessage extends raOptionDialog {

  JPanel pan = new JPanel();
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane msg = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  raComboBox dest = new raComboBox() {
    public void this_itemStateChanged() {};
  };
  
  raSendMessage inst = new raSendMessage();
  
  protected raSendMessage() {
    //
  }
  
  public static void show(Container parent) {
    
  }
}
