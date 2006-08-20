--
-- PostgreSQL database dump
--

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: asterisk_db; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE asterisk_db WITH TEMPLATE = template0 ENCODING = 'UTF8';


ALTER DATABASE asterisk_db OWNER TO postgres;

\connect asterisk_db

SET client_encoding = 'UTF8';
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'Standard public schema';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: asterisk_configuration; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE asterisk_configuration (
    id serial NOT NULL,
    cat_metric integer DEFAULT 0 NOT NULL,
    var_metric integer DEFAULT 0 NOT NULL,
    commented integer DEFAULT 0 NOT NULL,
    filename character varying(128) NOT NULL,
    category character varying(256) NOT NULL,
    var_name character varying(256) NOT NULL,
    var_val character varying(256) NOT NULL
);


ALTER TABLE public.asterisk_configuration OWNER TO postgres;

--
-- Name: asterisk_configuration_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('asterisk_configuration', 'id'), 13, true);


--
-- Name: cdr; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE cdr (
    id serial NOT NULL,
    calldate timestamp with time zone DEFAULT now() NOT NULL,
    clid text DEFAULT ''::text NOT NULL,
    src text DEFAULT ''::text NOT NULL,
    dst text DEFAULT ''::text NOT NULL,
    dcontext text DEFAULT ''::text NOT NULL,
    channel text DEFAULT ''::text NOT NULL,
    dstchannel text DEFAULT ''::text NOT NULL,
    lastapp text DEFAULT ''::text NOT NULL,
    lastdata text DEFAULT ''::text NOT NULL,
    duration bigint DEFAULT (0)::bigint NOT NULL,
    billsec bigint DEFAULT (0)::bigint NOT NULL,
    disposition text DEFAULT ''::text NOT NULL,
    amaflags bigint DEFAULT (0)::bigint NOT NULL,
    accountcode text DEFAULT ''::text NOT NULL,
    uniqueid text DEFAULT ''::text NOT NULL,
    userfield text DEFAULT ''::text NOT NULL
);


ALTER TABLE public.cdr OWNER TO postgres;

--
-- Name: cdr_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('cdr', 'id'), 1, false);


--
-- Name: extension; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE extension (
    id serial NOT NULL,
    context text DEFAULT ''::text NOT NULL,
    exten text DEFAULT ''::text NOT NULL,
    priority integer DEFAULT 0 NOT NULL,
    app character varying(20) DEFAULT ''::character varying NOT NULL,
    appdata character varying(128)
);


ALTER TABLE public.extension OWNER TO postgres;

--
-- Name: extension_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('extension', 'id'), 21, true);


--
-- Name: iax; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE iax (
    id serial NOT NULL,
    name text DEFAULT ''::text NOT NULL,
    username character varying(80) DEFAULT ''::character varying NOT NULL,
    accountcode character varying(50),
    "type" character varying DEFAULT 'friend'::character varying NOT NULL,
    secret character varying(50),
    md5secret character varying(32),
    dbsecret character varying(100),
    auth character varying(100) DEFAULT 'md5'::character varying NOT NULL,
    notransfer character varying(10) DEFAULT 'yes'::character varying,
    inkeys character varying(100),
    outkeys character varying(100),
    nat character varying(5) DEFAULT 'yes'::character varying NOT NULL,
    port character varying(5) DEFAULT ''::character varying NOT NULL,
    host character varying(31) DEFAULT 'dynamic'::character varying NOT NULL,
    ipaddr character varying(15) DEFAULT ''::character varying NOT NULL,
    defaultip character varying(15) DEFAULT ''::character varying NOT NULL,
    insecure character varying(10) DEFAULT 'very'::character varying,
    qualify character varying(4) DEFAULT 'yes'::character varying,
    disallow character varying(100) DEFAULT 'all'::character varying NOT NULL,
    allow character varying(100) DEFAULT 'g729;ilbc;gsm;ulaw;alaw'::character varying NOT NULL,
    regseconds integer DEFAULT 3,
    amaflags character varying(10),
    callgroup character varying(10),
    callerid character varying(100),
    canreinvite character varying(3) DEFAULT 'yes'::character varying,
    context text,
    dtmfmode character varying(7),
    fromuser character varying(80),
    fromdomain character varying(80),
    "language" character varying(2) DEFAULT 'en'::character varying,
    mailbox character varying(200),
    permit character varying(95),
    deny character varying(95),
    mask character varying(95),
    pickupgroup character varying(10),
    restrictcid character varying(1),
    rtptimeout character varying(3),
    rtpholdtimeout character varying(3),
    musiconhold character varying(100),
    regexten character varying(80) DEFAULT ''::character varying NOT NULL,
    cancallforward character varying(3) DEFAULT 'yes'::character varying
);


