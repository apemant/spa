# -*- coding: Cp1250 -*-
LABEL = 1
TEXT = 2
RATEXT = 3
RANAV = 4
RANAV2 = 5
BUTTON = 6
UPLABEL = 7
CHECK = 8
RADIO = 9

unnamedt = 0
unnamedc = 0

def convert_encoding(s):
   s = s.replace('Đ', '\\u0110').replace('Č','\\u010C').replace('Ć','\\u0106')
   return s.replace('đ', '\\u0111').replace('č','\\u010D').replace('ć','\\u0107')

def strip_suffix(column):
   if column.endswith("from"):
      return column[:-4]
   elif column.endswith("to"):
      return column[:-2]
   else:
      return column

def get_column(part):
   """Vraca tuple (kolona, labela) iz stringa oblika kolona(labela)."""
   
   parts = part.split("(") + [""]
   return parts[0].strip(), parts[1].split(")")[0].strip()

def data(type, width, xpos, ypos, text = ""):
   """Vraca mapu atributa ekranske komponente.

   type  - tip komponente (LABEL, TEXT, RATEXT, RANAV itd.).
   width - sirina komponente u pikselima.
   xpos  - horizontalna pozicija u pikselima.
   ypos  - vertikalna pozicija u pikselima.
   text  - (opcionalno) tekst labele (za labele i buttone)."""
   
   dat = {}
   dat["type"] = type
   dat["width"] = width
   dat["xpos"] = xpos
   dat["ypos"] = ypos
   if text:
      dat["text"] = text
   return dat

