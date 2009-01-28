/****license*****************************************************************
**   file: VirmaniSK.java
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
 * Created on Feb 15, 2005
 */
package hr.restart.sk;

import hr.restart.baza.Condition;
import hr.restart.baza.Orgstruktura;
import hr.restart.baza.Virmani;
import hr.restart.baza.dM;
import hr.restart.db.raVariant;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.raSelectTableModifier;
import hr.restart.util.lookupData;
import hr.restart.util.raMatPodaci;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.frmVirmani;

import java.math.BigDecimal;
import java.sql.Timestamp;

import javax.swing.JOptionPane;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.ReadRow;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * @author andrej
 * Klasa koja za zadani dataset skstavke ili skstavkerad priprema virmane i zove frmVirmani za ispis 
 */
public class VirmaniSK extends frmVirmani {
//Varijable za virmane
  String _jedzav = "";           //  Jedinica zavoda
  String _nateret = "";          //  Na teret racuna
  String _svrha = "";            //  Svrha doznake
  String _ukorist = "";          //  U korist racuna
  String _brracnateret = "";     //  Broj racuna na teret
  String _nacizv = "";           //  Nacin izvrs
  String _pnbz1 = "";            //  Poziv na broj (zaduz.) 1
  String _pnbz2 = "";            //  Poziv na broj (zaduz.) 2
  String _sif1 = "";             //  Sifra 1
  String _sif2 = "";             //  Sifra 2
  String _sif3 = "";             //  Sifra 3
  String _brracukorist = "";     //  Broj racuna u korist
  String _pnbo1 = "";            //  Poziv na broj (odobr.) 1
  String _pnbo2 ="";             //  Poziv na broj (odobr.) 2
  BigDecimal _iznos = null;      //  Iznos
  String _mjesto = "";           //  Mjesto
  Timestamp _datizv = null;      //  Datum izvrsenja
  Timestamp _datpred = null;     //  Datum predaje
  private DataSet _skstavke;
  private raSelectTableModifier _selectionTracker;
  private raMatPodaci _master;
private boolean beenHere;
private boolean browseMode = false;
private frmVirmani.identifikator i;

  public VirmaniSK() {
  	super("sk");
  	browseMode = true;
  }
  		
  public VirmaniSK(DataSet skstavke, raSelectTableModifier selectionTracker) {
    super("sk");
    _skstavke = skstavke;
    _selectionTracker = selectionTracker;
  }
  
  /**
 * @param raMaster
 */
public VirmaniSK(raMatPodaci raMaster) {
	this(raMaster.getRaQueryDataSet(), raMaster.getSelectionTracker());
	_master	 = raMaster;
}

public void show() {
	if (browseMode) {
		super.show();
		return;
	}
  	if (!beenHere) {
  		beenHere = true;
  	  	i = new frmVirmani.identifikator(this);
  	  	i.show();
//  	    getJpTableView().fireTableDataChanged();
  	} else { 		
  		process();
  		super.show();
  	}
  }
  
