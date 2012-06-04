package hr.restart.blpn;

import hr.restart.baza.dM;
import hr.restart.swing.JraTextField;
import hr.restart.util.Aus;
import hr.restart.zapod.frmSifrarnikZP;

import java.awt.BorderLayout;
import java.math.BigDecimal;

import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jb.util.TriStateProperty;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class frmPSredstva extends frmSifrarnikZP {
  JraTextField jrValue = new JraTextField();
  StorageDataSet vholder;
  private static frmPSredstva psinst;
  
  public frmPSredstva(String s) {
    super(s);
  }
  
  public static frmPSredstva getPSInstance() {
    if (psinst == null) new frmPSredstva();
    return psinst;
  }
  public frmPSredstva() {
    super("BLPS");
    setRaText("Prijevozno sredstvo");
    initValue();
    psinst =this;
  }
  public void beforeOpenSifraSet() {
    sifraSet.getColumn("PARAMETRI").setVisible(TriStateProperty.TRUE);
    sifraSet.getColumn("PARAMETRI").setCaption(getPCaption());
  }
  public void initValue() {
    vholder = new StorageDataSet();
    vholder.addColumn(dM.createBigDecimalColumn("V", 2));
    vholder.open();
    vholder.insertRow(false);
    jrValue.setDataSet(vholder);
    jrValue.setColumnName("V");
    XYLayout layV = new XYLayout();
    JPanel jpV = new JPanel(layV);
    jpV.add(new JLabel(getValueText()),new XYConstraints(15,0,-1,-1));
    jpV.add(jrValue, new XYConstraints(150, 0, 100, -1));
    layV.setHeight(40);
    jpRoot.add(jpV,BorderLayout.SOUTH);
//    jp.setBorder(BorderFactory.createLineBorder(Color.BLACK));
  }
  public void SetFokus2(char mode) {
  }
  public void raQueryDataSet_navigated(NavigationEvent e) {
    try {
      BigDecimal v = new BigDecimal(getRaDataSet().getString("PARAMETRI").trim());
      vholder.setBigDecimal("V", v);
    } catch (Exception ex) {
      vholder.setBigDecimal("V", Aus.zero2);
      //ex.printStackTrace();
    }
    
  }
  public boolean Validacija2(char mode) {
    getRaDataSet().setString("PARAMETRI", vholder.getBigDecimal("V").toString());
    return super.Validacija2(mode);
  }
  public String getValueText() {
    return "Cijena po kilometru";
  }
  public String getPCaption() {
    return "Cijena po km";
  }
}
