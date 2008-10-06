# -*- coding: Cp1250 -*-
import sys

CHAR = 0
INT = 1
SHORT = 2
FLOAT = 3
DATE = 4

char_types = ["alpha", "char", "character", "string"]
int_types = ["int", "integer"]
short_types = ["short"]
float_types = ["float", "numeric", "decimal"]
date_types = ["date", "timestamp"]

types_all = [char_types, int_types, short_types, float_types, date_types]

dx_types = ["com.borland.dx.dataset.Variant.STRING",
            "com.borland.dx.dataset.Variant.INT",
            "com.borland.dx.dataset.Variant.SHORT",
            "com.borland.dx.dataset.Variant.BIGDECIMAL",
            "com.borland.dx.dataset.Variant.TIMESTAMP"]

sql_types = ["1", "4", "5", "2", "93"]

invis = "com.borland.jb.util.TriStateProperty.FALSE"

dmdef = []
kreator = []


def main():
   fname = len(sys.argv) > 1 and sys.argv[1] or "tables.def"
   process(fname)

def process(fname):
   try:
      f = open(fname, "r")
   except (IOError, OSError):
      sys.exit(0)

   lastline = []
   while lastline != [""]:
      lines, lastline = load_definition(f, lastline)
      process_commands(lines)

   f.close()

   tf = open("dm.txt", "w")
   out(tf, dmdef)
   tf.close()

   tf = open("kreator.txt", "w")
   tf.write("".join(kreator))
   tf.write("\n")
   tf.close()

def load_definition(f, lastline):
   lines = lastline
   
   while f:
      line = f.readline()
      if not line: break
      line = line.strip()
      if line.startswith("$") and lines: break
      if not line or line.startswith("#"): continue
      lines.append(line)

   return lines, [line]

def out(f, list):
   f.write("\n".join(list + [""]))

def convert_encoding(s):
   s = s.replace('Đ', '\\u0110').replace('Č','\\u010C').replace('Ć','\\u0106')
   return s.replace('đ', '\\u0111').replace('č','\\u010D').replace('ć','\\u0107')
         
