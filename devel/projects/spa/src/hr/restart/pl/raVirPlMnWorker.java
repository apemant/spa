/****license*****************************************************************
**   file: raVirPlMnWorker.java
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
/*
 * raVirPlMnWorker.java
 *
 * Created on 2003. studeni 10, 10:55
 */

package hr.restart.pl;

import hr.restart.baza.Condition;
import hr.restart.baza.Odbici;
import hr.restart.baza.Radnici;
import hr.restart.baza.Radnicipl;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.raMnemVar;
import hr.restart.util.raMnemonics;

import com.borland.dx.sql.dataset.QueryDataSet;
/**
 *
 * @author  andrej
 */
public class raVirPlMnWorker extends hr.restart.util.raMnemWorker {
  
  private static raVirPlMnWorker _this;
  private lookupData ld = lookupData.getlookupData();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  String strVrOdb, strPov, strRad, strRbrOdb;
  QueryDataSet povDS;
  QueryDataSet striped_radpl;
  QueryDataSet radnici;

  protected raVirPlMnWorker() {
    super("virmaniPL");
    init();
    raMnemonics.addWorker(this);
  }
  
  public static raVirPlMnWorker getInstance() {
    if (_this == null) _this = new raVirPlMnWorker();
    return _this;
  }
  private void init() {
    striped_radpl = Radnicipl.getDataModule().getFilteredDataSet("cradnik, brojtek, jmbg","");
    striped_radpl.open();
    radnici = Radnici.getDataModule().copyDataSet();
    radnici.open();
    
    addVar(new raMnemVar("$matbr","Matièni broj poduzeæa") {
      public String getText() {
        ld.raLocate(dm.getLogotipovi(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getLogotipovi().getString(raObracunPL.isOIB()?"OIB":"MATBROJ");
      }
    });

    addVar(new raMnemVar("$cop","Oznaka opæine") {
      public String getText() {
        String ckeySif="";
        String cKeyVal="";
        ld.raLocate(dm.getVrsteodb(),new String[] {"CVRODB", "CPOV"},new String[] {strVrOdb, strPov});
        String nivoOdb = dm.getVrsteodb().getString("NIVOODB");
        if(nivoOdb.length()==2 && nivoOdb.equals("OP"))
          ckeySif = "CKEY";
        else if(nivoOdb.length()==4) {
          if(nivoOdb.substring(0,2).equals("OP"))
            ckeySif = "CKEY";
          else if(nivoOdb.substring(2,4).equals("OP"))
            ckeySif = "CKEY2";
          else
            return "";
        } else  return "";
        ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
        return dm.getOdbiciobr().getString(ckeySif);
      }
    });

    addVar(new raMnemVar("$mo","Mjesec obrade") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getOrgpl().getShort("MJOBR") + "";        
      }
    });

    addVar(new raMnemVar("$godo","Godina obrade") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getOrgpl().getShort("GODOBR") + "";
      }
    });
    
    addVar(new raMnemVar("$stopaobr","Stopa odbitka") {
      public String getText() {
        ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
        return dm.getOdbiciobr().getBigDecimal("OBRSTOPA").toString();        
      }
    });
    
    addVar(new raMnemVar("$osnobr","Osnovica za odbitak") {
      public String getText() {
        ld.raLocate(dm.getOdbiciobr(), new String[]{"CVRODB", "CRADNIK"}, new String[]{strVrOdb, strRad});
        return dm.getOdbiciobr().getBigDecimal("OBROSN").toString();        
      }
    });
    
    addVar(new raMnemVar("$rsind","Identifikator RS") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getOrgpl().getString("RSIND");        
      }
    });
    
    addVar(new raMnemVar("$nazpov", "Naziv povjerioca") {
      public String getText() {
        String cPov = povDS.getInt("CPOV") + "";
        ld.raLocate(dm.getPovjerioci(), new String[]{"CPOV"}, new String[]{cPov});
        return dm.getPovjerioci().getString("NAZPOV");        
      }
    });
    
    addVar(new raMnemVar("$regbrmio","Reg. br. MIO") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getOrgpl().getString("REGBRMIO");    
      }
    });
    
    addVar(new raMnemVar("$regbrzo","Reg. br. ZO") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return dm.getOrgpl().getString("REGBRZO");        
      }
    });

    addVar(new raMnemVar("$datisp","Datum isplate") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL"));        
      }
    });
    
    addVar(new raMnemVar("$jmbg","JMBG radnika") {
      public String getText() {
        ld.raLocate(striped_radpl, new String[]{"CRADNIK"}, new String[]{strRad});
        return striped_radpl.getString("JMBG");        
      }
    });
    
    addVar(new raMnemVar("$tek","Tekuæi raèun radnika") {
      public String getText() {
        ld.raLocate(striped_radpl, new String[]{"CRADNIK"}, new String[]{strRad});
        return striped_radpl.getString("BROJTEK");
      }
    });
    
    addVar(new raMnemVar("$ime","Ime radnika") {
      public String getText() {
        ld.raLocate(dm.getRadnici(), new String[]{"CRADNIK"}, new String[]{strRad});
        return dm.getRadnici().getString("IME");        
      }
    });
    
    addVar(new raMnemVar("$prez","Prezime radnika") {
      public String getText() {
        ld.raLocate(dm.getRadnici(), new String[]{"CRADNIK"}, new String[]{strRad});
        return dm.getRadnici().getString("PREZIME");        
      }
    });
    
    addVar(new raMnemVar("$mi","Mjesec isplate") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL")).substring(3,5);        
      }
    });
    
    addVar(new raMnemVar("$godi","Godina isplate") {
      public String getText() {
        ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
        return DateFormat(dm.getOrgpl().getTimestamp("DATUMISPL")).substring(6,10);        
      }
    });
    
    addVar(new raMnemVar("$opz","Žiro opæine") {
      public String getText() {
        if (!(lastCradnikForSelectedCopcine!=null && lastCradnikForSelectedCopcine.equals(strRad))) {
          fillSelectedCopcine();
        }
        return getZiroOpc(lastSelectedCopcine);        
      }
    });
    
    addVar(new raMnemVar("$opmj","Mjesto opæine") {
      public String getText() {
        if (!(lastCradnikForSelectedCopcine!=null && lastCradnikForSelectedCopcine.equals(strRad))) {
          fillSelectedCopcine();
        }
        return locateOpcine();
        }
        String locateOpcine() {
        ld.raLocate(dm.getOpcine(),"COPCINE",lastSelectedCopcine);
        return dm.getOpcine().getString("NAZIVOP");
      }
    });
    
    addVar(new raMnemVar("$pnb2odb","Poziv na broj 2 odbitka") {
      public String getText() {
        return getFromOdbici("PNB2");
      }
    });
    
    addVar(new raMnemVar("$banposl","Broj poslovnice banke") {
      public String getText() {
        return getFromBankepl("BRPOSL");
      }
    });
    
    addVar(new raMnemVar("$bandom","Broj domicilne banke") {
      public String getText() {
        return getFromBankepl("BRDOM");
      }
    });
    
    addVar(new raMnemVar("$pnbzN", "Poziv na broj zaduzenja za NETO (nn 49/12)") {
      
      public String getText() {
        return getPnb2zNeto();
      }
    });
  }

