/****license*****************************************************************
**   file: LinkClass.java
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
package hr.restart.util;
import java.lang.reflect.Field;
import java.math.BigDecimal;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;

//  08-08-2002  (ab.f)  dodao jos jednu instancu link klase koja uvijek radi
//  sa double poljima.

public class LinkClass {

  static private LinkClass lc ;

  // 08-08-2002 (ab.f) link klasa koja radi sa double
  static private LinkClass lcd ;

  private boolean greska = false;
  private boolean buseBD = true;
  public boolean getUseBigDecimal(){
    return buseBD;
 }
  public void setUseBigDecimal(boolean value){
    buseBD =value;
  }

  public void setGreska(boolean greska1) {
    greska = greska1;
  }

  static public LinkClass getLinkClass(){
    if (lc == null)
      lc = new LinkClass();
    return lc;
  }

  // 08-08-2002  (ab.f)  getter double link klase
  static public LinkClass getLinkClassD(){
    if (lcd == null) {
      lcd = new LinkClass();
      lcd.setUseBigDecimal(false);
    }
    return lcd;
  }

  private LinkClass() {}

/**
 *
 * Standardna metoda za transferiranje BigDecimal-a iz dataset-a u istoimene BigDecimal fildove
 * u odgovaraju\u0107em Objektu i obrnuto
 * @param tip
 * @param ds
 * @param obj
 */
  private void TransferCommon(char tip,DataSet ds, Object obj){
    ds.enableDataSetEvents(false);
    if (!ds.isOpen()) {
      ds.open();
    }
    Column[] columns= ds.getColumns();
    for (int i=0;i< columns.length;i++){
      if (isFind(columns[i].getColumnName().toLowerCase(),obj)) {
        if (tip=='D') {
          if (columns[i].getSqlType()!=java.sql.Types.CHAR) {
//          if (columns[i].getSqlType()==java.sql.Types.NUMERIC) {
          setBDField(columns[i].getColumnName().toLowerCase(),
                     columns[i].getDataSet().getBigDecimal(columns[i].getColumnName()),
                     obj);
           }
           else if (columns[i].getSqlType()==java.sql.Types.CHAR) {
           setBDField(columns[i].getColumnName().toLowerCase(),
                     columns[i].getDataSet().getString(columns[i].getColumnName()),
                     obj);
           }

        }
        else if (tip=='C')  {
          if (columns[i].getSqlType()==java.sql.Types.NUMERIC ||
              columns[i].getDataType()==com.borland.dx.dataset.Variant.BIGDECIMAL) {
            ds.setBigDecimal(columns[i].getColumnName().toLowerCase(),
               getBDField(columns[i].getColumnName().toLowerCase(),obj));
          }
        }
        else {
          System.out.println("Greška kod odabira tipa prenosa");
        }
      }
    }
    ds.enableDataSetEvents(true);
  }

/**
 * Metoda za transferiranje DataSet-a u objekt *
 * @param ds
 * @param obj
 */
  public void TransferFromDB2Class(DataSet ds, Object obj){
        TransferCommon('D',ds,obj);
  }

/**
 * Metoda za transferiranje object-a u DataSet
 * @param ds
 * @param obj
 */

  public void TransferFromClass2DB(DataSet ds, Object obj){
        TransferCommon('C',ds,obj);
  }

/**
 * Metoda koja provjerava da li postoji Field u Objektu takvog imena
 * @param ime
 * @param obj
 * @return boolean
 */

  public boolean isFind(String ime,Object obj) {
//hr.restart.util.TimeTrack TM = new hr.restart.util.TimeTrack(false);
boolean za_vratiti;
//TM.Start("isFind ___ obj.getClass().getField("+ime.toLowerCase()+")");
    try {
      obj.getClass().getField(ime);
//      if (obj.getClass().getField(ime).getType()
//      return true;
za_vratiti = true;
      }
      catch (Exception e) {
        if (greska)
          System.out.println("NIsam našao "+ime);
//        return false;
za_vratiti = false;
        }
//TM.Stop();
return za_vratiti;
  }

/**
 * Metoda koja setira u odre\u0111enom objektu field (BigDecimal) koji ima navedeno ime
 * @param ime
 * @param iznos
 * @param obj
 */

  public void setBDField(String ime,BigDecimal iznos,Object obj){
    try {
      if (buseBD)
         obj.getClass().getField(ime.toLowerCase()).set(obj,iznos);
      else {
        obj.getClass().getField(ime.toLowerCase()).setDouble(obj,iznos.doubleValue());
      }

    } catch (Exception e) {
        if (greska)
          System.out.println("Greška setBDField "+e);
    }
  }

  public void setBDField(String ime,String iznos,Object obj){
    try {
      obj.getClass().getField(ime.toLowerCase()).set(obj,iznos);
    } catch (Exception e) {
        if (greska)
          System.out.println("Greška setBDField "+e);
    }
  }

/**
 * Metoda koja vra\u0107a vrijednost fielda u odre\u0111enom objektu
 * @param ime
 * @param obj
 * @return BigDecimal
 */

  public BigDecimal getBDField(String ime,Object obj) {

    BigDecimal tmp_bd = new BigDecimal(0.0);
//    tmp_bd=tmp_bd.setScale(4);
    tmp_bd=tmp_bd.setScale(2,BigDecimal.ROUND_HALF_UP);    // promjena možda \u0107e se nešto desiti na zaokruživanju
    try {
      if (buseBD)
        tmp_bd = (BigDecimal) (obj.getClass().getField(ime.toLowerCase()).get(obj));
      else
      tmp_bd = new BigDecimal(obj.getClass().getField(ime.toLowerCase()).getDouble(obj));
    } catch (Exception e) {
      if (greska)
        System.out.println("Greška getBDField "+e);
    }
    return tmp_bd;
  }

  public void printAll(Object obj) {

    Field[] fields = obj.getClass().getFields();
    String caption = "";
    String reska = "";
    System.out.println("==================================================================================== ");
    for (int i =0 ; i< fields.length;i++) {
      try{
        caption = caption + fields[i].getName() +" :" +fields[i].get(obj)+"- o -";
        } catch (Exception e) {
          if (greska)
            System.out.println("Greška printAll "+ e);
        }
    }
    System.out.println(caption);
    System.out.println("==================================================================================== ");
  }
}