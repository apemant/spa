/****license*****************************************************************
**   file: rdZapodUtil.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.dM;
import hr.restart.util.Aus;

import com.borland.dx.sql.dataset.QueryDataSet;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2001</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class rdZapodUtil {
  private static rdZapodUtil myUtil;
  dM dm = dM.getDataModule();

  public static rdZapodUtil getrdZPUtil()
  {
    if(myUtil == null)
      myUtil = new rdZapodUtil();
    return myUtil;
  }

  public rdZapodUtil() {
  }

  public QueryDataSet getZiroParDS(int cpar)
  {
    return hr.restart.baza.Ziropar.getDataModule().getFilteredDataSet(Condition.equal("CPAR",cpar));
    /*
    String qStr = "select * from ziropar where cpar="+cpar;
    QueryDataSet qds = new QueryDataSet();

    qds.setColumns(new Column[]{
    (Column) dm.getZiropar().getColumn("CPAR").clone(),
    (Column) dm.getZiropar().getColumn("ZIRO").clone(),
    (Column) dm.getZiropar().getColumn("DEV").clone(),
    (Column) dm.getZiropar().getColumn("OZNVAL").clone()
    });

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
     */
  }

  public QueryDataSet getPjDS(int cpar)
  {
    return hr.restart.baza.Pjpar.getDataModule().getFilteredDataSet(Condition.equal("CPAR",cpar));
    /*
    String qStr = "select * from pjpar where cpar="+cpar;
    QueryDataSet qds = new QueryDataSet();

    qds.setColumns(new Column[]{
    (Column) dm.getPjpar().getColumn("LOKK").clone(),
    (Column) dm.getPjpar().getColumn("AKTIV").clone(),
    (Column) dm.getPjpar().getColumn("CPAR").clone(),
    (Column) dm.getPjpar().getColumn("PJ").clone(),
    (Column) dm.getPjpar().getColumn("NAZPJ").clone(),
    (Column) dm.getPjpar().getColumn("MJPJ").clone(),
    (Column) dm.getPjpar().getColumn("ADRPJ").clone(),
    (Column) dm.getPjpar().getColumn("PBRPJ").clone(),
    (Column) dm.getPjpar().getColumn("TELPJ").clone(),
    (Column) dm.getPjpar().getColumn("TELFAXPJ").clone(),
    (Column) dm.getPjpar().getColumn("KOPJ").clone(),
    (Column) dm.getPjpar().getColumn("REGIJA").clone(),
    (Column) dm.getPjpar().getColumn("CAGENT").clone()
    });

    qds.getColumn("CPAR").setRowId(true);

    qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));
    return qds;
   */
  }

  public int getPJ(int cpar)
  {
    QueryDataSet qds = new QueryDataSet();
    String qStr = "select max(pj) as PJ from pjpar where cpar = "+cpar;
    Aus.refilter(qds, qStr);    
    if(qds.getRowCount()>0)
      return qds.getInt("PJ");
    return 0;
  }
}