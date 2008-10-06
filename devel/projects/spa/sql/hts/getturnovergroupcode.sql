select cast (tog.code as char(10)) as tog_code 
 from turnovergroup tog
 where tog.TURNOVERGROUP_ID = &1