ALTER TABLE public.iax OWNER TO postgres;

--
-- Name: iax_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('iax', 'id'), 1, false);


--
-- Name: queue; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE queue (
    id serial NOT NULL,
    name character varying(128) NOT NULL,
    musiconhold character varying(128),
    announce character varying(128),
    context text,
    timeout bigint,
    monitor_join boolean,
    monitor_format character varying(128),
    queue_youarenext character varying(128),
    queue_thereare character varying(128),
    queue_callswaiting character varying(128),
    queue_holdtime character varying(128),
    queue_minutes character varying(128),
    queue_seconds character varying(128),
    queue_lessthan character varying(128),
    queue_thankyou character varying(128),
    queue_reporthold character varying(128),
    announce_frequency bigint,
    announce_round_seconds bigint,
    announce_holdtime character varying(128),
    retry bigint,
    wrapuptime bigint,
    maxlen bigint,
    servicelevel bigint,
    strategy character varying(128),
    joinempty character varying(128),
    leavewhenempty character varying(128),
    eventmemberstatus boolean,
    eventwhencalled boolean,
    reportholdtime boolean,
    memberdelay bigint,
    weight bigint,
    timeoutrestart boolean
);


ALTER TABLE public.queue OWNER TO postgres;

--
-- Name: queue_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('queue', 'id'), 1, false);


--
-- Name: queue_member; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE queue_member (
    id serial NOT NULL,
    queue_name character varying(128) NOT NULL,
    interface character varying(128) NOT NULL,
    penalty bigint
);


ALTER TABLE public.queue_member OWNER TO postgres;

--
-- Name: queue_member_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('queue_member', 'id'), 1, false);


--
-- Name: sip; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE sip (
    id serial NOT NULL,
    name text DEFAULT ''::text NOT NULL,
    username character varying(80) DEFAULT ''::character varying NOT NULL,
    accountcode character varying(50),
    "type" character varying DEFAULT 'friend'::character varying NOT NULL,
    secret character varying(50),
    md5secret character varying(32),
    dbsecret character varying(100),
    auth character varying(100) DEFAULT 'md5'::character varying NOT NULL,
    notransfer character varying(10) DEFAULT 'yes'::character varying,
    inkeys character varying(100),
    outkeys character varying(100),
    nat character varying(5) DEFAULT 'yes'::character varying NOT NULL,
    port character varying(5) DEFAULT ''::character varying NOT NULL,
    host character varying(31) DEFAULT 'dynamic'::character varying NOT NULL,
    ipaddr character varying(15) DEFAULT ''::character varying NOT NULL,
    defaultip character varying(15) DEFAULT ''::character varying NOT NULL,
    insecure character varying(10) DEFAULT 'very'::character varying,
    qualify character varying(4) DEFAULT 'yes'::character varying,
    disallow character varying(100) DEFAULT 'all'::character varying NOT NULL,
    allow character varying(100) DEFAULT 'g729;ilbc;gsm;ulaw;alaw'::character varying NOT NULL,
    regseconds integer DEFAULT 3,
    amaflags character varying(10),
    callgroup character varying(10),
    callerid character varying(100),
    canreinvite character varying(3) DEFAULT 'yes'::character varying,
    context text,
    dtmfmode character varying(7),
    fromuser character varying(80),
    fromdomain character varying(80),
    "language" character varying(2) DEFAULT 'en'::character varying,
    mailbox character varying(200),
    permit character varying(95),
    deny character varying(95),
    mask character varying(95),
    pickupgroup character varying(10),
    restrictcid character varying(1),
    rtptimeout character varying(3),
    rtpholdtimeout character varying(3),
    musiconhold character varying(100),
    regexten character varying(80) DEFAULT ''::character varying NOT NULL,
    cancallforward character varying(3) DEFAULT 'yes'::character varying
);


