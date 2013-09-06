/****license*****************************************************************
**   file: repDisk.java
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

import hr.restart.sisfun.frmParam;
import hr.restart.util.reports.mxRM;
import hr.restart.util.reports.mxReport;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

public class repDisk extends mxReport {

  int sirina;

  public QueryDataSet diskzap = new QueryDataSet();
  public repDisk() {
    this(250);
  }
  public repDisk(int sirina) {
    this.sirina = sirina;
    init();
  }
  public void init() {
    diskzap.addColumn(new Column("REDAK","Redak",Variant.STRING));
    setDataSet(diskzap);
    String[] detail = new String[] {"<#REDAK|"+sirina+"|left#>"};
    setDetail(detail);
  }
  public void fill() throws Exception
  {

  }
  public void setPrint(String filename)
  {
    setFilename(filename);
    mxRM diskrm = mxRM.getDefaultMxRM();
    diskrm.setPrintCommand(frmParam.getParam("zapod","copycmd","cp # a:\\","komanda za kopiranje file-a na medij za repDisk",true)+filename);//aj da to zasoftkodiramo
    setRM(diskrm);
  }
  public void setPrinter(hr.restart.util.reports.mxPrinter newPrinter) {
    newPrinter.setReset(""); // uvijek salje reset printera, a ovo nije printer nego file za disketu
    super.setPrinter(newPrinter);
  }

}