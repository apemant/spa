package hr.restart.gk;

import sun.security.action.GetBooleanAction;
import hr.restart.sisfun.frmParam;
import hr.restart.util.raDataFilter;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class repBrBillRDG extends repBrBilAllSource {
  raDataFilter filtDOB, filtGUB; 

  //new hr.restart.gk.repBrBillRDG().getDS()
  public DataSet getDS() {
    DataSet _tmp = super.getDS();
    StorageDataSet _set = ((StorageDataSet)_tmp).cloneDataSetStructure();
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
//    return _tmp;
    _tmp.refilter();
    for (_tmp.first();_tmp.inBounds();_tmp.next()) {
      _set.insertRow(false);
//      System.out.println("COPIIINGG TO "+_tmp.getString("BROJKONTA"));
      _tmp.copyTo(_set);
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
}
