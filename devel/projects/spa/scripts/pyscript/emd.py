import sys
import lay

oneliners = ["dataset", "visible", "check", "focus", "unique", "key"]


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

def getkey(line):
   return ", ".join(['"%s"' % v.strip() for v in line.split(",")])

def error(line, text):
   print "Error in line:"
   print "  ", line
   print text
   sys.exit(0)

def get_ranges(elems):
   ranges = []
   defaults = []
   for f in elems:
      fc = elems[f].get("column")
      if fc and fc.endswith("from"):
         for t in elems:
            tc = elems[t].get("column")
            if tc and tc.startswith(fc[:-4]) and tc.endswith("to"):
               ranges += ["    this.addSelRange(%s, %s);" % (f, t)]
               defaults += ['    getSelRow().setTimestamp("%s-from", hr.restart.util.Util.getUtil().getFirstDayOfMonth());  /**@todo: Provjeri datum */' % fc[:-4],
                            '    getSelRow().setTimestamp("%s-to", hr.restart.util.Valid.getValid().getToday());' % tc[:-2]]

   return ranges, defaults

def process_commands(lines):
   ism = 0
   isd = 0
   isp = 0
   isl = 0
   laym = []
   layd = []
   layp = []
   attm = {}
   attd = {}
   attp = {}

   for c in lines:
      if c.startswith("$"):
         fname, pname = [p.strip() for p in c[1:].split(",") + ["robno"]][:2]
      elif c.split(":")[0] in oneliners:
         isl = 0
         name, value = c.split(":")[:2]
         if ism:
            attm[name] = value
         elif isd:
            attd[name] = value
         elif isp:
            if name in ["dataset", "visible", "unique", "key"]:
               error(c, "Invalid command in preselect.")
            else:
               attp[name] = value
         elif c.split(":")[0] == "key":
            attm[name] = value
            attd[name] = value
         else:
            error(c, "Section not declared (master, detail or preselect).")
      elif c.startswith("layout"):
         if ism or isd or isp:
            isl = 1
         else:
            error(c, "Section not declared (master, detail or preselect).")
      elif c.startswith("master"):
         ism = 1
         isd = isp = isl = 0
      elif c.startswith("detail"):
         isd = 1
         ism = isp = isl = 0
      elif c.startswith("preselect"):
         isp = 1
         ism = isd = isl = 0
      elif isl == 1:
         if ism:
            laym += [c]
         elif isd:
            layd += [c]
         elif isp:
            layp += [c]
         else:
            error(c, "Silly error")
      else:
         error(c, "Unrecognized command.")

   melems, mtxname, mvars, mdefs, mpadds = lay.process(laym, "ds")

   delems, dtxname, dvars, ddefs, dpadds = lay.process(layd, "ds")

   pelems, ptxname, pvars, pdefs, ppadds = lay.process(layp, "getSelDataSet()")


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

   commons = ["  raCommonClass rcc = raCommonClass.getraCommonClass();",
              "  dM dm = dM.getDataModule();",
              "  Valid vl = Valid.getValid();"]

   class_header = ["public class frm%s extends raMasterDetail {" % fname,
                   commons[0], commons[1], commons[2], "",
                   "  jp%sMaster jpMaster;" % fname,
                   "  jp%sDetail jpDetail;" % fname, ""]

