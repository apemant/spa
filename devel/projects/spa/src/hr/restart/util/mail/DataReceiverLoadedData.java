/****license*****************************************************************
**   file: DataReceiverLoadedData.java
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
 */
package hr.restart.util.mail;

import java.util.HashMap;

import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Proxy for handling and caching loaded data
 * @author andrej
 *
 */
public class DataReceiverLoadedData {
  DataReceiver drec;
  String[] loadedEmailKeys;
  String[] loadedModules;
  HashMap loadedSetsByModule = new HashMap();
  /**
   * 
   */
  public DataReceiverLoadedData(DataReceiver _drec) {
    drec = _drec;
  }
  /**
   * @return all subjects of received e-mails
   */
  public String[] getLoadedEmailKeys() {
    if (loadedEmailKeys == null) loadedEmailKeys = drec.getLoadedEmailKeys();
    return loadedEmailKeys;
  }
  /**
   * @return all modules (table names) in received e-mails
   */
  public String[] getLoadedModules() {
    if (loadedModules == null) loadedModules = drec.getLoadedModules();
    return loadedModules;
  }
  
  /**
   * @param module module (table name)
   * @return array of QueryDataSets received in e-mails
   */
  public QueryDataSet[] getLoadedSetsByModule(String module) {
    if (loadedSetsByModule.get(module) == null) {
      loadedSetsByModule.put(module, drec.getLoadedSetsByModule(module));
    }
    return (QueryDataSet[])loadedSetsByModule.get(module);
  }
  /**
   * You don't need this
   * @param emailKey
   * @param module
   * @return specified QueryDataSet in specified message
   */
  public QueryDataSet getLoadedSet(String emailKey, String module) {
    return drec.getLoadedSet(emailKey,module);
  }
}
