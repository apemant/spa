/****license*****************************************************************
**   file: raImages.java
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

import hr.restart.raRes;

import java.awt.Image;

import javax.swing.ImageIcon;
/**
 * Title:
 * Description:
 * Copyright:    Copyright (c) 2001
 * Company:
 * @author
 * @version 1.0
 */

public abstract class raImages {
  static String picResPath = "hr/restart/util/images";
  static String[][] picResources = new String[][]
    {
      {"imgAdd",picResPath+"/Novi.gif"},
      {"imgChange",picResPath+"/Ispravak.gif"},
      {"imgDelete",picResPath+"/Delete.gif"},
      {"imgPrint",picResPath+"/Ispis.gif"},
      {"imgExit",picResPath+"/Izlaz.gif"},
      {"imgOk",picResPath+"/OK.gif"},
      {"imgCancel",picResPath+"/Izlaz.gif"},
      {"imgStav",picResPath+"/Obrada.gif"},
      {"defaultHelpIcon",picResPath+"/help.jpg"},
      {"defaultErrIcon",picResPath+"/error.gif"},
      {"defaultBrowseIcon",picResPath+"/browse.gif"},
      {"imgTab",picResPath+"/tabs.jpg"},
      {"imgTBexit",picResPath+"/exit.jpg"},
      {"imgTBtop",picResPath+"/top.jpg"},
      {"imgTBmenu",picResPath+"/menu.jpg"},
      {"imgTBright",picResPath+"/right.jpg"},
      {"imgSplash",picResPath+"/splash.jpg"},
      {"imgSum",picResPath+"/sum.gif"},
      {"imgCheck",picResPath+"/check.gif"},
      {"imgUncheck",picResPath+"/xgif.gif"},
      {"imgFind",picResPath+"/find.gif"},
      {"imgPreview",picResPath+"/preview.gif"},
      {"imgExport",picResPath+"/export.gif"},
      {"imgRefresh",picResPath+"/refresh.gif"},
      {"imgAnimLogo",picResPath+"/animlogo.gif"},
      {"imgProperties",picResPath+"/properties.gif"},
      {"imgInformation",picResPath+"/information.gif"},
      {"imgPreferences",picResPath+"/preferences.gif"},
      {"imgComposemail",picResPath+"/composemail.gif"},
      {"imgAligncenter",picResPath+"/aligncenter.gif"},
      {"imgAlignjustify",picResPath+"/alignjustify.gif"},
      {"imgAlignright",picResPath+"/alignright.gif"},
      {"imgAlignleft",picResPath+"/alignleft.gif"},
      {"imgBold",picResPath+"/bold.gif"},
      {"imgExport2",picResPath+"/export2.gif"},
      {"imgFindagain",picResPath+"/findagain.gif"},
      {"imgHelp",picResPath+"/help.gif"},
      {"imgHistory",picResPath+"/history.gif"},
      {"imgHome",picResPath+"/home.gif"},
      {"imgImport",picResPath+"/import.gif"},
      {"imgItalic",picResPath+"/italic.gif"},
      {"imgMovie",picResPath+"/movie.gif"},
      {"imgNormal",picResPath+"/normal.gif"},
      {"imgOpen",picResPath+"/open.gif"},
      {"imgPause",picResPath+"/pause.gif"},
      {"imgSave",picResPath+"/save.gif"},
      {"imgSaveall",picResPath+"/saveall.gif"},
      {"imgSaveas",picResPath+"/saveas.gif"},
      {"imgSendmail",picResPath+"/sendmail.gif"},
      {"imgStop",picResPath+"/stop.gif"},
      {"imgTip",picResPath+"/tip.gif"},
      {"imgUnderline",picResPath+"/underline.gif"},
      {"imgVolume",picResPath+"/volume.gif"},
      {"imgZoom",picResPath+"/zoom.gif"},
      {"imgZoomin",picResPath+"/zoomin.gif"},
      {"imgZoomout",picResPath+"/zoomout.gif"},
      {"imgBack",picResPath+"/back.gif"},
      {"imgForward",picResPath+"/forward.gif"},
      {"imgUp",picResPath+"/up.gif"},
      {"imgDown",picResPath+"/down.gif"},
      {"imgAllBack",picResPath+"/allback.gif"},
      {"imgAllForward",picResPath+"/allforward.gif"},
      {"imgAllUp",picResPath+"/allup.gif"},
      {"imgAllDown",picResPath+"/alldown.gif"},
      {"imgX",picResPath+"/x.gif"},
      {"imgTable",picResPath+"/table.gif"},
      {"imgRaIcon",picResPath+"/raicon.jpg"},
      {"imgDelAll",picResPath+"/deleteall.gif"},
      {"imgTClosed",picResPath+"/tclosed.png"},
      {"imgTOpen",picResPath+"/topen.png"},
      {"imgTLeaf",picResPath+"/tleaf.png"},
      {"imgCopyCurr",picResPath+"/copycurr.gif"},
      {"imgDlgXExit",picResPath+"/dlgx_exit.png"},
      {"imgDlgXBack",picResPath+"/dlgx_back.png"},
      {"imgDlgXUser",picResPath+"/dlgx_user.png"},
      {"imgDlgXKnjig",picResPath+"/dlgx_knjig.png"},
      {"imgDlgXBkp",picResPath+"/dlgx_bkp.png"},
      {"imgPogodak",picResPath+"/pogodak.gif"},
      {"imgSticker",picResPath+"/sticker.gif"}
    };
  static int defPicIndex = 8;
  public static String IMGADD           = "imgAdd";
  public static String IMGCHANGE        = "imgChange";
  public static String IMGDELETE        = "imgDelete";
  public static String IMGPRINT         = "imgPrint";
  public static String IMGEXIT          = "imgExit";
  public static String IMGOK            = "imgOk";
  public static String IMGCANCEL        = "imgCancel";
  public static String IMGSTAV          = "imgStav";
  public static String DEFAULTHELPICON  = "defaultHelpIcon";
  public static String DEFAULTERRICON   = "defaultErrIcon";
  public static String DEFAULTBROWSEICON= "defaultBrowseIcon";
  public static String IMGTAB           = "imgTab";
  public static String IMGTBEXIT        = "imgTBexit";
  public static String IMGTBTOP         = "imgTBtop";
  public static String IMGTBMENU        = "imgTBmenu";
  public static String IMGTBRIGHT       = "imgTBright";
  public static String IMGSPLASH        = "imgSplash";
  public static String IMGSUM           = "imgSum";
  public static String IMGCHECK         = "imgCheck";
  public static String IMGUNCHECK       = "imgUncheck";
  public static String IMGFIND          = "imgFind";
  public static String IMGPREVIEW       = "imgPreview";
  public static String IMGEXPORT        = "imgExport";
  public static String IMGREFRESH       = "imgRefresh";
  public static String IMGANIMLOGO      = "imgAnimLogo";
  public static String IMGPROPERTIES   = "imgProperties";
  public static String IMGINFORMATION  = "imgInformation";
  public static String IMGPREFERENCES  = "imgPreferences";
  public static String IMGCOMPOSEMAIL  = "imgComposemail";
  public static String IMGALIGNCENTER  = "imgAligncenter";
  public static String IMGALIGNJUSTIFY = "imgAlignjustify";
  public static String IMGALIGNRIGHT   = "imgAlignright";
  public static String IMGALIGNLEFT    = "imgAlignleft";
  public static String IMGBOLD         = "imgBold";
  public static String IMGEXPORT2      = "imgExport2";
  public static String IMGFINDAGAIN    = "imgFindagain";
  public static String IMGHELP         = "imgHelp";
  public static String IMGHISTORY      = "imgHistory";
  public static String IMGHOME         = "imgHome";
  public static String IMGIMPORT       = "imgImport";
  public static String IMGITALIC       = "imgItalic";
  public static String IMGMOVIE        = "imgMovie";
  public static String IMGNORMAL       = "imgNormal";
  public static String IMGOPEN         = "imgOpen";
  public static String IMGPAUSE        = "imgPause";
  public static String IMGSAVE         = "imgSave";
  public static String IMGSAVEALL      = "imgSaveall";
  public static String IMGSAVEAS       = "imgSaveas";
  public static String IMGSENDMAIL     = "imgSendmail";
  public static String IMGSTOP         = "imgStop";
  public static String IMGTIP          = "imgTip";
  public static String IMGUNDERLINE    = "imgUnderline";
  public static String IMGVOLUME       = "imgVolume";
  public static String IMGZOOM         = "imgZoom";
  public static String IMGZOOMIN       = "imgZoomin";
  public static String IMGZOOMOUT      = "imgZoomout";
  public static String IMGBACK         = "imgBack";
  public static String IMGFORWARD      = "imgForward";
  public static String IMGUP           = "imgUp";
  public static String IMGDOWN         = "imgDown";
  public static String IMGALLBACK      = "imgAllBack";
  public static String IMGALLFORWARD   = "imgAllForward";
  public static String IMGALLUP        = "imgAllUp";
  public static String IMGALLDOWN      = "imgAllDown";
  public static String IMGX            = "imgX";
  public static String IMGTABLE        = "imgTable";
  public static String IMGRAICON       = "imgRaIcon";
  public static String IMGDELALL        = "imgDelAll";
  public static String IMGTCLOSED       = "imgTClosed";
  public static String IMGTOPEN         = "imgTOpen";
  public static String IMGTLEAF         = "imgTLeaf";
  public static String IMGCOPYCURR      = "imgCopyCurr";
  public static String IMGDLGXEXIT      = "imgDlgXExit";
  public static String IMGDLGXBACK      = "imgDlgXBack";
  public static String IMGDLGXBKP       = "imgDlgXBkp";
  public static String IMGDLGXKNJIG      = "imgDlgXKnjig";
  public static String IMGDLGXUSER      = "imgDlgXUser";
  public static String IMGPOGODAK       = "imgPogodak";
  public static String IMGSTICKER       = "imgSticker";
  public static ImageIcon imgNull;

