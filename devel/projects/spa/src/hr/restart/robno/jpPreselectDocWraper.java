/****license*****************************************************************
**   file: jpPreselectDocWraper.java
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
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class jpPreselectDocWraper {

  private hr.restart.robno.jpPreselectDoc pressel = null;
  private jpSelectRevers preselrev = null;

  public void setPresel(Object obj) {
    if (obj instanceof hr.restart.robno.jpPreselectDoc) {
      pressel = (hr.restart.robno.jpPreselectDoc)obj;
      preselrev = null;
    }
    else if (obj instanceof jpSelectRevers) {
      pressel = null;
      preselrev = (jpSelectRevers) obj;
    }
  }

  public StorageDataSet getSelRow() {
    if (pressel != null)  return pressel.getSelRow();
    else if (preselrev != null) return  preselrev.getSelRow();
    return null;
  }

  public DataSet getSelDataSet() {
    if (pressel != null)  return   pressel.getSelDataSet();
    else if (preselrev != null)  return preselrev.getSelDataSet();
    return null;
  }

  public void copySelValues() {
    if (pressel != null)  pressel.copySelValues();
    else if (preselrev != null)  preselrev.copySelValues();
  }

  public String getCORG() {
    if (pressel != null)
      return pressel.jrfCSKL.getDataSet().getString(pressel.jrfCSKL.getColumnName());
    else if (preselrev != null)
      return preselrev.rpcskl.jrfCSKL.getDataSet().getString(preselrev.rpcskl.jrfCSKL.getColumnName());
    return null;
  }
}