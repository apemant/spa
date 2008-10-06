/****license*****************************************************************
**   file: raClientTrigger.java
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
package hr.restart.db;
import hr.restart.robno.RemoteConnection;
import hr.restart.robno.raLiteFrameRobnoReplicator;
import hr.restart.util.FileHandler;
import hr.restart.util.Valid;

import java.util.Date;
import java.util.StringTokenizer;
import java.util.Timer;
import java.util.TimerTask;

public class raClientTrigger {

  private hr.restart.baza.dM dm = hr.restart.baza.dM.getDataModule();
  private boolean jeGotovo = true;

  private FileHandler fh = null;
  private RemoteConnection RC = new RemoteConnection();
  private boolean gasi_odmah = false;
  private int broj_pokusaja = 10;
  private int vrijeme_izmedju_poziva=120;  //u sec
//  private int minimum_prije_shutdowna=30;
  private String param_replcmddialUp;
  private String param_replcmddialDis;
  private Date startTransferDate;
  private Date startShutDownDate;
  private raLiteFrameRobnoReplicator rLFRR = new raLiteFrameRobnoReplicator(){
     public void brbljaj(String text,boolean nesto){
       poruka(text);
     }
  };


  public raClientTrigger() {

    hr.restart.baza.dM.getDataModule();

    param_replcmddialUp = hr.restart.sisfun.frmParam.getParam
                ("robno","replcmddialUp","LAN",
                "Dial up komanda za konekciju (za prenos mrezom upisati LAN)");
    param_replcmddialDis = hr.restart.sisfun.frmParam.getParam
                ("robno","replcmddialDis","",
                "Naredba OS-u za prekid konekcije ako je odabran dialup != 'LAN'");
  }

  public boolean dialUp(){
    if (!RC.makeDialupConnection(param_replcmddialUp)) {
      RC.DialUpdisconnect(param_replcmddialDis);
      return false;
    };
    return true;
  }

  public void poruka(String ss){
    prepareZaIspis();
    fh.writeln(" ----- "+Valid.getValid().getToday()+" -----");
    fh.writeln(ss);
    fh.close();
//System.out.println(ss);
  }

  public void do_all(){

    jeGotovo = false;

    boolean is_uspjesno = false;

    for (int i = 0; i<broj_pokusaja;i++){
      poruka("Pokušaj spajanja br."+(i+1));
      if (is_uspjesno = dialUp()) {
        poruka("Uspješno spajanje .....");
        break;
      }
      poruka("Neuspješno spajanje ...");
      poruka("Èekam ...");

      try {
        Thread.sleep(1000*vrijeme_izmedju_poziva);
      }
      catch (InterruptedException ex) {
        ex.printStackTrace();
      }

    }

    if (!is_uspjesno) {
        if (gasi_odmah) {
          gasi_sebe_ili_se();
        }
        return;
    }




    rLFRR.ProccesTwoPartTransfer();
    poruka(rLFRR.getPoruka());
    poruka("Gotov prijenos");

// prièekaj za svaki sluèaj još minutu
    try {
      Thread.sleep(1000*60);
    }
    catch (InterruptedException ex) {
      ex.printStackTrace();
      }

      RC.DialUpdisconnect(param_replcmddialDis);
      try {
        Thread.sleep(1000*60);
      }
      catch (InterruptedException ex) {
        ex.printStackTrace();
      }
      jeGotovo= true;
      if (gasi_odmah) {
        gasi_sebe_ili_se();
      }
  }

  public void gasi_sebe_ili_se(){
    poruka("Shutdown pokrenut u "+Valid.getValid().getToday());
    hr.restart.util.Sys.shutdown(true);
  }


  public void setupTimer(){
    Timer tm_rad = new Timer();
    tm_rad.schedule(new TimerTask(){
      public void run(){
        do_all();
      }
    },startTransferDate,(long) 24*60*60*1000*60);

    if (!gasi_odmah) {
      gasiTimer(startShutDownDate);
   }
  }
  public void gasiTimer(Date date){
    Timer tm_gasi = new Timer();
      tm_gasi.schedule(new TimerTask(){
        public void run(){
          if (jeGotovo= true) {
            gasi_sebe_ili_se();
          } else {
            gasiTimer(new Date(Valid.getValid().getToday().getTime()+1000*60*15)); // pokušaj ugasiti za kvarat ure
          }
        }
      },date,(long) 1000*60);
  }

  public void testTimes(){
    if (startTransferDate==null) {
      startTransferDate = new Date(Valid.getValid().getToday().getTime()
                       +1*60*1000); // poèinje za 5 minuta od sada ako je prazno
    }
    if (startShutDownDate==null) {
      startShutDownDate = new Date(Valid.getValid().getToday().getTime()
                       +45*60*1000); // poèinje za 45 minuta od sada ako je prazno
    }
  }

  public static Date vrijeme(String param){
    StringTokenizer st = new StringTokenizer(param,":");
    String sati = "";
    String minute = "";
    long milis = 0;
    if (st.countTokens()!=2) {
      System.out.println("Neispravni argument "+param);
    } else {
      sati = st.nextToken();
      minute = st.nextToken();
      try {
          Integer isati = new Integer(sati);
          if (isati.intValue()<0 || isati.intValue()>23){
            System.out.println("Neispravni argument "+param);
          } else {
            milis = milis+isati.intValue()*60*60*1000;
          }
        }
        catch (NumberFormatException ex) {
          ex.printStackTrace();
          System.out.println("Neispravni argument "+param);
        }

     try {
         Integer inte = new Integer(minute);
         if (inte.intValue()<0 || inte.intValue()>59){
           System.out.println("Neispravni argument "+param);
           System.exit(0);
         } else {
           milis = milis+inte.intValue()*60*1000;
         }
       }
       catch (NumberFormatException ex) {
         ex.printStackTrace();
         System.out.println("Neispravni argument "+param);
         System.exit(0);
       }
       return new Date(hr.restart.util.Util.getUtil().getFirstSecondOfDay(Valid.getValid().getToday()).getTime()
                       +milis);
     }
     return null;
  }

  private void prepareZaIspis(){
    String ime_fajle="at_"+new java.text.SimpleDateFormat("yyyyMMdd").format(Valid.getValid().getToday())+".log";
    FileHandler fh_zacitanje = new FileHandler(ime_fajle);
    fh_zacitanje.openRead();
    String za_citanje= fh_zacitanje.read();
    fh_zacitanje.close();
    if (fh==null) {
      fh = new FileHandler(ime_fajle);
    } else   {
      fh.close();
    }
    fh.openWrite();
    fh.write(za_citanje);
  }

  public static void main(String[] args) {
    raClientTrigger raClientTrigger1 = new raClientTrigger();
    if (args.length!=0) {
      for (int i = 0; i<args.length;i++){
        if (args[i].trim().equalsIgnoreCase("-startTransfer")){
          i++;
          if (i<args.length){
            raClientTrigger1.startTransferDate = vrijeme(args[i]);
          } else {
            System.out.println("Ne postoji argument za -startTransfer");
            System.exit(0);
          }
        } else if (args[i].trim().equalsIgnoreCase("-shutdown")){
          i++;
          if (i<args.length){
            raClientTrigger1.startShutDownDate = vrijeme(args[i]);
          } else {
            System.out.println("Ne postoji argument za -shutdown");
            System.exit(0);
          }
        } else if (args[i].trim().equalsIgnoreCase("-trys")){
          i++;
          if (i<args.length){
            try {
              raClientTrigger1.broj_pokusaja = new Integer(args[i]).intValue();
            }
            catch (NumberFormatException ex) {
              ex.printStackTrace();
              System.out.println("Neispravni argument "+args[i]);
            }
          } else {
            System.out.println("Ne postoji argument za -trys");
            System.exit(0);
          }
        } else if (args[i].trim().equalsIgnoreCase("-shutnow")){
          raClientTrigger1.gasi_odmah = true;
        } else if (args[i].trim().equalsIgnoreCase("-delay")){
          i++;
          if (i<args.length){
            try {
              raClientTrigger1.vrijeme_izmedju_poziva = new Integer(args[i]).intValue();
            }
            catch (NumberFormatException ex) {
              ex.printStackTrace();
              System.out.println("Neispravni argument "+args[i]);
            }
          } else {
            System.out.println("Ne postoji argument za -delay");
            System.exit(0);
          }
        } else if (args[i].trim().equalsIgnoreCase("--help")){
          System.out.println("Program za automatski transfer podataka sa klijenta na server koji se pokrece  ");
          System.out.println("u vremenskom razmaku od 24 sada u odnosu na startTransfer");
          System.out.println("Parametri su slijedeci ");
          System.out.println("-startTransfer hh:mm   - vrijeme kad se zapocinje s prijenosom ako nije setirano ");
          System.out.println("                         prijenos poèinje 5 minuta nakon pokretanja programa");
          System.out.println("-shutdown hh:mm        - vrijeme kad ce se racunalo ugasiti samostalno ako nije setirano");
          System.out.println("                         raèunalo se neæe ugasiti osim ako je upaljena opcija -shutnow");
          System.out.println("-shutnow               - automatsko gašenje raèunala nakon uspješnog prijenosa ili n (op. trys) neuspješnih pokusaja");
          System.out.println("-trys n                - broj uzastopnih pokušaja spajanja na server default 10");
          System.out.println("-delays n              - vrijeme (u sec) izmedju dva pokušaja spajanja na server u jednoj sesiji ");
          System.out.println("--help                 - upravo ga èitate ");
          System.exit(0);
        } else {
          System.out.println("Neispravni argument "+args[i]);
          System.exit(0);
        }
      }
    } else {}

    raClientTrigger1.testTimes();
    raClientTrigger1.poruka("Otvoren u "+Valid.getValid().getToday());
    raClientTrigger1.setupTimer();
  }
}