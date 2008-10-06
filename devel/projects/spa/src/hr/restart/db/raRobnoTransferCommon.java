/****license*****************************************************************
**   file: raRobnoTransferCommon.java
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
package hr.restart.db;

public class raRobnoTransferCommon extends raRobnoTransferAbstract {

  public String sqlUpit(String qkeytab,String table_name){
    if (table_name.equalsIgnoreCase("doki") ||
        table_name.equalsIgnoreCase("doku") ||
        table_name.equalsIgnoreCase("rate") ||
        table_name.equalsIgnoreCase("meskla")) {
      return "SELECT datdok,("+qkeytab+") as slog FROM "+table_name+" where ("+qkeytab+") not in "+
          "(select replinfo.keytab from replinfo where imetab='"+table_name+"')";
      } else if (table_name.equalsIgnoreCase("stdoki") ||
                 table_name.equalsIgnoreCase("stdoku")) {
        String zaglavlje=table_name.substring(2,table_name.length());
        return "SELECT "+zaglavlje+".datdok,("+qkeytab+") as slog from "+zaglavlje+","+table_name+" where "+
            hr.restart.robno.Util.getUtil().getDoc(zaglavlje,table_name)+" and ("+qkeytab+") "+
            " not in (select replinfo.keytab from replinfo where imetab='"+table_name+"')";
    } else if (table_name.equalsIgnoreCase("stmeskla")) {
      return "SELECT meskla.datdok,("+qkeytab+") as slog from meskla,"+table_name+" where "+
          hr.restart.robno.Util.getUtil().getDocMes("meskla","stmeskla")+" and ("+qkeytab+") "+
          " not in (select replinfo.keytab from replinfo where imetab='"+table_name+"')";
    } else {
      return "select ("+qkeytab+") as slog from "+table_name+" where ("+qkeytab+")   not in "+
          "(select replinfo.keytab from replinfo where imetab='"+table_name+"')";
    }
  }
}