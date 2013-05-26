package hr.restart.distrib;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import hr.restart.util.PreSelect;
import hr.restart.util.startFrame;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class menuDistrib extends JMenu {
  startFrame SF;
  JMenuItem jmDistributeri = new JMenuItem();
  JMenuItem jmDistArt = new JMenuItem();
  JMenuItem jmDistKal = new JMenuItem();
  JMenuItem jmDistList = new JMenuItem();
  JMenuItem jmDistIzv = new JMenuItem();
  
  public menuDistrib(startFrame sf) {
    SF = sf;
    jInit();
  }

  private void jInit() {
    setText("Distribucija");
    jmDistributeri.setText("Distributeri");
    jmDistArt.setText("Definiranje distribucije");
    jmDistKal.setText("Distribucijski kalendar");
    jmDistList.setText("Distribucijske liste");
    jmDistIzv.setText("Izvještaji");
    
    jmDistributeri.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        jmDistributeri_actionPerformed(e);
      }
    });
    jmDistArt.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        jmDistArt_actionPerformed(e);
      }
    });
    jmDistKal.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        jmDistKal_actionPerformed(e);
      }
    });
    jmDistList.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        jmDistList_actionPerformed(e);
      }
    });
    jmDistIzv.addActionListener(new ActionListener() {
      
      public void actionPerformed(ActionEvent e) {
        jmDistIzv_actionPerformed(e);
      }
    });
    
    add(jmDistributeri);
    addSeparator();
    add(jmDistArt);
    add(jmDistKal);
    add(jmDistList);
    addSeparator();
    add(jmDistIzv);
  }

  protected void jmDistKal_actionPerformed(ActionEvent e) {
    
    PreSelect.showPreselect("hr.restart.distrib.presDistkal", "hr.restart.distrib.frmDistkal", "Distribucijski kalendari");
    
  }

  protected void jmDistIzv_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.distrib.frmDistribReportList", jmDistIzv.getText());
  }

  protected void jmDistList_actionPerformed(ActionEvent e) {
  	PreSelect.showPreselect("hr.restart.distrib.presDistList", "hr.restart.distrib.frmDistList", "Distribucijske liste");
  }

  protected void jmDistArt_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.distrib.frmDistart", jmDistArt.getText());
  }

  protected void jmDistributeri_actionPerformed(ActionEvent e) {
    SF.showFrame("hr.restart.distrib.frmDistributeri", jmDistributeri.getText());
  }
}
