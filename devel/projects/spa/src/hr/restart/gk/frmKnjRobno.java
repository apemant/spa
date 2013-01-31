/****license*****************************************************************
**   file: frmKnjRobno.java
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

import hr.restart.baza.Shkonta;
import hr.restart.baza.dM;
import hr.restart.sisfun.frmParam;
import hr.restart.sisfun.raUser;
import hr.restart.swing.raMultiLineMessage;
import hr.restart.util.Aus;
import hr.restart.util.Util;
import hr.restart.util.VarStr;
import hr.restart.util.raGlob;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.sysoutTEST;
import hr.restart.zapod.raKonta;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2002
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class frmKnjRobno extends frmKnjizenje {

    private boolean statusCheck=true;
	
	private sysoutTEST ST = new sysoutTEST(false);

    private hr.restart.robno.TypeDoc TD = hr.restart.robno.TypeDoc.getTypeDoc();

    private hr.restart.robno.Util rut = hr.restart.robno.Util.getUtil();

    private ArrayList ALpotvrdaKnjizenja = new ArrayList();

    private HashMap appMES = new HashMap();

    private knjigAddPanel kAP;

    private hr.restart.util.lookupData ld = hr.restart.util.lookupData
            .getlookupData();

    private StorageDataSet tmpskstavke = null;
    
    private StorageDataSet globSheme = null;

    private boolean prijenosUQNX = false;

    private hr.restart.robno.raMMat mat = new hr.restart.robno.raMMat();

    private Calendar cal = Calendar.getInstance();

    private Variant variantShare = new Variant();

    public void initTmpStavkaSet() {
        cal.set(Calendar.DATE, 9);
        cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
        cal.set(Calendar.YEAR, 9999);
        Column identifikator = getKnjizenje().getStavkaSK()
                .getColumn("BROJDOK").cloneColumn();
        identifikator.setColumnName("IDENT");
        tmpskstavke = new StorageDataSet();
        tmpskstavke.close();
        tmpskstavke.setColumns(getKnjizenje().getStavkaSK().cloneColumns());
        tmpskstavke.addColumn(identifikator);
        tmpskstavke.open();
        //ST.prn(tmpskstavke);
    }
    private String tmpskssort;
    public frmKnjRobno() {

        dm.getVrtros().open();
        dm.getKonta_par().open();
        dm.getPartneri().open();
        dm.getShkonta().open();
        dm.getSklad().open();
        tmpskssort = frmParam.getParam("robno", "tmpskssort","","Po kojoj koloni da sortira skstavke pri knjizenju iz robnog-prazno=bez sort-a");
        getKnjizenje().setCNalogaMode(getKnjizenje().CNALMODE_8);
        kAP = new knjigAddPanel();
        this.jp.add(kAP, java.awt.BorderLayout.CENTER);
    }
    
    public void setKnjVals(int vrdok, String cvrnal, String brnal) {
      kAP.rcbULIZ.setSelectedIndex(vrdok);
      dataSet.setString("CVRNAL", cvrnal);
      kAP.setBrnal(brnal);
    }
    
    public void simulateStart() {
      okpanel.jBOK_actionPerformed();
    }

    private String getValuteOpis(DataSet ds) {
        
        QueryDataSet par = Util.getNewQueryDataSet("SELECT *FROM PARTNERI "+
                "where cpar = "+ds.getInt("CPAR"));
        if (!par.getString("DI").equalsIgnoreCase("I")) return "";
        
//System.out.println("getValuteOpis1");
        if (ds.getString("VRDOK").equalsIgnoreCase("PRK")
                || ds.getString("VRDOK").equalsIgnoreCase("KAL")) {
            QueryDataSet ads = Util.getNewQueryDataSet("SELECT * FROM DOKU "
                    + "WHERE CSKL='" + ds.getString("CSKL") + "' AND GOD='"
                    + ds.getString("GOD") + "' AND VRDOK='"
                    + ds.getString("VRDOK") + "' " + "AND BRDOK="
                    + ds.getInt("BRDOK"));
            if (ads.getRowCount() != 0) {
//                System.out.println("getValuteOpis2");
                if (ads.getBigDecimal("TECAJ")
                        .compareTo(Aus.zero2) != 0) {
//                    System.out.println("getValuteOpis3");
                    return " Devizni iznos = "
                            + ads.getBigDecimal("UINAB").divide(
                                    ads.getBigDecimal("TECAJ"), 2,
                                    BigDecimal.ROUND_HALF_UP);
                }
            }
        }
        return "";

    }

    private String getOpisStavke(DataSet ds) {
        String osip = "";
        try {

          osip= ds.getString("CSKL") + "-" + ds.getString("GOD") + "-"
                    + ds.getString("VRDOK") + "-" + ds.getInt("BRDOK");
        } catch (Exception ex) {
          try {
            osip= ds.getString("CSKLIZ") + "-" + ds.getString("CSKLUL") + "-"
                    + ds.getString("GOD") + "-" + ds.getString("VRDOK") + "-"
                    + ds.getInt("BRDOK");
          } catch (Exception ex2) {
            osip= "prijenos iz RK";
          }
        }
        if (frmParam.getParam("robno", "krkpnbz2opis", "N", "Ubaciti pnbz2 u opis stavke gk pri prijenosu u robno").equalsIgnoreCase("D")) {
          osip = ds.getString("PNBZ2")+" "+osip.trim();
        }
        return osip;
    }

    public void setStatusCheck(boolean _statusCheck) {
    	statusCheck=_statusCheck;
    }
    public boolean getStatusCheck() {
    	return statusCheck;
    }
    public boolean Validacija() {
        handleStatus();
        if (hr.restart.sisfun.frmParam.getParam("robno", "prijZim", "N",
                "Transfer temeljnice u Zim aplikaciju opcije:D ili N!")
                .equalsIgnoreCase("D")
                && !getFake()) {
            if (javax.swing.JOptionPane.showConfirmDialog(this,
                    "Želite li napraviti prijenos u financijsko (ZIM) ?",
                    "Upit", javax.swing.JOptionPane.YES_NO_OPTION,
                    javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {
                prijenosUQNX = true;
            } else {
                prijenosUQNX = false;
            }
        } else {
            prijenosUQNX = false;
        }
        return validacijaLocal();
    }

    public boolean validacijaLocal() {
        kAP.setupString();
        ArrayList alVrdok = kAP.getArrayListVrdok();
        String skladista = arrayList2String(kAP.getArrayListSklad(), true);
        String orgstruktura = arrayList2String(kAP.getArrayListOrgstr(), true);
        String uis = kAP.getUlazIzlazSvi();
        StorageDataSet shemeerror = checkShemeKnjizenje();
        if (shemeerror.getRowCount() != 0) {
            getKnjizenje().setErrorMessage("Nema shema knjiženja ");
            ST.showInFrame(shemeerror, "Nedostajuæe sheme knjiženja");
            return false;
        }
        if (!statusCheck) {
        	return true;
        }
        int count;
        if ((count = hasData(alVrdok, skladista, orgstruktura, uis)) == 0) {
            //			getKnjizenje().setErrorMessage(
            //					"Nema podataka za knjiženje " + count);
            javax.swing.JOptionPane.showMessageDialog(null,
                    "Nema podataka za knjiženje !", "Greška",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;

    }

    public void SetFokus() {
        kAP.componentShow();
        super.SetFokus();
    }

    private boolean mycommitTransfer() {
      if (kAP != null && kAP.getBrnal().length() > 0) return true;
      
        QueryDataSet qds = null;
        String myCGK = getKnjizenje().cGK;
        String uis = kAP.getUlazIzlazSvi();
        Timestamp datknj = this.jtDATUMKNJ.getDataSet().getTimestamp(
                jtDATUMKNJ.getColumnName());
        for (int i = 0; i < ALpotvrdaKnjizenja.size(); i++) {
            qds = (QueryDataSet) ALpotvrdaKnjizenja.get(i);
            if (TD.isDocMeskla(qds.getString("VRDOK"))) {
                if (qds.getString("VRDOK").equalsIgnoreCase("MEU")
                        || (qds.getString("VRDOK").equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("U") || uis
                                .equalsIgnoreCase("S")))) {
                    for (qds.first(); qds.inBounds(); qds.next()) {
                        qds.setString("BRNALU", myCGK);
                        qds.setTimestamp("DATKNJU", datknj);
                        qds.setString("STATKNJU", "K");
                    }
                }
                if (qds.getString("VRDOK").equalsIgnoreCase("MEI")
                        || (qds.getString("VRDOK").equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("I") || uis
                                .equalsIgnoreCase("S")))) {
                    for (qds.first(); qds.inBounds(); qds.next()) {
                        qds.setString("BRNAL", myCGK);
                        qds.setTimestamp("DATKNJ", datknj);
                        qds.setString("STATKNJI", "K");
                    }
                }
            } else {
                for (qds.first(); qds.inBounds(); qds.next()) {
                    qds.setString("BRNAL", myCGK);
                    qds.setTimestamp("DATKNJ", datknj);
                    qds.setString("STATKNJ", "K");
                }
            }
            raTransaction.saveChanges(qds);
        }
        ALpotvrdaKnjizenja = null;
        return true;
    }

    public boolean commitTransfer() {

        if (!super.commitTransfer())
            return false;
        /*
         * stavljeno u transakciju zajedno sa obradom naloga return new
         * raLocalTransaction() { public boolean transaction() throws
         * java.lang.Exception { return mycommitTransfer(); }
         * }.execTransaction();
         */
        try {
          return mycommitTransfer();
        } catch (Exception e) {
          e.printStackTrace();
          return alwaysHasData();//Ako ne zeli da se provjerava jel ima podtaka 
                                 //znaci da se radi o vanjskom izvoru pa nema ni sto komitati.
                                 //Tu se javlja greska kod izmisljenih vrsta dokumenata koje nisu
                                 //hardkodirane u TypeDoc i slicnim grozotama 
        }
    }

    public void Newtmpskstavke(String konto, String corg) {
        tmpskstavke.insertRow(true);
        tmpskstavke.setString("BROJKONTA", konto);
        tmpskstavke.setString("CORG", corg);
        tmpskstavke.setBigDecimal("IP", Aus.zero2);
        tmpskstavke.setBigDecimal("ID", Aus.zero2);
    }

    public boolean handleStatus() {
        if (!getFake())
            return true;
        QueryDataSet ulazi = null;
        QueryDataSet izlazi = null;
        QueryDataSet meskla = null;

        ulazi = hr.restart.util.Util.getNewQueryDataSet(
                "SELECT * FROM DOKU WHERE STATKNJ='P'", true);
        izlazi = hr.restart.util.Util.getNewQueryDataSet(
                "SELECT * FROM DOKI WHERE STATKNJ='P'", true);
        meskla = hr.restart.util.Util
                .getNewQueryDataSet(
                        "SELECT * FROM MESKLA WHERE STATKNJI='P' OR STATKNJU='P'",
                        true);

        boolean bneed2update = (ulazi != null && ulazi.getRowCount() > 0)
                || (izlazi != null && izlazi.getRowCount() > 0)
                || (meskla != null && meskla.getRowCount() > 0);

        if (bneed2update) {
            if (javax.swing.JOptionPane
                    .showConfirmDialog(
                            this,
                            "Želite li potvrditi prethodni prijenos u financijsko (ZIM) ?",
                            "Upit", javax.swing.JOptionPane.YES_NO_OPTION,
                            javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION) {

                if (mytransaction(ulazi, izlazi, meskla, "K")) {
                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Prethodni prijenos je uspjesno potvr\u0111en !",
                            "Poruka",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);
                    return true;
                } else {
                    return false;
                }
            } else {
                if (mytransaction(ulazi, izlazi, meskla, "N")) {

                    javax.swing.JOptionPane.showMessageDialog(this,
                            "Prethodni prijenos je PONISTEN !", "Poruka",
                            javax.swing.JOptionPane.INFORMATION_MESSAGE);

                } else
                    return false;

            }
        }
        return true;
    }

    private void setStatus(DataSet ds, String polje, String value) {
        for (ds.first(); ds.inBounds(); ds.next()) {
            ds.setString(polje, value);
        }
    }

    private boolean mytransaction(final QueryDataSet ulazi,
            final QueryDataSet izlazi, final QueryDataSet meskla,
            final String valuestat) {

        return new raLocalTransaction() {
            public boolean transaction() throws Exception {
                if (ulazi != null && ulazi.getRowCount() > 0) {
                    setStatus(ulazi, "STATKNJ", valuestat);
                    saveChanges(ulazi);
                }
                if (izlazi != null && izlazi.getRowCount() > 0) {
                    setStatus(izlazi, "STATKNJ", valuestat);
                    saveChanges(izlazi);
                }
                if (meskla != null && meskla.getRowCount() > 0) {
                    setStatus(meskla, "STATKNJU", valuestat);
                    setStatus(meskla, "STATKNJI", valuestat);
                    saveChanges(meskla);
                }
                return true;
            }
        }.execTransaction();
    }

    public boolean handleshema(String sqlsheme) {

//        System.out.println("sqlsheme=" + sqlsheme);

        if (sqlsheme == null || sqlsheme.length() == 0)
            return false;

        QueryDataSet sheme = null;
        try {
            sheme = hr.restart.util.Util.getNewQueryDataSet(sqlsheme, true);
        } catch (Exception ex) {
//            System.out.println("sqlsheme=" + sqlsheme);
            ex.printStackTrace();
            return false;
        }

        boolean statira = false;
        QueryDataSet podaci = null;
        String sqlupit = null;
        for (sheme.first(); sheme.inBounds(); sheme.next()) {
            //			sqlupit = getChangeSqlCondition(sheme.getString("SQLCONDITION"));
            sqlupit = getChangeSqlCondition(sheme);
            if (sqlupit == null || sqlupit.length() == 0)
                continue;
            if (sqlupit.toLowerCase().indexOf("statira=") > 0 ||
                sqlupit.toLowerCase().indexOf("statira =") > 0)
              statira = true;
            try {
                podaci = hr.restart.util.Util.getNewQueryDataSet(sqlupit, true);
//                System.out.println("sqlupit=" + sqlupit);
            } catch (Exception ex) {

//                System.out.println("sqlupit=" + sqlupit);
                ex.printStackTrace();

            }
            for (podaci.first(); podaci.inBounds(); podaci.next()) {
                //				Newtmpskstavke(sheme.getString("BROJKONTA"),podaci.getString("CORG"));
                maintanceStavkaKnjizenja(sheme.getString("BROJKONTA"), podaci,
                        podaci.getString("CORG"), sheme.getShort("CKOLONE"),
                        sheme.getString("CKNJIGE"));
                BigDecimal iznos = null;
                BigDecimal iznosdev = null;
System.out.println("**** maintance prosao " + sheme.getString("POLJE"));

                
                if (podaci.hasColumn("DEVIZN")!=null) {
System.out.println("**** DEVIZNI ANLAGE");
                    
                  try {
                    try {
                      iznosdev = podaci.getBigDecimal("DEVIZN");
                    } catch (Exception ex) {
                      //ex.printStackTrace();
                      iznosdev = new BigDecimal(podaci.getDouble("DEVIZN"));
                      iznosdev = iznosdev.setScale(2, BigDecimal.ROUND_HALF_UP);
                    }
                    if (sheme.getString("KARAKTERISTIKA").equals("D")) {
                        tmpskstavke.setBigDecimal("DEVID", tmpskstavke.getBigDecimal(
                        "DEVID").add(iznosdev));
                    } else if (sheme.getString("KARAKTERISTIKA").equals("P")) {
                        tmpskstavke.setBigDecimal("DEVIP", tmpskstavke.getBigDecimal(
                        "DEVIP").add(iznosdev));
                    }                	
                    if (podaci.hasColumn("OZNVAL")!=null) {
                        tmpskstavke.setString("OZNVAL", podaci.getString("OZNVAL"));
                    	
                    }
                    if (podaci.hasColumn("TECAJ")!=null) {
                        tmpskstavke.setBigDecimal("TECAJ", podaci.getBigDecimal(
                        "TECAJ"));
                    }
                    System.out.println("Iznos devizni "+iznosdev+" oznval "+
                        tmpskstavke.getString("OZNVAL")+
                        podaci.getBigDecimal(
                        "TECAJ"));
                  } catch (RuntimeException e) {
                    e.printStackTrace();
                    throw e;
                  }
              	
                }

                //				podaci.getVariant(sheme.getString("POLJE"),variantShare);
                //				iznos = new BigDecimal(variantShare.getAsDouble());
                //				iznos = iznos.setScale(2,BigDecimal.ROUND_HALF_UP);
                try {
                    iznos = podaci.getBigDecimal(sheme.getString("POLJE"));
                } catch (Exception ex) {
                    try {
                      iznos = new BigDecimal(podaci.getDouble(sheme
                              .getString("POLJE")));
                    } catch (Exception e) {
                      System.out.println(sheme);
                      System.out.println(podaci);
                      throw (RuntimeException) e;
                    }
                    iznos = iznos.setScale(2, BigDecimal.ROUND_HALF_UP);
                }

                
                if (sheme.getString("KARAKTERISTIKA").equals("D")) {
                    tmpskstavke.setBigDecimal("ID", tmpskstavke.getBigDecimal(
                            "ID").add(iznos));
                  } else if (sheme.getString("KARAKTERISTIKA").equals("P")) {
                    tmpskstavke.setBigDecimal("IP", tmpskstavke.getBigDecimal(
                            "IP").add(iznos));
                  }
            }
            
        }
        
        return statira;
    }
    

    public String getChangeSqlCondition(DataSet sheme) {

        VarStr vs = new VarStr(sheme.getString("SQLCONDITION"));
        vs.replaceAll("[DATUM]", "'"
                + new java.sql.Date(Util.getUtil().addDays(
                        dataSet.getTimestamp("DATUMDO"), 1).getTime())
                        .toString() + "'");
        vs.replaceAll("[CSKL]", "'" + sheme.getString("CSKL") + "'");
        vs.replaceAll("[SKLAD]", "'" + sheme.getString("CSKL") + "'");
        vs.replaceAll("[CSKLIZ]", "'" + sheme.getString("CSKL") + "'");
        vs.replaceAll("[CSKLUL]", "'" + sheme.getString("CSKLUL") + "'");
        vs.replaceAll("[SHR2]", "'"
                + hr.restart.sisfun.frmParam.getParam(
                        "robno",
                        "shr2",
                        "",
                        "Shema temeljnice za R2 raèune kod prijenosa kalkulacija :"
                                + "prazno = ne koristi se!").trim() + "'");
        vs.replaceAll("[SHCAR]", "'"
                + hr.restart.sisfun.frmParam.getParam(
                        "robno",
                        "shcar",
                        "",
                        "Shema temeljnice za raèune carine kod prijenosa kalkulacija :"
                                + "prazno = ne koristi se!").trim() + "'");
        vs.replaceAll("[PARKUP]", hr.restart.sisfun.frmParam.getParam(
                "robno",
                "parkup",
                "",
                "Šifra partnera za knjigu C internih raèuna :"
                        + "prazno = ne koristi se!").trim());

        if (!getStatusCheck()) {
        	vs.replaceLast("'K'", "'QW' AND DATDOK>'"
                    + new java.sql.Date(Util.getUtil().addDays(
                            dataSet.getTimestamp("DATUMKNJ"), 1).getTime())
                            .toString() + "' ");
        }
        if (kAP != null && kAP.getBrnal().length() > 0) {
          vs.replaceLast("'K'", "'QW' AND brnal='"+kAP.getBrnal()+"' ");
          if (sheme.getString("VRDOK").equals("MEU") ||
              (sheme.getString("VRDOK").equals("MES") &&
                  sheme.getShort("CKOLONE") == 1))
            vs.replaceLast("brnal=", "brnalu=");
          
          System.out.println(vs);
        }
        return vs.toString();

        /*
         * and partneri.ugovor='D' ,[SHR2] as SHEMA
         */
    }

    public void maintanceStavkaKnjizenja(String konto, DataSet data,
            String corg, short kolona, String knjiga) {

        //System.out.println("KONTO "+konto+" --- CORG "+corg);
        String opisstavke = getOpisStavke(data);
        String brojdok = TD.isDocUlaz(data.getString("VRDOK")) ? (data
                .getString("BRRAC").equalsIgnoreCase("") ? opisstavke : data
                .getString("BRRAC")) : getPNBZ2Prefix()+data.getString("PNBZ2");
        String ident = null;
        if (data.hasColumn("CPAR") != null) {
            ident = brojdok
                    + (TD.isDocMeskla(data.getString("VRDOK")) ? "" : "-"
                            + data.getInt("CPAR"));
        } else {
            ident = brojdok;
        }

        Timestamp datdok = null;
        try {
            datdok = data.getTimestamp("DATDOK");
        } catch (Exception ex) {
            datdok = data.getTimestamp("DATRAC");
        }
        if (hr.restart.zapod.raKonta.isSaldak(konto)) {
            //			if (ld.raLocate(tmpskstavke, new String[] { "BROJKONTA",
            // "IDENT","CKNJIGE","CKOLONE"},
            //					new String[] { konto, ident,knjiga,String.valueOf(kolona) })) {
            if (ld.raLocate(tmpskstavke, new String[] { "BROJKONTA", "IDENT" },
                    new String[] { konto, ident })) {

                if (data.hasColumn("GLAVNA") != null) {
//                    System.out.println("Imamo glavnu nutra ralocate");
                    tmpskstavke.setString("GLAVNA", "D");
                }
/*
                System.out.println("Broj konta " + konto + "..." + ident
                        + "---" + tmpskstavke.getString("GLAVNA") + "---"
                        + tmpskstavke.getShort("CKOLONE") + "---"
                        + tmpskstavke.getString("CKNJIGE") + "---" + "GLAVNA "
                        + tmpskstavke.getString("GLAVNA"));
*/
                return;
            }
            Newtmpskstavke(konto, corg);
            if (data.hasColumn("GLAVNA") != null) {
//                System.out.println("Imamo glavnu A");
                tmpskstavke.setString("GLAVNA", "D");
            }
            //			System.out.println("GLAVNA = " +
            // tmpskstavke.getString("GLAVNA"));
            tmpskstavke.setTimestamp("DATDOK", datdok);
            if (data.hasColumn("DATDOSP") != null) {
                tmpskstavke.setTimestamp("DATDOSP", data
                        .getTimestamp("DATDOSP"));
            }
            tmpskstavke.setTimestamp("DATPRI", datdok);
            String pero = "Dokument " + opisstavke
            + getValuteOpis(data);
            if (pero.length()>100) {
                pero = opisstavke+ getValuteOpis(data);
                if (pero.length()>100) {
                    pero = pero.substring(0,100);
                }
            }
            tmpskstavke.setString("OPIS",pero );
            tmpskstavke.setInt("CPAR", data.getInt("CPAR"));
            //  
            //			tmpskstavke.setString("URAIRA", TD.isDocUlaz(data
            //					.getString("VRDOK")) ? "U" : "I");
            //			
            if (TD.isDocUlaz(data.getString("VRDOK"))) {
                tmpskstavke.setString("BROJDOK", data.getString("BRRAC"));
                tmpskstavke.setString("IDENT", ident);
                tmpskstavke.setTimestamp("DATDOSP", data
                        .getTimestamp("DATDOSP"));
                if (data.getString("BRRAC").equalsIgnoreCase("")) {
                    // ako nema brrac moramo forsati !!!
                    //					tmpskstavke.setString()
                    tmpskstavke.setString("BROJDOK", opisstavke);
                    tmpskstavke.setString("IDENT", (tmpskstavke
                            .getString("BROJDOK")
                            + "-" + data.getInt("CPAR")));
                    tmpskstavke.setTimestamp("DATDOSP", datdok);
                }
                if (data.hasColumn("SHEMA") != null) {
                    //					System.out.println("IMAMO SHEMU");
                    tmpskstavke.setString("CSKL", data.getString("SHEMA"));
                    tmpskstavke.setTimestamp("DATPRI", new Timestamp(cal
                            .getTime().getTime()));
                }
                /*
                 * String urashema = hr.restart.sisfun.frmParam
                 * .getParam("robno", "shemaR", "N", "Shema temeljnice za R2
                 * raèune kod prijenosa kalkulacija :N= ne koristi se!");
                 * 
                 * if (!urashema.equalsIgnoreCase("N")) { if
                 * (ld.getlookupData().raLocate(dm.getPartneri(), "CPAR",
                 * String.valueOf(data.getInt("CPAR")))) { if
                 * (dm.getPartneri().getString("DI").equalsIgnoreCase( "D") &&
                 * dm.getPartneri().getString("UGOVOR") .equalsIgnoreCase("D")) {
                 * //System.out.println("Oðe sam "+new //
                 * Timestamp(cal.getTime().getTime())); // hardcoded za obrtnika
                 * ako je u ugovoru D onda je // kao obrtnik
                 * tmpskstavke.setString("CSKL", urashema);
                 * tmpskstavke.setTimestamp("DATPRI", new Timestamp(
                 * cal.getTime().getTime())); } } }
                 */
            } else {
                try {
                    tmpskstavke.setString("BROJDOK", getPNBZ2Prefix()+data.getString("PNBZ2"));
                    tmpskstavke.setString("IDENT", ident);
                    tmpskstavke.setString("EXTBRDOK", String.valueOf(data
                            .getInt("BRDOK")));
                } catch (Exception ex) {
                    tmpskstavke.setString("BROJDOK", String.valueOf(data
                            .getInt("BRDOK")));
                    tmpskstavke.setString("IDENT", ident);
                    tmpskstavke.setString("EXTBRDOK", String.valueOf(data
                            .getInt("BRDOK")));
                }
                tmpskstavke.setTimestamp("DATDOSP", data
                        .getTimestamp("DATDOSP"));

            }
            if (!knjiga.equalsIgnoreCase("")) {
//                tmpskstavke.setString("URAIRA", TD.isDocUlaz(data
//                        .getString("VRDOK")) ? "U" : "I");
                tmpskstavke.setShort("CKOLONE", kolona);
                tmpskstavke.setString("CKNJIGE", knjiga);
            }
            tmpskstavke.setString("URAIRA", TD.isDocUlaz(data
                    .getString("VRDOK")) ? "U" : "I");
        } else {
            if (ld.raLocate(tmpskstavke, new String[] { "BROJKONTA", "IDENT",
                    "CORG" }, new String[] { konto, brojdok, corg })) {
                if (data.hasColumn("GLAVNA") != null) {
//                    System.out.println("Imamo glavnu nutra raLocate 2");
                    tmpskstavke.setString("GLAVNA", "D");
                }
                return;
            }
            Newtmpskstavke(konto, corg);
            tmpskstavke.setTimestamp("DATDOK", datdok);
            if (data.hasColumn("DATDOSP") != null) {
                tmpskstavke.setTimestamp("DATDOSP", data
                        .getTimestamp("DATDOSP"));
            }
            tmpskstavke.setTimestamp("DATPRI", datdok);
            if (detaljniOpisNonSK() && !raKonta.isZbirni(konto)) {
              tmpskstavke.setString("OPIS", opisstavke);
            } else {
              tmpskstavke.setString("OPIS", "prijenos iz RK");//getStavka vraca
            }
            // stavku za GK
            tmpskstavke.setString("BROJDOK", "");
            tmpskstavke.setString("IDENT", ident);

            if (data.hasColumn("GLAVNA") != null) {
//                System.out.println("Imamo glavnu B");
                tmpskstavke.setString("GLAVNA", "D");
            }

            if (!knjiga.equalsIgnoreCase("")) {
                tmpskstavke.setString("BROJDOK", brojdok);
                tmpskstavke.setInt("CPAR", data.getInt("CPAR"));
                tmpskstavke.setString("URAIRA", TD.isDocUlaz(data
                        .getString("VRDOK")) ? "U" : "I");
                tmpskstavke.setShort("CKOLONE", kolona);
                tmpskstavke.setString("CKNJIGE", knjiga);
            }
        }
    }
    String pnbz2prefix = null;
    private String getPNBZ2Prefix() {
      if (pnbz2prefix==null) pnbz2prefix = frmParam.getParam("gk", "pnbz2prefix","","Prefiks pozivu na broj pri prijemu iz robnog");
      return pnbz2prefix;
    }
    private boolean detaljniOpisNonSK() {
      return frmParam.getParam("robno", "knjdetopisNSK", "D", 
          "Ubaciti broj dokumenta u opis pojedinacne financijske stavke pri knjizenju u GK (D'N)")
          .equalsIgnoreCase("D");
    }
    
    private boolean checkOutDate() {
      if (getFake() || raUser.getInstance().isRoot()) return true;
      
      Timestamp datumknj = dataSet.getTimestamp("DATUMKNJ");
      Timestamp first = null;
      Timestamp last = null;
      for (tmpskstavke.first(); tmpskstavke.inBounds(); tmpskstavke.next()) {
        if (tmpskstavke.getBigDecimal("ID").signum() == 0
            && tmpskstavke.getBigDecimal("IP").signum() == 0) continue;
        Timestamp datdok = tmpskstavke.getTimestamp("DATDOK");
        if (first == null || first.after(datdok))
          first = new Timestamp(datdok.getTime());
        if (last == null || last.before(datdok))
          last = new Timestamp(datdok.getTime());
      }
      if (first == null) first = datumknj;
      if (last == null) last = datumknj; 
      if (!Util.getUtil().sameMonth(first, datumknj) ||
          !Util.getUtil().sameMonth(last, datumknj)) {
        clearProcessMessage();
        setEnabled(true);
      } else return true;
      if (!Util.getUtil().sameMonth(first, datumknj)) {
        int resp = JOptionPane.showConfirmDialog(this, 
            new raMultiLineMessage("Knjiženje zahvaæa dokument s datumom "+
                Aus.formatTimestamp(first) + "\nJeste li sigurni da ga želite "+
                "proknjižiti s datumom knjiženja " + Aus.formatTimestamp(datumknj) +
                "?"), "Pogrešan mjesec knjiženja", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
          getKnjizenje().setErrorMessage("Pogrešan mjesec knjiženja");
          return false;
        }
      }
      if (!Util.getUtil().sameMonth(first, last) &&
    		  !Util.getUtil().sameMonth(last, datumknj)) {
        int resp = JOptionPane.showConfirmDialog(this, 
            new raMultiLineMessage("Knjiženje zahvaæa dokument s datumom "+
                Aus.formatTimestamp(last) + "\nJeste li sigurni da ga želite "+
                "proknjižiti s datumom knjiženja " + Aus.formatTimestamp(datumknj) +
                "?"), "Pogrešan mjesec knjiženja", JOptionPane.YES_NO_OPTION);
        if (resp != JOptionPane.YES_OPTION) {
          getKnjizenje().setErrorMessage("Pogrešan mjesec knjiženja");
          return false;
        }
      }
      dwin = hr.restart.sisfun.raDelayWindow.show(this,"Obrada","Knjiženje u tijeku ...");
      setEnabled(false);
      return true;
    }

    private String getTmpSKSSortField() {
      return tmpskssort==null?"":tmpskssort;
    }
    public boolean realKnjizenje() {
        if (!checkOutDate()) return false;
      
        BigDecimal sumip = Aus.zero2;
        BigDecimal sumid = Aus.zero2;
        BigDecimal odstup = Aus.zero2;
        String kontras = frmParam.getParam("robno", "kontras", "739000",
                "Razlike po zaokruženju - rashod (D side)");
        String kontpri = frmParam.getParam("robno", "kontpri", "789090",
                "Razlike po zaokruženju - prihod (P side)");
        try {
            odstup = new BigDecimal(frmParam.getParam("robno", "knjOdst",
                    "0.00", "Dozvoljeno odstupanje po temeljnici"));
        } catch (Exception ex) {
        }
        //for (tmpskstavke.first(); tmpskstavke.inBounds(); tmpskstavke.next()) {
            //			sumip = sumip.add(tmpskstavke.getBigDecimal(""));
            //			sumid = sumid.add(tmpskstavke.getBigDecimal(""));
        //}
        BigDecimal razlika = sumip.subtract(sumid);
        if (razlika.floatValue() != 0) {
            if (sumip.subtract(sumid).abs().compareTo(odstup) > 0) {
            } else {
            }
        }

        if (!getKnjizenje().startKnjizenje(this))
            return false;
        StorageDataSet sds = null;
        dm.getKonta().open();
        // debug
        /*
         * for (tmpskstavke.first(); tmpskstavke.inBounds(); tmpskstavke.next()) {
         * if (!tmpskstavke.getString("GLAVNA").equalsIgnoreCase("")) {
         * System.out.println("CPAR "+tmpskstavke.getInt("CPAR"));
         * System.out.println("BROJDOK "+ tmpskstavke.getString("BROJDOK"));
         * System.out.println("EXTBRDOK "+tmpskstavke.getString("EXTBRDOK"));
         * System.out.println("URAIRA "+tmpskstavke.getString("URAIRA"));
         * System.out.println("CKOLONE "+tmpskstavke.getShort("CKOLONE"));
         * System.out.println("CKNJIGE "+tmpskstavke.getString("CKNJIGE"));
         * System.out.println("GLAVNA "+ tmpskstavke.getString("GLAVNA")); } }
         */
        //		
        //		ST.showInFrame(tmpskstavke,"ADA");
        if (!"".equals(getTmpSKSSortField())) {//"BROJDOK"
          tmpskstavke.setSort(new SortDescriptor(new String[] {getTmpSKSSortField()}));
        }
        for (tmpskstavke.first(); tmpskstavke.inBounds(); tmpskstavke.next()) {
            if (tmpskstavke.getBigDecimal("ID").doubleValue() == 0
                    && tmpskstavke.getBigDecimal("IP").doubleValue() == 0) {
                //				System.out.println("GLAVNA koja ispada"
                //						+ tmpskstavke.getString("GLAVNA"));
                continue;
            }
            sds = getKnjizenje().getNewStavka(
                    tmpskstavke.getString("BROJKONTA"),
                    tmpskstavke.getString("CORG"));
            if (sds != null) {
                getKnjizenje().setID(tmpskstavke.getBigDecimal("ID"));
                getKnjizenje().setIP(tmpskstavke.getBigDecimal("IP"));
                
                sds.setBigDecimal("DEVID",tmpskstavke.getBigDecimal("DEVID"));
                sds.setBigDecimal("DEVIP",tmpskstavke.getBigDecimal("DEVIP"));       
                sds.setString("OZNVAL",tmpskstavke.getString("OZNVAL"));
                sds.setBigDecimal("TECAJ",tmpskstavke.getBigDecimal("TECAJ"));
                
//System.out.println(sds.getBigDecimal("DEVID")+" "+sds.getBigDecimal("DEVIP")+" "+
//		sds.getString("OZNVAL")+" "+sds.getBigDecimal("TECAJ"));                
                
                sds.setTimestamp("DATDOK", tmpskstavke.getTimestamp("DATDOK"));
                //        if (getKnjizenje().isLastKontoZbirni()) {
                //          sds.setString("OPIS","prijenos iz RK");
                //        } else {
                sds.setString("OPIS", tmpskstavke.getString("OPIS"));
                //        }
                if (getKnjizenje().isLastKontoZbirni()) {
                    getKnjizenje().getStavka().setString("OPIS",
                            "prijenos iz RK");
                }
                sds.setInt("CPAR", tmpskstavke.getInt("CPAR"));
                sds.setString("BROJDOK", tmpskstavke.getString("BROJDOK"));
                sds.setString("EXTBRDOK", tmpskstavke.getString("EXTBRDOK"));
                sds
                        .setTimestamp("DATDOSP", tmpskstavke
                                .getTimestamp("DATDOSP"));
                sds.setString("URAIRA", tmpskstavke.getString("URAIRA"));
                sds.setShort("CKOLONE", tmpskstavke.getShort("CKOLONE"));
                sds.setString("CKNJIGE", tmpskstavke.getString("CKNJIGE"));
                sds.setString("GLAVNA", tmpskstavke.getString("GLAVNA"));
                sds.setTimestamp("DATPRI", tmpskstavke.getTimestamp("DATPRI"));
                sds.setString("CSKL", tmpskstavke.getString("CSKL"));
                //				System.out.println("GLAVNA za knjizenje "
                //						+ tmpskstavke.getString("GLAVNA"));
                getKnjizenje().saveStavka();
            } else {
                System.out.println("Nulll ....");
                Math.sqrt((double) 0.1);
            }
        }
        if (prijenosUQNX) {
            transfer2Zim();
        }
        return getKnjizenje().saveAll();
    }

    public void transfer2Zim() {
        prepare2Zim pz = new prepare2Zim();
        pz.openFiles();
        pz.setForKnjizenje(getKnjizenje().getStavka());
        pz.makeTransferFiles(false);
        pz.setForKnjizenje(getKnjizenje().getStavkaSK());
        pz.makeTransferFiles(true);
        pz.makeTransferFilesKnjiga();
        pz.closeAll();
        pz.transfer2QnxServer();
    }

    public void mesklaSetupApproval(String appruvalshema, String vrdok,
            String uis) {
        QueryDataSet appruvalsh = Util.getNewQueryDataSet(appruvalshema, true);
        for (appruvalsh.first(); appruvalsh.inBounds(); appruvalsh.next()) {
            prepare4approval(vrdok, appruvalsh.getString("CSKLIZ"), appruvalsh
                    .getString("CSKLUL"), uis, false);
        }
    }

    public void prepare4approval(String vrdok, String cskl, String csklul,
            String uis, boolean statira) {
        String sqlStr = null;
        if (TD.isDocStdoku(vrdok)) {
            sqlStr = "SELECT * from doku WHERE doku.vrdok ='" + vrdok + "' "
                    + " AND doku.datdok " + getDatumDoSQL()
                    + " AND doku.statknj not in ('K') AND DOKU.CSKL IN ('"
                    + cskl + "')";
            addInPotvrdaKnjizenje(sqlStr);
        } else if (TD.isDocStdoki(vrdok)) {
            sqlStr = "SELECT * from doki WHERE doki.vrdok ='" + vrdok + "' "
                    + " AND doki.datdok " + getDatumDoSQL()
                    + (statira ? " AND doki.statira != 'N' " : "")
                    + " AND doki.statknj not in ('K') AND DOKI.CSKL IN ('"
                    + cskl + "')";
            addInPotvrdaKnjizenje(sqlStr);
        } else if (TD.isDocStmeskla(vrdok)) {
            if (vrdok.equalsIgnoreCase("MEU")
                    || (vrdok.equalsIgnoreCase("MES") && (uis
                            .equalsIgnoreCase("U") || uis.equalsIgnoreCase("S")))) {
                sqlStr = "SELECT * from meskla WHERE meskla.vrdok ='" + vrdok
                        + "' " + " AND meskla.datdok " + getDatumDoSQL()
                        + " AND meskla.statknju not in ('K') AND "
                        + " meskla.CSKLUL IN ('" + csklul + "') AND "
                        + " meskla.CSKLIZ IN ('" + cskl + "')";
                addInPotvrdaKnjizenje(sqlStr);
            }
            if (vrdok.equalsIgnoreCase("MEI")
                    || (vrdok.equalsIgnoreCase("MES") && (uis
                            .equalsIgnoreCase("I") || uis.equalsIgnoreCase("S")))) {
                sqlStr = "SELECT * from meskla WHERE meskla.vrdok ='" + vrdok
                        + "' " + " AND meskla.datdok " + getDatumDoSQL()
                        + " AND meskla.statknji not in ('K') AND "
                        + " meskla.CSKLUL IN ('" + csklul + "') AND "
                        + " meskla.CSKLIZ IN ('" + cskl + "')";
                addInPotvrdaKnjizenje(sqlStr);
            }
        }
    }

    public void addInPotvrdaKnjizenje(String sqlStr) {
        if (ALpotvrdaKnjizenja == null) {
            ALpotvrdaKnjizenja = new ArrayList();
        }
        QueryDataSet qds = hr.restart.util.Util
                .getNewQueryDataSet(sqlStr, true);
        if (qds.getRowCount() != 0) {
            ALpotvrdaKnjizenja.add(qds);
        }
    }

    public boolean okPress() throws Exception {

        ALpotvrdaKnjizenja = null;
        appMES = null;
        kAP.setupString();
        ArrayList alVrdok = kAP.getArrayListVrdok();
        ArrayList alSkladista = kAP.getArrayListSklad();
        ArrayList alOrgstr = kAP.getArrayListOrgstr();
        checkDokument(alVrdok, alSkladista, alOrgstr);

        String skladista = arrayList2String(alSkladista, true);
        String orgstruktura = arrayList2String(alOrgstr, true);
        String uis = kAP.getUlazIzlazSvi();
        initTmpStavkaSet();
        QueryDataSet sheme = null;
        QueryDataSet podaci = null;
        String sqlsheme = null;
        String vrdok = null;
        String appruvalshema = null;

        for (int i = 0; i < alVrdok.size(); i++) {
            vrdok = (String) alVrdok.get(i);
            if (TD.isDocMeskla(vrdok)) {
                if (vrdok.equalsIgnoreCase("MEU")
                        || (vrdok.equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("U") || uis
                                .equalsIgnoreCase("S")))) {
                    for (int cnt = 0; cnt < alSkladista.size(); cnt++) {
                        sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                                + vrdok + "' and csklul='"
                                + alSkladista.get(cnt) + "' and ckolone=1";
                        handleshema(sqlsheme);
                        appruvalshema = "select max(cskl) as cskliz,max(csklul) as csklul "
                                + "from Shkonta where VRDOK = '"
                                + vrdok
                                + "' and csklul='"
                                + alSkladista.get(cnt)
                                + "' group by shkonta.cskl";
                        mesklaSetupApproval(appruvalshema, vrdok, uis);
                    }
                }
                if (vrdok.equalsIgnoreCase("MEI")
                        || (vrdok.equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("I") || uis
                                .equalsIgnoreCase("S")))) {
                    for (int cnt = 0; cnt < alSkladista.size(); cnt++) {
                        sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                                + vrdok + "' and cskl='" + alSkladista.get(cnt)
                                + "' and ckolone=2";
                        ;
                        handleshema(sqlsheme);
                        appruvalshema = "select max(cskl) as cskliz,max(csklul) as csklul "
                                + "from Shkonta where VRDOK = '"
                                + vrdok
                                + "' and cskl='"
                                + alSkladista.get(cnt)
                                + "' group by shkonta.csklul";
                        mesklaSetupApproval(appruvalshema, vrdok, uis);
                    }

                }
            } else {
                if (TD.isDocOJ(vrdok)) {
                    for (int cnt = 0; cnt < alOrgstr.size(); cnt++) {
                        sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                                + vrdok + "' and cskl='" + alOrgstr.get(cnt)
                                + "' order by stavka";
                        boolean statira = handleshema(sqlsheme);
                        prepare4approval(vrdok, (String) alOrgstr.get(cnt),
                                (String) alOrgstr.get(cnt), uis, statira);
                    }
                    for (globSheme.first(); globSheme.inBounds(); globSheme.next()) {
                    	if (globSheme.getString("VRDOK").equals(vrdok)) {
                    		raGlob cg = new raGlob(globSheme.getString("CSKL"));
                    		sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                          + vrdok + "' and cskl='" + globSheme.getString("CSKL")
                          + "' order by stavka";
                    		boolean statira = handleshema(sqlsheme);
                    		
                    		for (int cnt = 0; cnt < alOrgstr.size(); cnt++) {
                    			if (cg.matches((String) alOrgstr.get(cnt)))
                    				prepare4approval(vrdok, (String) alOrgstr.get(cnt),
                                (String) alOrgstr.get(cnt), uis, statira);		
                    		}
                    	}
                    }
                    
                } else {
                    for (int cnt = 0; cnt < alSkladista.size(); cnt++) {
                        sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                                + vrdok + "' and cskl='" + alSkladista.get(cnt)
                                + "' order by stavka";
                        boolean statira = handleshema(sqlsheme);
                        prepare4approval(vrdok, (String) alSkladista.get(cnt),
                                (String) alSkladista.get(cnt), uis, statira);
                    }
                    
                    for (globSheme.first(); globSheme.inBounds(); globSheme.next()) {
                    	if (globSheme.getString("VRDOK").equals(vrdok)) {
                    		raGlob cg = new raGlob(globSheme.getString("CSKL"));
                    		sqlsheme = "SELECT * FROM Shkonta WHERE VRDOK = '"
                          + vrdok + "' and cskl='" + globSheme.getString("CSKL")
                          + "' order by stavka";
                    		boolean statira = handleshema(sqlsheme);
                    		
                    		for (int cnt = 0; cnt < alSkladista.size(); cnt++) {
                    			if (cg.matches((String) alSkladista.get(cnt)))
                    				prepare4approval(vrdok, (String) alSkladista.get(cnt),
                                (String) alSkladista.get(cnt), uis, statira);		
                    		}
                    	}
                    }
                }
            }
        }
        return realKnjizenje();
    }

    private int hasData(ArrayList alVrdok, String skladista,
            String orgstruktura, String uis) {
      if (alwaysHasData()) {
        return 1;
      }
        int count = 0;
        String vrdok = "";
        QueryDataSet brojacic = null;
        String sqlQuery = "";

        for (int i = 0; i < alVrdok.size(); i++) {
            vrdok = (String) alVrdok.get(i);
            if (TD.isDocStdoku(vrdok)) {
                sqlQuery = "SELECT CAST(count(*) AS INT) as brojac from doku,stdoku "
                        + "WHERE " + rut.getDoc("doku", "stdoku")
                        + " AND doku.vrdok ='" + vrdok + "' "
                        + " AND doku.datdok " + getDatumDoSQL()
                        + " AND doku.statknj not in ('K') AND DOKU.CSKL IN "
                        + (TD.isDocOJ(vrdok) ? orgstruktura : skladista);
                try {
                    brojacic = Util.getNewQueryDataSet(sqlQuery, true);
                    count = count + brojacic.getInt("brojac");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (TD.isDocStdoki(vrdok)) {
                sqlQuery = "SELECT CAST(count(*) AS INT) as brojac from doki,stdoki "
                        + "WHERE " + rut.getDoc("doki", "stdoki")
                        + " AND doki.vrdok ='" + vrdok + "' "
                        + " AND doki.datdok " + getDatumDoSQL()
                        + " AND doki.statknj not in ('K') AND doki.cskl IN "
                        + (TD.isDocOJ(vrdok) ? orgstruktura : skladista);
                try {
                    brojacic = Util.getNewQueryDataSet(sqlQuery, true);
                    count = count + brojacic.getInt("brojac");
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } else if (TD.isDocStmeskla(vrdok)) {
                String dodatak = "(MESKLA.CSKLUL IN " + skladista + "OR "
                        + "MESKLA.CSKLIZ IN " + skladista + ")";

                if (vrdok.equalsIgnoreCase("MEI")
                        || (vrdok.equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("I") || uis
                                .equalsIgnoreCase("S")))) {
                    sqlQuery = "SELECT CAST(count(*) AS INT) as brojac from meskla,stmeskla "
                            + "WHERE meskla.csklul = stmeskla.csklul "
                            + "AND meskla.cskliz = stmeskla.cskliz "
                            + "AND meskla.vrdok = stmeskla.vrdok "
                            + "AND meskla.brdok = stmeskla.brdok "
                            + "AND meskla.god = stmeskla.god "
                            + "AND meskla.vrdok ='"
                            + vrdok
                            + "' "
                            + "AND meskla.datdok "
                            + getDatumDoSQL()
                            + " "
                            + "AND meskla.statknji not in ('K') AND "
                            + "MESKLA.CSKLIZ IN " + skladista;
                    try {
                        brojacic = Util.getNewQueryDataSet(sqlQuery, true);
                        count = count + brojacic.getInt("brojac");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
                if (vrdok.equalsIgnoreCase("MEU")
                        || (vrdok.equalsIgnoreCase("MES") && (uis
                                .equalsIgnoreCase("U") || uis
                                .equalsIgnoreCase("S")))) {
                    sqlQuery = "SELECT count(*) as brojac from meskla,stmeskla "
                            + "WHERE meskla.csklul = stmeskla.csklul "
                            + "AND meskla.cskliz = stmeskla.cskliz "
                            + "AND meskla.vrdok = stmeskla.vrdok "
                            + "AND meskla.brdok = stmeskla.brdok "
                            + "AND meskla.god = stmeskla.god "
                            + "AND meskla.vrdok ='"
                            + vrdok
                            + "' "
                            + "AND meskla.datdok "
                            + getDatumDoSQL()
                            + " "
                            + "AND meskla.statknju not in ('K') AND "
                            + "MESKLA.CSKLUL IN " + skladista;
                    try {
                        brojacic = Util.getNewQueryDataSet(sqlQuery, true);
                        count = count + brojacic.getInt("brojac");
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }
        if (brojacic != null) {
            brojacic.close();
            brojacic = null;
        }
        return count;
    }

    private boolean alwaysHasData() {
      return frmParam.getParam("robno","hasdata_knj","N","Da li provjera jel ima podataka u frmKnjRobno uvijek vraca true (D/N)")
         .equalsIgnoreCase("D");
    }

    public String arrayList2String(ArrayList al, boolean zagrade) {
        VarStr vs;
        if (zagrade) {
            vs = new VarStr("(");
        } else {
            vs = new VarStr("");
        }
        for (int i = 0; i < al.size(); i++) {
            vs.append("'").append(al.get(i)).append("',");
        }
        vs.chopRight(1);
        if (zagrade)
            vs.append(")");
        return vs.toString();
    }

    public StorageDataSet checkShemeKnjizenje() {
        StorageDataSet shemaMissing = getErrorSet();
        ArrayList alldocs = kAP.getArrayListVrdok();
        ArrayList alskladdok = new ArrayList();
        ArrayList alorgddok = new ArrayList();
        ArrayList almeulgddok = new ArrayList();
        ArrayList almeizgddok = new ArrayList();
        String lvrdok = "";
        for (int i = 0; i < alldocs.size(); i++) {
            lvrdok = (String) alldocs.get(i);
            if (TD.isDocSklad(lvrdok) && !TD.isDocMeskla(lvrdok)) {
                alskladdok.add(lvrdok);
            } else if (TD.isDocOJ(lvrdok) && !TD.isDocMeskla(lvrdok)) {
                alorgddok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MES")) {
                almeulgddok.add(lvrdok);
                almeizgddok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MEU")) {
                almeulgddok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MEI")) {
                almeizgddok.add(lvrdok);
            }
        }

        DataSet zagshem = Aus.q("SELECT DISTINCT vrdok,cskl,csklul FROM shkonta");
        globSheme = Shkonta.getDataModule().getScopedSet("VRDOK CSKL CSKLUL");
        globSheme.open();
        
        for (zagshem.first(); zagshem.inBounds(); zagshem.next()) {
          if (zagshem.getString("CSKL").indexOf("*") >= 0 || 
              zagshem.getString("CSKLUL").indexOf("*") >= 0) {
            globSheme.insertRow(false);
            dM.copyColumns(zagshem, globSheme, new String[] {"VRDOK", "CSKL", "CSKLUL"});
          }
        }
        
        String dokumentiskl = arrayList2String(alskladdok, false);
        String dokumentioj = arrayList2String(alorgddok, false);
        String dokumentimeul = arrayList2String(almeulgddok, false);
        String dokumentimeiz = arrayList2String(almeizgddok, false);
        String orgstruktura = arrayList2String(kAP.getArrayListOrgstr(), false);
        String skladista = arrayList2String(kAP.getArrayListSklad(), false);

        String sqlQueryUlSK = "SELECT MAX(CSKL) as CSKL,MAX(vrdok) as VRDOK from doku "
                + "WHERE doku.vrdok in ("
                + dokumentiskl
                + ") "
                + " AND doku.datdok "
                + getDatumDoSQL()
                + " AND doku.statknj not in ('K') AND CSKL IN ("
                + skladista
                + ") group by doku.cskl,doku.vrdok";

        String sqlQueryUlOJ = "SELECT MAX(CSKL) as CSKL,MAX(vrdok) as VRDOK from doku "
                + "WHERE doku.vrdok in ("
                + dokumentioj
                + ") "
                + " AND doku.datdok "
                + getDatumDoSQL()
                + " AND doku.statknj not in ('K') AND CSKL IN ("
                + orgstruktura
                + ") group by doku.cskl,doku.vrdok";

        String sqlQueryIZSK = "SELECT MAX(CSKL) as CSKL,MAX(vrdok) as VRDOK from doki "
                + "WHERE vrdok in ("
                + dokumentiskl
                + ") "
                + " AND datdok "
                + getDatumDoSQL()
                + " AND statknj not in ('K') AND CSKL IN ("
                + skladista + ") group by doki.cskl,doki.vrdok";

        String sqlQueryIZOJ = "SELECT MAX(CSKL) as CSKL,MAX(vrdok) as VRDOK from doki "
                + "WHERE vrdok in ("
                + dokumentioj
                + ") "
                + " AND datdok "
                + getDatumDoSQL()
                + " AND statknj not in ('K') AND CSKL IN ("
                + orgstruktura + ") group by doki.cskl,doki.vrdok";

        String sqlQuerymesklaUL = "SELECT MAX(CSKLUL) as CSKLUL,MAX(CSKLIZ) as CSKLIZ,"
                + "MAX(vrdok) as vrdok from meskla "
                + "WHERE vrdok in ("
                + dokumentimeul
                + ") "
                + " AND datdok "
                + getDatumDoSQL()
                + " AND statknju not in ('K')  AND CSKLUL IN ("
                + skladista
                + ") group by meskla.csklul,meskla.cskliz,meskla.vrdok";

        String sqlQuerymesklaIZ = "SELECT MAX(CSKLUL) as CSKLUL,MAX(CSKLIZ) as CSKLIZ,"
                + "MAX(vrdok) as vrdok from meskla "
                + "WHERE vrdok in ("
                + dokumentimeiz
                + ") "
                + " AND datdok "
                + getDatumDoSQL()
                + " AND statknji not in ('K')  AND CSKLIZ IN ("
                + skladista
                + ") group by meskla.csklul,meskla.cskliz,meskla.vrdok";
        QueryDataSet qdsset = null;

        try {
            if (!dokumentiskl.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(sqlQueryUlSK,
                        true);
                insertErrors(shemaMissing, qdsset, false);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQueryUlSK = " + sqlQueryUlSK);
        }
        try {
            if (!dokumentioj.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(sqlQueryUlOJ,
                        true);
                insertErrors(shemaMissing, qdsset, false);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQueryUlOJ=" + sqlQueryUlOJ);
        }

        try {
            if (!dokumentiskl.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(sqlQueryIZSK,
                        true);
                insertErrors(shemaMissing, qdsset, false);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQueryIZSK=" + sqlQueryIZSK);
        }

        try {
            if (!dokumentioj.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(sqlQueryIZOJ,
                        true);
                insertErrors(shemaMissing, qdsset, false);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQueryIZOJ=" + sqlQueryIZOJ);
        }

        try {
            if (!dokumentimeul.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(
                        sqlQuerymesklaUL, true);
                insertErrors(shemaMissing, qdsset, true);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQuerymesklaUL=" + sqlQuerymesklaUL);
        }
        try {
            if (!dokumentimeiz.equalsIgnoreCase("")) {
                qdsset = hr.restart.util.Util.getNewQueryDataSet(
                        sqlQuerymesklaIZ, true);
                insertErrors(shemaMissing, qdsset, true);
                qdsset.close();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.err.println("sqlQuerymesklaIZ=" + sqlQuerymesklaIZ);
        }

        return shemaMissing;
    }

    public void insertErrors(StorageDataSet errors, QueryDataSet data,
            boolean isMeskla) {
        dm.getShkonta().open();
        String[] filter;
        for (data.first(); data.inBounds(); data.next()) {
            if (isMeskla) {
                filter = new String[] { data.getString("CSKLUL"),
                        data.getString("CSKLIZ"), data.getString("VRDOK") };
            } else {
                filter = new String[] { data.getString("CSKL"),
                        data.getString("CSKL"), data.getString("VRDOK") };
            }

            if (!ld.raLocate(dm.getShkonta(), new String[] { "CSKLUL", "CSKL",
                    "VRDOK" }, filter)) {
              boolean globed = false;
              for (globSheme.first(); !globed && globSheme.inBounds(); globSheme.next()) {
                if (globSheme.getString("VRDOK").equals(filter[2]) && 
                    new raGlob(globSheme.getString("CSKLUL")).matches(filter[0]) &&
                    new raGlob(globSheme.getString("CSKL")).matches(filter[1]))
                  globed = true;
                  
              }
              if (!globed) {
                errors.insertRow(false);
                errors.setString("OPIS", filter[0] + "-" + filter[1] + "-"
                        + filter[2]);
              }
            }
        }
    }

    public StorageDataSet getErrorSet() {
        StorageDataSet shemaMissing = new StorageDataSet();
        Column opis = dm.getDoki().getColumn("OPIS").cloneColumn();
        opis.setWidth(1500);
        shemaMissing.setColumns(new Column[] { opis });
        shemaMissing.open();
        return shemaMissing;
    }

    public boolean checkDokument(ArrayList alVrdok, ArrayList alSkladista,
            ArrayList alOrgstr) {
        /*
         * String vrdok; String queryTest="SELECT max(brrac),sum(idob-irab) FROM
         * doku,stdoku "+ "WHERE doku.cskl = stdoku.cskl "+ "AND doku.vrdok =
         * stdoku.vrdok "+ "AND doku.god = stdoku.god "+ "AND doku.brdok =
         * stdoku.brdok ";
         * 
         * for (int i = 0; i < alVrdok.size(); i++) { vrdok = (String)
         * alVrdok.get(i); if (TD.isDocMeskla(vrdok)) { continue; } else { if
         * (vrdok.equalsIgnoreCase("PRK")){ queryTest = queryTest.concat("");
         *  } else if (vrdok.equalsIgnoreCase("KAL")){ queryTest =
         * queryTest.concat(""); }
         *  } }
         * 
         * QueryDataSet ulaz = hr.restart.util.Util.getNewQueryDataSet( "SELECT *
         * FROM SKSTAVKE WHERE BR");
         *  
         */

        return true;
    }

}