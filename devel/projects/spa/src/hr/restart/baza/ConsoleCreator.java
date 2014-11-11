/****license*****************************************************************
**   file: ConsoleCreator.java
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
package hr.restart.baza;

import hr.restart.util.Aus;
import hr.restart.util.Int2;
import hr.restart.util.ProcessInterruptException;
import hr.restart.util.Util;
import hr.restart.util.Valid;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.util.raGlob;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import com.borland.dx.dataset.Column;
import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.Locate;
import com.borland.dx.dataset.Variant;
import com.borland.dx.sql.dataset.QueryDataSet;

/**
 * <p>
 * Title:
 * </p>
 * <p>
 * Description:
 * </p>
 * <p>
 * Copyright: Copyright (c) 2003
 * </p>
 * <p>
 * Company:
 * </p>
 * 
 * @author unascribed
 * @version 1.0
 */

public class ConsoleCreator {
	public static int ERROR = -1;

	public static int NOP = 0;

	public static int TABLE_SAVE = 1;

	public static int TABLE_LOAD = 2;

	public static int TABLE_DELETE = 3;

	public static int TABLE_UNLOCK = 4;

	public static int TABLE_CHECK = 5;

	public static int TABLE_UPDATE = 6;

	public static int TABLE_CREATE = 7;

	public static int TABLE_RECREATE = 8;

	public static int TABLE_DROP = 9;

	public static int INDEX_CREATE = 10;

	public static int INDEX_RECREATE = 11;

	public static int INDEX_DROP = 12;

	public static int DDL_DUMP = 13;
    
    public static int maxLoad = 1000000;

	private int maxctab;

	private boolean tabChanged, checkTab, tabCreated;

	private String noteStatus;

	private QueryDataSet tab = Tablice.getDataModule().getFilteredDataSet("");

	public ConsoleCreator() {
	}

	public static void initDatabase() throws Exception {
		dM.setMinimalMode();
		dM dm = dM.getDataModule();
		if (dm.conURL == null || dm.conURL.length() == 0
				|| dm.database1 == null
				|| dm.database1.getJdbcConnection().isClosed())
			return;
		File subdir = new File("defdata");
		if (!subdir.exists() || !subdir.isDirectory()) {
			System.out.println("nema defdata direktorija!");
			return;
		}

		KreirDrop kdp = (KreirDrop) Class.forName("hr.restart.baza.Parametri")
				.newInstance();
		if (!kdp.KreirTable()) {
			System.out.println("Tablica Parametri vec postoji!");
			return;
		}
		kdp.KreirIdx();
		kdp.insertData(subdir);
		for (int i = 0; i < moduleClasses.length; i++)
			if (!moduleClasses[i].equals("hr.restart.baza.Parametri")) {
				kdp = (KreirDrop) Class.forName(moduleClasses[i]).newInstance();
				kdp.KreirTable();
				kdp.KreirIdx();
                try {
                  if (kdp.insertData(subdir)>0) {
                    System.out.println("Tablica napunjena");
                  } else {
                    System.out.println("Tablica prazna");
                  }
                } catch (FileNotFoundException fnfe) {
                  System.out.println("Datoteka ne postoji");
                } catch (Exception e) {
                  e.printStackTrace();
                  System.out.println("Opaka greska");
                }

			}
		System.out.println("Sve gotovo.");
	}

