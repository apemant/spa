/****license*****************************************************************
**   file: raParam.java
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

import com.borland.dx.dataset.DataSet;

public class raParam {
  public static int ORGPL_STATUS = 1;     //  'A' - arhivirano, 'I' - inicirano
  public raParam() {
  }
  /**
   * Dohvat parametra iz DataSeta na odredjenoj poziciji
   * @param ds  DataSet
   * @param pos  pozicija
   * @return  parametar iz dataseta na poziciji
   */
  public static String getParam(DataSet ds, int pos) {
    return getParam(ds,"PARAMETRI",pos);
  }
  /**
   * Dohvat parametra iz kolone u DataSetu na odredjenoj poziciji
   * @param ds  DataSet
   * @param colName  kolona u datasetu
   * @param pos  pozicija
   * @return  parametar iz dataseta na poziciji
   */
  public static String getParam(DataSet ds, String colName, int pos) {
    return (ds.getString(colName)+"                    ").substring(pos-1,pos);
  }
  /**
   * Setiranje parametra u DataSetu na odredjenoj poziciji sa odredjenim stringom
   * @param ds  DataSet
   * @param pos  pozicija
   * @param item  string
   */
  public static void setParam(DataSet ds, int pos, String item) {
    setParam(ds,"PARAMETRI",pos,item);
  }
  /**
   * Setiranje parametra u kolonu u DataSetu na odredjenoj poziciji sa odredjenim stringom
   * @param ds  DataSet
   * @param colName  kolona u datasetu
   * @param pos  pozicija
   * @param item  string
   */
  public static void setParam(DataSet ds, String colName, int pos, String item) {
    String temp=(ds.getString(colName)+"                    ").substring(0,20);
    ds.setString(colName, temp.substring(0,pos-1)+item+temp.substring(pos, temp.length()));
  }
}