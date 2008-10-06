/****license*****************************************************************
**   file: raReplicatePrepare.java
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

import hr.restart.util.Util;
import hr.restart.util.raProcess;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raReplicatePrepare {


  public raReplicatePrepare() {
  }
  public void prepareReplInf(String batch_index){
    prepareReplInf(batch_index,true);
  }

  public void prepareReplInf(String batch_index,boolean swing){

    if (swing) {
      raProcess.setMessage("Provjera prenesenosti",true);
    }
    QueryDataSet repldef = Util.getNewQueryDataSet(
    "SELECT * FROM repldef where repldef.BATCH_INDEX = '"
          +batch_index+"' ORDER BY SLIJED, NACINREP");
    for ( repldef.first();repldef.inBounds();repldef.next()) {
      if (swing) {
      raProcess.setMessage("Priprema "+repldef.getString("IMETAB"),true);
      }
      insertIntoRepDef(getNew4Transfere(repldef.getString("IMETAB")),
      repldef.getString("IMETAB"),repldef.getShort("RBR_URL"));
    }
  }

  private QueryDataSet getNew4Transfere(String name_table) {
    String query = ("SELECT * FROM "+name_table+" WHERE "+raReplicate.getKeyTab(name_table)+
                    " not in (select keytab from replinfo)");
System.out.println(query);
    return  Util.getNewQueryDataSet(query,true);
  }

  private void insertIntoRepDef(QueryDataSet dss,String imetab,short rbr_url) {
    for ( dss.first();dss.inBounds();dss.next()) {
      hr.restart.util.Valid.getValid().runSQL ("insert into replinfo (imetab ,keytab,rbr_url,rep_flag) values ('"+imetab+"','"+
      raReplicate.getKeyTab(dss)+"',"+rbr_url+",'N')");
    }
  }
}