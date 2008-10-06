/****license*****************************************************************
**   file: MenuDumper.java
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
 * Created on Sep 29, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package hr.restart.util;

import hr.restart.start;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
/**
 * @author andrej
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MenuDumper {
  public static boolean toDB = false;
  
  /**
   * dumping menus in some html form
   */
  public MenuDumper() {
    super();
    // TODO Auto-generated constructor stub
  }
  // dumping menus in some html form
  public void printMenus(String prefix, String suffix, String encoding) {
    //read res contents
    String[][] rcontents = start.getResContents();
    for (int i=0;i<rcontents.length;i++) {
      if (rcontents[i][0].startsWith("APL")) {
        try {
          String module = rcontents[i][0].substring(3);
          //get menu bar
          JMenuBar bar;
          bar = ((startFrame)raLoader.load(rcontents[i][1])).getRaJMenuBar();
          // open file
//          System.out.println("writing "+module+".menudump");
          FileHandler fh = new FileHandler(module+".menudump");
          fh.openWrite(encoding);
          if (!toDB) fh.writeln(prefix);
          for (int m=0;m<bar.getMenuCount();m++) {
            printMenu(bar.getMenu(m),fh, prefix, suffix);
          }
          if (!toDB) fh.writeln(suffix);
          fh.close();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
    
  }
  private String tabs="  ";
  public void printMenu(JMenu menu, FileHandler fh, String prefix, String suffix) {
    if (fh!=null) {
      if (menu.getText().equals("Zajednièki podaci") && !fh.getFileName().startsWith("zapod")) return;      
    }
    if (menu.getText().equals("Prozori")) return;
    if (menu.getText().equals("Pomoæ")) return;
    String menutext = menu.getText();
//    System.out.println(menu.getParent().getClass());
    if (menu.getParent() instanceof JMenuBar) {
//      System.out.println("toUpperCase");
      menutext = menutext.toUpperCase();
    }
    if (toDB) {
      //pisaj jmenu u bazu
    } else {
      fh.writeln(tabs+"<li>"+menutext+"</li>");
      fh.writeln(tabs+prefix);      
    }
    tabs = tabs+"  ";
    for (int i=0;i<menu.getMenuComponentCount();i++) {
      if (menu.getMenuComponent(i) instanceof javax.swing.JMenu) {
        printMenu((javax.swing.JMenu)menu.getMenuComponent(i),fh, prefix, suffix);
      } else if (menu.getMenuComponent(i) instanceof javax.swing.JMenuItem){
        //addOption((javax.swing.JMenuItem)menu.getMenuComponent(i),menunode);
        if (toDB) {
          //pisaj menuitem u bazu
        } else {
          fh.writeln(tabs+"<li>"+((javax.swing.JMenuItem)menu.getMenuComponent(i)).getText()+"</li>");          
        }
      }
    }
    tabs = tabs.substring(2);
    if (!toDB) fh.writeln(tabs+suffix);
  }
  public static void main(String[] args) {
    String arg1, arg2, arg3;
    if (args.length<2) {
      arg1 = "<ul>";
      arg2 = "</ul>";
    } else {
      arg1 = args[0];
      arg2 = args[1];
    }
    if (args.length==3) {
      arg3 = args[2];
    } else {
      arg3 = "UTF8";
    }
    new MenuDumper().printMenus(arg1,arg2,arg3);
   
    System.exit(0);
  }
  //eof dumping menus in some html form
}
