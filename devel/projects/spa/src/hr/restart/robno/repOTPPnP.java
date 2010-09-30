package hr.restart.robno;

import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataRow;


public class repOTPPnP extends repRacuniPnP {
  public String getNAZPAR() {
    if (!ds.isNull("CPAR") && ds.getInt("CPAR") != 0){
      ru.setDataSet(ds);
      colname[0] = "CPAR";
      String np = ru.getSomething(colname, dm.getPartneri(), "NAZPAR").getString();
      if(ds.getInt("PJ")>0){
        lookupData.getlookupData().raLocate(dm.getPjpar(),
            new String[]{"CPAR", "PJ"}
        ,
        new String[]{ds.getInt("CPAR")+"", ds.getInt("PJ")+""});
        np += "\n"+dm.getPjpar().getString("NAZPJ");
      }
      return np;
    } else if (!ds.isNull("CKUPAC") && ds.getInt("CKUPAC") != 0){
      DataRow dr = hr.restart.util.lookupData.getlookupData().raLookup(hr.restart.baza.dM.getDataModule().getKupci(),"CKUPAC", ds.getInt("CKUPAC")+"");
      return dr.getString("IME")+" "+dr.getString("PREZIME");
    }
    return "";
  }
}
