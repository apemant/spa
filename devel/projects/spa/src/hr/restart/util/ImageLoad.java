package hr.restart.util;

import hr.restart.baza.Condition;
import hr.restart.baza.Imageinfo;
import hr.restart.db.raVariant;
import hr.restart.sisfun.frmParam;
import hr.restart.swing.AWTKeyboard;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraDialog;
import hr.restart.swing.KeyAction;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.image.ImageProducer;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;

import com.borland.dx.sql.dataset.QueryDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;



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
    private boolean multi;
    
    JraButton add = new JraButton();
    JraButton del = new JraButton();
    JraButton prev = new JraButton();
    JraButton next = new JraButton();
    JLabel info = new JLabel();
    
    JPanel content;
    
    public void Img(Frame owner, String _table, String _key, String title, boolean multi) {
      this.multi = true;
      setTable(_table);
      setKey(_key);
      f = new JraDialog(owner, true);
      f.setTitle((title == null) ? "Odabir slike" : title);
      
      content = new JPanel(new BorderLayout());
      JPanel comms = new JPanel(new XYLayout(400, 35));
      comms.add(add, new XYConstraints(15, 5, 100, 27));
      comms.add(del, new XYConstraints(120, 5, 100, 27));
      comms.add(prev, new XYConstraints(250, 5, 40, 27));
      comms.add(info, new XYConstraints(295, 5, 50, 27));
      comms.add(next, new XYConstraints(350, 5, 40, 27));
      
      Aus.recursiveUpdateSizes(comms);
      
      add.setText("Dodaj sliku");
      del.setText("Ukloni");
      prev.setText("<");
      next.setText(">");
      info.setHorizontalAlignment(SwingConstants.CENTER);
      info.setVerticalAlignment(SwingConstants.CENTER);
      
      add.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser(lastChoosenDir);
            int returnValue = fileChooser.showOpenDialog(null);
            if (returnValue == JFileChooser.APPROVE_OPTION) {
              selectedFile = fileChooser.getSelectedFile();
              lastChoosenDir = selectedFile.getParentFile();
              System.out.println(selectedFile.getName());
              save();
              images.add(loadImage(set));
              active = images.size();
              updateView();
            }
          }
        });
      prev.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (active > 1) {
            active--;
            updateView();
          }
        }
      });
      next.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (active < images.size()) {
            active++;
            updateView();
          }
        }
      });
      del.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          if (images.size() == 0) return;
          if (JOptionPane.showConfirmDialog(f, "Želite li obrisati sliku?", "Brisanje", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
          
          delete();
          updateView();
        }
      });
      
      
      lblSlika.setHorizontalAlignment(SwingConstants.CENTER);
      lblSlika.setVerticalTextPosition(SwingConstants.BOTTOM);
      lblSlika.setVerticalAlignment(SwingConstants.CENTER);
      content.add(new JScrollPane(lblSlika));
      //content.add(lblSlika);
      content.add(comms, BorderLayout.NORTH); 
      content.setPreferredSize(new Dimension(600, 600));
      content.addComponentListener(new ComponentListener() {
        public void componentShown(ComponentEvent e) {
        }
        public void componentResized(ComponentEvent e) {
          updateView();
        }
        public void componentMoved(ComponentEvent e) {
        }
        public void componentHidden(ComponentEvent e) {
        }
      });
      f.getContentPane().add(content);
      startFrame.getStartFrame().centerFrame(f, 0 , f.getTitle());
      raProcess.runChild(new Runnable() {
        public void run() {
          loadMulti();          
        }
      });
      
      AWTKeyboard.registerKeyStroke(f, AWTKeyboard.ESC, new KeyAction() {
        public boolean actionPerformed() {
          f.setVisible(false);
          return true;
        }
      });
      f.setVisible(true);
    }
    
	public void Img(Frame owner, String _table,String _key, String title) {
	  multi = false;
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
      String subdir = frmParam.getParam("sisfun", "imgsubdir", "images", "Ime mape za slike");
      String dir = frmParam.getParam("sisfun", "imgdir", "", "Putanja do mape sa slikama", true);
      File imgdir = new File(System.getProperty("user.dir"), subdir);
      if (dir != null && dir.length() > 0) imgdir = new File(dir, subdir);
      
      if (imgdir.exists() && !imgdir.isDirectory()) imgdir.delete();
      if (!imgdir.exists()) imgdir.mkdirs();
      return imgdir;
	}
  
  public void deleteAll(String _table, String _key) {
    setTable(_table);
    setKey(_key);
    set = Imageinfo.getDataModule().openTempSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
    active = 1;
    int total = set.rowCount();
    for (int i = 0; i < total; i++) delete();
  }
  
  void delete() {
    set.goToRow(active - 1);
    
    if (Imageinfo.getDataModule().getRowCount(Condition.equal("IMGURL", set)) == 1) {
      System.out.println("Only 1 image, deleting...");
      String[] parsed = parseUrl(set.getString("IMGURL"));
      String protocol = parsed[0];
      String file = parsed[1];
      if (protocol.equals("file")) {
        File img = new File(getImgDir(), file);
        if (img.exists() && img.canWrite())
          img.delete();
      } else if (protocol.equals("ftp")) {
        raImageUtil u = new raImageUtil();
        u.deleteImage(file);
      } else if (protocol.equals("cloud")) {
        AmazonHandler ah = new AmazonHandler("amazonImages");
        ah.deleteFile(file);
      }
    }
    set.deleteRow();
    set.saveChanges();
    if (images.size() > 0) {
      images.remove(active - 1);
      if (active > images.size()) active = images.size();
    }
  }
  
  public QueryDataSet saveAll(File[] imgs, String _table, String _key) {
    setTable(_table);
    setKey(_key);
    set = Imageinfo.getDataModule().openTempSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
    multi = true;
    
    for (int i = 0; i < imgs.length; i++) {
      selectedFile = imgs[i];
      save();
    }
    return set;
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
		if (set.getRowCount() == 0 || multi) {
		  set.last();
			set.insertRow(false);
			set.setInt("img_id", getLastImgID());
			set.setString("TABLICA", getTable());
			set.setString("CKEY", getKey());
			set.saveChanges();
		}
		String protocol = frmParam.getParam("sisfun", "imgloadproto","file","Kako snima slike pridruzene preko ImageLoada (file-lokalno, ftp-na server, db-u bazu, cloud");
		String nejm = getTable()+"-"+getKey()+"-"+set.getInt("IMG_ID");
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
		} else if (protocol.equals("cloud")) {
		  AmazonHandler ah = new AmazonHandler("amazonImages");
		  ah.putFile(selectedFile, nejm);
		}
		//.....
		set.setString("imgurl", url);
		//set.setInt("img_id", getLastImgID());
		set.saveChanges();
		if (!multi) okp.jPrekid_actionPerformed();
	}
	private int getLastImgID() {
		QueryDataSet maxset = Util.getNewQueryDataSet("SELECT max(img_id) as mxid from imageinfo");
		maxset.open();
		return Integer.parseInt(raVariant.getDataSetValue(maxset, "MXID").toString())+1;
	}
	QueryDataSet set = null;
	private void load() {
		set = Imageinfo.getDataModule().getTempSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
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
	
	int active = 1;
	List images = new ArrayList();
	void loadMulti() {
	  set = Imageinfo.getDataModule().openTempSet("tablica = '"+getTable()+"' AND ckey = '"+getKey()+"'");
	  images.clear();
	  
	  for (set.first(); set.inBounds(); set.next()) {
	    images.add(loadImage(set));
	  }
	  active = 1;
	  updateView();
	}
	
	void updateView() {
	  if (images.size() == 0) {
	    info.setText("0 / 0");
	    lblSlika.setText("(nije odabrano)");
	    lblSlika.setIcon(null);
	    prev.setEnabled(false);
	    next.setEnabled(false);
	    del.setEnabled(false);
	  } else {
	    info.setText(active + " / " + images.size());
	    lblSlika.setText(null);
	    ImageIcon ic = (ImageIcon) images.get(active - 1);
/*	    if (ic.getIconHeight() > content.getHeight() || ic.getIconWidth() > content.getWidth()) {
	      double hratio = (double) ic.getIconWidth() / content.getWidth();
	      double vratio = (double) ic.getIconHeight() / content.getHeight();
	      int w = hratio > vratio ? content.getWidth() : ic.getIconWidth() * content.getHeight() / ic.getIconHeight();
	      int h = hratio > vratio ? ic.getIconHeight() * content.getWidth() / ic.getIconWidth() : content.getHeight();
	      lblSlika.setIcon(new ImageIcon(ic.getImage().getScaledInstance(w, h, java.awt.Image.SCALE_SMOOTH)));
	    } else */
	    lblSlika.setIcon(ic);
        prev.setEnabled(active > 1);
        next.setEnabled(active < images.size());
        del.setEnabled(true);
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
	
	public static Image loadImageArt(String cart) {
	  QueryDataSet ds = Imageinfo.getDataModule().getQueryDataSet();
	  ds.open();
	  System.out.println("cart " + cart);
	  lookupData.getlookupData().raLocate(ds, new String[] {"TABLICA","CKEY"}, new String[] {"artikli", cart});
	  System.out.println(ds);
	  return loadImage(ds).getImage();
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
	public static File loadFile(String url) {
	    String[] parsed = parseUrl(url);
	    String protocol = parsed[0];
	    String file = parsed[1];
	    return loadFile(protocol, file);
	}
	public static File loadFile(String protocol, String file) {
	  if (protocol.equals("ftp"))
	    return new raImageUtil().loadFile(file);
      if (protocol.equals("file"))
        try {
            return new File(getImgDir().getAbsolutePath(),file);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
      if (protocol.equals("cloud")) {
        AmazonHandler ah = new AmazonHandler("amazonImages");
        return ah.getFile(file);
      }
      return null;
	}
  public static ImageIcon loadImage(String protocol, String file) {
    if (protocol.equals("ftp")) {
        ImageIcon imi = new raImageUtil().loadImage(file);
        lastF = raImageUtil.getLastLoadedFile();
        return imi;
    } else if (protocol.equals("file")) {
        try {
            System.out.println(" Loadin' imidj :: "+file);
            lastF = new File(getImgDir().getAbsolutePath(),file);
            return new ImageIcon(
                Toolkit.getDefaultToolkit().createImage(getImgDir().getAbsolutePath()+File.separator+file)
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    } else if (protocol.equals("cloud")) {
      AmazonHandler ah = new AmazonHandler("amazonImages");
      return ah.readImage(file);
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
