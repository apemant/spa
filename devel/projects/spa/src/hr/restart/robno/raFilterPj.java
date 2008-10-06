/****license*****************************************************************
**   file: raFilterPj.java
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
import com.borland.dx.sql.dataset.QueryDataSet;

public class raFilterPj {

  private allSelect aSS = new allSelect();
//  public raFilterPj() {}
  public QueryDataSet getClone(String uvijetS) {
    int uvijet = -1;
    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    try {
        uvijet = Integer.parseInt(uvijetS);
    } catch (Exception e) {
        uvijet = -1;
    }

    QueryDataSet za_vratiti =
        hr.restart.util.Util.getNewQueryDataSet(aSS.getQuery4rFP4getClone(uvijet),false);
    za_vratiti.close();
    za_vratiti.setColumns(dm.getPjpar().cloneColumns());
    za_vratiti.open();
/*
    za_vratiti.setQuery(new QueryDescriptor(dm.getDatabase1(),
                        aSS.getQuery4rFP4getClone(uvijet)));
    za_vratiti.open();
    za_vratiti.interactiveLocate(String.valueOf("1"),"PJ",com.borland.dx.dataset.Locate.FIRST,true);
*/
    return za_vratiti;
  }
}