//  
//optimizacija za opcinu

  private String lastSelectedCopcine;
  private String lastCradnikForSelectedCopcine;
  private String lastSelectedMjestoOpcine;

  void fillSelectedCopcine() {
    QueryDataSet qds = Util.getNewQueryDataSet("SELECT copcine FROM radnicipl WHERE cradnik = '"+strRad+"'");
    lastSelectedCopcine = qds.getString("COPCINE");
    lastCradnikForSelectedCopcine = strRad;
  }
  
  private String DateFormat(java.sql.Timestamp _date) {
    String tempDate = _date.toString();
    String year =  tempDate.substring(0,4);
    String month = tempDate.substring(5,7);
    String day = tempDate.substring(8,10);
    return day+"."+month+"."+year+".";
  }

  private String getPnb2zNeto() {
    String ret = "";
    ld.raLocate(dm.getLogotipovi(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    ret = dm.getLogotipovi().getString(raObracunPL.isOIB()?"OIB":"MATBROJ");
    ld.raLocate(dm.getOrgpl(),new String[] {"CORG"},new String[] {hr.restart.zapod.OrgStr.getKNJCORG()});
    ret = ret + "-" + Valid.getValid().maskZeroInteger(new Integer(dm.getOrgpl().getShort("MJOBR")), 2);
    ret = ret + Short.toString(dm.getOrgpl().getShort("GODOBR")).substring(2);
//    Podatak o plaæi:
//      0 – isplata plaæe u cijelosti
//      1 – isplata prvog dijela plaæe
//      2 – isplata drugog dijela plaæe
//      3 – isplata koja ne podliježe uplati doprinosa za mirovinsko osiguranje temeljem individualne kapitalizirane štednje
//      4 – isplata koje ne podliježe uplati doprinosa na osnovicu
//    ret = ret + "-0"; // :)
    //
    return ret;
  }
/*
NN 1/2003: Raèuni se sastoje od dva dijela koji su meðusobno odvojeni: vodeæeg broja banke i
broja raèuna u banci. Vodeæi broj banke je 1001005 -
Hrvatska narodna banka. Drugi dio raèuna se sastoji od deset (10) znamenaka od kojih prve dvije znamenke
imaju oznaku "17" - oznaka raèuna za naplatu javnih prihoda, iduæe tri znamenke su oznaka grada/opæine iz
dosadašnjeg sadržaja raèuna, slijede èetiri znamenke - oznaka dosadašnje individualne partije raèuna,
posljednja znamenka je kontrolni broj prethodnih devet izraèunana po meðunarodnoj normi ISO 7064 Modul 11, 10.
*/

  public static String getZiroOpc(String copc) {
    int HNBVB=1001005; //Vodeci broj HNB
    int RN1=17000; //broj kom se doda copc i konkatinira RN2
    int RN2=1200; //concata se na RN1+copc i racuna kontrolni broj
    int CB;//kontrolni broj
    int RN = Integer.parseInt((RN1+Integer.parseInt(copc))+""+RN2);
    String zr = (HNBVB+"-"+RN).concat(getMod11(RN+"")+"");
    return zr;
  }

  static boolean trace = false;

  public static int getMod11(String br) {
    int rez=0;
    char[] chrs = br.toCharArray();
    int resto = 10;
    for (int i = 0; i < chrs.length; i++) {
      int digit = Integer.parseInt(String.valueOf(chrs[i]));
      int s1 = resto+digit;
      if (trace) System.out.print((i+1)+".  "+resto+"+"+digit+"="+s1);
      if (s1>10) s1=s1-10;
      if (trace) System.out.print("  ==> "+s1);
      s1 = s1*2;
      if (trace) System.out.print(" x 2 = "+s1);
      if (s1>11) resto = s1-11;
//      else if (s1 == 0) resto = 1;
      else resto = s1;
      if (trace) System.out.println(" ==> "+resto);
    }
    if (trace) System.out.println("resto = "+resto);
    if (resto==0)
      rez = 1;
    else if (resto==1)
      rez = 0;
    else
      rez = 11-resto;
    if (trace) System.out.println("rez....."+rez);
    return rez;
  }
//

  private String getFromOdbici(String col) {
    try {
      QueryDataSet odb;
      ld.raLocate(dm.getVrsteodb(),new String[] {"CVRODB"},new String[] {strVrOdb});
      String nivoOdb = dm.getVrsteodb().getString("NIVOODB");
      if (nivoOdb.startsWith("OP")) {//porez, prirez
System.out.println("OPA - OP.."+strVrOdb+" N: "+nivoOdb);      
        QueryDataSet r = Radnicipl.getDataModule().getTempSet("copcine,cvro", Condition.equal("CRADNIK", strRad));
        r.open();
        String _copc = r.getString("COPCINE");
        String _cvro = r.getString("CVRO");
        if (nivoOdb.equals("OPVR")) {
System.out.println(nivoOdb + " = OPVR");
          odb = Odbici.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"CVRODB","CKEY", "CKEY2"},new Object[] {strVrOdb,_copc, _cvro}));
        } else {//prirez mora pratit porez
          odb = Odbici.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"CVRODB","CKEY"},new Object[] {strVrOdb,_copc}));
          odb.open();
