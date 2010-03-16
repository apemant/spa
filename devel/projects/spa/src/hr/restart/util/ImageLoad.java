package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.baza.Imageinfo;
import hr.restart.db.raVariant;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.util.OKpanel;

import javax.swing.Icon;
import javax.swing.JFrame;
import java.awt.*;

import javax.swing.*;

import com.borland.dx.sql.dataset.QueryDataSet;

import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;



public class ImageLoad implements ActionListener{

	
    static File lastChoosenDir = null;
	JraDialog f;
	JButton odaberiButton = new JButton( "Odaberi" );
	JButton spremiButton = new JButton( "Spremi" );
	JButton scanButton = new JButton("Scan");
	JButton openButton = new JButton("Otvori");
    JLabel lblSlika=new JLabel();
    OKpanel okp;
    File selectedFile=null;
    private String table;
    private String key;

	public void Img(Frame owner, String _table,String _key, String title) {
		setTable(_table);
		setKey(_key);
		f = new JraDialog(owner,true);
		f.setTitle((title == null)?"Odabir slike":title);
		okp=new OKpanel() {
			public void jBOK_actionPerformed() {
				save();
			}
			public void jPrekid_actionPerformed() {
				f.dispose();
				
			}
		};
		JPanel content = new JPanel();
		JPanel buttons = new JPanel(new GridLayout(1,0));
		content.setLayout(new BorderLayout());
		lblSlika.setHorizontalAlignment(SwingConstants.CENTER);
		lblSlika.setVerticalTextPosition(SwingConstants.BOTTOM);
		content.add(new JScrollPane(lblSlika),BorderLayout.CENTER);
	    content.add(okp, BorderLayout.SOUTH); 
	    buttons.add(odaberiButton);
	    if (!getScanCommand().equals("")) buttons.add(scanButton);
	    if (!getOpenCommand().equals("")) buttons.add(openButton);
	    okp.add(buttons,BorderLayout.WEST);
	    content.setPreferredSize(new Dimension(600, 300));
	    f.getContentPane().add(content);
	    f.setSize(new Dimension(600, 300));
	    startFrame.getStartFrame().centerFrame(f, 0 , f.getTitle());
	    okp.registerOKPanelKeys(f);
	    
	    odaberiButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	    	//  File defaultDirectory = new File("C:/Documents and Settings/Bruno/workspace/devel/projects/spa/src/hr/restart/util/images"); 
	    	 // JFileChooser fileChooser = new JFileChooser(defaultDirectory);
	    	JFileChooser fileChooser = new JFileChooser(lastChoosenDir);
	        int returnValue = fileChooser.showOpenDialog(null);
	        if (returnValue == JFileChooser.APPROVE_OPTION) {
	          selectedFile = fileChooser.getSelectedFile();
	        lastChoosenDir = selectedFile.getParentFile();
	        System.out.println(selectedFile.getName());
	       // lblSlika=new JLabel("aa");
	        displayChosen();
	        
	   //     content.add(lblSlika);
	     
	        }
	      }
	    });
	    scanButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        scanAction();
	      }
	    });
	    openButton.addActionListener(new ActionListener() {
	      public void actionPerformed(ActionEvent e) {
	        openAction();
	      }
	    });
	    load();
	    f.setVisible(true);
        
 	}
	
	protected void openAction() {
	  if (lastF == null) return;
	  try {
      Runtime.getRuntime().exec(new VarStr(getOpenCommand()).replaceAll("#", lastF.getAbsolutePath()).toString());
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  protected void scanAction() {
    try {
      int ch;
      Process proc = Runtime.getRuntime().exec(getScanCommand());
      proc.waitFor();
      while ((ch = proc.getErrorStream().read()) > -1) System.out.write(ch);
      while ((ch = proc.getInputStream().read()) > -1) System.out.write(ch);
      selectedFile = new File(getScanedFileName());
      displayChosen();
    } catch (Exception e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

  public static File getImgDir() {
		File imgdir = new File(System.getProperty("user.dir")+File.separator+"images");
		if (imgdir.exists() && !imgdir.isDirectory()) imgdir.delete();
		if (!imgdir.exists()) imgdir.mkdirs();
		return imgdir;
	}
	private void save() {
		if (selectedFile == null) {//sto znaci da nije odabrao sliku
		  System.out.println("slika nije odabrana");
			okp.jPrekid_actionPerformed();
			return;
		}
		//QueryDataSet set = Imageinfo.getDataModule().getFilteredDataSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
//		QueryDataSet set1 = Imageinfo.getDataModule().getFilteredDataSet(Condition.equal("tablica", table).and(Condition.equal("CKEY", key)));
//		QueryDataSet set2 = Imageinfo.getDataModule().getFilteredDataSet(Condition.whereAllEqual(new String[] {"tablica","ckey"}, new String[] {table, key}));
		set.open();
		if (set.getRowCount() == 0) {
			set.insertRow(false);
			set.setString("TABLICA", getTable());
			set.setString("CKEY", getKey());
		}
		String protocol = frmParam.getParam("sisfun", "imgloadproto","file","Kako snima slike pridruzene preko ImageLoada (file-lokalno, ftp-na server");
		String nejm = getTable()+"-"+getKey();
		String url = protocol+":"+nejm;
		if (protocol.equals("file")) {
			//treba ga kopirati u work.dir 
			File imgdir = getImgDir();
			File src = selectedFile;
			File dest = new File(imgdir.getAbsolutePath()+File.separator+nejm);
			
			try {
				FileHandler.copy(src, dest);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return;
			}
			
		} else if (protocol.equals("ftp")) {
			//treba ga sejvati na server preko raImageUtil
			raImageUtil u = new raImageUtil();
			u.saveImage(selectedFile, nejm);
		} else if (protocol.equals("db")) {
		  try {
        set.setInputStream("IMG", new FileInputStream(selectedFile));
      } catch (FileNotFoundException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
		}
		//.....
		set.setString("imgurl", url);
		set.setInt("img_id", getLastImgID());
		set.saveChanges();
		okp.jPrekid_actionPerformed();
	}
	private int getLastImgID() {
		QueryDataSet maxset = Util.getNewQueryDataSet("SELECT max(img_id) as mxid from imageinfo");
		maxset.open();
		return Integer.parseInt(raVariant.getDataSetValue(maxset, "MXID").toString())+1;
	}
	QueryDataSet set = null;
	private void load() {
		set = Imageinfo.getDataModule().getFilteredDataSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
//		QueryDataSet set1 = Imageinfo.getDataModule().getFilteredDataSet(Condition.equal("tablica", table).and(Condition.equal("CKEY", key)));
//		QueryDataSet set2 = Imageinfo.getDataModule().getFilteredDataSet(Condition.whereAllEqual(new String[] {"tablica","ckey"}, new String[] {table, key}));
		set.open();
		if (set.getRowCount() == 0) return;
		//url tipa ili ftp:ime ili file:ime
		//parsires url
		//ucitas
		// nabijes na jlabel
		ImageIcon ic = loadImage(set);

		if (ic!=null) {
			lblSlika.setIcon(ic);
			lblSlika.setText(set.getString("IMGURL"));
			f.pack();
		} else {
		  lblSlika.setText("(nije odabrano)");
			System.out.println(" ic is null!!???!?!?");
		}
		
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		
	}
	private static File lastF = null;
	public static ImageIcon loadImage(QueryDataSet set) {
	    String[] parsed = parseUrl(set.getString("IMGURL"));
	    String protocol = parsed[0];
	    String file = parsed[1];
      ImageIcon imi = loadImage(protocol, file);
      if (imi != null) return imi;
      if (protocol.equals("db")) {
        try {
          BufferedInputStream is = new BufferedInputStream( set.getInputStream("IMG"));
          byte[] bytes = new byte[is.available()];
          is.read(bytes);
          return new ImageIcon(Toolkit.getDefaultToolkit().createImage(bytes));
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      return null;
	}
	private static String[] parseUrl(String url) {
    StringTokenizer tok = new StringTokenizer(url,":");
    String protocol = tok.nextToken();
    String file = tok.nextToken();
    return new String[] {protocol, file};
	}
	public static ImageIcon loadImage(String url) {
    String[] parsed = parseUrl(url);
    String protocol = parsed[0];
    String file = parsed[1];
    return loadImage(protocol, file);
	}
  public static ImageIcon loadImage(String protocol, String file) {
    if (protocol.equals("ftp")) {
        ImageIcon imi = new raImageUtil().loadImage(file);
        lastF = raImageUtil.getLastLoadedFile();
        return imi;
    } else if (protocol.equals("file")) {
        try {
            System.out.println(" Loadin' imidj :: "+file);
            lastF = new File(getImgDir().getAbsolutePath()+File.separator+file);
            return new ImageIcon(
                Toolkit.getDefaultToolkit().createImage(getImgDir().getAbsolutePath()+File.separator+file)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    return null;
  }

	public String getTable() {
		if (table == null) return "UNKNOWN";
		return table;
	}


	public void setTable(String table) {
		this.table = table;
	}


	public String getKey() {
		if (key == null) return "UNKNOWN";
		return key;
	}


	public void setKey(String key) {
		this.key = key;
	}
		
	private String getScanCommand() {
	  return frmParam.getParam("sisfun", "imgScanComm", "", "Komanda za direktno skeniranje slika iz ImageLoad-a");
	}
	private String getOpenCommand() {
	  return frmParam.getParam("sisfun", "imgOpenComm", "", "Komanda za otvaranje slika iz ImageLoad-a");
	}
  private String getScanedFileName() {
    return frmParam.getParam("sisfun", "scanedFN", "/tmp/scntmp", "Naziv file-a u koji imgScanComm pohranjuje rezultat");
  }

  private void displayChosen() {
    lastF = selectedFile;
    ImageIcon icon = new ImageIcon(selectedFile.getPath());
    lblSlika.setIcon(icon);
    f.pack();
  }
	
}
