create sequence hibernate_sequence start with 1 increment by 1;
create table action (id number(19,0) not null, version number(19,0) not null, assigned_user_id number(19,0) not null, date_created timestamp not null, incident_id number(19,0) not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, status_cd varchar2(255 char) not null, step_number number(10,0) not null, primary key (id));
create table action_status_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table activity (id number(19,0) not null, version number(19,0) not null, activity_type_code_id varchar2(255 char) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, primary key (id));
create table activity_type_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table asset (id number(19,0) not null, version number(19,0) not null, asset_type_cd varchar2(255 char) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, organization_id number(19,0) not null, primary key (id));
create table asset_type_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table incident (id number(19,0) not null, version number(19,0) not null, asset_id number(19,0) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, lead_id number(19,0) not null, status_cd varchar2(255 char) not null, threat_id number(19,0) not null, primary key (id));
create table incident_status_audit (id number(19,0) not null, version number(19,0) not null, date_created timestamp not null, incident_id number(19,0) not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, status_cd varchar2(255 char) not null, primary key (id));
create table incident_status_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table incident_team_member (id number(19,0) not null, version number(19,0) not null, date_created timestamp not null, incident_id number(19,0) not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, user_id number(19,0) not null, primary key (id));
create table media (id number(19,0) not null, version number(19,0) not null, date_created timestamp not null, incident_id number(19,0) not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, primary key (id));
create table organization (id number(19,0) not null, version number(19,0) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, primary key (id));
create table parameter (name varchar2(255 char) not null, version number(19,0) not null, date_created timestamp not null, description varchar2(255 char) not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, value varchar2(4000 char), primary key (name));
create table person (id number(19,0) not null, version number(19,0) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, organization_id number(19,0) not null, primary key (id));
create table person_asset_aff_type_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table person_asset_affiliation (id number(19,0) not null, version number(19,0) not null, affiliation_type_cd varchar2(255 char) not null, asset_id number(19,0) not null, date_created timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, person_id number(19,0) not null, primary key (id));
create table role (id number(19,0) not null, version number(19,0) not null, authority varchar2(255 char) not null, primary key (id));
create table threat (id number(19,0) not null, version number(19,0) not null, date_begin timestamp not null, date_created timestamp not null, date_end timestamp not null, last_updated timestamp not null, last_updated_by_user_id number(19,0) not null, last_updated_comment varchar2(255 char) not null, threat_severity_cd varchar2(255 char) not null, threat_type_cd varchar2(255 char) not null, primary key (id));
create table threat_severity_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table threat_type_code (code varchar2(255 char) not null, date_created timestamp not null, description varchar2(4000 char), enabled number(1,0) default 1 not null, label varchar2(255 char) not null, primary key (code));
create table user_role (user_id number(19,0) not null, role_id number(19,0) not null, primary key (user_id, role_id));
create table users (id number(19,0) not null, version number(19,0) not null, account_expired number(1,0) not null, account_locked number(1,0) not null, email varchar2(255 char) not null, enabled number(1,0) not null, first_name varchar2(255 char) not null, last_name varchar2(255 char) not null, "password" varchar2(255 char) not null, password_expired number(1,0) not null, username varchar2(255 char) not null, primary key (id));
alter table role add constraint UK_irsamgnera6angm0prq1kemt2 unique (authority);
alter table users add constraint UK_r43af9ap4edm43mmtq01oddj6 unique (username);
alter table action add constraint FKeu07eg2or69vumkl6v6a70mmm foreign key (assigned_user_id) references users;
alter table action add constraint FKl1l96r1eufvu5ndn6m31c3i2c foreign key (incident_id) references incident;
alter table action add constraint FK9ty9pmtpsbs32xc1mjas2iaqe foreign key (last_updated_by_user_id) references users;
alter table action add constraint FKm8ijy4qxnah4p7ttr89scq5c0 foreign key (status_cd) references action_status_code;
alter table activity add constraint FKkyvydlwcbu4lkk1xm4vn253rh foreign key (activity_type_code_id) references activity_type_code;
alter table activity add constraint FK5gsgm8mrxjci7adfy1dvc0qfs foreign key (last_updated_by_user_id) references users;
alter table asset add constraint FKw75uyp545khqqe1p87tlcq4b foreign key (asset_type_cd) references asset_type_code;
alter table asset add constraint FKggi40mk78vk4gu3l9yo5v7oml foreign key (last_updated_by_user_id) references users;
alter table asset add constraint FK11vv8j164wak62lkin13g92kf foreign key (organization_id) references organization;
alter table incident add constraint FKa4owkco37qw052c8mhyx4hixp foreign key (asset_id) references asset;
alter table incident add constraint FKpuvv6579u4aiqxla5gl3tq0q9 foreign key (last_updated_by_user_id) references users;
alter table incident add constraint FK5d2yqspycs3lfx4ohk3t01nxv foreign key (lead_id) references users;
alter table incident add constraint FKdu9o5rgubdfmcfpxoqsm5qs1r foreign key (status_cd) references incident_status_code;
alter table incident add constraint FKbm66h21yyqfug61s25bltnpgn foreign key (threat_id) references threat;
alter table incident_status_audit add constraint FKkyht67q3amwpatwu2il0tfrsl foreign key (incident_id) references incident;
alter table incident_status_audit add constraint FK861bdcrvuvd45ywkshbttb7qm foreign key (last_updated_by_user_id) references users;
alter table incident_status_audit add constraint FK14bdbqxqn9e7m9ifw7dc3503m foreign key (status_cd) references incident_status_code;
alter table incident_team_member add constraint FK3d3hdjkx8edxaceqixjwplrpf foreign key (incident_id) references incident;
alter table incident_team_member add constraint FK7g5fpv046hieap813j2q9om00 foreign key (last_updated_by_user_id) references users;
alter table incident_team_member add constraint FK8v0r83vn6eufb7mhee2p1wl1k foreign key (user_id) references users;
alter table media add constraint FKbwyg740du44goep7p7tmk27gy foreign key (incident_id) references incident;
alter table media add constraint FKqugf2uhiwkov65mbcgdlv844s foreign key (last_updated_by_user_id) references users;
alter table organization add constraint FKo19gwqdnlh878dce8xq92t1lf foreign key (last_updated_by_user_id) references users;
alter table parameter add constraint FKbkfpwi1tspulkbgpojxqs86et foreign key (last_updated_by_user_id) references users;
alter table person add constraint FK856tpvp809hurdubyju5mw79o foreign key (last_updated_by_user_id) references users;
alter table person add constraint FK23c7ajo7g43ugmupov5banbas foreign key (organization_id) references organization;
alter table person_asset_affiliation add constraint FK5q147q8d4a97g1tv7jr7k2pec foreign key (affiliation_type_cd) references person_asset_aff_type_code;
alter table person_asset_affiliation add constraint FKllwdtymxvmw4oedxfgsijie4e foreign key (asset_id) references asset;
alter table person_asset_affiliation add constraint FKojmx1iqlyijc7p5456tncl26n foreign key (last_updated_by_user_id) references users;
alter table person_asset_affiliation add constraint FKajcrt7wg03o9jmrbki5ghnl6y foreign key (person_id) references person;
alter table threat add constraint FKcijtemqd7u3t54by0psgvbova foreign key (last_updated_by_user_id) references users;
alter table threat add constraint FKh5p9jas2psde73e3fg1ktq7y5 foreign key (threat_severity_cd) references threat_severity_code;
alter table threat add constraint FKnrf4uvpym2sqikhrpf1c4ulpj foreign key (threat_type_cd) references threat_type_code;
alter table user_role add constraint FKj345gk1bovqvfame88rcx7yyx foreign key (user_id) references users;
alter table user_role add constraint FKa68196081fvovjhkek5m97n3y foreign key (role_id) references role;
