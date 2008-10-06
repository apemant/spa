/****license*****************************************************************
**   file: raPrepDelStatments.java
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

import hr.restart.util.raLocalTransaction;
import hr.restart.util.raTransaction;

import java.sql.PreparedStatement;
import java.sql.SQLException;
/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2002</p>
 * <p>Company: </p>
 * @author unascribed
 * @version 1.0
 */

abstract public class raPrepDelStatments {
/*
  final PreparedStatement delCjenik = raTransaction.getPreparedStatement
                                      ("delete from cjenik where cskl = (?)");
*/
// vtztr  cskl,god,vrdok
  final PreparedStatement delVTZTR = raTransaction.getPreparedStatement
                                      ("delete from vtztr where cskl = ? and god = ? ");

//VTZtrt  cskl,god,vrdok
  final PreparedStatement delVTZtrt = raTransaction.getPreparedStatement
                                      ("delete from VTZtrt where cskl = ? and god = ? ");
//  vtrabat
  final PreparedStatement delVTrabat = raTransaction.getPreparedStatement
                                      ("delete from vtrabat where cskl = ? and god = ? ");
  // vtzavtr
  final PreparedStatement delVTzavtr = raTransaction.getPreparedStatement
                                      ("delete from vtzavtr where cskl = ? and god = ? ");


  final PreparedStatement delDoki = raTransaction.getPreparedStatement
                                      ("delete from doki where cskl = ? and god = ? "+
                                       "and vrdok not in ('PRD','RAC','GRN','TER','ODB')");

  final PreparedStatement delDokiOJ = raTransaction.getPreparedStatement
                                      ("delete from doki where cskl = ? and god = ? "+
                                       "and vrdok in ('PRD','RAC','GRN','TER','ODB')");

  final PreparedStatement delDoku = raTransaction.getPreparedStatement
                                    ("delete from doku where cskl = ? and god = ?");

  final PreparedStatement delMesklaUl = raTransaction.getPreparedStatement
                                    ("delete from meskla where csklul = ? and god = ?");

  final PreparedStatement delMesklaIz = raTransaction.getPreparedStatement
                                    ("delete from meskla where cskliz = ? and god = ?");

  final PreparedStatement delInventura = raTransaction.getPreparedStatement
                                         ("delete from inventura where cskl = ?");

  final PreparedStatement delPos = raTransaction.getPreparedStatement
                                    ("delete from pos where cskl = ?");

  final PreparedStatement delRate = raTransaction.getPreparedStatement
                                    ("delete from rate where cskl = ?");

  final PreparedStatement delRn = raTransaction.getPreparedStatement
                                    ("delete from RN where cskl = ? and god = ? ");

  final PreparedStatement delStanje = raTransaction.getPreparedStatement
                                      ("delete from stanje where cskl = ? and god = ? ");

  final PreparedStatement delStdoki = raTransaction.getPreparedStatement
                                      ("delete from stdoki where cskl = ? and god= ? "+
                                       "and vrdok not in ('PRD','RAC','GRN','TER','ODB','RNL')");

  final PreparedStatement delStdokiOJ = raTransaction.getPreparedStatement
                                      ("delete from stdoki where cskl = ? and god= ? and "+
                                       "vrdok in ('PRD','RAC','GRN','TER','ODB')");

  final PreparedStatement delStdokiRN = raTransaction.getPreparedStatement
                                      ("delete from stdoki where cskl = ? and god= ? and "+
                                       "vrdok in ('RNL')");

  final PreparedStatement delStdoku = raTransaction.getPreparedStatement
                                      ("delete from stdoku where cskl = ? and god= ?");

  final PreparedStatement delStmesklaUL = raTransaction.getPreparedStatement
                                      ("delete from stmeskla where csklul = ? and god= ?");

  final PreparedStatement delStmesklaIZ = raTransaction.getPreparedStatement
                                      ("delete from stmeskla where cskliz = ? and god= ?");

  final PreparedStatement delStpos = raTransaction.getPreparedStatement
                                     ("delete from stpos where cskl = ?");

  final PreparedStatement delVtrabat = raTransaction.getPreparedStatement
                                       ("delete from vtrabat where cskl = ?");

  final PreparedStatement delVtzavtr = raTransaction.getPreparedStatement
                                       ("delete from vtzavtr where cskl = ?");

  final PreparedStatement delSeq = raTransaction.getPreparedStatement
                                       ("delete from seq where opis like ?");

  final PreparedStatement delVTTEXT = raTransaction.getPreparedStatement
                                      ("delete from vttext where ckey like ?");

  final PreparedStatement delVTprijenos1 = raTransaction.getPreparedStatement
                                      ("delete from vtprijenos where keysrc like ?");

