/****license*****************************************************************
**   file: repOTPss.java
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

import java.math.BigDecimal;

import hr.restart.util.lookupData;

import com.borland.dx.sql.dataset.QueryDataSet;


public class repOTPss extends repOTP {

  public repOTPss() {
    stranaShifra();
  }
  
  public String getSSIF() {
    if (lookupData.getlookupData().raLocate(setSifri, "CART", getSHIFRA()))
      return setSifri.getString("CCPAR");
    return "";
  }
  
  public BigDecimal getCJC() {
    if (raDOS.dosfc && ds.getString("VRDOK").equals("DOS")) return ds.getBigDecimal("FC");
    
    QueryDataSet tmpCjenik = allStanje.getallStanje().getCijenik(
        ds.getString("VRDOK"), ds.getString("CSKL"), ds.getInt("CPAR"), ds.getInt("CART"));
    if (tmpCjenik != null && tmpCjenik.rowCount() > 0)
      return tmpCjenik.getBigDecimal("VC");
    if (lookupData.getlookupData().raLocate(dm.getArtikli(), "CART", getSHIFRA()))
      return dm.getArtikli().getBigDecimal("VC");
    return null;
  }
  
  protected void nacPl() {
    super.nacPl();
    stranaShifra();
  }
  
  private static QueryDataSet setSifri = null;
  private void stranaShifra(){
    if (lastDok == null) return;
    String upit = "SELECT cart, ccpar FROM VTCartPart where cpar = "+getCPAR_OTP();
    setSifri = ut.getNewQueryDataSet(upit);
  }
}
