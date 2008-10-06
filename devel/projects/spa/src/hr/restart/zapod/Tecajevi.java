/****license*****************************************************************
**   file: Tecajevi.java
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
package hr.restart.zapod;



import hr.restart.baza.dM;
import hr.restart.util.lookupData;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.Variant;



/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class Tecajevi {

  public static final int KUPOVNI = 1;

  public static final int SREDNJI = 2;

  public static final int PRODAJNI = 3;

  public static int DEFAULT = SREDNJI;

  public static com.borland.dx.dataset.DataSet tecSearchSet;

  private static String lasSearchedOZNVAL = "";

  private static tecFilter tecajeviFilter = new tecFilter();

  public Tecajevi() {

  }

/**

 * Za zadani datum tipa java.util.Date, oznaku valute (brojcanu), i tip tecaja (Tecajevi.KUPOVNI,SREDNJI ili PRODAJNI)

 * vraca BigDecimal od unesenog tecaja ciji je DATVAL manji ili jednak od zadanog datuma.

 * Ako takav tecaj za tu valutu nije unesen vraca new BigDecimal(0)

 */

  public static java.math.BigDecimal getTecajSQL(java.sql.Date datec, String oznval, int tiptec) {



    if ((tiptec!=KUPOVNI) && (tiptec!=SREDNJI) && (tiptec!=PRODAJNI)) tiptec=SREDNJI;



    hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();

    Vl.execSQL(

      "SELECT * FROM TECAJEVI WHERE OZNVAL = '"+ oznval +"' AND DATVAL <= '" + datec.toString() + "' ORDER BY DATVAL DESCENDING;"

    );



    Vl.RezSet.open();

    if (Vl.RezSet.getRowCount() == 0) return new java.math.BigDecimal(0);

    Vl.RezSet.first();

    return getTecaj(Vl.RezSet,tiptec);

  }



  private static java.math.BigDecimal getTecaj(com.borland.dx.dataset.DataSet tecajset,int tiptec) {

    if (tiptec == KUPOVNI) return tecajset.getBigDecimal("TECKUP");

    if (tiptec == PRODAJNI) return tecajset.getBigDecimal("TECPROD");

    if (tiptec == SREDNJI) return tecajset.getBigDecimal("TECSRED");

    return tecajset.getBigDecimal("TECSRED");

  }



  public static java.math.BigDecimal getTecaj(java.util.Date datec, String oznval, int tiptec) {

    if (!lasSearchedOZNVAL.equals(oznval)||tecSearchSet==null) {

      hr.restart.util.Valid Vl = hr.restart.util.Valid.getValid();

      Vl.execSQL("SELECT * FROM TECAJEVI WHERE OZNVAL = '"+ oznval +"'");

      tecSearchSet = Vl.RezSet;

      com.borland.dx.dataset.SortDescriptor sd;

      tecSearchSet.setSort(new com.borland.dx.dataset.SortDescriptor(new String[] {"DATVAL"},true,true));

    }

    return getTecaj(datec,tiptec);

  }

  public static java.math.BigDecimal getTecaj(java.util.Date datec, int tiptec) {

    tecSearchSet.open();

    addFilter(tecSearchSet,datec);

    tecSearchSet.first();

    return getTecaj(tecSearchSet,tiptec);

  }

  private static void addFilter(DataSet ds,java.util.Date datec) {

    tecajeviFilter.setFilterDate(datec);

    RowFilterListener oldFilter = ds.getRowFilterListener();

    if (oldFilter instanceof tecFilter) {

      return;

    }

    try {

      if (oldFilter!=null) ds.removeRowFilterListener(oldFilter);

      ds.addRowFilterListener(tecajeviFilter);

    }

    catch (Exception ex) {

      ex.printStackTrace();

    }

    ds.refilter();

  }

/**

 * Konvertira vrijednost zadane JTextComponente u java.sql.Date i zove {@link #getTecaj(java.sql.Date,String,int)}

 * ako je komponenta ColumnAware (dbSwing) ocekuje se tip kolone Variant.DATE ili TIMESTAMP.

 * Ako komponenta to nije pokusava parsati datum po tekucem locale-u i ako mu to ne uspije koristi se danasnjim datumom.

 */

  public static java.math.BigDecimal getTecaj(javax.swing.text.JTextComponent jtDatec, String oznval, int tiptec) {

    java.sql.Date datec = null;

    if (jtDatec instanceof com.borland.dx.dataset.ColumnAware) {

      com.borland.dx.dataset.ColumnAware caDatec = (com.borland.dx.dataset.ColumnAware)jtDatec;

      try {

        if (caDatec.getDataSet().getColumn(caDatec.getColumnName()).getDataType() == Variant.DATE) {

          datec = caDatec.getDataSet().getDate(caDatec.getColumnName());

        }

        if (caDatec.getDataSet().getColumn(caDatec.getColumnName()).getDataType() == Variant.TIMESTAMP) {

          datec = new java.sql.Date(caDatec.getDataSet().getTimestamp(caDatec.getColumnName()).getTime());

        }

      } catch (Exception e) {

        System.out.println(e);

        datec = parseDate(jtDatec);

      }

    } else {

      datec = parseDate(jtDatec);

    }

    return getTecaj(datec, oznval, tiptec);

  }



  private static java.sql.Date parseDate(javax.swing.text.JTextComponent jtDatec) {

    java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat();

    try {

      return (java.sql.Date)sdf.parse(jtDatec.getText());

    } catch (Exception e) {

      System.out.println(e);

      return new java.sql.Date(System.currentTimeMillis());

    }

  }

