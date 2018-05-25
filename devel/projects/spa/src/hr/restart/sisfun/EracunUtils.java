/****license*****************************************************************
**   file: EracunUtils.java
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
package hr.restart.sisfun;

import hr.fina.eracun.erp.outgoinginvoicesdata.v3.OutgoingInvoicesDataType;
import hr.restart.baza.Condition;
import hr.restart.baza.Kosobe;
import hr.restart.baza.dM;
import hr.restart.baza.doki;
import hr.restart.baza.stdoki;
import hr.restart.pos.presBlag;
import hr.restart.robno.Util;
import hr.restart.robno.repUtil;
import hr.restart.sk.raSaldaKonti;
import hr.restart.util.Aus;
import hr.restart.util.ProcessInterruptException;
import hr.restart.util.VarStr;
import hr.restart.util.lookupData;
import hr.restart.zapod.OrgStr;
import hr.restart.zapod.Tecajevi;
import hr.restart.zapod.frmVirmani;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.URISyntaxException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeConstants;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import oasis.names.specification.ubl.schema.xsd.attacheddocument_2.AttachedDocumentType;
import oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.*;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.InstructionNoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.NoteType;
import oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.PaymentIDType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ExtensionContentType;
import oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.UBLExtensionType;
import oasis.names.specification.ubl.schema.xsd.invoice_2.InvoiceType;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import com.borland.dx.dataset.DataSet;
import com.borland.dx.dataset.SortDescriptor;
import com.borland.dx.dataset.StorageDataSet;


public class EracunUtils {
  
  private static final EracunUtils inst = new EracunUtils();
  
  private static final String CHECK_INBOX = "/apis/v2/queryInbox";
  
  private static final String CHECK_OUTBOX = "/apis/v2/queryOutbox";
  
  private static final String SEND_DOC = "/apis/v2/send";
  
  private static final String GET_DOC = "/apis/v2/receive";
  
  private static final String MARK = "/apis/v2/notifyimport/";
  
  private static final String CANCEL = "/apis/v2/documentAction";
  
  
  
  private String host;
  
  private int user;
  private String pass;
  private String unit;
  private String sid;
  private String coib;
  
  private String pdfview;
  
  private File dir;
  
  lookupData ld = lookupData.getlookupData();
  dM dm = dM.getDataModule();
  
  hr.fina.eracun.erp.outgoinginvoicesdata.v3.ObjectFactory oof = new hr.fina.eracun.erp.outgoinginvoicesdata.v3.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory iof = new oasis.names.specification.ubl.schema.xsd.invoice_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.attacheddocument_2.ObjectFactory dof = new oasis.names.specification.ubl.schema.xsd.attacheddocument_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ObjectFactory eof = new oasis.names.specification.ubl.schema.xsd.commonextensioncomponents_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ObjectFactory bof = new oasis.names.specification.ubl.schema.xsd.commonbasiccomponents_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ObjectFactory aof = new oasis.names.specification.ubl.schema.xsd.commonaggregatecomponents_2.ObjectFactory();
  /*oasis.names.specification.ubl.schema.xsd.commonsignaturecomponents_2.ObjectFactory sof = new oasis.names.specification.ubl.schema.xsd.commonsignaturecomponents_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.qualifieddatatypes_2.ObjectFactory qof = new oasis.names.specification.ubl.schema.xsd.qualifieddatatypes_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2.ObjectFactory uof = new oasis.names.specification.ubl.schema.xsd.unqualifieddatatypes_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.signatureaggregatecomponents_2.ObjectFactory saof = new oasis.names.specification.ubl.schema.xsd.signatureaggregatecomponents_2.ObjectFactory();
  oasis.names.specification.ubl.schema.xsd.signaturebasiccomponents_2.ObjectFactory sbof = new oasis.names.specification.ubl.schema.xsd.signaturebasiccomponents_2.ObjectFactory();*/
  
  DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
  
  
  DatatypeFactory dafact;
  
  public static EracunUtils getInstance() {
    return inst;
  }
  
  private EracunUtils() {
  }
  
  private void initParams() {
    user = Aus.getAnyNumber(frmParam.getParam("sisfun", "eracunUser", "", "User ID za eRacun"));
    pass = frmParam.getParam("sisfun", "eracunPass", "", "Lozinka za eRacun");
    
    unit = frmParam.getParam("sisfun", "eracunUnit", "", "Posl. jedinica za eRacun", true);
    sid = frmParam.getParam("sisfun", "eracunSid", "SPA-ERP", "Šifra softvera za eRacun");
    
    host = frmParam.getParam("sisfun", "eracunHost", "demo.moj-eracun.hr", "Hostname za eRacun");
    
    String inbox = frmParam.getParam("sisfun", "eracunInbox", "inbox", "Putanja za prihvat eRacuna");
    dir = new File(inbox).getAbsoluteFile();
    if (dir.exists() && !dir.isDirectory())
      throw new EracException("Pogrešna putanja za dolaznu poštu!");
    
    pdfview = frmParam.getParam("sisfun", "pdfViewer", "evince", "Naredba za pdf pregled");
  }
  
  public String createRacun(DataSet zag, File pdf, Runnable renderer) {
    if (zag == null) 
      throw new EracException("Nepostojeæi raèun!");
    
    DataSet org = presBlag.findOJ(zag);
    
    if (!ld.raLocate(dm.getLogotipovi(), "CORG", org))
        throw new EracException("Nedefiniran OIB tvrtke na logotipovima!");
        
    String oib = dm.getLogotipovi().getString("OIB");
    if (oib.length() == 0)
      throw new EracException("Nedefiniran OIB tvrtke na logotipovima!");
    
    String gln = dm.getLogotipovi().getString("GLN");
    //if (gln.length() == 0)
    //  throw new EracException("Nedefiniran GLN tvrtke na logotipovima!");
    
    String vrdok = zag.getString("VRDOK");
    if (vrdok.length() == 0 || "RAC|ROT|POD|ODB|TER|PRD".indexOf(vrdok) < 0)
      throw new EracException("Dokument nije raèun!");
    
    if (zag.isNull("CPAR") || zag.getInt("CPAR") == 0 || !ld.raLocate(dm.getPartneri(), "CPAR", zag))
      throw new EracException("Nedefinirani partner na dokumentu!");
    
    if (dm.getPartneri().getString("OIB").trim().length() == 0)
      throw new EracException("Nedefinirani OIB partnera na dokumentu!");
    
    if (zag.isNull("FBR") || zag.getInt("FBR") == 0)
      throw new EracException("Dokument nije fiskaliziran!");
    
    DataSet st = stdoki.getDataModule().openTempSet(Condition.whereAllEqual(Util.mkey, zag));
    if (st.rowCount() == 0)
      throw new EracException("Dokument nema stavaka!");
    
    /*String corg = zag.getString("CSKL");
    if (TypeDoc.getTypeDoc().isDocSklad(vrdok)) {
        if (!lookupData.getlookupData().raLocate(dm.getSklad(),new String[] {"CSKL"}, new String[] {zag.getString("CSKL")}))
          throw new EracException("Nepoznato skladište na dokumentu!");
        corg = dm.getSklad().getString("CORG");
    }
    
    if (!ld.raLocate(dm.getOrgstruktura(), "CORG", corg))
      throw new EracException("Nepoznata org. jedinica na dokumentu!");*/
    
    String mj = org.getString("MJESTO");
    
    String logodat = frmParam.getParam("zapod", "logoData", "", "Podaci s podnožja logotipa za eRacun (obavezno!)");
    if (logodat.length() == 0)
      throw new EracException("Nedefinirani podaci u podnožju logotipa!");
    
    try {
      dafact = DatatypeFactory.newInstance();
    } catch (DatatypeConfigurationException e) {
      throw new EracException("Greška kod inicijalizacije XML-a!");
    }
    
    String val = zag.getString("OZNVAL");
    if (raSaldaKonti.isDomVal(val)) val = Tecajevi.getDomOZNVAL();
    if (!ld.raLocate(dm.getValute(), "OZNVAL", val))
      throw new EracException("Nepoznata valuta na dokumentu!");
    
    String codeval = dm.getValute().getString("ISOCODE");
    
    coib = oib;
    initParams();
        
    // outgoing invoice envelope data
    OutgoingInvoicesDataType out = oof.createOutgoingInvoicesDataType();
    out.setHeader(oof.createOutgoingInvoicesDataTypeHeader());
    out.getHeader().setInvoiceType(BigInteger.valueOf(1));
    out.getHeader().setSupplierID(oib);
    out.setOutgoingInvoice(oof.createOutgoingInvoicesDataTypeOutgoingInvoice());
    out.getOutgoingInvoice().setSupplierInvoiceID(zag.getString("PNBZ2"));
    out.getOutgoingInvoice().setBuyerID(dm.getPartneri().getString("OIB"));
    
    out.getOutgoingInvoice().setInvoiceEnvelope(oof.createOutgoingInvoicesDataTypeOutgoingInvoiceInvoiceEnvelope());

    // ubl 2.1 invoice node
    InvoiceType ivo = iof.createInvoiceType();
    
    // ekstenzije
    fillExtensions(ivo, mj, logodat);
    
    // glavni podaci
    ivo.setUBLVersionID(bof.createUBLVersionIDType());
    ivo.getUBLVersionID().setValue("2.1");
    ivo.setCustomizationID(bof.createCustomizationIDType());
    ivo.getCustomizationID().setValue("urn:invoice.hr:ubl-2.1-customizations:FinaInvoice");
    ivo.setProfileID(bof.createProfileIDType());
    ivo.getProfileID().setValue("MojEracunInvoice");
    ivo.setID(bof.createIDType());
    ivo.getID().setValue(zag.getString("PNBZ2"));
    ivo.setCopyIndicator(bof.createCopyIndicatorType());
    ivo.getCopyIndicator().setValue(false);
    ivo.setIssueDate(bof.createIssueDateType());
    ivo.getIssueDate().setValue(createDatum(zag.getTimestamp("DATDOK")));
    ivo.setIssueTime(bof.createIssueTimeType());
    ivo.getIssueTime().setValue(createTimestamp(zag.getTimestamp("DATDOK")));
    
    ivo.setInvoiceTypeCode(bof.createInvoiceTypeCodeType());
    ivo.getInvoiceTypeCode().setListID("UN/ECE 1001");
    ivo.getInvoiceTypeCode().setListAgencyID("6");
    ivo.getInvoiceTypeCode().setListURI("http://www.unece.org/trade/untdid/d00a/tred/tred1001.htm");
    ivo.getInvoiceTypeCode().setValue("380");
    
    ivo.setDocumentCurrencyCode(bof.createDocumentCurrencyCodeType());
    ivo.getDocumentCurrencyCode().setListID("ISO 4217 Alpha");
    ivo.getDocumentCurrencyCode().setListAgencyID("5");
    ivo.getDocumentCurrencyCode().setListURI("http://docs.oasis-open.org/ubl/os-UBL-2.1/cl/gc/default/CurrencyCode-2.1.gc");
    ivo.getDocumentCurrencyCode().setValue(codeval);
    
    // opcionalna narudžba
    if (!zag.isNull("BRNARIZ") && !zag.isNull("DATNARIZ")) {
      ivo.setOrderReference(aof.createOrderReferenceType());
      ivo.getOrderReference().setID(bof.createIDType());
      ivo.getOrderReference().getID().setValue(zag.getString("BRNARIZ"));
      ivo.getOrderReference().setIssueDate(bof.createIssueDateType());
      ivo.getOrderReference().getIssueDate().setValue(createDatum(zag.getTimestamp("DATNARIZ")));
    }
    
    // dobavljaè i kupac
    ivo.setAccountingSupplierParty(aof.createSupplierPartyType());
    ivo.getAccountingSupplierParty().setParty(createParty("tvrtke", 
        org.getString("NAZIV"), oib, gln, org.getString("ADRESA"), org.getString("MJESTO"), org.getString("HPBROJ")));
    ivo.getAccountingSupplierParty().setAccountingContact(createMyContact(zag.getString("CUSER")));
    
    ivo.setAccountingCustomerParty(aof.createCustomerPartyType());
    ivo.getAccountingCustomerParty().setParty(createParty("partnera",
        dm.getPartneri().getString("NAZPAR"), dm.getPartneri().getString("OIB"), dm.getPartneri().getString("GLN"),
        dm.getPartneri().getString("ADR"), dm.getPartneri().getString("MJ"), dm.getPartneri().getInt("PBR") + ""));
    ivo.getAccountingCustomerParty().setAccountingContact(createContact(zag));
    
    // isporuka
    DeliveryType isp = aof.createDeliveryType();
    isp.setActualDeliveryDate(bof.createActualDeliveryDateType());
    isp.getActualDeliveryDate().setValue(createDatum(zag.getTimestamp("DVO")));
    isp.setActualDeliveryTime(bof.createActualDeliveryTimeType());
    isp.getActualDeliveryTime().setValue(createTimestamp(zag.getTimestamp("DVO")));
    ivo.getDelivery().add(isp);
    
    // podaci o vrsti plaæanja
    int cnacpl = 0;
    if (ld.raLocate(dm.getNacpl(), "CNACPL", zag))
      cnacpl = dm.getNacpl().getInt("UBLCODE");
    
    if (cnacpl == 0)
      throw new EracException("Nedefiniran kod naèina plaæanja raèuna!");
    
    String ziro = zag.getString("ZIRO");
    if (ziro == null || ziro.length() == 0)
      ziro = org.getString("ZIRO");
    
    if (ziro == null || ziro.length() == 0)
      throw new EracException("Nedefiniran žiro raèun za plaæanje!");
    
    String iban = frmVirmani.checkIBAN_HR(frmVirmani.getIBAN_HR(ziro, false), false);
    if (iban == null || iban.length() == 0 || iban.equals(frmVirmani.BAD_IBAN))
      throw new EracException("Pogrešan IBAN tvrtke!");
    
    PaymentMeansType pmt = aof.createPaymentMeansType();
    pmt.setPaymentMeansCode(bof.createPaymentMeansCodeType());
    pmt.getPaymentMeansCode().setListID("UN/ECE 4461");
    pmt.getPaymentMeansCode().setListAgencyID("6");
    pmt.getPaymentMeansCode().setListName("PaymentMeansCode");
    pmt.getPaymentMeansCode().setListVersionID("D10B");
    pmt.getPaymentMeansCode().setListURI("http://docs.oasis-open.org/ubl/os-UBL-2.1/cl/gc/default/PaymentMeansCode-2.1.gc");
    pmt.getPaymentMeansCode().setListSchemeURI("urn:un:unece:uncefact:codelist:standard:UNECE:PaymentMeansCode:D10B");
    pmt.getPaymentMeansCode().setValue(Integer.toString(cnacpl));
    pmt.setPaymentDueDate(bof.createPaymentDueDateType());
    pmt.getPaymentDueDate().setValue(createDatum(zag.getTimestamp("DATDOSP")));
    pmt.setPaymentChannelCode(bof.createPaymentChannelCodeType());
    pmt.getPaymentChannelCode().setListAgencyName("CEN/BII");
    pmt.getPaymentChannelCode().setValue("IBAN");
    pmt.setInstructionID(bof.createInstructionIDType());
    pmt.getInstructionID().setValue(zag.getString("PNBZ2"));
    InstructionNoteType note = bof.createInstructionNoteType();
    note.setValue("Plaæanje po raèunu");
    pmt.getInstructionNote().add(note);
    PaymentIDType pid = bof.createPaymentIDType();
    pid.setValue("HR99");
    pmt.getPaymentID().add(pid);
    pmt.setPayeeFinancialAccount(aof.createFinancialAccountType());
    pmt.getPayeeFinancialAccount().setCurrencyCode(bof.createCurrencyCodeType());
    pmt.getPayeeFinancialAccount().getCurrencyCode().setListID("ISO 4217 Alpha");
    pmt.getPayeeFinancialAccount().getCurrencyCode().setListAgencyID("5");
    pmt.getPayeeFinancialAccount().getCurrencyCode().setListURI("http://docs.oasis-open.org/ubl/os-UBL-2.1/cl/gc/default/CurrencyCode-2.1.gc");
    pmt.getPayeeFinancialAccount().getCurrencyCode().setValue(codeval);
    pmt.getPayeeFinancialAccount().setID(bof.createIDType());
    pmt.getPayeeFinancialAccount().getID().setValue(iban);
    ivo.getPaymentMeans().add(pmt);
    
    // podaci o ukupnom iznosu raèuna
    String paynote = frmParam.getParam("sisfun", "eracunTerms", "Uvjeti plaæanja...", "Uvjeti plaæanja za eRaèune");
    Condition predCond = repUtil.getCondFromBroj(zag.getString("BRPRD"));
    String pred = null;
    BigDecimal prepaid = BigDecimal.ZERO;
    if (predCond != null) {
      DataSet prd = doki.getDataModule().openTempSet(predCond);
      if (prd.rowCount() == 1) {
        pred = prd.getString("PBNZ2");
        prepaid = Aus.sum("IPRODSP", stdoki.getDataModule().openTempSet(predCond));
      }
    }
    
    BigDecimal ineto = Aus.sum("INETO", st);
    BigDecimal iprodbp = Aus.sum("IPRODBP", st);
    BigDecimal iprodsp = Aus.sum("IPRODSP", st);
    if (prepaid.compareTo(iprodsp) > 0 && iprodsp.signum() > 0)
      prepaid = iprodsp;
    
    PaymentTermsType ptt = aof.createPaymentTermsType();
    if (pred != null && pred.length() > 0) {
      ptt.setPrepaidPaymentReferenceID(bof.createPrepaidPaymentReferenceIDType());
      ptt.getPrepaidPaymentReferenceID().setValue(pred);
    }
    NoteType tnote = bof.createNoteType();
    tnote.setValue(paynote);
    ptt.getNote().add(tnote);
    ptt.setAmount(bof.createAmountType());
    ptt.getAmount().setCurrencyID(codeval);
    ptt.getAmount().setValue(iprodsp);
    ivo.getPaymentTerms().add(ptt);
    
    // podaci za porez
    ivo.getTaxTotal().add(createTax(zag, st, codeval));
    
    // ukupni iznosi na raèunu
    ivo.setLegalMonetaryTotal(aof.createMonetaryTotalType());
    ivo.getLegalMonetaryTotal().setLineExtensionAmount(bof.createLineExtensionAmountType());
    ivo.getLegalMonetaryTotal().getLineExtensionAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getLineExtensionAmount().setValue(iprodbp);
    ivo.getLegalMonetaryTotal().setTaxExclusiveAmount(bof.createTaxExclusiveAmountType());
    ivo.getLegalMonetaryTotal().getTaxExclusiveAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getTaxExclusiveAmount().setValue(iprodbp);
    ivo.getLegalMonetaryTotal().setTaxInclusiveAmount(bof.createTaxInclusiveAmountType());
    ivo.getLegalMonetaryTotal().getTaxInclusiveAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getTaxInclusiveAmount().setValue(iprodsp);
    ivo.getLegalMonetaryTotal().setAllowanceTotalAmount(bof.createAllowanceTotalAmountType());
    ivo.getLegalMonetaryTotal().getAllowanceTotalAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getAllowanceTotalAmount().setValue(iprodbp.subtract(ineto));
    ivo.getLegalMonetaryTotal().setPrepaidAmount(bof.createPrepaidAmountType());
    ivo.getLegalMonetaryTotal().getPrepaidAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getPrepaidAmount().setValue(prepaid);
    ivo.getLegalMonetaryTotal().setPayableAmount(bof.createPayableAmountType());
    ivo.getLegalMonetaryTotal().getPayableAmount().setCurrencyID(codeval);
    ivo.getLegalMonetaryTotal().getPayableAmount().setValue(iprodsp.subtract(prepaid));
    
    // podaci za svaku stavku posebno
    for (st.first(); st.inBounds(); st.next())
      ivo.getInvoiceLine().add(createLine(st, codeval));
    
    out.getOutgoingInvoice().getInvoiceEnvelope().setInvoice(ivo);
    
    // attachment pdf
    
    out.getOutgoingInvoice().setAttachedDocumentEnvelope(oof.createOutgoingInvoicesDataTypeOutgoingInvoiceAttachedDocumentEnvelope());
    
    AttachedDocumentType att = dof.createAttachedDocumentType();
    att.setID(bof.createIDType());
    att.getID().setValue(zag.getString("PNBZ2"));
    att.setIssueDate(bof.createIssueDateType());
    att.getIssueDate().setValue(createDatum(zag.getTimestamp("DATDOK")));
    att.setIssueTime(bof.createIssueTimeType());
    att.getIssueTime().setValue(createTimestamp(zag.getTimestamp("DATDOK")));
    att.setParentDocumentID(bof.createParentDocumentIDType());
    att.getParentDocumentID().setValue(zag.getString("PNBZ2"));
    
    att.setSenderParty(createPartyLite(org.getString("NAZIV"), oib,
        org.getString("ADRESA") + ", " + org.getString("HPBROJ") + " " + org.getString("MJESTO")));
    att.setReceiverParty(createPartyLite(dm.getPartneri().getString("NAZPAR"), dm.getPartneri().getString("OIB"),
        dm.getPartneri().getString("ADR") + ", " + dm.getPartneri().getInt("PBR") + " " + dm.getPartneri().getString("MJ")));
    
    att.setAttachment(aof.createAttachmentType());
    att.getAttachment().setEmbeddedDocumentBinaryObject(bof.createEmbeddedDocumentBinaryObjectType());
    att.getAttachment().getEmbeddedDocumentBinaryObject().setFilename(pdf.getName());
    att.getAttachment().getEmbeddedDocumentBinaryObject().setMimeCode("application/pdf");
    att.getAttachment().getEmbeddedDocumentBinaryObject().setEncodingCode("base64");
    
    try {
      renderer.run();
    } catch (ProcessInterruptException e) {
      throw e;
    } catch (Exception e) {
      throw new EracException("Greška kod formiranja pdf datoteke za prilog!");
    }
    
    try {
      att.getAttachment().getEmbeddedDocumentBinaryObject().setValue(FileUtils.readFileToByteArray(pdf));
    } catch (IOException e) {
      e.printStackTrace();
      throw new EracException("Greška kod uèitavanja pdf datoteke za prilog!");
    }
    
    out.getOutgoingInvoice().getAttachedDocumentEnvelope().getAttachedDocument().add(att);
    
    try {
      StringWriter sw = new StringWriter();
      //JAXBContext context = JAXBContext.newInstance(new Class[] {OutgoingInvoicesDataType.class, InvoiceType.class, AttachedDocumentType.class});
      JAXBContext context = JAXBContext.newInstance(new Class[] {OutgoingInvoicesDataType.class});
      Marshaller m = context.createMarshaller();
      //m.setAdapter(adapter)
      m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
      JAXBElement root = oof.createOutgoingInvoicesData(out);
      HashMap ns = new HashMap();
      grabNamespaces(context, root, ns);
      outputData(context, root, sw, ns);
      return sw.toString();
    } catch (JAXBException e) {
      e.printStackTrace();
      throw new EracException("Greška kod formiranja XML datoteke za dokument!");
    }

  }
  
  private void outputData(JAXBContext context, JAXBElement root, final Writer w, final HashMap ns) throws JAXBException {
    final String xsi = "http://www.w3.org/2001/XMLSchema-instance";
    final String xsd = "http://www.w3.org/2001/XMLSchema";
    
    Marshaller m = context.createMarshaller();
    m.marshal(root, new ContentAdapter() {
      List scopeStack = new ArrayList();      
      int indent = 0;
      boolean endTag = false;
      private String currNs() {
        return (String) scopeStack.get(scopeStack.size() - 1);
      }
      public void startDocument() throws SAXException {
        try {
          w.write("<?xml version=\"1.0\" encoding=\"utf-8\"?>");
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        try {
          endTag = false;
          w.write('\n');
          
          String full = uri + ":" + localName;
          if (full.equals(rootElem) || rootSchemas.containsKey(full)) {
            scopeStack.add(uri);
          }
          String qname = uri.equals(currNs()) ? localName : ((HashMap) ns.get(currNs())).get(uri) + ":" + localName;
          if (indent > 0) w.write(Aus.spc(indent));
          w.write('<');
          w.write(qname);
          if (full.equals(rootElem)) {
            w.write(" xmlns:xsi=");
            w.write('"');
            w.write(xsi);
            w.write('"');
            w.write(" xmlns:xsd=");
            w.write('"');
            w.write(xsd);
            w.write('"');
            w.write(" xmlns=");
            w.write('"');
            w.write(uri);
            w.write('"');
            outputNamespaces((HashMap) ns.get(currNs()));
          } else if (rootSchemas.containsKey(full)) {
            outputNamespaces((HashMap) ns.get(currNs()));
            w.write(" xsi:schemaLocation=");
            w.write('"');
            w.write((String) rootSchemas.get(full));
            w.write('"');
            w.write(" xmlns=");
            w.write('"');
            w.write(uri);
            w.write('"');
          }
          if (atts != null && atts.getLength() > 0)
            for (int i = 0; i < atts.getLength(); i++) {
              w.write(' ');
              if (atts.getURI(i) != null && atts.getURI(i).length() > 0 && !atts.getURI(i).equals(uri)) {
                w.write((String) ((HashMap) ns.get(currNs())).get(atts.getURI(i)));
                w.write(':');
              }
              w.write(atts.getLocalName(i));
              w.write('=');
              w.write('"');
              w.write(atts.getValue(i));
              w.write('"');
            }
          w.write('>');
          indent += 2;
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
      private void outputNamespaces(HashMap nsl) throws IOException {
        if (nsl == null || nsl.size() == 0) return;
        
        for (Iterator i = nsl.keySet().iterator(); i.hasNext(); ) {
          String key = (String) i.next();
          w.write(" xmlns:");
          w.write((String) nsl.get(key));
          w.write('=');
          w.write('"');
          w.write(key);
          w.write('"');
        }
      }
      public void endElement(String uri, String localName, String qName) throws SAXException {
        try {
          if (endTag) w.write('\n');
          if ((indent -= 2) > 0 && endTag) w.write(Aus.spc(indent));
          endTag = true;
          
          String qname = uri.equals(currNs()) ? localName : ((HashMap) ns.get(currNs())).get(uri) + ":" + localName;
          w.write("</");
          w.write(qname);
          w.write('>');
          
          String full = uri + ":" + localName;
          if (full.equals(rootElem) || rootSchemas.containsKey(full)) {
            scopeStack.remove(scopeStack.size() - 1);
          }
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
      public void characters(char[] ch, int start, int length) throws SAXException {
        try {
          endTag = false;
          w.write(ch, start, length);
        } catch (IOException e) {
          throw new SAXException(e);
        }
      }
    });
  }
  
  private void grabNamespaces(JAXBContext context, JAXBElement root, final HashMap ns) throws JAXBException {    
    Marshaller m = context.createMarshaller();
    m.marshal(root, new ContentAdapter() {
      List scopeStack = new ArrayList();
      /**
       * @throws SAXException  
       */
      public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        String full = uri + ":" + localName;
        if (full.equals(rootElem) || rootSchemas.containsKey(full)) {
          scopeStack.add(uri);
        }
        
        if (uri != null) addNs(uri);
        for (int i = 0; i < atts.getLength(); i++)
          if (atts.getURI(i) != null) addNs(uri);
      }
      
      void addNs(String uri) throws SAXException {
        String def = (String) scopeStack.get(scopeStack.size() - 1);
        if (uri.equals(def)) return;
         
        HashMap nsl = (HashMap) ns.get(def);
        if (nsl == null) ns.put(scopeStack.get(scopeStack.size() - 1), nsl = new HashMap());
        String pref = (String) prefs.get(uri);
        if (pref == null) throw new SAXException("Invalid namespace: " + uri);
        nsl.put(uri, pref);
      }
      
      public void endElement(String uri, String localName, String qName)
          throws SAXException {
        String full = uri + ":" + localName;
        if (full.equals(rootElem) || rootSchemas.containsKey(full)) {
          scopeStack.remove(scopeStack.size() - 1);
        }
      }
    });
  }
  
  
  
  private void fillExtensions(InvoiceType ivo, String mj, String logodat) {
    ivo.setUBLExtensions(eof.createUBLExtensionsType());
    
    ExtensionContentType place = eof.createExtensionContentType();
    place.setInvoiceIssuePlace(eof.createInvoiceIssuePlaceType());
    place.getInvoiceIssuePlace().setValue(mj);
    ivo.getUBLExtensions().getUBLExtension().add(
        getExtension("InvoiceIssuePlaceData", "MINGORP", "Mjesto izdavanja raèuna prema Pravilniku o PDV-u", place));
    
    ExtensionContentType issuer = eof.createExtensionContentType();
    issuer.setInvoiceIssuer(eof.createInvoiceIssuerType());
    issuer.getInvoiceIssuer().setValue(logodat);
    ivo.getUBLExtensions().getUBLExtension().add(
        getExtension("InvoiceIssuerData", "FINA", "Podaci o izdavatelju prema Zakonu o trgovaèkim društvima", issuer));
    
    ExtensionContentType logo = eof.createExtensionContentType();
    logo.setIssuerLogo(eof.createIssuerLogoType());
    ivo.getUBLExtensions().getUBLExtension().add(
        getExtension("IssuerLogoData", "FINA", "BASE64 logotipa tvrtke", logo));
  }
  
  private InvoiceLineType createLine(DataSet st, String codeval) {
    InvoiceLineType l = aof.createInvoiceLineType();
    l.setID(bof.createIDType());
    l.getID().setValue(Integer.toString(st.getShort("RBR")));
    
    if (!ld.raLocate(dm.getJedmj(), "JM", st))
      throw new EracException("Nedefinirana jedinica mjere na artiklu " + st.getInt("CART") + "!");
    
    String jm = dm.getJedmj().getString("ECECODE");
    if (jm == null || jm.length() == 0)
      throw new EracException("Nedefiniran UN/ECE kol za jedinica mjere " + st.getString("JM") + "!");
    
    String ean = st.getString("BC");
    if (ean == null || ean.length() == 0)
      throw new EracException("Nedefiniran barkod za artikl " + st.getInt("CART") + "!");
    
    if (!ld.raLocate(dm.getArtikli(), "CART", st))
      throw new EracException("Nedefiniran artikl " + st.getInt("CART") + "!");
    
    BigDecimal osn = st.getBigDecimal("IPRODBP");
    BigDecimal por = st.getBigDecimal("IPRODSP").subtract(osn);
    
    l.setInvoicedQuantity(bof.createInvoicedQuantityType());
    l.getInvoicedQuantity().setUnitCode(jm);
    l.getInvoicedQuantity().setUnitCodeListID("UN/ECE rec 20 8e");
    l.getInvoicedQuantity().setUnitCodeListAgencyID("6");
    l.getInvoicedQuantity().setValue(st.getBigDecimal("KOL"));
    
    l.setLineExtensionAmount(bof.createLineExtensionAmountType());
    l.getLineExtensionAmount().setValue(osn);
    l.getLineExtensionAmount().setCurrencyID(codeval);
    
    l.setItem(aof.createItemType());
    l.getItem().setName(bof.createNameType());
    l.getItem().getName().setValue(st.getString("NAZART"));
    ItemIdentificationType code = aof.createItemIdentificationType();
    code.setID(bof.createIDType());
    code.getID().setSchemeID("GTIN");
    code.getID().setValue(ean);
    l.getItem().getAdditionalItemIdentification().add(code);
    
    l.setPrice(aof.createPriceType());
    l.getPrice().setBaseQuantity(bof.createBaseQuantityType());
    l.getPrice().getBaseQuantity().setUnitCode(jm);
    l.getPrice().getBaseQuantity().setValue(Aus.one0);
    l.getPrice().setPriceAmount(bof.createPriceAmountType());
    l.getPrice().getPriceAmount().setValue(st.getBigDecimal("FVC"));
    l.getPrice().getPriceAmount().setCurrencyID(codeval);
    
    if (st.getBigDecimal("FC").compareTo(st.getBigDecimal("FVC")) != 0 && st.getBigDecimal("INETO").signum() != 0) {
      AllowanceChargeType pop = aof.createAllowanceChargeType();
      pop.setChargeIndicator(bof.createChargeIndicatorType());
      pop.getChargeIndicator().setValue(false);
      pop.setBaseAmount(bof.createBaseAmountType());
      pop.getBaseAmount().setValue(st.getBigDecimal("INETO"));
      pop.getBaseAmount().setCurrencyID(codeval);
      pop.setAmount(bof.createAmountType());
      pop.getAmount().setValue(st.getBigDecimal("INETO").subtract(osn));
      pop.getAmount().setCurrencyID(codeval);
      pop.setMultiplierFactorNumeric(bof.createMultiplierFactorNumericType());
      pop.getMultiplierFactorNumeric().setValue(st.getBigDecimal("INETO").subtract(osn).divide(st.getBigDecimal("INETO"), 2, BigDecimal.ROUND_HALF_UP));
      l.getAllowanceCharge().add(pop);
    }
    
    TaxTotalType tax = aof.createTaxTotalType();
    tax.setTaxAmount(bof.createTaxAmountType());
    tax.getTaxAmount().setValue(por);
    tax.getTaxAmount().setCurrencyID(codeval);
    
    tax.getTaxSubtotal().add(createTax(dm.getArtikli().getString("CPOR"), codeval, osn, por));
    
    l.getTaxTotal().add(tax);
    
    return l;
  }
    
  private TaxTotalType createTax(DataSet zag, DataSet st, String codeval) {
    TaxTotalType ret = aof.createTaxTotalType();
    
    BigDecimal tot = BigDecimal.ZERO;
    HashMap totals = new HashMap();
    for (st.first(); st.inBounds(); st.next()) {
      if (!ld.raLocate(dm.getArtikli(), "CART", st))
        throw new EracException("Nepoznat artikl " + st.getInt("CART") + " na dokumentu!");
      String cpor = dm.getArtikli().getString("CPOR");
      BigDecimal[] sums = (BigDecimal[]) totals.get(cpor);
      if (sums == null) totals.put(cpor, sums = new BigDecimal[] {BigDecimal.ZERO, BigDecimal.ZERO});
      sums[0] = sums[0].add(st.getBigDecimal("IPRODBP"));
      sums[1] = sums[1].add(st.getBigDecimal("IPRODSP")).subtract(st.getBigDecimal("IPRODBP"));
      tot = tot.add(st.getBigDecimal("IPRODSP")).subtract(st.getBigDecimal("IPRODBP"));
    }
    
    ret.setTaxAmount(bof.createTaxAmountType());
    ret.getTaxAmount().setValue(tot);
    ret.getTaxAmount().setCurrencyID(codeval);
    for (Iterator i = totals.keySet().iterator(); i.hasNext(); ) {
      String cpor = (String) i.next();
      BigDecimal[] sums = (BigDecimal[]) totals.get(cpor);
      ret.getTaxSubtotal().add(createTax(cpor, codeval, sums[0], sums[1]));
    }
    
    return ret;
  }
  
  private TaxSubtotalType createTax(String cpor, String codeval, BigDecimal osn, BigDecimal izn) {
    TaxSubtotalType ret = aof.createTaxSubtotalType();
    
    ret.setTaxableAmount(bof.createTaxableAmountType());
    ret.getTaxableAmount().setValue(osn);
    ret.getTaxableAmount().setCurrencyID(codeval);
    
    ret.setTaxAmount(bof.createTaxAmountType());
    ret.getTaxAmount().setValue(izn);
    ret.getTaxAmount().setCurrencyID(codeval);
        
    if (!ld.raLocate(dm.getPorezi(), "CPOR", cpor))
      throw new EracException("Nedefinirana vrsta poreza!");
    
    String neop = null;
    if (dm.getPorezi().getString("CNAP").length() > 0 && 
        ld.raLocate(dm.getNapomene(), "CNAP", dm.getPorezi()))
        neop = dm.getNapomene().getString("NAZNAP");
    
    String name = dm.getPorezi().getString("UBLNAME");
    if (name == null || name.length() == 0)
      throw new EracException("Nedefinirano UBL ime poreza!");
    
    String code = dm.getPorezi().getString("UBLCODE");
    if (code == null || code.length() == 0)
      throw new EracException("Nedefinirana UBL šifra poreza!");
    
    String scname = dm.getPorezi().getString("UBLSCN");
    if (scname == null || scname.length() == 0)
      throw new EracException("Nedefinirano UBL ime sheme poreza!");
    
    String sccode = dm.getPorezi().getString("UBLSCC");
    if (sccode == null || sccode.length() == 0)
      throw new EracException("Nedefinirana UBL šifra sheme poreza!");
    
    ret.setTaxCategory(aof.createTaxCategoryType());
    ret.getTaxCategory().setID(bof.createIDType());
    ret.getTaxCategory().getID().setSchemeID("UN/ECE 5305");
    ret.getTaxCategory().getID().setSchemeAgencyID("6");
    ret.getTaxCategory().getID().setSchemeURI("http://www.unece.org/trade/untdid/d07a/tred/tred5305.htm");
    ret.getTaxCategory().getID().setValue(code);
    ret.getTaxCategory().setName(bof.createNameType());
    ret.getTaxCategory().getName().setValue(name);
    ret.getTaxCategory().setPercent(bof.createPercentType());
    ret.getTaxCategory().getPercent().setValue(dm.getPorezi().getBigDecimal("UKUPOR"));
    if (neop != null && neop.length() > 0) {
      ret.getTaxCategory().setTaxExemptionReason(bof.createTaxExemptionReasonType());
      ret.getTaxCategory().getTaxExemptionReason().setValue(neop);
    }
    ret.getTaxCategory().setTaxScheme(aof.createTaxSchemeType());
    ret.getTaxCategory().getTaxScheme().setName(bof.createNameType());
    ret.getTaxCategory().getTaxScheme().getName().setValue(scname);
    ret.getTaxCategory().getTaxScheme().setTaxTypeCode(bof.createTaxTypeCodeType());
    ret.getTaxCategory().getTaxScheme().getTaxTypeCode().setValue(sccode);
    
    return ret;
  }
  
  private ContactType createMyContact(String cuser) {
    
    String email = frmParam.getParam("sisfun", "eracunMail", "", "Email s kojeg se šalju eRacuni", true);
    
    if (email == null || email.length() == 0) 
      email = dm.getLogotipovi().getString("EMAIL");

    if (email == null || email.length() == 0)
      throw new EracException("Nedefinirana odlazna e-mail adresa!");
    
    String osoba = frmParam.getParam("sisfun", "eracunOsoba", "", "Osoba koja šalje eRacune", true);
    if (osoba == null || osoba.length() == 0 && ld.raLocate(dm.getUseri(), "CUSER", cuser))
      osoba = dm.getUseri().getString("NAZIV");
    
    if (osoba == null || osoba.length() == 0)
      throw new EracException("Nedefinirana osoba za odlazni e-mail!");
    
    ContactType ret = aof.createContactType();
    ret.setElectronicMail(bof.createElectronicMailType());
    ret.getElectronicMail().setValue(email);
    ret.setName(bof.createNameType());
    ret.getName().setValue(osoba);
    NoteType note = bof.createNoteType();
    note.setValue(cuser);
    ret.getNote().add(note);
    
    return ret;
  }
  
  private ContactType createContact(DataSet zag) {
    String email = dm.getPartneri().getString("EMADR");
    DataSet ko = Kosobe.getDataModule().openTempSet(Condition.whereAllEqual("CPAR CKO", zag));
    if (ko.rowCount() == 1 && !ko.isNull("EMAIL") && ko.getString("EMAIL").trim().length() > 0)
      email = ko.getString("EMAIL");
    
    if (email == null || email.length() == 0)
      throw new EracException("Nedefinirana e-mail adresa partnera!");
        
    ContactType ret = aof.createContactType();
    ret.setElectronicMail(bof.createElectronicMailType());
    ret.getElectronicMail().setValue(email);
    
    return ret;
  }
  
  private PartyType createPartyLite(String naziv, String oib, String adr) {
    PartyType p = aof.createPartyType();
    
    PartyNameType name = aof.createPartyNameType();
    name.setName(bof.createNameType());
    name.getName().setValue(naziv);
    p.getPartyName().add(name);

    p.setPartyLegalEntity(aof.createPartyLegalEntityType());
    p.getPartyLegalEntity().setCompanyID(bof.createCompanyIDType());
    p.getPartyLegalEntity().getCompanyID().setValue(oib);
    p.getPartyLegalEntity().setRegistrationName(bof.createRegistrationNameType());
    p.getPartyLegalEntity().getRegistrationName().setValue(naziv);
    
    p.setPostalAddress(aof.createAddressType());
    
    AddressLineType adrLine = aof.createAddressLineType();
    adrLine.setLine(bof.createLineType());
    adrLine.getLine().setValue(adr);
    p.getPostalAddress().getAddressLine().add(adrLine);
    
    CountryType ctr = aof.createCountryType();
    ctr.setIdentificationCode(bof.createIdentificationCodeType());
    ctr.getIdentificationCode().setListID("ISO3166-1");
    ctr.getIdentificationCode().setListAgencyID("6");
    ctr.getIdentificationCode().setListName("Country");
    ctr.getIdentificationCode().setListVersionID("0.3");
    ctr.getIdentificationCode().setListURI("http://docs.oasis-open.org/ubl/os-ubl-2.0/cl/gc/default/CountryIdentificationCode-2.0.gc");
    ctr.getIdentificationCode().setListSchemeURI("urn:oasis:names:specification:ubl:codelist:gc:CountryIdentificationCode-2.0");
    ctr.getIdentificationCode().setValue("HR");
    p.getPostalAddress().setCountry(ctr);
    
    return p;
  }
  
  private PartyType createParty(String koga, String naziv, String oib, String gln, String adr, String mj, String pbr) {
    if (oib == null || oib.length() == 0)
      throw new EracException("Nedefiniran OIB " + koga +"!");
    
    //if (gln == null || gln.length() == 0)
    //  throw new EracException("Nedefiniran GLN " + koga +"!");
    
    if (mj == null || mj.length() == 0)
      throw new EracException("Nedefinirano mjesto " + koga +"!");
    
    if (pbr == null || pbr.length() == 0)
      throw new EracException("Nedefiniran poštanski broj " + koga +"!");
    
    if (adr == null || adr.length() == 0)
      throw new EracException("Nedefinirana adresa " + koga +"!");
    
    int split = (adr = adr.trim()).lastIndexOf(' ');
    if (split < 0)
      throw new EracException("Pogrešna adresa " + koga +"!");
      
    PartyType p = aof.createPartyType();
    if (gln != null && gln.length() > 0) {
      p.setEndpointID(bof.createEndpointIDType());
      p.getEndpointID().setValue(gln);
    }
    
    PartyNameType name = aof.createPartyNameType();
    name.setName(bof.createNameType());
    name.getName().setValue(naziv);
    p.getPartyName().add(name);

    p.setPartyLegalEntity(aof.createPartyLegalEntityType());
    p.getPartyLegalEntity().setCompanyID(bof.createCompanyIDType());
    p.getPartyLegalEntity().getCompanyID().setValue(oib);
    p.getPartyLegalEntity().setRegistrationName(bof.createRegistrationNameType());
    p.getPartyLegalEntity().getRegistrationName().setValue(naziv);
    
    p.setPostalAddress(aof.createAddressType());
    p.getPostalAddress().setStreetName(bof.createStreetNameType());
    p.getPostalAddress().getStreetName().setValue(adr.substring(0, split));
    p.getPostalAddress().setBuildingNumber(bof.createBuildingNumberType());
    p.getPostalAddress().getBuildingNumber().setValue(adr.substring(split).trim());
    p.getPostalAddress().setPostalZone(bof.createPostalZoneType());
    p.getPostalAddress().getPostalZone().setValue(pbr);
    p.getPostalAddress().setCityName(bof.createCityNameType());
    p.getPostalAddress().getCityName().setValue(mj);
    
    AddressLineType adrLine = aof.createAddressLineType();
    adrLine.setLine(bof.createLineType());
    adrLine.getLine().setValue(adr + ", " + pbr + " " + mj);
    p.getPostalAddress().getAddressLine().add(adrLine);
    
    CountryType ctr = aof.createCountryType();
    ctr.setIdentificationCode(bof.createIdentificationCodeType());
    ctr.getIdentificationCode().setListID("ISO3166-1");
    ctr.getIdentificationCode().setListAgencyID("6");
    ctr.getIdentificationCode().setListName("Country");
    ctr.getIdentificationCode().setListVersionID("0.3");
    ctr.getIdentificationCode().setListURI("http://docs.oasis-open.org/ubl/os-ubl-2.0/cl/gc/default/CountryIdentificationCode-2.0.gc");
    ctr.getIdentificationCode().setListSchemeURI("urn:oasis:names:specification:ubl:codelist:gc:CountryIdentificationCode-2.0");
    ctr.getIdentificationCode().setValue("HR");
    p.getPostalAddress().setCountry(ctr);
    
    return p;
  }
  
  private UBLExtensionType getExtension(String name, String id, String reason, ExtensionContentType content) {
    UBLExtensionType ext = eof.createUBLExtensionType();
    ext.setID(bof.createIDType());
    ext.getID().setValue("HRINVOICE1");
    ext.setName(bof.createNameType());
    ext.getName().setValue(name);
    ext.setExtensionAgencyID(eof.createExtensionAgencyIDType());
    ext.getExtensionAgencyID().setValue(id);
    ext.setExtensionAgencyName(eof.createExtensionAgencyNameType());
    ext.getExtensionAgencyName().setValue(id);
    ext.setExtensionAgencyURI(eof.createExtensionAgencyURIType());
    ext.getExtensionAgencyURI().setValue(id);
    ext.setExtensionReasonCode(eof.createExtensionReasonCodeType());
    ext.getExtensionReasonCode().setValue("MandatoryField");
    ext.setExtensionReason(eof.createExtensionReasonType());
    ext.getExtensionReason().setValue(reason);
    ext.setExtensionContent(content);
    
    return ext;
  }
  
  private XMLGregorianCalendar createDatum(Timestamp dat) {
    Calendar c = Calendar.getInstance();
    c.setTime(dat);
    
    XMLGregorianCalendar xc = dafact.newXMLGregorianCalendarDate(
            c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE), DatatypeConstants.FIELD_UNDEFINED);
    
    return xc;
  }
  
  private XMLGregorianCalendar createTimestamp(Timestamp dat) {
    Calendar c = Calendar.getInstance();
    c.setTime(dat);
    
    XMLGregorianCalendar xc = dafact.newXMLGregorianCalendarDate(
            c.get(Calendar.YEAR), c.get(Calendar.MONTH)+1, c.get(Calendar.DATE), DatatypeConstants.FIELD_UNDEFINED);
    xc.setHour(c.get(Calendar.HOUR));
    xc.setMinute(c.get(Calendar.MINUTE));
    xc.setSecond(c.get(Calendar.SECOND));
    xc.setMillisecond(0);
    System.out.println(xc);
    return xc;
  }
  
  public int sendRacun(String rac) {
    initParams();
    HttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
    try {
      HttpPost req = getRequest(SEND_DOC);
      
      JSONObject obj = getDefaultParams();
      obj.put("File", rac);
      
      String str = getResponse(client, req, obj);
      if (str == null || str.length() == 0 || !str.startsWith("{"))
        throw new EracException("Greška kod slanja e-Raèuna!");
      
      JSONObject ret = JSONObject.fromObject(str);
      return ret.getInt("ElectronicId");  
    } catch (EracException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    throw new EracException("Greška kod slanja e-Raèuna!");
  }
  
  public DataSet createData() {
    StorageDataSet ret = Aus.createSet("{Id}EID {Datum}@DATUM {Partner}NAZPAR:150 {Tip}TYPE:50 {Dokument}DOC:50 {Status}STATUS {Preuzet}@DATP OIB:20");
    ret.getColumn("EID").setVisible(0);
    ret.getColumn("DATUM").setWidth(20);
    ret.getColumn("DATUM").setDisplayMask("dd-MM-yyyy  'u' HH:mm:ss");
    ret.getColumn("NAZPAR").setWidth(40);
    ret.getColumn("TYPE").setWidth(12);
    ret.getColumn("DOC").setWidth(15);
    ret.getColumn("DATP").setWidth(20);
    ret.getColumn("DATP").setDisplayMask("dd-MM-yyyy  'u' HH:mm:ss");
    ret.getColumn("STATUS").setVisible(0);
    ret.getColumn("OIB").setVisible(0);
    ret.setTableName("OUTBOX");
    return ret;
  }
  
  public DataSet checkInbox() {
    return checkInbox(false, null, null);
  }
  
  HttpClient getClient() {
    if (!ld.raLocate(dm.getLogotipovi(), "CORG", OrgStr.getKNJCORG(false)))
      throw new EracException("Nedefiniran OIB tvrtke na logotipovima!");
      
    coib = dm.getLogotipovi().getString("OIB");
    if (coib.length() == 0)
      throw new EracException("Nedefiniran OIB tvrtke na logotipovima!");
    
    initParams();
    return HttpClientBuilder.create().setSSLHostnameVerifier(NoopHostnameVerifier.INSTANCE).build();
  }
  
  HttpPost getRequest(String path) throws URISyntaxException {
    HttpPost req = new HttpPost(new URIBuilder().setScheme("https").setHost(host).setPath(path).build());
    req.addHeader("content-type", "application/json"); 
    req.addHeader("charset", "utf-8");
    
    return req;
  }
  
  JSONObject getDefaultParams() {
    JSONObject obj = new JSONObject();
    obj.put("Username", new Integer(user));
    obj.put("Password", pass);
    obj.put("CompanyId", coib);
    if (unit != null && unit.length() > 0)
      obj.put("CompanyBu", unit);
    obj.put("SoftwareId", sid);
    return obj;
  }
  
  String getResponse(HttpClient client, HttpPost req, JSONObject params) throws ClientProtocolException, IOException {
    req.setEntity(new StringEntity(params.toString(), "utf-8"));
    
    HttpResponse resp = client.execute(req);
    
    if (resp.getEntity() == null) return null;
    
    String str = EntityUtils.toString(resp.getEntity(), "utf-8");
    if (str == null || str.length() == 0) return null;
    
    return str;
  }
  
  File getFile(String partner, String god, String name) {
    File d = new File(dir, partner);
    if (god != null) d = new File(d, god);
    return new File(d, name);
  }
  
  public void getDocument(DataSet ds, boolean outgoing) {
    String god = ds.isNull("DATUM") ? null : hr.restart.util.Util.getUtil().getYear(ds.getTimestamp("DATUM"));
     
    if (!outgoing && ds.getInt("STATUS") == 40) {
      File oldpdf = getFile(ds.getString("NAZPAR"), god, ds.getString("DOC") + ".pdf");
      if (oldpdf.exists() && oldpdf.canRead()) {
        System.out.println("Found old file: " + oldpdf);
        try {
          Runtime.getRuntime().exec(new String[] {pdfview, oldpdf.getCanonicalPath()});
          return;
        } catch (IOException e) {
          e.printStackTrace();
        }
        throw new EracException("Greška kod otvaranja e-Raèuna!");
      }
    }
    getDocument(ds.getInt("EID"), god, outgoing, ds.getInt("STATUS") != 40);
  }
  
  public void cancelDocument(int eid) {
    HttpClient client = getClient();
    try {
      HttpPost req = getRequest(CANCEL);
      JSONObject obj = getDefaultParams();
      obj.put("ElectronicId", new Integer(eid));
      obj.put("Apply", "cancel");
      System.out.println(getResponse(client, req, obj));
      return;
    } catch (EracException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    }
    throw new EracException("Greška kod poništavanja e-Raèuna!");
  }
  
  public void getDocument(int eid, String god, boolean outgoing, boolean unread) {
    HttpClient client = getClient();
    try {
      HttpPost req = getRequest(GET_DOC);
      
      JSONObject obj = getDefaultParams();
      obj.put("ElectronicId", new Integer(eid));
      
      String str = getResponse(client, req, obj);
      if (str == null || str.length() == 0 || !str.startsWith("<"))
        throw new EracException("Greška kod dohvata e-Raèuna!");
      
      XMLReader reader = XMLReaderFactory.createXMLReader();
      AttachmentExtractor handler = new AttachmentExtractor();
      reader.setContentHandler(handler);
      reader.parse(new InputSource(new StringReader(str)));
      
      if (outgoing) {
        File tmp = File.createTempFile("erac", ".pdf");
        FileUtils.writeByteArrayToFile(tmp, Base64.decodeBase64(handler.pdfData));
        Runtime.getRuntime().exec(new String[] {pdfview, tmp.getCanonicalPath()});
        tmp.deleteOnExit();
      } else {
        String sender = handler.sender;
        if (ld.raLocate(dm.getPartneri(), "OIB", handler.senderId))
          sender = dm.getPartneri().getString("NAZPAR");

        File pdir = new File(dir, sender);
        if (god != null) pdir = new File(pdir, god);
        pdir.mkdirs();
        File pdf = new File(pdir, handler.pdfName);
        FileUtils.writeStringToFile(new File(pdir, handler.invoice + ".xml"), str, "utf-8");
        FileUtils.writeByteArrayToFile(pdf, Base64.decodeBase64(handler.pdfData));
        
        if (unread) {
          System.out.println("Previously unread");
          HttpPost stamp = getRequest(MARK + eid);
          JSONObject sobj = getDefaultParams();
          System.out.println(getResponse(client, stamp, sobj));
        }
        Runtime.getRuntime().exec(new String[] {pdfview, pdf.getCanonicalPath()});
      }
      //FileUtils.writeStringToFile(outfile, str);
      return;
    } catch (EracException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (SAXException e) {
      e.printStackTrace();
    }
    throw new EracException("Greška kod dohvata e-Raèuna!");
  }
  
  public DataSet checkInbox(boolean all, Timestamp dfrom, Timestamp dto) {
    HttpClient client = getClient();
    try {
      HttpPost req = getRequest(CHECK_INBOX);
      
      JSONObject obj = getDefaultParams();
      if (!all) obj.put("Filter", "Undelivered");
      if (dfrom != null) obj.put("From", dfrom.toString().substring(0, 10));
      if (dto != null) obj.put("To", dto.toString().substring(0, 10));
      
      String str = getResponse(client, req, obj);
      if (str == null || str.length() == 0 || !str.startsWith("["))
        throw new EracException("Greška kod provjere pošte za e-Raèune!");
      
      DataSet ds = createData();
      JSONArray arr = JSONArray.fromObject(str);
      for (int i = 0; i < arr.size(); i++) {
        JSONObject o = (JSONObject) arr.get(i);
        ds.insertRow(false);
        ds.setInt("EID", o.getInt("ElectronicId"));
        ds.setTimestamp("DATUM", new Timestamp(df.parse(o.getString("Sent")).getTime()));
        ds.setString("NAZPAR", o.getString("SenderBusinessName"));
        ds.setString("OIB", o.getString("SenderBusinessNumber"));
        if (ld.raLocate(dm.getPartneri(), "OIB", ds))
          ds.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
        ds.setString("TYPE", o.getString("DocumentTypeName"));
        ds.setString("DOC", o.getString("DocumentNr"));
        ds.setInt("STATUS", o.getInt("StatusId"));
        String deliver = o.getString("Delivered");
        if (deliver != null && deliver.length() > 0 && !deliver.equals("null"))
          ds.setTimestamp("DATP", new Timestamp(df.parse(deliver).getTime()));
        ds.post();
      }
      ds.setSort(new SortDescriptor(new String[] {"DATUM"}, false, true, null));
      return ds;
    } catch (EracException e) {
      throw e;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (JSONException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    throw new EracException("Greška kod provjere pošte za e-Raèune!");
  }
  
  public DataSet checkOutbox(Timestamp dfrom, Timestamp dto) {
    HttpClient client = getClient();
    try {
      HttpPost req = getRequest(CHECK_OUTBOX);
      
      JSONObject obj = getDefaultParams();
      if (dfrom != null) obj.put("From", dfrom.toString().substring(0, 10));
      if (dto != null) obj.put("To", dto.toString().substring(0, 10));
      
      String str = getResponse(client, req, obj);
      if (str == null || str.length() == 0 || !str.startsWith("[")) 
        throw new EracException("Greška kod provjere pošte za e-Raèune!");
      
      DataSet ds = createData();
      JSONArray arr = JSONArray.fromObject(str);
      for (int i = 0; i < arr.size(); i++) {
        JSONObject o = (JSONObject) arr.get(i);
        ds.insertRow(false);
        ds.setInt("EID", o.getInt("ElectronicId"));
        String sent = o.getString("Sent");
        if (sent != null && sent.length() > 0 && !sent.equals("null"))
          ds.setTimestamp("DATUM", new Timestamp(df.parse(o.getString("Sent")).getTime()));
        ds.setString("NAZPAR", o.getString("RecipientBusinessName"));
        ds.setString("OIB", o.getString("RecipientBusinessNumber"));
        if (ld.raLocate(dm.getPartneri(), "OIB", ds))
          ds.setString("NAZPAR", dm.getPartneri().getString("NAZPAR"));
        ds.setString("TYPE", o.getString("DocumentTypeName"));
        ds.setString("DOC", o.getString("DocumentNr"));
        ds.setInt("STATUS", o.getInt("StatusId"));
        String deliver = o.getString("Delivered");
        if (deliver != null && deliver.length() > 0 && !deliver.equals("null"))
          ds.setTimestamp("DATP", new Timestamp(df.parse(deliver).getTime()));
        ds.post();
      }
      ds.setSort(new SortDescriptor(new String[] {"DATUM"}, false, true, null));
      return ds;
    } catch (URISyntaxException e) {
      e.printStackTrace();
    } catch (ClientProtocolException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (ParseException e) {
      e.printStackTrace();
    }
    throw new EracException("Greška kod provjere pošte za e-Raèune!");
  }
  
  
  protected String rootElem = "http://fina.hr/eracun/erp/OutgoingInvoicesData/v3.2:OutgoingInvoicesData";
  protected HashMap rootSchemas = new HashMap();
  {
    rootSchemas.put("urn:oasis:names:specification:ubl:schema:xsd:Invoice-2:Invoice", 
        "urn:oasis:names:specification:ubl:schema:xsd:Invoice-2 HRInvoice.xsd");
    rootSchemas.put("urn:oasis:names:specification:ubl:schema:xsd:AttachedDocument-2:AttachedDocument", 
        "urn:oasis:names:specification:ubl:schema:xsd:AttachedDocument-2 UBL-AttachedDocument-2.1.xsd");
  }
  
  protected HashMap prefs = new HashMap();
  {
    prefs.put("urn:oasis:names:specification:ubl:schema:xsd:CommonBasicComponents-2", "cbc");
    prefs.put("urn:oasis:names:specification:ubl:schema:xsd:SignatureAggregateComponents-2", "sac");
    prefs.put("urn:oasis:names:specification:ubl:schema:xsd:CommonExtensionComponents-2", "ext");
    prefs.put("urn:oasis:names:specification:ubl:schema:xsd:CommonAggregateComponents-2", "cac");
    prefs.put("urn:oasis:names:specification:ubl:schema:xsd:CommonSignatureComponents-2", "sig");
  }
  
  static class AttachmentExtractor extends ContentAdapter {
    
    StringBuffer buf = new StringBuffer();
    String pdfData, pdfName, sender, senderId, invoice;
    List path = new ArrayList();
    boolean capture = false;
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      path.add(localName);
      buf.setLength(0);
      capture = true;
      if (path.equals(pdfPath)) {
        pdfName = atts.getValue("filename");
      } else if (!path.equals(senderPath) && !path.equals(invoicePath) && !path.equals(senderIdPath))
        capture = false;
    }
    public void characters(char[] ch, int start, int length) throws SAXException {
      if (capture) buf.append(ch, start, length);
    }
    public void endElement(String uri, String localName, String qName) throws SAXException {
      if (capture) {
        if (path.equals(pdfPath))
          pdfData = buf.toString();
        else if (path.equals(senderPath))
          sender = buf.toString();
        else if (path.equals(senderIdPath))
          senderId = buf.toString();
        else if (path.equals(invoicePath))
          invoice = buf.toString();
        capture = false;
      }
      path.remove(path.size() - 1);
    }
    
    
    static List pdfPath, senderPath, senderIdPath, invoicePath;
    static {
      pdfPath = new VarStr("OutgoingInvoicesData OutgoingInvoice AttachedDocumentEnvelope AttachedDocument Attachment EmbeddedDocumentBinaryObject").splitAsList();
      senderPath = new VarStr("OutgoingInvoicesData OutgoingInvoice AttachedDocumentEnvelope AttachedDocument SenderParty PartyLegalEntity RegistrationName").splitAsList();
      senderIdPath = new VarStr("OutgoingInvoicesData OutgoingInvoice AttachedDocumentEnvelope AttachedDocument SenderParty PartyLegalEntity CompanyID").splitAsList();
      invoicePath = new VarStr("OutgoingInvoicesData OutgoingInvoice SupplierInvoiceID").splitAsList();
    }
  }
  
  static class ContentAdapter implements ContentHandler {
    /**
     * @throws SAXException  
     */
    public void startPrefixMapping(String prefix, String uri) throws SAXException {
      //
    }     
    /**
     * @throws SAXException  
     */
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
      //
    }
    /**
     * @throws SAXException  
     */
    public void startDocument() throws SAXException {
      //
    }
    /**
     * @throws SAXException  
     */
    public void skippedEntity(String name) throws SAXException {
      //
    }
    public void setDocumentLocator(Locator locator) {
      //
    }
    /**
     * @throws SAXException  
     */
    public void processingInstruction(String target, String data) throws SAXException {
      //
    }
    
    /**
     * @throws SAXException  
     */
    public void ignorableWhitespace(char[] ch, int start, int length) throws SAXException {
      //
    }
    
    /**
     * @throws SAXException  
     */
    public void endPrefixMapping(String prefix) throws SAXException {
      //
    }
    
    /**
     * @throws SAXException  
     */
    public void endElement(String uri, String localName, String qName) throws SAXException {
      //
    }
    
    /**
     * @throws SAXException  
     */
    public void endDocument() throws SAXException {
      //
    }
    
    /**
     * @throws SAXException  
     */
    public void characters(char[] ch, int start, int length) throws SAXException {
      // 
    }
  }
}