  static {
    try {
//      System.err.println("tkit");
//      Aus.dumpClassName(Toolkit.getDefaultToolkit());
      imgNull = new ImageIcon(raRes.class.getClassLoader().getResource(picResPath+"/null.gif"));
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  //private static String modExt = ".jpg";
  private static String modExt = "2.png";
  public static ImageIcon getModuleIcom(String module) {
    try {
//      System.err.println("getModuleIcom("+module);
      return new javax.swing.ImageIcon(raRes.class.getClassLoader().getResource("hr/restart/util/images/"+module.toLowerCase()+modExt));
//      return new javax.swing.ImageIcon(ClassLoader.getSystemResource("hr/restart/util/images/"+module.toLowerCase()+modExt));
    } catch (Exception e) {
      return imgNull;
    }
  }
  public static ImageIcon getModuleIcomHelp(String module) {
    try {
//      System.err.println("getModuleIcomHelp("+module);
      ImageIcon moduleIcon = new javax.swing.ImageIcon(raRes.class.getClassLoader().getResource("hr/restart/util/images/"+module.toLowerCase()+modExt),module.toLowerCase());
//      ImageIcon moduleIcon = new javax.swing.ImageIcon(ClassLoader.getSystemResource("hr/restart/util/images/"+module.toLowerCase()+modExt),module.toLowerCase());
      return getSizedImage(moduleIcon,120);
    } catch (Exception e) {
      e.printStackTrace();
      return imgNull;
    }
  }
  public static ImageIcon getModuleIcon(startFrame SF) {
//    String module = hr.restart.mainFrame.findAPPBundleSec(SF.getClass().getName());
    return getModuleIcon(SF.getClass().getName());
  }//
  public static ImageIcon getModuleIcon(String className) {
    String module = hr.restart.mainFrame.findAPPBundleSec(className);
    return getModuleIcom(module);
  }
  public static ImageIcon getSizedImage(ImageIcon imgIc, int imgwidth) {
    imgIc.setImage(getOriginalImage(imgIc).getImage().getScaledInstance(imgwidth,-1,Image.SCALE_FAST));
    return imgIc;
//    return imgNull;
  }
  public static ImageIcon getOriginalImage(ImageIcon img) {
    try {
//      System.err.println("getOriginalImage("+img);
      return new ImageIcon(raRes.class.getClassLoader().getResource(getPicResource(img.getDescription())),img.getDescription());
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return imgNull;
    }
  }
  /**
   * vra\u0107a ikonu za ulazni parametar opisa ikone. Ulazni parametar moze biti jedan od statickih stringova u raImages.
   * Npr: JButton.setIcon(raImages.getImage(raImages.IMGADD));
   */
  public static ImageIcon getImageIcon(String desc) {
    try {
//      System.err.println("getImageIcon("+desc);
      return new ImageIcon(raRes.class.getClassLoader().getResource(getPicResource(desc)),desc);
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return imgNull;
    }
//    return new ImageIcon(ClassLoader.getSystemResource(getPicResource(desc)),desc);
//    return imgNull;
  }
  /** wrapper izmedju getImageIcon i getModuleIcom
   * @param desc - ili raImages.IMG... ili naziv modula
   * @return sliku ili preko raImages.<String> ili preko naziva modula (sliku od tog modula)
   */ 
  public static ImageIcon getImageOrModuleIcon(String desc) {
    if (findPicResource(desc) != null) {
      return getImageIcon(desc);
    } else {
      return getModuleIcom(desc);
    }
  }
  public static ImageIcon getImageIcon(String desc, int scale, boolean width) {
    ImageIcon _imc = getImageIcon(desc);
    int w = (width)?scale:-1;
    int h = (width)?-1:scale;
    try {
      
//    System.err.println("getImageIcon("+desc+","+scale+")");
    return new ImageIcon(_imc.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return imgNull;
    }
    
  }
  public static ImageIcon getImageIcon(String desc, double scale) {
    ImageIcon _imc = getImageIcon(desc);
    int w = (int)(_imc.getIconWidth()*scale);
    int h = (int)(_imc.getIconHeight()*scale);
    try {
//      System.err.println("getImageIcon("+desc+","+scale+")");
      return new ImageIcon(_imc.getImage().getScaledInstance(w,h,Image.SCALE_SMOOTH));
    }
    catch (Exception ex) {
      ex.printStackTrace();
      return imgNull;
    }
//    return imgNull;
  }
  public static ImageIcon getNullImage() {
    return imgNull;
  }
  
  public static String getPicResource(String desc) {
    String res = findPicResource(desc);
    if (res == null) {
      return picResources[defPicIndex][1];
    } else return res;
  }
  
  public static void setPicResource(String desc, String resource) {
    int idx = findPicResourceIndex(desc);
    if (idx>-1) picResources[idx][1] = resource;
  }
  
  public static void setRelativePicResource(String desc, String resource) {
    int idx = findPicResourceIndex(desc);
    if (idx>-1) picResources[findPicResourceIndex(desc)][1] = picResPath+"/"+resource;
  }
  private static String findPicResource(String desc) {
    for (int i=0;i<picResources.length;i++) {
      if (picResources[i][0].equals(desc)) return picResources[i][1];
    }
    return null;
  }
  
  private static int findPicResourceIndex(String desc) {
    for (int i=0;i<picResources.length;i++) {
      if (picResources[i][0].equals(desc)) return i;
    }
    return -1;
  }
  
  
}