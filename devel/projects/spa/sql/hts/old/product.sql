select p.code as productcode,
       (select pd.description
          from product_desc pd
         where pd.product_id = p.product_id
           and pd.language_id =
               (select l.language_id from languages l where l.code = 'HR_HR')) as name,
       pg.code as groupcode
  from product p, productgroup pg
 where p.productgroup_id = pg.productgroup_id
 order by p.code;