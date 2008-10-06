<#>
gr = hr.restart.baza.Grupart.getDataModule().getTempSet();
gr.open();
ABSgr = orac.getData("pg-level1.sql","");
ABSgr.open();
String cg = "-$-";
String cg2 = "-$-";
for (ABSgr.first(); ABSgr.inBounds(); ABSgr.next()) {
//podgrupe
  if (!cg.equals(ABSgr.getString("GROUPCODE"))) {
    gr.insertRow(false);
    gr.setString("CGRART",ABSgr.getString("GROUPCODE"));
    gr.setString("CGRARTPRIP",ABSgr.getString("ACTIVITYCODE"));
    gr.setString("NAZGRART",ABSgr.getString("GROUPNAME"));
    gr.post();
    cg = ABSgr.getString("GROUPCODE");
  }
    if (!cg2.equals(ABSgr.getString("ACTIVITYCODE"))) {
      gr.insertRow(false);
      gr.setString("CGRART",ABSgr.getString("ACTIVITYCODE"));
      gr.setString("CGRARTPRIP",ABSgr.getString("ACTIVITYCODE"));
      gr.setString("NAZGRART",ABSgr.getString("ACTIVITYNAME"));
      gr.post();
      cg2 = ABSgr.getString("ACTIVITYCODE");
    }
}


gr;

