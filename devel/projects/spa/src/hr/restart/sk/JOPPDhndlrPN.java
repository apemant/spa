package hr.restart.sk;

import java.sql.Timestamp;

import javax.swing.JTable;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Shkonta;
import hr.restart.baza.dM;
import hr.restart.pl.raIzvjestaji;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.XYPanel;
import hr.restart.swing.raInputDialog;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raCommonClass;
import hr.restart.zapod.dlgGetKnjig;


public class JOPPDhndlrPN extends JOPPDhndlr {

  StorageDataSet per;
  XYPanel xy;
  raInputDialog dlg = new raInputDialog();
  public JOPPDhndlrPN(frmPDV2 _fpdv2) {
    super(_fpdv2);
    per = Aus.createSet("{Datum od}@DATOD {Datum do}@DATDO");
    xy = new XYPanel(per).label("Datum (od - do)").text("DATOD").text("DATDO").expand();
    Timestamp lm = Util.getUtil().addMonths(Valid.getValid().getToday(), -1);
    per.setTimestamp("DATOD", Util.getUtil().getFirstDayOfMonth(lm));
    per.setTimestamp("DATDO", Util.getUtil().getLastDayOfMonth(lm));
  }
  
  public boolean checkPeriod() {
    return dlg.show(fPDV2, xy, "Odabir razdoblja");
  }
  
  protected void doJOPPD() {
    if (currOIB == null) {
      getStrAset();
      strAset.empty();
      strAset.insertRow(false);
      strAset.setString("OZNIZV", getOZNJOPPD(fPDV2.getDatumOd()));
      strAset.setInt("VRSTAIZV", 1);
      strAset.setString("OZNPOD",frmParam.getParam("pl", "jpod"+dlgGetKnjig.getKNJCORG(), 
          "1", "Vrsta podnositelja JOPPD za knjigovodstvo "+dlgGetKnjig.getKNJCORG()));
      //strB
      String[] keys = {"OIB", "JNP", "JNI"};
      getStrBset();
      strBset.empty();
      int rbr = 0;
      
      lookupData.getlookupData().raLocate(dM.getDataModule().getOrgpl(), "CORG", dlgGetKnjig.getKNJCORG(false));
      String copr = "0" + raIzvjestaji.convertCopcineToRS(dM.getDataModule().getOrgpl().getString("COPCINE"));
      
      String[] cols = {"CSKL", "STAVKA"};
      DataSet sh = Shkonta.getDataModule().getTempSet(Condition.equal("VRDOK", "PN"));
      DataSet ds = Aus.q("SELECT p.cradnik, p.datobr, s.cskl, s.stavka, s.iznos from putninalog p, stavkepn s WHERE " +
      		"p.knjig = s.knjig AND p.godina = s.godina AND p.broj = s.broj AND p.indputa = s.indputa AND " +
          Aus.getKnjigCond().and(Condition.between("DATOBR", per.getTimestamp("DATOD"), per.getTimestamp("DATDO"))).qualified("p"));
      ds.setSort(new SortDescriptor(new String[] {"CRADNIK"}));
      
      for (ds.first(); ds.inBounds(); ds.next()) {
        lookupData.getlookupData().raLocate(dM.getDataModule().getRadnici(), "CRADNIK", ds.getString("CRADNIK"));
        String imeprez = dM.getDataModule().getRadnici().getString("IME")+
            " "+dM.getDataModule().getRadnici().getString("PREZIME");
        lookupData.getlookupData().raLocate(dM.getDataModule().getRadnicipl(), "CRADNIK", ds.getString("CRADNIK"));
        String coib = dM.getDataModule().getRadnicipl().getString("OIB");
        String cop = dM.getDataModule().getRadnicipl().getString("COPCINE");
        lookupData.getlookupData().raLocate(sh, cols, ds);
        String[] param = new VarStr(sh.getString("POLJE")).split();
        
        if (!lookupData.getlookupData().raLocate(strBset, keys, new String[] {coib, param[0], param[1]})) {
          strBset.insertRow(false);
          strBset.setInt("RBR", ++rbr);
          strBset.setString("COPCINE", "0" + raIzvjestaji.convertCopcineToRS(cop));
          strBset.setString("COPRADA", copr);
          strBset.setString("OIB", coib);
          strBset.setString("IMEPREZ", imeprez);
          strBset.setString("JOS", "0000");
          strBset.setString("JOP", "0000");
          strBset.setString("JOB", "0");
          strBset.setString("JOZ", "0");
          strBset.setString("JOM", "0");
          strBset.setString("JRV", "0");
          strBset.setInt("SATI", 0);
          strBset.setInt("NSATI", 0);
          strBset.setString("JNP", param[0]);
          strBset.setString("JNI", param[1]);
          strBset.setTimestamp("ODJ", per.getTimestamp("DATOD"));
          strBset.setTimestamp("DOJ", per.getTimestamp("DATDO"));
        }
        
        Aus.add(strBset, "NEOP", ds.getBigDecimal("IZNOS"));
        Aus.add(strBset, "NETOPK", ds.getBigDecimal("IZNOS"));
      }
      currOIB = getKnjCurrOIB();
      sumStrA();
    }
    strBset.setSort(new SortDescriptor(new String[] {"RBR"}));
    strBset.setTableName("JOPPDB");
    fPDV2.getJPTV().getMpTable().setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
    fPDV2.setDataSet(strBset);
    fPDV2.getJPTV().removeAllTableModifiers();
    fPDV2.killAllReports();
    fPDV2.addReport("hr.restart.pl.repJOPPDDisk", "Datoteka JOPPD za e-poreznu");
    fPDV2.setTitle("Obrazac JOPPD za dan "+Aus.formatTimestamp(fPDV2.getDatumOd()));
    setMode("Strana A");
    jbGet.setEnabled(true);
//    jraPoctDat.setEnabled(true);
    raCommonClass.getraCommonClass().setLabelLaF(fPDV2.jraPoctDat, true);
    raCommonClass.getraCommonClass().setLabelLaF(fPDV2.jraKrajDat, true);
  }

}
