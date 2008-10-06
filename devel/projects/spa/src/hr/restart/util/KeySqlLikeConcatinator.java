/****license*****************************************************************
**   file: KeySqlLikeConcatinator.java
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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import com.borland.dx.dataset.DataSet;


public class KeySqlLikeConcatinator {

  public static String keydelimiter="-";


  public KeySqlLikeConcatinator() {
  }
  public HashMap getHM(String tablename,String columnname){
//    HashMap hm_column= new HashMap();
    HashMap hm_column_opis = new HashMap();
    return hm_column_opis;
  }

  /**
   *  Iz postojeceg sloga pronalazi primary key i iz istoga sklapa String koji
   * je konkatinacija vrijednosti primarnog kljuca delimitirana s -
   * @param ds DataSet iz kojeg cupamo cuurent row;
   * @return kljuc
   */

  public String getKeySqlLike(DataSet ds){
    return     getKeySqlLike(ds,new String[] {""});
//    throw new RuntimeException("Nije implementirana");
  }


  /**
   *
   * @param ds - dataset iz kojeg se cupaju polja
   * @param fields imena polja
   * @return key
   */

  public String getKeySqlLike(DataSet ds,String[] fields){
    throw new RuntimeException("Nije implementirana");
  }

  public ResultSet getPrimaryFields(String tablename){
    try {
      ResultSet rs = hr.restart.baza.dM.getDatabaseConnection().getMetaData().getPrimaryKeys(null,null,tablename);
      return rs;
    }
    catch (SQLException ex) {
      return null;
    }
  }


  /**
   *  Ova metoda za odreðenu tablicu (tablename) i polja u kojemu su navedena imena kolona u tablici
   * vraæa HashMapu u kojoj se nalazi kao kljuc ime polja a ako vrijednost drugu HashMapu u kojoj se nalazi
   * opis polja tj. kljuèevi       "COLUMN_SIZE" koji je Integer i "ORIENTATION" koji je Boolean a služi za
   * izradu kompozitnog kljuèa
   *
   * @param tablename   ime tablice
   * @param fieldnames  array imena kolona za koje želimo da nam vrati HashMapu
   * @return HashMap ili null ako su nastale neke greške ili su poslani krivi parametri
   */


  public HashMap getFieldsHMDescriptor(String tablename,String[] fieldnames){
    if (fieldnames == null ||  fieldnames.length==0){
      return null;
    }
    HashMap hmdescription = new HashMap();
    for (int i = 0; i<fieldnames.length;i++){
      HashMap hm_size_orient = getHMColumnSize_orijentacija(tablename,fieldnames[i]);
      if (hm_size_orient== null) return null;
      hmdescription.put(fieldnames[i],hm_size_orient);
    }
    return hmdescription;
  }

  public HashMap getHMColumnSize_orijentacija(String tablename,String field){
    HashMap hm = new HashMap();
    Integer columnsize ;
    Boolean desno ;
    try {
      java.sql.ResultSet comkys = hr.restart.baza.dM.getDatabaseConnection()
                                .getMetaData().getColumns(null,null,tablename,field);
      columnsize = new Integer(comkys.getInt("COLUMN_SIZE"));
      if (comkys.getShort("DATA_TYPE")==java.sql.Types.CHAR ||
          comkys.getShort("DATA_TYPE")==java.sql.Types.LONGVARCHAR ||
          comkys.getShort("DATA_TYPE")==java.sql.Types.VARCHAR){
        desno = Boolean.TRUE;
      } else {
        desno = Boolean.FALSE;
      }
      comkys.close();
      hm.put("COLUMN_SIZE",columnsize);
      hm.put("ORIENTATION",desno);
    } catch (SQLException ex) {
      ex.printStackTrace();
      return null;
    }
    return hm;
  }
}
