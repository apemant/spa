select cast (e.employeenumber as char(10)) as CAG 
  from contractspercustomer cp, customer c, employee e 
 where cp.customer_id = c.customer_id 
   and cp.accountmanager_id = e.employee_id 
   and c.CUSTOMERNUMBER = &1