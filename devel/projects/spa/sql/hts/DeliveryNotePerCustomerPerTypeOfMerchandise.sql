select CAST (pn.notenumber AS CHAR(20)) as notenumber,
       c.customernumber,
       pn.deliverydate,
       case
         when siz.sizedefinition_id in (1,2) then
           p.code
         else
           p.code||'#'||siz.code 
       end
               as productcode,
       pnl.deliveredamount,
       (select ih.invoicenumber from invoiceheader ih where ih.invoiceheader_id=pn.invoiceheader_id and ih.invoicenumber>0) as invoicenumber,
       pn.timestamp as modification_date,
       p.typeofmerchandise,
       case p.typeofmerchandise
         when 1 then
          'ALLGEMEIN'
         when 2 then
          'BEKLEIDUNG'
         when 3 then
          'VERKAUF'
         when 4 then
          'EMBLEMS'
         else
          'NICHT BEKANNT'
       end as type_of_m_desc,
       pn.status_id,
       st.factorydescription as status_desc
  from packingnote       pn,
       packingnoteline   pnl,
       distributionpoint dis,
       department        dep,
       customer          c,
       product           p,
       sizedefinition    siz,
       status            st
 where pnl.packingnote_id = pn.packingnote_id
   and pnl.product_id = p.product_id
   and pnl.sizedefinition_id = siz.sizedefinition_id
   and pn.distributionpoint_id = dis.distributionpoint_id
   and dis.department_id = dep.department_id
   and dep.customer_id = c.customer_id
   and st.status_id = pn.status_id
   and pn.notenumber >&1
   and p.typeofmerchandise=3
 order by pn.notenumber, pn.deliverydate, c.customernumber, p.code, siz.code
