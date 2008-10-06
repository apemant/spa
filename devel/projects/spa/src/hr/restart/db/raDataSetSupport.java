/****license*****************************************************************
**   file: raDataSetSupport.java
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

import com.borland.dx.dataset.AccessEvent;
import com.borland.dx.dataset.AccessListener;
import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataChangeEvent;
import com.borland.dx.dataset.DataChangeListener;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.NavigationListener;

public class raDataSetSupport implements NavigationListener, AccessListener, DataChangeListener {
  private DataSet dataSet;
  private String columnName;
  private raDataChangeAdapter dataChangeAdapter;
  private boolean eventsEnabled = true;
  private Object newValue;
  private Object oldValue;
  public raDataSetSupport() {
  }

  public void setDataSet(DataSet ds, raDataChangeAdapter dca) {
    if (ds == null) {
      if (dataSet != null) {
        dataSet.removeNavigationListener(this);
        dataSet.removeAccessListener(this);
        dataSet.removeDataChangeListener(this);
      }
      dataSet = null;
      dataChangeAdapter = null;
    } else {
      dataSet = ds;
      dataChangeAdapter = dca;
      dataSet.addNavigationListener(this);
      dataSet.addAccessListener(this);
      dataSet.addDataChangeListener(this);
    }
  }

  public DataSet getDataSet() {
    return dataSet;
  }

  public void setColumnName(String colNm) {
    columnName = colNm;
  }

  public String getColumnName() {
    return columnName;
  }

  private void fireDataChanged() {
    if (dataChangeAdapter!=null && dataSet!=null && columnName!=null && eventsEnabled) {
      Column dsCol = dataSet.hasColumn(columnName);
      if (dsCol==null) {
        return;
      }
      newValue = raVariant.getDataSetValue(dataSet,columnName);
      dataChangeAdapter.dataChanged(oldValue, newValue);
      oldValue = newValue;
    }
  }
//NavigationListener
  public void navigated(NavigationEvent e) {
    fireDataChanged();
  }
//AccessListener
  public void accessChange(AccessEvent ev) {
    if (ev.getID() == 2 && ev.getReason() == 8) {
      eventsEnabled = false;
    }
    if (ev.getID() == 1 && ev.getReason() == 2) {
      eventsEnabled = true;
    }
  }
//DataChangeListener
  public void dataChanged(DataChangeEvent event)  {
      if(!eventsEnabled)  return;
      int affectedRow = event.getRowAffected();
      boolean affectedOurRow = affectedRow == dataSet.getRow() || affectedRow == -1;
      if(affectedOurRow) fireDataChanged();
  }

  public void postRow(DataChangeEvent datachangeevent) throws Exception  {
  }
}

/*
    public void enableDataSetEvents(boolean flag)
    {
        if(!n)
            m();
        if(sb != null)
            if(!flag)                     getID getReason()
                a(sb, new AccessEvent(this, 2, 8));
            else
                a(sb, new AccessEvent(this, 1, 2));
    }
*/