def process_commands(lines):
   fields = []
   clones = {}
   dataset = "QueryDataSet"
   for c in lines:
      if c.startswith("$"):
         names = [part.strip() for part in c[1:].split(",")]
         names.extend([names[0]]*2)
         tablename, shortname, filename = names[0:3]
      elif c.startswith("+"):
         parts = c.split(":")
         if len(parts) > 1:
            clones[parts[0].strip()[1:]] = parts[1].strip()
         else:
            dataset = "raDataSet"
      else:
         fields.append(c.split(";") + [""])

   
   utablename = tablename.upper()
   shortname = shortname.lower()
   
   column_def = []
   jbinit_def = [""]
   sql_def = []
   colnames = []
   colnames_length = 30 + len(shortname)
   indices = []
   pkeys = []
   
   for f in fields:
      name, typestr, length, index, default, caption, width = \
         [p.strip() for p in f[0:7]]
      
      index, typestr = index.lower(), typestr.lower()
      lname, uname = name.lower(), name.upper();
      colname = shortname + uname

      caption = convert_encoding(caption)

      colnames.append(colname)
      colnames_length += len(colname) + 2
      if colnames_length > 160:
         colnames[-1] = "\n        " + colnames[-1]
         colnames_length = len(colnames[-1])

      if index in ["pkey", "key", "yes", "idx"]: indices.append('"%s"' % lname)
      if index in ["pkey", "key"]: pkeys.append(lname)
      
      for types in types_all:
         if typestr in types:
            type = types_all.index(types)

      if type in [FLOAT, INT, SHORT]:
         if length.find(",") != -1:
            length, scale = length.split(",")
         elif length.find(".") != -1:
            length, scale = length.split(".")
         else:
            type = eval(length) >= 5 and INT or SHORT
            
      column_def.append("  Column %s = new Column();" % colname)

      jbinit_def.append('    %s.setCaption("%s");' % (colname, caption))
      jbinit_def.append('    %s.setColumnName("%s");' % (colname, uname))
      jbinit_def.append('    %s.setDataType(%s);' % (colname, dx_types[type]))
      if type in [CHAR, FLOAT]:
         jbinit_def.append('    %s.setPrecision(%s);' % (colname, length))
      if type == FLOAT:
         jbinit_def.append('    %s.setScale(%s);' % (colname, scale))
         jbinit_def.append('    %s.setDisplayMask("###,###,##0.%s");' % (colname, "0" * int(scale)))
         jbinit_def.append('    %s.setDefault("0");' % colname)
      if type == DATE:
         jbinit_def.append('    %s.setDisplayMask("dd-MM-yyyy");' % colname)
         jbinit_def.append('//    %s.setEditMask("dd-MM-yyyy");' % colname)
      if index in ["pkey","key"]:
         jbinit_def.append('    %s.setRowId(true);' % colname)
      jbinit_def.append('    %s.setTableName("%s");' % (colname, utablename))
      jbinit_def.append('    %s.setServerColumnName("%s");' % (colname, uname))
      jbinit_def.append('    %s.setSqlType(%s);' % (colname, sql_types[type]))
      setwidth = ""
      if type in [INT, SHORT]:
         setwidth = length
      if type == CHAR and int(length) >= 30:
         setwidth = "30"
      if type == DATE:
         setwidth = "10"
      if width.isdigit():
         setwidth = width
      if setwidth:
         jbinit_def.append('    %s.setWidth(%s);' % (colname, setwidth))
      
      if width.startswith("*"):
         jbinit_def.append('    %s.setVisible(%s);' % (colname, invis))
      if default:
         if default.startswith('"'): default = default[1:-1]
         jbinit_def.append('    %s.setDefault("%s");' % (colname, default))

      notnull = index in ["pkey", "key"] and ", true" or ""    
      if type == CHAR:
         defval = default and ', "%s"' % default or ""
         sql_def.append('       .addChar("%s", %s%s%s)' % (lname, length, defval, notnull))
      elif type == INT:
         sql_def.append('       .addInteger("%s", %s%s)' % (lname, length, notnull))
      elif type == SHORT:
         sql_def.append('       .addShort("%s", %s%s)' % (lname, length, notnull))
      elif type == FLOAT:
         sql_def.append('       .addFloat("%s", %s, %s)' % (lname, length, scale))
      elif type == DATE:
         sql_def.append('       .addDate("%s")' % lname)

   objects = []
   getters = []
   creates = clones and [""] or []
   for c in clones:
      objects += ["  QueryDataSet %s%s = new %s();" % (shortname, c, dataset)]
      getters += ["  public QueryDataSet get%s() {" % c.capitalize(),
                  "    return %s%s;" % (shortname, c),
                  "  }", ""]
      creates += ['    createFilteredDataSet(%s%s, "%s");' % (shortname, c, clones[c])]
   
   file_header = ["package hr.restart.baza;",
                  "import com.borland.dx.dataset.*;",
                  "import com.borland.dx.sql.dataset.*;",
                  "","",""]

   class_header = ["public class %s extends KreirDrop implements DataModule {" % tablename]
   
   class_data = ["", 
                 "  dM dm  = dM.getDataModule();",
                 "  private static %s %sclass;" % (tablename, tablename), "",
                 "  QueryDataSet %s = new %s();" % (shortname, dataset)] + objects + [""]
   
   class_getter = ["  public static %s getDataModule() {" % tablename,
                   "    if (%sclass == null) {" % tablename,
                   "      %sclass = new %s();" % (tablename, tablename),
                   "    }",
                   "    return %sclass;" % tablename,
                   "  }", ""]

   qds_getter = ["  public QueryDataSet getQueryDataSet() {",
                 "    return %s;" % shortname,
                 "  }", ""]

   constructor = ["  public %s() {" % tablename,
                  "    try {",
                  "      modules.put(this.getClass().getName(), this);",
                  "      jbInit();",
                  "    }",
                  "    catch(Exception e) {",
                  "      e.printStackTrace();",
                  "    }",
                  "  }", ""]
   
   jbinit_def[0] = "  private void jbInit() throws Exception {"
   
   jbinit_def.extend(["    %s.setResolver(dm.getQresolver());" % shortname,
                      "    %s.setQuery(new QueryDescriptor(dm.getDatabase1(),"\
                            '"select * from %s", null, true, Load.ALL));' % (shortname, tablename),
                      "    setColumns(new Column[] {%s});" % (", ".join(colnames))] + creates + ["  }", ""])
                      
   setall = ["  public void setall() {", "",
             '    ddl.create("%s")' % tablename]

   setall.extend(sql_def)
   
   setall.extend(['       .addPrimaryKey("%s");' % ",".join(pkeys), "", "",
                  '    Naziv = "%s";' % tablename, "",
                  "    SqlDefTabela = ddl.getCreateTableString();", "",
                  "    String[] idx = new String[] {%s};" % ", ".join(indices),
                  '    String[] uidx = new String[] {};',
                  "    DefIndex = ddl.getIndices(idx, uidx);",
                  "    NaziviIdx = ddl.getIndexNames(idx, uidx);",
                  "  }"])
   

   #output
   
   java = open(tablename.split(".")[0]+".java", "w"); 
   
   out(java, file_header)
   out(java, class_header)
   out(java, class_data)
   out(java, column_def)
   out(java, [""])
   out(java, class_getter)
   out(java, qds_getter)
   out(java, getters)
   out(java, constructor)
   out(java, jbinit_def)
   out(java, setall)
   out(java, ["}"])

   java.close()
   print java.name, "written."

   dmdef.append("")
   dmdef.append("  public com.borland.dx.sql.dataset.QueryDataSet get%s() {" % tablename)
   dmdef.append("    return %s.getDataModule().getQueryDataSet();" % tablename)
   dmdef.append("  }")
   kreator.append(', "hr.restart.baza.%s"' % tablename)
   

if __name__ == "__main__": main()
