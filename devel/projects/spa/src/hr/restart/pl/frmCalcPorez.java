/****license*****************************************************************
**   file: frmCalcPorez.java
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
 * frmCalcPorez.java
 *
 * Created on 2003. prosinac 01, 16:45
 */

package hr.restart.pl;

import hr.restart.swing.raPropertiesBinder;
import hr.restart.util.Aus;
import hr.restart.util.raImages;
import hr.restart.util.raTabDialog;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import javax.swing.JOptionPane;
import javax.swing.JTextField;

/**
 * Ekran za brzi obracun poreza bez placa,
 * pokusaj standalone app za javaws
 * koristi:
 * panel 
 *  polja <read-only> 1-n - do 5 stavki
 *    bruto,    (koefsindrom31860)
 *    stdop1-n, 
 *    maxosndop1-n,
 *    <iznosdop1-n>
 *    <ukdop>
 *    osnolak, koefolak
 *    premije1-n
 *    <sumolak>
 *    <iskolak>
 *    <porosn>
 *    stpor1-n,
 *    maxosnpor1-n,
 *    <iznospor1-n>
 *    <ukporez>
 *    stprir
 *    <iznosprir>
 *    <por+prir>
 *    neto
 *    
 * class hr.restart.pl.raCalcPorez, jpCalcPorezCalc, jpCalcPorezParams, jpCalcPorez
 * interface raCalcPorezDataGetter za dohvat svih podataka - iz placa preko cradnik
 * class hr.restart.util.FileHandler - oslobodjen jdbc gluposti
 * hr.restart.swing.JraKeyListener
 * mozda hr.restart.swing.JraFrame
 * mozda hr.restart.swing.JraButton
 * @author  andrej
 */
public class frmCalcPorez extends raTabDialog {
  raPropertiesBinder binder;
  private BigDecimal sto = new BigDecimal("100.00");
  public frmCalcPorez() {
    this((raCalcPorezDataGetter[])null);
  }
  public frmCalcPorez(raCalcPorezDataGetter getter) {
    this(new raCalcPorezDataGetter[] {getter});
  }
  public frmCalcPorez(raCalcPorezDataGetter[] getters) {
    super("Kalkulator za plaæu");
    binder = new raPropertiesBinder("default.calcporez");
    if (getters != null) {
      for (int i=0; i<getters.length; i++) {
        getters[i].setFrmCalcPorez(this);
        addFrameTab(getters[i],getters[i].getImageIcon());
      }
    }
    addFrameTab(new jpCalcPorezCalc(this, "Bruto i doprinosi",1), raImages.getImageOrModuleIcon("gk"));
    addFrameTab(new jpCalcPorezCalc(this, "Olakšice",2), raImages.getImageOrModuleIcon("blpn"));
    addFrameTab(new jpCalcPorezCalc(this, "Porezi i neto",3), raImages.getImageOrModuleIcon("pl"));
    addFrameTab(new jpCalcPorezCalc(this), raImages.getImageOrModuleIcon("sk"));
    addFrameTab(new jpCalcPorezParam(this), raImages.getImageOrModuleIcon("rn"));
    taskInit();
    fixOkp();
    setHideOnOK(false);
  }
  private boolean debug = true;
  public void calcGo(boolean isCalcBack) {
    //prije
    preCalc(isCalcBack);
    //calcPorez
    doCalc(isCalcBack);
    //poslije
    postCalc(isCalcBack);
    String scrollInd = isCalcBack?"Bruto":"Neto";
    JTextField[] bcmps = binder.getBcomps(scrollInd);
    for (int i=0; i<bcmps.length; i++) {
      if (bcmps[i].getTopLevelAncestor() != null) {
        //raPropertiesBindedPanel.scrollToVisible(bcmps[i]);
        bcmps[i].requestFocus();
        break;
      }
    }
    repaint();
  }
  
  public void pack() {
    super.pack();
    setSize(new Dimension(600,450));
  }
  
/*ulaz Porezna osnovica za calc(), Neto prije kredita za calcBack
 */
  BigDecimal getUlaz(boolean isBack) {
    return binder.getBigDecimal(isBack?"Neto":"Porezna osnovica");
  }
/*_stope Stope poreza  
 */
  BigDecimal[] getStope(boolean isBack) {
    String bareTag = "Stopa poreza";
    BigDecimal[] stope = new BigDecimal[4];
    for (int i=0; i<stope.length; i++) {
      String ident = bareTag+(i+1);
      stope[i] = binder.getBigDecimal(ident).divide(sto, 6, BigDecimal.ROUND_HALF_UP);
    }
    return stope;
  }

/* _oldosn ako se zove calc(): setOldOsn(suma_poreznih_osnovica_u_tom_mjesecu);
 * ako se zove calcBack(): setOldOsn(suma_neta_u_tom_mjesecu);
 */  
  BigDecimal getOldOsn(boolean isBack) {
    return Aus.zero2;
  }

