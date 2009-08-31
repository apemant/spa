package hr.restart.pl;

import java.util.HashMap;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRField;

public class repIPP implements JRDataSource {
  int pos = 0;
  HashMap row;
  public repIPP() {
    row = frmIPP.getFrmIPP().getDatarow();
    pos = 0;
  }
  public Object getFieldValue(JRField arg0) throws JRException {
    // TODO Auto-generated method stub
    return row.get(arg0.getName());
  }

  public boolean next() throws JRException {
    // TODO Auto-generated method stub
    pos++;
    return pos == 1;
  }


}
