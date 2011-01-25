/****license*****************************************************************
**   file: prepare2Zim.java
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
package hr.restart.gk;
import hr.restart.baza.Condition;
import hr.restart.baza.Sifrarnici;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;


public class prepare2Zim {

  private DataSet forKnjizenje;
  private hr.restart.util.sysoutTEST ST = new hr.restart.util.sysoutTEST(false);
  private java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyyMMdd");
  private hr.restart.util.lookupData ld =  hr.restart.util.lookupData.getlookupData();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.FileHandler fhsturir= new hr.restart.util.FileHandler("dsturir.dat");

  private hr.restart.util.FileHandler fhdeik9107= new hr.restart.util.FileHandler("deik9107.dat");
  private hr.restart.util.FileHandler fhvrtem= new hr.restart.util.FileHandler("vrtem.dat");
  private hr.restart.util.FileHandler fhstav_doc= new hr.restart.util.FileHandler("stav_doc.dat");
  private hr.restart.robno.raDateUtil rd = hr.restart.robno.raDateUtil.getraDateUtil();
  private boolean microlab = hr.restart.sisfun.frmParam.getParam("robno","temVerMicrolab",
                                       "microlab","Transfer temeljnice u Zim aplikaciju opcije:microlab ili restart !").equals("microlab");

  private boolean tecVersionTrans = hr.restart.sisfun.frmParam.getParam("robno","tecVersionTrans",
        "N","Prenos u microlab apli. (partner iz TransData)!").equals("D");

  public void setForKnjizenje(DataSet ds){
//    forKnjizenje = null;
    forKnjizenje = ds;
  }

  public static String prepareString(String what,int duzina,boolean desno,char mask) throws Exception{

    if (duzina<what.length()) {
//      throw new Exception("duzina < "+what+" (.length()) !!!");
            return what.substring(0,duzina);
    }
    else if (duzina== what.length()) return what;

    hr.restart.util.VarStr vs =  new hr.restart.util.VarStr();
    for (int i=0;i<duzina-what.length();i++){
      vs.append(mask);
    }
    String empty = vs.toString();
    return (desno?what+empty:empty+what);
  }

  private String prepareString(String what,int duzina,boolean desno) throws Exception{
    return prepareString(what,duzina,desno,' ');
  }
  /*
  xsif 1    numeric 12   (sam se dodjeljuje zadnji najve\u0107i)

  xpodp     alpha    2     // sifra poduze\u0107a - knjigocvodstva u Ecosu 01
  xkontp    alpha    8     // sifra konta
  xojp      alpha    6     // sifra organizacijske jedinice
  xpartnp   alpha    5     // sifra partnera
  xpozd     alpha    6     // poziv na broj konta 2200 i 1200
  xdnev     alpha    3     // dnevnik oni upisuju
  xdatk     numeric  8     // upisuju iz ekrana
  xtem      alpha    6     // upisuje se iz ekrana
  xmj       numeric  8     // mjesec iz datk
  xgod      numeric  8     // mjesec iz xdatk
  xdvo      numeric  8     // datum dvo
  xdup      numeric  8     // datum uplate
  xavans    alpha    5        // ?
  xstat     alpha    1     // mora biti '1'
  xstatkam  alpha    1
  xozk      alpha    2
  xdug      numeric 17,2
  xpot      vastint 17,2
  xopis     alpha   35
  xstatz    alpha    1
  xnac      alpha    2
  xsistdat  numeric  8
  ufa       alpha    8
  brzak     alpha    8
  sifvalute alpha    3
  xdevdug   numeric 17,2
  xdevpot   numeric 17,2
  agent     alpha    4
  ruc       numeric 17,2    // pojma nemam s cim to puniti uglavnom ima samo onaj s kontom 120000

*/
/*
  stavkeurir

  OrgPripadnost   alpha    12
  COrg            alpha    12
  BrojDokumenta   alpha    15
  ExtBrojDok      alpha    15
  DatumDokumenta  numeric   8
  DatumDospijeca  numeric   8
  DatumKnjizenja  numeric   8
  CPartner        numeric  12
  BrojKonta       alpha     8
  ID              numeric  17.2
  IP              numeric  17.2
  UraIra          alpha     1
  KnjBrCol        numeric   8
  PredBroj        alpha    10
*/



  public void makeTransferFilesKnjiga() {
    if (forKnjizenje == null) return;
    for (forKnjizenje.first();forKnjizenje.inBounds();forKnjizenje.next()) {
      try {
        if (!forKnjizenje.getString("CKNJIGE").equalsIgnoreCase(""))           {
        //
        //  OrgPripadnost   alpha    12
          fhsturir.write(prepareString("01",12,true));
        //  COrg            alpha    12
//          fhsturir.write(prepareString(forKnjizenje.getString("CORG"),12,true));
          fhsturir.write(prepareString("01",12,true));
        //  BrojDokumenta   alpha    15
          fhsturir.write(prepareString(forKnjizenje.getString("EXTBRDOK"),15,true));
        //  ExtBrojDok      alpha    15
          fhsturir.write(prepareString(forKnjizenje.getString("BROJDOK"),15,true));
//          fhsturir.write(prepareString(forKnjizenje.getString("BROJDOK"),15,true));
        //  DatumDokumenta  numeric   8
          fhsturir.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOK")),8,true));
        //  DatumDospijeca  numeric   8
          fhsturir.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOSP")),8,true));
        //  DatumKnjizenja  numeric   8
          fhsturir.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATUMKNJ")),8,true));
        //  CPartner        numeric  12
          fhsturir.write(prepareString(String.valueOf(forKnjizenje.getInt("CPAR")),12,false));
        //  BrojKonta       alpha     8
          fhsturir.write(prepareString(forKnjizenje.getString("BROJKONTA"),8,true));
        //  ID              numeric  17.2
          fhsturir.write(prepareString(forKnjizenje.getBigDecimal("ID").toString(),17,false));
        //  IP              numeric  17.2
          fhsturir.write(prepareString(forKnjizenje.getBigDecimal("IP").toString(),17,false));
        //  UraIra          alpha     1
          fhsturir.write(prepareString(forKnjizenje.getString("URAIRA"),1,true));
        //  KnjBrCol        numeric   8
          fhsturir.write(prepareString(String.valueOf(forKnjizenje.getShort("CKOLONE")),8,true));
        //  PredBroj        alpha    10
          fhsturir.writeln(prepareString(forKnjizenje.getString("CKNJIGE"),10,true));
        }
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }
  }

  public void transferKnjigaRestart(int i,boolean saldak) throws Exception {

/*
    u robnom je fajl za prijenos :stavfink.cmd
    u finku je                     finkcom.cmd
    StavkeFink
     1 BrojNaloga         alpha           8        0 no
     2 OrgPripadnost      alpha          12        0 no
     3 COrg               alpha          12        0 no
     4 RBS                int             4        0 no
     5 BrojKonta          alpha           8        0 no
     6 DatumKnjizenja     date            8        0 no
     7 DatumDokumenta     date            8        0 no
     8 DatumDospijeca     date            8        0 no
     9 BrojDokumenta      alpha          15        0 no
    10 VrstaDok           alpha           1        0 no
    11 CPartner           longint         6        0 no
    12 ExtBrojDok         varalpha       15        0 no
    13 BrojIzvoda         int             3        0 no
    14 NacinPl            alpha           3        0 no
    15 Opis               varalpha       50        0 no
    16 ID                 vastint        13        2 no
    17 IP                 vastint        13        2 no
    18 Tip                alpha           1        0 no
    19 Pokriveno          alpha           1        0 no
    20 Saldo              vastint        13        2 no
    10 CNaloga            alpha          21        0 yes
   101 CDokumenta         alpha          46        0 yes

    stav_doc.dat dokument

     1 BrojNaloga         alpha           8        0 no
     2 OrgPripadnost      alpha          12        0 no
     3 COrg               alpha          12        0 no
     4 RBS                numeric         8        0 no
     5 BrojKonta          alpha           8        0 no
     6 DatumKnjizenja     numeric         8        0 no
     7 DatumDokumenta     numeric         8        0 no
     8 DatumDospijeca     numeric         8        0 no
     9 BrojDokumenta      alpha          15        0 no
    10 VrstaDok           alpha           1        0 no
    11 CPartner           numeric        12        0 no
    12 ExtBrojDok         alpha          15        0 no
    13 BrojIzvoda         numeric         8        0 no
    14 NacinPl            alpha           3        0 no
    15 Opis               alpha          50        0 no
    16 ID                 numeric        17        2 no
    17 IP                 numeric        17        2 no
    18 Tip                alpha           1        0 no
    19 Pokriveno          alpha           1        0 no
    20 Saldo              numeric        17        2 no

   $procmsg('on','Dodavanje stavaka za glavnu knjigu...')
   add 1 StavkeFink let \
                   OrgPripadnost = vOrg  \
                   COrg          = vCOrg \
                   RBS           = vRBS  \
                   BrojKonta     = vBrojKonta\
                   DatumDokumenta= fStavFink.Datum  \
                   CPartner      = ''    \
                   Opis          = 'Total konta za zadano razdoblje' \
                   ID            = vIznosDug \
                   IP            = vIznosPot \
                   Tip           = ' '   \
                   Saldo         = {ID wh ID <> 0,IP} \

// zim kod
    $procmsg('on','Dodavanje stavaka salda konti - dobavlja~...')
    add 1 StavkeFink let \
            OrgPripadnost = vOrg \
            COrg          = vOrg \
            RBS           = vRBS + 1  \
            BrojKonta     = vKONTO   \
            DatumDokumenta= vDDok   \
            DatumDospijeca= vDDok   \
            BrojDokumenta = vBD      \
            VrstaDok      = {'R' wh vUKUPNO > 0,'K'} \
            CPartner      = vCP     \
            Opis          = vOPIS       \
            ID            = 0 \
            IP            = vUKUPNO                 \
            Tip           = 'D'                     \
            Saldo         = {ID wh ID <> 0,IP}      \
    	\%        NacinPl       = vNP     \
*/
//     1 BrojNaloga         alpha           8        0 no
//    fhstav_doc.write(prepareString("",8,false));
    fhstav_doc.write(prepareString(forKnjizenje.getString("CVRNAL"),8,false));
//    fhvrtem.writeln(prepareString(forKnjizenje.getString("CVRNAL"),2,false));
//     2 OrgPripadnost      alpha          12        0 no
    String pripad = prepareString(hr.restart.zapod.OrgStr.getOrgStr().getPripKnjig(forKnjizenje.getString("CORG")),12,true);
    fhstav_doc.write(pripad);
//    3 COrg               alpha          12        0 no
    if (saldak) {
      fhstav_doc.write(pripad);
    } else {
      fhstav_doc.write(prepareString(forKnjizenje.getString("CORG"),12,true));
    }
//    4 RBS                numeric         8        0 no
    fhstav_doc.write(prepareString(String.valueOf(i),8,false));
//    5 BrojKonta          alpha           8        0 no
    fhstav_doc.write(prepareString(forKnjizenje.getString("BROJKONTA"),8,true));
//    6 DatumKnjizenja     numeric         8        0 no
//    fhstav_doc.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATUMKNJ")),8,true));
      fhstav_doc.write(prepareString("",8,true));
//    7 DatumDokumenta     numeric         8        0 no
//    if (saldak) {
      fhstav_doc.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOK")),8,true));
//    } else {
//      fhstav_doc.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATUMKNJ")),8,true));
//      fhstav_doc.write(prepareString("",8,true));
//    }
//    8 DatumDospijeca     numeric         8        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOSP")),8,true));
    } else {
      fhstav_doc.write(prepareString("",8,true));
    }
//    9 BrojDokumenta      alpha          15        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(forKnjizenje.getString("BROJDOK"),15,true));
    } else {
      fhstav_doc.write(prepareString("",15,true));
    }
