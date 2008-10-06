/****license*****************************************************************
**   file: repIzvProdajaArtikli.java
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
package hr.restart.robno;

import hr.restart.util.lookupData;
import hr.restart.util.reports.raReportData;

import java.sql.Timestamp;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repIzvProdajaArtikli implements raReportData {

    _Main main;

    repMemo rpm = repMemo.getrepMemo();

    frmIzvArt fia = frmIzvArt.getInstance();

    DataSet ds, ds2;

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

    raDateUtil rdu = raDateUtil.getraDateUtil();

    Timestamp datOd = fia.datOd;

    Timestamp datDo = fia.datDo;

    hr.restart.util.Valid val = hr.restart.util.Valid.getValid();

    String vrDok = fia.vrstaDok;

    String vrArt = fia.vrstaArt;

    public repIzvProdajaArtikli() {
        ds = fia.getQDS();
        ds2 = fia.getQDS3();
 //       sysoutTEST st = new sysoutTEST(false); //XDEBUG delete when no more needed
 //       st.prn(ds);
  //      System.out.println("\n\n"); //XDEBUG delete when no more needed
 //       st.prn(ds2);
    }

    public raReportData getRow(int i) {
        ds.goToRow(i);
        ds2.goToRow(i);
        return this;
    }

    public int getRowCount() {
        return ds.rowCount();
    }

    public void close() {
        ds = null;
    }

    public String getCSKL() {
      if (!fia.getCSKLART().equals("")) return fia.getCSKLART();
        return ds.getString("CSKL");
    }

    public String getSifra() {
        return Aut.getAut().getCARTdependable(ds);
    }

    public String getNazSklad() {
      if (!fia.getCSKLART().equals("")) {
        if (lookupData.getlookupData().raLocate(dm.getSklad(),
            new String[] { "CSKL" }, new String[] { fia.getCSKLART() }))
        return dm.getSklad().getString("NAZSKL");
      }
        String cskl = getCSKL();
        if (lookupData.getlookupData().raLocate(dm.getSklad(),
                new String[] { "CSKL" }, new String[] { cskl }) && fia.notKnjig())
            return dm.getSklad().getString("NAZSKL");
        else
            lookupData.getlookupData().raLocate(dm.getOrgstruktura(),
                    new String[] { "CORG" }, new String[] { cskl });
        return "Org. jedinica "+dm.getOrgstruktura().getString("NAZIV")+" sva skladišta";
    }

    public String getFirstLine() {
        return rpm.getFirstLine();
    }

    public String getSecondLine() {
        return rpm.getSecondLine();
    }

    public String getThirdLine() {
        return rpm.getThirdLine();
    }

    public String getNazArt() {
        return ds.getString("NAZART");
    }

    public double getKol() {
        return ds.getBigDecimal("KOL").doubleValue();
    }

    public double getNabVri() {
        return ds.getBigDecimal("INAB").doubleValue();
    }

    public double getProVri() {
        return ds.getBigDecimal("IZAD").doubleValue();
    }

    public double getUtrzak() {
        return ds.getBigDecimal("IPRODSP").doubleValue();
    }

    public String getDatumIsp() {
        return rdu.dataFormatter(val.getToday());
    }

    public String getVrDok() {
        if (vrDok.equals("GOT"))
            return "Gotovinski raèun";
        else if (vrDok.equals("ROT"))
            return "Raèun - otpremnica";
        else if (vrDok.equals("POD"))
            return "Povratnica - odobrenje";
        else if (vrDok.equals("RAC"))
            return "Bezgotovinski raèun";
        else if (vrDok.equals("GRN"))
            return "gotovinski raèun";
        else if (vrDok.equals("TER"))
            return "Tereæenje";
        else if (vrDok.equals("ODB"))
            return "Odobrenje";
        else {
            return "Svi dokumenti - " + fia.getVrsta();
        }
    }

    public String getVrArt() {
      if (!fia.getGrupaArtikala().equals("")){
        return fia.getGrupaArtikala();
      }
        QueryDataSet vrstaArt = dm.getVrart();
        vrstaArt.open();
        vrstaArt.first();
        do {
            if (vrArt.equals(vrstaArt.getString("CVRART")))
                return vrstaArt.getString("NAZVRART");
        } while (vrstaArt.next());
        return "Sve vrste artikala";
    }

    public String getPeriod() {
        return rdu.dataFormatter(datOd) + "-" + rdu.dataFormatter(datDo);
    }

    public double getIRaz() {
        return ds.getBigDecimal("IRAZ").doubleValue();
    }

    public double getUIRab() {
        return ds2.getBigDecimal("UIRAB").doubleValue();
    }

    public double getIProdBP() {
        return ds.getBigDecimal("IPRODBP").doubleValue();
    }

    public double getPor() {
        return ds2.getDouble("POR");
    }

    public double getInab() {
        return ds.getBigDecimal("INAB").doubleValue();
    }

    public double getIProdSP() {
        return ds.getBigDecimal("IPRODSP").doubleValue();
    }

    public double getUkupno() {
        return ds2.getDouble("UKUPNO");
    }

}