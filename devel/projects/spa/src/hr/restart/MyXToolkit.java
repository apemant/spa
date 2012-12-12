package hr.restart;

import java.awt.Dimension;

//import sun.awt.X11.XToolkit;
/**
 * Kemija da bi mi aplikacija otvarala prozore samo u odredjenom kvadrantu mog super-duper-dual-monitor-big-ass-screen-setup-a.
 * Ocito radi samo za X11 (Linux i sl.)
 * Prije prikaza prvog ekrana bubnuti System.setProperty("awt.toolkit", "hr.restart.MyXToolkit"); - stoji zakomentirano u hr.restart.start
 */
public class MyXToolkit /*extends XToolkit*/ {
  public java.awt.Dimension getScreenSize() {
    return new Dimension(1366,768);
  };
}