ALTER TABLE public.sip OWNER TO postgres;

--
-- Name: sip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('sip', 'id'), 3, true);


--
-- Name: voicemail; Type: TABLE; Schema: public; Owner: postgres; Tablespace: 
--

CREATE TABLE voicemail (
    id serial NOT NULL,
    customer_id bigint DEFAULT (0)::bigint NOT NULL,
    context text DEFAULT ''::text NOT NULL,
    mailbox character varying(200) NOT NULL,
    "password" character varying(4) DEFAULT '0'::character varying NOT NULL,
    fullname character varying(50) DEFAULT ''::character varying NOT NULL,
    email character varying(50) DEFAULT ''::character varying NOT NULL,
    pager character varying(50) DEFAULT ''::character varying NOT NULL,
    stamp timestamp with time zone DEFAULT now() NOT NULL,
    tz character varying(10) DEFAULT 'central'::character varying NOT NULL,
    attach character varying(4) DEFAULT 'yes'::character varying NOT NULL,
    saycid character varying(4) DEFAULT 'yes'::character varying NOT NULL,
    dialout character varying(10) DEFAULT ''::character varying NOT NULL,
    callback character varying(10) DEFAULT ''::character varying NOT NULL,
    review character varying(4) DEFAULT 'no'::character varying NOT NULL,
    "operator" character varying(4) DEFAULT 'no'::character varying NOT NULL,
    envelope character varying(4) DEFAULT 'no'::character varying NOT NULL,
    sayduration character varying(4) DEFAULT 'no'::character varying NOT NULL,
    saydurationm integer DEFAULT 1 NOT NULL,
    sendvoicemail character varying(4) DEFAULT 'no'::character varying NOT NULL,
    "delete" character varying(4) DEFAULT 'no'::character varying NOT NULL,
    nextaftercmd character varying(4) DEFAULT 'yes'::character varying NOT NULL,
    forcename character varying(4) DEFAULT 'no'::character varying NOT NULL,
    forcegreetings character varying(4) DEFAULT 'no'::character varying NOT NULL,
    hidefromdir character varying(4) DEFAULT 'yes'::character varying NOT NULL
);


ALTER TABLE public.voicemail OWNER TO postgres;

--
-- Name: voicemail_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval(pg_catalog.pg_get_serial_sequence('voicemail', 'id'), 2, true);


--
-- Data for Name: asterisk_configuration; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (1, 0, 0, 0, 'voicemail.conf', 'general', 'format', 'wav49|gsm|wav');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (2, 0, 0, 0, 'voicemail.conf', 'general', 'serveremail', 'asterisk');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (3, 0, 0, 0, 'voicemail.conf', 'general', 'attack', 'yes');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (4, 0, 0, 0, 'voicemail.conf', 'general', 'skipms', '3000');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (5, 0, 0, 0, 'voicemail.conf', 'general', 'maxsilence', '10');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (6, 0, 0, 0, 'voicemail.conf', 'general', 'silencethreshold', '128');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (7, 0, 0, 0, 'voicemail.conf', 'general', 'maxlogins', '3');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (8, 0, 0, 0, 'voicemail.conf', 'general', 'emaildateformat', '%A, %B %d, %Y at %r');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (9, 0, 0, 0, 'voicemail.conf', 'general', 'sendvoicemail', 'yes');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (10, 0, 0, 0, 'voicemail.conf', 'zonemessages', 'eastern', 'America/New_York|''vm-received'' Q ''digits/at'' IMp');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (11, 0, 0, 0, 'voicemail.conf', 'zonemessages', 'central', 'America/Chicago|''vm-received'' Q ''digits/at'' IMp');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (12, 0, 0, 0, 'voicemail.conf', 'zonemessages', 'central24', 'America/Chicago|''vm-received'' q ''digits/at'' H N ''hours''');
INSERT INTO asterisk_configuration (id, cat_metric, var_metric, commented, filename, category, var_name, var_val) VALUES (13, 0, 0, 0, 'voicemail.conf', 'zonemessages', 'military', 'Zulu|''vm-received'' q ''digits/at'' H N ''hours'' ''phonetic/z_p''');