  /**
 * 
 */
private void process() {
	if (checkExistAndDelete()) {
		processStavke();
	}
}

/**
 * @return
 */
private boolean checkExistAndDelete() {
	QueryDataSet qds = Virmani.getDataModule().getTempSet(Condition.equal("CKEY",ckey));
	qds.open();
	if (qds.getRowCount() > 0) {
	      if(JOptionPane.showConfirmDialog(null,"Postoje virmani sa identifikatorom \""+ckey+"\" !\n Želite li ih obrisati ?",
	            "Upozorenje !", JOptionPane.YES_NO_OPTION)==0) {
	      		qds.deleteAllRows();
	      		qds.saveChanges();
	      		return true;
	      } else {
	      	return false;
	      }
	}
	return true;
}

private void clearVars() {
		_jedzav = "";           //  Jedinica zavoda    
		_nateret = "";          //  Na teret racuna  
		_svrha = "";            //  Svrha doznake  
		_ukorist = "";          //  U korist racuna  
		_brracnateret = "";     //  Broj racuna na teret  
		_nacizv = "";           //  Nacin izvrs  
		_pnbz1 = "";            //  Poziv na broj (zaduz.) 1  
		_pnbz2 = "";            //  Poziv na broj (zaduz.) 2  
		_sif1 = "";             //  Sifra 1  
		_sif2 = "";             //  Sifra 2  
		_sif3 = "";             //  Sifra 3  
		_brracukorist = "";     //  Broj racuna u korist  
		_pnbo1 = "";            //  Poziv na broj (odobr.) 1  
		_pnbo2 ="";             //  Poziv na broj (odobr.) 2  
		_iznos = null;      //  Iznos  
		_mjesto = "";           //  Mjesto  
		_datizv = null;      //  Datum izvrsenja  
		_datpred = null;     //  Datum predaje  
  }
  private String getDefaultSvrha() {
    return frmParam.getParam("sk","svrhavir","Plaæanje po rn. ","Tekst svrhe doznake na virmanima",true);
  }
  private String getPnbz2Column() {
    return frmParam.getParam("sk","colpnbz2","","Naziv kolone u skstavkama iz koje se vadi pnbz2");
  }
  /**
   * @param skstavke
   */
  private void processStavke() {
  	if (_master!=null) _master.getJpTableView().enableEvents(false);
    clearVars();
    QueryDataSet naTerDS = Orgstruktura.getDataModule().getTempSet(Condition.equal("CORG",OrgStr.getKNJCORG()));
    naTerDS.open();
    String nazT = handleEmptyStr(naTerDS.getString("NAZIV"),0);
    String adrT = handleEmptyStr(naTerDS.getString("ADRESA"),1);
    String mjT = handleEmptyStr(naTerDS.getString("MJESTO"),0);
    String pbrT = handleEmptyStr(naTerDS.getString("HPBROJ") ,0);
    _nateret = nazT + "\n"+adrT+pbrT+" "+mjT;
    _jedzav = "NNDNN";
    _brracnateret = naTerDS.getString("ZIRO"); //TODO ziro racun na upit iz zirorn?
    DataSet sk = _selectionTracker.getSelectedView();
    for (sk.first(); sk.inBounds(); sk.next()) {
	      _svrha = getDefaultSvrha() + " " + sk.getString("BROJDOK");
	      _ukorist = getUkorist(sk.getInt("CPAR"));
	      _pnbz2 = getPnbz2(sk);
	      _brracukorist = getBRRUkorist(sk.getInt("CPAR"));
	      _brracnateret = OrgStr.getZiroForCorg(sk.getString("CORG"));
	      _pnbo2 = sk.getString("BROJDOK");
	      _iznos = sk.getBigDecimal("ID").add(sk.getBigDecimal("IP"));
	      _mjesto = mjT;
	      _datizv = getDatumPlacanja();
	      _datpred = getDatumPlacanja();	      
	      if (raVrdokMatcher.isRacun(sk)) {
	        if (raVrdokMatcher.isDob(sk))
	          add(_jedzav, _nateret, _svrha, _ukorist, _brracnateret, _nacizv, _pnbz1, _pnbz2, _sif1, _sif2, _sif3, _brracukorist, _pnbo1, _pnbo2, _iznos, _mjesto, _datizv, _datpred);
	        else if (raVrdokMatcher.isKup(sk)) {          
	          add(_jedzav, _ukorist, _svrha, _nateret, " ", _nacizv, _pnbz1, _pnbz2, _sif1, _sif2, _sif3, _brracnateret, _pnbo1, _pnbo2, _iznos, _mjesto, _datizv, _datpred);
	        }
	      }
    }
    _selectionTracker.destroySelectedView();
    save();
    /*if (_master!=null) {
    	_master.getJpTableView().enableEvents(true);
    	_master.getColumnsBean().refreshAction();
    }*/
  }

  /**
 * @return
 */
	private boolean doProcessStavka(ReadRow stavka) {
		if (_selectionTracker == null) return true;
		return _selectionTracker.isSelected(stavka);
	}

/**
   * Povuci datum sa nekakve predselekcije
   * @return
   */
  private Timestamp getDatumPlacanja() {
    // TODO Auto-generated method stub
    return i.getData().getTimestamp("datum");
  }

  /**
   * @param int1
   * @return
   */
  private String getBRRUkorist(int int1) {
    if (locatePar(int1)) {
      return dM.getDataModule().getPartneri().getString("ZR");
    } else {
      return "!!!NEISPRAVAN PARTNER!!!"; 
    }
  }
  private boolean locatePar(int int1) {
    QueryDataSet pp = dM.getDataModule().getPartneri();
    pp.open();
    return lookupData.getlookupData().raLocate(pp, "CPAR", int1+"");
  }
  /**
   * @param skstavke
   * @return
   */
  private String getPnbz2(DataSet skstavke) {
    String col = getPnbz2Column();
    if (skstavke.hasColumn(col)!=null) {
      return raVariant.getDataSetValue(skstavke,col).toString().trim();
    } 
    return "";
  }

  /**
   * @param int1
   * @return
   */
  private String getUkorist(int int1) {
    if (locatePar(int1)) {
      QueryDataSet pp = dM.getDataModule().getPartneri();
      return pp.getString("NAZPAR")+"\n"+pp.getString("ADR")+" "+pp.getInt("PBR")+" "+pp.getString("MJ");
    } else {
      return "!!!NEISPRAVAN PARTNER!!!"; 
    }
  } 
}   
                            