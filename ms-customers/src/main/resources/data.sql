INSERT INTO customers(company_name,contact_name,creation_timestamp,update_timestamp)
VALUES('Tyrell Corporation','Eldon Tyrell', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('V.I.L.E.','Carmen Sandiego', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Rekall','Douglas Quaid', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Onmi Consumer Products','Dick Jones', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP),
('Acme Corporation','Wile E. Coyote', CURRENT_TIMESTAMP,CURRENT_TIMESTAMP);

INSERT INTO addresses(location,city,country,email,creation_timestamp,update_timestamp,customer_id)
VALUES('Sinister skyscrapper 121 st','Neo Tokyo','Nippon','tyrell@tyrell.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,1),
('Orohena volcano','Tahiti','French Polynesia','vile@vile.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,2),
('comercial distric 71','Tanganika','Congo','rekall@rekall.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,3),
('3st Ave','Detroit','USA','ocp@ocp.com',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,4),
('Eddie Valiant Pz','Toontown','Planet Toons','acme@acme.tt',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,5),
('Sunset Bulevard 87','Hollywood','USA','acme@acme.us',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,5),
('2 more cactus and one mirage to the left','Gobi Desert','China','acme@acme.cn',CURRENT_TIMESTAMP,CURRENT_TIMESTAMP,5);