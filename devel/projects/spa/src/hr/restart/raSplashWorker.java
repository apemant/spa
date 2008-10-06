/****license*****************************************************************
**   file: raSplashWorker.java
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
 * raSplashWorker.java
 *
 * Created on 2004. veljaèa 19, 09:12
 */

package hr.restart;
import hr.restart.util.versions.raVersionInfo;

import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.CropImageFilter;
import java.awt.image.FilteredImageSource;

/**
 * set statickih membera i metoda za prikazivanje splasha
 * @author  andrej
 */
public class raSplashWorker {
  public static int spl_width = 357;//345;//381;
  public static int spl_height = 209;//197;//143;
  public static int spl_margin = 10;//0;
  public static Color textColor = Color.black;//Color.white;
  public static Color backColor = Color.white;//new Color(159,156,185,255);
  private static boolean draw_lines = false;
  private static boolean imagedr = false;
  private static Rectangle clearedRect = null;
  private static Image cropedim = null; 
  static boolean displayVersion = true;
  static String dateMF = raVersionInfo.getBuildDateMF();
  static String verMF = raVersionInfo.getCurrentVersion(); 

  /** Creates a new instance of raSplashWorker */
  protected raSplashWorker() {
  }
  
  public static void paint(Graphics g, Image im, String messg, Component source) {
    if (g == null) return;
    g.drawImage(im,0,0,source);
    //System.out.println("cropedim "+cropedim+" source "+source);
    if (cropedim == null && source != null && im != null && clearedRect!=null) {
//System.out.println("crtam dropedim "+clearedRect.x+", "+clearedRect.y+", "+clearedRect.width+", "+clearedRect.height);
      cropedim = source.createImage(new FilteredImageSource(im.getSource(),
 			   new CropImageFilter(clearedRect.x, clearedRect.y, clearedRect.width, clearedRect.height)));
    } else if (cropedim == null) {
      try {
        Thread.sleep(30);
        paint(g, im, messg, source);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
    }
    drawMess(g, messg);
  }
  public static void drawMess(Graphics g, String messg) {
    drawMess(g, messg, null);
  }
  /**
   * Draws string messg in graphics g. If messg is null clears messg
   * @param g
   * @param messg
   */
  public static void drawMess(Graphics g, String messg, Component source) {
//    VarStr.join(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames(),"\n");
    if (g==null) {
      return;
    }
    //    g.setFont(g.getFont().deriveFont(Font.BOLD));
    g.setFont(Font.getFont("Arial"));
    g.setFont(g.getFont().deriveFont(Font.PLAIN,10));
    int fontHeight = g.getFontMetrics().getHeight()+(spl_margin);
    Graphics2D g2d = (Graphics2D)g;
    g2d.setColor(textColor);
    int tx_lmargin = spl_margin==0?15:spl_margin;
    int tx_bmargin = spl_height-tx_lmargin;
    //draw version info
    if (displayVersion) {
      fontHeight = fontHeight*4;
    }
//    if (messg == null) {
//      System.out.println("g2d.clearRect(0, "+(tx_bmargin-fontHeight)+", "+spl_width+", "+(fontHeight+tx_lmargin)+")");
    clearedRect = new Rectangle(0, tx_bmargin-fontHeight, spl_width, fontHeight+tx_lmargin);
//      g2d.clearRect(clearedRect.x, clearedRect.y, clearedRect.width, clearedRect.height);
//      System.out.println(cropedim+" "+clearedRect);
    if (cropedim!=null && clearedRect !=null) {
      g.drawImage(cropedim,clearedRect.x, clearedRect.y, clearedRect.width, clearedRect.height, source);
/*
      try {
        Thread.sleep(30);
      } catch (InterruptedException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
*/
    }
//    } else {
    g2d.drawString(messg,tx_lmargin,tx_bmargin+3);
    if (displayVersion) {
//      displayVersion = false;
      int fh = (fontHeight/4);
      int xoff = 170;
      //    g2d.setFont(g.getFont().deriveFont(Font.BOLD));
      String s1_red_lijevo = "Sustav Poslovnih Aplikacija";
      String s2_red_lijevo = "2007 Rest Art";
      String s1_red_desno = "Verzija: "+verMF;
      String s2_red_desno = "Datum: "+dateMF;
      g2d.drawString(s1_red_lijevo,tx_lmargin, tx_bmargin-(fh+5));
      g2d.drawString(s2_red_lijevo,tx_lmargin, tx_bmargin-(fh-9));
      g2d.drawString(s1_red_desno,tx_lmargin+xoff, tx_bmargin-(fh+5));
      g2d.drawString(s2_red_desno,tx_lmargin+xoff, tx_bmargin-(fh-9));
    }
//    }
    //draw lines
    if (!draw_lines) return;
    g2d.setColor(new Color(224,224,224));
    //horizontal
    g2d.drawLine(10, spl_height-10, spl_width-11, spl_height-10);
    //vertical
    g2d.drawLine(10, tx_bmargin-fontHeight, 10, spl_height-10);
    g2d.drawLine(spl_width-11, tx_bmargin-fontHeight, spl_width-11, spl_height-10);
  }

  public static Point getCenter(Window spla) {
    int sw = Toolkit.getDefaultToolkit().getScreenSize().width;
    int sh = Toolkit.getDefaultToolkit().getScreenSize().height;
    return new Point((sw-spla.getWidth())/2,(sh-spla.getHeight())/2);
  }
  
  public static Image loadSplashImage() throws Exception {
    final Image fim = Toolkit.getDefaultToolkit().getImage(
    ftpStart.class.getClassLoader().getResource("hr/restart/util/images/splash.jpg")
    );
    MediaTracker mtracker = new MediaTracker(new Container());
    mtracker.addImage(fim,1);
    mtracker.waitForID(1);
/*    Component cropper = new Label();
    int tx_lmargin = spl_margin==0?15:spl_margin;
    int tx_bmargin = spl_height-tx_lmargin;
    int fontHeight = cropper.getGraphics().getFontMetrics().getHeight()+spl_margin;
    clearedRect = new Rectangle(0, tx_bmargin-fontHeight, spl_width, fontHeight+tx_lmargin);
System.out.println("clearedRect "+clearedRect);    
    cropedim = cropper.createImage(new FilteredImageSource(fim.getSource(),
			   new CropImageFilter(clearedRect.x, clearedRect.y, clearedRect.width, clearedRect.height)));
    mtracker.addImage(cropedim, 2);
    mtracker.waitForID(2);
System.out.println("cropedim "+cropedim);*/
    return fim;
  }
}
