/****license*****************************************************************
**   file: Clock.java
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
package hr.restart.help;



import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.swing.JComponent;
import javax.swing.JPanel;
 


/**

 * Title:

 * Description:

 * Copyright:    Copyright (c) 2001

 * Company:

 * @author

 * @version 1.0

 */



public class Clock extends JPanel {

  private boolean digital;

  private String delimiter = ":";

//  private String datePattern = "EEEE, dd MMMM yyyy";

  private String datePattern = "dd-MM-yyyy";

  private DigitalClock dclock = new DigitalClock();

  private AnalogClock aclock = new AnalogClock();

  private boolean showDate = false;



  public Clock() {

    setLayout(new BorderLayout());

    setOpaque(false);

    setPreferredSize(new Dimension(100,100));

  }



  public Clock(boolean isdigital) {

    this();

    setDigital(isdigital);

  }



  public JComponent getDigitalClock() {

    return dclock;

  }



  public JComponent getAnalogClock() {

    return aclock;

  }



  public boolean isDigital() {

    return digital;

  }



  public void setDigital(boolean newDigital) {

    digital = newDigital;

    remove(aclock);

    remove(dclock);

    if (digital) {

      add(dclock,BorderLayout.CENTER);

    } else {

      add(aclock,BorderLayout.CENTER);

    }

    validate();

    repaint();

  }



  /**

   * Gets ShowDate property.

   * @result ShowDate property value.

   */

  public boolean isShowDate() {

    return showDate;

  }



  /**

   * Sets DhowDate property.

   * @param showDate new property value.

   */

  public void setShowDate(boolean showDate) {

    this.showDate = showDate;

    repaint();

  }

  public boolean getShowDate() {

    return showDate;

  }





///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

////

/////////    DIGITAL CLOKK

////

///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

  public class DigitalClock extends JComponent {

    private ClockThread clockThread = null;



    /**

     * Default bean constructor.

     */

    public DigitalClock() {

      clockThread = new ClockThread();

      clockThread.start();

    }



    public Thread getThread() {

      return clockThread;

    }



    /**

     * Finds a font size which better fits the specified message to given sizes.

     * @param g current graphical context.

     * @param s string message.

     * @param w width of the rectangle.

     * @param h height of the rectangle.

     */

    private Font findFont(Graphics g, String s, int w, int h) {

      int size = 6;

      Font font = new Font(this.getFont().getName(), this.getFont().getStyle(), size);

      while (true) {

        FontMetrics m = g.getFontMetrics(font);

        if (m.getHeight() > h || m.stringWidth(s) > w)

          break;

        size++;

        font = new Font(this.getFont().getName(), this.getFont().getStyle(), size);

      }

      return font;

    }



    /**

     * Paints current time on the screen.

     * It uses current foreground and background colors fro drawing.

     * @param g current graphical context.

     */

    public void paint(Graphics g) {

      String strTime, strDate;

      Calendar calendar = new GregorianCalendar();

      SimpleDateFormat format = new SimpleDateFormat();

      double timeRate = (showDate) ? 0.5 : 0.8;



      // Find suitable font and position

      Dimension d = this.getSize();

//      g.clearRect(0, 0, d.width, d.height);

     // Draw current time

      format.applyPattern("HH" + delimiter + "mm" + delimiter + "ss");

      strTime = format.format(calendar.getTime());

      Font font = findFont(g, strTime, (int)(d.getWidth() * 0.8),

        (int)(d.getHeight() * timeRate));

      FontMetrics m = g.getFontMetrics(font);

      int x = (d.width - m.stringWidth(strTime)) / 2;

      int y = (int) ((d.getHeight() * timeRate - m.getHeight()) / 2

        + d.getHeight() * 0.1) + m.getAscent();

      g.setFont(font);

    //AI

      g.setColor(Color.white);

//      g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.height,m.getHeight()-m.getAscent()/2+1);

      g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.width,m.getHeight()-m.getAscent()/2+1);

      g.setColor(Color.black);

    //EAI

      g.drawString(strTime, x, y);



      // Draw current date

      if (showDate) {

        format.applyPattern(datePattern);

        strDate = format.format(calendar.getTime());

        font = findFont(g, strDate, (int)(d.getWidth() * 0.8),

          (int)(d.getHeight() * 0.3));

        m = g.getFontMetrics(font);

        x = (d.width - m.stringWidth(strDate)) / 2;

        y = (int) ((d.getHeight() * 0.3 - m.getHeight()) / 2

          + d.getHeight() * 0.6) + m.getAscent();

        g.setFont(font);

      //AI

        g.setColor(Color.white);

//        g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.height,m.getHeight()-m.getAscent()/2+1);

        g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.width,m.getHeight()-m.getAscent()/2+1);

        g.setColor(Color.black);

      //EAI

        g.drawString(strDate, x, y);

      }

    }



    /**

     * Shows this bean in separate application frame on the screen.

     * It allows to use this bean as stand along application.

     */

