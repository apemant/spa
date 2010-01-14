/****license*****************************************************************
**   file: repDiskRSm.java
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
 * Created on Jan 21, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.pl;

import hr.restart.util.Util;

import java.sql.Timestamp;

/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class repDiskRSm extends repDiskRS {
  public repDiskRSm() {
    super(250);
    setPrint("Regos.rm0");
  }
  public StringBuffer insertSlog0() {
    StringBuffer b = super.insertSlog0();
    b.replace(223, 223 + "RM500".length(), "RM500" );
    b.replace(249, 250,"0");
    return b;
  }
  public StringBuffer insertSlog3() {
    StringBuffer b = super.insertSlog3();
    String mio1 = formatIznos(kumulDS.getBigDecimal("MIO1"),14);
    String mio2 = formatIznos(kumulDS.getBigDecimal("MIO2"),14);
    String neto = formatIznos(kumulDS.getBigDecimal("NETOPK"),14);
    Timestamp datumispl = frmRS.getInstance().getDatumIspl();
    String godmjispl = frmRS.getInstance().jpHead.isMjesecIsplate()?
        Util.getUtil().getYear(datumispl)+Util.getUtil().getMonth(datumispl):"000000";
    b.replace(174, 174+mio1.length(), mio1 );
    b.replace(189, 189+mio2.length(), mio2 );
    b.replace(204, 204+neto.length(), neto );
    b.replace(219, 219+godmjispl.length(), godmjispl );
    b.replace(225, (225+24), "                        ");
    b.replace(249, 250,"3");
    
    return b;
  }
  
  public StringBuffer getSlog5(int rbr) {
  	StringBuffer b = getNullSB();//super.getSlog5();
  	String jmbg = qds.getString("JMBG");
  	String imePrezime = getImePrez(qds.getString("CRADNIK"));
  	String copc = raIzvjestaji.convertCopcineToRS(qds.getString("COPCINE"));
  	String osnObr=qds.getString("RSOO")+qds.getString("RSB");
  	String razdoblje = getDani(qds.getShort("ODDANA")+"",qds.getShort("DODANA")+"");
  	
    String brutomj = formatIznos(qds.getBigDecimal("BRUTOMJ"),14);
    String bruto = formatIznos(qds.getBigDecimal("BRUTO"),14);
    String mio1 =formatIznos(qds.getBigDecimal("MIO1"),14);
    String mio2 =formatIznos(qds.getBigDecimal("MIO2"),14);
    String netopk = formatIznos(qds.getBigDecimal("NETOPK"),14);
    String srbr = vl.maskZeroInteger(new Integer(rbr),5);
	b.replace(0,4,srbr);
  	b.replace(5,17,jmbg);
  	b.replace(18,47,imePrezime);
  	b.replace(48,51,copc);
  	b.replace(52,54,osnObr);
  	b.replace(55,58,razdoblje);
  	b.replace(59,73,brutomj);
  	b.replace(74,88,bruto);
  	b.replace(89,103,mio1);
  	b.replace(104,118,mio2);
  	b.replace(119,133,netopk);
  	b.replace(249,249,"5");
  	return b;
  }
  
  public StringBuffer insertSlog7() {
  	StringBuffer b = super.insertSlog7();
  	b.replace(249,249,"7");
  	return b;
  }
  
  public StringBuffer insertSlog9() {
  	StringBuffer b = super.insertSlog9();
  	b.replace(33,38,"000001");
  	b.replace(249,249,"9");
  	return b;
  }
}
