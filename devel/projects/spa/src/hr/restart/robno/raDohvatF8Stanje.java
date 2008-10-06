/****license*****************************************************************
**   file: raDohvatF8Stanje.java
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
import hr.restart.util.JlrNavField;
import hr.restart.util.Valid;
import hr.restart.util.lookupData;
import hr.restart.util.lookupFrame;

import java.awt.Container;
import java.awt.Dialog;
import java.awt.Frame;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.MetaDataUpdate;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class raDohvatF8Stanje {

  public String keyF8Pressed(String cSkl, Container c,String polje,String value,String tCartSifparam)  {

    Valid vl = hr.restart.util.Valid.getValid();
    JlrNavField jrfCART = new JlrNavField();
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    Frame frame = null;
    Dialog dialog = null;
    lookupFrame lookUp = null;

    String[] result;

    String qStr = "select ARTIKLI.CART AS CART, ARTIKLI.CART1 AS CART1, ARTIKLI.BC AS BC, ARTIKLI.CGRART AS CGRART, ARTIKLI.NAZART AS NAZART,"+
                  " STANJE.KOL AS KOL, STANJE.KOLREZ AS KOLREZ from ARTIKLI, STANJE"+
                  " where ARTIKLI.CART = STANJE.CART and STANJE.CSKL = '"+cSkl+"' and stanje.kol>0";
    if (!value.equalsIgnoreCase("")) {
      qStr = polje=="CART"?qStr+" and "+polje + "="+value:qStr+" and "+ polje+ " like '"+value+"%'";
      qStr = qStr +" order by "+polje; /// PROXIMITY WARNING
    }

    vl.execSQL(qStr);
    vl.RezSet.setMetaDataUpdate(MetaDataUpdate.TABLENAME+MetaDataUpdate.PRECISION+MetaDataUpdate.SCALE+MetaDataUpdate.SEARCHABLE);
    vl.RezSet.setColumns(new Column[] {
        (Column) dm.getArtikli().getColumn("CART").clone(),
        (Column) dm.getArtikli().getColumn("CART1").clone(),
        (Column) dm.getArtikli().getColumn("BC").clone(),
        (Column) dm.getArtikli().getColumn("CGRART").clone(),
        (Column) dm.getArtikli().getColumn("NAZART").clone(),
        (Column) dm.getStanje().getColumn("KOL").clone(),
        (Column) dm.getStanje().getColumn("KOLREZ").clone()
    });

    vl.RezSet.open();
    vl.RezSet.setTableName("TOMOF8");
    lookupData LD = lookupData.getlookupData();

    if (tCartSifparam.equals("CART"))
      result = LD.lookUp(new java.awt.Frame(), vl.RezSet, new int[] {0, 4, 5, 6});
    else if (tCartSifparam.equals("CART1"))
      result = LD.lookUp(new java.awt.Frame(), vl.RezSet, new int[] {1, 4, 5, 6});
    else
      result = LD.lookUp(new java.awt.Frame(), vl.RezSet, new int[] {2, 4, 5, 6});


    if (result == null ) {
      return null;
    }

    return result[0];
  }
}