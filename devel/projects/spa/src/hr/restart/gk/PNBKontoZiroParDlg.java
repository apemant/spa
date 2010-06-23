package hr.restart.gk;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Condition;
import hr.restart.baza.PNBKonto;
import hr.restart.baza.Ziropar;
import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.swing.jpCpar;
import hr.restart.util.JlrNavField;
import hr.restart.util.OKpanel;
import hr.restart.util.VarStr;
import hr.restart.util.raCommonClass;
import hr.restart.util.startFrame;
import hr.restart.zapod.Tecajevi;
import hr.restart.zapod.raKonta;

public class PNBKontoZiroParDlg extends JraDialog {
  ReadWriteRow row;
  private OKpanel okp;
  private JlrNavField jlrBROJKONTA;
  private JlrNavField jlrNAZIVKONTA;
  private JraButton jbGetKonto;
  private JButton jbAddZiroPar, jbAddPNBKonto, jbAddPNBZiro;
  QueryDataSet ziropar = null, pnbkonto = null, zirokonto = null;

  protected PNBKontoZiroParDlg() {
    
  }
  public PNBKontoZiroParDlg(ReadWriteRow _row) {
    row = _row;
    initDlg();
  }
  private void initDlg() {
    final StorageDataSet screenset = new StorageDataSet();
    screenset.addColumn(dM.createStringColumn("BROJKONTA","Konto",12));
    screenset.addColumn(dM.createIntColumn("CPAR","Partner"));
    
    JPanel content = new JPanel();
    BoxLayout boxl = new BoxLayout(content, BoxLayout.Y_AXIS);
    JPanel jpContent = new JPanel();
    XYLayout zpl = new XYLayout();
    zpl.setHeight(195);
    zpl.setWidth(570);
    jpContent.setLayout(zpl);
    JLabel jlZR = new JLabel(row.getString("ZIRO"));
    JLabel jlPNB = new JLabel(row.getString("PNBZ"));
    makeLikeText(jlZR);
    makeLikeText(jlPNB);
    
    jbGetKonto = new JraButton();
    jlrBROJKONTA = new JlrNavField() {
      public void after_lookUp() {
        afterLookupKonto();
      }
    };
    jlrNAZIVKONTA = new JlrNavField() {
      public void after_lookUp() {
        afterLookupKonto();
      }
    };
    jlrBROJKONTA.setColumnName("BROJKONTA");
    jlrBROJKONTA.setDataSet(screenset);
    jlrBROJKONTA.setColNames(new String[] {"NAZIVKONTA"});
    jlrBROJKONTA.setVisCols(new int[] {0,1});
    jlrBROJKONTA.setTextFields(new javax.swing.text.JTextComponent[] {jlrNAZIVKONTA});
    jlrBROJKONTA.setRaDataSet(raKonta.getAnalitickaKonta());
    jlrBROJKONTA.setSearchMode(3);
    jlrNAZIVKONTA.setColumnName("NAZIVKONTA");
    jlrNAZIVKONTA.setSearchMode(1);
    jlrNAZIVKONTA.setNavProperties(jlrBROJKONTA);
    jlrBROJKONTA.setNavButton(jbGetKonto);
    
    jpCpar jpp = new jpCpar(80, 305);
    jpp.bind(screenset);
    /*
    jp.add(jtfBrojKonta, new XYConstraints(150, 30, 80, -1));
    jp.add(jtfNazivKonta, new XYConstraints(235, 30, 305, -1));
     */
    jbAddZiroPar = new JButton("Dodaj žiro raèun za partnera");
    jbAddPNBKonto = new JButton("Dodaj vezu poziv na broj - konto");
    jbAddPNBZiro = new JButton("Dodaj vezu žiro - konto");
    jbAddZiroPar.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addZiroPar(screenset);
      }
    });
    jbAddPNBKonto.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addPNBKonto(screenset);
      }
    });
    jbAddPNBZiro.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        addPNBZiro(screenset);
      }
    });
    jpContent.add(new JLabel("Raèun"), new XYConstraints(15, 20, -1, -1));
    jpContent.add(jlZR, new XYConstraints(150, 20, 385, -1));
    jpContent.add(new JLabel("Poziv na broj"), new XYConstraints(15, 45, -1, -1));
    jpContent.add(jlPNB, new XYConstraints(150, 45, 385, -1));
    jpContent.add(new JLabel("Konto"), new XYConstraints(15, 70, -1, -1));
    jpContent.add(jlrBROJKONTA, new XYConstraints(150, 70, 80, -1));
    jpContent.add(jlrNAZIVKONTA, new XYConstraints(235, 70, 305, -1));
    jpContent.add(jbGetKonto, new XYConstraints(545, 70, 21, 21));
    jpContent.add(jpp, new XYConstraints(0,95,-1,-1));
    jpContent.add(jbAddZiroPar, new XYConstraints(15,120,551,21));
    jpContent.add(jbAddPNBKonto, new XYConstraints(15,145,551,21));
    jpContent.add(jbAddPNBZiro, new XYConstraints(15,170,551,21));
    
    
    okp = new OKpanel() {
      
      public void jPrekid_actionPerformed() {
        hideDlg();
      }
      
      public void jBOK_actionPerformed() {
        zirokonto.saveChanges();
        pnbkonto.saveChanges();
        ziropar.saveChanges();
        if (IzvodFromFile.ziropar != null) IzvodFromFile.ziropar.refresh();
        if (IzvodFromFile.pnbkonto != null) IzvodFromFile.pnbkonto.refresh();
        row.setString("BROJKONTA", IzvodFromFile.getKonto(row.getString("SALDAK").equals("K"), row.getString("ZIRO"), row.getString("PNBZ")));
        if (raKonta.isSaldak(row.getString("BROJKONTA"))) {
          row.setInt("CPAR", IzvodFromFile.getCPar(row.getString("ZIRO")));
        } else row.setAssignedNull("CPAR");       
        hideDlg();
      }
    };
    getContentPane().setLayout(new BorderLayout());
    getContentPane().add(jpContent, BorderLayout.CENTER);
    getContentPane().add(okp, BorderLayout.SOUTH);
    screenset.open(); screenset.empty();
    screenset.insertRow(false);
    screenset.setString("BROJKONTA", row.getString("BROJKONTA"));
    screenset.setInt("CPAR", row.getInt("CPAR"));
    startFrame.getStartFrame().centerFrame(this, 0, "Detalji za "+row.getString("NAZPAR"));
    setButtonLabels(screenset);
    setModal(true);
    setVisible(true);
  }
  private void setButtonLabels(ReadRow set) {
    findRelated(set);
    if (ziropar.getRowCount() > 0) 
      jbAddZiroPar.setText(new VarStr(jbAddZiroPar.getText()).replace("Dodaj", "Obriši").toString());
    else 
      jbAddZiroPar.setText(new VarStr(jbAddZiroPar.getText()).replace("Obriši", "Dodaj").toString());
    
    if (pnbkonto.getRowCount() > 0) 
      jbAddPNBKonto.setText(new VarStr(jbAddPNBKonto.getText()).replace("Dodaj", "Obriši").toString());
    else
      jbAddPNBKonto.setText(new VarStr(jbAddPNBKonto.getText()).replace("Obriši", "Dodaj").toString());
    
    if (zirokonto.getRowCount() > 0) 
      jbAddPNBZiro.setText(new VarStr(jbAddPNBZiro.getText()).replace("Dodaj", "Obriši").toString());
    else
      jbAddPNBZiro.setText(new VarStr(jbAddPNBZiro.getText()).replace("Obriši", "Dodaj").toString());
      
  }
  
  protected void addPNBZiro(ReadRow set) {
    findRelated(set);
    if (zirokonto.getRowCount() == 0) {
      zirokonto.insertRow(false);
      zirokonto.setString("PNB", row.getString("ZIRO"));
      zirokonto.setString("BROJKONTA", set.getString("BROJKONTA"));
      zirokonto.post();
      JOptionPane.showMessageDialog(this, "Veza "+zirokonto.getString("PNB")+" -> "+zirokonto.getString("BROJKONTA")+" dodana.");
    } else {
      zirokonto.first();
      JOptionPane.showMessageDialog(this, "Veza "+zirokonto.getString("PNB")+" -> "+zirokonto.getString("BROJKONTA")+" OBRISANA!");
      zirokonto.deleteRow();
      //JOptionPane.showMessageDialog(this, "Postoji veæ veza "+row.getString("ZIRO")+" -> "+set.getString("BROJKONTA"), "GREŠKA", JOptionPane.ERROR_MESSAGE);
    }
    setButtonLabels(set);
  }

  protected void addPNBKonto(ReadRow set) {
    findRelated(set);
    if (pnbkonto.getRowCount() == 0) {
      pnbkonto.insertRow(false);
      pnbkonto.setString("PNB", row.getString("PNBZ"));
      pnbkonto.setString("BROJKONTA", set.getString("BROJKONTA"));
      pnbkonto.post();
      JOptionPane.showMessageDialog(this, "Veza "+pnbkonto.getString("PNB")+" -> "+pnbkonto.getString("BROJKONTA")+" dodana.");
    } else {
      pnbkonto.first();
      JOptionPane.showMessageDialog(this, "Veza "+pnbkonto.getString("PNB")+" -> "+pnbkonto.getString("BROJKONTA")+" OBRISANA!");
      pnbkonto.deleteRow();
//      JOptionPane.showMessageDialog(this, "Postoji veæ veza "+row.getString("ZIRO")+" -> "+set.getString("BROJKONTA"), "GREŠKA", JOptionPane.ERROR_MESSAGE);
    }
    setButtonLabels(set);
  }
  protected void addZiroPar(ReadRow set) {
    findRelated(set);
    if (ziropar.getRowCount() == 0) {
      ziropar.insertRow(false);
      ziropar.setInt("CPAR", set.getInt("CPAR"));
      ziropar.setString("ZIRO", row.getString("ZIRO"));
      ziropar.setString("DEV","N");
      ziropar.setString("OZNVAL", Tecajevi.getDomOZNVAL());
      ziropar.post();
      JOptionPane.showMessageDialog(this, "Žiro raèun "+ziropar.getString("ZIRO")+" za partnera "+ziropar.getInt("CPAR")+" je dodan!");
    } else {
      ziropar.first();
      JOptionPane.showMessageDialog(this, "Žiro raèun "+ziropar.getString("ZIRO")+" za partnera "+ziropar.getInt("CPAR")+" je OBRISAN!");
      ziropar.deleteRow();
//      JOptionPane.showMessageDialog(this, "Postoji veæ taj žiro raèun za tog partnera", "GREŠKA", JOptionPane.ERROR_MESSAGE);
    }
    setButtonLabels(set);
  }
  private void findRelated(ReadRow set) {
    if (ziropar != null && zirokonto != null && pnbkonto != null) return;
    ziropar = Ziropar.getDataModule().getFilteredDataSet(Condition.equal("ZIRO", row).and(Condition.equal("CPAR", set)));
    ziropar.open();
    zirokonto = PNBKonto.getDataModule().getFilteredDataSet(Condition.equal("PNB", row.getString("ZIRO")).and(Condition.equal("BROJKONTA", set)));
    zirokonto.open();
    pnbkonto = PNBKonto.getDataModule().getFilteredDataSet(Condition.equal("PNB", row.getString("PNBZ")).and(Condition.equal("BROJKONTA", set)));
    pnbkonto.open();
    
    //debug
    System.out.println("ZIROPAR:: "+ziropar);
    System.out.println("ZIROKONTO:: "+zirokonto);
    System.out.println("PNBKONTO:: "+pnbkonto);
  }
  protected void afterLookupKonto() {
    // TODO Auto-generated method stub
    
  }
  private void makeLikeText(JLabel jl) {
    JraTextField jt = new JraTextField();
    jl.setOpaque(true);
    jl.setBackground(jt.getBackground());
    jl.setBorder(jt.getBorder());
    jl.setHorizontalAlignment(SwingConstants.CENTER);
  }
  protected void hideDlg() {
    this.hide();
    this.dispose();
  }
  
}
