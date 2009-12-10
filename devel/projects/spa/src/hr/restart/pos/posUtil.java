/****license*****************************************************************
**   file: posUtil.java
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
package hr.restart.pos;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class posUtil {
  static hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();

  public posUtil() {
  }
  public static String findPrintString(int kupac) {
    String strSql;
    strSql="select * from kupci where ckupac='"+kupac+"'";
    System.out.println("String: "+strSql);
    vl.execSQL(strSql);
    vl.RezSet.open();
    return "Kupac: "+vl.RezSet.getInt("CKUPAC")+"<$newline$>"+
           "       "+vl.RezSet.getString("IME")+" "+vl.RezSet.getString("PREZIME")+"<$newline$>"+
           "       "+vl.RezSet.getString("OIB")+"<$newline$>";
  }

}