--
-- Data for Name: cdr; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: extension; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (1, 'internal', '_0XX', 1, 'Dial', 'SIP/${EXTEN}|20|rT');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (2, 'internal', '_0XX', 2, 'Voicemail', '${EXTEN}@${CONTEXT}');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (5, 'internal', '_0XX', 4, 'HangUp', NULL);
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (4, 'internal', '_0XX', 3, 'Playback', 'vm-goodbye');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (6, 'internal', '*98', 1, 'VoiceMailMain', '${CALLERIDNUM}@${CONTEXT}');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (7, 'internal', '_*99XXZ', 1, 'Goto', 'conferences|${EXTEN:3}|1');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (8, 'internal', '*97', 1, 'Answer', NULL);
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (9, 'internal', '*97', 2, 'Playback', 'please-enter-the');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (10, 'internal', '*97', 3, 'Playback', 'extension');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (11, 'internal', '*97', 4, 'Playback', 'within');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (12, 'internal', '*97', 5, 'Playback', 'digits/15');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (13, 'internal', '*97', 6, 'Playback', 'seconds');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (14, 'internal', '*97', 7, 'WaitExten', '15');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (15, 'internal', '999', 1, 'Answer', NULL);
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (16, 'internal', '999', 2, 'Echo', NULL);
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (18, 'internal', '_9.', 2, 'HangUp', NULL);
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (17, 'internal', '_9.', 1, 'Dial', 'SIP/${EXTEN:1}@sip.messagenet.it-out|20|rT');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (20, 'mn-incoming', 'mn-incoming-ext', 1, 'Goto', 'internal|*97|1');
INSERT INTO extension (id, context, exten, priority, app, appdata) VALUES (21, 'conferences', '_XXZ', 1, 'Conference', '${EXTEN}/S/1');


--
-- Data for Name: iax; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: queue; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: queue_member; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- Data for Name: sip; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO sip (id, name, username, accountcode, "type", secret, md5secret, dbsecret, auth, notransfer, inkeys, outkeys, nat, port, host, ipaddr, defaultip, insecure, qualify, disallow, allow, regseconds, amaflags, callgroup, callerid, canreinvite, context, dtmfmode, fromuser, fromdomain, "language", mailbox, permit, deny, mask, pickupgroup, restrictcid, rtptimeout, rtpholdtimeout, musiconhold, regexten, cancallforward) VALUES (2, 'sip.messagenet.it-out', '5317936', '5317936', 'peer', 'megidik', NULL, NULL, '', 'yes', NULL, NULL, 'yes', '5061', 'sip.messagenet.it', '', '', 'very', 'yes', 'all', 'g729;ilbc;gsm;ulaw;alaw', 3, NULL, NULL, NULL, 'no', 'mn-incoming', NULL, NULL, NULL, 'en', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'yes');
INSERT INTO sip (id, name, username, accountcode, "type", secret, md5secret, dbsecret, auth, notransfer, inkeys, outkeys, nat, port, host, ipaddr, defaultip, insecure, qualify, disallow, allow, regseconds, amaflags, callgroup, callerid, canreinvite, context, dtmfmode, fromuser, fromdomain, "language", mailbox, permit, deny, mask, pickupgroup, restrictcid, rtptimeout, rtpholdtimeout, musiconhold, regexten, cancallforward) VALUES (3, '008', '008', '008', 'friend', '8000', NULL, NULL, '', 'yes', NULL, NULL, 'yes', '0', 'dynamic', '0.0.0.0', '', 'very', 'yes', 'all', 'g729;ilbc;gsm;ulaw;alaw', 1150640604, NULL, NULL, NULL, 'yes', 'internal', NULL, NULL, NULL, 'en', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'yes');
INSERT INTO sip (id, name, username, accountcode, "type", secret, md5secret, dbsecret, auth, notransfer, inkeys, outkeys, nat, port, host, ipaddr, defaultip, insecure, qualify, disallow, allow, regseconds, amaflags, callgroup, callerid, canreinvite, context, dtmfmode, fromuser, fromdomain, "language", mailbox, permit, deny, mask, pickupgroup, restrictcid, rtptimeout, rtpholdtimeout, musiconhold, regexten, cancallforward) VALUES (1, '009', '009', '009', 'friend', '9000', 'f46f59ab760a4f602e7600cb36185e65', NULL, 'md5', 'yes', NULL, NULL, 'yes', '0', 'dynamic', '0.0.0.0', '', 'very', 'yes', 'all', 'g729;ilbc;gsm;ulaw;alaw', 1150726130, NULL, NULL, NULL, 'yes', 'internal', NULL, NULL, NULL, 'en', NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, NULL, '', 'yes');