//   10 VrstaDok           alpha           1        0 no
//    'R' , 'K'
    if (saldak) {
      if (forKnjizenje.getBigDecimal("IP").doubleValue()>0 || forKnjizenje.getBigDecimal("ID").doubleValue()>0) {
        fhstav_doc.write(prepareString("R",1,true));
      } else {
        fhstav_doc.write(prepareString("K",1,true));
      }
    } else {
      fhstav_doc.write(prepareString("",1,true));
    }
//   11 CPartner           numeric        12        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(String.valueOf(forKnjizenje.getInt("CPAR")),12,false));
    } else {
      fhstav_doc.write(prepareString("",12,false));
    }
//   12 ExtBrojDok         alpha          15        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(forKnjizenje.getString("EXTBRDOK"),15,false));
    } else {
      fhstav_doc.write(prepareString("",15,false));
    }
//   13 BrojIzvoda         numeric         8        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(forKnjizenje.getString("BROJIZV"),8,false));
    } else {
      fhstav_doc.write(prepareString("",8,false));
    }
//   14 NacinPl            alpha           3        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(forKnjizenje.getString("CNACPL"),3,false));
    } else {
      fhstav_doc.write(prepareString("",3,false));
    }
//   15 Opis               alpha          50        0 no
    fhstav_doc.write(prepareString(forKnjizenje.getString("OPIS"),50,true));
