package hr.restart.util;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.dx.dataset.Variant;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;

import hr.restart.baza.dM;
import hr.restart.sisfun.TextFile;
import hr.restart.sisfun.frmTableDataView;
import hr.restart.swing.JDirectoryChooser;
import hr.restart.swing.JraButton;
import hr.restart.swing.JraCheckBox;
import hr.restart.swing.JraTextField;
import hr.restart.swing.raInputDialog;
import hr.restart.swing.raTableModifier;

public class raSearchTextFiles extends raInputDialog {
	
	private JPanel jp = new JPanel();
	private JraTextField path = new JraTextField() {
    public void focusGained(java.awt.event.FocusEvent e) {}
    public boolean maskCheck() { return true; }
  };
  private JraButton doh = new JraButton();
  private JDirectoryChooser dc = new JDirectoryChooser();
  private JraTextField mask = new JraTextField() {
    public void focusGained(java.awt.event.FocusEvent e) {}
    public boolean maskCheck() { return true; }
  };
  private JraTextField text = new JraTextField() {
    public void focusGained(java.awt.event.FocusEvent e) {}
    public boolean maskCheck() { return true; }
  };
  private JraCheckBox recur = new JraCheckBox(" Provjeri poddirektorije ");
  private StorageDataSet results = new StorageDataSet();
	
	public void show() {
		if (super.show(null, jp, "Traži tekst")) {
			IntParam.setTag("lastSearchDir", path.getText());
			IntParam.setTag("lastSearchMask", mask.getText());
			results.empty();
			raGlob glob = mask.isEmpty() ? null : new raGlob(mask.getText());
			search(new File(path.getText()), recur.isSelected(), glob, text.getText());
			showResults();
		}
	}
	
