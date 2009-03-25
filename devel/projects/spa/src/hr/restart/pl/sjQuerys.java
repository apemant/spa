/****license*****************************************************************
**   file: sjQuerys.java
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

import hr.restart.zapod.OrgStr;

import java.sql.Timestamp;

public class sjQuerys {

  public sjQuerys() {
  }
  public static String getOrgplFromArhiv(String sel) {
    String str="SELECT GODOBR, MJOBR, RBROBR, sum(sati) as SATI, sum(bruto) as bruto, sum(doprinosi) as doprinosi, sum(neto) as neto, "+
               "sum(neop) as neop, sum(iskneop) as iskneop, sum(porosn) as porosn, sum(por1) as por1, sum(por2) as por2, sum(por3) as por3, sum(poruk) as poruk, "+
               "sum(prir) as prir, sum(poriprir) as poriprir, sum(neto2) as neto2, sum(naknade) as naknade, sum(netopk) as netopk, sum(krediti) as krediti, "+
               "sum(naruke) as naruke, sum(doprpod) as doprpod ,max(datumispl) as datumispl FROM kumulorgarh "+
               "where "+sel+
               " group by godobr,mjobr,rbrobr";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getOrgsplFromArhiv(String sel) {
    String str="SELECT * FROM kumulorgarh "+
               "where "+sel;
    System.out.println("SQL: "+str);
    return str;
  }

  public static String delKUMULORGARH(short god, short mj, short rbr, String corg) {
    String str="delete from KUMULORGARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delKUMULRADARH(short god, short mj, short rbr, String corg) {
    String str="delete from KUMULRADARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delPRIMANJAARH(short god, short mj, short rbr, String corg) {
    String str="delete from PRIMANJAARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delODBICIARH(short god, short mj, short rbr, String corg) {
    String str="delete from ODBICIARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and CRADNIK in (select cradnik from radnicipl where "+getPripOrg(corg, "", "")+")";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delPRISUTARH(short god, short mj, short rbr, String corg) {
    String str="delete from PRISUTARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and CRADNIK in (select cradnik from radnicipl where "+getPripOrg(corg, "", "")+")";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delRSPERIODARH(short god, short mj, short rbr, String corg) {
    String str="delete from RSPERIODARH where GODOBR="+god+" and MJOBR="+mj+" and RBROBR="+rbr+""+
               " and CRADNIK in (select cradnik from radnicipl where "+getPripOrg(corg, "", "")+")";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getKUMULORGARH(short god, short mj, short rbr, String corg, Timestamp dan) {
    /** @todo insertiranje iz orgpl-a */
    String str="insert into KUMULORGARH (GODOBR, MJOBR, RBROBR, CORG, CVRO, KNJIG, SATI, BRUTO, "+
                "DOPRINOSI, NETO, NEOP, ISKNEOP, POROSN, POR1, POR2, POR3, POR4, POR5, PORUK, PRIR, "+
                "PORIPRIR, NETO2, NAKNADE, NETOPK, KREDITI, NARUKE, DOPRPOD, MINPL, MINOSDOP, OSNPOR1, "+
                "OSNPOR2, OSNPOR3, OSNPOR4, OSNPOR5, PARAMETRI, COPCINE, NACOBRS, NACOBRB, SATIMJ, "+
                "OSNKOEF, SATNORMA, BROJDANA, STOPAK, DATUMISPL) "+
               "select '"+god+"','"+mj+"','"+rbr+"', KUMULORG.CORG, KUMULORG.CVRO, KUMULORG.KNJIG, "+
                "KUMULORG.SATI, KUMULORG.BRUTO, KUMULORG.DOPRINOSI, KUMULORG.NETO, KUMULORG.NEOP, "+
                "KUMULORG.ISKNEOP, KUMULORG.POROSN, KUMULORG.POR1, KUMULORG.POR2, KUMULORG.POR3, "+
                "KUMULORG.POR4, KUMULORG.POR5, KUMULORG.PORUK, KUMULORG.PRIR, KUMULORG.PORIPRIR, "+
                "KUMULORG.NETO2, KUMULORG.NAKNADE, KUMULORG.NETOPK, KUMULORG.KREDITI, KUMULORG.NARUKE, "+
                "KUMULORG.DOPRPOD, PARAMETRIPL.MINPL, PARAMETRIPL.MINOSDOP, PARAMETRIPL.OSNPOR1, "+
                "PARAMETRIPL.OSNPOR2, PARAMETRIPL.OSNPOR3, PARAMETRIPL.OSNPOR4, PARAMETRIPL.OSNPOR5, "+
                "ORGPL.PARAMETRI, ORGPL.COPCINE, ORGPL.NACOBRS, ORGPL.NACOBRB, ORGPL.SATIMJ, "+
                "ORGPL.OSNKOEF, ORGPL.SATNORMA, ORGPL.BROJDANA, ORGPL.STOPAK, '"+dan+"' "+
               "from KUMULORG, PARAMETRIPL, ORGPL "+
