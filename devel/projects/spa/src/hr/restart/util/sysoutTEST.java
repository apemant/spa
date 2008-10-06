/****license*****************************************************************
**   file: sysoutTEST.java
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
package hr.restart.util;



/**

 * Title: sysoutTEST

 * Description:

 *          * Ispisuje konkatinacije razlicitih logova u jBuilderov project tab

 *          * koristi klase System.out.print i System.out.println

 *          * metode su sysoutTEST.prn(par1,par2) gdje su par1 i(ili) par2

 *            kombinacije ulaznih objekata koje klasa ispisuje.

 *            Ako neka kombinacija fali, slobodno si je dodaj :-)

 *          * KONSTRUKTOR sadrzi boolean varijablu 'jelSuti'

 *            ako je ona true - nista se ne ispisuje tako da ne iritira pri izvodjenju

 *          * vidi primjer koristenja u ColumnsBean.java

 * Company: RA

 * @author AI

 * @version 1.0

 */

import java.lang.reflect.Field;
import java.lang.reflect.Method;



public class sysoutTEST {

  boolean silentMode;

  public sysoutTEST(boolean jelSuti) {

    silentMode = jelSuti;

  }

  public void setSilentMode(boolean newSilentMode) {

    silentMode = newSilentMode;

  }

  public void prn(java.lang.Object par1, int par2) {

    if (!silentMode) {

      System.out.print(par1);

      System.out.println(par2);

    }

  }

  public void prn(java.lang.Object par1, long par2) {

    if (!silentMode) {

      System.out.print(par1);

      System.out.println(par2);

    }

  }



  public void prn(java.lang.Object par1, java.lang.Object par2) {

    if (!silentMode) {

      System.out.print(par1);

      System.out.println(par2);

    }

  }



  public void prn(java.lang.Object par1, boolean par2) {

    if (!silentMode) {

      System.out.print(par1);

      System.out.println(par2);

    }

  }



  public void prn(java.lang.Object par1) {

    if (!silentMode)

      System.out.println(par1);

  }



  public void prn(java.lang.Object[] par1) {

    if (!silentMode) {

      if (par1 == null) {

        System.out.println("null");

        return;

      }

//      System.out.println(par1.getClass().toString());

      System.out.println("========================================");

      for (int i=0;i<par1.length;i++) {

        System.out.print("Member ");

        System.out.print(i);

        System.out.print("->:");

        System.out.println(par1[i]);

      }

    }

  }

  public void prn(int[] par1) {

    if (!silentMode) {

//      System.out.println(par1.getClass().toString());

      System.out.println("========================================");

      for (int i=0;i<par1.length;i++) {

        System.out.print("Member ");

        System.out.print(i);

        System.out.print("->:");

        System.out.println(par1[i]);

      }

    }

  }

  public void prn(java.util.Enumeration _enum) {

    if (!silentMode) {

//      System.out.println(par1.getClass().toString());

      System.out.println("========================================");

      int i=0;

      while (_enum.hasMoreElements()) {

        Object obj = _enum.nextElement();

        System.out.print("Member ");

        System.out.print(i);

        System.out.print("->:");

        System.out.println(obj.toString());

        i++;

      }

    }

  }

  public void prnc(com.borland.dx.dataset.DataSet rr) {

    if (silentMode) return;

    com.borland.dx.dataset.Variant vvar = new com.borland.dx.dataset.Variant();

    for (int i=0;i<rr.getColumnCount();i++) {

      rr.getVariant(i,vvar);

      this.prn(rr.getColumn(i).getServerColumnName()+" = "+vvar.toString());

    }

  }



  public void prn(com.borland.dx.dataset.DataSet ds) {

    if (silentMode) return;

    hr.restart.util.lookupData LD = hr.restart.util.lookupData.getlookupData();

    System.out.println("====================================================================");

    for (int x=0;x<ds.columnCount();x++) {

      System.out.print(ds.getColumn(x).getColumnName());

      System.out.print(" -o- ");

    }

    System.out.println();

    System.out.println("====================================================================");

    int i=0;

    for (i=0;i<ds.rowCount();i++) {

      for (int j=0;j<ds.columnCount();j++) {

        try {

          System.out.print(LD.getCurrRowColValue(ds,ds.getColumn(j).getColumnName(),i));

        } catch (Exception e) {System.out.print("null");}

        System.out.print(" -o- ");

      }

      System.out.println();

    }

    System.out.println(">>EOF<<");

  }



  public void showMembers(Object obj) {

    if (silentMode) return;

    System.out.println("-------------------------");

    System.out.println("       F I E L D S");

    System.out.println("-------------------------");

    Field[] flds = obj.getClass().getFields();

    for (int i=0; i<flds.length; i++) {

      try {

        System.out.println("Field_"+i+" :: "+flds[i].getName()+" = "+flds[i].get(obj));

      }

      catch (Exception ex) {

        System.out.println("showMembers exc = "+ex);

      }

    }

    System.out.println("-------------------------");

    System.out.println("     M E T H O D S       ");

    System.out.println("-------------------------");

    Method[] mths = obj.getClass().getMethods();

    for (int i=0; i<mths.length; i++) {

      try {

        Class[] cpars = mths[i].getParameterTypes();

        Object[] params = new Object[cpars.length];

        if (params.length==0) params = null;

        else {

          for (int j = 0; j < params.length; j++) {

            params[j] = cpars[j].newInstance();

          }

        }

        System.out.println("Method_"+i+" :: "+mths[i].getName()+" = "+mths[i].invoke(obj,params));

      }

      catch (Exception ex) {

        System.out.println("Method_"+i+" :: "+mths[i].getName()+" = !exception!::"+ex);

//        System.out.println("showMembers exc = "+ex);

//        ex.printStackTrace();

      }

    }



  }

  public void showInFrame(com.borland.dx.dataset.DataSet ds,String title) {

    if (silentMode) return;

    prn(ds);

    if (ds instanceof com.borland.dx.dataset.StorageDataSet) {

      com.borland.dx.dataset.StorageDataSet dss = (com.borland.dx.dataset.StorageDataSet)ds;

      javax.swing.JDialog d = new hr.restart.swing.JraDialog();

      d.setModal(true);

      raJPTableView jp = new raJPTableView();

      jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

      jp.setDataSet(dss);

      jp.getColumnsBean().initialize();

      d.getContentPane().add(jp,java.awt.BorderLayout.CENTER);

      startFrame.getStartFrame().centerFrame(d,0,title);

      d.show();

    }

//    lookupFrame lf = lookupFrame.getLookupFrame((java.awt.Frame)null,ds,null);

//    lf.jdbTable1.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);

//    lf.ShowCenter();

  }

}

