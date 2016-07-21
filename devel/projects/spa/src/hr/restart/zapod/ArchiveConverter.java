package hr.restart.zapod;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.StorageDataSet;

import hr.restart.baza.Condition;
import hr.restart.baza.Kumulorgarh;
import hr.restart.baza.Odbiciarh;
import hr.restart.baza.Primanjaarh;
import hr.restart.baza.Radnici;
import hr.restart.util.Aus;


public class ArchiveConverter {

  public ArchiveConverter() {
    
  }
  
  public void convert(String knjig) {
    
    StorageDataSet outkum = Aus.createSet("GODOBR MJOBR RBROBR REDOVNI:1 CORG:12 KNJIG:12 SATI.2 " +
        "BRUTO.2 MIO1.2 MIO2.2 DOPZO.2 DOPZAP.2 DOPOZ.2 DOHODAK.2 NEOP.2 ISKNEOP.2 POROSN.2 " +
        "POR1.2 POR2.2 POR3.2 POR4.2 PORUK.2 PRIR.2 PORIPRIR.2 NETO.2 NAKNADE.2 OBUSTAVE.2 KREDITI.2 NARUKE.2 " +
        "SATIMJ.2 @DATUMOBR @DATUMISPL BROJDANA MINPL.2 MINOSDOP.2 MAXOSDOP.2 " +
        "OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 STOPAPOR1.2 STOPAPOR2.2 STOPAPOR3.2 STOPAPOR4.2");
    
    StorageDataSet outnak = Aus.createSet("CNAK NAZIV:50 IZNOS.2");
    
    StorageDataSet outkred = Aus.createSet("CBANKE CKREDIT CRADNIK:6 GLAVNICA.2 RATA.2 SALDO.2");
    
    StorageDataSet outob = Aus.createSet("COBUS NAZIV:50 IZNOS.2");
    
    StorageDataSet outprim = Aus.createSet("CVRP NAZIV:50 VRSTA:1 KOEF.2 NEOD:1 VRODN:1");
    
    StorageDataSet outprimarh = Aus.createSet("CRADNIK:6 CVRP GODOBR MJOBR RBROBR REDOVNI:1 BRUTO.2 KOEF.2 NETO.2 SATI.2 " +
    		"ODDANA DODANA BRUTO2.2 DOPZO.2 DOPOZ.2 DOPZAP.2 MIO1.2 MIO2.2 DOHODAK.2 OLAKSICA.2 OSNOVICA.2 " +
    		"POR1.2 POR2.2 POR3.2 POR4.2 OSNPOR1.2 OSNPOR2.2 OSNPOR3.2 OSNPOR4.2 POR.2 PRIREZ.2");
    
    StorageDataSet outnakarh = Aus.createSet("GODOBR MJOBR RBROBR CNAK CRADNIK:6 IZNOS.2");
    
    StorageDataSet outkredarh = Aus.createSet("GODOBR MJOBR RBROBR CBANKE CKREDIT CRADNIK:6 IZNOS.2");
    
    StorageDataSet outobarh = Aus.createSet("GODOBR MJOBR RBROBR COBUS CRADNIK:6 IZNOS.2");
   
    
    Condition corgs = OrgStr.getCorgsCond(knjig);
    
    DataSet kums = Kumulorgarh.getDataModule().openTempSet(corgs);
    
    DataSet zrad = Radnici.getDataModule().openTempSet(corgs);
    
    Condition crads = Condition.in("CRADNIK", zrad);
    
    DataSet prim = Primanjaarh.getDataModule().openTempSet(corgs);
    
    DataSet odb = Odbiciarh.getDataModule().openTempSet(crads);
    
    
    
    
    for (kums.first(); kums.inBounds(); kums.next()) {
      
    }
    
    
    
  }
  
}