System.out.println(" za fejk1 :: "+odb.getQuery().getQueryString());        
          boolean fejk;
          fejk = odb.getRowCount() == 0;
System.out.println("fejk1 = "+fejk);        
          if (!fejk) {
            odb.first();
            fejk = odb.getString(col).trim().equals("");
System.out.println("fejk2 = "+fejk+" ("+odb.getString(col).trim()+")");          
          }
          if (fejk) {//nadji OPVR odbitak sa istim povjeriocem
            String in = frmVirmaniPl.getCVRODB(new Integer(strPov).intValue());
            odb = Odbici.getDataModule().getTempSet("CVRODB!='"+strVrOdb+"' and CVRODB in ("+in+") AND "
                +Condition.whereAllEqual(new String[] {"CKEY", "CKEY2"},new Object[] {_copc, _cvro}));
System.out.println("!!ULTIMATE!!! "+odb.getQuery().getQueryString());          
          }
        }        
      } else {
        odb = Odbici.getDataModule().getTempSet(Condition.whereAllEqual(new String[] {"CVRODB","CKEY", "RBRODB"},new Object[] {strVrOdb,strRad, strRbrOdb}));      
      }
      odb.open();
      if (odb.getRowCount() == 0) {
        return "";
      } else {
        return odb.getString(col);
      }
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
      return "";
    }
  }
  
  private String getFromBankepl(String col) {
    //cradnik - isplmj - bankepl
    QueryDataSet __CISPLMJset = Util.getNewQueryDataSet("SELECT CISPLMJ FROM radnicipl where cradnik='"+strRad+"'");
    __CISPLMJset.open();
    if (__CISPLMJset.getRowCount() == 0) return "";
    short __cisplmj = __CISPLMJset.getShort("CISPLMJ");
    dm.getIsplMJ().open();
    if (!ld.raLocate(dm.getIsplMJ(),"CISPLMJ", __cisplmj+"")) return "";
    int __cbanke = dm.getIsplMJ().getInt("CBANKE");
    dm.getBankepl().open();
    if (!ld.raLocate(dm.getBankepl(),"CBANKE", __cbanke+"")) return "";
    return dm.getBankepl().getString(col);
  }

}
