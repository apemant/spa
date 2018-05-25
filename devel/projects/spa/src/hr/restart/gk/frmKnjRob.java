/****license*****************************************************************
**   file: frmKnjRob.java
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
package hr.restart.gk;

import hr.restart.baza.Condition;
import hr.restart.robno.TypeDoc;
import hr.restart.sisfun.frmSkripte;
import hr.restart.util.Aus;
import hr.restart.util.VarStr;
import hr.restart.util.raGlob;
import hr.restart.util.raProcess;
import hr.restart.util.sysoutTEST;

import java.util.ArrayList;

import javax.swing.JOptionPane;

import bsh.EvalError;
import bsh.Interpreter;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

public class frmKnjRob extends frmKnjizenje {
	
	Interpreter bsh;
	
	private sysoutTEST ST = new sysoutTEST(false);
	private TypeDoc TD = TypeDoc.getTypeDoc();

	private knjigAddPanel kAP;
	
	public frmKnjRob() {
		srcTem = raKnjizenje.ROB;
    kAP = new knjigAddPanel();
    this.jp.add(kAP, java.awt.BorderLayout.CENTER);
	}
	
	public void SetFokus() {
		bsh = new Interpreter();
		try {
			bsh.set("thisBsh", bsh);
			frmSkripte.invoke("frmKnjRob.init", bsh);
		} catch (EvalError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    kAP.componentShow();
    super.SetFokus();
	}
	
	boolean noData;
	public boolean Validacija() {		
		raProcess.runChild(this, "Provjera", "Provjera nedostajuæih shema knjiženja...", new Runnable() {
			public void run() {
				kAP.setupString();
				raProcess.yield(checkShemeKnjizenje());
			}
		});
        if (!raProcess.isCompleted()) return false;
    		
        StorageDataSet shemeerror = (StorageDataSet) raProcess.getReturnValue();
        if (shemeerror.getRowCount() != 0) {
            getKnjizenje().setErrorMessage("Nema shema knjiženja ");
            ST.showInFrame(shemeerror, "Nedostajuæe sheme knjiženja");
            return false;
        }
        if (noData) {
            JOptionPane.showMessageDialog(this,
                    "Nema podataka za knjiženje !", "Greška",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
	}
	
	public boolean okPress() throws Exception {
		
		DataSet sh = dm.getRobSheme();
		for (sh.first(); sh.inBounds(); sh.next())
			if (sh.getString("CCSKL").length() > 0)
				checkAndRun(sh);
		
		for (sh.first(); sh.inBounds(); sh.next())
			if (sh.getString("CCSKL").length() == 0)
				checkAndRun(sh);
		
		return true;
	}
	
	void checkAndRun(DataSet sh) {
		
	}

	StorageDataSet checkShemeKnjizenje() {
		noData = true;
        StorageDataSet shemaMissing = getErrorSet();
        ArrayList alldocs = kAP.getArrayListVrdok();
        ArrayList alskladdok = new ArrayList();
        ArrayList alorgdok = new ArrayList();
        ArrayList almeuldok = new ArrayList();
        ArrayList almeizdok = new ArrayList();
        String lvrdok = "";
        for (int i = 0; i < alldocs.size(); i++) {
            lvrdok = (String) alldocs.get(i);
            if (TD.isDocSklad(lvrdok) && !TD.isDocMeskla(lvrdok)) {
                alskladdok.add(lvrdok);
            } else if (TD.isDocOJ(lvrdok) && !TD.isDocMeskla(lvrdok)) {
                alorgdok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MES")) {
                almeuldok.add(lvrdok);
                almeizdok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MEU")) {
                almeuldok.add(lvrdok);
            } else if (lvrdok.equalsIgnoreCase("MEI")) {
                almeizdok.add(lvrdok);
            }
        }
        
        Condition nknj = Condition.diff("STATKNJ", "K").and(datumCond("DATDOK"));
    
        if (alskladdok.size() > 0) {
        	Condition cond = Condition.in("VRDOK", alskladdok).and(nknj).and(kAP.skladCond());
        	insertErrors(shemaMissing, Aus.q("SELECT cskl,vrdok from doku WHERE " + cond + " group by cskl,vrdok"));
        	insertErrors(shemaMissing, Aus.q("SELECT cskl,vrdok from doki WHERE " + cond + " group by cskl,vrdok"));
        }
        
        if (alorgdok.size() > 0) {
        	Condition cond = Condition.in("VRDOK", alorgdok).and(nknj).and(kAP.corgCond());
        	insertErrors(shemaMissing, Aus.q("SELECT cskl,vrdok from doku WHERE " + cond + " group by cskl,vrdok"));
        	insertErrors(shemaMissing, Aus.q("SELECT cskl,vrdok from doki WHERE " + cond + " group by cskl,vrdok"));
        }
        
        Condition nknju = Condition.diff("STATKNJU", "K").and(datumCond("DATDOK"));
        Condition nknji = Condition.diff("STATKNJI", "K").and(datumCond("DATDOK"));
        
        if (almeuldok.size() > 0)
        	insertErrors(shemaMissing, Aus.q("SELECT csklul AS cskl,'MEU' AS vrdok from meskla WHERE " +
          		Condition.in("VRDOK", almeuldok).and(nknju).
          		and(Condition.in("CSKLUL", kAP.getArrayListSklad())) + " group by csklul"));
        
        if (almeizdok.size() > 0)
        	insertErrors(shemaMissing, Aus.q("SELECT cskliz AS cskl,'MEI' AS vrdok from meskla WHERE " +
        			Condition.in("VRDOK", almeizdok).and(nknji).
        			and(Condition.in("CSKLIZ", kAP.getArrayListSklad())) + " group by cskliz"));
    
        return shemaMissing;
	}

	StorageDataSet getErrorSet() {
        StorageDataSet shemaMissing = new StorageDataSet();
        Column opis = dm.getDoki().getColumn("OPIS").cloneColumn();
        opis.setWidth(1500);
        shemaMissing.setColumns(new Column[] { opis });
        shemaMissing.open();
        return shemaMissing;
	}
	
	void insertErrors(StorageDataSet errors, DataSet data) {
		if (data.rowCount() > 0) noData = false;
    	for (data.first(); data.inBounds(); data.next())
    	  	if (!matchShema(data)) {
    	      errors.insertRow(false);
    	      errors.setString("OPIS", data.getString("VRDOK") + " - " + data.getString("CSKL"));
    	    }
	}

	boolean matchShema(DataSet data) {
		DataSet sh = dm.getRobSheme();
		sh.open();
		for (sh.first(); sh.inBounds(); sh.next()) {
			String ccskl = sh.getString("CCSKL");
			String cskl = data.getString("CCSKL");
			if (sh.getString("CVRDOK").indexOf(data.getString("VRDOK")) >= 0) {
				if (ccskl.length() == 0) return true;
				String[] acsk = ccskl.indexOf(',') > 0 ? new VarStr(ccskl).splitTrimmed(',') : new VarStr(ccskl).split();
				for (int i = 0; i < acsk.length; i++)
					if (acsk[i].equals(cskl)) return true;
					else if (new raGlob(acsk[i]).matches(cskl)) return true;
			}
		}
		return false;
	}

}
