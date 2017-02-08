insert into THREAT_SEVERITY_CODE(CODE, LABEL, DESCRIPTION, DATE_CREATED) (select 'INFO', 'Information', 'Information', sysdate from dual);
insert into THREAT_SEVERITY_CODE(CODE, LABEL, DESCRIPTION, DATE_CREATED) (select 'WARNING', 'Warning', 'Warning', sysdate from dual);
insert into THREAT_SEVERITY_CODE(CODE, LABEL, DESCRIPTION, DATE_CREATED) (select 'ALERT', 'Alert', 'Alert', sysdate from dual);