/**

 * Varijacija na temu {@link #getTecaj(javax.swing.text.JTextComponent,String,int)} gdje je String = jtOznval.getText()

 */

  public static java.math.BigDecimal getTecaj(javax.swing.text.JTextComponent jtDatec, javax.swing.text.JTextComponent jtOznval, int tiptec) {

    return getTecaj(jtDatec,jtOznval.getText(),tiptec);

  }

/**

 * Varijacija na temu {@link #getTecaj(java.util.Date,String,int)} gdje je oznval = jtOznval.getText()

 */

  public static java.math.BigDecimal getTecaj(java.util.Date datec, javax.swing.text.JTextComponent jtOznval, int tiptec) {

    return getTecaj(datec,jtOznval.getText(),tiptec);

  }

/**

 * Funkcija koja zove {@link #getTecaj(java.sql.Date,javax.swing.text.JTextComponent,int)}

 * sa tipom tecaja = DEFAULT = SREDNJI

 */

  public static java.math.BigDecimal getTecaj(java.util.Date datec, javax.swing.text.JTextComponent jtOznval) {

    return getTecaj(datec,jtOznval.getText(),DEFAULT);

  }

/**

 * Funkcija koja zove {@link #getTecaj(java.util.Date,String,int)}

 * sa tipom tecaja = DEFAULT = SREDNJI

 */

  public static java.math.BigDecimal getTecaj(java.util.Date datec, String oznval) {

    return getTecaj(datec,oznval,DEFAULT);

  }

  public static java.math.BigDecimal getTecaj1(java.util.Date datec, String oznval) {

    return getTecaj(datec,oznval,DEFAULT).divide(getJedVal(oznval),java.math.BigDecimal.ROUND_HALF_UP);

  }
  public static java.math.BigDecimal getJedVal(String val) {
    hr.restart.baza.dM.getDataModule().getValute().open();
    boolean b = hr.restart.util.lookupData.getlookupData().raLocate(hr.restart.baza.dM.getDataModule().getValute(),"OZNVAL",val);
    if (b)
      return new java.math.BigDecimal((double)hr.restart.baza.dM.getDataModule().getValute().getInt("JEDVAL"));
    else
      return new java.math.BigDecimal(1);
  }

/**

 * Funkcija koja zove {@link #getTecaj(javax.swing.text.JTextComponent,javax.swing.text.JTextComponent,int)}

 * sa tipom tecaja = DEFAULT = SREDNJI

 */

  public static java.math.BigDecimal getTecaj(javax.swing.text.JTextComponent jtDatec, javax.swing.text.JTextComponent jtOznval) {

    return getTecaj(jtDatec,jtOznval.getText(),DEFAULT);

  }

/**

 * Funkcija koja zove {@link #getTecaj(javax.swing.text.JTextComponent,String,int)}

 * sa tipom tecaja = DEFAULT = SREDNJI

 */

  public static java.math.BigDecimal getTecaj(javax.swing.text.JTextComponent jtDatec, String oznval) {

    return getTecaj(jtDatec,oznval,DEFAULT);

  }

/**

 * Vraca oznaku domicilne valute OZNVAL where VALUTE.STRVAL = 'N') npr. KN

 */

  public static String getDomOZNVAL() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "STRVAL", "N");
    return dM.getDataModule().getValute().getString("OZNVAL");
  }

/**

 * Vraca oznaku nize jedinice (sitnisa) domicilne valute

 */

  public static String getDomNIZJED() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "STRVAL", "N");
    return dM.getDataModule().getValute().getString("NIZJED");
  }

/**

 * Vraca sifru domicilne valute CVAL where VALUTE.STRVAL = 'N') npr. 100

 */

  public static short getDomCVAL() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "STRVAL", "N");
    return dM.getDataModule().getValute().getShort("CVAL");
  }

/**

 * Vraca oznaku referentne valute OZNVAL where VALUTE.REFVAL = 'D') npr. DEM

 */

  public static String getRefOZNVAL() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "REFVAL", "D");
    return dM.getDataModule().getValute().getString("OZNVAL");
  }

/**

 * Vraca oznaku nize jedinice (sitnisa) referentne valute

 */

  public static String getRefNIZJED() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "REFVAL", "D");
    return dM.getDataModule().getValute().getString("NIZJED");
  }

/**

 * Vraca sifru referentne valute CVAL where VALUTE.REFVAL = 'D') npr. 100

 */

  public static short getRefCVAL() {
    lookupData.getlookupData().raLocate(dM.getDataModule().getValute(), "REFVAL", "D");
    return dM.getDataModule().getValute().getShort("CVAL");
  }



  /*static com.borland.dx.dataset.DataSetView getDomValSet() {

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

    dm.getValute().open();

    com.borland.dx.dataset.DataSetView domValSet = dm.getValute().cloneDataSetView();

    domValSet.interactiveLocate("N","STRVAL",com.borland.dx.dataset.Locate.FIRST,false);

    return domValSet;

  }



  static com.borland.dx.dataset.DataSetView getRefValSet() {

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();

    dm.getValute().open();

    com.borland.dx.dataset.DataSetView refValSet = dm.getValute().cloneDataSetView();

    refValSet.interactiveLocate("D","REFVAL",com.borland.dx.dataset.Locate.FIRST,false);

    return refValSet;

  }*/
}