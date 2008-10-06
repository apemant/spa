/****license*****************************************************************
**   file: raLiteBrowser.java
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
 * raLiteBrowser.java
 *
 * Created on 2003. listopad 16, 16:46
 */

package hr.restart.help;

import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraScrollPane;
import hr.restart.util.raImages;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.swing.JEditorPane;
/**
 *
 * @author  andrej
 */
public class raLiteBrowser extends hr.restart.swing.JraFrame {
  private raNavBar navBar = new raNavBar(raNavBar.EMPTY);
  private JEditorPane pane;
  private JraScrollPane scroll;
  private ArrayList history = new ArrayList();
  private int historyPosition = 0;
  URL url;
  /** Creates a new instance of raLiteBrowser */
  public raLiteBrowser(URL _url) {
    url = _url;
    jinit();
  }
  public raLiteBrowser() {
    this(getHelpURL());
  }
  private void jinit() {
    setIconImage(raImages.getImageIcon(raImages.IMGRAICON).getImage());
    try {
      pane = new JEditorPane(url);
      pane.setEditable(false);
      pane.addHyperlinkListener(new javax.swing.event.HyperlinkListener() {
        public void hyperlinkUpdate(javax.swing.event.HyperlinkEvent e) {
	          if (e.getEventType() == javax.swing.event.HyperlinkEvent.EventType.ACTIVATED) {
		      JEditorPane pane = (JEditorPane) e.getSource();
                      addHis();
/*System.out.println("pane.getPage() := " + pane.getPage());
System.out.println("scroll.getViewport().getViewRect().y:= " + scroll.getViewport().getViewRect().y);
System.out.println("e.getURL():= " + e.getURL());
 */

		      if (e instanceof javax.swing.text.html.HTMLFrameHyperlinkEvent) {
		          javax.swing.text.html.HTMLFrameHyperlinkEvent  evt = (javax.swing.text.html.HTMLFrameHyperlinkEvent)e;
		          javax.swing.text.html.HTMLDocument doc = (javax.swing.text.html.HTMLDocument)pane.getDocument();
		          doc.processHTMLFrameHyperlinkEvent(evt);
		      } else {
		          try {
			      pane.setPage(e.getURL());
		          } catch (Throwable t) {
			      t.printStackTrace();
		          }
		      }
                      addHis();
                      //historyPosition--;
	          }
	      }
      });
    } catch (Exception ex) {
      ex.printStackTrace();
      pane = new JEditorPane();
      pane.setText("Nije moguce prikazati tekst. File upute.jar bi trebao biti u classpathu \n" + ex +"\n"+ex.getMessage());
    }
    setTitle("SPA - Upute");
    scroll = new JraScrollPane(pane);
    getContentPane().add(scroll,BorderLayout.CENTER);
    initNavBar();
    addHis();
  }
  public void pack() {
    super.pack();
    int sfrH = 0;
    if (hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame() != null) {
      sfrH = hr.restart.util.raLLFrames.getRaLLFrames().getMsgStartFrame().getSize().height;
    }
    setSize(hr.restart.start.getSCREENSIZE().width, hr.restart.start.getSCREENSIZE().height-sfrH);
    setLocation(0, sfrH);
  }
  
  public static void openSystemBrowser(URL url) {
    String comm = frmParam.getParam("sisfun", "webbrowser", "", "Komanda za pokretanje web browsera", true);
    if (comm.equals("")) {
      String os = System.getProperty("os.name");
      if (os.toLowerCase().startsWith("win")) {
        comm = "rundll32 url.dll,FileProtocolHandler";
      } else if (os.toLowerCase().startsWith("mac")) {
        comm = "open";
      } else if (os.toLowerCase().startsWith("linux")) {
        comm = "firefox";
      }
    }
    try {
      Runtime.getRuntime().exec(comm + " " + url);
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  public static URL getHelpURL() {
    return ClassLoader.getSystemClassLoader().getResource("opce/opce.html");
  }
  private void initNavBar() {
    navBar.addOption(new raNavAction("Nazad", raImages.IMGBACK, KeyEvent.VK_LEFT, KeyEvent.CTRL_MASK) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        back();
      }
    });
    navBar.addOption(new raNavAction("Pocetak", raImages.IMGHOME, KeyEvent.VK_HOME) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        home();
      }
    });
    navBar.addOption(new raNavAction("Naprijed", raImages.IMGFORWARD, KeyEvent.VK_RIGHT, KeyEvent.CTRL_MASK) {
      public void actionPerformed(java.awt.event.ActionEvent ev) {
        forward();
      }
    });
    getContentPane().add(navBar, BorderLayout.NORTH);
    navBar.registerNavBarKeys(this);
  }
  
  void addHis() {
    int newPos = history.size();
    history.add(newPos, new raLiteBrowser.HistoryInfo(pane.getPage(), scroll.getViewport().getViewPosition().y));
    historyPosition = newPos;
  }
  
  void moveHis(int position) {
    try {
      HistoryInfo info = (HistoryInfo)history.get(position);
      info.moveTo(pane, scroll);
      historyPosition=position;
    } catch (Exception ex) {
    }

  }
  
  void back() {
    moveHis(historyPosition - 1);
  }
  
  void forward() {
    moveHis(historyPosition + 1);    
  }
  
  void printHis() {
    for (int i=0; i<history.size(); i++) {
      System.out.println(i + " := "+history.get(i));
    }
    //System.out.println("POSITION: "+historyPosition);
  }
  
  void home() {
    addHis();
    try {
      ((raLiteBrowser.HistoryInfo)history.get(0)).moveTo(pane, scroll);
      historyPosition++;
    } catch (Exception ex) {
    }
  }

  class HistoryInfo {
    URL hURL;
    int hy;
    
    public HistoryInfo(URL _url, int _y) {
      hURL = _url;
      hy = _y;
    }
    public void moveTo(JEditorPane pane, JraScrollPane scroll) throws java.io.IOException {
      pane.setPage(hURL);
      scroll.getViewport().setViewPosition(new java.awt.Point(0, hy));
    }
    public String toString() {
      return hURL.getFile()+"#"+hy;
    }
  }
  
}
