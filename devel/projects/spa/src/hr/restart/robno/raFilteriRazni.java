/****license*****************************************************************
**   file: raFilteriRazni.java
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

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public class raFilteriRazni {

  private fVshrab_rab fVT = new fVshrab_rab();
  private fVtrabat fVTrab = new fVtrabat();

  public raFilteriRazni() {
  }

  public fVshrab_rab getfVshrab_rab() {
    return fVT;
  }
  public fVtrabat getfVtrabat() {
    return fVTrab;
  }

  class fVshrab_rab implements com.borland.dx.dataset.RowFilterListener {
    private String usporedba;

    public void setCriteria(String crit){
      usporedba=crit;
    }

    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }

    boolean isRow(com.borland.dx.dataset.ReadRow row) {
      if (row.getString("CSHRAB").equals(usporedba)) return true;
      return false;
    }
  }

  class fVtrabat implements com.borland.dx.dataset.RowFilterListener {
    private String cskl;
    private String vrdok;
    private String god;
    private int brdok;
    private short rbr;

    public void setCriteria(String skladiste,String vrstadokumenta,String godina,int brojdok,short rbroj){
      cskl = skladiste;
      vrdok= vrstadokumenta;
      god=godina;
      brdok=brojdok;
      rbr=rbroj;
    }

    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }
    private boolean isCSKL(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("CSKL").equals(cskl));
    }
    private boolean isVRDOK(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("VRDOK").equals(vrdok));
    }
    private boolean isGOD(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("GOD").equals(god));
    }
    private boolean isBRDOK(com.borland.dx.dataset.ReadRow row) {
      return (row.getInt("BRDOK")==brdok);
    }
    private boolean isRBR(com.borland.dx.dataset.ReadRow row) {
      //return (row.getShort("RBR")==brdok);
      return (row.getShort("RBR")==rbr);
    }

    boolean isRow(com.borland.dx.dataset.ReadRow row) {
      return (isCSKL(row)&&isVRDOK(row)&&isGOD(row)&&isBRDOK(row)&&isRBR(row));
    }
  }


  //-> Rade - zavsni troskovi
  private fVshztr_ztr fVT1 = new fVshztr_ztr();
  private fVtzavtr fVTzt = new fVtzavtr();

  public fVshztr_ztr getfVshztr_ztr() {
    return fVT1;
  }
  public fVtzavtr getfVtzavtr() {
    return fVTzt;
  }

  class fVshztr_ztr implements com.borland.dx.dataset.RowFilterListener {
    private String usporedba;

    public void setCriteria(String crit){
      usporedba=crit;
    }

    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }

    boolean isRow(com.borland.dx.dataset.ReadRow row) {
      if (row.getString("CSHZT").equals(usporedba)) return true;
      return false;
    }
  }

  class fVtzavtr implements com.borland.dx.dataset.RowFilterListener {
    private String cskl;
    private String vrdok;
    private String god;
    private int brdok;
    private short rbr;

    public void setCriteria(String skladiste,String vrstadokumenta,String godina,int brojdok,short rbroj){
      cskl = skladiste;
      vrdok= vrstadokumenta;
      god=godina;
      brdok=brojdok;
      rbr=rbroj;
    }

    public void filterRow(com.borland.dx.dataset.ReadRow row, com.borland.dx.dataset.RowFilterResponse resp) {
      if (isRow(row)) {
        resp.add();
      } else {
        resp.ignore();
      }
    }
    private boolean isCSKL(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("CSKL").equals(cskl));
    }
    private boolean isVRDOK(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("VRDOK").equals(vrdok));
    }
    private boolean isGOD(com.borland.dx.dataset.ReadRow row) {
      return (row.getString("GOD").equals(god));
    }
    private boolean isBRDOK(com.borland.dx.dataset.ReadRow row) {
      return (row.getInt("BRDOK")==brdok);
    }
    private boolean isRBR(com.borland.dx.dataset.ReadRow row) {
      //return (row.getShort("RBR")==brdok);
      return (row.getShort("RBR")==rbr);
    }

    boolean isRow(com.borland.dx.dataset.ReadRow row) {
      return (isCSKL(row)&&isVRDOK(row)&&isGOD(row)&&isBRDOK(row)&&isRBR(row));
    }
  }

}