 /*_oldpor iznosi obracunatih poreza u ranijim isplatama
  * za calc() _oldpor.length() mora biti jednako _stope.length()
  * za calcBack() oldpor[5] = lodbitak jer nemre bit vise od 5 poreza, oldpor[6] = minpl (1500)
  */
  BigDecimal[] getOldPor(boolean isBack) {
    BigDecimal[] oldpor;
    if (isBack) oldpor = new BigDecimal[7];
    else oldpor = new BigDecimal[5];
    for (int i=0; i<oldpor.length; i++) {
      oldpor[i] = Aus.zero2;
    }
    if (isBack) {
      oldpor[5] = binder.getBigDecimal("Ukupno olakšica").add(binder.getBigDecimal("Premije osiguranja"));
      oldpor[6] = binder.getBigDecimal("Osnovna olakšica");
    }
    return oldpor;
  }
  
/*_stprir stopa prireza
 */  
  BigDecimal getStPrir(boolean isBack) {
    return binder.getBigDecimal("Stopa prireza").setScale(4).divide(sto,BigDecimal.ROUND_HALF_UP);
  }
  
/*_limits osnovice za poreze npr. 3000,6750,21000
 */  
  BigDecimal[] getLimits(boolean isBack) {
    BigDecimal[] limits = new BigDecimal[3];
    for (int i=0; i<limits.length; i++) {
      limits[i] = binder.getBigDecimal("Porez "+(i+1));
    }
    return limits;
  }
    
  void preCalc(boolean isBack) {
    //ukupno olaksica
    BigDecimal ukol = binder.getBigDecimal("Osnovna olakšica").multiply(binder.getBigDecimal("Koeficijent olakšice"));
    binder.setBigDecimal("Ukupno olakšica", ukol);
    if (isBack) {
      
    } else {
      //doprinosi
      BigDecimal[] stdops = getStopeDop();
      BigDecimal bto = binder.getBigDecimal("Bruto");
      BigDecimal sumDop = Aus.zero2;
      for (int i=0; i<stdops.length; i++) {
        BigDecimal maxos = binder.getBigDecimal("Maksimalna za MIO "+(i+1)+". stup");
        BigDecimal os;
        if (maxos.compareTo(Aus.zero2)!=0 && bto.compareTo(maxos) > 0) os = maxos;
        else os = bto;
        binder.setBigDecimal("Osnovica"+(i+1), os);
        BigDecimal iznos = os.multiply(stdops[i].divide(sto,BigDecimal.ROUND_HALF_UP));
        binder.setBigDecimal("Iznos"+(i+1), iznos);
        sumDop = sumDop.add(iznos);
      }
      //placa po odbitku doprinosa
      binder.setBigDecimal("Plaæa po odbitku doprinosa", bto.add(sumDop.negate()));
      //iskoristena olaksica
      BigDecimal iskol = ukol.add(binder.getBigDecimal("Premije osiguranja"));
      if (bto.add(sumDop.negate()).compareTo(iskol) < 0) iskol = bto.add(sumDop.negate());
      binder.setBigDecimal("Iskorištena olakšica", iskol);
      //porezna osnovica
      binder.setBigDecimal("Porezna osnovica", bto.add(sumDop.negate()).add(iskol.negate()));
    }
  }
   

  
  void doCalc(boolean isCalcBack) {
    raCalcPorez calcer = new raCalcPorez();
    calcer.init(getUlaz(isCalcBack), getStope(isCalcBack), getOldOsn(isCalcBack),
    getOldPor(isCalcBack), getStPrir(isCalcBack), getLimits(isCalcBack));
    if (isCalcBack) {
      calcer.calcBack();
//      BigDecimal uulaz = calcer.getUlaz();
      BigDecimal neto1 = calcer.getIzlaz();
      System.out.println("neto1 = "+neto1);
//      System.out.println("korekcija "+binder.getBigDecimal("Korekcija-31860"));
      BigDecimal stopa1 = binder.getBigDecimal("Stopa1").divide(sto,BigDecimal.ROUND_HALF_UP);
      BigDecimal stopa2 = binder.getBigDecimal("Stopa2").divide(sto,BigDecimal.ROUND_HALF_UP);
      BigDecimal maxosn1 = binder.getBigDecimal("Maksimalna za MIO 1. stup");
      BigDecimal maxosn2 = binder.getBigDecimal("Maksimalna za MIO 2. stup");
      binder.setBigDecimal("Bruto", raCalcPorez.neto1ToBruto(neto1,new BigDecimal[] {stopa1,stopa2},new BigDecimal[] {maxosn1,maxosn2}));
      calcGo(false);
      return; 
    }
    calcer.calc();
    binder.setBigDecimal("Prirez", calcer.getRes_prir());
    BigDecimal sumPorez = Aus.zero2;
    for (int i=0; i<calcer.getRes_por().length; i++) {
      if (calcer.getStope()[i].compareTo(Aus.zero2)!=0) {
        binder.setBigDecimal("Iznos poreza"+(i+1), calcer.getRes_por()[i]);
        binder.setBigDecimal("Por.osnovica"+(i+1), 
          calcer.getRes_por()[i].divide(calcer.getStope()[i], BigDecimal.ROUND_HALF_UP));
        sumPorez = sumPorez.add(calcer.getRes_por()[i]);
      }
    }
    binder.setBigDecimal("Porez i prirez", sumPorez.add(calcer.getRes_prir()));
    binder.setBigDecimal("Neto", 
      binder.getBigDecimal("Plaæa po odbitku doprinosa")
      .add(sumPorez.add(calcer.getRes_prir()).negate()));
  }
  
