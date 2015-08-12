package hr.restart.sk;

import hr.restart.robno.sgQuerys;
import hr.restart.util.Aus;

import java.math.BigDecimal;


public class repIOSdev extends repIOS {

  public repIOSdev() {
    // TODO Auto-generated constructor stub
  }
  
  
  public double getID(){
    if (ds.getBigDecimal("PVID").signum() != 0)
      return ds.getBigDecimal("PVSALDO").doubleValue();
    return raSaldaKonti.n0.doubleValue();
  }

  public double getIP(){
    if (ds.getBigDecimal("PVIP").signum() != 0)    
      return ds.getBigDecimal("PVSALDO").doubleValue(); 
    return raSaldaKonti.n0.doubleValue(); 
  }

  public BigDecimal getSALDO(){
     return ds.getBigDecimal("PVSALDO");
  }

  public BigDecimal getPOKAZNISALDO(){
    return rik.getTotals(getCPAR()).getBigDecimal("PVSALDO");
  }
  
  public BigDecimal getPOKAZNISALDO2(){
    if (rik.isKupac())
      return rik.getTotals(getCPAR()).getBigDecimal("PVSALDO");
    return rik.getTotals(getCPAR()).getBigDecimal("PVSALDO").negate();
  }
    
  public String getPokazniSaldo(){
    BigDecimal sal =  getPOKAZNISALDO();
    String side = Aus.leg(getPOKAZNISALDO2().signum(), "u VAŠU korist", "", "u NAŠU korist");
    //String side = sal.signum() < 0 ? "u VAŠU korist" : "u NAŠU korist";
    return "\nPokazuje dug od    " + sgQuerys.getSgQuerys().format(sal.abs(), 2) + 
           " " + ds.getString("OZNVAL") + "    "+side; 
  }
  
  public String getSuglasniSaldo(){
    BigDecimal sal =  getPOKAZNISALDO();
    String side = Aus.leg(getPOKAZNISALDO2().signum(),
        "u korist " + rik.getNazivPartnera(ds.getInt("CPAR")), "",
        "u korist " + re.getFirstLine());
    //String side = sal.signum() < 0 ? rik.getNazivPartnera(ds.getInt("CPAR")) : re.getFirstLine();
    return "Potvrðujemo suglasnost duga od " + hr.restart.robno.sgQuerys.getSgQuerys().format(sal.abs(),2) + 
           " " + ds.getString("OZNVAL") + " " + side; 
  }
  
}
