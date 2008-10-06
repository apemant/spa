/****license*****************************************************************
**   file: JraTextMultyKolField.java
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
package hr.restart.swing;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.math.BigDecimal;

abstract public class JraTextMultyKolField extends JraTextField implements PropertyChangeListener{
	
	protected boolean isMultyKolPopUpAllowed(){
	  	return true;
	}
	protected void setRaPopup() {
		if (getRaPopup() != null) {
			if (getRaPopup() instanceof JraMultyKolPopup)
				return;
		} else
        removeRaPopup();
		super.raPopup = new JraMultyKolPopup(this);
		
	}
	public void myfirePropertyKolChange(BigDecimal oldBD,BigDecimal newBD){
	  firePropertyChange("KOL",oldBD,newBD);	
	}
	
	abstract public void propertyChange(PropertyChangeEvent evt);
}