--
-- Data for Name: voicemail; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO voicemail (id, customer_id, context, mailbox, "password", fullname, email, pager, stamp, tz, attach, saycid, dialout, callback, review, "operator", envelope, sayduration, saydurationm, sendvoicemail, "delete", nextaftercmd, forcename, forcegreetings, hidefromdir) VALUES (1, 0, 'internal', '008', '8000', 'Ivan De Marino', 'ivan.de.marino@gmail.com', '', '2006-06-18 15:10:39.132369+01', 'central', 'yes', 'yes', '', '', 'no', 'no', 'no', 'no', 1, 'no', 'no', 'yes', 'no', 'no', 'yes');
INSERT INTO voicemail (id, customer_id, context, mailbox, "password", fullname, email, pager, stamp, tz, attach, saycid, dialout, callback, review, "operator", envelope, sayduration, saydurationm, sendvoicemail, "delete", nextaftercmd, forcename, forcegreetings, hidefromdir) VALUES (2, 0, 'internal', '009', '9000', 'Detro', 'detronizator@gmail.com', '', '2006-06-18 15:10:57.937276+01', 'central', 'yes', 'yes', '', '', 'no', 'no', 'no', 'no', 1, 'no', 'no', 'yes', 'no', 'no', 'yes');


--
-- Name: asterisk_configuration_filename_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY asterisk_configuration
    ADD CONSTRAINT asterisk_configuration_filename_key UNIQUE (filename, category, var_name);


--
-- Name: asterisk_configuration_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY asterisk_configuration
    ADD CONSTRAINT asterisk_configuration_pkey PRIMARY KEY (id);


--
-- Name: cdr_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY cdr
    ADD CONSTRAINT cdr_pkey PRIMARY KEY (id);


--
-- Name: extension_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extension
    ADD CONSTRAINT extension_id_key UNIQUE (id);


--
-- Name: extension_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY extension
    ADD CONSTRAINT extension_pkey PRIMARY KEY (context, exten, priority);


--
-- Name: iax_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY iax
    ADD CONSTRAINT iax_id_key UNIQUE (id);


--
-- Name: iax_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY iax
    ADD CONSTRAINT iax_pkey PRIMARY KEY (username, id);


--
-- Name: queue_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_id_key UNIQUE (id);


--
-- Name: queue_member_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue_member
    ADD CONSTRAINT queue_member_id_key UNIQUE (id);


--
-- Name: queue_member_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue_member
    ADD CONSTRAINT queue_member_pkey PRIMARY KEY (queue_name, interface);


--
-- Name: queue_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY queue
    ADD CONSTRAINT queue_pkey PRIMARY KEY (name);


--
-- Name: sip_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sip
    ADD CONSTRAINT sip_id_key UNIQUE (id);


--
-- Name: sip_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY sip
    ADD CONSTRAINT sip_pkey PRIMARY KEY (username, id);


--
-- Name: voicemail_id_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY voicemail
    ADD CONSTRAINT voicemail_id_key UNIQUE (id);


--
-- Name: voicemail_mailbox_key; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY voicemail
    ADD CONSTRAINT voicemail_mailbox_key UNIQUE (mailbox, context);


--
-- Name: voicemail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres; Tablespace: 
--

ALTER TABLE ONLY voicemail
    ADD CONSTRAINT voicemail_pkey PRIMARY KEY (mailbox, id, context);


--
-- Name: asterisk_configuration_filename_category_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX asterisk_configuration_filename_category_idx ON asterisk_configuration USING btree (filename, category);


--
-- Name: asterisk_configuration_filename_idx; Type: INDEX; Schema: public; Owner: postgres; Tablespace: 
--

CREATE INDEX asterisk_configuration_filename_idx ON asterisk_configuration USING btree (filename);


