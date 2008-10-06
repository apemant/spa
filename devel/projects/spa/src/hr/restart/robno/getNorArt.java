/****license*****************************************************************
**   file: getNorArt.java
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
import java.awt.Toolkit;

import com.borland.dx.dataset.Column;
import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.dx.sql.dataset.QueryDescriptor;

public class getNorArt {

  private QueryDataSet NorArt = new QueryDataSet();
  private QueryDataSet Sastojak = new QueryDataSet();
  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private allSelect aSS = new allSelect();
  private mygeter mygnorma = new mygeter("Normirani artikli"){
    public void jdodbutton_actionPerformed(java.awt.event.ActionEvent e) {
      presdbatn();
    }
    public void pressOK(){
      myPressOK();
      super.pressOK();
    }
  };

  private int[] cols;
  private int[] colsn;

  public getNorArt() {
    cols = new int [] {Aut.getAut().getCARTdependable(0,1,2),3,4};
    colsn = new int []{Aut.getAut().getCARTdependable(1,2,3),4,5,6};
    setupSets();
    findAllNormArtikl();
    mygnorma.init(NorArt,cols);
    mygnorma.addButton("Sastav","");
  }
  public void setupSets(){
    for (int i = 0 ;i< dm.getArtikli().getColumns().length; i++){
      NorArt.addColumn((Column) dm.getArtikli().getColumn(i).clone());
    }
    for (int i = 0 ;i< dm.getNorme().getColumns().length; i++){
      Sastojak.addColumn((Column)dm.getNorme().getColumn(i).clone());
    }
  }
  public void findAllSastojak(){
      if (Sastojak.isOpen()) {
        Sastojak.close();
        Sastojak.closeStatement();
      }
      Sastojak.setQuery(new QueryDescriptor(dm.getDatabase1(),
            aSS.getQuery4Sastojak(NorArt.getInt("CART"))));
      Sastojak.open();
  }

  public void findAllNormArtikl() {

      if (NorArt.isOpen()) {
        NorArt.close();
        NorArt.closeStatement();
      }
      NorArt.setQuery(new QueryDescriptor(dm.getDatabase1(),
            aSS.getQuery4AllNorArt()));
      NorArt.open();
  }
  public QueryDataSet getSastojak() {
    return Sastojak;
  }

  public void show(){
    mygnorma.pack();
    mygnorma.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().width-2*mygnorma.getSize().getWidth())/2),30);
    mygnorma.setEnabledChildFrames(false);
    mygnorma.show();
  }
  public void presdbatn(){
    findAllSastojak();
    mygeter mygartikl = new mygeter("Normativ artikla"){
      public void pressOK(){
        mygnorma.setEnabled(true);
        this.hide();
      }
      public void pressCancel(){
        mygnorma.setEnabled(true);
        this.hide();
      }
    };
    mygnorma.setEnabled(false);
    mygartikl.init(Sastojak,colsn);
    mygartikl.pack();
    mygartikl.setLocation((int) ((Toolkit.getDefaultToolkit().getScreenSize().width-2*mygnorma.getSize().getWidth())/2
                                  + mygnorma.getSize().getWidth()) ,30);
    mygartikl.setEnabledChildFrames(false);
    mygartikl.show();
  }
  public void myPressOK(){
  }
}