	public static void main(String[] args) {
		if (args.length == 0 || args.length == 1
				&& args[0].equalsIgnoreCase("help")) {
			showHelp();
			return;
		}

		if (args.length == 1 && args[0].equalsIgnoreCase("init")) {
			try {
				new ConsoleCreator().initDatabase();
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.exit(0);
		}

		try {
			System.out.println("Inicijaliziram datamodule...");
			dM.setMinimalMode();
			dM.getDataModule().loadModules();
			Refresher.getDataModule().stop();
			System.out.println("Moduli ucitani.");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}

		ConsoleCreator con = new ConsoleCreator();
		con.run(VarStr.join(args, ' ').toString());
		System.out.println("Sve gotovo.");
        System.exit(0);
	}

	private static void showHelp() {
		System.out.println("Argumenti:  <naredba> <popis tablica>");
		System.out.println();
		System.out.println("Naredbe:");
		System.out
				.println("  export - sprema podatke iz tablica na disk (.dat i .def)");
		System.out.println("  import - puni tablice iz .dat i .def datoteka");
		System.out.println("  delete - brise sve redove iz tablica");
		System.out.println("  unlock - brise lokk flag u tablicama");
		System.out.println("  check - provjerava strukturu tablica");
		System.out
				.println("  update - check, ako ima promjena, onda export, recreate, import");
		System.out.println("  create - kreira tablice");
		System.out.println("  recreate - rekreira tablice (unistava podatke!)");
		System.out.println("  drop - ubija tablice (unistava podatke!)");
		System.out.println("  icreate - kreira indekse");
		System.out.println("  irecreate - rekreira indekse");
		System.out.println("  idrop - brise indekse");
		System.out
				.println("  init - inicijalizira praznu bazu s podacima iz ./defdata");
		System.out
				.println("  ddldump - ispisuje ddl naredbe za tablice i indekse");
		System.out.println();
		System.out
				.println("Napomena: popis tablica podrzava wildcard-e, no tada je dobro");
		System.out.println("staviti sve argumente zajedno unutar navodnika");
		System.out.println();
	}
    
    public static void updateEverything() {
      new ConsoleCreator().run("update");
    }

	public void run(String paramLine) {
		dM dm;
		int ncom = -1;
		String[] params = new VarStr(paramLine).split();
		String[] comms = { "nop", "export", "import", "delete", "unlock",
				"check", "update", "create", "recreate", "drop", "icreate",
				"irecreate", "idrop", "ddldump" };
		for (int i = 0; i < comms.length; i++)
			if (comms[i].equalsIgnoreCase(params[0]))
				ncom = i;
		if (ncom <= 0) {
			System.out.println("Pogresna naredba: " + params[0]);
			return;
		}

		String tables[] = new String[moduleClasses.length];
		for (int x = 0; x < moduleClasses.length; x++)
			tables[x] = moduleClasses[x].substring(
					moduleClasses[x].lastIndexOf('.') + 1).toLowerCase();

		int[] ncoms = new int[tables.length];
		java.util.Arrays.fill(ncoms, NOP);
		for (int i = 1; i < params.length; i++) {
            if (params[i].equalsIgnoreCase("all")) params[i] = "*";
            
			raGlob g = new raGlob(params[i].toLowerCase());
			for (int x = 0; x < tables.length; x++)
				if (g.matches(tables[x]))
					ncoms[x] = ncom;
		}
		if (params.length == 1)
			for (int x = 0; x < tables.length; x++)
				ncoms[x] = ncom;

		initActions();

		try {
			for (int x = 0; x < tables.length; x++) {
				if (ncoms[x] != NOP) {
					try {
						String status = "";
						int com = ncoms[x];
						boolean tabtab = tables[x].equals("tablice");
						KreirDrop kdp = KreirDrop.getModule(moduleClasses[x]);

						if (kdp == null) {
							System.out.println("HEJ! nema modula!!");
							Class klasica = Class.forName(moduleClasses[x]);
							kdp = (KreirDrop) klasica.newInstance();
						}

						if (com == DDL_DUMP) {
							VarStr ddl = new VarStr(kdp.SqlDefTabela);
							ddl.replaceAll(", ", ",\r\n  ");
							System.out.print(ddl.append(';').append("\r\n"));
							for (int i = 0; i < kdp.DefIndex.length; i++)
								System.out.print(kdp.DefIndex[i] + ";\r\n");
							System.out.print("\r\n");
							continue;
						}

						checkTab = false;

						if (com == INDEX_DROP || com == INDEX_RECREATE
								|| com == TABLE_DROP || com == TABLE_RECREATE) {
							if (kdp.DropIdx())
								status = tables[x] + "Indeksi dropani!";
							else
								status = "Greška!";
						}

						if (com == TABLE_DROP || com == TABLE_RECREATE) {
							if (!Valid.getValid().runSQL(
									"DELETE FROM " + tables[x])
									|| !kdp.DropTable()) {
								status = "Tablica zauzeta!";
								com = NOP;
								/*
								 * if (tm.getValueAt(x,3).equals("Dropaj"))
								 * continue;
								 */
							} else if (com == TABLE_DROP)
								status = "Tablica dropana";
							if (com != NOP && tabtab)
								tabCreated = true;
						}

						if (com == TABLE_CREATE || com == TABLE_RECREATE) {
							if (kdp.KreirTable()) {
								status = com == TABLE_CREATE ? "Tablica iskreirana!"
										: "Tablica rekreirana!";
								if (com == TABLE_CREATE)
									checkTab = true;
								if (tabtab)
									tabCreated = true;
							} else {
								status = "Tablica zauzeta!";
								com = NOP;
							}
						}

						if (com == TABLE_CREATE || com == TABLE_RECREATE
								|| com == INDEX_CREATE || com == INDEX_RECREATE) {
							if (kdp.KreirIdx()) {
								if (status.equals(""))
									status = "Indeksi iskreirani!";
							} else {
								if (status.equals(""))
									status = "Indeksi iskreirani!";
							}
						}
						if (tabtab)
							if (com == TABLE_LOAD || com == TABLE_DELETE
									|| com == TABLE_UNLOCK)
								tabCreated = true;
						if (com == TABLE_SAVE)
							status = dumpData(kdp) + noteStatus;
						if (com == TABLE_LOAD)
							status = loadData(kdp) + noteStatus;
						if (com == TABLE_DELETE) {
							if (Valid.getValid().runSQL(
									"DELETE FROM " + tables[x]))
								status = "Tablica obrisana!";
							else
								status = "Greška kod brisanja!";
							//            System.out.println(tm.getValueAt(x, 0)+"
							// obrisan!");
						}
						if (com == TABLE_CHECK) {
							status = checkData(kdp, tables[x]);
							checkTab = true;
							//            checkTableRow(x);
						}
						if (com == TABLE_UNLOCK) {
							if (Valid.getValid().runSQL(
									"UPDATE " + tables[x] + " SET LOKK='N'"))
								status = "Tablica otklju\u010Dana!";
							else
								status = "Greška kod otkljuèavanja!";
						}
						if (com == TABLE_UPDATE) {
							status = checkData(kdp, tables[x]);
							if (status.equals("Tablica je OK")) {
								status = "Tablica nepromijenjena!";
                            } else if (fastUpdate(kdp, tables[x])) {
                              status = "Tablica ažurirana! (q)";
							} else {
								if (!status.equals("Tablica ne postoji!"))
									status = dumpData(kdp);
								else checkTab = true;
                                
								if (!status.endsWith("spremanja!")) {
									if (tabtab) tabCreated = true;
									if (!kdp.DropTable() /*|| !checkTab*/) {
										status = "Tablica zauzeta!";
									} else {
                                        kdp.KreirTable();
										kdp.KreirIdx();
										if (status.endsWith("spremljen!")) {
											status = loadData(kdp);
											if (status.equals("Tablica napunjena!"))
												status = "Tablica ažurirana!" + noteStatus;
											else status = "Tablica rekreirana!";
										} else if (checkTab) status = "Tablica kreirana!";
										else status = "Tablica rekreirana!";
									}
								}
							}
						}
						System.out.println(tables[x] + ": " + status);
						if (checkTab) checkTableRow(x);
					} catch (Exception e) {
						e.printStackTrace();
						//          System.out.println(e.getMessage());
					}
				}
			}
		} finally {
			KreirDrop.removeNotifier();
			try {
				System.out.println("Finaliziranje procesa ...");
				if (tabChanged && !tabCreated) {
					tab.saveChanges();
					dM.getDataModule().getTablice().refresh();
				}
				if (tabCreated)
					tab.refresh();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private void displayInsertError(String table, HashMap row) {
		KreirDrop mod = KreirDrop.getModuleByName(table);
		VarStr errlin = new VarStr(table)
				.append(": neuspjesno dodavanje reda - ");
		for (Iterator i = row.keySet().iterator(); i.hasNext();) {
			String coln = (String) i.next();
			Column col = mod.getColumn(coln);
			if (col != null && col.isRowId())
				errlin.append(coln).append('=').append(row.get(coln)).append(
						';');
		}
		System.err.println(errlin.chop().toString());
		if (row.containsKey(KreirDrop.ERROR_KEY))
			System.err.println(row.get(KreirDrop.ERROR_KEY));
	}

	private void initActions() {
		tabChanged = tabCreated = false;
		try {
			tab.refresh();
			Valid.getValid().execSQL("SELECT MAX(ctab) AS maxc FROM tablice");
			Valid.getValid().RezSet.open();
			maxctab = Valid.getValid().RezSet.getInt("MAXC");
			Valid.getValid().RezSet.close();
			Valid.getValid().RezSet = null;
		} catch (Exception e) {
			e.printStackTrace();
			maxctab = 0;
		}

		KreirDrop.installNotifier(new raTransferNotifier() {
			private int total, errs;

			public void rowTransfered(String table, int action, int row,
					Object data) {
				switch (action) {
				case raTransferNotifier.INSERT_STARTED:
					total = errs = 0;
					break;
				case raTransferNotifier.DUMP_STARTED:
					total = row;
					break;
				case raTransferNotifier.ROW_INSERT_FAILED:
					if (++errs <= 100) {
						displayInsertError(table, (HashMap) data);
						if (errs == 100)
							System.err.println("Prikazano prvih 100 grešaka.");
					}
				case raTransferNotifier.ROW_INSERTED:
					++total;
					break;
				case raTransferNotifier.INSERT_FINISHED:
					noteStatus = " " + row + "/" + total + " redova";
					break;
				case raTransferNotifier.DUMP_FINISHED:
					noteStatus = " " + Aus.getRedova(total);
					break;
				}
			}
		});
	}

    public static boolean columnsEqual(Column cdm, Column cb) {
        return (cdm.getColumnName().equalsIgnoreCase(cb.getColumnName()) &&
            cdm.getDataType() == cb.getDataType() && cdm.getSqlType() == cb.getSqlType() &&
            (cb.getDataType() != Variant.STRING || cdm.getPrecision() == cb.getPrecision()) &&
            (cb.getDataType() != Variant.BIGDECIMAL || cdm.getScale() == cb.getScale()) &&
            cdm.isRowId() == cb.isRowId());
    }

    public static void dumpColumns(Column[] dmcols, QueryDataSet db) {
      System.out.print("modul: ");
      for (int i = 0; i < dmcols.length; i++)
        System.out.print(dmcols[i].getColumnName() + " ");
      System.out.println();
      System.out.print("baza:  ");
      for (int i = 0; i < db.getColumnCount(); i++)
        System.out.print(db.getColumn(i).getColumnName() + " ");
      System.out.println();
    }

    public static String getExtraData(Column c) {
      return "Column "+c.getColumnName()+" is type "+Variant.typeName(c.getDataType())+
          ", sql type "+c.getSqlType()+(c.getDataType() == Variant.BIGDECIMAL ?
          ", scale "+c.getScale() : "")+(c.getDataType() == Variant.STRING ?
          ", precision "+c.getPrecision() : "") + (c.isRowId() ? ", primary key" : "");
    }

    public static String checkData(KreirDrop kdp, String name) {
      Column[] cols = kdp.getColumns();
      String ret = "Tablica je OK";
      System.out.println("**** provjeravam " + name + ":   (" + kdp.Naziv + ")");
      Valid.getValid().execSQL("SELECT * FROM " + name + " WHERE 1 = 0");
      QueryDataSet db = Valid.getValid().getDataAndClear();
      try {
        db.open();
      } catch (Exception e) {
        return "Tablica ne postoji!";
      }

      boolean wrongOrder = false;
      //table.open();
      if (cols.length != db.getColumnCount()) {
        ret = "Greška u tablici!";
        System.out.println("nejednak broj kolona! " + cols.length + "  " + db.getColumnCount());
        dumpColumns(cols, db);
      } else
      for (int i = 0; i < cols.length; i++) {
        Column dbc = db.hasColumn(cols[i].getColumnName());
        if (dbc == null) {
          ret = "Greška u tablici!";
          System.out.println("Baza nema kolonu iz dM:");
          System.out.println(getExtraData(cols[i]));
        } else if (!columnsEqual(cols[i], dbc)) {
          ret = "Greška u tablici!";
          System.out.println("kolone nisu jednake!");
          System.out.println("modul: " + getExtraData(cols[i]));
          System.out.println("baza:  " + getExtraData(dbc));
        } else if (!cols[i].getColumnName().equalsIgnoreCase(cols[i].getServerColumnName())) {
          ret = "Greška u tablici!";
          System.out.println("Greška u modulu! ColumnName = "+cols[i].getColumnName()+
                             ", ServerColumnName = "+cols[i].getServerColumnName());
        } else if (db.getColumn(i) != dbc)
          wrongOrder = true;
      }
      if (wrongOrder) {
        ret = "Greška u tablici!";
        System.out.println("pogrešan redoslijed kolona!");
        dumpColumns(cols, db);
      }
      db.close();
//      if (name.equalsIgnoreCase("zavtr")) checkbd();
      return ret;
    }
    
    public static boolean fastUpdate(KreirDrop kdp, String name) {
      try {
        Dialect.getDDL("ADD-COLUMN");
        Dialect.getDDL("ALTER-COLUMN");
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }

      Column[] cols = kdp.getColumns();
      Valid.getValid().execSQL("SELECT * FROM " + name + " WHERE 1 = 0");
      QueryDataSet db = Valid.getValid().getDataAndClear();
      try {
        db.open();
      } catch (Exception e) {
        e.printStackTrace();
        return false;
      }
      if (cols.length < db.getColumnCount()) {
        System.out.println("length loo small");
        return false;
      }
      
      ArrayList comms = new ArrayList();
      for (int i = 0; i < cols.length; i++)
        if (i >= db.getColumnCount()) {
          VarStr add = new VarStr();
          add.append(Dialect.getDDL("ADD-COLUMN"));
          add.replace("%NAME", name);
          add.replace("%COLUMN", cols[i].getColumnName());
          if (cols[i].getDataType() == Variant.STRING)
            add.replace("%TYPE", kdp.ddl.stringComm(
                cols[i].getPrecision(), cols[i].getDefault(), cols[i].isRowId()));
          else if (cols[i].getDataType() == Variant.INT)
            add.replace("%TYPE", kdp.ddl.intComm("INTEGER",
                cols[i].getPrecision(), cols[i].isRowId()));
          else if (cols[i].getDataType() == Variant.BIGDECIMAL)
            add.replace("%TYPE", kdp.ddl.floatComm(
                cols[i].getPrecision(), cols[i].getScale(), cols[i].isRowId()));
          else if (cols[i].getDataType() == Variant.SHORT)
            add.replace("%TYPE", kdp.ddl.intComm("SHORT",
                cols[i].getPrecision(), cols[i].isRowId()));
          else if (cols[i].getDataType() == Variant.TIMESTAMP)
            add.replace("%TYPE", kdp.ddl.dateComm(cols[i].isRowId()));
          else {
            System.out.println("invalid type");
            return false;
          }
          comms.add(add.toString());
        } else {
          Column dcol = db.getColumn(i);
          if (columnsEqual(dcol, cols[i])) continue;
          if (dcol.getColumnName().equalsIgnoreCase(cols[i].getColumnName()) &&
              dcol.getDataType() == cols[i].getDataType() &&
              cols[i].getPrecision() >= dcol.getPrecision() &&
              (dcol.getDataType() == Variant.STRING ||
              (dcol.getDataType() == Variant.BIGDECIMAL && 
                  cols[i].getScale() >= dcol.getScale()))) {
            VarStr alter = new VarStr();
            alter.append(Dialect.getDDL("ALTER-COLUMN"));
            alter.replace("%NAME", name);
            alter.replace("%COLUMN", dcol.getColumnName());
            if (dcol.getDataType() == Variant.STRING)
              alter.replace("%TYPE", kdp.ddl.stringComm(
                  cols[i].getPrecision(), cols[i].getDefault(), cols[i].isRowId()));              
            else
              alter.replace("%TYPE", kdp.ddl.floatComm(
                  cols[i].getPrecision(), cols[i].getScale(), cols[i].isRowId()));
            comms.add(alter.toString());
          } else {
            System.out.println("invalid type");
            return false;
          }
        }
      
      for (int i = 0; i < comms.size(); i++) {
        String com = (String) comms.get(i);
        System.out.println(com);
        if (!Valid.getValid().runSQL(com)) return false;
      }
      return true;
    }
    
	public String dumpData(KreirDrop kdp) {
		try {
			noteStatus = "";
			String tname = kdp.getColumns()[0].getTableName().toLowerCase();
            int totalRows = kdp.getRowCount();
            if (totalRows * kdp.getColumns().length < maxLoad) {
              DataSet ds = Util.getNewQueryDataSet("SELECT * FROM " + tname);
              if (kdp.dumpTable(ds, new File("")) > 0 || (ds.close() && false))
                return tname + ".dat - spremljen!";
              return "Tablica prazna!";              
            }
            
            Int2 sgt = kdp.findBestKeyForSegments();
            if (sgt == null) return "Greška kod spremanja!";
            
            String bestCol = kdp.getColumns()[sgt.one].getColumnName().toLowerCase();
            int bestNum = sgt.two;
            int minSegments = totalRows * kdp.getColumns().length / maxLoad + 2;
            if (bestNum / 10 <= minSegments)
              return "Greška kod spremanja!";

            Condition[] conds = kdp.createSegments(bestCol, minSegments);
            if (kdp.dumpSegments(new File(""), conds) > 0)
              return tname + ".dat - spremljen!";
            return "Greška kod spremanja!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Greška kod spremanja!";
		}
	}

	public String loadData(KreirDrop kdp) {
		try {
			noteStatus = "";
			if (kdp.insertData(new File("")) > 0)
				return "Tablica napunjena!";
			else
				return "Datoteka prazna!";
		} catch (ProcessInterruptException e) {
			throw (ProcessInterruptException) e.fillInStackTrace();
		} catch (FileNotFoundException e) {
			return "Datoteka ne postoji!";
		} catch (Exception e) {
			e.printStackTrace();
			return "Greška!";
		}
	}

	private void checkTableRow(int row) {
		if (maxctab == 0)
			return;
		String table = moduleClasses[row].substring(moduleClasses[row]
				.lastIndexOf('.') + 1);
		if (!lookupData.getlookupData().raLocate(tab, "IMETAB",
				moduleClasses[row], Locate.CASE_INSENSITIVE)) {
			System.out.println("Ubacujem tablicu u tablicu tablica");
			tab.insertRow(false);
			tab.setInt("CTAB", ++maxctab);
			tab.setString("KLASATAB", moduleClasses[row]);
			tab.setString("IMETAB", table);
			tab.post();
			tabChanged = true;
		}
		if (!tab.getString("IMETAB").equals(table))
			System.out.println("Pogresno ime tablice: " + table);
		if (tab.getString("APP") == null || tab.getString("APP").length() == 0) {
			//      Object app = JOptionPane.showInputDialog(this, "Aplikacija kojoj
			// pripada tablica "+
			//                                  tab.getString("IMETAB"), "Unos aplikacije",
			//                                  JOptionPane.PLAIN_MESSAGE, null, null, lastapp);
			//      if (app != null) {
			tab.setString("APP", "!undef!");
			tab.post();
			tabChanged = true;
			//      }
		}
		//    Valid.getValid().execSQL("SELECT MAX(ctab*1));
	}

	public static String[] getModuleClasses() {
		return moduleClasses;
	}

	public static void addModuleClass(String mc) {
	  if (Util.containsArr(moduleClasses, mc)) return;
	  String[] newModuleClasses = new String[moduleClasses.length+1];
	  for (int i = 0; i < moduleClasses.length; i++) {
	    newModuleClasses[i] = moduleClasses[i];
      }
	  newModuleClasses[moduleClasses.length] = mc;
	  moduleClasses = newModuleClasses;
	}
	
	private static String[] moduleClasses = { "hr.restart.baza.Agenti",
			"hr.restart.baza.Artikli", "hr.restart.baza.dob_art",
			"hr.restart.baza.doki", "hr.restart.baza.Doku",
			"hr.restart.baza.franka", "hr.restart.baza.Grupart",
			"hr.restart.baza.jedmj", "hr.restart.baza.Konta",
			/* "hr.restart.baza.Korisnik", */"hr.restart.baza.kup_art",
			"hr.restart.baza.nacotp", "hr.restart.baza.nacpl",
			"hr.restart.baza.namjena", "hr.restart.baza.napomene",
			"hr.restart.baza.norme", "hr.restart.baza.Orgstruktura",
			"hr.restart.baza.Parametri", "hr.restart.baza.Partneri",
			"hr.restart.baza.Pjpar", "hr.restart.baza.Porezi",
			"hr.restart.baza.rabati", "hr.restart.baza.SEQ",
			"hr.restart.baza.Serbr", "hr.restart.baza.shrab",
			"hr.restart.baza.zirorn", "hr.restart.baza.Zavtr",
			"hr.restart.baza.Shzavtr", "hr.restart.baza.Sklad",
			"hr.restart.baza.Stanje", "hr.restart.baza.stdoki",
			"hr.restart.baza.Stdoku", "hr.restart.baza.Shkonta",
			"hr.restart.baza.Valute", "hr.restart.baza.vtrabat",
			"hr.restart.baza.vtzavtr", "hr.restart.baza.Tecajevi",
			"hr.restart.baza.Aplikacija", "hr.restart.baza.Vrtros",
			"hr.restart.baza.Logotipovi", "hr.restart.baza.Pos",
			"hr.restart.baza.Stpos", "hr.restart.baza.Meskla",
			"hr.restart.baza.Stmeskla", "hr.restart.baza.Prodmj",
			"hr.restart.baza.Useri", "hr.restart.baza.Grupeusera",
			"hr.restart.baza.Banke", "hr.restart.baza.Rate",
			"hr.restart.baza.Ugovori", "hr.restart.baza.Cjenik",
			"hr.restart.baza.Kartice", "hr.restart.baza.vshrab_rab",
			"hr.restart.baza.Prava", "hr.restart.baza.Pravagrus",
			"hr.restart.baza.Pravauser", "hr.restart.baza.Prod_mj",
			"hr.restart.baza.Skupart", "hr.restart.baza.Inventura",
			"hr.restart.baza.vshztr_ztr", "hr.restart.baza.Gruppart",
			"hr.restart.baza.Kupci", "hr.restart.baza.Defshkonta",
			"hr.restart.baza.Vrdokum", "hr.restart.baza.Konta_par",
			"hr.restart.baza.Temelj", "hr.restart.baza.RN",
			"hr.restart.baza.RN_subjekt", "hr.restart.baza.RN_tekstovi",
			"hr.restart.baza.RN_znacsub", "hr.restart.baza.RN_sifznac",
			"hr.restart.baza.RN_znacajke", "hr.restart.baza.OS_Amgrupe",
			"hr.restart.baza.OS_Artikli", "hr.restart.baza.OS_Kontrola",
			"hr.restart.baza.OS_Lokacije", "hr.restart.baza.OS_Obrada1",
			"hr.restart.baza.OS_Obrada2", "hr.restart.baza.OS_Obrada4",
			"hr.restart.baza.OS_Promjene", "hr.restart.baza.OS_Revskupine",
			"hr.restart.baza.OS_Sredstvo", "hr.restart.baza.OS_Vrpromjene",
			"hr.restart.baza.OS_SI", "hr.restart.baza.OS_StSI",
			"hr.restart.baza.OS_Metaobrada", "hr.restart.baza.OS_Porijeklo",
			"hr.restart.baza.Vrstenaloga", "hr.restart.baza.Nalozi",
			"hr.restart.baza.Gkstavkerad", "hr.restart.baza.Gkstavke",
			"hr.restart.baza.Gkkumulativi", "hr.restart.baza.Skstavkerad",
			"hr.restart.baza.Skstavke", "hr.restart.baza.UIstavke",
			"hr.restart.baza.Pokriveni", "hr.restart.baza.Skkumulativi",
			"hr.restart.baza.KnjigeUI", "hr.restart.baza.Vrshemek",
			"hr.restart.baza.Programi", "hr.restart.baza.Tablice",
			"hr.restart.baza.Funkcije", "hr.restart.baza.Kljucevi",
			"hr.restart.baza.Knjigod", "hr.restart.baza.Usersklad",
			"hr.restart.baza.Refresher", "hr.restart.baza.KoloneknjUI",
			"hr.restart.baza.ShemevezeUI", "hr.restart.baza.Izvodi",
			"hr.restart.baza.Ziropar", "hr.restart.baza.VTprijenos",
			"hr.restart.baza.Radnici", "hr.restart.baza.Sifrarnici",
			"hr.restart.baza.Vrstesif", "hr.restart.baza.RadMJ",
			"hr.restart.baza.Vrodn", "hr.restart.baza.Opcine",
			"hr.restart.baza.Zupanije", "hr.restart.baza.Povjerioci",
			"hr.restart.baza.Bankepl", "hr.restart.baza.IsplMJ",
			"hr.restart.baza.Vrsteodb", "hr.restart.baza.Odbici",
			"hr.restart.baza.Radnicipl", "hr.restart.baza.Orgrad",
			"hr.restart.baza.Orgpl", "hr.restart.baza.FondSati",
			"hr.restart.baza.Vriboda", "hr.restart.baza.Nacobr",
			"hr.restart.baza.Sume", "hr.restart.baza.SumePrim",
			"hr.restart.baza.Plosnovice", "hr.restart.baza.Plosnprim",
			"hr.restart.baza.Plizv", "hr.restart.baza.Grupeizv",
			"hr.restart.baza.Grizvprim", "hr.restart.baza.Vrsteprim",
			"hr.restart.baza.Parametripl", "hr.restart.baza.Iniprim",
			"hr.restart.baza.Primanjaobr", "hr.restart.baza.Odbiciobr",
			"hr.restart.baza.Kumulrad", "hr.restart.baza.Kumulorg",
			"hr.restart.baza.Kumulorgarh", "hr.restart.baza.Kumulradarh",
			"hr.restart.baza.Primanjaarh", "hr.restart.baza.Odbiciarh",
			"hr.restart.baza.Blagajna", "hr.restart.baza.Blagizv",
			"hr.restart.baza.Stavblag", "hr.restart.baza.Stavkeblarh",
			"hr.restart.baza.Prisutobr", "hr.restart.baza.Prisutarh",
			"hr.restart.baza.Vrart", "hr.restart.baza.Zemlje",
			"hr.restart.baza.PutniNalog", "hr.restart.baza.Stavkepn",
			"hr.restart.baza.Paramblpn", "hr.restart.baza.Putnalarh",
			"hr.restart.baza.Stavpnarh", "hr.restart.baza.Defvaluedok",
			"hr.restart.baza.Ztr", "hr.restart.baza.VTZtr",
			"hr.restart.baza.VTZtrt", "hr.restart.baza.RSPeriod",
			"hr.restart.baza.RSPeriodobr", "hr.restart.baza.RSPeriodarh",
			"hr.restart.baza.OS_Objekt", "hr.restart.baza.OS_Log",
			"hr.restart.baza.OS_Arhiva", "hr.restart.baza.OS_Kontaisp",
			"hr.restart.baza.OS_Obrada3", "hr.restart.baza.OS_Obrada5",
			"hr.restart.baza.Vrsteugo", "hr.restart.baza.Virmani",
			"hr.restart.baza.VTText", "hr.restart.baza.Grizvodb",
			"hr.restart.baza.Logodat", "hr.restart.baza.Reportext",
			"hr.restart.baza.Repldef", "hr.restart.baza.Replinfo",
			"hr.restart.baza.Replurl", "hr.restart.baza.Diorep",
			"hr.restart.baza.MxPrinter", "hr.restart.baza.MxPrinterRM",
			"hr.restart.baza.MxDokument", "hr.restart.baza.Kamate",
			"hr.restart.baza.KamRac", "hr.restart.baza.KamUpl",
			"hr.restart.baza.IzvjPDV", "hr.restart.baza.StIzvjPDV",
			"hr.restart.baza.Kosobe", "hr.restart.baza.PVROdbCorg",
			"hr.restart.baza.KPR", "hr.restart.baza.PokriveniRadni",
			"hr.restart.baza.Reportdef", "hr.restart.baza.RN_vrsub",
			"hr.restart.baza.TransHistory", "hr.restart.baza.ZpZemlje",
			"hr.restart.baza.Mjesta", "hr.restart.baza.NadzornaKnjiga",
			"hr.restart.baza.PlZnacRad", "hr.restart.baza.PlZnacRadData",
			"hr.restart.baza.GrIzvZnac", "hr.restart.baza.RN_znachint",
			"hr.restart.baza.Tmjesta", "hr.restart.baza.Transdata",
			"hr.restart.baza.VTRnl", "hr.restart.baza.Blagajnici",
			"hr.restart.baza.Menus", "hr.restart.baza.TableSync",
			"hr.restart.baza.TempCRM", "hr.restart.baza.VTPred",
			"hr.restart.baza.Replication_TX", "hr.restart.baza.stugovor",
			"hr.restart.baza.Repgk", "hr.restart.baza.Detrepgk",
			"hr.restart.baza.Repgkdata", "hr.restart.baza.Detrepgkdata",
			"hr.restart.baza.Carinarnica", "hr.restart.baza.Cardrzava",
			"hr.restart.baza.Fizosobe", "hr.restart.baza.Paritet1PP",
			"hr.restart.baza.Paritet3PP", "hr.restart.baza.Vrstaprometa",
			"hr.restart.baza.Jmcarina", "hr.restart.baza.Vrstaposla",
			"hr.restart.baza.Carpotpor", "hr.restart.baza.Caroslobod",
			"hr.restart.baza.Carpostpp37", "hr.restart.baza.Carpostpp372",
			"hr.restart.baza.Cartrosotp", "hr.restart.baza.Carodob",
			"hr.restart.baza.Carvri", "hr.restart.baza.Cardav",
			"hr.restart.baza.Carnacpl", "hr.restart.baza.Cardozvole",
			"hr.restart.baza.CarTG", "hr.restart.baza.Carugovor",
			"hr.restart.baza.Cartarifa", "hr.restart.baza.Carulaz",
			"hr.restart.baza.Carulazst", "hr.restart.baza.Carizlaz",
			"hr.restart.baza.Carizlazst", "hr.restart.baza.VTCartPart",
			"hr.restart.baza.Telemark", "hr.restart.baza.Telehist",
            "hr.restart.baza.VTCparHist", "hr.restart.baza.Urdok",
            "hr.restart.baza.Urvrdok", "hr.restart.baza.Urstat",
            "hr.restart.baza.Urshist", "hr.restart.baza.Approdok",
            "hr.restart.baza.Imageinfo", "hr.restart.baza.Artrans",
            "hr.restart.baza.stdokitmp", "hr.restart.baza.Stolovi", 
            "hr.restart.baza.Verinfo", "hr.restart.baza.Artnap",
            "hr.restart.baza.Akcije", "hr.restart.baza.Stakcije",
            "hr.restart.baza.UplRobno","hr.restart.baza.PNBKonto",
            "hr.restart.baza.Kredkar","hr.restart.baza.Mesg",
            "hr.restart.baza.Smjene", "hr.restart.baza.Repxhead",
            "hr.restart.baza.Repxdata", "hr.restart.baza.dokidod",
            "hr.restart.baza.Exphead", "hr.restart.baza.Expdata",
            "hr.restart.baza.Rabshema", "hr.restart.baza.Rnser",
            "hr.restart.baza.Rnus", "hr.restart.baza.MesgStatus",
            "hr.restart.baza.Distlist","hr.restart.baza.StDistlist",
            "hr.restart.baza.Distart","hr.restart.baza.Distkal","hr.restart.baza.StDistkal",
            "hr.restart.baza.Radplsifre","hr.restart.baza.JoppdA","hr.restart.baza.JoppdB",
            "hr.restart.baza.VezaFLH",
            "hr.restart.baza.Klijenti","hr.restart.baza.Kontosobe","hr.restart.baza.Kontakti",
            "hr.restart.baza.Kanali","hr.restart.baza.KlijentStat",
            "hr.restart.baza.Segmentacija", "hr.restart.baza.Kampanje"
      };
}
