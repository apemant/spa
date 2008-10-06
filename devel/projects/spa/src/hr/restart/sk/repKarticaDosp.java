/****license*****************************************************************
**   file: repKarticaDosp.java
**   Copyright 2006 Rest Art
**
**   Licensed under the Apache License, Version 2.0 (the "License");
**   you may not use this file except in compliance with the License.
**   You may obtain a copy of the License at
**
**       http://www.apache.org/licenses/LICENSE-2.0
**
**   Unless required by applicable law or agreed to in writing, software
**   distributed under the License is distributed on an "AS IS" BASIS,
**   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
**   See the License for the specific language governing permissions and
**   limitations under the License.
**
****************************************************************************/
package hr.restart.sk;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class repKarticaDosp extends repOpomena {

//******************** M O J I  P O D A C I ******************************

  public String getCOrg()
  {
    return ds.getString("CORG");
  }

/*  public String getCNaloga()
  {
    String gks = ds.getString("CNALOGA");
    int lastHyphen = gks.lastIndexOf('-');
    if (lastHyphen > 0 && gks.length() - lastHyphen <= 5)
      return gks.substring(0,  lastHyphen);
    return gks;
  }

  public String getDatKnj()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATUMKNJ"));
  }*/

  public String getDatDok()
  {
    return rdu.dataFormatter(ds.getTimestamp("DATDOK"));
  }

  public String getDatDosp()
  {
    String vr = ds.getString("VRDOK");
    if (vr.equals("UPL") || vr.equals("IPL")) return "";
    return rdu.dataFormatter(ds.getTimestamp("DATDOSP"));
  }

  public String getBrojDok()
  {
    return ds.getString("BROJDOK");
  }

  public double getSaldo()
  {
    return ds.getBigDecimal("SALDO").doubleValue();
  }
  
  private double getRealSaldo() {
    if (ds.getBigDecimal("ID").signum() != 0) return getSaldo();
    return -getSaldo();
    //return getID() - getIP();
  }
  
  public double getRsaldo()
  {
    if (rik.isKupac()) return getRealSaldo();
    return -getRealSaldo();
  }
  
  public String getVD() {
    if (raVrdokMatcher.isUplata(ds)) return "U";
    else if (raVrdokMatcher.isRacun(ds)) return "R";
    return "K";
  }

  public String getNaslov()
  {
    return "\nDOSPJELI RAÈUNI "+(rik.isKupac() ? "ZA KUPCE" : "DOBAVLJAÈA");
  }

  public String getNazivPartnera()
  {
    return rik.getNazivPartnera(ds.getInt("CPAR"));
  }

  public String getPeriod()
  {
    return "\nna dan "+rdu.dataFormatter(rik.getLastDay());
  }

  public String getGlobPartner()
  {
    return (ds.getInt("CPAR")+"    "+getNazivPartnera());
  }
  public String getParAdress()
  {
    int cpar = ds.getInt("CPAR");
    // Nije potrebno ako se prvo zove getNazivPartnera(), jer se tamo pozicionira partner (ab.f)
//    lookupData.getlookupData().raLocate(dm.getPartneri(), new String[] {"CPAR"}, new String[] {""+cpar});
    return (dm.getPartneri().getInt("PBR") + " " +dm.getPartneri().getString("MJ")+
        ", "+dm.getPartneri().getString("ADR"));
  }

  public String getPartnerLab()
  {
    if(rik.isKupac())
      return ("Kupac ");
    return ("Dobavlja\u010D ");
  }

  public String getPartnerAdresa()
  {
    return (getGlobPartner()+"   "+getParAdress());
  }
  
  public String getKontoLab() {
    return rik.getKonto() == null || rik.getKonto().length() == 0 ? "" : "Konto";
  }
  
  public String getKonto() {
    return rik.getKonto() == null ? "" : rik.getKonto();
  }

  public double getTotRac()
  {
    return totals.getBigDecimal("TOTALKRN").doubleValue();
  }

  public double getTotUpl()
  {
    return totals.getBigDecimal("TOTALKUP").doubleValue();
  }

  public double getTotKO()
  {
    return totals.getBigDecimal("TOTALKOB").doubleValue();
  }
  
  public double getTotKOU()
  {
    return totals.getBigDecimal("TOTALKOBU").doubleValue();
  }

  public double getSaldoRac()
  {
    return totals.getBigDecimal("SALDOKRN").doubleValue();
  }

  public double getSaldoUpl()
  {
    return totals.getBigDecimal("SALDOKUP").doubleValue();
  }

  public double getSaldoKO()
  {
    return totals.getBigDecimal("SALDOKOB").doubleValue();
  }
  
  public double getSaldoKOU()
  {
    return totals.getBigDecimal("SALDOKOBU").doubleValue();
  }

//*****************************************************************  
}