/*

    public static void main(String[] args) {

      DigitalClock digitalClock = new DigitalClock();

      JFrame frame = new JFrame("Digital Clock");

  //    digitalClock.setShowDate(false);

      frame.setContentPane(digitalClock);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.setSize(300, 300);

      frame.setLocation(300, 300);

      frame.show();

    }

*/

  /**

   * Thread for display current time each 0.5 second.

   */

    private class ClockThread extends Thread {

      /**

       * Makes all work in the thread.

       */

      public void run() {

        while (true) {

          try {

            repaint();

            sleep(500);

          }

          catch (InterruptedException e){}

        }

      }

    }

  }//EOC DigitalCLock





///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

////

/////////    ANAL OG CLOKK

////

///////////////////////////////////////////////////////////

///////////////////////////////////////////////////////////

  public class AnalogClock extends JComponent {

    private ClockThread clockThread = null;

    private Point center;

    private Point leftTop;

    private Point rightBottom;

    private double radius;



    /**

     * Default bean constructor.

     */

    public AnalogClock() {

      clockThread = new ClockThread();

      clockThread.start();

    }

    public Thread getThread() {

      return clockThread;

    }



    /**

     * Finds a font size which better fits the specified message to given sizes.

     * @param g current graphical context.

     * @param s string message.

     * @param w width of the rectangle.

     * @param h height of the rectangle.

     */

    private Font findFont(Graphics g, String s, int w, int h) {

      int size = 6;

      Font font = new Font(this.getFont().getName(), this.getFont().getStyle(), size);

      while (true) {

        FontMetrics m = g.getFontMetrics(font);

        if (m.getHeight() > h || m.stringWidth(s) > w)

          break;

        size++;

        font = new Font(this.getFont().getName(), this.getFont().getStyle(), size);

      }

      return font;

    }



    /**

     * Counts a point on the circle.

     * The center of the circle is in center variable.

     * @param radius radius of the circle.

     * @param min angle value in minutes.

     * @result point on the circle.

     */

    private Point countPoint(double radius, double min) {

      return new Point((int)(-radius * Math.sin(-3.14f/30 * min)) + center.x,

        (int)(-radius * Math.cos(3.14f/30 * min)) + center.y);

    }



    /**

     * Draws colored box.

     * @param g current graphics context.

     * @param x the x coordinat of the center of the box.

     * @param y the y coordinat of the center of the box.

     * @param w the width of the box.

     * @param c1 color of the left/top edge.

     * @param c2 color of the right/bottom edge.

     * @param c3 color of the box background.

     */

    private void drawColoredPoint(Graphics g, int x, int y, int w, Color c1,

      Color c2, Color c3) {

      g.setColor(c3);

      g.fillRect(x-w, y-w, w*2, w*2);

      g.setColor(c1);

      g.drawLine(x-w, y+w, x-w, y-w);

      g.drawLine(x-w, y-w, x+w-1, y-w);

      g.setColor(c2);

      g.drawLine(x+w, y-w, x+w, y+w);

      g.drawLine(x+w, y+w, x-w+1, y+w);

    }



    /**

     * Draws minute pointes of this clock.

     * @param g current graphics context.

     * @param min the angle in minutes.

     */

    private void drawPoint(Graphics g, double min) {

      Point p = countPoint(radius, min);

      int w = (int) (radius * 0.02);

      w = (w == 0) ? 1 : w;

      if (((int) min) % 5 == 0)

        drawColoredPoint(g, p.x, p.y, w, new Color(0, 255, 255), Color.black,

          new Color(0, 130, 132));

//      else

//        drawColoredPoint(g, p.x, p.y, w, Color.gray, Color.white, Color.lightGray);

    }



    /**

     * Draws the second arrow of this clock.

     * @param g current graphics context.

     * @param min the angle of the arrow in minutes.

     */

    private void drawSecArrow(Graphics g, double min) {

      Point p = countPoint(radius * 0.85, min);

//      g.setColor(new Color(41, 44, 49));

      g.setColor(Color.black);

      g.drawLine(center.x, center.y, p.x, p.y);

    }



    /**

     * Draws the thick arrow of this clock (for minutes or hours).

     * @param g current graphics context.

     * @param min the angle of the arrow in minutes.

     * @param l1 the forward length of the arrow.

     * @param l2 the backward length of the arrow.

     * @param l3 the side length of the arrow.

     * @param c the color of the arrow.

     */

    private void drawThickArrow(Graphics g, double min, double l1, double l2,

      double l3, Color c) {

      Point p1 = countPoint(radius * l1, min);

      Point p2 = countPoint(radius * l2, min + 30);

      Point p3 = countPoint(radius * l3, min + 15);

      Point p4 = countPoint(radius * l3, min + 45);

      int[] x = {p1.x, p3.x, p2.x, p4.x};

      int[] y = {p1.y, p3.y, p2.y, p4.y};

      g.setColor(c);

      g.fillPolygon(x, y, 4);

    }



    /**

     * Draws the minute arrow of this clock.

     * @param g current graphics context.

     * @param min the angle of the arrow in minutes.

     */

    private void drawMinArrow(Graphics g, double min) {

      Point oldCenter = center;

//      center = countPoint(3, min + 45);//45

//      drawThickArrow(g, min, 0.8, 0.15, 0.05, Color.white);

      center = countPoint(5, min + 15);

      drawThickArrow(g, min, 0.8, 0.15, 0.05, Color.gray);

      center = oldCenter;

//      drawThickArrow(g, min, 0.8, 0.15, 0.05, new Color(0, 130, 132));

      drawThickArrow(g, min, 0.8, 0.15, 0.05, Color.black);

    }



    /**

     * Draws the hour arrow of this clock.

     * @param g current graphics context.

     * @param min the angle of the arrow in minutes.

     */

    private void drawHourArrow(Graphics g, double min) {

      Point oldCenter = center;

//      center = countPoint(3, min + 45);//45

//      drawThickArrow(g, min, 0.6, 0.15, 0.07, Color.white);

      center = countPoint(5, min + 15);

      drawThickArrow(g, min, 0.6, 0.15, 0.07, Color.gray);

      center = oldCenter;

//      drawThickArrow(g, min, 0.6, 0.15, 0.07, new Color(0, 130, 132));

      drawThickArrow(g, min, 0.6, 0.15, 0.07, Color.black);

    }



    /**

     * Paints current time on the screen.

     * It uses current foreground and background colors fro drawing.

     * @param g current graphical context.

     */

    public void paint(Graphics g) {

      String strTime, strDate;

      Calendar calendar = new GregorianCalendar();

      SimpleDateFormat format = new SimpleDateFormat();

      double timeRate = (showDate) ? 0.6 : 0.8;



      // Find suitable font and position

      Dimension d = this.getSize();

  //    g.clearRect(0, 0, d.width, d.height);

//      g.fillRect(0, 0, d.width, d.height);



      // Draw current time

      leftTop = new Point((int)(d.getWidth() * 0.1), (int)(d.getHeight() * 0.1));

      rightBottom = new Point((int)(d.getWidth() * 0.9), (int)(d.getHeight()

        * (timeRate + 0.1)));

      center = new Point((rightBottom.x + leftTop.x) / 2,

        (rightBottom.y + leftTop.y) / 2);

      if (center.x < center.y)

        radius = (rightBottom.x - leftTop.x) / 2;

      else

        radius = (rightBottom.y - leftTop.y) / 2;

      Color orgColor = new Color(g.getColor().getRed(),g.getColor().getGreen(),g.getColor().getBlue(),g.getColor().getAlpha());

      g.setColor(Color.white);

      fillOval(g);

      for(int i = 0; i < 60; i++)

        drawPoint(g, i);

      drawHourArrow(g, calendar.get(Calendar.HOUR) % 12 * 5

        + calendar.get(Calendar.MINUTE) / 12);

      drawMinArrow(g, calendar.get(Calendar.MINUTE));

      drawSecArrow(g, calendar.get(Calendar.SECOND));



      // Draw current date

      if (showDate) {

        format.applyPattern(datePattern);

        strDate = format.format(calendar.getTime());

        Font font = findFont(g, strDate, (int)(d.getWidth() * 0.8),

          (int)(d.getHeight() * 0.2));

        FontMetrics m = g.getFontMetrics(font);

        int x = (d.width - m.stringWidth(strDate)) / 2;

        int y = (int) ((d.getHeight() * 0.2 - m.getHeight()) / 2

          + d.getHeight() * 0.7) + m.getAscent();

        g.setFont(font);

//AI        g.setColor(Color.black);

      //AI

        g.setColor(Color.white);

//        g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.height,m.getHeight()-m.getAscent()/2+1);

        g.fillRect(0,y-m.getHeight()+m.getAscent()/2+1,d.width,m.getHeight()-m.getAscent()/2+1);

        g.setColor(Color.black);

      //EAI

        g.drawString(strDate, x, y);

      }

    }

    /**

     * fills inside the clock which is actualy transparent ... by AI

     */

    private void fillOval(Graphics g) {

      int r2 = (int)radius * 2 + 6;

      int r = (int)r2/2-1;

      int x = center.x - r;

      int y = center.y - r;

      int w = r2;

      int h = r2;

      g.fillOval(x,y,w,h);

    }

    /**

     * Shows this bean in separate application frame on the screen.

     * It allows to use this bean as stand along application.

     */

  /*

    public static void main(String[] args) {

      CAnalogClock analogClock = new CAnalogClock();

      JFrame frame = new JFrame("Analog Clock");

      frame.setContentPane(analogClock);

  //    analogClock.setShowDate(false);

  //    analogClock.setBackground(Color.red);

      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      frame.setSize(300, 300);

      frame.setLocation(300, 300);

      frame.show();

    }

  */

  /**

   * Thread for display current time each 0.5 second.

   */

    private class ClockThread extends Thread {

      /**

       * Makes all work in the thread.

       */

      public void run() {

        while (true) {

          try {

            repaint();

            sleep(500);

          }

          catch (InterruptedException e){}

        }

      }

    }

  }//EOC AnalogClock



}