def parse_elem(line, xpos, ypos):
   """Generira mapu sa ekranskim komponentama generiranim iz jedne komponente
   layouta (line). Kljucevi mape su imena varijabli, a vrijednosti su mape
   atributa komponente. Vraca tuple sastavljen od mape komponenti, liste
   imena komponenti te oznake ima li u ovoj layout komponenti labela iznad
   tekst komponenti (potrebno zbog vertikalnog pomaka, koji je 25 ako nema
   gornjih labela, odn. 45 ako ima)

   xpos  - horizontalna pozicija pocetka layout komponente.
   ypos  - vertikalna pozicija."""
   
   global unnamedt, unnamedc
   elems = {}              # mapa ekranskih komponenti
   el = []                 # popis imena ekranskih komponenti (zbog redoslijeda)
   abbr = ""               # kratica iz koje se generiraju imena
   ladd = 0                # dodatni pomak labele ispred tekst komponenti
   lab2 = 0                # oznaka postojanja labela iznad tekst komponenti
   oldxpos = xpos

   label, text = ([""] + line.strip().split(":"))[-2:]

   if label:
      lfull, lshort = (label.split(",") + [""])[:2]
      abbr = lshort and lshort.strip().capitalize() or ""

   if xpos > 0:
      ladd = xpos + 25
      xpos -= 150
      oldxpos = xpos
      if label:
         xpos += (len(lfull) // 10) * 75 + 130

   if text.strip().isdigit():
      if int(text):
         if not label:
            el += ["jl"]
            elems[el[-1]] = data(LABEL, int(text), 150 + xpos, ypos, " ")
         else:
            if not abbr:
               unnamedt += 1
               abbr = "%d" % unnamedt
            el += ["jt" + abbr]
            elems[el[-1]] = data(TEXT, int(text), 150 + xpos, ypos)
      else:
         if not abbr:
            unnamedc += 1
            abbr = "%d" % unnamedc
         xpos = oldxpos
         lfull = label and lfull or "CheckBox%s" % abbr
         width = 50 + (len(lfull) // 5) * 20
         el += ["jcb" + abbr]
         elems[el[-1]] = data(CHECK, width, 150 + xpos, ypos, lfull)
         label = ""
   else:
      tokens = text.strip().split(" ")
      col = " ".join(tokens[:-1])
      width = tokens[-1]
#      col, width = text.strip().split(" ", 1)
      if not abbr:
         abbr = col.split("+")[0].split("(")[0].strip().lower().capitalize()
      if col.find("+") == -1:
         if not width.isdigit():
            raise SyntaxError, "Bad component: '%s'" % line.strip()
         if int(width):
            name, lab = get_column(col)
            if lab:
               lab2 = 1
               el += ["jla" + abbr]
               elems[el[-1]] = data(UPLABEL, int(width) - 2, 150 + xpos + 1, ypos, lab.replace("-", " "))
            dat = data(RATEXT, int(width), 150 + xpos, ypos)
            dat["column"] = name
            el += ["jra" + abbr]
            elems[el[-1]] = dat
         else:
            xpos = oldxpos
            lfull = label and lfull or "CheckBox%s" % abbr
            width = 50 + (len(lfull) // 5) * 20
            el += ["jcb" + abbr]
            dat = data(CHECK, width, 150 + xpos, ypos, lfull)
            dat["column"] = col
            elems[el[-1]] = dat
            label = ""
      else:
         xp = xpos + 150
         widths = width.split("+")
         cols = col.split("+")
         txfields = []
         colnames = []
         for index in xrange(len(cols) - 1):
            if not widths[index].isdigit():
               raise SyntaxError, "Bad component: '%s'" % line.strip()
            
            name, lab = get_column(cols[index])
            name = name.lower().capitalize()
            dat = data(index and RANAV2 or RANAV, int(widths[index]), xp, ypos)
            dat["column"] = name.upper()
            if lab:
               lab2 = 1
               el += ["jla" + name]
               elems[el[-1]] = data(UPLABEL, dat["width"] - 2, xp + 1, ypos, lab.replace("-", " "))
            if index:
               txfields += ["jlr" + name]
               colnames += [name.upper()]
            else:
               kdat = dat
               kname = name

            el += ["jlr" + name]
            elems[el[-1]] = dat
            
            xp += dat["width"] + 5

         kdat["colnames"] = colnames
         kdat["txfields"] = txfields
         kdat["dataset"] = cols[-1]
         kdat["button"] = "jbSel" + kname

         dat = data(BUTTON, 21, xp, ypos)
         dat["ranav"] = "jlr" + kname
         el += ["jbSel" + kname]
         elems[el[-1]] = dat


   if label:
      dat = data(LABEL, -1, 15 + ladd, ypos)
      dat["text"] = lfull.strip()
      el = ["jl" + abbr] + el
      elems[el[0]] = dat

   return elems, el, lab2

def change_name(layout, lel, table, orig, new = ""):
   if not new:
      num = 1
      while orig + str(num) in layout:
         num += 1
      new = orig + str(num)
   for e in lel:
      if "txfields" in lel[e] and orig in lel[e]["txfields"]:
         lel[e]["txfields"][lel[e]["txfields"].index(orig)] = new
      if "button" in lel[e] and lel[e]["button"] == orig:
         lel[e]["button"] = new
      if "ranav" in lel[e] and lel[e]["ranav"] == orig:
         lel[e]["ranav"] = new

   if lel:
      lel[new] = lel[orig]
      del lel[orig]

   for line in table:
      if orig in line:
         line[line.index(orig)] = new
         break

def process_layout(lines):
   """Generira ekranske komponente iz layouta. Vraca mapu svih ekranskih
   komponenti, listu button grupa i donju marginu layouta.

   lines - lista linija layouta."""

   lay = {}                # mapa ekranskih komponenti
   ypos = 20               # tekuca vertikalna pozicija komponente
   cnum = []               # broj layout komponenti u liniji layouta
   empty = []              # broj praznih komponenti u liniji (za poravnavanje)
   table = []              # dvodimenzionalna lista imena ekranskih komponenti
   ralign = 0              # desni rub najsireg reda layouta (za poravnavanje)
   groups = []             # button grupe za radio buttone
   gnum = []               # broj buttona u grupi
   
   for l in lines:
      coms = l.split("|")
      xpos = 0
      alab = 0
      cnum += [len(coms)]
      empty += [0]
      table += [[]]

      for com in coms:
         if com.strip():
            elems, el, lab2 = parse_elem(com, xpos, ypos)
            alab = alab or lab2
            table[-1] += el
            last = elems[el[-1]]["xpos"] + elems[el[-1]]["width"]
            xpos = last + (elems[el[-1]]["type"] == BUTTON and 9 or 5)
            last -= elems[el[-1]]["type"] == BUTTON and 26 or 0
            ralign = last > ralign and last or ralign
            
            for e in elems.copy():
               if e in lay:
                  if elems[e]["type"] == CHECK:
                     groups += [e[3:]]
                     gnum += [2]
                     elems[e]["type"] = RADIO
                     elems[e]["num"] = 2
                     elems[e]["bg"] = "bg%d" % (groups.index(groups[-1]) + 1)
                     elems["jrb" + e[3:] + "2"] = elems[e]
                     change_name({}, {}, table, e, "jrb" + e[3:] + "2")
                     del elems[e]
                     lay[e]["type"] = RADIO
                     lay[e]["num"] = 1
                     lay[e]["bg"] = "bg%d" % (groups.index(groups[-1]) + 1)
                     lay["jrb" + e[3:] + "1"] = lay[e]
                     change_name({}, {}, table, e, "jrb" + e[3:] + "1")
                     del lay[e]
                  else:
                     change_name(lay, elems, table, e)
                     
               elif e[3:] in groups:
                  idx = groups.index(e[3:])
                  gnum[idx] += 1
                  elems[e]["type"] = RADIO
                  elems[e]["num"] = gnum[idx]
                  elems[e]["bg"] = "bg%d" % (idx + 1)
                  elems["jrb%s%d" % (e[3:], gnum[idx])] = elems[e]
                  change_name({}, {}, table, e, "jrb%s%d" % (e[3:], gnum[idx]))
                  del elems[e]
                     
            lay.update(elems)
         else:
            table[-1] += "|"
            empty[-1] += 1
            xpos += 5

      ypos += alab and 45 or 25
      if alab:
         for e in table[-1]:
            if e != "|":
               lay[e]["ypos"] += lay[e]["type"] == UPLABEL and 3 or 20
      
   unfin = [index for index in xrange(len(cnum)) if empty[index] > 0]

   for lnum in unfin:
      le = lay[table[lnum][-1]]
      last = le["type"] == BUTTON and le["xpos"] - 5 or le["xpos"] + le["width"]
      if last < ralign:
         total = ralign - last
         curr = inc = 0
         for e in table[lnum]:
            if e == "|":
               inc += 1
               curr = total * inc // empty[lnum]
            else:
               lay[e]["xpos"] += curr
   
   return lay, ypos + 15


def process(lines, set):
   global unnamedt, unnamedc
   unnamedt = unnamedc = 0
   
   elem, bottom = process_layout(lines)

   txname = {}
   vars = []
   nvars = []
   ldef = []
   rdef = []
   ndef = []
   defs = []
   padds = []
   width = 0
   bgs = 0

   for e in elem:
      d = elem[e]
      ty, x, y, w = d["type"], d["xpos"], d["ypos"], d["width"]
      if ty in [RATEXT, RANAV]:
         txname[d["column"]] = e
         txname[e[3:]] = e
      if x + w > width:
         width = x + w
      if ty in [LABEL, UPLABEL] and d["text"].strip():
         vars += ["  JLabel %s = new JLabel();" % e]
         ldef += ['    %s.setText("%s");' % (e, convert_encoding(d["text"].strip()))]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]
         if ty == UPLABEL:
            align = d["text"].endswith(" ") and "LEADING" or \
                    d["text"].startswith(" ") and "TRAILING" or "CENTER"
            ldef += ["    %s.setHorizontalAlignment(SwingConstants.%s);" % (e, align)]
      elif ty == TEXT:
         txname[e[2:]] = e
         vars += ["  JTextField %s = new JTextField();" % e]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]
      elif ty == BUTTON:
         vars += ["  JraButton %s = new JraButton();" % e]
         ldef += ['    %s.setText("...");' % e]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, 21, 21));" % (e, x, y)]
      elif ty == CHECK:
         vars += ["  JraCheckBox %s = new JraCheckBox();" % e]
         todo = d["text"].startswith("CheckBox") and " /**@todo: Odrediti tekst checkboxa */" or ""
         if "column" in d:
            rdef += ['    %s.setText("%s");%s' % (e, convert_encoding(d["text"]), todo),
                     '    %s.setColumnName("%s");' % (e, d["column"]),
                     "    %s.setDataSet(%s);" % (e, set),
                     '    %s.setUnselectedDataValue("N"); /**@todo: Definirati vrijednosti checkboxa */' % e,
                     '    %s.setSelectedDataValue("D");' % e,
                     "    %s.setHorizontalTextPosition(SwingConstants.LEADING);" %e,
                     "    %s.setHorizontalAlignment(SwingConstants.RIGHT);" % e]
         else:
            ldef += ['    %s.setText("%s");%s' % (e, convert_encoding(d["text"]), todo),
                     "    %s.setHorizontalTextPosition(SwingConstants.LEADING);" % e,
                     "    %s.setHorizontalAlignment(SwingConstants.RIGHT);" % e]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]
      elif ty == RADIO:
         if d["num"] == 1:
            vars += ["  raButtonGroup %s = new raButtonGroup();" % d["bg"]]
            if "column" in d:
               ldef += ["    %s.setDataSet(%s);" % (d["bg"], set),
                        '    %s.setColumnName("%s");' % (d["bg"], d["column"])]
               
         vars += ["  JraRadioButton %s = new JraRadioButton();" % e]
         
         todo = d["text"].startswith("CheckBox") and " /**@todo: Odrediti tekst checkboxa */" or ""
         ldef += ['    %s.add(%s, "%s");%s' % (d["bg"], e, convert_encoding(d["text"]), todo)]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]
      elif ty == RATEXT:
         vars += ["  JraTextField %s = new JraTextField();" % e]
         rdef += ['    %s.setColumnName("%s");' % (e, strip_suffix(d["column"])),
                  "    %s.setDataSet(%s);" % (e, set)]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]
      elif ty == RANAV:
         nvars += ["  JlrNavField %s = new JlrNavField() {" % e,
                   "    public void after_lookUp() {",
                   "    }", "  };"]
         ndef += ["", '    %s.setColumnName("%s");' % (e, d["column"]),
                  "    %s.setDataSet(%s);" % (e, set)]
         
         if d["colnames"]:
            ndef += ['    %s.setColNames(new String[] {"%s"});' % (e, '", "'.join(d["colnames"])),
                     "    %s.setTextFields(new JTextComponent[] {%s});" % (e, ", ".join(d["txfields"]))]
            
         ndef += ["    %s.setVisCols(new int[] {0, 1}); /**@todo: Dodati visible cols za lookup frame */" % e,
                  "    %s.setSearchMode(0);" % e,
                  "    %s.setRaDataSet(dm.get%s());" % (e, d["dataset"]),
                  "    %s.setNavButton(%s);" % (e, d["button"])]

         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]

         for tx in d["txfields"]:
            ndef += ["", '    %s.setColumnName("%s");' % (tx, elem[tx]["column"]),
                     "    %s.setNavProperties(%s);" % (tx, e),
                     "    %s.setSearchMode(1);" % tx]
      elif ty == RANAV2:
         nvars += ["  JlrNavField %s = new JlrNavField() {" % e,
                   "    public void after_lookUp() {",
                   "    }", "  };"]
         padds += ["    jpDetail.add(%s, new XYConstraints(%d, %d, %d, -1));" % (e, x, y, w)]

   vars.sort()
   ldef.sort()
   rdef.sort()
   padds.sort()
   if vars or nvars:
      vars = ["  XYLayout lay = new XYLayout();"] + vars + nvars
      defs = ["    jpDetail.setLayout(lay);",
              "    lay.setWidth(%d);" % (width + 15),
              "    lay.setHeight(%d);" % bottom,
              ""] + ldef + rdef + ndef

   return elem, txname, vars, defs, padds

