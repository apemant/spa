/****license*****************************************************************
**   file: raAutoOtpfromRacMask.java
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
// sds
package hr.restart.robno;

import hr.restart.util.VarStr;
import hr.restart.util.raProcess;

import com.borland.dx.sql.dataset.QueryDataSet;

public class raAutoOtpfromRacMask  {
  private int okOTP= 0;
  private String cskl;
  private String vrdok;
  private String god;
  private int brdok;



  public void ispisiizradaOTP(boolean auto, boolean stop){

    okOTP = 0;
    QueryDataSet qds = hr.restart.util.Util.getNewQueryDataSet(
        "select Count(*) as sve from stdoki where " +
        "cskl='" + cskl + "' and vrdok='" + vrdok +
        "' and god='" + god + "' and brdok=" + brdok, true);
    if (qds.getInt("sve") == 0) {
    	if (!auto || stop)
    		javax.swing.JOptionPane.showMessageDialog
          (null,"Ne postoje stavke za izradu otpremnica !","Obavijest",
           javax.swing.JOptionPane.INFORMATION_MESSAGE);
    	okOTP = -1;
      return;
    }
    qds = hr.restart.util.Util.getNewQueryDataSet
        ("select Count(*) as sve from stdoki where "+
        "cskl='"+cskl+"' and vrdok='"+vrdok+
        "' and god='"+god+"' and brdok="+brdok+
        " and ((status='N' and vrdok!='ROT') or " +
        "      (status='X' and vrdok='ROT'))",true);
    if (qds.getInt("sve")!=0){
      if (auto || javax.swing.JOptionPane.showConfirmDialog(null,
          "Želite li napraviti otpremnice za ovaj raèun ?","Poruka",
          javax.swing.JOptionPane.OK_CANCEL_OPTION,
          javax.swing.JOptionPane.QUESTION_MESSAGE) ==
                                           javax.swing.JOptionPane.OK_OPTION){
        autoOtpremnica();
        if (!auto || (stop && okOTP < 0)) poruke();
        //if (okOTP==0) ispisOTP();
      } else {
        qds = hr.restart.util.Util.getNewQueryDataSet
            ("select Count(*) as sve from stdoki where "+
            "cskl='"+cskl+"' and vrdok='"+vrdok+
            "' and god='"+god+"' and brdok="+brdok+
            " and status!='N'",true);
        if (qds.getInt("sve")!=0){
System.out.println("sve==0");
          if (!auto) ispisOTP();
        } else {
System.out.println("sve != 0");
        }
        okOTP = -1;
      }
    } else {
      if (!auto) ispisOTP();
      okOTP = 0;
    }
  }
  
  public boolean wasError() {
    return okOTP < 0;
  }

  public void autoOtpremnica(){

    Runnable runAO = new Runnable(){
      public void run(){
        raProcess.setMessage("Kreiranje otpremnica u tijeku !!",true);
        raAutoOtpfromRac rAOfR = new raAutoOtpfromRac();
        okOTP = rAOfR.makeOtp(cskl,
                              vrdok,
                              brdok,
                              god);
      }
    };
    raProcess.runChild(runAO);
  }
  public void poruke(){
    switch (okOTP){
      case (0) :{
        javax.swing.JOptionPane.showMessageDialog(null,
            "Otpremnice uspjesno napravljene !","Obavijest",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        ispisOTP();
        break;}
      case (-1): {
        javax.swing.JOptionPane.showMessageDialog(null,
        "Neispravno nalaženje raèuna ili ga nema ili ih je previše ",
        "Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break;}
      case (-2): {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Neispravno nalaženje zaglavlje otpremnica !!!","Obavijest",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break;}
      case (-3): {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Neki artikli nemaju zalihu za izradu otpremnica !","Obavijest",
            javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break;
      }
      case (-5):{
        javax.swing.JOptionPane.showMessageDialog(null,
            "Nedovoljna zaliha za izradu otpremnica !","Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break; }

      case (-9): {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Sistemska greška neuspješno kreiranje otpremnica !","Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break;}
      default: {
        javax.swing.JOptionPane.showMessageDialog(null,
            "Nastala je greška kod kreiranja otpremnica (greška br. "+okOTP+" )!","Obavijest",javax.swing.JOptionPane.INFORMATION_MESSAGE);
        break;
      }
    }
  }

  public boolean start(){
    if (javax.swing.JOptionPane.showConfirmDialog(null,
        "Želite li napraviti otpremnice za ovaj raèun ?","Poruka",javax.swing.JOptionPane.OK_CANCEL_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE)==javax.swing.JOptionPane.OK_OPTION){
      autoOtpremnica();
      poruke();
      return true;
    }
    return true;
  }
  public void ispisOTP(){

    boolean isSrc = true;
    QueryDataSet priv = hr.restart.util.Util.getNewQueryDataSet(
        "select * from vtprijenos where keysrc = '"+raPrenosVT.makeKey("doki",cskl,
        vrdok,
        god,
        brdok)+"'",true);
    if (priv.getRowCount()==0) {
      priv = hr.restart.util.Util.getNewQueryDataSet(
        "select * from vtprijenos where keydest = '"+raPrenosVT.makeKey("doki",cskl,
        vrdok,
        god,
        brdok)+"'",true);
        if (priv.getRowCount()==0) {
          return;
        }
        isSrc = false;
    }

    if (javax.swing.JOptionPane.showConfirmDialog(null,
        "Želite li ispisati novonastale otpremnice  ?","Poruka",javax.swing.JOptionPane.OK_CANCEL_OPTION,
        javax.swing.JOptionPane.QUESTION_MESSAGE)==javax.swing.JOptionPane.OK_OPTION){
      VarStr vst= new VarStr("and doki.cskl||'-'|| doki.vrdok ||'-'|| doki.god ||'-'|| doki.brdok||'-' in (");


      for (priv.first();priv.inBounds();priv.next()){
        if (isSrc) {
          vst = vst.append("'").append(priv.getString("KEYDEST")).append("',");
        } else {
          vst = vst.append("'").append(priv.getString("KEYSRC")).append("',");
        }
      }
      vst.rightChop(1);
      vst=vst.append(")");
      reportsQuerysCollector.getRQCModule().ReSql(vst.toString(),"OTP");
      hr.restart.util.reports.raRunReport rRR= hr.restart.util.reports.raRunReport.getRaRunReport();
      rRR.addReport("hr.restart.robno.repOTP","hr.restart.robno.repOTP","OTP","Otpremnica");
      rRR.go();
    }
  }

  public void setBrdok(int brdok) {
    this.brdok = brdok;
  }
  public void setCskl(String cskl) {
    this.cskl = cskl;
  }
  public void setGod(String god) {
    this.god = god;
  }
  public void setVrdok(String vrdok) {
    this.vrdok = vrdok;
  }
}
