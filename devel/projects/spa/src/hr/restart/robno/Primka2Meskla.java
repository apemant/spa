/****license*****************************************************************
**   file: Primka2Meskla.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.Sklad;
import hr.restart.util.Aus;
import hr.restart.util.lookupData;

import java.math.BigDecimal;

import com.borland.dx.sql.dataset.QueryDataSet;

public class Primka2Meskla {

    QueryDataSet stprimke = null;

    QueryDataSet stmeskla = null;

    QueryDataSet meskla = null;

    QueryDataSet stanjeul = null;

    QueryDataSet stanjeiz = null;

    String vrzalul = "";

    String vrzaliz = "";

    public void initALL(QueryDataSet stprimke, QueryDataSet stmeskla,
            QueryDataSet meskla) {

        this.stprimke = stprimke;
        this.stmeskla = stmeskla;
        this.meskla = meskla;

        QueryDataSet a = Sklad.getDataModule().getTempSet(
                Condition.where("CSKL", Condition.EQUAL, meskla
                        .getString("CSKLUL")));
        a.open();
        
        vrzalul = a.getString("VRZAL");
        
         a = Sklad.getDataModule().getTempSet(
                Condition.where("CSKL", Condition.EQUAL, meskla
                        .getString("CSKLIZ")));
         a.open();
         vrzaliz =a.getString("VRZAL");
         
         stanjeiz = hr.restart.util.Util.getNewQueryDataSet(
                 "select * from stanje WHERE exists "+
                 "(select * from stdoku where stanje.cart=stdoku.cart and "+
                 "stanje.god=stdoku.god and stanje.cskl='"+stprimke.getString("CSKL")+
                 "' and stdoku.god='"+stprimke.getString("GOD")+
                 "' and stdoku.vrdok='"+stprimke.getString("VRDOK")+
                 "' and stdoku.cskl='"+stprimke.getString("CSKL")+
                 "' and brdok="+stprimke.getInt("BRDOK")+")");
         
System.out.println("STANJEIZ "+stanjeiz.getQuery().getQueryString());         
         
         stanjeul = hr.restart.util.Util.getNewQueryDataSet(
                 "select * from stanje WHERE exists "+
                 "(select * from stdoku where stanje.cart=stdoku.cart and "+
                 "stanje.god=stdoku.god and stanje.cskl='"+meskla.getString("CSKLUL")+
                 "' and stdoku.god='"+stprimke.getString("GOD")+
                 "' and stdoku.vrdok='"+stprimke.getString("VRDOK")+
                 "' and stdoku.cskl='"+stprimke.getString("CSKL")+
                 "' and brdok="+stprimke.getInt("BRDOK")+")");

System.out.println("STANJEUL "+stanjeul.getQuery().getQueryString());         
         
         /*
        stanjeiz = Stanje.getDataModule().getTempSet(
                Condition.in("CART", stprimke).and(
                        Condition.whereAllEqual(new String[] { "GOD", "CSKL" },
                                stprimke)));
        stanjeiz.open();
        stanjeul = Stanje.getDataModule().getTempSet(
                Condition.in("CART", stprimke).and(
                        Condition.whereAllEqual(
                                new String[] { "GOD", "CSKL" }, new Object[]{
                                        meskla.getString("GOD"),meskla.getString("CSKLUL")})));

        System.out.println(
stanjeul.getQuery().getQueryString());        
        stanjeul.open();
        */
    }

    lookupData lD = lookupData.getlookupData();

    public void p2m() {
        if (stprimke == null)
            throw new RuntimeException("Stprimke == null ");
        if (stmeskla == null)
            throw new RuntimeException("Stmeskla == null ");
        if (meskla == null)
            throw new RuntimeException("meskla == null ");

        int rbr = 1;
        boolean isstanjefindulaz = false;
        boolean isstanjefindizlaz = false;

        for (stprimke.first(); stprimke.inBounds(); stprimke.next()) {
            isstanjefindulaz = false;
            isstanjefindizlaz = false;
            if (lD.raLocate(stanjeul, new String[] { "CSKL", "GOD", "CART" },
                    new String[] { meskla.getString("CSKLUL"),
                            meskla.getString("GOD"),
                            String.valueOf(stprimke.getInt("CART")) })) {
                isstanjefindulaz = true;
            }
            if (lD.raLocate(stanjeiz, new String[] { "CSKL", "GOD", "CART" },
                    new String[] { meskla.getString("CSKLIZ"),
                            meskla.getString("GOD"),
                            String.valueOf(stprimke.getInt("CART")) })) {
                isstanjefindizlaz = true;
            }

            stmeskla.insertRow(false);
            stmeskla.setString("VRDOK", meskla.getString("VRDOK"));
            stmeskla.setString("GOD", meskla.getString("GOD"));
            stmeskla.setString("CSKLUL", meskla.getString("CSKLUL"));
            stmeskla.setString("CSKLIZ", meskla.getString("CSKLIZ"));
            stmeskla.setInt("BRDOK", meskla.getInt("BRDOK"));
            stmeskla.setShort("RBR", (short) rbr);
            stmeskla.setInt("RBSID", rbr);

            stmeskla.setInt("CART", stprimke.getInt("CART"));
            stmeskla.setString("CART1", stprimke.getString("CART1"));
            stmeskla.setString("BC", stprimke.getString("BC"));
            stmeskla.setString("NAZART", stprimke.getString("NAZART"));
            stmeskla.setString("JM", stprimke.getString("JM"));
            stmeskla.setBigDecimal("KOL", stprimke.getBigDecimal("KOL"));
            stmeskla.setBigDecimal("NC", stprimke.getBigDecimal("NC"));
            stmeskla.setBigDecimal("VC", stprimke.getBigDecimal("VC"));
            stmeskla.setBigDecimal("MC", stprimke.getBigDecimal("MC"));
            stmeskla.setBigDecimal("ZC", stprimke.getBigDecimal("ZC"));

            if (isstanjefindulaz) {
                stmeskla.setBigDecimal("SKOL", stanjeul.getBigDecimal("KOL"));
                stmeskla.setBigDecimal("SNC", stanjeul.getBigDecimal("NC"));
                stmeskla.setBigDecimal("SVC", stanjeul.getBigDecimal("VC"));
                stmeskla.setBigDecimal("SMC", stanjeul.getBigDecimal("MC"));
            } else {
                stmeskla.setBigDecimal("SKOL", Aus.zero2);
                stmeskla.setBigDecimal("SNC", Aus.zero2);
                stmeskla.setBigDecimal("SVC", Aus.zero2);
                stmeskla.setBigDecimal("SMC", Aus.zero2);
            }

            stmeskla.setBigDecimal("INABIZ", stprimke.getBigDecimal("INAB"));
            stmeskla.setBigDecimal("INABUL", stprimke.getBigDecimal("INAB"));
            stmeskla.setBigDecimal("IMARIZ", stprimke.getBigDecimal("IMAR"));
            if (vrzalul.equalsIgnoreCase("N")) {
                stmeskla.setBigDecimal("IMARUL", Aus.zero2);
                stmeskla.setBigDecimal("IPORUL", Aus.zero2);
                stmeskla.setBigDecimal("ZCUL", stprimke.getBigDecimal("NC"));
                stmeskla.setBigDecimal("ZADRAZUL", stprimke
                        .getBigDecimal("INAB"));
            } else if (vrzalul.equalsIgnoreCase("V")) {
                stmeskla.setBigDecimal("IMARUL", stprimke.getBigDecimal("IBP")
                        .subtract(stprimke.getBigDecimal("INAB")));
                stmeskla.setBigDecimal("IPORUL", Aus.zero2);
                stmeskla.setBigDecimal("ZCUL", stprimke.getBigDecimal("VC"));
                stmeskla.setBigDecimal("ZADRAZUL", stprimke
                        .getBigDecimal("IBP"));
            } else if (vrzalul.equalsIgnoreCase("M")) {
                stmeskla.setBigDecimal("IMARUL", stprimke.getBigDecimal("IBP")
                        .subtract(stprimke.getBigDecimal("INAB")));
                stmeskla.setBigDecimal("IPORUL", stprimke.getBigDecimal("ISP")
                        .subtract(stprimke.getBigDecimal("IBP")));
                stmeskla.setBigDecimal("ZCUL", stprimke.getBigDecimal("MC"));
                stmeskla.setBigDecimal("ZADRAZUL", stprimke
                        .getBigDecimal("ISP"));
            }
            stmeskla.setBigDecimal("IPORIZ", stprimke.getBigDecimal("IPOR"));
            stmeskla.setBigDecimal("PMAR", stprimke.getBigDecimal("PMAR"));

            if (stmeskla.getBigDecimal("SKOL")
                    .compareTo(Aus.zero2) != 0) {
                if (vrzalul.equalsIgnoreCase("N")) {
                    stmeskla.setBigDecimal("PORAV", Aus.zero2);
                    stmeskla.setBigDecimal("DIOPORMAR", Aus.zero2);
                    stmeskla.setBigDecimal("DIOPORPOR", Aus.zero2);
                } else if (vrzalul.equalsIgnoreCase("V")) {
                    BigDecimal skolputasvc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("SVC"));
                    BigDecimal skolputavc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("VC"));
                    stmeskla.setBigDecimal("DIOPORMAR", skolputavc.subtract(
                            skolputasvc).setScale(2, BigDecimal.ROUND_HALF_UP));
                    stmeskla.setBigDecimal("DIOPORPOR", Aus.zero2);
                    stmeskla.setBigDecimal("PORAV", stmeskla
                            .getBigDecimal("DIOPORMAR"));
                } else if (vrzalul.equalsIgnoreCase("M")) {
                    BigDecimal skolputasvc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("SVC"));
                    BigDecimal skolputavc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("VC"));
                    BigDecimal skolputasmc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("SMC"));
                    BigDecimal skolputamc = stmeskla.getBigDecimal("SKOL")
                            .multiply(stmeskla.getBigDecimal("MC"));
                    stmeskla.setBigDecimal("DIOPORMAR", skolputavc.subtract(
                            skolputasvc).setScale(2, BigDecimal.ROUND_HALF_UP));
                    stmeskla.setBigDecimal("DIOPORPOR", skolputamc.subtract(
                            skolputasmc).setScale(2, BigDecimal.ROUND_HALF_UP));
                    stmeskla.setBigDecimal("PORAV", stmeskla.getBigDecimal(
                            "DIOPORMAR").add(
                            stmeskla.getBigDecimal("DIOPORPOR")));
                }
            } else {
                stmeskla.setBigDecimal("PORAV", Aus.zero2);
                stmeskla.setBigDecimal("DIOPORMAR", Aus.zero2);
                stmeskla.setBigDecimal("DIOPORPOR", Aus.zero2);
            }
            stmeskla.setBigDecimal("ZADRAZIZ", stprimke.getBigDecimal("IZAD"));
            stmeskla.setBigDecimal("KOLFLH", Aus.zero2);
            stmeskla.setBigDecimal("KOL1", stprimke.getBigDecimal("KOL1"));
            stmeskla.setBigDecimal("KOL2", stprimke.getBigDecimal("KOL2"));
            if (!isstanjefindulaz) {
                stanjeul.insertRow(false);
                stanjeul.setString("CSKL", stmeskla.getString("CSKLUL"));
                stanjeul.setString("GOD", stmeskla.getString("GOD"));
                stanjeul.setInt("CART", stmeskla.getInt("CART"));
                initStanje(stanjeul);
            }

            if (!isstanjefindizlaz) {
                stanjeiz.insertRow(false);
                stanjeiz.setString("CSKL", stmeskla.getString("CSKLIZ"));
                stanjeiz.setString("GOD", stmeskla.getString("GOD"));
                stanjeiz.setInt("CART", stmeskla.getInt("CART"));
                initStanje(stanjeiz);
            }
            updataStanje();
            definicijaSitkalaItkala();
            rbr ++;
        }
        
    }

    private void initStanje(QueryDataSet qds) {

        BigDecimal nula = Aus.zero2;
        qds.setBigDecimal("KOLPS", nula);
        qds.setBigDecimal("KOLUL", nula);
        qds.setBigDecimal("KOLIZ", nula);
        qds.setBigDecimal("KOLREZ", nula);
        qds.setBigDecimal("NABPS", nula);
        qds.setBigDecimal("MARPS", nula);
        qds.setBigDecimal("PORPS", nula);
        qds.setBigDecimal("VPS", nula);
        qds.setBigDecimal("NABUL", nula);
        qds.setBigDecimal("MARUL", nula);
        qds.setBigDecimal("PORUL", nula);
        qds.setBigDecimal("VUL", nula);
        qds.setBigDecimal("NABIZ", nula);
        qds.setBigDecimal("MARIZ", nula);
        qds.setBigDecimal("PORIZ", nula);
        qds.setBigDecimal("VIZ", nula);
        qds.setBigDecimal("KOL", nula);
        qds.setBigDecimal("ZC", nula);
        qds.setBigDecimal("VRI", nula);
        qds.setBigDecimal("NC", nula);
        qds.setBigDecimal("VC", nula);
        qds.setBigDecimal("MC", nula);
        qds.setString("SKAL", "");
        qds.setString("TKAL", "");
        qds.setString("ITKAL", "");
        qds.setString("SITKAL", "");
        qds.setShort("BSIZ", (short) 0);
        qds.setShort("SBSIZ", (short) 0);
        qds.setBigDecimal("KOLMAT", nula);
        qds.setBigDecimal("KOLSKLAD", nula);
        qds.setBigDecimal("KOLSKLADUL", nula);
        qds.setBigDecimal("KOLSKLADIZ", nula);
        qds.setBigDecimal("KOLSKLADPS", nula);
    }

    private void updataStanje() {
        // ulaz
        stanjeul.setBigDecimal("KOLUL", stanjeul.getBigDecimal("KOLUL").add(
                stmeskla.getBigDecimal("KOL")));
        stanjeul.setBigDecimal("NABUL", stanjeul.getBigDecimal("NABUL").add(
                stmeskla.getBigDecimal("INABUL")));
        stanjeul.setBigDecimal("MARUL", stanjeul.getBigDecimal("MARUL").add(
                stmeskla.getBigDecimal("IMARUL")));
        stanjeul.setBigDecimal("MARUL", stanjeul.getBigDecimal("MARUL").add(
                stmeskla.getBigDecimal("DIOPORMAR")));
        stanjeul.setBigDecimal("PORUL", stanjeul.getBigDecimal("PORUL").add(
                stmeskla.getBigDecimal("IPORUL")));
        stanjeul.setBigDecimal("PORUL", stanjeul.getBigDecimal("PORUL").add(
                stmeskla.getBigDecimal("DIOPORPOR")));
        stanjeul.setBigDecimal("VUL", stanjeul.getBigDecimal("VUL").add(
                stmeskla.getBigDecimal("ZADRAZUL")));
        stanjeul.setBigDecimal("VUL", stanjeul.getBigDecimal("VUL").add(
                stmeskla.getBigDecimal("PORAV")));

        stanjeul.setBigDecimal("KOL", stanjeul.getBigDecimal("KOLUL").subtract(
                stanjeul.getBigDecimal("KOLIZ")));
        stanjeul.setBigDecimal("VRI", stanjeul.getBigDecimal("VUL").subtract(
                stanjeul.getBigDecimal("VIZ")));
        stanjeul.setBigDecimal("NC", stmeskla.getBigDecimal("NC"));
        stanjeul.setBigDecimal("VC", stmeskla.getBigDecimal("VC"));
        stanjeul.setBigDecimal("MC", stmeskla.getBigDecimal("MC"));

        try {
            stanjeul
                    .setBigDecimal("ZC", stanjeul.getBigDecimal("VRI").divide(
                            stanjeul.getBigDecimal("KOL"), 2,
                            BigDecimal.ROUND_HALF_UP));
        } catch (Exception ex) {
        }

        stanjeul.setBigDecimal("KOLSKLADUL", stanjeul.getBigDecimal(
                "KOLSKLADUL").add(stmeskla.getBigDecimal("KOL")));
        stanjeul.setBigDecimal("KOLSKLAD", stanjeul.getBigDecimal("KOLSKLADUL")
                .subtract(stanjeul.getBigDecimal("KOLSKLADIZ")));

        // izlaz

        stanjeiz.setBigDecimal("KOLIZ", stanjeiz.getBigDecimal("KOLIZ").add(
                stmeskla.getBigDecimal("KOL")));
        stanjeiz.setBigDecimal("NABIZ", stanjeiz.getBigDecimal("NABIZ").add(
                stmeskla.getBigDecimal("INABIZ")));
        stanjeiz.setBigDecimal("MARIZ", stanjeiz.getBigDecimal("MARIZ").add(
                stmeskla.getBigDecimal("IMARIZ")));
        stanjeiz.setBigDecimal("PORIZ", stanjeiz.getBigDecimal("PORIZ").add(
                stmeskla.getBigDecimal("IPORIZ")));
        stanjeiz.setBigDecimal("VIZ", stanjeiz.getBigDecimal("VIZ").add(
                stmeskla.getBigDecimal("ZADRAZIZ")));
        stanjeiz.setBigDecimal("KOL", stanjeiz.getBigDecimal("KOLUL").subtract(
                stanjeiz.getBigDecimal("KOLIZ")));
        stanjeiz.setBigDecimal("VRI", stanjeiz.getBigDecimal("VUL").subtract(
                stanjeiz.getBigDecimal("VIZ")));

        stanjeiz.setBigDecimal("KOLSKLADIZ", stanjeiz.getBigDecimal(
                "KOLSKLADIZ").add(stmeskla.getBigDecimal("KOL")));
        stanjeiz.setBigDecimal("KOLSKLAD", stanjeiz.getBigDecimal("KOLSKLADUL")
                .add(stanjeiz.getBigDecimal("KOLSKLADIZ")));
    }

    private void definicijaSitkalaItkala() {

        /*
         * SKAL (stdoku ili stmeskla kod MES i MEU) = SKAL (stanje) SKAL
         * (stanje) = TKAL (stanje) TKAL (stanje) = 'broj novounesene
         * kalkulacije)
         */

        stmeskla.setString("SKAL", stanjeul.getString("SKAL"));
        stanjeul.setString("SKAL", stanjeul.getString("TKAL"));
        stanjeul.setString("TKAL", raControlDocs.getKey(stmeskla, "stmeskla"));

        /*
         * * ako je ITKAL = TKAL
         * 
         * BSIZ (stanje) = BSIZ (stanje) + 1 SITKAL (stdoku ili stmeskla kod MES
         * i MEU) = SITKAL (stanje) SBSIZ (stdoku ili stmeskla kod MES i MEU) =
         * SBSIZ (stanje)
         * 
         */
        if (stanjeiz.getString("ITKAL").equalsIgnoreCase(
                stanjeiz.getString("TKAL"))) {
            stmeskla.setString("ITKAL", stanjeiz.getString("TKAL"));
            stmeskla.setString("SITKAL", stanjeiz.getString("SITKAL"));
            stmeskla.setShort("SBSIZ", stanjeiz.getShort("SBSIZ"));
            stanjeiz.setShort("BSIZ", (short) (stanjeiz.getShort("BSIZ") + 1));
        } else {

            /*
             * ako je ITKAL != TKAL
             * 
             * SITKAL (stdoku ili stmeskla kod MES i MEU) = SITKAL (stanje)
             * SBSIZ (stdoku ili stmeskla kod MES i MEU) = SBSIZ (stanje) SITKAL
             * (stanje) = ITKAL (stanje) SBSIZ (stanje) = BSIZ (stanje) ITKAL
             * (stanje) = TKAL (stanje) BSIZ (stanje) = 0
             * 
             */
            stmeskla.setString("SITKAL", stanjeiz.getString("SITKAL"));
            stmeskla.setShort("SBSIZ", stanjeiz.getShort("SBSIZ"));
            stanjeiz.setString("SITKAL", stanjeiz.getString("ITKAL"));
            stanjeiz.setShort("SBSIZ", stanjeiz.getShort("BSIZ"));
            stanjeiz.setString("ITKAL", stanjeiz.getString("TKAL"));
            stanjeiz.setShort("BSIZ", (short) 0);
        }
    }

}
