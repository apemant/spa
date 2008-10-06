/****license*****************************************************************
**   file: DataPostreceiverSaveChanges.java
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
/*
 * Created on Oct 12, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util.mail;

import hr.restart.util.raTransaction;

import java.util.ArrayList;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Saves changes of all received and loaded QueryDataSets in one or separate transactions 
 * @author andrej
 */
public class DataPostreceiverSaveChanges implements DataPostreceiver {
  private boolean inTrans;
  
  public DataPostreceiverSaveChanges() {
    inTrans = true;
  }
  
  public DataPostreceiverSaveChanges(boolean _inTrans) {
    inTrans = _inTrans;
  }
  
  public boolean run(DataReceiverLoadedData data) {
    try {
      //collect all sets in array
      ArrayList setslist = new ArrayList(); 
      String[] modules = data.getLoadedModules();
      for (int i = 0; i < modules.length; i++) {
        QueryDataSet[] qdss = data.getLoadedSetsByModule(modules[i]);
        for (int j = 0; j < qdss.length; j++) {
          setslist.add(qdss[j]);
        }
      }
      QueryDataSet[] sets = (QueryDataSet[])setslist.toArray(new QueryDataSet[setslist.size()]);
      if (inTrans) {
        return raTransaction.saveChangesInTransaction(sets); 
      } else {
        for (int i = 0; i < sets.length; i++) {
          sets[i].saveChanges();
        }
      }
      return true;
    } catch (Exception e) {
      e.printStackTrace();
      return false;
    }
  }

  /* (non-Javadoc)
   * @see hr.restart.util.mail.DataPostreceiver#getShortInfo()
   */
  public String getShortInfo() {
    return "snimanje promjena";
  }

}
