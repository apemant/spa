/****license*****************************************************************
**   file: deleteAllDoc.java
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

import hr.restart.baza.Condition;
import hr.restart.baza.stdoki;
import hr.restart.swing.JraDialog;
import hr.restart.swing.JraTextField;
import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;
import hr.restart.util.startFrame;

import java.awt.BorderLayout;
import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

public class deleteAllDoc {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private hr.restart.util.Valid val= hr.restart.util.Valid.getValid();
  private hr.restart.util.raCommonClass rcc  =  hr.restart.util.raCommonClass.getraCommonClass();
  private String greska = "";

  final private PreparedStatement delZaglav = raTransaction.getPreparedStatement
  ("DELETE FROM doki WHERE cskl = ? and vrdok = ? and god = ? and brdok between ? and ?");

  final private PreparedStatement findZaglav = raTransaction.getPreparedStatement
  ("DELETE FROM doki WHERE cskl = ? and vrdok = ? and god = ? and brdok between ? and ?");

  final private PreparedStatement updateZaglav = raTransaction.getPreparedStatement
  ("UPDATE doki set uirac = 0 WHERE cskl = ? and vrdok = ? and god = ? and brdok = ? ");

  final private PreparedStatement updateStanje = raTransaction.getPreparedStatement
  ("UPDATE stanje set kolrez = kolrez - ? WHERE cskl = ?  and god = ? and cart = ? ");
  
  final private PreparedStatement delStavke = raTransaction.getPreparedStatement
  ("DELETE FROM stdoki WHERE cskl = ? and vrdok = ? and god = ? and brdok between ? and ?");

  final private PreparedStatement delStavkePoje = raTransaction.getPreparedStatement
  ("DELETE FROM stdoki WHERE cskl = ? and vrdok = ? and god = ? and brdok =  ? ");

  final private PreparedStatement delVtrabat = raTransaction.getPreparedStatement
  ("delete from vtrabat where cskl  = ? and vrdok = ? and god  = ? and brdok between ? and ?");

  final private PreparedStatement delVtrabatPoje = raTransaction.getPreparedStatement
  ("delete from vtrabat where cskl  = ? and vrdok = ? and god  = ? and brdok = ? and rbr = ?");

  final private PreparedStatement delVTzavtr = raTransaction.getPreparedStatement
  ("delete from vtzavtr where cskl = ? and vrdok = ? and god = ? and brdok between ? and ?");

  final private PreparedStatement delVTzavtrPoje = raTransaction.getPreparedStatement
  ("delete from vtzavtr where cskl = ? and vrdok = ? and god = ? and brdok = ? and rbr = ?");

  final private PreparedStatement updateSEQ = raTransaction.getPreparedStatement
  ("UPDATE SEQ SET BROJ = ? WHERE opis = ? ");

  final private PreparedStatement delVTText = raTransaction.getPreparedStatement
  ("DELETE FROM VTText WHERE ckey = ?");

  final private PreparedStatement delVTPrijenos1 = raTransaction.getPreparedStatement
  ("DELETE FROM VTprijenos WHERE keysrc = ?");

  final private PreparedStatement delVTPrijenos2 = raTransaction.getPreparedStatement
  ("DELETE FROM VTprijenos WHERE keydest = ?");
  
  
  final private PreparedStatement rezervationVeza = raTransaction.getPreparedStatement
     ("UPDATE STDOKI SET VEZA  = '',STATUS='N' WHERE VEZA = ?");
  /*final private PreparedStatement rezervationRefresh = raTransaction.getPreparedStatement
	  ("UPDATE STANJE SET KOLREZ = (SELECT sum(kol) FROM stdoki WHERE "+
       "STDOKI.CART = STANJE.CART AND STDOKI.GOD=STANJE.GOD AND "+
       "STDOKI.CSKLART=STANJE.CSKL AND STDOKI.VRDOK='PON' AND "+
       "(VEZA='' or VEZA iS NULL or veza=? )) where cskl = ? and god=? and cart =?");
*/
//  final private PreparedStatement updateAktiv = raTransaction.getPreparedStatement
//  ("UPDATE doki set aktiv='D' WHERE ?");

  private QueryDataSet qds = null;
  private QueryDataSet masterset = null;
  private raControlDocs rCD = new raControlDocs();
  private QueryDataSet DummySet = new QueryDataSet();
  private Column cBROJOD = new Column();
  private Column cBROJDO = new Column();
  private Column cCSKL = new Column();
  private Column cVRDOK = new Column();
  private Column cGOD = new Column();
  private JDialog deleteDialog = null;

  public void presOK(){
    if (ValidacijaAllDell()) {
      if (javax.swing.JOptionPane.showConfirmDialog(deleteDialog,"Sigurno želite obrisati te ra\u010Dune ?","Upit",
          javax.swing.JOptionPane.YES_NO_OPTION,javax.swing.JOptionPane.QUESTION_MESSAGE) == javax.swing.JOptionPane.YES_OPTION){
        Brisanje();
      }
      else {
        presCancel();
      }
    }
    else {
      javax.swing.JOptionPane.showConfirmDialog(deleteDialog,greska,"Greška",
          javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
    }
  }

  public void presCancel(){
    deleteDialog.hide();
    deleteDialog = null;
  }
  String titel;

  public void initDummyMY(){

    DummySet.close();
    cCSKL = (Column) dm.getDoki().getColumn("CSKL").clone();
    cVRDOK = (Column) dm.getDoki().getColumn("VRDOK").clone();
    cGOD = (Column) dm.getDoki().getColumn("GOD").clone();
    cBROJOD.setColumnName("BROJOD");
    cBROJOD.setDataType(com.borland.dx.dataset.Variant.INT);
    cBROJOD.setPrecision(8);
    cBROJOD.setDisplayMask("########");
    cBROJDO.setColumnName("BROJDO");
    cBROJDO.setDataType(com.borland.dx.dataset.Variant.INT);
    cBROJDO.setPrecision(8);
    cBROJDO.setDisplayMask("########");
    DummySet.setColumns(new Column[] {cBROJOD,cBROJDO,cCSKL,cVRDOK,cGOD});
    DummySet.open();
    DummySet.setString("CSKL",masterset.getString("CSKL"));
    DummySet.setString("VRDOK",masterset.getString("VRDOK"));
    DummySet.setString("GOD",masterset.getString("GOD"));
    QueryDataSet sometodelfor = hr.restart.util.Util.getNewQueryDataSet
    ("SELECT max(brdok) as pero FROM doki WHERE cskl = '"+DummySet.getString("CSKL")+
     "' and vrdok = '"+DummySet.getString("VRDOK")+
     "' and god = '"+ DummySet.getString("GOD")+"'",true);
    DummySet.setInt("BROJOD",1);
    DummySet.setInt("BROJDO",sometodelfor.getInt("PERO"));
  }

  private raLocalTransaction rtDellAllStavke = new raLocalTransaction(){
    public boolean transaction() throws Exception{
//      raPrenosVT rPVT = new raPrenosVT();

      qds.first();
      String key ="";
//      String key2 ="";
      do {
        QueryDataSet v = stdoki.getDataModule().getTempSet(
            Condition.equal("VEZA", qds.getString("ID_STAVKA")));
        v.open();
        BigDecimal koliko = qds.getBigDecimal("KOL");
        for (v.first(); v.inBounds(); v.next()) {
          if (v.getString("REZKOL").equalsIgnoreCase("D") &&
              !TypeDoc.getTypeDoc().isDocDiraZalihu(v.getString("VRDOK")))
            koliko = koliko.subtract(v.getBigDecimal("KOL"));
        }
        
      	if (qds.getString("REZKOL").equalsIgnoreCase("D")){
            try {
      		updateStanje.setBigDecimal(1,koliko);
      		if (qds.getString("CSKLART").equalsIgnoreCase("")) {
      			updateStanje.setString(2,qds.getString("CSKL"));
      		} else {
      			updateStanje.setString(2,qds.getString("CSKLART"));
      		}
      		updateStanje.setString(3,qds.getString("GOD"));
      		updateStanje.setInt(4,qds.getInt("CART"));
      		updateStanje.execute();
            }
            catch (SQLException ex) {
              ex.printStackTrace();
              return false ;
            }	
      	
      	}
      	try {
      	    rezervationVeza.setString(1,qds.getString("ID_STAVKA"));
      	    rezervationVeza.execute();
      	}
        catch (SQLException ex) {
            ex.printStackTrace();
            return false ;
          }

      	/*try {
      		rezervationRefresh.setString(1,qds.getString("ID_STAVKA"));
	      	if (qds.getString("CSKLART").equalsIgnoreCase("")) {
	      		rezervationRefresh.setString(2,qds.getString("CSKL"));
	  		} else {
	  			rezervationRefresh.setString(2,qds.getString("CSKLART"));
	  		}
	      	rezervationRefresh.setString(3,qds.getString("GOD"));
	      	rezervationRefresh.setInt(4,qds.getInt("CART"));
	      	rezervationRefresh.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false ;
        }*/

      	
        try {
          key = rCD.getKey(qds);
          delVTText.setString(1,key);
          delVTText.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false ;
        }

        try {
          delVtrabatPoje.setString(1,qds.getString("CSKL"));
          delVtrabatPoje.setString(2,qds.getString("VRDOK"));
          delVtrabatPoje.setString(3,qds.getString("GOD"));
          delVtrabatPoje.setInt(4,qds.getInt("BRDOK"));
          delVtrabatPoje.setInt(5,qds.getInt("RBSID"));
          delVtrabatPoje.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false;
        }
        try {
          delVTzavtrPoje.setString(1,qds.getString("CSKL"));
          delVTzavtrPoje.setString(2,qds.getString("VRDOK"));
          delVTzavtrPoje.setString(3,qds.getString("GOD"));
          delVTzavtrPoje.setInt(4,qds.getInt("BRDOK"));
          delVTzavtrPoje.setInt(5,qds.getInt("RBSID"));
          delVTzavtrPoje.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false;
        }


      } while (qds.next());


      try {
        delStavkePoje.setString(1,qds.getString("CSKL"));
        delStavkePoje.setString(2,qds.getString("VRDOK"));
        delStavkePoje.setString(3,qds.getString("GOD"));
        delStavkePoje.setInt(4,qds.getInt("BRDOK"));
        delStavkePoje.execute();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        return false;
      }
      try {
        updateZaglav.setString(1,qds.getString("CSKL"));
        updateZaglav.setString(2,qds.getString("VRDOK"));
        updateZaglav.setString(3,qds.getString("GOD"));
        updateZaglav.setInt(4,qds.getInt("BRDOK"));
        updateZaglav.execute();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        return false;
      }
     return true;
    }
    };

  raLocalTransaction rlt = new raLocalTransaction(){
  public boolean transaction() throws Exception{
//        boolean retValue = true;
    raPrenosVT rPVT = new raPrenosVT();
    String key = "";
    DummySet.getString("CSKL");
    DummySet.getString("VRDOK");
    DummySet.getString("GOD");
    QueryDataSet sometodelfor = hr.restart.util.Util.getNewQueryDataSet
    ("SELECT * FROM stdoki WHERE cskl = '"+DummySet.getString("CSKL")+
     "' and vrdok = '"+DummySet.getString("VRDOK")+
     "' and god = '"+ DummySet.getString("GOD")+
     "' and brdok between "+DummySet.getInt("BROJOD")+
     " and "+DummySet.getInt("BROJDO"),true);
    sometodelfor.first();
    do {
      try {
        key = rCD.getKey(sometodelfor);
        delVTText.setString(1,key);
        delVTText.execute();
      }
      catch (SQLException ex) {
        ex.printStackTrace();
        return false ;
      }
    }while (sometodelfor.next());


//// za vtprijenos
      QueryDataSet sometodelfor2 = hr.restart.util.Util.getNewQueryDataSet
      ("SELECT * FROM doki WHERE cskl = '"+DummySet.getString("CSKL")+
       "' and vrdok = '"+DummySet.getString("VRDOK")+
       "' and god = '"+ DummySet.getString("GOD")+
       "' and brdok between "+DummySet.getInt("BROJOD")+
       " and "+DummySet.getInt("BROJDO"),true);

      for (sometodelfor2.first();sometodelfor2.inBounds();sometodelfor2.next()) {
        String  key2= rPVT.makeKey("doki",sometodelfor2.getString("CSKL"),
        sometodelfor2.getString("VRDOK"),
        sometodelfor2.getString("GOD"),
        sometodelfor2.getInt("BRDOK"));

        QueryDataSet tmp1 = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM VTprijenos WHERE keysrc='"+key2+"'",true);
        for (tmp1.first();tmp1.inBounds();tmp1.next())
          raTransaction.saveChanges(rPVT.getOtherDocUpdated(tmp1.getString("keydest")));

//          QueryDataSet upd = hr.restart.util.Util.getNewQueryDataSet(
//          "SELECT * FROM doki WHERE "+rPVT.demakeKey(tmp1.getString("keydest"),"doki"),true);
//          for (upd.first();upd.inBounds();upd.next()){
//            upd.setString("AKTIV","D");
//          }
//          raTransaction.saveChanges(upd);
//        }

        QueryDataSet tmp2 = hr.restart.util.Util.getNewQueryDataSet(
            "SELECT * FROM VTprijenos WHERE keydest='"+key2+"'",true);
        for (tmp2.first();tmp2.inBounds();tmp2.next())
          raTransaction.saveChanges(rPVT.getOtherDocUpdated(tmp2.getString("keysrc")));
//          QueryDataSet upd = hr.restart.util.Util.getNewQueryDataSet(
//          "SELECT * FROM doki WHERE "+rPVT.demakeKey(tmp2.getString("keysrc"),"doki"),true);
//          for (upd.first();upd.inBounds();upd.next()){
//            upd.setString("AKTIV","D");
//          }
//          raTransaction.saveChanges(upd);
//        }

        try {
          delVTPrijenos1.setString(1,key2);
          delVTPrijenos1.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false ;
        }
        try {
          delVTPrijenos2.setString(1,key2);
          delVTPrijenos2.execute();
        }
        catch (SQLException ex) {
          ex.printStackTrace();
          return false ;
        }
      }

    try {
      delZaglav.setString(1,DummySet.getString("CSKL"));
      delZaglav.setString(2,DummySet.getString("VRDOK"));
      delZaglav.setString(3,DummySet.getString("GOD"));
      delZaglav.setInt(4,DummySet.getInt("BROJOD")); //BRDOK
      delZaglav.setInt(5,DummySet.getInt("BROJDO")); //BRDOK
      delZaglav.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false ;
    }
    try {
//  ("DELETE FROM stdoki WHERE cskl = ? and vrdok = ? and god = ? and brdok between ? and ?");
//System.out.println("o\u0111e brišem stavke");
      delStavke.setString(1,DummySet.getString("CSKL"));
      delStavke.setString(2,DummySet.getString("VRDOK"));
      delStavke.setString(3,DummySet.getString("GOD"));
      delStavke.setInt(4,DummySet.getInt("BROJOD")); //BRDOK
      delStavke.setInt(5,DummySet.getInt("BROJDO")); //BRDOK
      delStavke.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false ;
    }
    try {
      delVtrabat.setString(1,DummySet.getString("CSKL"));
      delVtrabat.setString(2,DummySet.getString("VRDOK"));
      delVtrabat.setString(3,DummySet.getString("GOD"));
      delVtrabat.setInt(4,DummySet.getInt("BROJOD")); //BRDOK
      delVtrabat.setInt(5,DummySet.getInt("BROJDO")); //BRDOK
      delVtrabat.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false ;
    }
    try {
      delVTzavtr.setString(1,DummySet.getString("CSKL"));
      delVTzavtr.setString(2,DummySet.getString("VRDOK"));
      delVTzavtr.setString(3,"GOD");
      delVTzavtr.setInt(4,DummySet.getInt("BROJOD")); //BRDOK
      delVTzavtr.setInt(5,DummySet.getInt("BROJDO")); //BRDOK
      delVTzavtr.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false ;
    }
    key=masterset.getString("CSKL")+
        masterset.getString("VRDOK")+
        val.findYear(masterset.getTimestamp("DATDOK"));
    try {
      updateSEQ.setString(1,String.valueOf(DummySet.getInt("BROJOD")-1));
      updateSEQ.setString(2,key);
      updateSEQ.execute();
    }
    catch (SQLException ex) {
      ex.printStackTrace();
      return false ;
    }
//          updateSEQ.setString(1,String.valueOf(DummySet.getInt("BROJOD")));
    return true;
  }};


  public deleteAllDoc() {
  }

  public void delZaglav(QueryDataSet ds){
    masterset = ds;
    if (deleteDialog == null) initDialog();
    deleteDialog.pack();
    startFrame.getStartFrame().centerFrame(deleteDialog,0,titel);
    startFrame.getStartFrame().showFrame(deleteDialog);
//  ("DELETE FROM stdoki WHERE cskl = ? and vrdok = ? and god = ? and brdok = ?");
  }


  public void delStavke(QueryDataSet ds){
    qds = ds;


    QueryDataSet privqds= hr.restart.util.Util.getNewQueryDataSet(
    "select * from stdoki where cskl='"+qds.getString("CSKL")+"' and vrdok ='"+
                                        qds.getString("VRDOK")+"' and god='"+
                                        qds.getString("GOD")+"' and brdok="+
                                        qds.getInt("BRDOK")+ "and status !='N'",true);

    if (privqds.getRowCount() !=0) {
      javax.swing.JOptionPane.showConfirmDialog(null,"Brisanje neuspješno jer postoje stavke za koje je napravljena otpremnica","Greška",
      javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
          return;
    }


    if (rtDellAllStavke.execTransaction()) {
      dm.getSynchronizer().markAsDirty("VTText");
    javax.swing.JOptionPane.showConfirmDialog(null,"Brisanje završeno","Obavijest",
        javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.INFORMATION_MESSAGE);
    }
    else {
        javax.swing.JOptionPane.showConfirmDialog(null,"Brisanje neuspješno","Greška",
        javax.swing.JOptionPane.DEFAULT_OPTION,javax.swing.JOptionPane.ERROR_MESSAGE);
    }

  }
  public void initDialog(){

    initDummyMY();
    deleteDialog = new JraDialog();
    deleteDialog.getContentPane().setLayout(new BorderLayout());
    deleteDialog.addKeyListener(new java.awt.event.KeyAdapter(){
      public void keyPressed(java.awt.event.KeyEvent e){
        if (e.getKeyCode()==java.awt.event.KeyEvent.VK_F10){
          presOK();
        }
        else if (e.getKeyCode()==java.awt.event.KeyEvent.VK_ESCAPE){

          presCancel();
        }
      }
    });

    hr.restart.util.OKpanel okp = new hr.restart.util.OKpanel(){
      public void  jBOK_actionPerformed(){
        presOK();
      }
      public void  jPrekid_actionPerformed(){
        presCancel();
      }
    };
    JPanel jpanel = new JPanel();
    XYLayout XYLayout = new XYLayout();
    jpanel.setLayout(XYLayout);
    JLabel jlabel = new JLabel("Brisati od");
    JraTextField tfOD = new JraTextField();
    tfOD.setColumnName("BROJOD");
    tfOD.setDataSet(DummySet);
    JLabel jlabeldo = new JLabel("do");
    JraTextField tfDO = new JraTextField();
    tfDO.setColumnName("BROJDO");
    tfDO.setDataSet(DummySet);
  /*
    JraLabel jrlCSKL = new JraLabel();
    jrlCSKL.setColumnName("CSKL");
    jrlCSKL.setDataSet(DummySet);
    JraLabel jrlVRDOK = new JraLabel();
    jrlVRDOK.setColumnName("VRDOK");
    jrlVRDOK.setDataSet(DummySet);
    JraLabel jrlGOD = new JraLabel();
    jrlGOD.setColumnName("GOD");
    jrlGOD.setDataSet(DummySet);
*/


    XYLayout.setHeight(40);
    XYLayout.setWidth(400);
/*
    jpanel.add(new JLabel("Višestruko brisanje dokumenata za"),new XYConstraints(15,1,1,-1));
    jpanel.add(jrlCSKL,new XYConstraints(150,1,50,-1));
    jpanel.add(jrlVRDOK,new XYConstraints(210,1,50,-1));
    jpanel.add(jrlGOD,new XYConstraints(270,1,70,-1));
*/
    jpanel.add(jlabel,new XYConstraints(15,10,-1,-1));
    jpanel.add(tfOD,new XYConstraints(150,10,100,-1));
    jpanel.add(jlabeldo,new XYConstraints(255,10,-1,-1));
    jpanel.add(tfDO,new XYConstraints(275,10,100,-1));
    jpanel.setBorder(BorderFactory.createEtchedBorder());
    deleteDialog.getContentPane().add(okp,BorderLayout.SOUTH);
    deleteDialog.getContentPane().add(jpanel,BorderLayout.CENTER);


    rcc.setLabelLaF(tfDO,false);
    titel = ("Brisanje više ra\u010Duna za "+DummySet.getString("VRDOK")+" - "+
             DummySet.getString("CSKL")+" / "+ DummySet.getString("GOD"));
  }


  public boolean ValidacijaAllDell(){

    QueryDataSet findZaglav1 = hr.restart.util.Util.getNewQueryDataSet
    ("SELECT * FROM doki WHERE cskl = '"+DummySet.getString("CSKL")+
     "' and vrdok = '"+DummySet.getString("VRDOK")+
     "' and god = '"+ DummySet.getString("GOD")+
     "' and brdok = "+DummySet.getInt("BROJOD"),true);
    if (findZaglav1.getRowCount()==0) {
      greska =" Ne postoji dokument s brojem "+String.valueOf(DummySet.getInt("BROJOD"));
      return false;
    }

    QueryDataSet privqds= hr.restart.util.Util.getNewQueryDataSet(
        "SELECT * FROM stdoki WHERE cskl = '"+DummySet.getString("CSKL")+
         "' and vrdok = '"+DummySet.getString("VRDOK")+
         "' and god = '"+ DummySet.getString("GOD")+
         "' and brdok between "+DummySet.getInt("BROJOD")+
         " and "+DummySet.getInt("BROJDO")+ " and status !='N'",true);

    if (privqds.getRowCount()>0) {
      greska =" Postoje stavke raèuna za koje postoje otpremnice !!";
      return false;
    }




    QueryDataSet findZaglav = hr.restart.util.Util.getNewQueryDataSet
    ("SELECT * FROM doki WHERE cskl = '"+DummySet.getString("CSKL")+
     "' and vrdok = '"+DummySet.getString("VRDOK")+
     "' and god = '"+ DummySet.getString("GOD")+
     "' and brdok between "+DummySet.getInt("BROJOD")+
     " and "+DummySet.getInt("BROJDO"),true);

    findZaglav.first();
    do {
      if (!findZaglav.getString("CUSER").equals(
          hr.restart.sisfun.raUser.getInstance().getUser())){
        greska =" Ne možete brisati dokumente koje niste sami kreirali";
        return false;
      }
      /** @todo  provjera statusa*/
    } while (findZaglav.next());


    return true;
  }

  public void Brisanje(){


      if (rlt.execTransaction()) {
        dm.getSynchronizer().markAsDirty("VTText");
        javax.swing.JOptionPane.showMessageDialog(deleteDialog,"Brisanje završeno !","Obavijest",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        afterDelAll();
        presCancel();

      }
      else {
        javax.swing.JOptionPane.showMessageDialog(deleteDialog,"Brisanje neuspješno !","Greška",
            javax.swing.JOptionPane.ERROR_MESSAGE);
      }

  }
  public void afterDelAll(){}
  
  public void nesto(){
/*	  
	  ()
	  "SELECT sum(kol) as kol FROM stdoki WHERE CART = "
		+cart+" AND (CSKL='"+cskl+"' OR CSKLART='"+
		cskl+"') AND GOD='"+god+"' AND "+
		"(VEZA='' or VEZA iS NULL)", true).getBigDecimal("KOL");
*/		
	  
	  
  }
}
