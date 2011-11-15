package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Kumulradarh;
import hr.restart.baza.Odbici;
import hr.restart.baza.Odbiciarh;
import hr.restart.baza.Odbiciobr;
import hr.restart.baza.Povjerioci;
import hr.restart.baza.Vrsteodb;
import hr.restart.sisfun.frmParam;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.raTransaction;
import hr.restart.zapod.OrgStr;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.HashSet;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.dataset.ReadWriteRow;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class Harach {
  private static boolean harachOsnPodCreated = false /*true na Derbyju ne valja condition jer ne moze usporediti short i smallint !?*/;
  public static boolean createHarachOsnPod(String harachCVRODB, String harachCPOV) {
    boolean odbicichanged = false;
    if (!harachOsnPodCreated) {
      QueryDataSet vrsteodb = Vrsteodb.getDataModule().getFilteredDataSet(Condition.equal("CVRODB", Short.parseShort(harachCVRODB)));
      QueryDataSet povjerioci = Povjerioci.getDataModule().getFilteredDataSet(Condition.equal("CPOV", Integer.parseInt(harachCPOV)));
      vrsteodb.open();
      povjerioci.open();
      if (vrsteodb.getRowCount() == 0) {
        vrsteodb.insertRow(false);
        vrsteodb.setShort("CVRODB", Short.parseShort(harachCVRODB));
        vrsteodb.setString("OPISVRODB", "Krizni porez");
        vrsteodb.setString("NIVOODB", "RA");
        vrsteodb.setString("TIPODB", "S");
        vrsteodb.setString("VRSTAOSN", "3");
        vrsteodb.setString("OSNOVICA", "3");
        vrsteodb.setInt("CPOV", Integer.parseInt(harachCPOV));
        vrsteodb.setBigDecimal("STOPA", new BigDecimal("-1.00"));
        vrsteodb.post();
        vrsteodb.saveChanges();
        odbicichanged = true;
      }
      if (povjerioci.getRowCount() == 0) {
        povjerioci.insertRow(false);
        povjerioci.setInt("CPOV", Integer.parseInt(harachCPOV));
        povjerioci.setString("NAZPOV", "Državni proraèun");
        povjerioci.setString("MJESTO", ".");
        povjerioci.setString("ADRESA", ".");
        povjerioci.setString("NACISP", "2");
        povjerioci.setString("PNBO1", "21");
        povjerioci.setString("PNBO2", "1902-$matbr");
        povjerioci.setString("ZIRO", "1001005-1863000160");
        povjerioci.setString("SVRHA", "Poseban porez na neto plaæe isplaæene u $mi/$godi");
        povjerioci.saveChanges();
        odbicichanged = true;
      }
      harachOsnPodCreated = true;
    }
    return odbicichanged;
  }
  private static HashMap haracParams = null;
  /**
   * 
   * @param parname CVRODB CPOV limit1 limit2 stopa1 stopa2
   * @return
   */
  public static String getHarachParam(String parname) {
    if (haracParams == null) {
      haracParams = new HashMap();
      haracParams.put("CVRODB", frmParam.getParam("pl", "krizpcvrodb", "8765", "Oznaka vrste odbitka za krizni porez"));
      haracParams.put("CPOV", frmParam.getParam("pl", "krizpcpov", "8765", "Oznaka povjerioca-virmana za krizni porez"));
      haracParams.put("limit1", frmParam.getParam("pl", "krizposn1", "3000.01", "Minimalna osnovica za 2% kriznog poreza"));
      haracParams.put("limit2", frmParam.getParam("pl", "krizposn2", "6000.01", "Minimalna osnovica za 4% kriznog poreza"));
      haracParams.put("stopa1", frmParam.getParam("pl", "krizpstopa1", "2.00", "Stopa za 2% kriznog poreza :)"));
      haracParams.put("stopa2", frmParam.getParam("pl", "krizpstopa2", "4.00", "Stopa za 4% kriznog poreza :)"));
    }
    return (String)haracParams.get(parname);
  }
  public static BigDecimal getStopa(BigDecimal harachosnovica) {
    if (harachosnovica.compareTo(new BigDecimal(getHarachParam("limit1"))) < 0) return null;
    BigDecimal stopa = new BigDecimal(getHarachParam("stopa1"));
    if (harachosnovica.compareTo(new BigDecimal(getHarachParam("limit2"))) >= 0) {
      stopa = new BigDecimal(getHarachParam("stopa2"));
    }
    return stopa;
  }
  
  public static ReadRow addHaracOdbitak(String cradnik, BigDecimal harachosnovica, QueryDataSet harachset) {
    Util ut = Util.getUtil();
    QueryDataSet _odbiciobr = Odbiciobr.getDataModule().getTempSet(Condition.nil);
    _odbiciobr.open();
    BigDecimal stopa = getStopa(harachosnovica);
    if (stopa == null) return null;
    harachset.insertRow(false);
    harachset.setShort("CVRODB", Short.parseShort(Harach.getHarachParam("CVRODB")));
    harachset.setString("CKEY", cradnik);
    harachset.setString("CKEY2", "");
    harachset.setShort("RBRODB", (short)0);
    harachset.setBigDecimal("SALDO", Aus.zero2);
    harachset.setBigDecimal("GLAVNICA", Aus.zero2);
    harachset.setBigDecimal("STOPA", stopa);
    harachset.post();
    
    _odbiciobr.insertRow(false);
    setValues(harachset,_odbiciobr);
    _odbiciobr.setString("CRADNIK",cradnik);
    _odbiciobr.setShort("CVRP",Short.parseShort("0"));
    _odbiciobr.setShort("RBR",Short.parseShort("0"));
    _odbiciobr.setBigDecimal("OBRIZNOS",ut.setScale(harachosnovica.multiply(stopa.divide(new BigDecimal("100.00"), BigDecimal.ROUND_HALF_UP)),2));
    _odbiciobr.setBigDecimal("OBRSTOPA",ut.setScale(stopa,2));
    _odbiciobr.setBigDecimal("OBROSN",ut.setScale(harachosnovica,2));
    _odbiciobr.setBigDecimal("SALDO",ut.setScale(Aus.zero2,2));
    _odbiciobr.post();
    return _odbiciobr;
  }
  /**
   * 
   * @param godmj
   * @param cradnik
   * @param corg
   * @return
   */
  public static BigDecimal[] getHaracMj(String godmj, String cradnik, String corg) {
    BigDecimal osn = Aus.zero2, izn = Aus.zero2, cnt = Aus.zero2;
    String t,tt,join,when, radnik;
    VarStr q = new VarStr("SELECT [t].cradnik, [t].obrosn, [t].obriznos from [tt] where cvrodb=" + getHarachParam("CVRODB") +
    		" [radnik] [join] [when]");
    if (godmj==null || godmj.trim().length()!=6) {
      t="odbiciobr";
      tt="odbiciobr, radnici";
      join = "AND odbiciobr.cradnik = radnici.cradnik";
      when = "";
    } else {
      t = "odbiciarh";
      tt = "odbiciarh, kumulorgarh, kumulradarh";
      join = "AND kumulradarh.godobr = kumulorgarh.godobr " +
      		"AND kumulradarh.mjobr = kumulorgarh.mjobr " +
      		"AND kumulradarh.rbrobr = kumulorgarh.rbrobr " +
      		"AND kumulradarh.cvro = kumulorgarh.cvro " +
      		"AND kumulradarh.corg = kumulorgarh.corg " +
      		"AND kumulradarh.godobr = odbiciarh.godobr " +
      		"AND kumulradarh.mjobr = odbiciarh.mjobr " +
      		"AND kumulradarh.rbrobr = odbiciarh.rbrobr " +
      		"AND kumulradarh.cradnik = odbiciarh.cradnik";
        //"AND odbiciarh.godobr = kumulorgarh.godobr AND odbiciarh.mjobr = kumulorgarh.mjobr AND odbiciarh.rbrobr = kumulorgarh.rbrobr";
      Calendar c = Calendar.getInstance();
      c.set(Integer.parseInt(godmj.substring(0, 4)), Integer.parseInt(godmj.substring(4,6))-1, 1);
      Timestamp datumispl = new Timestamp(c.getTimeInMillis());
      when = " AND "+Condition.between("kumulorgarh.datumispl", 
                        Util.getUtil().getFirstDayOfMonth(datumispl), 
                        Util.getUtil().getLastDayOfMonth(datumispl));
    }
    if (cradnik == null) {
      if (godmj==null || godmj.trim().length()!=6) {
        radnik = "AND (radnici.corg in "+OrgStr.getOrgStr().getInQuery(OrgStr.getOrgStr().getOrgstrAndKnjig(corg), "radnici.cradnik")+")";
      } else {
        OrgStr ors = OrgStr.getOrgStr();
        radnik = "AND (kumulradarh.corg in "+ors.getInQuery(ors.getOrgstrAndCurrKnjig(),"kumulradarh.corg")+")";
      }
    } else {
      radnik = "AND "+t+".cradnik = '" + cradnik + "'";
    }
    String qry = q
      .replaceAll("[t]", t)
      .replaceAll("[tt]", tt)
      .replaceAll("[radnik]", radnik)
      .replaceAll("[join]", join)
      .replaceAll("[when]", when).toString();
  
    System.out.println("getHaracMj qry: "+qry);
    QueryDataSet qds = Aus.q(qry);
    HashSet cnter = new HashSet();
    for (qds.first(); qds.inBounds(); qds.next()) {
      osn = osn.add(qds.getBigDecimal("OBROSN"));
      izn = izn.add(qds.getBigDecimal("OBRIZNOS"));
      cnter.add(qds.getString("CRADNIK"));
    }
    cnt = new BigDecimal(cnter.size());
    return new BigDecimal[] {osn, izn, cnt};
  }
  private static void setValues(ReadRow src, ReadWriteRow dest) {
    String[] dcolNames = dest.getColumnNames(dest.getColumnCount());
    String[] scolNames = src.getColumnNames(src.getColumnCount());
    Variant v = new Variant();
    for (int i = 0; i < scolNames.length; i++) {
      if (Util.getUtil().containsArr(dcolNames,scolNames[i])) {
        src.getVariant(scolNames[i],v);
        dest.setVariant(scolNames[i],v);
      }
    }
  }
  
  //hr.restart.pl.Harach.otmiHarachArhiva(2009,6,1,"01")
  public static void otmiHarachArhiva(int god, int mj, int rbr, String corg) {
    ArrayList transactsets = new ArrayList();
    QueryDataSet kumorgs;
    if (corg!=null) {
      kumorgs = Kumulorgarh.getDataModule().getFilteredDataSet("GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+" and "+sjQuerys.getPripOrg(corg, "", ""));
    } else {
      kumorgs = Kumulorgarh.getDataModule().getFilteredDataSet("GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr);
    }
    kumorgs.open();
    for (kumorgs.first(); kumorgs.inBounds(); kumorgs.next()) {
      QueryDataSet kumrads = Kumulradarh.getDataModule().getFilteredDataSet(Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR","CORG"}, kumorgs));
      kumrads.open();
      QueryDataSet harachset = Odbici.getDataModule().getTempSet(Condition.nil);
      harachset.open();
      QueryDataSet odbiciarh = Odbiciarh.getDataModule().getTempSet(Condition.nil);
      odbiciarh.open();
      for (kumrads.first(); kumrads.inBounds(); kumrads.next()) {
        QueryDataSet haracharhset = Odbiciarh.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"GODOBR","MJOBR","RBROBR","CRADNIK"}, kumrads)
            .and(Condition.equal("CVRODB", Short.parseShort(getHarachParam("CVRODB"))).and(Condition.equal("CKEY",kumrads.getString("CRADNIK")))));
        haracharhset.open();
        if (haracharhset.getRowCount() > 0) continue;//otelo vec
        ReadRow _odbiciobr = addHaracOdbitak(kumrads.getString("CRADNIK"), kumrads.getBigDecimal("NETO2"), harachset);
        if (_odbiciobr == null) continue;
        odbiciarh.insertRow(false);
        setValues(_odbiciobr, odbiciarh);
        odbiciarh.setShort("GODOBR", (short)god);
        odbiciarh.setShort("MJOBR", (short)mj);
        odbiciarh.setShort("RBROBR", (short)rbr);
        odbiciarh.post();
        kumrads.setBigDecimal("KREDITI",kumrads.getBigDecimal("KREDITI").add(odbiciarh.getBigDecimal("OBRIZNOS")));
        kumrads.setBigDecimal("NARUKE",kumrads.getBigDecimal("NARUKE").add(odbiciarh.getBigDecimal("OBRIZNOS").negate()));
        kumorgs.setBigDecimal("KREDITI",kumorgs.getBigDecimal("KREDITI").add(odbiciarh.getBigDecimal("OBRIZNOS")));
        kumorgs.setBigDecimal("NARUKE",kumorgs.getBigDecimal("NARUKE").add(odbiciarh.getBigDecimal("OBRIZNOS").negate()));
//          addBigDec_kumulorg("KREDITI",kumulrad.getBigDecimal("KREDITI"));
//          addBigDec_kumulorg("NARUKE",kumulrad.getBigDecimal("NARUKE"));
        
      }
      transactsets.add(kumrads);
      transactsets.add(odbiciarh);
    }
    transactsets.add(kumorgs);
    raTransaction.saveChangesInTransaction((QueryDataSet[])transactsets.toArray(new QueryDataSet[]{}));
  }
  
  public static String getHaracFlag(DataSet vrsteprim) {
    String flag = raParam.getParam(vrsteprim, 4).trim();
    if (flag.equals("")) {
      flag = vrsteprim.getString("PARAMETRI").trim().substring(0, 2).equals("NN")?"N":"D";
    }
    return flag;
  }
}
