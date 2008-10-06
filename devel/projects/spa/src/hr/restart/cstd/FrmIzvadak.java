/****license*****************************************************************
**   file: FrmIzvadak.java
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

import hr.restart.baza.dM;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raDateRange;
import hr.restart.util.JlrNavField;
import hr.restart.util.Util;
import hr.restart.util.raUpitFat;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.TableDataSet;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

public class FrmIzvadak extends raUpitFat {

    hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
    hr.restart.util.Valid vl = hr.restart.util.Valid.getValid();
    JPanel jp = new JPanel();
    TableDataSet tds = new TableDataSet();
    TableDataSet datads = new TableDataSet();
    JraTextField jtfZavDatum = new JraTextField();
    JraTextField jtfPocDatum = new JraTextField();
    JlrNavField jrtfCTG = new JlrNavField();
    JraButton jrtfCTGB = new JraButton();
    JlrNavField jrtfNaziv = new JlrNavField();
    JlrNavField jrtfCPAR = new JlrNavField();
    JlrNavField jrtfNAZIVPAR = new JlrNavField();
    JraButton jrtfCPARB = new JraButton();
     
    
    public FrmIzvadak(){
        setupJPanel();
        setupDB();
    }

    public void setupDB(){
        Column kolul = dm.getStdoki().getColumn("KOL").cloneColumn();
        kolul.setColumnName("KOLUL");
        kolul.setCaption("Kol. ulaz");
        Column koliz = dm.getStdoki().getColumn("KOL").cloneColumn();
        koliz.setColumnName("KOLIZ");
        koliz.setCaption("Kol. izlaz");
        Column kolsum = dm.getStdoki().getColumn("KOL").cloneColumn();
        kolsum.setColumnName("KOLSUM");
        kolsum.setCaption("Kol. stanje");
        Column vul = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
        vul.setColumnName("VUL");
        vul.setCaption("Vrijednost ulaz");
        Column viz = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
        viz.setColumnName("VIZ");
        viz.setCaption("Vrijednost izlaz");
        Column vst = dm.getStdoki().getColumn("IPRODSP").cloneColumn();
        vst.setColumnName("VST");
        vst.setCaption("Vrijednost stanje");
        
        datads.setColumns(new Column[] {
                dM.createStringColumn("jcd", "Broj JCD - a", 12),
                dM.createTimestampColumn("datum", "Datum JCD-a"),
                dM.createStringColumn("bkz", "Broj knjig. zapisa", 12),
                kolul,koliz,kolsum,vul,viz,vst
        });
        datads.open();
    }
    
    public void setupJPanel(){


//        jp.setPreferredSize(new Dimension(555, 45));
        XYLayout xy = new XYLayout();
        xy.setHeight(85);
        xy.setWidth(630);
        jp.setLayout(xy);
        JLabel jl = new JLabel("Datum (od-do)");
        JLabel tb = new JLabel("Tarifni broj");
        JLabel princ = new JLabel("Principal");
        
        jrtfCPAR.setColumnName("CPAR");
        jrtfCPAR.setColNames(new String[] { "NAZPAR" });
        jrtfCPAR.setVisCols(new int[] { 0, 1, 2 });
        jrtfCPAR.setTextFields(new javax.swing.text.JTextComponent[] { jrtfNAZIVPAR });
        jrtfCPAR.setRaDataSet(dm.getPartneri());
        jrtfNAZIVPAR.setColumnName("NAZPAR");
        jrtfNAZIVPAR.setSearchMode(1);
        jrtfNAZIVPAR.setNavProperties(jrtfCPAR);
        jrtfCPARB.setText("...");
        jrtfCPAR.setNavButton(jrtfCPARB);
        
        jrtfCTG.setNavColumnName("CTG");
        jrtfCTG.setRaDataSet(dm.getCarTG());
        jrtfCTG.setColumnName("CTG");
        jrtfCTG.setColNames(new String[] { "NAZIV" });
        jrtfCTG.setTextFields(new javax.swing.text.JTextComponent[] { jrtfNaziv });
        jrtfNaziv.setColumnName("NAZIV");
        jrtfNaziv.setSearchMode(1);
        jrtfNaziv.setNavProperties(jrtfCTG);
        jrtfCTG.setVisCols(new int[] { 0, 1 });
        jrtfCTGB.setText("...");
        jrtfCTG.setNavButton(jrtfCTGB);

        tds.setColumns(new Column[] {
                       dM.createTimestampColumn("pocDatum", "Po\u010Detni datum"),
                       dM.createTimestampColumn("zavDatum", "Završni datum"),
                       dM.createStringColumn("cskl", "Skladište", 12)
        });

        jtfPocDatum.setColumnName("pocDatum");
        jtfPocDatum.setDataSet(tds);
        jtfPocDatum.setText("jraTextField1");
        jtfPocDatum.setHorizontalAlignment(SwingConstants.CENTER);
        jtfZavDatum.setDataSet(tds);
        jtfZavDatum.setColumnName("zavDatum");
        jtfZavDatum.setText("");
        jtfZavDatum.setHorizontalAlignment(SwingConstants.CENTER);
        new raDateRange(jtfPocDatum, jtfZavDatum);
        
//        jp.add(tb,     new XYConstraints(15, 5, -1, -1));
//        jp.add(jrtfCTG, new XYConstraints(150, 5, 100, -1));
//        jp.add(jrtfNaziv, new XYConstraints(254, 5,  327, -1));
//        jp.add(jrtfCTGB, new XYConstraints(584, 5, 21, 21));
//
        jp.add(princ,     new XYConstraints(15, 15, -1, -1));
        jp.add(jrtfCPAR, new XYConstraints(150, 15, 100, -1));
        jp.add(jrtfNAZIVPAR, new XYConstraints(254, 15, 327, -1));
        jp.add(jrtfCPARB, new XYConstraints(584, 15, 21, 21));
/*
        jp.add(jl,     new XYConstraints(15, 55, -1, -1));
        jp.add(jtfPocDatum, new XYConstraints(150, 55, 100, -1));
        jp.add(jtfZavDatum, new XYConstraints(255, 55, 100, -1));
        */
        jp.add(jl,     new XYConstraints(15, 40, -1, -1));
        jp.add(jtfPocDatum, new XYConstraints(150, 40, 100, -1));
        jp.add(jtfZavDatum, new XYConstraints(255, 40, 100, -1));
        
        this.setJPan(jp);
    
    }
    

    public String navDoubleClickActionName() {
        return null;
    }

    public int[] navVisibleColumns() {
        return null;
    }

    public void okPress() {
        
        
        QueryDataSet carulaz = Util.getNewQueryDataSet(
                "select * from Carulaz,Carulazst "+
        		"WHERE carulazst.id_ulaz_zag = carulaz.id_ulaz_zag");
        QueryDataSet carizlaz = Util.getNewQueryDataSet("");
        
        setDataSetAndSums(
                datads , new String[] {"KOLUL","KOLIZ","VUL","VIZ"});
        
    }

    public boolean runFirstESC() {
        return false;
    }

    public void firstESC() {
    }

    public void componentShow() {
    }

 
}
