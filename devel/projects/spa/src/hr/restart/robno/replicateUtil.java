/****license*****************************************************************
**   file: replicateUtil.java
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
import hr.restart.db.raConnectionFactory;

import java.util.Arrays;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class replicateUtil {

  QueryDataSet qds_replInfo = null;
  String imetab ;
  String[] keys ;
  private static String keyDelimiter = "-";

  public replicateUtil() {
    init_qds_replInfo();
  }

  public void init_qds_replInfo(){
    qds_replInfo = hr.restart.util.Util.getNewQueryDataSet("select * from replinfo",true);
  }

  public void refillReplInfo(QueryDataSet ds,String imet,short rbr_url){

    if (qds_replInfo==null) {
System.out.println("init radim !!! wrong");
      init_qds_replInfo();
    }
    for (ds.first();ds.inBounds();ds.next()) {
      qds_replInfo.insertRow(true);
      qds_replInfo.setString("IMETAB",imet);
      qds_replInfo.setString("KEYTAB",getKeyTab(ds));
      qds_replInfo.setShort("RBR_URL",rbr_url);
      qds_replInfo.setString("REP_FLAG","D");
    }
  }

  public void startFillReplInfo(){
System.out.println("Start .....");
    QueryDataSet qds_repldef = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM repldef",true);

    for (qds_repldef.first();qds_repldef.inBounds();qds_repldef.next()){

System.out.println("Sredjujem "+qds_repldef.getString("IMETAB"));
      QueryDataSet for_transfer = hr.restart.util.Util.getNewQueryDataSet("select * from "+
                                  qds_repldef.getString("IMETAB"));
      imetab = hr.restart.util.Valid.getTableName("select * from "+
                                  qds_repldef.getString("IMETAB"));
      keys = raConnectionFactory.getKeyColumns(for_transfer.getDatabase().getJdbcConnection(),imetab);
String tmpS="";
    for (int i = 0; i < keys.length; i++) {
      tmpS=tmpS+"-"+keys[i];

    }
System.out.println(tmpS);

      refillReplInfo(for_transfer,qds_repldef.getString("IMETAB"),qds_repldef.getShort("RBR_URL"));
    }
System.out.println("Snimanje");
    qds_replInfo.saveChanges();
System.out.println("Kraj");
System.exit(0);
  }

  public String getKeyTab(QueryDataSet ds) {
    String keytab = "";
    for (int i = 0; i < keys.length; i++) {
      Variant v = new Variant();
      ds.getVariant(keys[i],v);
      String keyVal;// = v.toString();
      Column c = ds.getColumn(keys[i]);
      if (c.getDataType() == Variant.STRING) {
        String _s = v.toString();
        int cl = c.getPrecision() - _s.length();
        String _sa = "";
        if (cl>0) {
          char[] chr = new char[cl];
          Arrays.fill(chr,' ');
          _sa = new String(chr);
        }
        keyVal = _s.concat(_sa);
      } else if (c.getDataType() == Variant.TIMESTAMP) {
        //to ne bude islo
        keyVal = new java.sql.Date(v.getTimestamp().getTime()).toString();
      } else {
        keyVal = v.toString();
      }
      keytab = keytab.concat(keyVal).concat(keyDelimiter);
    }
//System.out.println(keytab);
    return keytab;
  }

}