  final PreparedStatement delVTprijenos2 = raTransaction.getPreparedStatement
                                      ("delete from vtprijenos where keydest like ?");



  final String[] araj_docs = TypeDoc.araj_docs;
  final String[] araj_docsOJ = TypeDoc.araj_docsOJ;

  abstract public void  execute_plus();

  public boolean del_obrada_prometOJ_outTransaction(final String cskl, final String godina) throws Exception {

    boolean returnValue = true;

    try {
      delDokiOJ.setString(1,cskl);
      delDokiOJ.setString(2,godina);
      delDokiOJ.execute();
      execute_plus();
    } catch (Exception ex) {
      System.out.println("greska u delDokiOJ");
      System.out.println(ex);
      returnValue = false;
    }

    if (returnValue) {
      try {
        delStdokiOJ.setString(1,cskl);
        delStdokiOJ.setString(2,godina);
        delStdokiOJ.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStdokiOJ");
        System.out.println(ex);
        returnValue = false;
      }
    }
// SEQ
    String myKey = "";
    if (returnValue) {
      for (int i = 0 ;i<araj_docsOJ.length;i++) {
        myKey = "%"+cskl+"%"+araj_docsOJ[i]+godina+"%";
        try {

          delSeq.setString(1,myKey);
          delSeq.execute();
//              execute_plus();
        }
        catch (Exception ex) {
          System.out.println("greska u delSeq (OJ)");
          System.out.println(ex);
          returnValue = false;
        }
        // delVTTEXT
        try {
          delVTTEXT.setString(1,myKey);
          delVTTEXT.execute();
        }
        catch (SQLException ex) {
          System.out.println("greska u delVTTEXT (OJ)");
          System.out.println(ex);
          returnValue = false;
        }
        try {
          delVTprijenos1.setString(1,myKey);
          delVTprijenos1.execute();
        }
        catch (SQLException ex) {
          System.out.println("greska u delVTprijenos1 (OJ)");
          System.out.println(ex);
          returnValue = false;
        }
        try {
          delVTprijenos2.setString(1,myKey);
          delVTprijenos2.execute();
        }
        catch (SQLException ex) {
          System.out.println("greska u delVTprijenos2 (OJ)");
          System.out.println(ex);
          returnValue = false;
        }
        if("D".equalsIgnoreCase((hr.restart.sisfun.frmParam.getParam("robno","brisNalog")))){
// brisNalog
          if (returnValue) {
            myKey = "%"+cskl+"%"+"RNL"+godina+"%";
            try {

              delSeq.setString(1,myKey);
              delSeq.execute();
//              execute_plus();
            }
            catch (Exception ex) {
              System.out.println("greska u delSeq (OJ)");
              System.out.println(ex);
              returnValue = false;
        }


            try {
              delRn.setString(1,cskl);
              delRn.setString(2,godina);
              delRn.execute();
              execute_plus();
            }
            catch (Exception ex) {
              System.out.println("greska u delRn");
              System.out.println(ex);
              returnValue = false;
            }
          }
          if (returnValue) {
            try {
              delStdokiRN .setString(1,cskl);
              delStdokiRN.setString(2,godina);
              delStdokiRN.execute();
              execute_plus();
            }
            catch (Exception ex) {
              System.out.println("greska u delStdokiRN");
              System.out.println(ex);
              returnValue = false;
            }
          }
        }
        if (!returnValue) break;
      }
    }
    return returnValue;
  }

  public boolean del_obrada_prometOJ(final String cskl, final String godina) {
    return new raLocalTransaction(){
      public boolean transaction() throws Exception {
        return del_obrada_prometOJ_outTransaction(cskl,godina);
        }}.execTransaction();
  }

