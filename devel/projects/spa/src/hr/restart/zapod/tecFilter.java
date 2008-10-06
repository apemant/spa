/****license*****************************************************************
**   file: tecFilter.java
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

import hr.restart.util.Util;
import hr.restart.util.Valid;

import java.sql.Timestamp;

public class tecFilter implements com.borland.dx.dataset.RowFilterListener {
  private Timestamp filterDate;
  public tecFilter() {
    filterDate = Util.getUtil().getLastSecondOfDay(Valid.getValid().getToday());
  }
  public tecFilter(java.util.Date date) {
    filterDate = Util.getUtil().getLastSecondOfDay(new Timestamp(date.getTime()));
  }
  public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse response) {
    if ((row.getTimestamp("DATVAL")).compareTo(filterDate) <= 0) {
      response.add();
    } else {
      response.ignore();
    }
  }
  public void setFilterDate(java.util.Date date) {
    filterDate = Util.getUtil().getLastSecondOfDay(new Timestamp(date.getTime()));
  }
}