#   pres_add = []
#   if layp:
#      class_header += ["  pres%s pres;" % fname, ""]
#      pres_add = ["", "    pres = new pres%s(this);" % fname,
#                  "    pres.setSelDataSet(this.getMasterSet());",
#                  "    pres.setSelPanel(pres.jpDetail);"]

   pranges = []
   pdefaults = []
   if layp:
      pranges, pdefaults = get_ranges(pelems)
      
      

   jpm_header = ["public class jp%sMaster extends JPanel {" % fname,
                 commons[0], commons[1], "",
                 "  frm%s f%s;" % (fname, fname),
                 "  JPanel jpDetail = new JPanel();", ""]
   
   jpd_header = ["public class jp%sDetail extends JPanel {" % fname,
                 commons[0], commons[1], "",
                 "  frm%s f%s;" % (fname, fname),
                 "  JPanel jpDetail = new JPanel();", ""]


   jpp_header = ["public class pres%s extends PreSelect {" % fname,
                 commons[0], commons[1], commons[2], "",
 #                "  frm%s f%s;" % (fname, fname),
                 "  JPanel jpDetail = new JPanel();", ""]

   
   constructor = ["", "  public frm%s() {" % fname,
                  "    try {",
                  "      jbInit();",
                  "    }",
                  "    catch(Exception e) {",
                  "      e.printStackTrace();",
                  "    }",
                  "  }", ""]

   mconst = ["", "  public jp%sMaster(frm%s md) {" % (fname, fname),
             "    try {",
             "      f%s = md;" % fname] + constructor[3:]

   dconst = ["", "  public jp%sDetail(frm%s md) {" % (fname, fname)] + mconst[2:]
   
   pconst = ["", "  public pres%s() {" % fname] + constructor[2:]

   mentry_point = []
   if "unique" in attm:
      uname = mtxname[attm["unique"].split(",")[0].strip()]
      mentry_point = ["  public void EntryPointMaster(char mode) {",
                      "    // Disabla tekst komponente kljuca kod izmjene",
                      "    if (mode == 'I') {",
                      "      rcc.setLabelLaF(jpMaster.%s, false);" % uname,
                      "    }", "  }", ""]
