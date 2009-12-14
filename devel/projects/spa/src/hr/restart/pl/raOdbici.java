/****license*****************************************************************
**   file: raOdbici.java
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
package hr.restart.pl;

import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.lookupData;
import hr.restart.zapod.Tecajevi;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Calendar;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.sql.dataset.QueryDataSet;

public class raOdbici {
  private static raOdbici _this;
  private Util ut = Util.getUtil();
  private lookupData ld = lookupData.getlookupData();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private DataSet radnicipl = null;
  private raPlObrRange obrRange = null;

  /**
   * za dohvat odbitaka iz tabele definicije odbitaka (odbici)
   */
  public static int DEF = 0;

  /**
   * za dohvat odbitaka iz tabele obracunatih odbitaka (odbiciobr)
   */
  public static int OBR = 1;

  /**
   * za dohvat odbitaka iz tabele arhiviranih odbitaka (odbiciarh)
   */
  public static int ARH = 2;

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * OLAKSICE
   */
  public static String[] OLAK_param = new String[] {"RA","K","1","4",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * DOPRINOSI RADNIKA na nivou radnika
   * - query za SVE doprinose radnika bi trebao biti:
   * "select <*> from odbici<..> where ("+getOdbiciWhereQuery(DOPR_param1,odbici<..>)+") or ("+getOdbiciWhereQuery(DOPR_param2,odbici<..>)+") AND ..."
   */
  public static String[] DOPR_param1 = new String[] {"RA","S","1","1",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * DOPRINOSI RADNIKA na nivou vrste radnog odnosa
   * - query za SVE doprinose radnika bi trebao biti:
   * "select <*> from odbici<..> where ("+getOdbiciWhereQuery(DOPR_param1,odbici<..>)+") or ("+getOdbiciWhereQuery(DOPR_param2,odbici<..>)+") AND ..."
   */
  public static String[] DOPR_param2 = new String[] {"VR","S","1","1",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * POREZI
   */
  public static String[] POREZ_param = new String[] {"OPVR","S","2","4",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * PRIREZI
   */
  public static String[] PRIREZ_param = new String[] {"OP","S","2","5",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * DOPRINOSI poduzeca (NA)
   */
  public static String[] DOPN_param = new String[] {"POVR","S","1","1",null,null};

  /**
   * pozvati metodu getOdbiciWhereQuery(String[] _param, String tableName) i dobiva se
   * String "CVRODB in (cvo1,cvo2,cvo3...,cvoN) gdje su cvoX oznake vrste odbitaka koje su
   * KREDITI
   */
  public static String[] KREDITI_param = new String[] {"RA","S","3",null,null,null};

  public static String[] RAZPOREZA_param = new String[] {"RA","P","2","0", null, null};
  
  public static String[] RAZPRIREZA_param = new String[] {"RA","R","2","0", null, null};
  
  private QueryDataSet vrsteodb = null;
//cache za specificne getere odbitaka
  private String opc_por = null;
  private String vro_por = null;
  private QueryDataSet qds_porez = null;

  private String opc_prir = null;
  private QueryDataSet qds_prirez = null;

  private String knjig_dopNa = null;
  private String vro_dopNa = null;
  private QueryDataSet qds_doprinosiNa = null;
//endcache

  protected raOdbici() {
  }
  public static raOdbici getInstance() {
    if (_this==null) _this = new raOdbici();
    return _this;
  }
  /**
   *
   * @param nivoodb
   * @param tipodb
   * @param vrstaosn
   * @param osnovica
   * @param ckey
   * @param ckey2
   * @param tableName
   * @return
   */
  public QueryDataSet getOdbici(String nivoodb, String tipodb, String vrstaosn, String osnovica, String ckey, String ckey2, String tableName, String cradnik, int mode) {
    String q = "SELECT * FROM "+tableName
             +" WHERE "+getOdbiciWhereQuery(nivoodb,tipodb,vrstaosn,osnovica,ckey,ckey2,tableName)
             +getCradObrQuery(cradnik,mode);
    QueryDataSet reqOdbici = Util.getNewQueryDataSet(q);
    return reqOdbici;
  }
  /**
   * see getOdbiciWhereQuery
   * @param par
   * @param tableName
   * @param cradnik
   * @param mode
   * @return
   */
  public QueryDataSet getOdbici(String[] par, String tableName, String cradnik, int mode) {
                                                  
    return getOdbici(par[0],par[1],par[2],par[3],
        	  //ako je prvi nivo odbitka radnik i trazi se def, daj mu cradnik
        		(par[4]==null && par[0].startsWith("RA") && mode == DEF)?cradnik:par[4],
        		//ako je drugi nivo odbitka radnik i trazi se def, daj mu cradnik
        		(par[5]==null && par[0].endsWith("RA") && !par[0].equals("RA") && mode == DEF)?cradnik:par[5],
            tableName,cradnik,mode
           );
  }
  private String getCradObrQuery(String cradnik, int mode) {
    String[] ret = new String[] {"",""};
    if (mode!=DEF) ret[0] = " AND CRADNIK = '"+cradnik+"'";
    if (mode==ARH) {
      if (obrRange == null) throw new NullPointerException("Treba setirati obrRange!!");
      ret[1] = " AND "+obrRange.getQuery();
    }
    return ret[0].concat(ret[1]);
  }
  private QueryDataSet vrodbkeys = null;

  private boolean isMatchVrodbKeys(ReadRow row, String nivoodb, String tipodb, String vrstaosn, String osnovica) {
    if (nivoodb != null) {
      if (!nivoodb.equals(row.getString("NIVOODB"))) return false;
    }
    if (tipodb != null) {
      if (!tipodb.equals(row.getString("TIPODB"))) return false;
    }
    if (vrstaosn != null) {
      if (!vrstaosn.equals(row.getString("VRSTAOSN"))) return false;
    }
    if (osnovica != null) {
      if (!osnovica.equals(row.getString("OSNOVICA"))) return false;
    }
    return true;
  }

  public String[] getVrsteOdbKeysQuery(String nivoodb, String tipodb, String vrstaosn, String osnovica,boolean array) {
    String[] ret;
    String qcvrodbs = "";
    if (vrodbkeys == null) vrodbkeys = Util.getNewQueryDataSet("SELECT * FROM vrsteodb WHERE aktiv='D'");
//where"+qnivoodb+qtipodb+qvrstaosn+qosnovica
    java.util.HashSet ckset = new java.util.HashSet();
    vrodbkeys.first();
    do {
      if (isMatchVrodbKeys(vrodbkeys, nivoodb, tipodb, vrstaosn, osnovica)) {
        short _vrodb = vrodbkeys.getShort("CVRODB");
        qcvrodbs = qcvrodbs+_vrodb+",";
        ckset.add(""+_vrodb);
      }
    } while (vrodbkeys.next());
    if (qcvrodbs.equals("")) return new String[] {null};
    qcvrodbs = qcvrodbs.substring(0,qcvrodbs.length()-1);//strip last comma
    if (array) {
      ret = new String[ckset.size()];
      ret = (String[])ckset.toArray(ret);
    } else {
      ret = new String[] {qcvrodbs};
    }
    return ret;
  }

  public String getOdbiciWhereQuery(String[] niv_tip_vr_os_k1_k2, String tableName) {
    String[] s = niv_tip_vr_os_k1_k2;
    return getOdbiciWhereQuery(s[0],s[1],s[2],s[3],s[4],s[5],tableName);
  }


  public String getOdbiciWhereQuery(String nivoodb, String tipodb, String vrstaosn, String osnovica, String ckey, String ckey2, String tableName) {
    /** @todo keshiranje */
    if (tableName == null) throw new NullPointerException("Ime tableName mora biti zadano!!!");
    String qckey = (ckey==null)?"":"AND CKEY = '"+ckey+"'";
    String qckey2 = (ckey2==null)?"":"AND CKEY2 = '"+ckey2+"'";
    if (!(tableName.toLowerCase().equals("odbici") ||
          tableName.toLowerCase().equals("odbiciobr") ||
          tableName.toLowerCase().equals("odbiciarh"))) throw new IllegalArgumentException("Ime tabele moze biti odbici, odbiciarh ili odbiciobr");

    String qcvrodbs = getVrsteOdbKeysQuery(nivoodb, tipodb, vrstaosn, osnovica,false)[0];
    if (qcvrodbs == null) return "0=1";//nema vrsti odbitaka pa returnam prazan set
    return "CVRODB in ("+qcvrodbs+") "+ qckey+qckey2;
  }
  /* pripadajuce metode, getOlak, getDopr, getKred, getPor, getPrir */

  /**
   * dohvaca olaksice - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = RA,K,1,4
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getOlaksice(String cradnik,int mode) {
    return getOdbici("RA","K","1","4",cradnik,null,getOdbiciTable(mode),cradnik,mode);
//    return getOdbici("RA","K","1",null,cradnik,null,getOdbiciTable(mode),cradnik,mode);
  }

  /**
   * dohvaca osiguranja - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = RA,K,1,0
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getPremije(String cradnik, int mode) {
    return getOdbici("RA","K","1","0",cradnik,null,getOdbiciTable(mode),cradnik,mode);
  }

  /**
   * dohvaca pausalno priznati trosak - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = RA,K,1,1
   * za autorske i umjetnicke ugovore
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getPausalniOdbitak(String cradnik, int mode) {
    return getOdbici("RA","K","1","1",cradnik,null,getOdbiciTable(mode),cradnik,mode);
  }
  /**
   * dohvaca doprinose radnika - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = RA,S,1,1 or VR,S,1,1
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getDoprinosiRadnik(String cradnik, int mode) {
    String vro = getKeyFromRadnicipl(cradnik,"CVRO");
    String odbici1 = getOdbiciWhereQuery("RA","S","1","1",cradnik,null,getOdbiciTable(mode));
    String odbici2 = getOdbiciWhereQuery("VR","S","1","1",vro,null,getOdbiciTable(mode));
    return Util.getNewQueryDataSet(
        "SELECT * FROM "+getOdbiciTable(mode)+" WHERE (("+odbici1+") OR ("+odbici2+")) "+
        getCradObrQuery(cradnik,mode) + " ORDER BY CVRODB, RBRODB "
        );
  }

  public static void clearCache() {
    getInstance().qds_porez = null;
    getInstance().qds_prirez = null;
    getInstance().qds_doprinosiNa = null;
  }
  /**
   * dohvaca poreze za radnika - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = OPVR,S,2,4
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getPorez(String cradnik, int mode) {
    String _opc = getKeyFromRadnicipl(cradnik,"COPCINE");
    String _vro = getKeyFromRadnicipl(cradnik,"CVRO");
    if (qds_porez != null && _opc.equals(opc_por) && _vro.equals(vro_por)) return qds_porez;
    opc_por = _opc;
    vro_por = _vro;
    qds_porez = getOdbici("OPVR","S","2","4",opc_por,vro_por,getOdbiciTable(mode),cradnik,mode);
    return qds_porez;
  }

  /**
   * dohvaca prirez za radnika - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = OP,S,2,5
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getPrirez(String cradnik, int mode) {
    String _opc = getKeyFromRadnicipl(cradnik,"COPCINE");
    if (qds_prirez != null && _opc.equals(opc_prir)) return qds_prirez;
    opc_prir = _opc;
    qds_prirez = getOdbici("OP","S","2","5",opc_prir,null,getOdbiciTable(mode),cradnik,mode);
    return qds_prirez;
  }

  /**
   * dohvaca doprinose poduze\u0107a za radnika - odbitke koji imaju vrsteodb (nivo,tip,vrsta,osnovica) = POVR,S,1,1
   * @param cradnik radnik za kojeg trebamo podatke
   * @param mode moze biti DEF,OBR,ARH
   * @return
   */
  public QueryDataSet getDoprinosiNa(String cradnik, int mode) {
    String _knjig = hr.restart.zapod.OrgStr.getKNJCORG();
    String _vro = getKeyFromRadnicipl(cradnik,"CVRO");
    if (qds_doprinosiNa != null && _knjig.equals(knjig_dopNa)
        && _vro.equals(vro_dopNa) && mode == DEF && (qds_doprinosiNa.hasColumn("STOPA")!=null))
      return qds_doprinosiNa;

    knjig_dopNa = _knjig;
    vro_dopNa = _vro;
    qds_doprinosiNa = getOdbici("POVR","S","1","1",knjig_dopNa,vro_dopNa,getOdbiciTable(mode),cradnik,mode);
    return qds_doprinosiNa;
  }


  public QueryDataSet getKrediti(String cradnik,int mode) {
    return getOdbici("RA","S","3",null,cradnik,null,getOdbiciTable(mode),cradnik,mode);
  }

  private String getKeyFromRadnicipl(String cradnik,String keyName) {
    if (radnicipl != null) return radnicipl.getString(keyName);
    if (dm.getAllRadnicipl().isOpen() && dm.getAllRadnicipl().getString("CRADNIK").equals(cradnik)) {
      return dm.getAllRadnicipl().getString(keyName);
    }
    String val;
    if (dm.getAllRadnicipl().isOpen()
    && ld.raLocate(dm.getAllRadnicipl(),new String[] {"CRADNIK"},new String[] {cradnik})) {
      val = dm.getAllRadnicipl().getString(keyName);
    } else {
      QueryDataSet _radnicipl = Util.getNewQueryDataSet("SELECT * FROM radnicipl where cradnik = '"+cradnik+"'");
      if (_radnicipl.getRowCount() == 0) return null;
      _radnicipl.first();
      val = _radnicipl.getString(keyName);
    }
    return val;
  }

  public void setRadniciPL(DataSet _radnicipl) {
    radnicipl = _radnicipl;
  }
  public void setObrRange(raPlObrRange range) {
    obrRange = range;
  }
  public void setObrRange(short gfrom,short mfrom,short rbrfrom, short gto, short mto, short rbrto) {
    obrRange = new raPlObrRange(gfrom,mfrom,rbrfrom,gto,mto,rbrto);
  }
  public void setObrRange(short g,short m,short rbr) {
    obrRange = new raPlObrRange(g,m,rbr);
  }
  private String getOdbiciTable(int mode) {
    if (mode == DEF) return "odbici";
    if (mode == OBR) return "odbiciobr";
    if (mode == ARH) return "odbiciarh";
    return null;
  }

  public String getVrstaOsnKumulrad_CN(String vrstaosn) {
    if (vrstaosn.equals("1")) {
      return "BRUTO";
    } else if (vrstaosn.equals("2")) {
      return "POROSN";
    } else if (vrstaosn.equals("3")) {
      return "NETOPK";
    }
    return null;
  }
  public QueryDataSet get_vrsteodb() {
    if (vrsteodb == null) vrsteodb = Util.getNewQueryDataSet("SELECT * FROM vrsteodb");
    return vrsteodb;
  }
  public void null_vrsteodb() {
    vrsteodb = null;
  }
  private BigDecimal getDomIznos(ReadRow odbitak) {
    BigDecimal retIznos = odbitak.getBigDecimal("IZNOS");
    try {
      String oznval = odbitak.getString("OZNVAL");
      hr.restart.zapod.Tecajevi tec = null;
      if (oznval.equals("")) return retIznos;
      if (Tecajevi.getDomOZNVAL().equals(oznval)) return retIznos;
      BigDecimal tecaj = Tecajevi.getTecaj(raObracunPL.getInstance().datumispl,oznval);
      BigDecimal jedval = null;
      if (jedvalset != null && jedvalset.getRowCount() > 0 && jedvalset.getString("OZNVAL").equals(oznval)) {
        jedval = new BigDecimal(jedvalset.getInt("JEDVAL")+".00");
      } else {
        jedvalset = Util.getNewQueryDataSet("SELECT valute.oznval,valute.jedval from valute where valute.oznval = '"+oznval+"'");
        jedvalset.first();
        if (jedvalset.getRowCount() > 0) {
          jedval = new BigDecimal(jedvalset.getInt("JEDVAL")+".00");
        }
      }
      if (jedval == null) return retIznos;
      return retIznos.multiply(tecaj.divide(jedval,8,BigDecimal.ROUND_HALF_UP));
    }
    catch (Exception ex) {
      return retIznos;
    }
  }

  private java.util.HashMap preSums_calcOdbitak;
  public void setPreSum_calcOdbitak(String id,BigDecimal preSum) {
    if (preSums_calcOdbitak == null) preSums_calcOdbitak = new java.util.HashMap();
    preSums_calcOdbitak.put(id,preSum);
  }

  private QueryDataSet jedvalset;
  public CalcRes calcOdbitak(ReadWriteRow kumulrad, ReadRow odbitak, String negColName, boolean updKumul) {
    //if (vrsteodb == null) vrsteodb = Util.getNewQueryDataSet("SELECT * FROM vrsteodb");
    get_vrsteodb();
    CalcRes res = new CalcRes();
    String cvrodb = odbitak.getShort("CVRODB")+"";
    if (!ld.raLocate(vrsteodb,new String[]{"CVRODB"},new String[]{cvrodb})) return null;
    String tipodb = vrsteodb.getString("TIPODB"); //K-kalkulativni, S-stvarni, P - razlika poreza, R - razlika prireza
    String vrstaosn = vrsteodb.getString("VRSTAOSN"); //od kojeg se iznosa odbija: 1-Bruto,2-Porezna osnovica,3-Neto
    String osnovica = vrsteodb.getString("OSNOVICA"); //na sto ide %: 0-Fiksni iznos, 1-Bruto,2-Porezna osnovica,
                                                      //3-Neto,4-Zakonska, 5-Porez
    res.saldo = odbitak.getBigDecimal("SALDO"); //saldo za odbitke sa glavnicom i saldom
    //prije cijele operacije provjeriti datume  i salda za RA-S-3-null
    if (vrsteodb.getString("NIVOODB").equals("RA") && tipodb.equals("S") && vrstaosn.equals("3")) {
    	if (odbitak.getBigDecimal("GLAVNICA").signum()!=0 && odbitak.getBigDecimal("SALDO").signum()==0) {
    	  System.out.println("calcOdbitak retnull 001 (gl >0 & saldo=0)");    		
    	  return null;
    	}
    	//raIniciranje.getInstance().posOrgsPl(kumulrad.getString("CORG"));
    	Calendar cal = Calendar.getInstance();
    	cal.set(dm.getOrgpl().getShort("GODOBR"),dm.getOrgpl().getShort("MJOBR"),1);
    	Timestamp datza = new Timestamp(cal.getTime().getTime());
    	if (!odbitak.isNull("DATPOC") && odbitak.getTimestamp("DATPOC").compareTo(ut.getLastDayOfMonth(datza)) > 0) {
    	  System.out.println("calcOdbitak retnull 002 (datpoc>datobr)");    		 
    	  return null;
    	}
    	if (!odbitak.isNull("DATZAV") && odbitak.getTimestamp("DATZAV").compareTo(ut.getFirstDayOfMonth(datza)) <= 0) {
    	  System.out.println("calcOdbitak retnull 003 (datzav<=datobr)");    		
    	  return null;
    	}

    }
    //od kojeg iznosa odbijam
    if (negColName == null) {
      res.negateColumnName = getVrstaOsnKumulrad_CN(vrstaosn);
    } else {
      res.negateColumnName = negColName;
    }
    if (res.negateColumnName == null) {
      System.out.println("calcOdbitak retnull 004 (negateColumnName == null)");
      return null;
    }
    //kroz 100
    BigDecimal divider = new BigDecimal("100.00");
    //osnovica
    if (osnovica.equals("0")) {
      res.obrosn = res.nula;
    } else if (osnovica.equals("1")) {
      res.obrosn = kumulrad.getBigDecimal("BRUTO");
    } else if (osnovica.equals("2")) {
      res.obrosn = kumulrad.getBigDecimal("POROSN");
    } else if (osnovica.equals("3")) {
      String netocol = 
        frmParam.getParam("pl", "net2posto"+cvrodb,"D","U neto osnovicu za odbitak u postotku " +
        cvrodb + " uraèunati i naknade (D/N").equals("D")?"NETOPK":"NETO2";
      res.obrosn = kumulrad.getBigDecimal(netocol);
    } else if (osnovica.equals("4")) {
      if (tipodb.equals("K") && vrstaosn.equals("1")) {
        res.obrosn = dm.getParametripl().getBigDecimal("MINPL");
        divider = new BigDecimal("1.00");
      }
    } if (osnovica.equals("5")) {
      res.obrosn = kumulrad.getBigDecimal("PORUK");
    }
    //maximalni iznosi iz parametra maxosn+cvrodb
    String s_maxos = hr.restart.sisfun.frmParam.getParam("pl","maxosn".concat(cvrodb), "0", "Maksimalna osnovica za doprinos ".concat(cvrodb));
    if (!s_maxos.equals("0")) {
      try {
        BigDecimal maxos = new BigDecimal(s_maxos);
        maxos = maxos.setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal preos = (BigDecimal)preSums_calcOdbitak.get("preosn");
        if (preos == null) preos = Aus.zero2;
        if (res.obrosn.add(preos).compareTo(maxos) > 0) {
          res.obrosn = maxos.add(preos.negate());
          if (res.obrosn.signum() < 0) res.obrosn = Aus.zero2;
        }
      }
      catch (Exception ex) {
        System.out.println("Vjerojatno nije greska, ali pri citanju parametra maxosn"+cvrodb+" : "+ex);
      }
    }
    //minimalni iznosi iz parametra minosn+cvrodb
    String s_minos = hr.restart.sisfun.frmParam.getParam("pl","minosn".concat(cvrodb), "0", "Minimalna osnovica za doprinos ".concat(cvrodb));
    if (!s_minos.equals("0") && res.obrosn.scale() > 0) {
      try {
        BigDecimal minos = new BigDecimal(s_minos);
        minos = minos.setScale(2,BigDecimal.ROUND_HALF_UP);
        BigDecimal preos = (BigDecimal)preSums_calcOdbitak.get("preosn");
        if (preos == null) preos = Aus.zero2;
        if (res.obrosn.add(preos).compareTo(minos) < 0) {
//          res.obrosn = minos.add(preos.negate());
//          if (res.obrosn.signum() < 0) res.obrosn = Aus.zero2;
          res.obrosn = minos;
        }
      }
      catch (Exception ex) {
        System.out.println("Vjerojatno nije greska, ali pri citanju parametra maxosn"+cvrodb+" : "+ex);
      }
    }
    //stopa
    res.obrstopa = odbitak.getBigDecimal("STOPA");
    //iznos
    if (osnovica.equals("0")) {
      //valutna klauzula
      //res.obriznos = odbitak.getBigDecimal("IZNOS");
      res.obriznos = getDomIznos(odbitak);
      //ako nema za kredit ne uzimaj nista
      if (ut.setScale(res.obriznos,2).compareTo(ut.setScale(kumulrad.getBigDecimal(res.negateColumnName),2)) > 0 ) {
      	//ne odbija nista, parametrizirati u buducnosti da odbije koliko moze
        System.out.println("calcOdbitak retnull 005 (obriznos("+ut.setScale(res.obriznos,2)+") > negatevalue("+ut.setScale(kumulrad.getBigDecimal(res.negateColumnName),2)+"))");
      	return null;
      }
    } else {
      BigDecimal stopa = res.obrstopa.setScale(8).divide(divider,BigDecimal.ROUND_HALF_UP);
      res.obriznos = res.obrosn.multiply(stopa);
    }
    //handlanje salda
//System.out.println("HANDLAM KREDIT "+odbitak);
//System.out.println(res);
    if (res.saldo.compareTo(res.nula) > 0) {
			BigDecimal _save_saldo = res.saldo; 
      res.saldo = ut.setScale(ut.setScale(res.saldo,2).add(ut.setScale(res.obriznos.negate(),2)),2);
      if (res.saldo.compareTo(res.nula) < 0) {//istekao & previse oteo
        //res.obriznos = res.saldo.setScale(8,BigDecimal.ROUND_HALF_UP);
        res.obriznos = _save_saldo.setScale(8,BigDecimal.ROUND_HALF_UP);
        res.saldo = res.nula.setScale(8);
      }
    }
    // update
    if (updKumul) {
      kumulrad.setBigDecimal(res.negateColumnName,
                             kumulrad.getBigDecimal(res.negateColumnName)
                             .add(res.obriznos.setScale(2,BigDecimal.ROUND_HALF_UP).negate()));
    }
//    System.out.println("neposredno prije returna " + res);
    return res;
  }

  /**
   * rezultat obracuna odbitka
   */
  public class CalcRes {
    public BigDecimal obrosn;
    public BigDecimal obrstopa;
    public BigDecimal obriznos;
    public BigDecimal saldo;
    public BigDecimal nula = new BigDecimal(0);
    public String negateColumnName = "";

    public CalcRes() {
      obrosn = nula.setScale(8);
      obrstopa = nula.setScale(8);
      obriznos = nula.setScale(8);
      saldo = nula.setScale(8);
    }
    public String toString() {
      return "raOdbici.CalcRes: obrosn = "+obrosn+"\n"+
             "                obrstopa = "+obrstopa+"\n"+
             "                obriznos = "+obriznos+"\n"+
             "                   saldo = "+saldo+"\n";
    }
    
  }

  public static void addDefault() {
    /** @todo
     1. dodati defaultne vrste odbitaka prema String[]ovima iz raOdbici
     2. procedure za dodavanje defaultnih odbitaka na tabele nivoa odbitka (radnici,vro, opcine, zarade ...)
     */
  }
}