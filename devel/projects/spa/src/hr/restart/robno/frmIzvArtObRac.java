/****license*****************************************************************
**   file: frmIzvArtObRac.java
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
 * Created on 2004.08.26
 */
package hr.restart.robno;

import hr.restart.swing.JraButton;
import hr.restart.util.JlrNavField;
import hr.restart.zapod.OrgStr;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;
import com.borland.jbcl.layout.XYConstraints;

/**
 * @author Administrator
 */
public class frmIzvArtObRac extends frmIzvArt {
    JlrNavField jlrCorg = new JlrNavField() {
        public void after_lookUp() {
            if (!jlrCorg.getText().equals(""))
                enabCorg(false);
        }
    };

    JlrNavField jlrNaziv = new JlrNavField();

    JraButton jbSelCorg = new JraButton();

    StorageDataSet fieldSet = new StorageDataSet();

    JLabel jlCorg = new JLabel("Org. jedinica");

    JLabel jlSifra = new JLabel();

    JLabel jlNaziv = new JLabel();


    public static frmIzvArt getInstanceObRac() {
      return newInstance;
    }
    
    static frmIzvArtObRac newInstance = null;

    public frmIzvArtObRac() {
      super('V');
      newInstance = this;
        try {
            jbInit();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected void enabCorg(boolean yesno) {
        if (yesno) {
            jlrCorg.setText("");
            jlrNaziv.setText("");
        }
        rcc.setLabelLaF(jlrCorg, yesno);
        rcc.setLabelLaF(jlrNaziv, yesno);
        rcc.setLabelLaF(jbSelCorg, yesno);
    }

    public void componentShow() {
        fieldSet.setString("CORG", OrgStr.getKNJCORG());
        jlrCorg.forceFocLost();
        rpcskl.Clear();
        rcc.EnabDisabAll(rpcskl,true);
        rpcskl.jrfCSKL.requestFocus();

        rpcskl.jrfCSKL.setRaDataSet(dm.getSklad());
        rpcart.setDefParam();
        tds.setTimestamp("pocDatum", hr.restart.util.Valid.getValid().findDate(false,0));
        tds.setTimestamp("zavDatum", hr.restart.util.Valid.getValid().findDate(false,0));
        if(!tds2.isOpen())
          tds2.open();
        tds2.setString("VRDOK","");
        rcbVrDok.setSelectedIndex(0);
        rcbVrArt.setSelectedIndex(0);
    }

    public boolean runFirstESC() {
        if (!jlrCorg.getText().equals("") || !rpcart.jrfCART.isEnabled())
            return true;
        return false;
    }

    public void firstESC() {
        rcc.EnabDisabAll(jp, true);
        if (okprest){
          rcc.setLabelLaF(jlrCorg, false);
          rcc.setLabelLaF(jlrNaziv, false);
          rcc.setLabelLaF(jbSelCorg, false);
          rpcskl.jrfCSKL.requestFocus();
          okprest = false;
        } else if (!rpcart.jrfCART.getText().equals("") || !rpcart.jrfCART.isEnabled()) {
            rcc.EnabDisabAll(rpcart, true);
            rpcart.setCART();
        } else if (!rpcskl.getCSKL().equalsIgnoreCase("")) {
          rcc.EnabDisabAll(rpcskl, true);
          rpcskl.Clear();
//          jraDatumOd.requestFocus();
          rpcskl.jrfCSKL.requestFocus();
        } else if (!jlrCorg.getText().equals("")) {
            rcc.setLabelLaF(jlrCorg, true);
            rcc.setLabelLaF(jlrNaziv, true);
            rcc.setLabelLaF(jbSelCorg, true);
            //        rcc.EnabDisabAll(rpcskl, true);
            jlrCorg.setText("");
            jlrCorg.forceFocLost();
            //        jraDatumOd.requestFocus();
            jlrCorg.requestFocus();
        }
    }

    private void jbInit() throws Exception {

      xYLayout1.setHeight(250);

        jlNaziv.setHorizontalAlignment(SwingConstants.LEFT);
        jlNaziv.setText("Naziv");

        jlSifra.setHorizontalAlignment(SwingConstants.LEFT);
        jlSifra.setText("Šifra");

        fieldSet.setColumns(new Column[] { hr.restart.baza.dM.getDataModule()
                .createStringColumn("CORG", "Corg", 13) });

        jp.remove(rpcskl);
        jlrCorg.setColumnName("CORG");
        jlrCorg.setDataSet(fieldSet);
        jlrCorg.setColNames(new String[] { "NAZIV" });
        jlrCorg.setTextFields(new javax.swing.text.JTextComponent[] { jlrNaziv });
        jlrCorg.setVisCols(new int[] { 0, 1 });
        jlrCorg.setSearchMode(0);
        jlrCorg.setRaDataSet(hr.restart.zapod.OrgStr.getOrgStr()
                .getOrgstrAndCurrKnjig());
        jlrCorg.setNavButton(jbSelCorg);

        jlrNaziv.setColumnName("NAZIV");
        jlrNaziv.setNavProperties(jlrCorg);
        jlrNaziv.setSearchMode(1);

        rcbVrDok.setRaItems(new String[][] { { "Svi dokumenti", "" },
                { "RAC - Bezgotovinski raèuni", "RAC" },
                { "GRN - Gotovinski Raèuni", "GRN" },
                { "TER - Tereæenja", "TER" }, { "ODB - Odobrenja", "ODB" } });

        jp.add(jlSifra, new XYConstraints(150, 8, 100, -1));
        jp.add(jlNaziv, new XYConstraints(255, 8, 260, -1));
        jp.add(jlCorg, new XYConstraints(15, 25, -1, -1));
        jp.add(jlrCorg, new XYConstraints(150, 25, 100, -1));
        jp.add(jlrNaziv, new XYConstraints(255, 25, 348, -1)); // 260
        jp.add(jbSelCorg, new XYConstraints(260 + 348, 25, 21, 21)); // 519
        
        jp.add(rpcskl,    new XYConstraints(0, 50, 655, -1));
        

        jp.add(rcbVrDok,               new XYConstraints(454, 100, 150, 20));
        jp.add(rcbVrArt,      new XYConstraints(454, 125, 150, 20));
        
        
        jp.add(jraDatumOd,     new XYConstraints(150, 100, 100, -1));
        jp.add(jraDatumDo,     new XYConstraints(255, 100, 100, -1));
        jp.add(jlDatum,   new XYConstraints(15, 100, -1, -1));
        jp.add(rpcart,     new XYConstraints(0, 150, 655, 75));
        jp.add(jlVrDok,         new XYConstraints(365, 102, 70, -1));
        jp.add(jlVrArt,      new XYConstraints(365, 127, 70, -1));

    }

    public boolean isIspis() {
        return false;
    }

    public boolean ispisNow() {
        return true;
    }
    private boolean okprest;

    public void okPress() {
        System.out.println("vrstadok " + vrstaDok);
        rcc.EnabDisabAll(rpcart, false);
        try {
            if (preparePrint() == 0) {
                setNoDataAndReturnImmediately();
                rpcart.setCART();
                rcc.EnabDisabAll(rpcart, true);
            }
            
            reporting();
            
        } catch (Exception ex) {
            setNoDataAndReturnImmediately();
            ex.printStackTrace();
        }
        okprest=true;
    }

    protected int preparePrint() {
      vrdoks = "obrada";
        qds.close();
        //      qds2.close();
        qds3.close();

        datOd = tds.getTimestamp("pocDatum");
        datDo = tds.getTimestamp("zavDatum");
        String uvjet = "";

        if (!rpcart.findCART(podgrupe).equals("")) {
            uvjet = "AND " + rpcart.findCART(podgrupe);
        }

        /* "AND "+rpcart.findCART(podgrupe); */
        
        
        
        String qStr = rdUtil.getUtil().getIzArt(fieldSet.getString("CORG"),
                rpcart.jrfCART.getText(),
                utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
                utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok,
                vrstaArt, uvjet, "OBRAC",rpcskl.getCSKL());
        
        
        //---------------------------------------------------------------------------------
        qds.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr));

        //      String qStr2 = rdUtil.getUtil().getIzArt2(rpcskl.jrfCSKL.getText(),
        // rpcart.jrfCART.getText(), utRobno.getTimestampValue(datOd,
        // utRobno.NUM_FIRST),
        //          utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok,
        // vrstaArt, uvjet,"OBRAC");
        //---------------------------------------------------------------------------------
        //      qds2.setQuery(new QueryDescriptor(dm.getDatabase1(),qStr2));

        String qStr3 = rdUtil.getUtil().getIzArt3(fieldSet.getString("CORG"),
                rpcart.jrfCART.getText(),
                utRobno.getTimestampValue(datOd, utRobno.NUM_FIRST),
                utRobno.getTimestampValue(datDo, utRobno.NUM_LAST), vrstaDok,
                vrstaArt, uvjet, "OBRAC",rpcskl.getCSKL());
        //---------------------------------------------------------------------------------
        qds3.setQuery(new QueryDescriptor(dm.getDatabase1(), qStr3));

        qds.open();
        //      qds2.open();
        qds3.open();

        return qds.getRowCount();
    }

