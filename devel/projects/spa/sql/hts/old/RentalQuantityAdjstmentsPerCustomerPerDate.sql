select c.customernumber,
       rqa.adjustmentdate,
       res.code as reason_code,
       (select rd.description
          from reasoncode_desc rd
         where rd.reasoncode_id = rqa.reasoncode_id
           and rd.language_id =
               (select l.language_id from languages l where l.code = 'DE_DE')) as reason_description,
       rqa.quantity,
       p.code as product_code,
       siz.code as size_code,
       rqa.timestamp as modification_date
  from rentalqtyadjustment        rqa,
       productlistperdistribpoint plpd,
       distributionpoint          dis,
       department                 dep,
       reasoncode                 res,
       customer                   c,
       product                    p,
       sizedefinition             siz
 where rqa.productlistperdistribpoint_id =
       plpd.productlistperdistribpoint_id
   and plpd.distributionpoint_id = dis.distributionpoint_id
   and dis.department_id = dep.department_id
   and dep.customer_id = c.customer_id
   and rqa.reasoncode_id = res.reasoncode_id
   and plpd.product_id = p.product_id
   and plpd.sizedefinition_id = siz.sizedefinition_id
 order by rqa.adjustmentdate, c.customernumber, p.code, siz.code
