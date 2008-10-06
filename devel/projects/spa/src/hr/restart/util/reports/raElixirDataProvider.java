/****license*****************************************************************
**   file: raElixirDataProvider.java
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
package hr.restart.util.reports;


/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: Rest-Art</p>
 * @author ab.f
 * @version 1.0
 */

public class raElixirDataProvider implements sg.com.elixir.reportwriter.datasource.IDataProvider {

  private static raElixirDataProvider inst = null;

  private Class dataClass;
  private raReportData data;

  public static raElixirDataProvider getInstance() {
    if (inst == null) new raElixirDataProvider();
    return inst;
  }

  private raElixirDataProvider() {
    inst = this;
  }

  public void setDataClass(String name) {
    try {
      dataClass = Class.forName(name);
      data = (raReportData) dataClass.newInstance();
    } catch (Exception e) {
      dataClass = null;
      e.printStackTrace();
    }
  }

  public boolean isDataOk() {
    return dataClass != null;
  }

  public raReportData getReportData() {
    return data;
  }

  public java.util.Enumeration getData() {
    return new java.util.Enumeration() {
      int indx=0;
      public Object nextElement() {
        return data.getRow(indx++);
      }
      public boolean hasMoreElements() {
        return indx < data.getRowCount();
      }
    };
  }

  public void close() {
    data.close();
  }
}
