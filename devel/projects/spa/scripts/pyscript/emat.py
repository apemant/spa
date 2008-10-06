# -*- coding: Cp1250 -*-
import sys
import lay

oneliners = ["dataset", "visible", "check", "focus", "unique"]


def main():
   fname = len(sys.argv) > 1 and sys.argv[1] or "frm.def"
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
  
      
def load_definition(f, lastline):
   lines = lastline
   
   while f:
      line = f.readline()
      if not line: break
      line = line.strip()
      if line.startswith("$") and lines: break
      if not line or line.startswith("#"): continue
      lines += [line.split("#")[0]]
   
   return lines, [line]


def out(f, list):
   if list:
      f.write("\n".join(list + [""]))


def process_commands(lines):
   layout_status = 0
   layout = []
   attrib = {}

   for c in lines:
      if c.startswith("$"):
         fname, pname = [p.strip() for p in c[1:].split(",") + ["robno"]][:2]
      elif c.split(":")[0] in oneliners:
         layout_status = 0
         name, value = c.split(":")[:2]
         attrib[name] = value
      elif c.startswith("layout"):
         layout_status = 1
      elif layout_status == 1:
         layout += [c]
      else:
         print "Error in line:"
         print "  ",c
         print "Unrecognized command."
         return

   elems, txname, vars, defs, padds = lay.process(layout, "ds")

   package = ["package hr.restart.%s;" % pname, ""]

   imports = ["import java.awt.*;",
              "import java.awt.event.*;",
              "import javax.swing.*;",
              "import javax.swing.text.JTextComponent;",
              "import com.borland.jbcl.layout.*;",
              "import com.borland.dx.dataset.*;",
              "import com.borland.dx.sql.dataset.*;",
              "import hr.restart.swing.*;",
              "import hr.restart.baza.*;",
              "import hr.restart.util.*;", "", ""]

   class_header = ["public class frm%s extends raMatPodaci {" % fname,
                   "  raCommonClass rcc = raCommonClass.getraCommonClass();",
                   "  dM dm = dM.getDataModule();",
                   "  Valid vl = Valid.getValid();", "",
                   "  jp%s jpDetail;" % fname, ""]

   jp_header = ["public class jp%s extends JPanel {" % fname,
                "  raCommonClass rcc = raCommonClass.getraCommonClass();",
                "  dM dm = dM.getDataModule();",
                "  Valid vl = Valid.getValid();", "",
                "  frm%s f%s;" % (fname, fname),
                "  JPanel jpDetail = new JPanel();", ""]
                

   constructor = ["", "  public frm%s() {" % fname,
                  "    try {",
                  "      jbInit();",
                  "    }",
                  "    catch(Exception e) {",
                  "      e.printStackTrace();",
                  "    }",
                  "  }", ""]

   jpconst = ["", "  public jp%s(frm%s f) {" % (fname, fname),
              "    try {",
              "      f%s = f;" % fname] + constructor[3:]
   
   entry_point = ""
   
   if "unique" in attrib:
      uname = txname[attrib["unique"].split(",")[0].strip()]
      disable = ["      rcc.setLabelLaF(jpDetail.%s, false);" % uname]
      if elems[uname]["type"] == lay.RANAV:
         for sec in elems[uname]["txfields"]:
            disable += ["      rcc.setLabelLaF(jpDetail.%s, false);" % sec]
         disable += ["      rcc.setLabelLaF(jpDetail.%s, false);" % elems[uname]["button"]]
         
      entry_point = ["  public void EntryPoint(char mode) {",
                     "    // Disabla tekst komponente kljuca kod izmjene",
                     "    if (mode == 'I') {"] + disable + ["    }", "  }", ""]
      unicheck = "    if (mode == 'N' && vl.notUnique(jpDetail.%s))" % uname
   else:
      unicheck = "    if (mode == 'N' && vl.notUnique()) /**@todo: Provjeriti jedinstvenost kljuca */"
      
   navempty = []
   if "focus" in attrib:
      nname = txname[attrib["focus"].split(",")[0].strip()]
      iname = txname[attrib["focus"].split(",")[1].strip()]
      
      for nav in elems:
         if elems[nav]["type"] == lay.RANAV:
            navempty += ["      jpDetail.%s.forceFocLost();" % nav]
      
      nfocus = "      jpDetail.%s.requestFocus();" % nname
      ifocus = "      jpDetail.%s.requestFocus();" % iname
   else:
      nfocus = "      /**@todo: Postaviti fokus za unos novog sloga */"
      ifocus = "      /**@todo: Postaviti fokus za izmjenu sloga */"

   set_fokus = ["  public void SetFokus(char mode) {",
                "    if (mode == 'N') {"] + navempty + [nfocus,
                "    } else if (mode == 'I') {", ifocus,
                "    }", "  }", ""]

   if "check" in attrib:
      check = ["vl.isEmpty(jpDetail.%s)" % txname[c.strip()] for c in attrib["check"].split(",")]
      empty = "    if (%s)" % " || ".join(check)
   else:
      empty = "    if (vl.isEmpty()) /**@todo: Provjeriti polja s obaveznim unosom */"

   validacija = ["  public boolean Validacija(char mode) {", empty,
                 "      return false;", unicheck,
                 "      return false;",
                 "    return true;",
                 "  }", ""]
                 
   bind = [bind for bind in defs if bind.find("setDataSet(ds)") >= 0]
   defs = [norm for norm in defs if norm.find("setDataSet(ds)") < 0]
                 
   binder = ["  public void BindComponents(DataSet ds) {"]
   binder += bind
   binder += ["  }", ""]

   if "visible" in attrib:
      vis = "    this.setVisibleCols(new int[] {%s});" % ", ".join([v.strip() for v in attrib["visible"].split(",")])
   else:
      vis = "    this.setVisibleCols(new int[] {0,1}); /**@todo: Odrediti vidljive kolone */"

   if "dataset" in attrib:
      dataset = "    this.setRaQueryDataSet(dm.get%s());" % attrib["dataset"].strip()
   else:
      dataset = "    this.setRaQueryDataSet(dm.get); /**@todo: Ubaciti dataset */"
      
   jbinit_def = ["  private void jbInit() throws Exception {", dataset, vis,
                 "    jpDetail = new jp%s(this);" % fname,
                 "    this.setRaDetailPanel(jpDetail);", "  }", "}"]

   jbd = jbinit_def[:1]
   if layout:
      jbd += defs
      jbd += [""]
      jbd += padds
      jbd += ["", "    /**@todo: Odkomentirati sljedeću liniju :) */",
      		  "//    this.add(jpDetail, BorderLayout.CENTER);"]
      jbd += ["  }", "}"]
   
   #output

   java = open("frm" + fname + ".java", "w");

   out(java, package)
   out(java, imports)
   out(java, class_header)
#   out(java, vars)
   out(java, constructor)
   out(java, entry_point)
   out(java, set_fokus)
   out(java, validacija)
   out(java, jbinit_def)

   java.close()
   print "frm%s.java written." % fname

   java = open("jp" + fname + ".java", "w");

   out(java, package)
   out(java, imports)
   out(java, jp_header)
   out(java, vars)
   out(java, jpconst)
   out(java, binder)
   out(java, jbd)

   java.close()
   print "jp%s.java written." % fname
   
   
if __name__ == "__main__": main()
