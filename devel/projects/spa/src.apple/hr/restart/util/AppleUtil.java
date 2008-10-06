package hr.restart.util;

import hr.restart.help.raUserDialog;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JLabel;

import com.apple.eawt.Application;

public class AppleUtil {
  /*
   * apple.laf.useScreenMenuBar If you are using the Aqua look and feel, this
   * property puts Swing menus in the Mac OS X menu bar. Note that JMenuBars in
   * JDialogs are not moved to the Mac OS X menu bar.
   * 
   * The default value is false. Java applications created with Xcode have this
   * property set to true.
   * 
   * apple.awt.brushMetalLook Allows you to display your main windows with the
   * “textured” Aqua window appearance. This property should be applied only to
   * the primary application window, and should not affect supporting windows
   * like dialogs or preference windows.
   * 
   * The default value is false.
   * 
   * apple.awt.fileDialogForDirectories By default, the AWT File Dialog lets you
   * choose a file. Under certain circumstances, however, it may be proper for
   * you to choose a directory instead. If that is the case, set this property
   * to allow for directory selection in a file dialog.
   * 
   * The default value is false.
   * 
   * apple.awt.showGrowBox Most native Mac OS X windows have a resize control in
   * the bottom-right corner. By default, Java application windows that use the
   * Aqua look and feel show this control, but there may be circumstances where
   * you want it to be hidden. This property is used to decide if the grow box
   * is shown or not.
   * 
   * The default value is true.
   */
  /*
   * OLD? PARAMETERS: -Dcom.apple.macos.useScreenMenuBar=true
   * -Dcom.apple.mrj.application.apple.menu.about.name=JUnit
   * -Dcom.apple.mrj.application.growbox.intrudes=false
   * -Dcom.apple.mrj.application.live-resize=true
   * -Dcom.apple.macos.smallTabs=true
   */
  /*
   * JVM PARAMS -Xdock:name="Sustav Poslovnih Aplikacija"
   * -Xdock:icon=/path/to/icon
   */
  public static void bootInit() {
    System.out.println("invoking AppleUtil.bootInit ...");
    System.setProperty("apple.laf.useScreenMenuBar", "true");
    System.setProperty("apple.awt.showGrowBox", "false");
    // System.setProperty("apple.awt.brushMetalLook","true");
    System.setProperty("com.apple.macos.smallTabs", "true");// 1.3 only
    System
        .setProperty("com.apple.mrj.application.apple.menu.about.name", "SPA");
    Application app = Application.getApplication();
    if (!app.isAboutMenuItemPresent()) {
      app.addAboutMenuItem();
    }
    app.setEnabledAboutMenu(true);
    if (!app.isPreferencesMenuItemPresent()) {
      app.addPreferencesMenuItem();
    }
    app.setEnabledPreferencesMenu(true);

    app.addApplicationListener(new SPAApplicationListener());
  }

  public static void startFrameInit(final startFrame sfr) {
    sfr.setUndecorated(true);
  }

  public static void userDialogInit(raUserDialog ud) {
    ud.setUndecorated(true);
  }

  public static int getStartFrameRelativeY() {
    return 21; // pola od 42:)
  }
  public static void initStatusBar(raStatusBar bar) {
    bar.jlDate.setIcon(raImages.getImageIcon(raImages.IMGRAICON));
  }
  public static String getStatusBarDate() {
    return raLLFrames.getRaLLFrames().getMsgStartFrame().getTitle();
  }
  /*
   * Ne radi kako treba jer OSX negdje drzi referencu na menubar :)
   */
  public static void registerWithMenuBar(raFrame fr) {
    if ("A".equals("A"))
      return;// dont you even think about it
    if (fr == null)
      return;
    WindowListener[] larr = (WindowListener[]) (fr.getWindow()
        .getListeners(WindowListener.class));
    if (larr.length > 0) {
      for (int i = 0; i < larr.length; i++) {
        if (larr[i] instanceof MenuBarWinListener) {
          System.out.println("MenuBarWinListener found !!! removing ...");
          fr.removeWindowListener((MenuBarWinListener) larr[i]);
        }
      }
    }
    System.out.println("Adding windowListener for " + fr.getTitle());
    fr.addWindowListener(new MenuBarWinListener(fr));
  }
}
