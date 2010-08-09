package hr.restart.robno;

import hr.restart.util.Aus;

import java.math.BigDecimal;

public class repMesklaPnP extends repMeskla {
	
	public String getCSKL() {
		return getCSKLIZ();
	}
	public String getSTRANASIFRA(){
		return ds.getString("BC");
	}
	public int getImaPor1(){
    return 0;
  }
	public int getImaPor2(){
    return 0;
  }
	public int getImaPor3(){
    return 0;
  }
	
	public BigDecimal getPor1p2p3Naz(){
		return new BigDecimal("23.00");
	}
	public BigDecimal getUPRAB1() {
		return Aus.zero2;
	}
	
	public BigDecimal getFC() {
		return Aus.zero2;
	}
	public BigDecimal getINETOP() {
		return Aus.zero2;
	}
}
