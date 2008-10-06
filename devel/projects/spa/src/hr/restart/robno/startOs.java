/****license*****************************************************************
**   file: startOs.java
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

import hr.restart.baza.dM;

import javax.swing.JDialog;
import javax.swing.JLabel;

public class startOs {
  boolean packFrame = false;
  JDialog splashScreen = null;
  JLabel splashLabel = null;
  dM dm;
//  ResourceBundle res = ResourceBundle.getBundle("hr.restart.robno.Res");
  /**Construct the application*/
  public startOs() {
 //   createSplashScreen();
    hr.restart.os.osMain frame = new hr.restart.os.osMain() {
      public void hide() {
        System.exit(0);
      }
    };
    frame.ShowMe(false, "Osnovna sredstva");
    //Validate frames that have preset sizes
    //Pack frames that have useful preferred size info, e.g. from their layout
    /*if (packFrame) {
      frame.pack();
    }
    else {
      frame.validate();
    }
    Center the window
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension frameSize = frame.getSize();
    if (frameSize.height > screenSize.height) {
      frameSize.height = screenSize.height;
    }
    if (frameSize.width > screenSize.width) {
      frameSize.width = screenSize.width;
    }
    frame.setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);*/
 //   hideSplashScreen();
//    frame.setVisible(true);
//    frame.ShowMe(true, "Jebo");
    try {
      jbInit();
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  /**Main method*/
  public static void main(String[] args) {
/*    try {
      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
    }
    catch(Exception e) {
      e.printStackTrace();
    }*/
//    hr.restart.util.raDataSetAdmin.configureDataSets(hr.restart.baza.dM.getDataModule());
    hr.restart.util.startFrame SF=_Main.getStartFrame();
    SF.raLookAndFeel();
    new startOs();
//    hr.restart.util.raLoader.lazyLoad();
  }
  /*
  public void createSplashScreen() {
    System.out.println("tu sam");
    splashLabel = new JLabel(new ImageIcon("C:/Documents and Settings/Sinisa/My Documents/Splash.jpg"));
    splashScreen = new JDialog();
    splashScreen.getContentPane().add(splashLabel);
    splashScreen.pack();
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    splashScreen.setLocation(screenSize.width/2 - splashScreen.getSize().width/2,
    screenSize.height/2 - splashScreen.getSize().height/2);
//    splashScreen.show();
  }
  public void hideSplashScreen() {
    splashScreen.setVisible(false);
    splashScreen = null;
    splashLabel = null;
  }*/
  private void jbInit() throws Exception {
    dm = dM.getDataModule();
  }


}