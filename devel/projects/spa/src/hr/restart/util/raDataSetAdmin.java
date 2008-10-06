/****license*****************************************************************
**   file: raDataSetAdmin.java
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
package hr.restart.util;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */
import java.lang.reflect.Method;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raDataSetAdmin {
  public static int MAXROWS = 500;
//static sysoutTEST ST= new sysoutTEST(false);
  protected raDataSetAdmin() {
  }

  public static void configureDataSets(Object datamodule) {
    Method[] metods = datamodule.getClass().getMethods();
//    configureStore();
    for (int i=0;i<metods.length;i++) {
      if ((metods[i].getReturnType() == QueryDataSet.class)) {
        try {
          String tableStoreName = metods[i].getName().substring(3);
          configureQueryDataSet((QueryDataSet)metods[i].invoke(datamodule,null),tableStoreName);
        } catch (Exception e) {
          System.out.println(metods[i].getName()+"--"+e);
        }
      }
    }
  }
/*
  static com.borland.datastore.DataStore dataStore;
  public static void configureStore() {
    dataStore = new com.borland.datastore.DataStore();
    dataStore.setFileName("tmpstore");
    java.io.File file = new java.io.File("tmpstore.jds");
    if (!file.exists()) dataStore.create();
  }
*/
  public static void configureQueryDataSet(QueryDataSet qds,String tableStoreName) {
    qds.close();
//    qds.getQuery().setExecuteOnOpen(false);
    qds.setMaxRows(MAXROWS);
//    qds.getQuery().setLoadOption(Load.ASYNCHRONOUS);
//    qds.getStorageDataSet().setStore(dataStore);
//    qds.getStorageDataSet().setStoreName(tableStoreName);
    qds.open();
  }
}