//               "where orgpl.corg=kumulorg.corg and "+getPripOrg(corg, "", "");
               "where "+getPripOrg(corg, "", "") + " and kumulorg.corg=orgpl.corg";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getKUMULRADARH(short god, short mj, short rbr, String corg) {
    String str="insert into KUMULRADARH (GODOBR, MJOBR, RBROBR, CRADNIK, SATI, BRUTO, "+
                "DOPRINOSI, NETO, NEOP, ISKNEOP, POROSN, POR1, POR2, POR3, POR4, POR5, PORUK, PRIR, "+
                "PORIPRIR, NETO2, NAKNADE, NETOPK, KREDITI, NARUKE, CRADMJ, CSS, CVRO, CISPLMJ, COPCINE, "+
                "RSINV, RSOO, BRUTOSN, BRUTDOD, BRUTMR, BRUTUK, GODSTAZ, STOPASTAZ, DATSTAZ, PODSTAZ, "+
                "DATPODSTAZ, NACOBRB, KOEF, KOEFZAR, OLUK, OLOS, CLANOMF, CORG, PARAMETRI) "+
               "select '"+god+"','"+mj+"','"+rbr+"', CRADNIK, SATI, BRUTO, "+
                "DOPRINOSI, NETO, NEOP, ISKNEOP, POROSN, POR1, POR2, POR3, POR4, POR5, PORUK, PRIR, "+
                "PORIPRIR, NETO2, NAKNADE, NETOPK, KREDITI, NARUKE, CRADMJ, CSS, CVRO, CISPLMJ, COPCINE, "+
                "RSINV, RSOO, BRUTOSN, BRUTDOD, BRUTMR, BRUTUK, GODSTAZ, STOPASTAZ, DATSTAZ, PODSTAZ, "+
                "DATPODSTAZ, NACOBRB, KOEF, KOEFZAR, OLUK, OLOS, CLANOMF, CORG, PARAMETRI "+
               "from KUMULRAD, RADNICIPL "+
               "where KUMULRAD.CRADNIK=RADNICIPL.CRADNIK and "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getPRIMANJAARH(short god, short mj, short rbr, String corg) {
    String str="insert into PRIMANJAARH (GODOBR, MJOBR, RBROBR, CRADNIK, CVRP, RBR, CORG, SATI, "+
                "KOEF, BRUTO, DOPRINOSI, NETO, COBR, COSN, RSOO, RNALOG, REGRES, CGRPRIM, CVRPARH, "+
                "STAVKA, PARAMETRI) "+
               "select '"+god+"','"+mj+"','"+rbr+"', CRADNIK, CVRP, RBR, CORG, SATI, "+
                "KOEF, BRUTO, DOPRINOSI, NETO, COBR, COSN, RSOO, RNALOG, REGRES, CGRPRIM, CVRPARH, "+
                "STAVKA, PARAMETRI "+
               "from PRIMANJAOBR, VRSTEPRIM where PRIMANJAOBR.CVRP=VRSTEPRIM.CVRP";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getODBICIARH(short god, short mj, short rbr, String corg) {
    String str="insert into ODBICIARH (GODOBR, MJOBR, RBROBR, CRADNIK, CVRP, RBR, CVRODB, CKEY, CKEY2, "+
                "RBRODB, OBROSN, OBRSTOPA, OBRIZNOS, NIVOODB, TIPODB, VRSTAOSN, OSNOVICA, CPOV, PNB1, "+
                "PNB2, IZNOS, STOPA, DATPOC, DATZAV, GLAVNICA, RATA, SALDO, STAVKA, PARAMETRI) "+
               "select '"+god+"','"+mj+"','"+rbr+"', CRADNIK, CVRP, RBR, CVRODB, CKEY, CKEY2, "+
                "RBRODB, OBROSN, OBRSTOPA, OBRIZNOS, NIVOODB, TIPODB, VRSTAOSN, OSNOVICA, CPOV, PNB1, "+
                "PNB2, IZNOS, STOPA, DATPOC, DATZAV, GLAVNICA, RATA, SALDO, STAVKA, PARAMETRI "+
               "from ODBICIOBR, VRSTEODB, ODBICI where ODBICIOBR.CVRODB=VRSTEODB.CVRODB and "+
                "ODBICIOBR.CVRODB=ODBICI.CVRODB and ODBICIOBR.CKEY=ODBICI.CKEY and "+
                "ODBICIOBR.CKEY2=ODBICI.CKEY2 and ODBICIOBR.RBRODB=ODBICI.RBRODB";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getPRISUTARH(short god, short mj, short rbr, String corg) {
    String str="insert into PRISUTARH (GODOBR, MJOBR, RBROBR, CRADNIK, CVRP, DAN, GRPRIS, SATI, IZNOS, "+
                "SATIRADA, SATIPRAZ, IZNOSOBU, IZNOSP, PARAMETRI) "+
               "select '"+god+"','"+mj+"','"+rbr+"', CRADNIK, CVRP, DAN, GRPRIS, SATI, IZNOS, "+
                "SATIRADA, SATIPRAZ, IZNOSOBU, IZNOSP, PARAMETRI "+
               "from PRISUTOBR";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getRSPERIODARH(short god, short mj, short rbr, String corg) {
    String str="insert into RSPERIODARH (GODOBR, MJOBR, RBROBR, CRADNIK, RBR, RSOO, ODDANA, DODANA, "+
                "JMBG, COPCINE, RSINV, RSB, RSZ, SATI, BRUTO, BRUTOMJ, MIO1, MIO1MJ, MIO2, MIO2MJ, ZO, ZOMJ, "+
                "ZAPOS, ZAPOSMJ, PREMOS, OSODB, POREZ, POREZMJ, PRIREZ, PRIREZMJ, NETOPK, MJESEC, GODINA, "+
                "IDENTIFIKATOR, VRSTAUPL) "+
               "select '"+god+"','"+mj+"','"+rbr+"', CRADNIK, RBR, RSOO, ODDANA, DODANA, "+
               "JMBG, COPCINE, RSINV, RSB, RSZ, SATI, BRUTO, BRUTOMJ, MIO1, MIO1MJ, MIO2, MIO2MJ, ZO, ZOMJ, "+
               "ZAPOS, ZAPOSMJ, PREMOS, OSODB, POREZ, POREZMJ, PRIREZ, PRIREZMJ, NETOPK, MJESEC, GODINA, "+
               "IDENTIFIKATOR, VRSTAUPL "+
               "from RSPERIODOBR";
    System.out.println("SQL: "+str);
    return str;
  }





  public static String selectRadniciPl(String corg, String radnik) {
    String str="select * from RADNICIPL where radnicipl.AKTIV='D' and "+getPripOrg(corg, radnik, "RADNICIPL.");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String getRbr(String radnik) {
    String str="select max(RBR) from PRIMANJAOBR where CRADNIK='"+radnik+"'";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String selectOrgStr(String corg) {
    String str="select * from ORGSTRUKTURA where "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String selectOrgPl(String corg) {
    String str="select * from ORGPL where "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delPrimanjaObr(String corg) {
    String str="delete from PRIMANJAOBR where PRIMANJAOBR.CRADNIK in (Select RADNICIPL.CRADNIK from radnicipl where "+getPripOrg(corg, "", "RADNICIPL.")+")";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String delRSPeriod(String corg) {
    String str="delete from RSPERIOD where RSPERIOD.CRADNIK in (Select RADNICIPL.CRADNIK from radnicipl where "+getPripOrg(corg, "", "RADNICIPL.")+")";
    System.out.println("SQL: "+str);
    return str;
  }
  public static String selectRadnici(String corg) {
    String str="select * from RADNICI where "+getPripOrg(corg, "", "");
    System.out.println("SQL: "+str);
    return str;
  }
  static String getPripOrg(String corg, String radnik, String baza) {
    int i=0;
    String cVrati="("+baza+"CORG in ";
    com.borland.dx.dataset.StorageDataSet tds =  hr.restart.zapod.OrgStr.getOrgStr().getOrgstrAndKnjig(corg);
//    tds.first();
//    do {
//      if (i>0) {
//        cVrati=cVrati+',';
//      }
//      i++;
//      cVrati=cVrati+"'"+tds.getString("CORG")+"'";
//      tds.next();
//    } while (tds.inBounds());
//    cVrati=cVrati+")";
    cVrati = cVrati + OrgStr.getOrgStr().getInQuery(tds, baza+"CORG")+") ";
    if (!radnik.equals("")) {
      cVrati=cVrati+" and "+baza+"CRADNIK='"+radnik+"'";
    }
    return cVrati;
  }
}