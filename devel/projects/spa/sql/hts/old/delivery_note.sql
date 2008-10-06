select pn.notenumber,
       pn.deliverydate,
       p.code as productcode,
       pnl.deliveredamount,
       (select ih.invoicenumber
          from invoiceheader ih
         where ih.invoiceheader_id = pn.invoiceheader_id) as invoicenumber
  from packingnote pn, packingnoteline pnl, product p
 where pnl.packingnote_id = pn.packingnote_id
   and pnl.product_id = p.product_id
   and pn.status_id=7
   and pn.notenumber>&1;