//   16 ID                 numeric        17        2 no
    fhstav_doc.write(prepareString(forKnjizenje.getBigDecimal("ID").toString(),17,false));
//   17 IP                 numeric        17        2 no
    fhstav_doc.write(prepareString(forKnjizenje.getBigDecimal("IP").toString(),17,false));
//   18 Tip                alpha           1        0 no
    if (saldak) {
/// BUG TV
      if (hr.restart.util.lookupData.getlookupData().raLocate(dm.getKonta(),
          new String[] {"BROJKONTA"},new String[]{forKnjizenje.getString("BROJKONTA")})){
        fhstav_doc.write(prepareString(dm.getKonta().getString("SALDAK"),1,false));
      } else {
        fhstav_doc.write(prepareString("G",1,false)); /// forsana greška ako konto nije ni kupac ni dobavljac
      }

/*
      if (forKnjizenje.getBigDecimal("ID").doubleValue()>0) {
      fhstav_doc.write(prepareString("K",1,false));
      } else {
      fhstav_doc.write(prepareString("D",1,false));
      }
*/


    } else {
      fhstav_doc.write(prepareString("",1,false));
    }
//   19 Pokriveno          alpha           1        0 no
    if (saldak) {
      fhstav_doc.write(prepareString(forKnjizenje.getString("POKRIVENO"),1,false));
    } else {
      fhstav_doc.write(prepareString("",1,false));
    }
