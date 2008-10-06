/****license*****************************************************************
**   file: offlineTransfer.java
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

import hr.restart.db.raReplicate;

import java.util.HashMap;

import com.borland.dx.sql.dataset.QueryDataSet;


/**
 * Ova klasica prebacuje podatke koji nastaju na izdvojenom radnom mjestu
 *
 * Za prijenos su slijedece tabele :
 *
 * doku
 * stdoku
 * doki
 * stdoki
 * meskla
 * stmeskla
 *
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class offlineTransfer {


  QueryDataSet qds_replInfo = null;

  public offlineTransfer() {


  }

  public void init_qds_replInfo(){
    qds_replInfo = hr.restart.util.Util.getNewQueryDataSet("select * from repli",true);
  }

  public void refillReplInfo(QueryDataSet ds,short rbr_url){

    if (qds_replInfo==null) {
      init_qds_replInfo();
    }

    for (ds.first();ds.inBounds();ds.next()) {
      qds_replInfo.insertRow(true);
      qds_replInfo.setString("IMETAB",ds.getTableName());
      qds_replInfo.setString("KEYTAB",raReplicate.getKeyTab(ds));
       qds_replInfo.setShort("RBR_URL",rbr_url);
       qds_replInfo.setString("REP_FLAG","P");
    }
  }

  public void something(){
    QueryDataSet qds_repldef = hr.restart.util.Util.getNewQueryDataSet("SELECT * FROM repldef",true);
    for (qds_repldef.first();qds_repldef.inBounds();qds_repldef.next()){
      QueryDataSet for_transfer = hr.restart.util.Util.getNewQueryDataSet("select * from "+
                                  qds_repldef.getString("IMETAB"));
      refillReplInfo(for_transfer,qds_repldef.getShort("RBR_URL"));
    }
  }



  public HashMap getCountAll(String cskl){

    HashMap hm = new HashMap();

    try {
      hm.put("DOKU",new Long(hr.restart.util.Util.getNewQueryDataSet(
                        "select count(*) as brojac from doku where cskl='"+
                         cskl+"'",true).getLong("BROJAC")));
      hm.put("STDOKU",new Long(hr.restart.util.Util.getNewQueryDataSet(
                        "select count(*) as brojac from stdoku where cskl='"+
                         cskl+"'",true).getLong("BROJAC")));
      hm.put("DOKI",new Long(hr.restart.util.Util.getNewQueryDataSet(
                        "select count(*) as brojac from doki where cskl='"+
                         cskl+"'",true).getLong("BROJAC")));
      hm.put("STDOKI",new Long(hr.restart.util.Util.getNewQueryDataSet(
                        "select count(*) as brojac from stdoki where cskl='"+
                         cskl+"'",true).getLong("BROJAC")));
      hm.put("MESKLA",new Long(hr.restart.util.Util.getNewQueryDataSet(
                        "select count(*) as brojac from meskla where csklul='"+
                         cskl+"' or cskliz ='"+cskl+"'",true).getLong("BROJAC")));
      hm.put("STMESKLA",new Long(hr.restart.util.Util.getNewQueryDataSet(
                         "select count(*) as brojac from stmeskla where csklul='"+
                         cskl+"' or cskliz ='"+cskl+"'",true).getLong("BROJAC")));
    }
    catch (Exception ex) {
      hm = null;
    }
    return hm;
  }


}