  public boolean del_obrada_promet__outTransaction(final String cskl, final String godina) throws Exception  {
    boolean returnValue = true;
/*
    try {
      delCjenik.setString(1,cskl);
      delCjenik.execute();
      execute_plus();
    } catch (Exception ex) {
      System.out.println(ex);
      returnValue = false;
    }
*/
//    System.out.println("skl = "+cskl+" godina ="+ godina );

    if (returnValue) {
      try {
        delDoku.setString(1,cskl);
        delDoku.setString(2,godina);
        delDoku.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delDoku");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delStdoku.setString(1,cskl);
        delStdoku.setString(2,godina);
        delStdoku.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStdoku");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delDoki.setString(1,cskl);
        delDoki.setString(2,godina);
        delDoki.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delDoki");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delStdoki.setString(1,cskl);
        delStdoki.setString(2,godina);
        delStdoki.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStdoki");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delMesklaUl.setString(1,cskl);
        delMesklaUl.setString(2,godina);
        delMesklaUl.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delMesklaUl ----");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delStmesklaUL.setString(1,cskl);
        delStmesklaUL.setString(2,godina);
        delStmesklaUL.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStmesklaUL");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delMesklaIz.setString(1,cskl);
        delMesklaIz.setString(2,godina);
        delMesklaIz.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delMesklaIz");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delStmesklaIZ.setString(1,cskl);
        delStmesklaIZ.setString(2,godina);
        delStmesklaIZ.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStmesklaIZ");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delInventura.setString(1,cskl);
        delInventura.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delInventura");
        System.out.println(ex);
        returnValue = false;
      }
    }
/*
    if (returnValue) {
      try {
        delNalozi.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delNalozi");
        System.out.println(ex);
        returnValue = false;
      }
    }
*/
    if (returnValue) {
      try {
        delPos.setString(1,cskl);
        delPos.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delPos");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delRate.setString(1,cskl);
        delRate.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delRate");
        System.out.println(ex);
        returnValue = false;
      }
    }


    if (returnValue) {
      try {
        delStanje.setString(1,cskl);
        delStanje.setString(2,godina);
        delStanje.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStanje");
        System.out.println(ex);
        returnValue = false;
      }
    }
    if (returnValue) {
      try {
        delVTZTR.setString(1,cskl);
        delVTZTR.setString(2,godina);
        delVTZTR.execute();
        execute_plus();
      }
      catch (SQLException ex) {
        System.out.println("greska u delVTZTR");
        System.out.println(ex);
        returnValue = false;
      }
    }
    if (returnValue) {
      try {
        delVTZtrt.setString(1,cskl);
        delVTZtrt.setString(2,godina);
        delVTZtrt.execute();
        execute_plus();
      }
      catch (SQLException ex) {
        System.out.println("greska u delVTZtrt");
        System.out.println(ex);
        returnValue = false;
      }
    }
    if (returnValue) {
      try {
        delVTrabat.setString(1,cskl);
        delVTrabat.setString(2,godina);
        delVTrabat.execute();
        execute_plus();
      }
      catch (SQLException ex) {
        System.out.println("greska u delVTrabat");
        System.out.println(ex);
        returnValue = false;
    }}
    if (returnValue) {
      try {
        delVTzavtr.setString(1,cskl);
        delVTzavtr.setString(2,godina);
        delVTzavtr.execute();
        execute_plus();
      }
      catch (SQLException ex) {
        System.out.println("greska u delVTzavtr");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delStpos.setString(1,cskl);
        delStpos.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delStpos");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delVtrabat.setString(1,cskl);
        delVtrabat.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delVtrabat");
        System.out.println(ex);
        returnValue = false;
      }
    }

    if (returnValue) {
      try {
        delVtzavtr.setString(1,cskl);
        delVtzavtr.execute();
        execute_plus();
      }
      catch (Exception ex) {
        System.out.println("greska u delVtzavtr");
        System.out.println(ex);
        returnValue = false;
      }
// SEQ
      String myKey = "";
      if (returnValue) {
        for (int i = 0 ;i<araj_docs.length;i++) {
          myKey = "%"+cskl+"%"+araj_docs[i]+godina+"%";
          try {
/// delSeq
            delSeq.setString(1,myKey);
            delSeq.execute();
//              execute_plus();
          }
          catch (Exception ex) {
            System.out.println("greska u delSeq");
            System.out.println(ex);
            returnValue = false;
          }
          // delVTTEXT
          try {
            delVTTEXT.setString(1,myKey);
            delVTTEXT.execute();
          }
          catch (SQLException ex) {
            System.out.println("greska u delVTTEXT");
            System.out.println(ex);
            returnValue = false;
          }
          try {
            delVTprijenos1.setString(1,myKey);
            delVTprijenos1.execute();
          }
          catch (SQLException ex) {
            System.out.println("greska u delVTprijenos1 (OJ)");
            System.out.println(ex);
            returnValue = false;
          }
          try {
            delVTprijenos2.setString(1,myKey);
            delVTprijenos2.execute();
          }
          catch (SQLException ex) {
            System.out.println("greska u delVTprijenos2 (OJ)");
            System.out.println(ex);
            returnValue = false;
          }
          if (!returnValue) break;
        }
      }
   }
   return returnValue;
  }

  public boolean del_obrada_promet(final String cskl, final String godina) {

    return new raLocalTransaction(){
      public boolean transaction() throws Exception {
        return del_obrada_promet__outTransaction(cskl,godina);
      }
    }.execTransaction();

  }
}