//   20 Saldo              numeric        17        2 no
    if (forKnjizenje.getBigDecimal("ID").doubleValue()!=0) {
      fhstav_doc.writeln(prepareString(forKnjizenje.getBigDecimal("ID").toString(),17,false));
    } else {
      fhstav_doc.writeln(prepareString(forKnjizenje.getBigDecimal("IP").toString(),17,false));
    }

  }
  public void transferKnjigaMicrolab(int i) throws Exception {

/*
      LOKK -o-
      AKTIV -o-
      KNJIG -o-
      GOD -o-
      CVRNAL -o-
      RBR -o-
      RBS -o-
      BROJKONTA -o-
      CORG -o-
      DATUMKNJ -o-
      DATDOK -o-
      DATDOSP -o-
      BROJDOK -o-
      VRDOK -o-
      CPAR -o-
      EXTBRDOK -o-
      BROJIZV -o-
      CNACPL -o-
      OPIS -o-
      ID -o-
      IP -o-
      POKRIVENO -o-
      SALDO -o-
      CKOLONE -o-
      RBSRAC -o-
      CKNJIGE -o-
      URAIRA -o-
      GODMJ -o-
      TECAJ -o-
      DEVID -o-
      DEVIP -o-
      CNALOGA -o-
      ZAGK -o-
*/
    //      xsif 1    numeric 12   (sam se dodjeljuje zadnji najve\u0107i)
          fhdeik9107.write(prepareString(String.valueOf(i),12,false));
    //      xpodp     alpha    2     // sifra poduze\u0107a - knjigocvodstva u Ecosu 01
          fhdeik9107.write(prepareString("01",2,true));
    //      xkontp    alpha    8     // sifra konta
          fhdeik9107.write(prepareString(forKnjizenje.getString("BROJKONTA"),8,true));
    //      xojp      alpha    6     // sifra organizacijske jedinice
          fhdeik9107.write(prepareString(/*forKnjizenje.getString("CORG")*/"",6,true));
    //      xpartnp   alpha    5     // sifra partnera
          if (tecVersionTrans) {
          	QueryDataSet td = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM TransData where ndat = '"+forKnjizenje.getInt("CPAR")+"'",true);
//System.out.println(td.getQuery().getQueryString());          	
          	if (td.getRowCount()==0){
          		fhdeik9107.write(prepareString(String.valueOf(forKnjizenje.getInt("CPAR")),5,false,' '));	
          	} else {
          		fhdeik9107.write(prepareString(td.getString("SDAT"),5,false));	
          	}
          } else {
          	fhdeik9107.write(prepareString(String.valueOf(forKnjizenje.getInt("CPAR")),5,false,' '));          	
          }
   
    //      xpozd     alpha    6     // poziv na broj konta 2200 i 1200
          fhdeik9107.write(prepareString(forKnjizenje.getString("BROJDOK"),6,true));
    //      xdnev     alpha    3     // dnevnik oni upisuju
          fhdeik9107.write(prepareString("",3,true));
    //      xdatk     numeric  8     // upisuju iz ekrana
          fhdeik9107.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATUMKNJ")),8,true));
    //      xtem      alpha    6     // upisuje se iz ekrana
          fhdeik9107.write(prepareString("",6,true));
    //      xmj       numeric  8     // mjesec iz datk
          fhdeik9107.write(prepareString(rd.getMonth(forKnjizenje.getTimestamp("DATDOK")),8,true));
    //      xgod      numeric  8     // mjesec iz xdatk
          fhdeik9107.write(prepareString(rd.getYear2Digit(forKnjizenje.getTimestamp("DATDOK")),8,true));
    //      xdvo      numeric  8     // datum dvo
          fhdeik9107.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOK")),8,true));
    //      xdup      numeric  8     // datum uplate
          fhdeik9107.write(prepareString(sdf.format(forKnjizenje.getTimestamp("DATDOK")),8,true));
//        fhdeik9107.write(prepareString("",8,true));
    //      xavans    alpha    5        // ?
          fhdeik9107.write(prepareString("",5,true));
    //      xstat     alpha    1     // mora biti '1'
          fhdeik9107.write(prepareString("1",1,true));
    //      xstatkam  alpha    1
          fhdeik9107.write(prepareString("1",1,true));
    //      xozk      alpha    2
          fhdeik9107.write(prepareString("",2,true));
    //      xdug      numeric 17,2
          fhdeik9107.write(prepareString(forKnjizenje.getBigDecimal("ID").toString(),17,false));
    //      xpot      numeric 17,2
          fhdeik9107.write(prepareString(forKnjizenje.getBigDecimal("IP").toString(),17,false));
    //      xopis     alpha   35
          fhdeik9107.write(prepareString(forKnjizenje.getString("OPIS"),35,true));
    //      xstatz    alpha    1
          fhdeik9107.write(prepareString("",1,false));
    //      xnac      alpha    2
           fhdeik9107.write(prepareString(getXNAC(forKnjizenje) ,2,false));
    //      xsistdat  numeric  8
          fhdeik9107.write(prepareString("",8,false));
    //      ufa       alpha    8
          fhdeik9107.write(prepareString(forKnjizenje.getString("EXTBRDOK"),8,false));
    //      brzak     alpha    8
          fhdeik9107.write(prepareString("",8,false));
    //      sifvalute alpha    3
          fhdeik9107.write(prepareString("",3,true));
    //      xdevdug   numeric 17,2
          fhdeik9107.write(prepareString(forKnjizenje.getBigDecimal("DEVID").toString(),17,false));
    //      xdevpot   numeric 17,2
          fhdeik9107.write(prepareString(forKnjizenje.getBigDecimal("DEVIP").toString(),17,false));
    //      agent     alpha    4
          fhdeik9107.write(prepareString("",4,true));
    //      ruc       numeric 17,2    // pojma nemam s cim to puniti uglavnom ima samo onaj s kontom 120000
          fhdeik9107.writeln(prepareString(Aus.zero2.toString(),17,false));

//        if (!forKnjizenje.getString("URAIRA").equalsIgnoreCase("")) {

  }

  private QueryDataSet nacini = null;
  private String getXNAC(DataSet fknji) {
    int razlika = (int) Math.round((fknji.getTimestamp("DATDOSP").getTime() -
        fknji.getTimestamp("DATDOK").getTime()) /
        (1000 * 60 * 60 * 24.));
    if (nacini == null) nacini = Sifrarnici.getDataModule().getTempSet(Condition.equal("VRSTASIF", "xnac"));
    nacini.open();
    if (lookupData.getlookupData().raLocate(nacini, "NAZIV", razlika+"")) {
      return nacini.getString("CSIF").trim();
    } else {
      System.err.println("NIJE NADJEN sifrarniciXNAC za "+razlika);
      return "0";
    }
//    if (razlika >99) razlika = 99; // namjerna greska zbog dva alpha znaka ako je vece od 99 !!!!
  }

  public void makeTransferFiles(boolean  saldak) {

    int i = 1;
    if (forKnjizenje == null) return;

    if (!microlab) {
      try {
        fhvrtem.writeln(prepareString(forKnjizenje.getString("CVRNAL"),2,false));
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
    }


    boolean goahead = true;
    boolean bsld = true;
    for (forKnjizenje.first();forKnjizenje.inBounds();forKnjizenje.next()) {
      goahead = hr.restart.zapod.raKonta.isSaldak(forKnjizenje.getString("BROJKONTA"));
      bsld = goahead;
      if (!saldak) goahead = !goahead;
      if (goahead) {
        try {
          if (microlab) {
            transferKnjigaMicrolab(i);
          } else {
            transferKnjigaRestart(i,saldak);
          }
        }
        catch (Exception ex) {
          ex.printStackTrace();
        }
        i++;
      }
    }
  }
  public void openFiles() {
    if (microlab) {
      fhdeik9107.openWrite();
    } else {
      fhstav_doc.openWrite();
      fhvrtem.openWrite();
    }
    fhsturir.openWrite();
  }

  public void closeAll() {
    if (microlab) {
      fhdeik9107.close();
    } else {
      fhstav_doc.close();
      fhvrtem.close();
    }
    fhsturir.close();
  }
  public String[] checkParams(String addr, int port, String username, String passwd, String folder) {
    String[] ret = new String[] {addr, port+"",username, passwd, folder};
    if (addr==null || addr.trim().equals("")) 
      ret[0] = hr.restart.sisfun.frmParam.getParam("robno","QNXaddr","161.53.200.89");
    if (port <= 0) 
      ret[1] = hr.restart.sisfun.frmParam.getParam("robno","QNXport","21");
    if (username==null || username.trim().equals("")) 
      ret[2] = hr.restart.sisfun.frmParam.getParam("robno","QNXuser","fist_ftp");
    if (passwd==null || passwd.trim().equals("")) 
      ret[3] = hr.restart.sisfun.frmParam.getParam("robno","QNXpasswd","fist_ftp");
    if (folder==null || folder.trim().equals(""))
      ret[4] = hr.restart.sisfun.frmParam.getParam("robno","QNXfolder","/zimdb/ECOS/bazaet");
    
    return ret;
  }

  public void transfer2QnxServer() {
    String[] ret = checkParams(null, 0, null, null, null);
    transfer2QnxServer(ret[0], Integer.parseInt(ret[1]), ret[2], ret[3], ret[4]);
  }
  public void transfer2QnxServer(String addr, int port, String username, String passwd, String folder) {
    String[] ret = checkParams(addr, port, username, passwd, folder);
    addr = ret[0]; port = Integer.parseInt(ret[1]); username = ret[2]; passwd = ret[3]; folder = ret[4]; 
    BufferedReader in  = null;
    BufferedWriter out  = null;

    
System.out.println(addr);
System.out.println(port);
System.out.println(username);
System.out.println(passwd);
System.out.println(folder);


    try {
      java.net.Socket soc = new java.net.Socket(java.net.InetAddress.getByName(addr),port);
      soc.setSoTimeout(60000);

      in = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      out = new BufferedWriter(new OutputStreamWriter(soc.getOutputStream()));
System.out.println(in.readLine());

      out.write("USER "+username);
      out.newLine();
      out.flush();
System.out.println(in.readLine());

      out.write("PASS "+passwd);
      out.newLine();
      out.flush();
System.out.println(in.readLine());

      out.write("CWD "+folder);
      out.newLine();
      out.flush();
System.out.println(in.readLine());
      out.write("type i");
      out.newLine();
      out.flush();
System.out.println(in.readLine());

      stor(fhsturir,out,in);
      if (microlab) {
        stor(fhdeik9107,out,in);
      } else {
        stor(fhvrtem,out,in);
        stor(fhstav_doc,out,in);
      }

      out.write("QUIT");
      out.newLine();
      out.flush();
System.out.println(in.readLine());


      soc.close();

  } catch (Exception ex){
    ex.printStackTrace();
  }

  }

  private int[] getValues(String acacutija) {

    int [] numbers = new int[6];
    int brojac=0;
    int begin = acacutija.indexOf('(');
    int end = acacutija.indexOf(')');
    String tmp = "";
    if(begin!=-1 && end !=-1 && begin < end)
      acacutija = acacutija.substring(begin+1,end+1);

    for (int i=0;i<acacutija.length();i++) {
      if (acacutija.substring(i,i+1).equalsIgnoreCase(",") || acacutija.substring(i,i+1).equalsIgnoreCase(")")) {
        try {
          numbers[brojac] = Integer.parseInt(tmp);
          brojac ++;
        }
        catch (NumberFormatException ex) {
          ex.printStackTrace();
        }
        tmp="";
      } else {
        tmp=tmp+acacutija.substring(i,i+1);
      }
    }
    return numbers;
  }

  private String getAddr(int[] numbers) {
    return String.valueOf(numbers[0])+"."+
        String.valueOf(numbers[1])+"."+
        String.valueOf(numbers[2])+"."+
        String.valueOf(numbers[3]);
  }

  private int getPort(int[] numbers) {
    return numbers[4]*256+numbers[5];
  }

  private byte[] readData(hr.restart.util.FileHandler fh) {

    try {
      fh.openRead();
      int len = fh.fileInputStream.available();
      byte[] b = new byte[len];
      fh.fileInputStream.read(b);
      fh.close();
      return b;
    }
    catch (IOException ex) {
      ex.printStackTrace();
      return null;
    }

  }

  private void stor(hr.restart.util.FileHandler fh,BufferedWriter out,BufferedReader in) throws Exception {
    out.write("PASV");
    out.newLine();
    out.flush();
    int[] poljesnova = getValues(in.readLine());
System.out.println("getAddr(poljesnova) "+getAddr(poljesnova));
System.out.println("getPort(poljesnova) "+getPort(poljesnova));
    java.net.Socket data = new java.net.Socket(java.net.InetAddress.getByName(getAddr(poljesnova)),getPort(poljesnova));
    data.setSoTimeout(60000);
    DataOutputStream dos = new DataOutputStream(data.getOutputStream());
    out.write("STOR "+fh.getFileName());
    out.newLine();
    out.flush();
    System.out.println(in.readLine());
    dos.write(readData(fh));
    dos.flush();
    dos.close();
    data.close();
    System.out.println(in.readLine());
//    out.write("\n");
//    while (true) {
//      String ss = in.readLine();
//      System.out.println("waiting for empty row :"+ss);
//      if (ss.trim().equals("")) {
//        break;
//      }
//    }
  }
}