    protected void reset() {
        rcc.EnabDisabAll(jp, true);
        if (!rpcart.jrfCART.getText().equals("")) {
            jlrCorg.forceFocLost();
        } else {
            rcc.setLabelLaF(jlrCorg, true);
            rcc.setLabelLaF(jlrNaziv, true);
            rcc.setLabelLaF(jbSelCorg, true);
        }
        OKEsc = false;
        jp.repaint();
    }

    public boolean Validacija() {

      if (!rpcart.getCGRART().equals("") && (rpcart.getCART().equals("") && rpcart.getCART1().equals(""))){
        int grupe = JOptionPane.showConfirmDialog(this.jp,"Ukljuèiti i podgrupe?","Grupe artikala",JOptionPane.YES_NO_OPTION);
        if (grupe == JOptionPane.CANCEL_OPTION) return false;
        if (grupe == JOptionPane.NO_OPTION) {podgrupe = false;}
        else {podgrupe = true;}
      }

      vrstaDok = tds2.getString("VRDOK");
      vrstaArt = tds2.getString("VRART");
//      if(rpcskl.jrfCSKL.getText().equals("")) {
//        rpcskl.jrfCSKL.requestFocus();
//        JOptionPane.showConfirmDialog(this.jp,"Obavezan unos skladišta !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//        return false;
//      }
      if (tds.getTimestamp("zavDatum").after(hr.restart.util.Valid.getValid().findDate(false, 0))) {
        jraDatumDo.requestFocus();
        JOptionPane.showConfirmDialog(this.jp,"Završni datum ve\u0107i od današnjeg !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
      if (tds.getTimestamp("zavDatum").before(tds.getTimestamp("pocDatum"))) {
        jraDatumOd.requestFocus();
        JOptionPane.showConfirmDialog(this.jp,"Po\u010Detni datum mora biti manji od završnog !","Greška",JOptionPane.DEFAULT_OPTION,JOptionPane.ERROR_MESSAGE);
        return false;
      }
//      rcc.EnabDisabAll(rpcart, false);
//      try {
//        if(preparePrint()==0) {
//          rpcart.setCART();
//          rcc.EnabDisabAll(rpcart, true);
//          JOptionPane.showConfirmDialog(this.jp,"Nema podataka koji zadovoljavaju traženi uvjet !","Upozorenje",JOptionPane.DEFAULT_OPTION,JOptionPane.WARNING_MESSAGE);
//          return false;
//        }
//      } catch (Exception ex) {
//        ex.printStackTrace();
//      }
      return true;
    }
    
//    
//          //
//    @####[S]XXXXX:::mmmmmmmmmmmmm>
//          \\
//    
    
    public String getCSKLART(){
      return rpcskl.getCSKL();
    }
    
    public boolean notKnjig(){
      return false;
    }
    
    public String getVrsta(){
      return vrdoks;
    }

}