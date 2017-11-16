select 
  SESSION_NAME,
  DATE_TIME,
  LATITUDE,
  LONGITUDE,
  ALTITUDE
  from SESSION_PERSIST s,
  GPS_DATA g
where s.SESSION_PERSIST_ID = g.session_persist_id
and  s.SESSION_NAME like 'Saved Garmin Track session%' 
order by s.SESSION_PERSIST_ID, date_time
 




select * 
from SESSION_PERSIST s
where  s.SESSION_NAME like 'Saved Garmin Track session%' 
order by s.SESSION_PERSIST_ID

select * from SESSION_PERSIST s


select s.session_name,g.* 
from SESSION_PERSIST s, GPS_DATA g
where s.SESSION_PERSIST_ID = g.session_persist_id
and s.SESSION_NAME like 'Saved Garmin Track session%' 
order by s.SESSION_PERSIST_ID

select * from GPS_DATA g
where g.session_persist_id in (
select s.SESSION_PERSIST_ID
from SESSION_PERSIST s
where  s.SESSION_NAME like 'Saved Garmin Track session%' 
)

delete from GPS_DATA g
where g.session_persist_id in (
select s.SESSION_PERSIST_ID
from SESSION_PERSIST s
where  s.SESSION_NAME like 'Saved Garmin Track session%' 
)

delete from SESSION_PERSIST s
where  s.SESSION_NAME like 'Saved Garmin Track session%' 


/*
SELECT SESSION_PERSIST_ID,
  DATE_TIME_CREATE,
  SESSION_END_DATE_TIME,
  SESSION_DESCRIPTION,
  SESSION_NAME,
  SESSION_START_DATE_TIME
FROM SESSION_PERSIST ;

SELECT GPS_DATA_ID,
  ALTITUDE,
  DATE_TIME,
  LATITUDE,
  LATITUDE_DIRECTION,
  LEG_HEADING,
  LEG_LENGTH,
  LEG_SPEED,
  LEG_TIME_SECS,
  LONGITUDE,
  LONGITUDE_DIRECTION,
  SESSION_PERSIST_ID
FROM GPS_DATA ;
*/