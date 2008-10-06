import sys
import traceback

if len(sys.argv) <= 1:
   sys.exit(0)

try:
   f = open(sys.argv[1])
except (IOError, OSError):
   sys.exit(0)


podaci = 0
master_detail = 0
semicolons = 0

UNDEFINED = 0
TABLE = 1
MAT = 2
MD = 3

filetype = UNDEFINED
pause = 0

while 1:
   line = f.readline()
   if not line:
      break
   line = line.strip()
   if line.startswith("#"):
      type = line[1:].split(" ")[0].strip().lower()
      if type == "pause":
         pause = 1
      elif type in ["table", "tablica", "tab", "tables", "tablice", "tabs"]:
         filetype = TABLE
      elif type in ["matpodaci", "ramatpodaci", "mat"]:
         filetype = MAT
      elif type in ["masterdetail", "ramasterdetail", "md"]:
         filetype = MD

      if filetype:
         break

   for prefix in ["layout", "dataset", "visible", "check", "unique", "focus"]:
      if line.startswith(prefix):
         podaci = 1

   for prefix in ["master", "detail", "key"]:
      if line.startswith(prefix):
         master_detail = 1

   if line.count(";") >= 4:
      semicolons = 1
      break

f.close()

if semicolons:
   filetype = TABLE
else:
   if podaci:
      filetype = MAT
   if master_detail:
      filetype = MD

try:
   if filetype == TABLE:
      import parsetables
      parsetables.process(sys.argv[1])
   elif filetype == MAT:
      import emat
      emat.process(sys.argv[1])
   elif filetype == MD:
      import emd
      emd.process(sys.argv[1])
   else:
      print "Unknown file type:", sys.argv[1]
except Exception:
   print traceback.print_exc()

print
if pause:
   raw_input("Press Enter to exit.\n")
