package hr.restart.help;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JEditorPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.EditorKit;
import javax.swing.text.html.HTMLEditorKit;

import bsh.EvalError;
import bsh.Interpreter;

import com.borland.dx.dataset.NavigationEvent;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.Mesg;
import hr.restart.sisfun.raUser;
import hr.restart.swing.JraScrollPane;
import hr.restart.swing.JraTable2;
import hr.restart.swing.raTableModifier;
import hr.restart.util.Aus;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.raImages;
import hr.restart.util.raMatPodaci;
import hr.restart.util.raNavAction;
import hr.restart.util.raNavBar;


public class frmMessages extends raMatPodaci {

	static frmMessages instance;
	
  XYLayout lay = new XYLayout();
  
  JraScrollPane vp = new JraScrollPane();
  
  JEditorPane msg = new JEditorPane() {
    public boolean getScrollableTracksViewportWidth() {
      return true;
    }
 };
 
   raNavAction rnvSend = new raNavAction("Po�alji", raImages.IMGCOMPOSEMAIL, KeyEvent.VK_F2) {
     public void actionPerformed(ActionEvent e) {
       send();
     }
   };
 	raNavAction rnvRead = new raNavAction("Ozna�i", raImages.IMGDELETE, KeyEvent.VK_F3) {
 	  public void actionPerformed(ActionEvent e) {
 	    mark();
 	  }
 	};
 	raNavAction rnvReadAll = new raNavAction("Ozna�i sve", raImages.IMGDELALL, KeyEvent.VK_F3, KeyEvent.SHIFT_MASK) {
 	   public void actionPerformed(ActionEvent e) {
 	     markAll();
 	   }
 	};
  
  
  public frmMessages() {
    super(2);
    try {
    	instance = this;
      jbInit();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
  
  public static frmMessages getInstance() {
  	return instance;
  }
  
  public void table2Clicked() {
  	//
  }
  
  public void SetFokus(char mode) {
    //
  }

  public boolean Validacija(char mode) {
    // 
    return false;
  }
  
  public void switchPanel(boolean prvi,boolean drugi){
  	super.switchPanel(prvi, true);
  }
  
  public boolean isNormal() {
  	return false;
  }
  
  void send() {
    raSendMessage.show(this);
  }
  
  void mark() {
  	if (getRaQueryDataSet().rowCount() == 0) return;
  	if (!getRaQueryDataSet().getString("NOVA").equals("D")) return;
  	
  	getRaQueryDataSet().setString("NOVA", "N");
  	getRaQueryDataSet().saveChanges();
  	getRaQueryDataSet().emptyRow();
  	getJpTableView().fireTableDataChanged();
  	MsgDispatcher.refresh();
  	jeprazno();
  	if (getRaQueryDataSet().rowCount() == 0) msg.setText("");
  }
  
  void markAll() {
  	if (getRaQueryDataSet().rowCount() == 0) return;
  	Valid.getValid().runSQL("UPDATE mesg SET nova='N' WHERE dest='" + raUser.getInstance().getUser() + "' AND nova='D'");
  	getRaQueryDataSet().refresh();
  	getJpTableView().fireTableDataChanged();
  	MsgDispatcher.refresh();
  	jeprazno();
  }
  
  public void beforeShow() {
  	Aus.setFilter(getRaQueryDataSet(), "SELECT * FROM mesg WHERE dest='" + raUser.getInstance().getUser() + "' AND nova='D'");
  	getRaQueryDataSet().open();
  	setSort(new String[] {"DATUM"});
  }
  
  public void raQueryDataSet_navigated(NavigationEvent e) {
  	if (getRaQueryDataSet().rowCount() == 0) msg.setText("");
  	else {
  		String tx = getRaQueryDataSet().getString("MTEXT");
  		EditorKit kit = tx.startsWith("<html>") || tx.startsWith("<HTML>") ? html : def;
  		if (msg.getEditorKit() != kit) msg.setEditorKit(kit);
  		msg.setText(tx);
  	}
  }

  EditorKit def = msg.getEditorKit();
  EditorKit html = new HTMLEditorKit();
  private void jbInit() throws Exception {

    msg.setEditable(false);
    //msg.setEditorKit(new HTMLEditorKit());
    msg.addHyperlinkListener(new HyperlinkListener() {
			public void hyperlinkUpdate(HyperlinkEvent e) {
				if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
					Interpreter bsh = new Interpreter(); 
					try {
						bsh.eval(e.getDescription());
					} catch (EvalError e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
    });

    setRaQueryDataSet(Mesg.getDataModule().getFilteredDataSet("1=0"));
    getRaQueryDataSet().getColumn("DATUM").setDisplayMask("dd-MM-yyyy  'u' HH:mm:ss");
    getRaQueryDataSet().getColumn("DATUM").setWidth(24);
    getRaQueryDataSet().getColumn("SRC").setWidth(10);
    getRaQueryDataSet().getColumn("DEST").setWidth(10);
    
    removeRnvCopyCurr();
    getNavBar().removeStandardOptions(new int[] {raNavBar.ACTION_ADD, raNavBar.ACTION_DELETE, 
    		raNavBar.ACTION_UPDATE, raNavBar.ACTION_PRINT, raNavBar.ACTION_TOGGLE_TABLE});
    
    addOption(rnvSend, 0, false);
    addOption(rnvRead, 1, true);
    addOption(rnvReadAll, 2, true);

    setVisibleCols(new int[] {1,3,4});
    getJpTableView().getMpTable().setPreferredScrollableViewportSize(new Dimension(500, 150));
    vp.setPreferredSize(new Dimension(500, 200));
    vp.setViewportView(msg);
    jpDetailView.add(vp);
    
    getJpTableView().addTableModifier(new raTableModifier() {
      Variant v = new Variant();
      public boolean doModify() {
        return isColumn("MTEXT");
      }
      public void modify() {
        ((JraTable2) getTable()).getDataSet().getVariant("MTEXT", getRow(), v);
        String text = v.getString().trim();
        if (text.startsWith("<html>") || text.startsWith("<HTML>")) {
        	text = removeHtml(text);
        	int nl = text.indexOf('\n');
          if (nl > 0)
            text = text.substring(0, nl);
          setComponentText(text);
        } else {
        	int nl = text.indexOf('\n');
        	if (nl > 0)
        		setComponentText(text.substring(0, nl));
        }
      }
      String removeHtml(String text) {
      	VarStr v = new VarStr(text);
      	int b, e;
      	while (((b = v.indexOf('<')) >= 0) && ((e = v.indexOf('>')) >= 0) && (b < e))
      		v.replace(b, e + 1, "");
      	return v.toString();
      }
    });
  }
}