  void postCalc(boolean isBack) {
    /**@todo Implement this method
    throw new java.lang.UnsupportedOperationException(
    "Method postCalc(boolean) not yet implemented.");*/
  }
  
  BigDecimal[] getStopeDop() {
    return new BigDecimal[] {
      binder.getBigDecimal("Stopa1"),
      binder.getBigDecimal("Stopa2")
    };
  }

  public void action_ok() {
    if (getSelectedTabComponent() != null && getSelectedTabComponent().doSaving()) {
      getSelectedTabComponent().action_jBOK();
    } else {
      askAndSave();
    }
  }
  
  private void askAndSave() {
    String newBindFile = JOptionPane.showInputDialog(this, "Snimiti podatke pod imenom: ");
    if (newBindFile == null) return;
    raPropertiesBinder newBinder = null;
    try {
      binder.store();
      newBinder = raPropertiesBinder.cloneBinder(binder, newBindFile+".calcporez");
      newBinder.store();
      JOptionPane.showMessageDialog(this, "Snimljeno u datoteku "+newBinder.getFileName()+" !", "Info", JOptionPane.INFORMATION_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this, "Snimanje u datoteku "+newBindFile+" nije uspjelo! ("+ex.getMessage()+")", "Greška", JOptionPane.ERROR_MESSAGE);
    }
  }
  
  public void fixOkp() {
    getOKPanel().jBOK.setText("Snimi");
    getOKPanel().jBOK.setIcon(raImages.getImageIcon(raImages.IMGSAVE));
    getOKPanel().jPrekid.setText("Izlaz");
  }

  public static void main(String[] args) {
    hr.restart.util.startFrame.raLookAndFeel();//
    frmCalcPorez f;
    if (args !=null) {
      ArrayList arrl = new ArrayList();
      for (int i=0; i<args.length; i++) {
        System.out.println("args["+i+"] = "+args[i]);
        try {
          Object maybegetter = Class.forName(args[i]).newInstance();
          if (maybegetter instanceof raCalcPorezDataGetter) {
            arrl.add(maybegetter);
          }
        } catch (Exception ex) {
          ex.printStackTrace();
        }
      }
      for (Iterator x = arrl.iterator(); x.hasNext(); ) {
        Object item = x.next();
        System.out.println("item["+x+"] = "+item);
      }
      raCalcPorezDataGetter[] getters = (raCalcPorezDataGetter[])arrl.toArray(new raCalcPorezDataGetter[arrl.size()]);
      f = new frmCalcPorez(getters);
    } else {
      f = new frmCalcPorez();
    }
/*
    String arr = "{";
    System.out.println("public String getValue(String identifier) {");
    for (Iterator i = new TreeSet(f.binder.props.keySet()).iterator(); i.hasNext(); ) {
      Object item = i.next();
      arr = arr+"\""+item.toString()+"\", ";
      System.out.println("if (identifier.equals(\""+item.toString()+"\")) {\n\n}");
    }
    System.out.println("}");
//    System.out.println(arr);
 */
    f.pack();
    f.show();
  }

}
