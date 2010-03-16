package hr.restart.robno;

import hr.restart.swing.JraDialog;
import hr.restart.util.FileTransferUtil;
import hr.restart.util.OKpanel;
import hr.restart.util.startFrame;
import hr.restart.zapod.dlgGetKnjig;

import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import javax.swing.JList;
import javax.swing.JScrollPane;

public class frmEDI extends JraDialog {
  JList fileList;
  FileTransferUtil ftu;
  public static String EDI_PROPS="edi.properties";
  OKpanel okp;
  public frmEDI() {
    try {
      Properties p = new Properties();
      p.load(new FileInputStream(EDI_PROPS));
      ftu = new FileTransferUtil(p);
      initUI();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  private void initUI() {
    fileList = new JList(ftu.list());
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(new JScrollPane(fileList), BorderLayout.CENTER);
    okp = new OKpanel() {
      public void jPrekid_actionPerformed() {
        frmEDI.this.setVisible(false);
      }
      
      public void jBOK_actionPerformed() {
        File f = ftu.loadFile(fileList.getSelectedValue().toString());
        raEDI.createOrder(dlgGetKnjig.getKNJCORG(), f);
      }
    };
    getContentPane().add(okp,BorderLayout.SOUTH);
  }
  public static void main(String[] args) {
    startFrame.getStartFrame().STEALTH_MODE = true;
    frmEDI f = new frmEDI();
    f.pack();
    f.show();
    
  }
}