##      municheck = "    if (mode == 'N' && vl.notUnique(%s))" % uname
##   else:
##      municheck = "    if (mode == 'N' && vl.notUnique()) /**@todo: Provjeriti jedinstvenost kljuca mastera */"

   dentry_point = []
   if "unique" in attd:
      uname = dtxname[attd["unique"].split(",")[0].strip()]
      dentry_point = ["  public void EntryPointDetail(char mode) {",
                      "    // Disabla tekst komponentu kljuca kod izmjene",
                      "    if (mode == 'I') {",
                      "      rcc.setLabelLaF(jpDetail.%s, false);" % uname,
                      "    }", "  }", ""]

   dunicheck = "    if (mode == 'N' && notUnique()) /**@todo: Provjeriti jedinstvenost kljuca detaila */"

   dunique = ["  public boolean notUnique() {",
              "    /**@todo: Implementirati notUnique metodu */",
              "  }", ""]
      
   if "focus" in attm:
      nname = mtxname[attm["focus"].split(",")[0].strip()]
      iname = mtxname[attm["focus"].split(",")[1].strip()]
      nfocus = "      jpMaster.%s.requestFocus();" % nname
      ifocus = "      jpMaster.%s.requestFocus();" % iname
   else:
      nfocus = "      /**@todo: Postaviti fokus za unos novog sloga mastera */"
      ifocus = "      /**@todo: Postaviti fokus za izmjenu sloga mastera */"

   mset_fokus = ["  public void SetFokusMaster(char mode) {",
                 "    if (mode == 'N') {", nfocus,
                 "    } else if (mode == 'I') {", ifocus,
                 "    }", "  }", ""]

   if "focus" in attd:
      nname = dtxname[attd["focus"].split(",")[0].strip()]
      iname = dtxname[attd["focus"].split(",")[1].strip()]
      nfocus = "      jpDetail.%s.requestFocus();" % nname
      ifocus = "      jpDetail.%s.requestFocus();" % iname
   else:
      nfocus = "      /**@todo: Postaviti fokus za unos novog sloga detaila */"
      ifocus = "      /**@todo: Postaviti fokus za izmjenu sloga detaila */"

   dset_fokus = ["  public void SetFokusDetail(char mode) {",
                 "    if (mode == 'N') {", nfocus,
                 "    } else if (mode == 'I') {", ifocus,
                 "    }", "  }", ""]

   pset_focus = []
   if "focus" in attp:
      nname = ptxname[attp["focus"].split(",")[0].strip()]
      pset_focus = ["  public void SetFokus() {"] + pdefaults + ["    %s.requestFocus();" % nname, "  }", ""]


   if "check" in attm:
      check = ["vl.isEmpty(jpMaster.%s)" % mtxname[c.strip()] for c in attm["check"].split(",")]
      mempty = "    if (%s)" % " || ".join(check)
   else:
      mempty = "    if (vl.isEmpty()) /**@todo: Provjeriti polja s obaveznim unosom */"

   mvalidacija = ["  public boolean ValidacijaMaster(char mode) {", mempty,
                  "      return false;",
                  "    return true;",
                  "  }", ""]

   if "check" in attd:
      check = ["vl.isEmpty(jpDetail.%s)" % dtxname[c.strip()] for c in attd["check"].split(",")]
      dempty = "    if (%s)" % " || ".join(check)
   else:
      dempty = "    if (vl.isEmpty()) /**@todo: Provjeriti polja s obaveznim unosom */"

   dvalidacija = ["  public boolean ValidacijaDetail(char mode) {", dempty,
                  "      return false;", dunicheck,
                  "      return false;",
                  "    return true;",
                  "  }", ""]

   pvalidacija = []
   if "check" in attp:
      check = ["vl.isEmpty(%s)" % ptxname[c.strip()] for c in attp["check"].split(",")]
      pempty = "    if (%s)" % " || ".join(check)
      pvalidacija = ["  public boolean Validacija() {", pempty,
                     "      return false;",
                     "    return true;",
                     "  }", ""]
   
   if "visible" in attm:
      mvis = "    this.setVisibleColsMaster(new int[] {%s});" % ", ".join([v.strip() for v in attm["visible"].split(",")])
   else:
      mvis = "    this.setVisibleColsMaster(new int[] {0,1}); /**@todo: Odrediti vidljive kolone mastera */"

   if "visible" in attd:
      dvis = "    this.setVisibleColsDetail(new int[] {%s});" % ", ".join([v.strip() for v in attd["visible"].split(",")])
   else:
      dvis = "    this.setVisibleColsDetail(new int[] {0,1}); /**@todo: Odrediti vidljive kolone mastera */"

   if "dataset" in attm:
      mdataset = "    this.setMasterSet(dm.get%s());" % attm["dataset"].strip()
   else:
      mdataset = "    this.setMasterSet(dm.get); /**@todo: Ubaciti dataset mastera */"
      attm["dataset"] = ""

   if "dataset" in attd:
      ddataset = "    this.setDetailSet(dm.get%s());" % attd["dataset"].strip()
   else:
      ddataset = "    this.setDetailSet(dm.get); /**@todo: Ubaciti dataset detaila */"
      attd["dataset"] = ""

   keys = []
   if attm["key"] == attd["key"]:
      if attm["key"]:
         keys = ["  String[] key = new String[] {%s};" % getkey(attm["key"])]
      else:
         keys = ["  String[] key = new String[] {}; /**@todo: Definirati master-detail vezu */"]
      mkey = "    this.setMasterKey(key);"
      dkey = "    this.setDetailKey(key);"
   else:
      if attm["key"]:
         mkey = "    this.setMasterKey(new String[] {%s});" % getkey(attm["key"])
      else:
         mkey = "    this.setMasterKey(new String[] {}); /**@todo: Definirati master key */"
      if attd["key"]:
         dkey = "    this.setDetailKey(new String[] {%s});" % getkey(attd["key"])
      else:
         dkey = "    this.setDetailKey(new String[] {}); /**@todo: Definirati detail key */"
      
   mbind = [bind for bind in mdefs if bind.find("setDataSet(ds)") >= 0]
   mdefs = [norm for norm in mdefs if norm.find("setDataSet(ds)") < 0]
   
   dbind = [bind for bind in ddefs if bind.find("setDataSet(ds)") >= 0]
   ddefs = [norm for norm in ddefs if norm.find("setDataSet(ds)") < 0]
   
   mbinder = ["  public void BindComponents(DataSet ds) {"]
   mbinder += mbind
   mbinder += ["  }", ""]
   
   dbinder = ["  public void BindComponents(DataSet ds) {"]
   dbinder += dbind
   dbinder += ["  }", ""]
   
   jbinit_def = ["  private void jbInit() throws Exception {", mdataset,
                 '    this.setNaslovMaster("%s"); /**@todo: Naslov mastera */' % attm["dataset"].strip(),
                 mvis, mkey, "    jpMaster = new jp%sMaster(this);" % fname,
                 "    this.setJPanelMaster(jpMaster);", "", ddataset,
                 '    this.setNaslovDetail("Stavke %s"); /**@todo: Naslov detaila */' % attd["dataset"].strip(),
                 dvis, dkey, "    jpDetail = new jp%sDetail(this);" % fname,
                 "    this.setJPanelDetail(jpDetail);", "  }", "}"]

   jbm = jbinit_def[:1]
   jbd = jbinit_def[:1]
   jbp = jbinit_def[:1]

   if laym:
      jbm += mdefs
      jbm += [""]
      jbm += mpadds
      jbm += ["", "    BindComponents(f%s.getMasterSet());" % fname,
              "    /**@todo: Odkomentirati sljedeæu liniju :) */",
              "//    this.add(jpDetail, BorderLayout.CENTER);"]
      jbm += ["  }", "}"]
      
   if layd:
      jbd += ddefs
      jbd += [""]
      jbd += dpadds
      jbd += ["", "    BindComponents(f%s.getDetailSet());" % fname,
              "    /**@todo: Odkomentirati sljedeæu liniju :) */",
              "//    this.add(jpDetail, BorderLayout.CENTER);"]
      jbd += ["  }", "}"]

   if layp:
      jbp += ["    this.setSelDataSet(dm.get%s());" % attm["dataset"].strip()]
      jbp += pdefs
      jbp += [""]
      jbp += ppadds
      jbp += [""]
      jbp += pranges
      jbp += ["    this.setSelPanel(jpDetail);"]
      jbp += ["  }", "}"]
      
   #output

   java = open("frm" + fname + ".java", "w");

   out(java, package)
   out(java, imports)
   out(java, class_header)
   out(java, keys)
   out(java, constructor)
   out(java, mentry_point)
   out(java, mset_fokus)
   out(java, mvalidacija)
   out(java, dentry_point)
   out(java, dset_fokus)
   out(java, dvalidacija)
   out(java, dunique)
   out(java, jbinit_def)

   java.close()
   print "frm%s.java written." % fname

   java = open("jp" + fname + "Master.java", "w");
   out(java, package)
   out(java, imports)
   out(java, jpm_header)
   out(java, mvars)
   out(java, mconst)
   out(java, mbinder)
   out(java, jbm)

   java.close()
   print "jp%sMaster.java written." % fname
   
   java = open("jp" + fname + "Detail.java", "w");
   out(java, package)
   out(java, imports)
   out(java, jpd_header)
   out(java, dvars)
   out(java, dconst)
   out(java, dbinder)
   out(java, jbd)

   java.close()
   print "jp%sDetail.java written." % fname

   if layp:
      java = open("pres" + fname + ".java", "w");
      out(java, package)
      out(java,imports)
      out(java, jpp_header)
      out(java, pvars)
      out(java, pconst)
      if pset_focus:
         out(java, pset_focus)
      if pvalidacija:
         out(java, pvalidacija)
      out(java, jbp)
      
      java.close()
      print "pres%s.java written." % fname

   
if __name__ == "__main__": main()
