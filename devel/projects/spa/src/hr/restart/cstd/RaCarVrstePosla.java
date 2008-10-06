/****license*****************************************************************
**   file: RaCarVrstePosla.java
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

import hr.restart.baza.Vrstaposla;
import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.util.raSifraNaziv;

import javax.swing.JLabel;

import com.borland.jbcl.layout.XYConstraints;

public class RaCarVrstePosla extends raSifraNaziv {
	hr.restart.baza.dM dm;

	hr.restart.robno.Util util = hr.restart.robno.Util.getUtil();
	JlrNavField jrfCPRIPAD = new JlrNavField();
	JlrNavField jrfNAZPRIPAD = new JlrNavField();
	JraButton jbCPRIPAD = new JraButton();
	
	

	public RaCarVrstePosla() {
		try {
			jbInit();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void jbInit() throws Exception {
		dm = hr.restart.baza.dM.getDataModule();
		this.setRaDataSet(dm.getVrstaposla());
		this.setRaColumnSifra("CVRPOS");
		this.setRaColumnNaziv("NAZIV");
		this.setRaText("Vrsta posla");
		jrfCPRIPAD.setColumnName("CVRPOSPRIP");
		jrfCPRIPAD.setColNames(new String[] { "NAZIV" });
		jrfCPRIPAD.setVisCols(new int[] { 0, 1, 2 });
		jrfCPRIPAD.setTextFields(new javax.swing.text.JTextComponent[] { jrfNAZPRIPAD });
		jrfCPRIPAD.setRaDataSet(Vrstaposla.getDataModule().getTempSet(""));
		jrfCPRIPAD.setNavColumnName("CVRPOS");
		jrfCPRIPAD.setDataSet(dm.getVrstaposla());
		jrfNAZPRIPAD.setColumnName("NAZIV");
		jrfNAZPRIPAD.setSearchMode(1);
		jrfNAZPRIPAD.setNavProperties(jrfCPRIPAD);
		jrfCPRIPAD.setNavButton(jbCPRIPAD);
		defaultAdd2Panel2();
		
	}
	public void defaultAdd2Panel(int width,int heigh){
	    
	}
	  public void defaultAdd2Panel2(){
	      xYLayout1.setWidth(555);
	      xYLayout1.setHeight(100);
	      jp.add(jlSifra, new XYConstraints(150, 15, 100, -1));
	      jp.add(jlNaziv, new XYConstraints(255, 15, 200, -1));
	      jp.add(jcbAktivan, new XYConstraints(440, 7, 100, -1));
	      jp.add(jlText, new XYConstraints(15, 38, -1, -1));
	      jp.add(jtfCSIFRA, new XYConstraints(150, 32, 100, -1));
	      jp.add(jtfNAZIV,  new XYConstraints(255, 32, 285, -1));
	      jp.add(new JLabel("Pripadnost"), new XYConstraints(15, 60, -1, -1));
	      jp.add(jrfCPRIPAD, new XYConstraints(150, 60, 100, -1));
	      jp.add(jrfNAZPRIPAD,  new XYConstraints(255, 60, 259, -1));
	      jp.add(jbCPRIPAD,  new XYConstraints(519, 60, 21, 21));
	    }

	public boolean DeleteCheck() {
		return true;
	}
	  public void SetFokus2(char mode) {
	      if (mode=='N'){
	          jrfCPRIPAD.forceFocLost();
	      }
	  }

}
