package hr.restart.gk;

import java.math.BigDecimal;

import sun.security.action.GetBooleanAction;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.raDataFilter;
import hr.restart.util.reports.dlgRunReport;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class repBrBillRDG extends repBrBilAllSource {
  raDataFilter filtDOB, filtGUB; 

  //new hr.restart.gk.repBrBillRDG().getDS()
  public DataSet getDS() {
    DataSet _tmp = super.getDS();
    StorageDataSet _set = ((StorageDataSet)_tmp).cloneDataSetStructure();
    String[] bdcols = {"POCID", "POCIP", "ID", "IP", "SALPS", "SALPROM", "SALDO"};
    for (int i = 0; i < bdcols.length; i++) _set.getColumn(bdcols[i]).setScale(2);
    _set.open();
    filtDOB = raDataFilter.parse(frmParam.getParam("gk", "filterDOBIT", "and[-BROJKONTA|BROJKONTA|3|74|16][-BROJKONTA|BROJKONTA|2|77999999|16]", "Filter za stavke glavne knjige koje su PRIHOD"));
    filtGUB = raDataFilter.parse(frmParam.getParam("gk", "filterGUBIT", "or[and[-BROJKONTA|BROJKONTA|3|4|16][-BROJKONTA|BROJKONTA|2|49999999|16]][and[-BROJKONTA|BROJKONTA|3|7|16][+BROJKONTA|BROJKONTA|3|74|16]]", "Filter za stavke glavne knjige koje su RASHOD"));
    try {
      if (_tmp.getRowFilterListener()!=null) {
        _tmp.removeRowFilterListener(_tmp.getRowFilterListener());
      }
      _tmp.addRowFilterListener(filtGUB.copy().or(filtDOB));
      System.out.println("filtGUB.or(filtDOB) added!!!");
    } catch (Exception e) {
      e.printStackTrace();
    }
    
    BigDecimal mult = null; 
    if (dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().getName().equals("repRDG2")) 
    	mult = fbb.multData.getBigDecimal("MULT");
    System.out.println("Scale:" +_tmp.getColumn("ID").getScale());
//    return _tmp;
    _tmp.refilter();
    for (_tmp.first();_tmp.inBounds();_tmp.next()) {
      _set.insertRow(false);
//      System.out.println("COPIIINGG TO "+_tmp.getString("BROJKONTA"));
      _tmp.copyTo(_set);
      if (mult != null && mult.signum() > 0) {
      	Aus.mul(_set, "POCID", mult);
      	Aus.mul(_set, "POCIP", mult);
      	Aus.mul(_set, "ID", mult);
      	Aus.mul(_set, "IP", mult);
      	Aus.sub(_set, "SALPS", "POCID", "POCIP");
      	Aus.sub(_set, "SALPROM", "ID", "IP");
      	Aus.add(_set, "SALDO", "SALPS", "SALPROM");
      }
      _set.post();
    }
    return _set;
  }
  public String getDOBGUB() {
    if (filtDOB.isRow(ds)) {
      return "P R I H O D I";
    } else {
      return "R A S H O D I";
    }
    
  }
  public String getRKLASE() {
//    return super.getRKLASE();
    return "";
  }
  public String getNAZIVKLASE() {
    return "";
  }
  public String getKLASE() {
    return "";
  }
  public double getSALID() {
    double sid = super.getSALID();
    double sip = super.getSALIP();
//System.err.println("SALID->" +getBROJKONTA()+" / "+ds.getString("BROJKONTA")+" filtDOB.isRow(ds)="+filtDOB.isRow(ds)+" , filtGUB.isRow(ds)="+filtGUB.isRow(ds));
    if (filtDOB.isRow(ds) && sid != 0) {//ako je prihod nemoj prikazivati duguje
      return 0;
    }
    if (filtGUB.isRow(ds) && sip !=0) {//ako je rashod i postoji potrazni saldo prikazi ga kao minus dugovni
      return sid-sip;
    }
    return sid;
  }
  public double getSALIP() {
    //return super.getSALIP();
    double sid = super.getSALID();
    double sip = super.getSALIP();
//System.err.println("SALIP->" +getBROJKONTA()+" / "+ds.getString("BROJKONTA")+" filtDOB.isRow(ds)="+filtDOB.isRow(ds)+" , filtGUB.isRow(ds)="+filtGUB.isRow(ds));
    if (filtGUB.isRow(ds) && sip != 0) {//ako je rashod nemoj prikazivati potrazuje
      return 0;
    }
    if (filtDOB.isRow(ds) && sid !=0) {//ako je prihod i postoji dugovni saldo prikazi ga kao minus potrazni
      return sip-sid;
    }
    return sip;
  }
  public String getGrouper() {
    if (fbb.isTreeSelected()) return super.getGrouper();
    return "";
  };
  public String getZaPeriod() {
  	BigDecimal mult = null; 
    if (dlgRunReport.getCurrentDlgRunReport().getCurrentDescriptor().getName().equals("repRDG2")) 
    	mult = fbb.multData.getBigDecimal("MULT");
    
    if (mult == null) return super.getZaPeriod();
        
    String prefix = "Na temelju poèetnog stanja i prometa ";
    if (fbb.getPromPocSt().equals("N")) prefix = "Na temelju prometa ";
    if (fbb.getPromPocSt().equals("P")) return "Na temelju poèetnog stanja " + fbb.getGodina() + " godine";
    if (fbb.getVRSTA().equals("BB")){
      if (fbb.getPocMj().equals(fbb.getZavMj()))
        return prefix + "za " + fbb.getPocMj() + " mjesec " + fbb.getGodina();
      else 
        return prefix + "u periodu od "  + fbb.getPocMj() + " mjeseca do " + fbb.getZavMj()+ " mjeseca " + fbb.getGodina();
    } else {
      return prefix + "u periodu od "  + rdu.dataFormatter(fbb.getPocDat()) + " do " + rdu.dataFormatter(fbb.getZavDat());
    }
  }
}
