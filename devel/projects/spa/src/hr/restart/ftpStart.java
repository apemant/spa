/****license*****************************************************************
**   file: ftpStart.java
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
 * ftpStart.java
 *
 * Created on 2004. veljaèa 18, 16:27
 */

package hr.restart;

import hr.restart.util.FileHandler;

import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Window;
/**
 * uses ftpTransfer, FileHandler, raSplashWorker, VarStr, 
 * @author  andrej
 */
public class ftpStart {
  
  static ftpStartSplash spla;

  /** Creates a new instance of ftpStart */
  public ftpStart() {
  }
  
  public static void showSplash() throws Exception {
    spla = new ftpStartSplash();
    spla.setSize(raSplashWorker.spl_width,raSplashWorker.spl_height);
    spla.setLocation(raSplashWorker.getCenter(spla));
    spla.show();
  }
  public static void hideSplash() throws Exception {
    spla.dispose();
  }
  static void splashMessg(String mess) {
    System.out.println(mess);
    if (spla == null) return;
    spla.setMs(mess);
  }
  
  public static void redirectSystemOut(String logname) {
    FileHandler FH = new FileHandler(logname);
    FH.openWrite();
    System.setOut(new java.io.PrintStream(FH.fileOutputStream));
    System.setErr(new java.io.PrintStream(FH.fileOutputStream));
  }
  public static void main(String args[]) {
    try {
      redirectSystemOut("ftpstart.log");
      showSplash(); /**@todo: u ftpVersionWorker.transferNewResourcesAndStart() ako ima razlika */
      ftpVersionWorker.transferNewResourcesAndStart();
      ftpVersionWorker.waitForAndExit();
      //System.exit(0);
    } catch (Exception ex) {
      javax.swing.JOptionPane.showMessageDialog(null, "Greska pri provjeri verzije: "+ex.getMessage(), "Pozor!", javax.swing.JOptionPane.ERROR_MESSAGE);
      ex.printStackTrace();
      System.exit(0);
    }
  }
}
class ftpStartSplash extends Window {
  Image im;
  public ftpStartSplash() {
    super(new Frame("Starting SPA..."));
    try {
      im = raSplashWorker.loadSplashImage();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  String ms = "Provjera nove verzije ...";
  public void setMs(String _ms) {
    ms = _ms;
    //repaint();
    raSplashWorker.drawMess(getGraphics(),ms);
  }
  public void paint(Graphics g) {
    raSplashWorker.paint(g, im, ms, this);
  }    
}