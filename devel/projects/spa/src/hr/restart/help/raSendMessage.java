package hr.restart.help;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.raComboBox;


public class raSendMessage extends raOptionDialog {

  JPanel pan = new JPanel(new BorderLayout());
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane msg = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
  };
  
  raComboBox dest = new raComboBox();
  
  StorageDataSet ds = new StorageDataSet();
  
  static raSendMessage inst = new raSendMessage();
  
  protected raSendMessage() {
    JLabel lab = new JLabel("Prima: ");
    Box box = Box.createHorizontalBox();
    box.add(lab);
    box.add(Box.createHorizontalStrut(10));
    box.add(dest);
    box.add(Box.createHorizontalGlue());
    box.setBorder(BorderFactory.createEmptyBorder(2, 5, 2, 2));
        
    vp.setViewportView(msg);
    
    pan.add(box, BorderLayout.NORTH);
    pan.add(vp);
    pan.add(okp, BorderLayout.SOUTH);
    
    pan.setPreferredSize(new Dimension(400, 250));
    
    ds.setColumns(new Column[] {
        dM.createStringColumn("CUSER", "Korisnik", 15)
    });
    ds.open();
    
    dest.setRaDataSet(ds);
    dest.setRaColumn("CUSER");
    dest.setRaItems(dM.getDataModule().getUseri(), "CUSER",  "NAZIV");
    dest.addItemListener(new ItemListener() {
      public void itemStateChanged(ItemEvent e) {
        SwingUtilities.invokeLater(new Runnable() {
          public void run() {
            msg.requestFocus();
          }
        });
      }
    });
  }
  
  public static void show(Container parent) {
    inst.showImpl(parent);
  }
  
  private void showImpl(Container parent) {
    dest.removeAllItems();
    dest.setRaItems(dM.getDataModule().getUseri(), "CUSER",  "NAZIV");
    dest.findCombo();
    msg.setText("");
    
    if (show(parent, pan, "Poruka")) 
      MsgDispatcher.send(raUser.getInstance().getUser(),
          ds.getString("CUSER"), msg.getText());
  }
}