--
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


--
-- Name: asterisk_configuration; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE asterisk_configuration FROM PUBLIC;
REVOKE ALL ON TABLE asterisk_configuration FROM postgres;
GRANT ALL ON TABLE asterisk_configuration TO postgres;
GRANT ALL ON TABLE asterisk_configuration TO asterisk_user;


--
-- Name: asterisk_configuration_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE asterisk_configuration_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE asterisk_configuration_id_seq FROM postgres;
GRANT ALL ON TABLE asterisk_configuration_id_seq TO postgres;
GRANT ALL ON TABLE asterisk_configuration_id_seq TO asterisk_user;


--
-- Name: cdr; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cdr FROM PUBLIC;
REVOKE ALL ON TABLE cdr FROM postgres;
GRANT ALL ON TABLE cdr TO postgres;
GRANT ALL ON TABLE cdr TO asterisk_user;


--
-- Name: cdr_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE cdr_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE cdr_id_seq FROM postgres;
GRANT ALL ON TABLE cdr_id_seq TO postgres;
GRANT ALL ON TABLE cdr_id_seq TO asterisk_user;


--
-- Name: extension; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE extension FROM PUBLIC;
REVOKE ALL ON TABLE extension FROM postgres;
GRANT ALL ON TABLE extension TO postgres;
GRANT ALL ON TABLE extension TO asterisk_user;


--
-- Name: extension_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE extension_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE extension_id_seq FROM postgres;
GRANT ALL ON TABLE extension_id_seq TO postgres;
GRANT ALL ON TABLE extension_id_seq TO asterisk_user;


--
-- Name: iax; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE iax FROM PUBLIC;
REVOKE ALL ON TABLE iax FROM postgres;
GRANT ALL ON TABLE iax TO postgres;
GRANT ALL ON TABLE iax TO asterisk_user;


--
-- Name: iax_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE iax_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE iax_id_seq FROM postgres;
GRANT ALL ON TABLE iax_id_seq TO postgres;
GRANT ALL ON TABLE iax_id_seq TO asterisk_user;


--
-- Name: queue; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue FROM PUBLIC;
REVOKE ALL ON TABLE queue FROM postgres;
GRANT ALL ON TABLE queue TO postgres;
GRANT ALL ON TABLE queue TO asterisk_user;


--
-- Name: queue_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE queue_id_seq FROM postgres;
GRANT ALL ON TABLE queue_id_seq TO postgres;
GRANT ALL ON TABLE queue_id_seq TO asterisk_user;


--
-- Name: queue_member; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue_member FROM PUBLIC;
REVOKE ALL ON TABLE queue_member FROM postgres;
GRANT ALL ON TABLE queue_member TO postgres;
GRANT ALL ON TABLE queue_member TO asterisk_user;


--
-- Name: queue_member_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE queue_member_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE queue_member_id_seq FROM postgres;
GRANT ALL ON TABLE queue_member_id_seq TO postgres;
GRANT ALL ON TABLE queue_member_id_seq TO asterisk_user;


--
-- Name: sip; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sip FROM PUBLIC;
REVOKE ALL ON TABLE sip FROM postgres;
GRANT ALL ON TABLE sip TO postgres;
GRANT ALL ON TABLE sip TO asterisk_user;


--
-- Name: sip_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE sip_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE sip_id_seq FROM postgres;
GRANT ALL ON TABLE sip_id_seq TO postgres;
GRANT ALL ON TABLE sip_id_seq TO asterisk_user;


--
-- Name: voicemail; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE voicemail FROM PUBLIC;
REVOKE ALL ON TABLE voicemail FROM postgres;
GRANT ALL ON TABLE voicemail TO postgres;
GRANT ALL ON TABLE voicemail TO asterisk_user;


--
-- Name: voicemail_id_seq; Type: ACL; Schema: public; Owner: postgres
--

REVOKE ALL ON TABLE voicemail_id_seq FROM PUBLIC;
REVOKE ALL ON TABLE voicemail_id_seq FROM postgres;
GRANT ALL ON TABLE voicemail_id_seq TO postgres;
GRANT ALL ON TABLE voicemail_id_seq TO asterisk_user;


--
-- PostgreSQL database dump complete
--