	public void showResults() {
		if (results == null || results.rowCount() == 0) {
			JOptionPane.showMessageDialog(null, "Nije pronaðen nijedan rezultat.", "Pretraga", JOptionPane.WARNING_MESSAGE);
			return;
		}
		
		frmTableDataView ret = new frmTableDataView() {
			protected void doubleClick(raJPTableView jp2) {
				openFile(jp2.getStorageDataSet().getString("PATH"), jp2.getStorageDataSet().getString("FILENAME"));
			}
		};
		ret.jp.addTableModifier(new LengthModifier());
    ret.setDataSet(results);
    ret.setSaveName("Pregled-search.files");
    ret.jp.getMpTable().setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_SUBSEQUENT_COLUMNS);
    ret.setTitle("Rezultati pretrage");
    ret.setVisibleCols(new int[] {0, 1, 2, 3, 4});
    ret.show();
	}
	
	public void search(File dir, boolean recursive, raGlob glob, String text) {
		search(dir, recursive, glob, new SearchedText(text));
	}
	
	public void search(File dir, boolean recursive, raGlob glob, SearchedText text) {
		File[] f = dir.listFiles();
		ArrayList dirs = new ArrayList();
		for (int i = 0; i < f.length; i++)
			if (f[i].isFile() && (glob == null || glob.matches(f[i].getName())))
				process(f[i], text);
			else if (recursive && f[i].isDirectory())
				dirs.add(f[i]);
		if (recursive && dirs.size() > 0)
			for (Iterator i = dirs.iterator(); i.hasNext(); )
				search((File) i.next(), true, glob, text);
	}
	
	private void openFile(String path, String fname) {
		try {
			Runtime.getRuntime().exec("notepad \"" + new File(new File(path), fname).getAbsolutePath() + "\"");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void process(File f, SearchedText text) {
		if (text.isEmpty()) {
			add(f, null);
			return;
		}
		TextFile tf = TextFile.read(f);
		List lines = tf.lines();
		List nums = new ArrayList();
		for (ListIterator i = lines.listIterator(); i.hasNext(); ) {
			String line = (String) i.next();
			if (text.check(line)) nums.add(Integer.valueOf(i.nextIndex()));
		}
		if (nums.size() > 0)
			add(f, VarStr.join(nums, ", ").toString());
	}
	
	private void add(File f, String lines) {
		results.insertRow(false);
		results.setString("FILENAME", limit(f.getName(), 50));
		results.setString("PATH", limit(f.getAbsoluteFile().getParent(), 100));
		results.setInt("LENGTH", (int) f.length());
		results.setTimestamp("MODIFIED", new Timestamp(f.lastModified()));
		results.setString("LINES", limit(lines, 100));
	}
	
	private String limit(String orig, int length) {
		if (orig == null) return "";
		if (orig.length() <= length) return orig;
		return orig.substring(0, length);
	}
	
	protected boolean checkOk() {
		File dir = new File(path.getText());
		if (!dir.exists()) {
			JOptionPane.showMessageDialog(win, "Nepostojeæa putanja!", "Greška", JOptionPane.ERROR_MESSAGE);
			path.requestFocusLater();
			return false;
		}
		if (!dir.isDirectory()) {
			JOptionPane.showMessageDialog(win, "Putanja nije direktorij!", "Greška", JOptionPane.ERROR_MESSAGE);
			path.requestFocusLater();
			return false;
		}
		return true;
	}
	
	public raSearchTextFiles() {
		jp.setLayout(new XYLayout(545, 125));
		jp.add(new JLabel("Putanja"), new XYConstraints(15, 20, -1, -1));
		jp.add(path, new XYConstraints(150, 20, 350, -1));
		jp.add(doh, new XYConstraints(505, 20, 21, 21));
		jp.add(new JLabel("Maska"), new XYConstraints(15, 45, -1, -1));
		jp.add(mask, new XYConstraints(150, 45, 100, -1));
		jp.add(recur, new XYConstraints(300, 42, -1, -1));
		jp.add(new JLabel("Traži"), new XYConstraints(15, 70, -1, -1));
		jp.add(text, new XYConstraints(150, 70, 350, -1));
		dc.setFileSelectionMode(dc.DIRECTORIES_ONLY);
    dc.setDialogTitle("Odabir putanje");
    doh.setText("...");
    doh.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
	        File cur = new File(path.getText());
	        if (cur.exists() && cur.isDirectory())
	          dc.setCurrentDirectory(cur);
	      } catch (Exception ex) {}
	      if (dc.showDialog(win) == dc.APPROVE_OPTION) {
	        path.setText(dc.getSelectedFile().getAbsolutePath());
	      }
			}
		});
    String dir = IntParam.getTag("lastSearchDir");
    if (dir == null || dir.length() == 0) {
    	dc.setCurrentDirectory(new File(".").getAbsoluteFile());
    	path.setText(new File(".").getAbsoluteFile().getParent());
    } else {
    	path.setText(dir);
    	dc.setCurrentDirectory(new File(dir).getParentFile());
    }
    String lm = IntParam.getTag("lastSearchMask");
    if (lm != null) mask.setText(lm);
    results.setColumns(new Column[] {
    		dM.createStringColumn("FILENAME", "Datoteka", 50),
    		dM.createIntColumn("LENGTH", "Velièina"),
    		dM.createTimestampColumn("MODIFIED", "Datum promjene"),
    		dM.createStringColumn("PATH", "Putanja", 100),
    		dM.createStringColumn("LINES", "Linije", 100)
    });
    results.open();
	}
	
	class LengthModifier extends raTableModifier {
		BigDecimal K = new BigDecimal(1024);
		
		public boolean doModify() {
			return isColumn("LENGTH");
		}
		public void modify() {
			long length = Long.parseLong(getValue().toString());
			if (length >= 10000*1024)
				setComponentText(new BigDecimal(length).divide(K.multiply(K), 1, BigDecimal.ROUND_HALF_UP) + " MB");
			else if (length >= 10000)
				setComponentText(new BigDecimal(length).divide(K, 1, BigDecimal.ROUND_HALF_UP) + " KB");
		}
	}
	
	static class SearchedText {
		String tx;
		String[] parts;
		public SearchedText(String text) {
			tx = text.toLowerCase();
			if (tx.indexOf('|') >= 0)
				parts = new VarStr(tx).split('|');
		}
		public boolean isEmpty() {
			return tx.length() == 0;
		}
		public boolean check(String line) {
			line = line.toLowerCase();
			if (parts == null || parts.length <= 1)
				return line.indexOf(tx) >= 0;
			
			for (int i = 0; i < parts.length; i++)
				if (line.indexOf(parts[i]) < 0) return false;
			return true;
		}
	}
}
