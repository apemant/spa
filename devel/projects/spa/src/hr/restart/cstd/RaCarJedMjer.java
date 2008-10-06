/****license*****************************************************************
**   file: RaCarJedMjer.java
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
package hr.restart.cstd;

import hr.restart.swing.JraTextField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;

import com.borland.jbcl.layout.XYConstraints;

public class RaCarJedMjer extends raSifraNaziv {
    hr.restart.baza.dM dm;

    hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
	JraTextField oznaka = new JraTextField();

    public RaCarJedMjer() {
        try {
            jbInit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public void defaultAdd2Panel(int width,int heigh){
		
	}
	  public void defaultAdd2Panel2(){
	    xYLayout1.setWidth(555);
	    xYLayout1.setHeight(73);
	    jp.add(jlSifra, new XYConstraints(150, 15, 100, -1));
	    jp.add(new JLabel("Oznaka"), new XYConstraints(230, 15, 75, -1));
	    jp.add(jlNaziv, new XYConstraints(310, 15, 200, -1));
	    jp.add(jcbAktivan, new XYConstraints(440, 7, 100, -1));
	    jp.add(jlText, new XYConstraints(15, 38, -1, -1));
	    jp.add(jtfCSIFRA, new XYConstraints(150, 32, 75, -1));
	    jp.add(oznaka, new XYConstraints(230, 32, 75, -1));
	    jp.add(jtfNAZIV,  new XYConstraints(310, 32, 200, -1));
	  }
	
    
    private void jbInit() throws Exception {
		this.setVisibleCols(new int[] {0,1,2});
        dm = hr.restart.baza.dM.getDataModule();
		oznaka.setColumnName("CJMCAROZN");
		oznaka.setDataSet(dm.getJmcarina());
		
        this.setRaDataSet(dm.getJmcarina());
        this.setRaColumnSifra("CJMCAR");
        this.setRaColumnNaziv("NAZIV");
        this.setRaText("Jedinica mjere");
		defaultAdd2Panel2();        
    }

    public boolean DeleteCheck() {
        return true;
    }
}
