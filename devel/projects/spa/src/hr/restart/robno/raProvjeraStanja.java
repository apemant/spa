package hr.restart.robno;

import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.baza.Stanje;
import hr.restart.sisfun.dlgErrors;
import hr.restart.swing.JraTextField;
import hr.restart.util.DataSetComparator;
import hr.restart.util.lookupData;
import hr.restart.util.raProcess;
import hr.restart.util.raUpitLite;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class raProvjeraStanja extends raUpitLite {
  hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  
  lookupData ld = lookupData.getlookupData();
  
  String cskl = "2";

  String god = "2002";
  
  
  QueryDataSet tmpStanje = new QueryDataSet();
  StorageDataSet oldStanje = new StorageDataSet();
  
  dlgErrors err;

  
  JPanel jPanel1 = new JPanel();

  XYLayout xYLayout1 = new XYLayout();
  
  rapancskl1 rpcskl = new rapancskl1() {
    void jbInitRest(boolean how) throws Exception {
        super.jbInitRest(how);
        this.xYLayout1.setWidth(640);
        remove(jbCSKL);
        remove(jrfNAZSKL);
        this.add(jrfNAZSKL, new XYConstraints(255, 25, 348, -1));
        this.add(jbCSKL, new XYConstraints(609, 25, 21, 21));
    }

    public void MYpost_after_lookUp() {
    }
  };

  JLabel tekst = new JLabel("Godina");

  JraTextField poljesnova = new JraTextField();
  
  class myrapancart extends rapancart {
    public myrapancart(int i) {
        super(i);
    }

    public void Clear() {
        jrfCART.emptyTextFields();
        jrfCART1.emptyTextFields();
    }

    public void EnabDisabS(boolean how) {
        super.EnabDisab(how);
    }

    public void EnabDisab(boolean how) {
    }

    public void MYpost_after_lookUp() {
    }
  }

  myrapancart rpcart = new myrapancart(1);

  
  public raProvjeraStanja() {
    try {
        jbInit();
    } catch (Exception ex) {
        ex.printStackTrace();
    }
  }
  
  public void okPress() {
    if (err != null && !err.isDead()) err.hide();
    Condition ccond = Condition.none;
    try {
      ccond = Condition.equal("CART", Integer.parseInt(rpcart.jrfCART.getText()));
    } catch (NumberFormatException ex) {
      //
    }
    
    Condition main = Condition.equal("CSKL", cskl).and(
                    Condition.equal("GOD", god)).and(ccond);
    
    tmpStanje = Stanje.getDataModule().getTempSet(main);
    tmpStanje.open();
    
    oldStanje = new StorageDataSet();
    oldStanje.setColumns(tmpStanje.cloneColumns());
    oldStanje.open();
    for (tmpStanje.first(); tmpStanje.inBounds(); tmpStanje.next()) {
      oldStanje.insertRow(false);
      tmpStanje.copyTo(oldStanje);
      oldStanje.post();
    }
    int j = tmpStanje.rowCount(), i = 0;
    int ev = Math.min(17, (int) Math.round(Math.sqrt(j)) + 1);
    setNewMessage("Obraðeno 0/" + j + " artikala");
    for (tmpStanje.first(); tmpStanje.inBounds(); tmpStanje.next(), i++) {
      checkClosing();
      sub("KOL", "KOLUL", "KOLIZ");
      mulkol("NABUL", "NC", "KOLUL");
      mulsubkol("MARUL", "VC", "NC", "KOLUL");
      mulsubkol("PORUL", "MC", "VC", "KOLUL");
      mulkol("VUL", "MC", "KOLUL");
      mulkol("NABIZ", "NC", "KOLIZ");
      mulsubkol("MARIZ", "VC", "NC", "KOLIZ");
      mulsubkol("PORIZ", "MC", "VC", "KOLIZ");
      mulkol("VIZ", "MC", "KOLIZ");
      mulkol("VRI", "MC", "KOL");
      if ((i+1) % ev == 1 || (i+1) == j)
        setMessage("Obraðeno " + (i+1) + "/" + j + " artikala");
    }
  }
  
  public void afterOKPress() {
    if (raProcess.isCompleted()) {
      DataSetComparator dc = new DataSetComparator(true,false);
      DataSet result = dc.compare(oldStanje, tmpStanje, "CART");
      if (result.rowCount() == 0) {
        JOptionPane.showMessageDialog(this.getWindow(),
            "Trenutaèno stanje je ispravno !", "Nema greške", 
            JOptionPane.INFORMATION_MESSAGE);        
      } else {
        err = new dlgErrors(this.getWindow(), 
            "Razlike izmeðu tablice stanja i rekalkulacije", false);
        JButton popr = new JButton("Popravi!");
        popr.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {            
            saveNewStanje();
          }
        });
        err.setActionButton(popr);
        err.setData(result, dc.getOtherColumns());
        err.setColumnWidth(50);
        err.setSize(640, 400);        
        for (result.first(); result.inBounds(); result.next())
          err.addError(result.getString(dc.getMainColumn()), result);
        err.show();
      }
    } else if (raProcess.isFailed()) 
      JOptionPane.showMessageDialog(this.getWindow(),
          "Rekalkulacija stanja neuspješna !", 
          "Greška", JOptionPane.ERROR_MESSAGE);        
    
    oslobodi();
  }
  
  void saveNewStanje() {
    raProcess.runChild(this.getWindow(), new Runnable() {
      public void run() {
        try {         
          tmpStanje.saveChanges();
        } catch (Exception ex) {
          ex.printStackTrace();
          raProcess.fail();
        }        
      }
    });
    if (raProcess.isFailed())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje neuspješno !", "Greška", JOptionPane.ERROR_MESSAGE);
    else if (raProcess.isCompleted())
      JOptionPane.showMessageDialog(this.getWindow(),
          "Snimanje uspješno završeno!", "Informacija", JOptionPane.INFORMATION_MESSAGE);
  }
    
  void sub(String dest, String from, String to) {
    tmpStanje.setBigDecimal(dest, tmpStanje.getBigDecimal(from).
        subtract(tmpStanje.getBigDecimal(to)));
  }
  
  void add(String dest, String one, String two) {
    tmpStanje.setBigDecimal(dest, tmpStanje.getBigDecimal(one).
        add(tmpStanje.getBigDecimal(two)));
  }
  
  void mulkol(String dest, String src, String kol) {
    tmpStanje.setBigDecimal(dest, tmpStanje.getBigDecimal(src).multiply(
        tmpStanje.getBigDecimal(kol)).setScale(2, BigDecimal.ROUND_HALF_UP));
  }
  
  void mulsubkol(String dest, String from, String to, String kol) {
    tmpStanje.setBigDecimal(dest, tmpStanje.getBigDecimal(from).
        subtract(tmpStanje.getBigDecimal(to)).multiply(
        tmpStanje.getBigDecimal(kol)).setScale(2, BigDecimal.ROUND_HALF_UP));
  }
  
  public boolean Validacija() {
    return ValidateUnos();
  }

    public boolean ValidateUnos() {
        if (rpcskl.jrfCSKL.getText().equals("")) {
            rpcskl.jrfCSKL.requestFocus();
            return false;       
    } else if (!ValGod()) {
            poljesnova.setText("");
            poljesnova.requestFocus();
            return false;
        } else {
            cskl = rpcskl.jrfCSKL.getText();
      if (!validateVrzal()) return false;
            god = poljesnova.getText();
            EnabDisab(false);
            return true;
        }
    }

    public boolean ValGod() {
        int pero = 0;
        try {
            pero = Integer.parseInt(poljesnova.getText());
        } catch (Exception e) {
            return false;
        }
        if (pero < 1900 || pero > 3900)
            return false;
        return true;
    }
  
  String vrzal;
  public boolean validateVrzal() {
    vrzal = null;
    DataSet qds = Sklad.getDataModule().getTempSet("VRZAL", Condition.equal("CSKL", cskl));
    qds.open();
    if (qds.getRowCount() == 1) {
      vrzal = qds.getString("VRZAL");
    } else if (qds.getRowCount() > 1) {
      throw new RuntimeException(
          "Postoji više slogova u tabeli skladište za cskl="+cskl);
    } else if (qds.getRowCount() == 0) {
      throw new RuntimeException(
          "Ne postoji slog u tabeli skladište za cskl="+cskl);
    }
    if (vrzal == null || vrzal.length() != 1 || "NVM".indexOf(vrzal) < 0) {
      rpcskl.jrfCSKL.requestFocus();
      JOptionPane.showMessageDialog(this.getWindow(),
          "Pogrešna vrsta zalihe ("+vrzal+") na skladištu "+cskl+"!", 
          "Greška", JOptionPane.ERROR_MESSAGE);
      return false;
    }
    return true;
  }
  
    public void EnabDisab(boolean kako) {
        rpcskl.disabCSKL(kako);
        rpcart.EnabDisabS(kako);
        hr.restart.util.raCommonClass.getraCommonClass().setLabelLaF(
                poljesnova, kako);
        //    hr.restart.util.raCommonClass.getraCommonClass().EnabDisabAll(okp,kako);
    }

    void jbInit() throws Exception {          
        rpcart.setMyAfterLookupOnNavigate(false);
        rpcart.setMode("DOH");
        rpcart.setnextFocusabile(poljesnova);
        rpcart.setBorder(null);
        xYLayout1.setWidth(645);
        xYLayout1.setHeight(145);
        jPanel1.setLayout(xYLayout1);
        jPanel1.setBorder(new javax.swing.border.EtchedBorder());
        rpcskl.setOverCaption(true);
        poljesnova.setHorizontalAlignment(SwingConstants.CENTER);
        jPanel1.add(rpcskl, new XYConstraints(0, 0, -1, -1)); //(0, 0, -1, -1)
        jPanel1.add(rpcart, new XYConstraints(0, 40, -1, 75));
        jPanel1.add(tekst, new XYConstraints(15, 115, -1, -1)); //15, 50, -1,
        jPanel1.add(poljesnova, new XYConstraints(150, 115, 100, -1));
        this.setJPan(jPanel1);

    }

    public void cancel() {
        oslobodi();
        setVisible(false);
    }

    public void ESC_izlaz(KeyEvent e) {
        if (rpcskl.jrfCSKL.getText().equals(""))
            cancel();
        else {
            oslobodi();
        }
        e.consume();
    }

    public void oslobodi() {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                EnabDisab(true);
                //rpcskl.Clear();
                //rpcart.Clear();
                rpcskl.jrfCSKL.requestFocus();
            }
        });
    }

    public void enableAllDataSetEvents(boolean kako) {
        tmpStanje.enableDataSetEvents(kako);
        //    tmpSkladStanjeArt.enableDataSetEvents(kako);
        //    tmpStdoki.enableDataSetEvents(kako);
        //    tmpStdoku.enableDataSetEvents(kako);
        //    tmpStmesklaUl.enableDataSetEvents(kako);
        //    tmpStmesklaIz.enableDataSetEvents(kako);

    }
  
    public boolean runFirstESC() {
      return true;
  }

  public void firstESC() {
      cancel();
  }

  public void componentShow() {
      rpcskl.Clear();
      poljesnova.setText(hr.restart.util.Valid.getValid().findYear());
      rpcskl.jrfCSKL.requestFocus();
  }

  public boolean isIspis() {
      return false;
  }

  public boolean ispisNow() {
      return false;
  }
}
