/****license*****************************************************************
**   file: RN.java
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
package hr.restart.baza;

import com.borland.dx.dataset.DataModule;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class RN extends KreirDrop implements DataModule {

  private static RN inst = new RN();

  QueryDataSet RNser = new raDataSet();
  QueryDataSet RNpro = new raDataSet();
  QueryDataSet RNp = new raDataSet();
  QueryDataSet RNo = new raDataSet();
  QueryDataSet RNz = new raDataSet();

  {
    createFilteredDataSet(RNser, "serpr = 'S'");
    createFilteredDataSet(RNpro, "serpr = 'P'");
    createFilteredDataSet(RNp, "status = 'P'");
    createFilteredDataSet(RNo, "status = 'O'");
    createFilteredDataSet(RNz, "status = 'Z'");
  }

  public static RN getDataModule() {
    return inst;
  }


  public com.borland.dx.sql.dataset.QueryDataSet getRnser() {
    return RNser;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnpro() {
    return RNpro;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnp() {
    return RNp;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRno() {
    return RNo;
  }

  public com.borland.dx.sql.dataset.QueryDataSet getRnz() {
    return RNz;
  }
  
  public boolean isAutoRefresh() {
    return true;
  }
}
