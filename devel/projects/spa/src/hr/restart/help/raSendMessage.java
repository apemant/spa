package hr.restart.help;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.plaf.basic.BasicComboBoxRenderer;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.dM;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.raOptionDialog;
import hr.restart.util.lookupData;
import hr.restart.util.raComboBox;
import hr.restart.util.raImages;


public class raSendMessage extends raOptionDialog {
  
  lookupData ld = lookupData.getlookupData();

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

    dest.setRenderer(new BasicComboBoxRenderer() {
      Font normal, bold;
      public Component getListCellRendererComponent(JList list, Object value,
          int index, boolean isSelected, boolean cellHasFocus) {
        // TODO Auto-generated method stub
        
        JLabel comp = (JLabel) super.getListCellRendererComponent(
            list, value, index, isSelected, cellHasFocus);
        
        if (list.getFont() != normal || bold == null)
          bold = list.getFont().deriveFont(Font.BOLD);
        normal = list.getFont();
        
        DataRow user = index < 0 ? null :
          ld.raLookup(dM.getDataModule().getUseri(), "CUSER", dest.getDataValue(index));
        if (user == null || !"D".equals(user.getString("LOKK"))) {
          comp.setFont(normal);
          comp.setIcon(null);
        } else {
          comp.setFont(bold);
          comp.setIcon(raImages.getImageIcon(raImages.IMGTIP));
        }
        
        return comp;
      }
    
    });
  }
  
  public static void show(Container parent) {
    inst.showImpl(parent);
  }
  
  private void showImpl(Container parent) {
    dest.removeAllItems();
    dM.getDataModule().getUseri().refresh();
    dest.setRaItems(dM.getDataModule().getUseri(), "CUSER",  "NAZIV");
    dest.findCombo();
    msg.setText("");
    
    if (show(parent, pan, "Poruka")) 
      MsgDispatcher.send(raUser.getInstance().getUser(),
          ds.getString("CUSER"), msg.getText());
  }
}
