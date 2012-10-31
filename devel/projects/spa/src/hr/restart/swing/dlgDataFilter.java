/****license*****************************************************************
**   file: dlgDataFilter.java
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

package hr.restart.swing;

import hr.restart.util.FileHandler;
import hr.restart.util.VarStr;
import hr.restart.util.raComboBox;
import hr.restart.util.raDataFilter;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataRow;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.RowFilterListener;
import com.borland.dx.dataset.StorageDataSet;
import com.borland.jbcl.layout.XYConstraints;
import com.borland.jbcl.layout.XYLayout;


public class dlgDataFilter extends raOptionDialog {

  JPanel fpan = new JPanel();
  JPanel main = new JPanel();
  JTree tree = new JTree();
  raComboBox field = new raComboBox() {
    public void this_itemStateChanged() {
      if (showing) fieldChanged();
    };
  };
  JLabel op = new JLabel();
  JraTextField val = new JraTextField() {
    public void valueChanged() {
      if (showing) valChanged();
    };
  };
  JraScrollPane tp = new JraScrollPane();
  
  JraTextField jraFname = new JraTextField();
  JPanel cont = new JPanel();
  
  raOptionDialog od = new raOptionDialog() {
    protected void beforeShow() {
      jraFname.requestFocusLater();
    }
    protected boolean checkOk() {
      return true;
    }
  };
  
  Map history = new TreeMap();
  
  StorageDataSet origRow;
  String origCol;
  String currCol;
  int origIdx;
  
  boolean showing = false;
  boolean needLoad = true;
  boolean needSave = false;

  static dlgDataFilter inst = new dlgDataFilter();
  
  private dlgDataFilter() {
    main.setLayout(new BorderLayout());
    XYLayout uplay = new XYLayout();
    uplay.setWidth(500);
    uplay.setHeight(40);
    fpan.setLayout(uplay);
    op.setHorizontalAlignment(JLabel.CENTER);
    op.setFont(op.getFont().deriveFont(op.getFont().getSize2D() * 1.25f));
    showFilterComponents(true);
    main.add(fpan, BorderLayout.NORTH);
    main.add(tp);
    main.add(okp, BorderLayout.SOUTH);
    
    tp.setViewportView(tree);
    DefaultTreeCellRenderer tcr = new DefaultTreeCellRenderer() {
      public Component getTreeCellRendererComponent(JTree t, Object o,
          boolean sel, boolean exp, boolean leaf, int row, boolean focus) {
        return super.getTreeCellRendererComponent(t, o, sel, exp, leaf, row, false);
      }
    };

    tree.setExpandsSelectedPaths(true);
    tree.setCellRenderer(tcr);
    tcr.setLeafIcon(tcr.getClosedIcon());
    tp.setPreferredSize(new Dimension(500, 200));
    
    tree.addTreeSelectionListener(new TreeSelectionListener() {
      public void valueChanged(TreeSelectionEvent e) {
        TreePath tp = e.getNewLeadSelectionPath();
        if (tp != null)
          displayFilter((DefaultMutableTreeNode) tp.getLastPathComponent());
        else showFilterComponents(false);
      }
    });
    raFilterTreeSelectPopup.installFor(tree);
    raFilterOperandPopup.installFor(op);
    
    JPanel pan = new JPanel(new XYLayout(640, 50));
    pan.add(new JLabel("Ime filtera"), new XYConstraints(15, 18, -1, -1));
    pan.add(jraFname, new XYConstraints(150, 15, 475, -1));
    
    cont.setLayout(new BorderLayout());
    cont.add(pan);
    cont.add(od.getOkPanel(), BorderLayout.SOUTH);
  }
  
  boolean showInstance(Container parent, DataSet ds, String col, int row) {
  	if (needLoad) loadHistory();
    showing = false;
    createFields(ds, col, row);
    createFilterTree(ds);
    showing = true;
    if (show(parent, main, "Filtriranje tablice")) {
    	if (needSave) saveHistory();
      RowFilterListener filter = ds.getRowFilterListener();
      if (filter != null) ds.removeRowFilterListener(filter);
      try {
        DefaultMutableTreeNode root = 
          (DefaultMutableTreeNode) tree.getModel().getRoot();
        Object filt = root.getUserObject();
        if (filt != null && filt instanceof raDataFilter) {
        	System.out.println(((raDataFilter) filt).store());
          ds.addRowFilterListener((raDataFilter) filt);
          ds.refilter();
        } else
          ds.setSort(ds.getSort());
        return true;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    if (needSave) saveHistory();
    return false;
  }
  
  KeyAction popupAct;
  protected void beforeShow() {
    AWTKeyboard.registerKeyStroke(AWTKeyboard.ESC, popupAct = new KeyAction() {
      public boolean actionPerformed() {
        if (!raFilterTreeSelectPopup.isDisplayed() &&
            !raFilterOperandPopup.isDisplayed()) return false;
        if (raFilterTreeSelectPopup.isDisplayed())
          raFilterTreeSelectPopup.hideInstance();
        if (raFilterOperandPopup.isDisplayed())
          raFilterOperandPopup.hideInstance();
        return true;
      }
    });
    AWTKeyboard.registerKeyStroke(win, AWTKeyboard.DELETE, new KeyAction() {
    	public boolean actionPerformed() {
    		TreePath tp = tree.getSelectionPath();
        if (tp != null) {
        	raFilterTreeSelectPopup.inst.selRow =
        		(DefaultMutableTreeNode) tp.getLastPathComponent();
        	if (raFilterTreeSelectPopup.inst.selRow != null) {
            Object o = raFilterTreeSelectPopup.inst.selRow.getUserObject();
            if (o instanceof raDataFilter) {
            	raFilterTreeSelectPopup.inst.self = (raDataFilter) o;
            	raFilterTreeSelectPopup.inst.deleteFilter();
            }
        	}
        }
        return true;
    	}
    });
  }
  
  protected void cancelPress() {
    super.cancelPress();
    AWTKeyboard.unregisterKeyStroke(AWTKeyboard.ESC, popupAct);
  }
  
  private void createFields(DataSet ds, String col, int row) {
    origRow = new StorageDataSet();
    Column[] cols = new Column[ds.getColumnCount()];
    for (int i = 0; i < cols.length; i++)
      cols[i] = ds.getColumn(i).cloneColumn();
    origRow.setColumns(cols);
    origRow.open();
    origRow.insertRow(false);
    
    DataRow dr = new DataRow(ds);
    ds.getDataRow(row, dr);
    dr.copyTo(origRow);
    origRow.post();
    origCol = col;
    origIdx = 0;
    int ccount = 0;
    for (int i = 0; i < origRow.getColumnCount(); i++)
      if (origRow.getColumn(i).getVisible() != 0) ++ccount;
    String[][] items = new String[ccount][2];
    for (int i = 0, ii = 0; i < origRow.getColumnCount(); i++)
      if (origRow.getColumn(i).getVisible() != 0) {
        if (origRow.getColumn(i).getColumnName().equalsIgnoreCase(col))
          origIdx = ii;
        items[ii][0] = origRow.getColumn(i).getCaption();
        items[ii++][1] = origRow.getColumn(i).getColumnName();
      }
    field.setRaItems(items);
    field.setSelectedIndex(origIdx);
  }
  
  private DefaultMutableTreeNode createFilterTree(raDataFilter filt) {
    DefaultMutableTreeNode node = new DefaultMutableTreeNode(filt);
    raDataFilter[] subs = filt.getSubfilters();
    if (subs != null)
      for (int i = 0; i < subs.length; i++)
        node.add(createFilterTree(subs[i]));

    return node;
  }
  
  raDataFilter createNewFilter() {
    return raDataFilter.equal(origRow, origCol);
  }
  
  private void createFilterTree(DataSet ds) {
    DefaultMutableTreeNode root = null;
    RowFilterListener filt = ds.getRowFilterListener();
    if (filt instanceof raDataFilter)
      root = createFilterTree(((raDataFilter) filt).copy());
    else if (filt != null)
      root = new DefaultMutableTreeNode("Nepoznat filter");
    else root = new DefaultMutableTreeNode("Iskljuèen filter");
    
    tree.setModel(new DefaultTreeModel(root));
    tree.expandRow(0);
    for (int i = 0; i < tree.getRowCount(); i++)
      tree.expandRow(i);
    tree.setSelectionRow(0);
    displayFilter(root);
  }

  public static boolean show(Container parent, DataSet ds, String col, int row) {
    return inst.showInstance(parent, ds, col, row);
  }
  
  public static void saveHistory() {
  	inst.saveImpl();
  }
  
  public static void loadHistory() {
  	inst.loadImpl();
  }
  
  void saveImpl() {
  	needSave = false;
  	if (history == null || history.size() == 0) return;
  	
  	Properties pr = new Properties();
  	fillProps(pr, history, "");
  	FileHandler.storeProperties("filter.properties", pr, false);
  }
  
  void fillProps(Properties pr, Map hist, String pref) {
  	for (Iterator i = hist.keySet().iterator(); i.hasNext(); ) {
  		String key = (String) i.next();
  		Object filt = hist.get(key);
  		if (filt instanceof raDataFilter)
  			pr.setProperty(pref + key, ((raDataFilter) filt).store());
  		else fillProps(pr, (Map) filt, pref + key + "->");
  	}
  }
  
  void loadImpl() {
  	needLoad = false;
  	Properties pr = new Properties();
  	FileHandler.loadProperties("filter.properties", pr, false);
  	if (pr.size() == 0) return;
  	
  	if (history == null) history = new TreeMap();
  	
  	for (Iterator it = pr.keySet().iterator(); it.hasNext(); ) {
  		String key = (String) it.next();
    	String[] recur = new VarStr(key).splitTrimmed("->");
    	Map temp = history;
    	for (int i = 0; i < recur.length - 1; i++) {
    		if (!temp.containsKey(recur[i]))
    			temp.put(recur[i], new TreeMap());
    		temp = (Map) temp.get(recur[i]);
    	}
    	try {
				temp.put(recur[recur.length - 1], raDataFilter.parse(pr.getProperty(key)));
			} catch (Exception e) {
				e.printStackTrace();
			}
  	}
  }
  
  void fieldChanged() {
  	val.removeFieldMask();
    val.setHorizontalAlignment(JLabel.LEADING);
    val.setColumnName(field.getDataValue());
    valChanged();
  }
  
  void valChanged() {
    TreePath tp = tree.getSelectionPath();
    if (tp != null) {
      DefaultMutableTreeNode n = (DefaultMutableTreeNode) tp.getLastPathComponent();
      if (n.getUserObject() instanceof raDataFilter) {
        raDataFilter df = (raDataFilter) n.getUserObject();
        if (df.isSimple()) {
          df.setField(origRow, field.getDataValue());
          if (!df.operatorAllowed(df.getOperator())) {
            df.setOperator(raDataFilter.EQUAL_TO, false);
            op.setText(raDataFilter.ops[1]);
            op.setToolTipText(raDataFilter.opdescs[1]);
          }
          updateNode(n, false);
        }
      }
    }
  }
  
  void showFilterComponents(boolean show) {
    if (show && fpan.getComponentCount() == 0) {
      fpan.add(field, new XYConstraints(15, 10, 200, -1));
      fpan.add(op, new XYConstraints(220, 10, 50, -1));
      fpan.add(val, new XYConstraints(275, 10, 210, -1));
      AWTKeyboard.bindKeyStroke(val, AWTKeyboard.DELETE);
    } else if (!show) fpan.removeAll();
    fpan.revalidate();
    fpan.repaint();
  }
  
  void displayFilter(DefaultMutableTreeNode node) {
    Object o = node.getUserObject();
    if (o instanceof raDataFilter) {
      raDataFilter df = (raDataFilter) o;
      if (df.isSimple()) {
        df.putValue(origRow);
        field.setDataValue(df.getColumn());
        op.setText(df.isNot() ? 
            raDataFilter.nops[df.getOperator()] :
            raDataFilter.ops[df.getOperator()]);
        op.setToolTipText(df.isNot() ? 
            raDataFilter.nopdescs[df.getOperator()] :
            raDataFilter.opdescs[df.getOperator()]);
        val.setDataSet(origRow);
        val.setColumnName(df.getColumn());
        val.select(0,0);
        showFilterComponents(true);
        return;
      }
    }
    showFilterComponents(false);
  }
  
  void editFilter(DefaultMutableTreeNode node) {
    displayFilter(node);
    if (fpan.getComponentCount() > 0)
      val.requestFocusLater();
  }
  
  void updateNode(DefaultMutableTreeNode node, boolean structural) {
    DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
    if (structural) {
      model.nodeStructureChanged(node);
      TreePath tp = new TreePath(model.getPathToRoot(node));
      tree.expandPath(tp);
    }
    model.nodeChanged(node);
    tree.revalidate();
    tree.repaint();
  }
  
  String getFilterName(String def) {
  	jraFname.setText(def);
  	if (od.show(win, cont, "Pamæenje filtera")) 
  		return jraFname.getText();
  	return null;
  }
  
  static class raFilterTreeSelectPopup extends JPopupMenu {

    JTree tree = null;
    DefaultMutableTreeNode selRow;
    raDataFilter self;
    
    String clip;
    
    static raFilterTreeSelectPopup inst = new raFilterTreeSelectPopup();
 
    public static void installFor(JTree t) {
      t.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          inst.checkPopup(e);
          if (e.getClickCount() == 2)
            inst.doubleClick();
        }
        public void mousePressed(MouseEvent e) {
          inst.checkPopup(e);
        }
        public void mouseReleased(MouseEvent e) {
          inst.checkPopup(e);
        }
      });
    }
    
    public static boolean isDisplayed() {
      return inst.isVisible();
    }

    public static void hideInstance() {
      inst.setVisible(false);
    }
    
    public void checkPopup(MouseEvent e) {
      tree = (JTree) e.getSource();
      if (e.isPopupTrigger() && e.getSource() instanceof JTree) {
        TreePath path = tree.getPathForLocation(e.getX(), e.getY());
        if (path != null) {
          tree.setSelectionPath(path);
          selRow = (DefaultMutableTreeNode) path.getLastPathComponent();
          if (selRow != null) {
            removeAll();
            Action paste = new AbstractAction("Umetni uvjet") {
              public void actionPerformed(ActionEvent e) {
                pasteFilter(raDataFilter.parse(inst.clip));
              }
            };
            Object o = selRow.getUserObject();
            if (o instanceof raDataFilter) {
              self = (raDataFilter) o;
              if (!self.isCompound()) {
                add(new AbstractAction("Izmijeni uvjet") {
                  public void actionPerformed(ActionEvent e) {
                    dlgDataFilter.inst.editFilter(selRow);
                  }
                });
              } else {
                add(new AbstractAction(self.isAnd() ? 
                    "Postavi poduvjete kao opcionalne" :
                    "Postavi poduvjete kao nužne") {
                  public void actionPerformed(ActionEvent e) {
                    toggleFilter();
                  }
                });
                add(new AbstractAction("Restrukturiraj uvjet") {
                  public void actionPerformed(ActionEvent e) {
                    packFilter(selRow);
                  }
                });
              }
              addSeparator();
              add(new AbstractAction("Dodaj nužni uvjet") {
                public void actionPerformed(ActionEvent e) {
                  addFilter(true);
                }
              });
              add(new AbstractAction("Dodaj opcionalni uvjet") {
                public void actionPerformed(ActionEvent e) {
                  addFilter(false);
                }
              });
              addSeparator();
              add(new AbstractAction("Izreži uvjet") {
                public void actionPerformed(ActionEvent e) {
                  cutFilter();
                }
              });
              add(new AbstractAction("Kopiraj uvjet") {
                public void actionPerformed(ActionEvent e) {
                  copyFilter();
                }
              });
              add(paste);
              add(new AbstractAction("Izbaci uvjet") {
                public void actionPerformed(ActionEvent e) {
                  deleteFilter();
                }
              });
              addSeparator();
              add(createMemory(true));
              add(createHistory());
            } else {
              add(new AbstractAction("Definiraj uvjet") {
                public void actionPerformed(ActionEvent e) {
                  defineFilter();
                }
              });
              add(paste);
              addSeparator();
              add(createMemory(false));
              add(createHistory());
            }
            paste.setEnabled(clip != null && raDataFilter.parse(clip).compatibleWith(dlgDataFilter.inst.origRow));
            raFilterTreeSelectPopup.this.show(tree, e.getX(), e.getY());
          }
        }
      }
    }
    
    void doubleClick() {
      TreePath tp = tree.getSelectionPath();
      selRow = (DefaultMutableTreeNode) tp.getLastPathComponent();
      if (selRow != null) {
        Object o = selRow.getUserObject();
        if (o instanceof raDataFilter) {
          self = (raDataFilter) o;
          if (self.isSimple())
            dlgDataFilter.inst.editFilter(selRow);
        } else defineFilter();
      }
    }
    
    void toggleFilter() {
      if (self != null && self.isCompound()) {
        self.setAnd(!self.isAnd());
        dlgDataFilter.inst.updateNode(selRow, false);
      }
    }
    
    void packFilter(DefaultMutableTreeNode node) {
      if (node.getUserObject() instanceof raDataFilter) {
        raDataFilter df = (raDataFilter) node.getUserObject();
        
        DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
        DefaultMutableTreeNode p = (DefaultMutableTreeNode) node.getParent();
        DefaultMutableTreeNode nn = dlgDataFilter.inst.createFilterTree(df.pack());
        if (p == null) model.setRoot(nn);
        else {
          int idx = p.getIndex(node);
          node.removeFromParent();
          p.insert(nn, idx);
        }
        dlgDataFilter.inst.updateNode(nn, true);
      }
    }
    
    void defineFilter() {
      selRow.setUserObject(dlgDataFilter.inst.createNewFilter());
      dlgDataFilter.inst.fieldChanged();
      dlgDataFilter.inst.updateNode(selRow, false);
      dlgDataFilter.inst.editFilter(selRow);
    }
    
    void addFilter(boolean req) {
      if (self.isCompound() && (self.isAnd() == req)) {
        addFilter(selRow);
        return;
      }
      if (!self.isCompound() && selRow.getParent() != null) {
        DefaultMutableTreeNode p = (DefaultMutableTreeNode) selRow.getParent();
        if (p.getUserObject() instanceof raDataFilter) {
          raDataFilter dfp = (raDataFilter) p.getUserObject();
          if (dfp.isCompound() && (dfp.isAnd() == req)) {
            addFilter(p);
            return;
          }
        }
      }
      combineFilter(selRow, req);
    }
    
    void addFilter(DefaultMutableTreeNode node) {
      if (node.getUserObject() instanceof raDataFilter) {
        raDataFilter dfp = (raDataFilter) node.getUserObject();
        if (dfp.isCompound()) {
          raDataFilter nf = dlgDataFilter.inst.createNewFilter();
          if (dfp.isAnd()) dfp.and(nf);
          else dfp.or(nf);
          DefaultMutableTreeNode nn = new DefaultMutableTreeNode(nf);
          node.add(nn);
          DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
          model.nodeStructureChanged(node);
          expandAndSelect(nn);
        }
      }
    }
    
    void combineFilter(DefaultMutableTreeNode node, boolean req) {
      if (node.getUserObject() instanceof raDataFilter) {
        raDataFilter of = (raDataFilter) node.getUserObject();
        DefaultMutableTreeNode p = (DefaultMutableTreeNode) node.getParent();
        if (p == null || p.getUserObject() instanceof raDataFilter) {
          raDataFilter dfp = null;
          if (p != null) dfp = (raDataFilter) p.getUserObject();
          if (dfp == null || dfp.isCompound()) {
            raDataFilter nf = dlgDataFilter.inst.createNewFilter();
            raDataFilter cf = req ? of.and(nf) : of.or(nf);
            if (dfp != null) dfp.replaceFilter(of, cf);
            
            DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
            DefaultMutableTreeNode nn = new DefaultMutableTreeNode(cf);            
            if (p == null)
              model.setRoot(nn);
            else {
              int idx = p.getIndex(node);
              p.remove(idx);
              p.insert(nn, idx);
            }
            nn.add(node);
            nn.add(p = new DefaultMutableTreeNode(nf));
            model.nodeStructureChanged(nn);
            TreePath tpn = new TreePath(model.getPathToRoot(node));
            tree.expandPath(tpn);
            expandAndSelect(p);
          }
        }
      }
    }
    
    void expandAndSelect(DefaultMutableTreeNode node) {
      DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
      TreePath tp = new TreePath(model.getPathToRoot(node));
      tree.expandPath(tp);
      tree.setSelectionPath(tp);
      dlgDataFilter.inst.editFilter(node);
    }
    
    void cutFilter() {
    	copyFilter();
    	deleteFilter();
    }
    
    void copyFilter() {
    	clip = self.store();
    }
    
    void pasteFilter(raDataFilter df) {
    	if (selRow.isRoot()) {
    		selRow.setUserObject(df);
    		packFilter(selRow);
    	} else if (selRow.getUserObject() instanceof raDataFilter) {
          raDataFilter of = (raDataFilter) selRow.getUserObject();
          DefaultMutableTreeNode p = (DefaultMutableTreeNode) selRow.getParent();
          if (p != null && p.getUserObject() instanceof raDataFilter) {
          	raDataFilter dfp = (raDataFilter) p.getUserObject();
          	dfp.replaceFilter(of, df);
          	packFilter(p);
          }
    	}
    }
    
    void deleteFilter() {
      if (selRow.isRoot()) {
        selRow.removeAllChildren();
        selRow.setUserObject("Iskljuèen filter");
        dlgDataFilter.inst.updateNode(selRow, true);
      } else {
        DefaultMutableTreeNode p = (DefaultMutableTreeNode) selRow.getParent();
        if (p.getUserObject() instanceof raDataFilter) {
          raDataFilter dfp = (raDataFilter) p.getUserObject();
          if (dfp.isCompound()) {
            dfp.removeFilter(self);
            selRow.removeFromParent();
            if (p.getChildCount() == 0) packFilter(p);
            else dlgDataFilter.inst.updateNode(p, true);
          }
        }
      }
    }
    
    JMenu createMemory(boolean add) {
    	JMenu ret = new JMenu("Dodaj filter");
    	Map hist = dlgDataFilter.inst.history;
    	if (hist == null || hist.size() == 0) {
    		AbstractAction filt = new AbstractAction("Zapamti ovdje") {
  				public void actionPerformed(ActionEvent e) {
  					memorizeFilter("");
  				}
  			};
  			if (!add) filt.setEnabled(false);
  			ret.add(filt);
    	} else fillMemory(ret, hist, "", add);
    	
    	return ret;
    }
    
    void fillMemory(JMenu m, Map hist, String prev, boolean add) {
    	if (add) {
    		AbstractAction ins = new AbstractAction("Zapamti ovdje") {
  				public void actionPerformed(ActionEvent e) {
  					memorizeFilter((String) getValue("old-filter"));
  				}
  			};
  			ins.putValue("old-filter", prev);
  			m.add(ins);
  			if (hist.size() > 0) m.addSeparator();
    	}
    	for (Iterator i = hist.keySet().iterator(); i.hasNext(); ) {
    		String key = (String) i.next();
    		Object val = hist.get(key);
    		if (val instanceof Map) {
    			JMenu subm = new JMenu("+" + key);
      		m.add(subm);
    			fillMemory(subm, (Map) val, prev + key + "->", add);
    		}
    	}
    	for (Iterator i = hist.keySet().iterator(); i.hasNext(); ) {
    		String key = (String) i.next();
    		Object val = hist.get(key);
    		if (val instanceof raDataFilter) {
    			JMenu subm = new JMenu(key);
      		m.add(subm);
	    		if (add) {
	    			AbstractAction over = new AbstractAction("Zamijeni filter") {
	    				public void actionPerformed(ActionEvent e) {
	    					memorizeFilter((String) getValue("old-filter"));
	    				}
	    			};
	    			over.putValue("old-filter", prev + key);
	    			subm.add(over);
	    		}
	    		AbstractAction del = new AbstractAction("Obriši filter") {
    				public void actionPerformed(ActionEvent e) {
    					removeFromMemory((String) getValue("old-filter"));
    				}
    			};
    			del.putValue("old-filter", prev + key);
    			subm.add(del);
    		}
    	}
    	if (prev.length() > 0) {
	    	AbstractAction delall = new AbstractAction("Obriši sve") {
					public void actionPerformed(ActionEvent e) {
						removeFromMemory((String) getValue("old-submenu"));
					}
				};
				delall.putValue("old-submenu", prev.substring(0, prev.length() - 2));
				m.addSeparator();
				m.add(delall);
    	}
    }
    
    void removeFromMemory(String def) {
    	if (def == null || def.trim().length() == 0) return;
    	
    	Map hist = dlgDataFilter.inst.history;
    	String[] recur = new VarStr(def).splitTrimmed("->");
    	for (int i = 0; i < recur.length - 1; i++) {
    		if (!hist.containsKey(recur[i])) return;
    		hist = (Map) hist.get(recur[i]);
    	}
    	hist.remove(recur[recur.length - 1]);
    	dlgDataFilter.inst.needSave = true;
    }
    
    void memorizeFilter(String def) {
    	String fname = dlgDataFilter.inst.getFilterName(def);
    	if (fname == null || fname.trim().length() == 0) return;
    	
    	removeFromMemory(def);
    	    	
    	Map hist = dlgDataFilter.inst.history;
    	String[] recur = new VarStr(fname).splitTrimmed("->");
    	for (int i = 0; i < recur.length - 1; i++) {
    		if (!hist.containsKey(recur[i]))
    			hist.put(recur[i], new TreeMap());
    		hist = (Map) hist.get(recur[i]);
    	}
    	hist.put(recur[recur.length - 1], self.copy());
    	dlgDataFilter.inst.needSave = true;
    }
    
    JMenu createHistory() {
    	JMenu ret = new JMenu("Umetni filter");
    	Map hist = dlgDataFilter.inst.history;
    	if (hist == null || hist.size() == 0)	
    		ret.setEnabled(false);
    	else fillHistory(ret, hist);
    	
    	return ret;
    }
    
    void fillHistory(JMenu m, Map hist) {
    	for (Iterator i = hist.keySet().iterator(); i.hasNext(); ) {
    		String key = (String) i.next();
    		Object val = hist.get(key);
    		if (val instanceof Map) {
    			JMenu subm = new JMenu(key);
    			fillHistory(subm, (Map) val);
    			m.add(subm);
    		} 
    	}
    	for (Iterator i = hist.keySet().iterator(); i.hasNext(); ) {
    		String key = (String) i.next();
    		Object val = hist.get(key);
    		if (val instanceof raDataFilter) {
    			raDataFilter df = (raDataFilter) val;
    			AbstractAction filt = new AbstractAction(key) {
    				public void actionPerformed(ActionEvent e) {
    					pasteFilter((raDataFilter) getValue("data-filter"));
    				}
    			};
    			filt.putValue("data-filter", df);
    			m.add(filt);
    			if (!df.compatibleWith(dlgDataFilter.inst.origRow))
    				filt.setEnabled(false);
    		}
    	}
    }

    private raFilterTreeSelectPopup() {
      //
    }
  }
  
  static class raFilterOperandPopup extends JPopupMenu {
    static raFilterOperandPopup inst = new raFilterOperandPopup();
    JLabel op;
    
    public static boolean isDisplayed() {
      return inst.isVisible();
    }

    public static void hideInstance() {
      inst.setVisible(false);
    }
    
    public static void installFor(JLabel op) {
      inst.op = op;
      op.addMouseListener(new MouseAdapter() {
        public void mouseClicked(MouseEvent e) {
          inst.checkPopup(e);
        }
        public void mousePressed(MouseEvent e) {
          inst.checkPopup(e);
        }
        public void mouseReleased(MouseEvent e) {
          inst.checkPopup(e);
        }
      });
    }
    
    public void checkPopup(MouseEvent e) {
      if (e.isPopupTrigger() && e.getSource() instanceof JLabel) {
        TreePath tp = dlgDataFilter.inst.tree.getSelectionPath();
        if (tp != null) {
          DefaultMutableTreeNode node = (DefaultMutableTreeNode) tp.getLastPathComponent();
          if (node.getUserObject() instanceof raDataFilter) {
            raDataFilter df = (raDataFilter) node.getUserObject();
            if (df.isSimple()) showPopup(e, node);
          }
        }
      }
    }
    
    void showPopup(MouseEvent e, DefaultMutableTreeNode node) {
      removeAll();
      raDataFilter df = (raDataFilter) node.getUserObject();
      for (int i = 1; i <= 10; i++)
        if (df.operatorAllowed(i)) {
          add(new OperatorChange(node, i, false));
          add(new OperatorChange(node, i, true));
        }
      this.show(op, e.getX(), e.getY());
    }
    
    private raFilterOperandPopup() {
      //
    }
  }
  
  static class OperatorChange extends AbstractAction {
    DefaultMutableTreeNode node;
    int op;
    boolean not;
    
    public OperatorChange(DefaultMutableTreeNode node, int op, boolean not) {
      super(not ? raDataFilter.nopdescs[op] : raDataFilter.opdescs[op]);
      this.node = node;
      this.op = op;
      this.not = not;
    }
    
    public void actionPerformed(ActionEvent e) {
      raDataFilter df = (raDataFilter) node.getUserObject();
      df.setOperator(op, not);
      dlgDataFilter.inst.op.setText(not ? 
          raDataFilter.nops[op] :
          raDataFilter.ops[op]);
      dlgDataFilter.inst.op.setToolTipText(not ? 
          raDataFilter.nopdescs[op] :
          raDataFilter.opdescs[op]);
      dlgDataFilter.inst.updateNode(node, false);
    }
  }
}

