--
-- PostgreSQL database dump
--

-- Dumped from database version 10.8 (Ubuntu 10.8-0ubuntu0.18.04.1)
-- Dumped by pg_dump version 10.8 (Ubuntu 10.8-0ubuntu0.18.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

--
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: -
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: author; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.author (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    name character varying(255)
);


--
-- Name: book; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    asset_id character varying(255),
    last_tx_id character varying(255),
    status character varying(255),
    bookdetail_id bigint,
    user_id bigint,
    transfer_status character varying(255)
);


--
-- Name: book_author; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_author (
    book_id bigint NOT NULL,
    author_id bigint NOT NULL
);


--
-- Name: book_category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_category (
    book_id bigint NOT NULL,
    category_id bigint NOT NULL
);


--
-- Name: book_detail; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_detail (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    name character varying(255),
    publisher_id bigint
);


--
-- Name: book_requests; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_requests (
    book_id bigint NOT NULL,
    requests_id bigint NOT NULL
);


--
-- Name: book_transactions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.book_transactions (
    book_id bigint NOT NULL,
    transactions_id bigint NOT NULL
);


--
-- Name: category; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    name character varying(255)
);


--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: image_url; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.image_url (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    url character varying(255),
    image_id bigint
);


--
-- Name: image_url_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.image_url_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: image_url_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.image_url_id_seq OWNED BY public.image_url.id;


--
-- Name: matching; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.matching (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    matching_start_date timestamp without time zone,
    pin character varying(255),
    status integer,
    book_id bigint,
    borrower_request_id bigint,
    returner_request_id bigint
);


--
-- Name: matching_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.matching_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: matching_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.matching_id_seq OWNED BY public.matching.id;


--
-- Name: publisher; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.publisher (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    name character varying(255)
);


--
-- Name: publisher_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.publisher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: publisher_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.publisher_id_seq OWNED BY public.publisher.id;


--
-- Name: request; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.request (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    status integer,
    type integer,
    book_id bigint,
    book_detail_id bigint,
    paired_user bigint,
    user_id bigint,
    mode integer
);


--
-- Name: request_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.request_id_seq OWNED BY public.request.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    role_name character varying(255)
);


--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- Name: tbl_user; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tbl_user (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    email character varying(255),
    fullname character varying(255),
    password character varying(255),
    phone character varying(255),
    is_disabled boolean
);


--
-- Name: tbl_user_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.tbl_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: tbl_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.tbl_user_id_seq OWNED BY public.tbl_user.id;


--
-- Name: transaction; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.transaction (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    update_date timestamp without time zone,
    bc_transaction_id character varying(255),
    book_id bigint,
    borrower_id bigint,
    returner_id bigint
);


--
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.transaction_id_seq OWNED BY public.transaction.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


--
-- Name: category id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- Name: image_url id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.image_url ALTER COLUMN id SET DEFAULT nextval('public.image_url_id_seq'::regclass);


--
-- Name: matching id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matching ALTER COLUMN id SET DEFAULT nextval('public.matching_id_seq'::regclass);


--
-- Name: publisher id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.publisher ALTER COLUMN id SET DEFAULT nextval('public.publisher_id_seq'::regclass);


--
-- Name: request id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request ALTER COLUMN id SET DEFAULT nextval('public.request_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- Name: tbl_user id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user ALTER COLUMN id SET DEFAULT nextval('public.tbl_user_id_seq'::regclass);


--
-- Name: transaction id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transaction ALTER COLUMN id SET DEFAULT nextval('public.transaction_id_seq'::regclass);


--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.author (id, created_date, update_date, name) FROM stdin;
1	2019-06-05 17:17:08.077	2019-06-05 17:17:08.077	Adam Licol
2	2019-06-05 17:17:08.098	2019-06-05 17:17:08.098	John Thanos
3	2019-06-05 17:17:08.102	2019-06-05 17:17:08.102	Alex Man
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book (id, created_date, update_date, asset_id, last_tx_id, status, bookdetail_id, user_id, transfer_status) FROM stdin;
9	2019-06-05 17:17:59.846	2019-06-11 02:58:59.45	2b2762f6215852ad73764b812e7c2059be4a9b6bc1b0cea585cac60776400106	2fe6950e74b12dbb0ddeaf1a43e2a96bb0148626c2a75797f41f060464a7986f	in use	4	4	transferring
98	2019-06-05 17:17:59.953	2019-06-11 03:05:33.815	5cf5f51e82a76fd0644cc35e41c8efa67f1b7c96c644cb4071a5407106163503	9fb3f9bba5bbc63257d070953cee12cda398b406dd236a5588ec469dd71142d3	in use	13	43	transferring
1	2019-06-05 17:17:59.836	2019-06-11 03:15:20.323	2430b79f82ab52de07985fb5c2c4863b978903ec5540326b672dbcbbb813499a	c59a3aa6ca0cf15227c63331f17eb268c321de94ddf58f121126b58a4c6381d2	in use	4	41	transferring
47	2019-06-05 17:17:59.887	2019-06-11 04:10:59.182	d5ba761456e8e861aed164030f115f7a1a67910a6b1ab2bccc63d966d1e3a28b	0a8cc3200b873e17656d6936278e1a37cf72ac3a433377e60a1fc7ef49a6d423	in use	8	48	transferring
43	2019-06-05 17:17:59.883	2019-06-10 16:46:45.979	78cc0a0d4ee7a0a70732ab4c25b9238b6307bc7b0b1845f1e0607053296b7142	3d3c6743022686760d916936193877ad4bdc276be54bb82667e7e78bb1b19c71	in use	8	4	transferred
39	2019-06-05 17:17:59.878	2019-06-11 04:26:17.494	dd9efc10546c0dc80c8cd39dcf62cfb509a24a3c2051f7625f481857c486b913	417adeeb4396fb0bf00e37e110bcff381922d0ef2114d6719d5cc01c2982366c	in use	7	4	transferred
4	2019-06-05 17:17:59.84	2019-06-10 18:35:31.706	58263fd66d4ec69b5a196ec0e8effc68c817c707903978ddc736c5bc96b75103	fee9571354c5250ae9af37abed0a51278859141b508d8be46b7ef82279fdd41e	in use	4	18	transferred
65	2019-06-05 17:17:59.919	2019-06-11 03:23:17.11	94749480f20234da989cda17ba546ca371a25197668f257a16b22cf62aa9729a	8151d272f3f68211591303c9e2532af6b38c1888979fe0c5c781c5bf70b76c4e	in use	10	44	transferring
42	2019-06-05 17:17:59.882	2019-06-11 04:44:46.85	9e04480e465bc8dcee638cfcc1aef32a56266c014c666f1cf5a7430d6ec52708	94986e70d6f98b40b22be11020af2f7f73f3bcd4e643c3c2b3888d52c89e62b0	in use	8	50	transferring
52	2019-06-05 17:17:59.895	2019-06-11 03:40:56.705	5dc8c0b0a7aa6750d6423db19c7e096face818e98c149dafe5b87faa9d44b66c	c4320834d22f7af9b16a8afd0d330de861366cbb8a6de8d7b0c14666b3dae476	in use	9	45	transferring
54	2019-06-05 17:17:59.899	2019-06-11 03:49:10.623	d0bfabac22594c3e7dcd60d0c04bdc5c3d360febf7863c08d15ce900d0b44dab	1ca2e36e1626dd37638bddbc078f728b2e3447316739a07ee045e68bd7d61683	in use	9	46	transferred
45	2019-06-05 17:17:59.885	2019-06-11 03:55:51.037	6a4cd5cb1ae2cf522890f21ed74c2cf3feac6b14a7a1de7550d04d1d8b530754	40cf6af14759c2eb0df02f87fe8812ef47906965fff7390646c11f38cf8122dd	in use	8	47	transferring
48	2019-06-05 17:17:59.888	2019-06-09 15:30:20.571	122d2cd3ea2056cb06b769b758e56c9fce9782ae065727994a689dbc68a20338	122d2cd3ea2056cb06b769b758e56c9fce9782ae065727994a689dbc68a20338	in use	8	1	transferred
61	2019-06-05 17:17:59.91	2019-06-10 10:13:55.176	50de097957c6e8a0dcafff77023aa22f98be41405d92a96e80a5a5887b4d015b	0f147bb3491d2b9e8285c14b75f6d6bcc3b980ff40fe243d5b08e17f5c2e1168	in use	10	2	transferring
11	2019-06-05 17:17:59.848	2019-06-10 16:41:14.316	504d23a658fa39ec158c3083222d108609f681b54c787005dd47335413de4d06	eef43ea6ca5c14a3a051c045488ece70738e61a47ee40a2638e0dfa14511075a	in use	5	4	transferred
70	2019-06-05 17:17:59.924	2019-06-10 16:41:37.793	bf9d134ff797e90715dfb276a028e8de758c07b74e1c947d5c017e5a4fcb2839	be9248e8e1941694987b132822301d50ce3aea471463343cbd933d92c761a236	in use	10	4	transferred
67	2019-06-05 17:17:59.921	2019-06-10 16:46:42.783	9f78478c3fa9f6ceab755eb1a9387ce68dd42f253da949d35101b5a67b3b8fe3	f0879714ad6d692ac120840534eebca17ce4cd0a2b7f6cf7ee70ffa7578d7e69	in use	10	4	transferred
66	2019-06-05 17:17:59.92	2019-06-06 04:22:49.727	79a53faedf396e68e0648f9f090ea7cd30df877f851746fb290fbbb43e82492b	eba1397cc7f087f26251cf1389423ccd394d3ef523b185eb24243166977a9635	in use	10	4	transferred
22	2019-06-05 17:17:59.86	2019-06-09 15:30:18.232	5fa3dc1ed50170b4e99844a03dd8e4a0e75c15aaabc373bc14f914e2f46cb792	5fa3dc1ed50170b4e99844a03dd8e4a0e75c15aaabc373bc14f914e2f46cb792	in use	6	1	transferred
29	2019-06-05 17:17:59.867	2019-06-09 15:30:18.709	a06694f4738704df633529d33c419b7ca95f24421251f269367353c5ef16f6c0	752860add23b64530843a1718d17960760492a94a5601f4ae679c83608175a07	in use	6	4	transferred
15	2019-06-05 17:17:59.853	2019-06-09 15:30:18.766	6c8f0e8592dd6b6fb93ec52687b311d7b39e45cf023fadcc1014f445e6cb3c19	69e0c6ccd915e2a07c53e0732fc7be601a915bf617b31d007221f761308bfef1	in use	5	4	transferred
38	2019-06-05 17:17:59.877	2019-06-09 15:30:19.11	aaa7504ca7094be6a328e897d3a2f73887a4cf5b2c5e5a3ad967c39ab191c9be	70eed95d22eeb2dcc200fbf162a6cab4fe6de83dd39e339693ae8942987d3888	in use	7	4	transferred
34	2019-06-05 17:17:59.873	2019-06-09 15:30:19.228	505c583cc737864f65bd1184c345c6913abe3b60b3977a17f416f0e47649100d	1174ef60e4bbb096b7ad699dcaca0b750f83d3e86a6638093c4b14a768dbecb8	in use	7	18	transferred
7	2019-06-05 17:17:59.844	2019-06-09 15:30:19.286	49175dae83a454c4132dc0f6d60ad560d72363d3f5d748f18593084f78f4bd2d	962274c3e8d6564b6160b43068ddb68e8d57c5d2d0cf5ef68eda906920ca8b63	in use	4	28	transferred
20	2019-06-05 17:17:59.858	2019-06-09 15:30:19.344	b3f25614abcaa3f071a7fe5bfa849d4340d336cb11c0ad93d13b856417329af4	1282f98a59c18aae954884efa9d4f23ffeaee3e2ffaa0cb98671fc627b2d0680	in use	5	28	transferred
5	2019-06-05 17:17:59.841	2019-06-09 15:30:19.401	26f3f0687470cd2345ce0d6c46a1ebb2af0155b79a1e78d837f2f0f51af60b0f	5fc191d16c0831130c0af1f490d1c4d8358a75f4992c9c2c2f9699880f7b12ca	in use	4	28	transferred
16	2019-06-05 17:17:59.853	2019-06-09 15:30:19.462	2ebc3a880642d6ab33a69332f8b160960cc46a8a8b2641023c169d16e4730780	453cb7b7664ba61438109ac050f035d0a36658bda9b95ebede80747f002ee5ec	in use	5	18	transferred
14	2019-06-05 17:17:59.851	2019-06-09 15:30:19.52	dc88d6e30e953109cfec4a85e783a6c3e219726d82c7884aea8b6fa598772594	e6839e2002cbbd1a2ab625f79611b9530858d7bf63d3f18462f9a1d44c53155c	in use	5	28	transferred
8	2019-06-05 17:17:59.845	2019-06-09 15:30:19.577	2b87eb1007f3eaf88047f54f497438462838d78d217945df7c865dce5b59965c	7edffcd21b5cba58d8a1144527475e5408f31599fd2af02174eefe8fe7b26d66	in use	4	28	transferred
3	2019-06-05 17:17:59.839	2019-06-09 15:30:19.634	756e237d492561ef2005c524551fa2cd68b37bcb5c14c75f740d19c2b4b40adf	caa999afc25dcf82fc54cd81e5f21817c5d7b3e1f6f16d77e106d1303d02772c	in use	4	28	transferred
19	2019-06-05 17:17:59.857	2019-06-09 15:30:19.693	babf24a69b2d6c18ff406acff47e873e1d9f722f266065a81ab9ff98155d0a1f	d6373f84242a9ebcbe76b08189a46448ba67df3e4a2f6ea2e2c1b88245eeed03	in use	5	28	transferred
26	2019-06-05 17:17:59.864	2019-06-09 15:30:19.751	8e12bc87bd5780a98bafb6e9fe54a0eb12afec9346de5e0827dbc179205d0a72	05aedce04b7f316b8e75004004628220d8665008251846a6f006d5e535a9e1a5	in use	6	18	transferred
6	2019-06-05 17:17:59.843	2019-06-09 15:30:20.219	b2fd43bb316b1bb559482aec9e8580173f14aa108a32d4d7ce8bcab77f20492f	aefd870537b77a3edf617c0412c484b3b961bf5339aa20d737c256536909a867	in use	4	18	transferred
56	2019-06-05 17:17:59.902	2019-06-09 15:30:20.748	2274f77d21221e5be56549a2c8564ad78b6f75ebbb9a1995a5b76a0a74ebe15a	2274f77d21221e5be56549a2c8564ad78b6f75ebbb9a1995a5b76a0a74ebe15a	in use	9	1	transferred
59	2019-06-05 17:17:59.907	2019-06-09 15:30:20.863	8bdaf4554fb7ae70fa5de293f44d7b78362f60664081aa01669a8720fa6452c1	8bdaf4554fb7ae70fa5de293f44d7b78362f60664081aa01669a8720fa6452c1	in use	9	1	transferred
17	2019-06-05 17:17:59.854	2019-06-11 04:29:51.372	119c1b2d515747968dd69691798c758f4ec9de47052b354a0302eac2b7021ef9	3eb4f86d6f4c7903fff79f3da30fc6edea257b60ea2321fb5fa3e140e4670d62	in use	5	4	transferring
33	2019-06-05 17:17:59.872	2019-06-10 16:41:10.356	a57cc3bcdf498f1c9cbfbc24fe199908e670bf434dd1c7251879af9b8a0fb76d	fe79f3c24e6af3625273daeeb605ed3374afe6018af4cb2bffeb92cb8844554b	in use	7	4	transferred
37	2019-06-05 17:17:59.876	2019-06-10 16:23:32.028	d66657c8508fbe9418e528a060a893c68bb4b81a8573eab79e00733a4ca829ad	eda26b087a8a96f601ca044a9f31e30f7367dee6dbb66b113862c6813e057149	in use	7	5	transferred
12	2019-06-05 17:17:59.849	2019-06-11 22:37:44.586	a974335c651206f06215978c06635e59368416f922405ccca0784648ff5e1f2f	3c934e735f58975126f26b44da2014c31d6583ce67f3b546c280e9176db22e3c	in use	5	2	transferring
25	2019-06-05 17:17:59.863	2019-06-11 05:35:06.441	ba8c3cd816e554e5c36118cefaa23df1818760b7fb831c16dbdbfd3e0aa94ab3	e9ffa594008fabd2b91e54f718536c1230c0cc93924387f3280e544306152c27	in use	6	44	transferred
18	2019-06-05 17:17:59.856	2019-06-10 16:41:12.605	bda9fd5a091ae923db586fbe899400a4fd4bc64619ed891db97b270987d13f2a	286c276a0574b4a88ab58a274010f96b2852cd55331dd2dcbea1c77ff04bf836	in use	5	4	transferred
35	2019-06-05 17:17:59.874	2019-06-10 16:41:29.09	215106bae6323e154bdba84cbf559f2a93cd96e3c3268c30c4554e6f8b054fae	56ec2785b4b6be0a03558acbd51e794bad80262ab0b5e3798484146b267900b9	in use	7	4	transferred
24	2019-06-05 17:17:59.862	2019-06-10 16:41:30.647	b7e54db5ffec7bda1e50baafe6ed06c8b12f97a2275a062c2c12615ba369239b	c1eac2e3a0d715df12ad42723a0bd165343407780080a4535a16cf43698cdf0a	in use	6	4	transferred
57	2019-06-05 17:17:59.904	2019-06-10 16:41:31.188	b1f182b619fcd678a8dcfcb5dba132c8ac69cc555c961a18a1e368b8e25796c9	5bc5b3459b320008ba2ca9b45f12b333b10e5c457d16e2065668d7171f8db13c	in use	9	4	transferred
30	2019-06-05 17:17:59.869	2019-06-10 16:41:39.423	039f9f8799a2021907f4e6c90ae8e940074d809ec6576f189be42d1a7615fccf	e30676a24870b597840ce476aa3aff21852e3726cb299aa2d6881ef06624f2ca	in use	6	4	transferred
32	2019-06-05 17:17:59.871	2019-06-10 16:41:41.058	38f7567f91dde498d9b526b326c67afa65dca2bfc64720eca7a4386ca7863232	0981d602e929293b3263b55990ac21b07283867e2ee16cb18a9983ea286abd10	in use	7	4	transferred
2	2019-06-05 17:17:59.838	2019-06-11 05:26:31.876	ce65d9f16c3ad48a594d5ac4f588d3fba68a62554ea404be34dd40b9f46e0100	fc991d42e9ac5f2418fb246d8231591d55cef5c82d44ce7d45062f66bc1a44df	in use	4	51	transferring
31	2019-06-05 17:17:59.869	2019-06-10 16:46:44.912	170c317d33b9a7f2bf7de5390fc2fc31e09ccaa0f9d23d46dbe8956b39dc55e2	a42c70b6ee4518d63e42a3b4326b74d90f46b565f25c64dfe84f0de3d87e85a1	in use	7	4	transferred
27	2019-06-05 17:17:59.865	2019-06-10 16:46:46.498	d3cc668f9b16a99cdba2a18788ce0b3514a9cfb4fc3ffec39f106c421fdb946e	6688558bab3dae3d2fbf986fdb7fe53cc78a45695cdb448544a0063d9ab40952	in use	6	4	transferred
10	2019-06-05 17:17:59.847	2019-06-11 03:01:01.8	05f53bed304fbdf92d5e0c83d83555721a1771c2d9458a298e9b8d64a2ad2fad	d95da34a4a4919dc03213f5a56b1be8e2414ffcacbd5f5edec2cd8b883eb28e0	in use	4	43	transferred
13	2019-06-05 17:17:59.85	2019-06-11 02:44:38.5	01c429a7313d427d0b2a6f55cb9f79e7780a0b8e2f685702dbc212cf77da510f	01c429a7313d427d0b2a6f55cb9f79e7780a0b8e2f685702dbc212cf77da510f	in use	5	21	transferred
21	2019-06-05 17:17:59.859	2019-06-11 05:36:02.055	68fd0ff4586452b905fb9b4846a44f601abaad9e5ec1102f2aa8d48bf670fdb5	57ab0f3c48f39365beca795bf0c7870a2855a6c0ea4fb56934615b2b1dd8c1cc	in use	6	2	transferred
76	2019-06-05 17:17:59.93	2019-06-09 15:30:21.037	2894de3bab7ece3dc3477df659bab0c0fd40e9d8a0d12879cdc341f157efc042	2894de3bab7ece3dc3477df659bab0c0fd40e9d8a0d12879cdc341f157efc042	in use	11	1	transferred
60	2019-06-05 17:17:59.908	2019-06-09 15:30:21.094	67afb305b3ec508f30126e7084f4731b53236f0858daf4d4440fc5da34ead21b	fe5753fecdd184e81f1fac95d06bf91c454cbf4dbf5bcda93e189bcceab05f6c	in use	9	4	transferred
46	2019-06-05 17:17:59.886	2019-06-09 15:30:21.151	ad7c51a2bc8466b6795ab9b0e48f3c88a3a0dc60b58ac3bf4bbf9575c88986c4	7acc09928670002631bc0e74a3afeb0bc770ec4dac015d9e877ccfa93635fb24	in use	8	28	transferred
55	2019-06-05 17:17:59.9	2019-06-09 15:30:21.268	0f14076b5f0b24b67c310c22654622009091e3dd4547973024016bc89ce58b72	2e53396054c6e403aa3f078b540aeaefb23540d89b2759986d07e73d6ec30f4c	in use	9	3	transferred
75	2019-06-05 17:17:59.929	2019-06-09 15:30:21.325	dd329bc0221a8985250b7d5adee22dc6e9591860334f516b2a63df742aed296e	d14562ff52fe734bba8193b59a35541dd4b82b82791f5a8fd8bb4b046b8366b7	in use	11	4	transferred
50	2019-06-05 17:17:59.891	2019-06-09 15:30:21.384	980c62af35ca0e60134992427ea664dab31c4cacd394a40ffb960e59e0c9b36b	8276253d59fb633c64c8061dcd467277baca5d2a231a0d4e34792e6e96ae89b4	in use	8	2	transferred
40	2019-06-05 17:17:59.88	2019-06-09 15:30:21.443	d7703d77a4f74fc57f4fd23d49010e7f2876596ba9c0bf69592ba2cefa4eea46	03f06dee574366c839e8bd381646df550283cc323d47f32d6da737c4780bc17d	in use	7	28	transferred
71	2019-06-05 17:17:59.925	2019-06-09 15:30:21.502	5d82412e2688237f87a9da4a04b747bb0644f7bb57196c077f3bc4134c0c55f3	23835e767523079c8af9fa601ff8f13fce09665d3ccda69b16a43f3f21f65d55	in use	11	28	transferred
68	2019-06-05 17:17:59.922	2019-06-09 15:30:21.559	1f109b087938f9138ccb442cde334e31e3f2b40251652a5ff5118a4f43b7ae20	c87e0caa23a2a8a44e074ce38cd311dc927d3155d2b9bc249deae988747c3d3d	in use	10	2	transferred
49	2019-06-05 17:17:59.89	2019-06-09 15:30:21.618	a1b3db925362d647e9f5f5abf23f8e8f1ddf172d7b7f3efe163f691e794a48d6	ed8d1be50ad9df575d819d0ba3f2f70d64f1ee21ad61d586b4db9de4ef5fdcae	in use	8	2	transferred
41	2019-06-05 17:17:59.881	2019-06-09 15:30:21.676	cf36cef62004c0faddeca0ad847dab97778c1ff3dcfc135fc03a85110495020a	1eaaf8d9f2829920bd6925b09889873ddd761417b54ead957767e6748593bcf5	in use	8	2	transferred
73	2019-06-05 17:17:59.927	2019-06-09 15:30:21.733	3d8b8e43a0a61ec5416ddc1c26816e0c5c26295cb06b1d014bc227f17ff29105	03be85e37eb18aeb73b12bb65a09612df7faa036955830738d9c7b6ca89b9669	in use	11	2	transferred
53	2019-06-05 17:17:59.897	2019-06-09 15:30:21.79	c6bb6cea0ab26f90b0181b364782fa4f4edc6020c6025cf097f03118d900e6e5	2e859cf041bd841e1c7a01b574a0eea221c89f39807eaf8f273acd38989f1350	in use	9	2	transferred
58	2019-06-05 17:17:59.906	2019-06-09 15:30:21.848	0ce94168a00834076f1b524160f855300dcc7e791dea71c844629d802fa9ad11	958af018fcc7945af5a7c8e0a24f1aa5bb4ad476b1f82cf4197b2c8e1a0e2020	in use	9	2	transferred
82	2019-06-05 17:17:59.936	2019-06-09 15:30:22.676	05bb5fff47b9543e2f37ea7ef1b04f58c8bffba22f2b7c02be945d5a72344af8	4e2337d089b5e3fd38749b030969e6eba2b53893390f36ba6d2a2959df76e328	in use	12	2	transferred
36	2019-06-05 17:17:59.875	2019-06-09 15:30:22.798	2b2e90b7ffc77c2525142cc01a7f64552cbe41a135a64f257c1dca8a94039c5c	58fd076931c1918121255ba540a48e85e4b7273bd77776a2773701f3f2a86adf	in use	7	4	transferred
77	2019-06-05 17:17:59.931	2019-06-09 15:30:22.859	63be4b2e5f03aba38c1941580586182b6cf08f1e8bfbe73c79940f7532940f9a	c7ec21836aaec9040a80d8a507f1baa1c881fa5adfb7bc8f61f5756ab4ad4eac	in use	11	2	transferred
88	2019-06-05 17:17:59.942	2019-06-09 15:30:22.917	0ca663bec7d972ad427c0989992bd2edbc643774cf23d8f6b02bf90c4ee5349b	f8bc7cf9af9f32d7cbb22e30e3b591b28f17a304900baef8bdca2242147c47e6	in use	12	28	transferred
86	2019-06-05 17:17:59.94	2019-06-09 15:30:22.977	88832e3f509bd4597a54987dc2f608c6521de0486e8b53321dedf64a192c6c61	ca7895e34adc6872cc8205faccc2459091edc83c6021e1ca1a2d81b5f45e1bc3	in use	12	4	transferred
79	2019-06-05 17:17:59.933	2019-06-10 16:41:09.269	e902c3e1befa5703a756702863378f2066cb9636f51de826b2d5e01acf0245a9	06be97089d51fa88670d29cb4b091bc52503342e0e084b0ef8ab2f5c42e0a808	in use	11	4	transferred
91	2019-06-05 17:17:59.945	2019-06-10 16:41:11.472	d2ab98e4b0cd8e98f2b355b62f4b082b6fd937ccaed384840f7c143ba4ffae86	506f14a27eb10b4a367442db096e7eb3bfd408aeec15090d9fc83a8358c8629e	in use	13	4	transferred
100	2019-06-05 17:17:59.955	2019-06-10 16:41:29.591	67202e8c9ae6d84a5c5d9218ca3e5b40abaaded96d06fb896bf3100d13b84c4b	492191e4da6287e62bd6348427ddcfc32abf4e5adc3be08cbfe89421ee1fce54	in use	13	4	transferred
83	2019-06-05 17:17:59.937	2019-06-10 16:41:31.832	b5cbfa08ebaa919ee02445a7936f8c91acb3d99a6c435bcb084cdb72debc3a57	fab0e12f02d8e2cdc568ded487d856218701584363104105358e33318a4e56c9	in use	12	4	transferred
93	2019-06-05 17:17:59.947	2019-06-10 16:41:33.382	08da8698733efa9f5a83ffb2079e6a04179f7ea50ca6abf2c1d67e153c4a881a	760fde109606eefc9ba23a4c430a0c216423baf1d890192b11646bb5bb0268bb	in use	13	4	transferred
89	2019-06-05 17:17:59.943	2019-06-10 16:41:33.91	69055f3d1ed3718a7c7b24f6f52c02b45fda766d89153405f4e61ac36a68528a	51afd6b3b0408a8e8a4f48a2ec598b84f7d455a501725397e566746addf86278	in use	12	4	transferred
72	2019-06-05 17:17:59.926	2019-06-10 16:41:36.74	5721311b3f6b4958542a6f520a04481c5fe2d1b2f69644826fe6efbb0b4ac254	8ddda13c7a998afdafc58129c6ce522a1991de45a26639af330fdb87e31158db	in use	11	4	transferred
80	2019-06-05 17:17:59.934	2019-06-10 16:41:37.243	12caa660b1489396b2f89b9e0ab9fe4fbd8da5afbb66b47022c7ae8de379ecc6	b49d2a8f7988ad45b290c5d0f635456afca2f5ba64d4daedd11659fdcf036cc5	in use	11	4	transferred
87	2019-06-05 17:17:59.941	2019-06-10 16:41:38.393	8b0f1dae1b3c22236a35fce6ac2e8b8ce355e32dedd246224b855714262d1ab3	ecd7cae41ba18cfc078df0b38be8d72b5c5d43ae0023606e840fe1ea04383893	in use	12	4	transferred
74	2019-06-05 17:17:59.928	2019-06-10 16:41:39.985	7e21b5e954b03b7831186f1df3bdc456c920839e0c2ae7388b24c62e36f85fbf	7132cd12dac02c376cf5ec4cda679e624f287a371b815e8e435a06b047b863b1	in use	11	4	transferred
78	2019-06-05 17:17:59.932	2019-06-10 16:41:40.498	3d68f1880e2b67a71ecfd5a672e2d16dd78eb47f9f37dcdce20baeac7e44c1cd	248e53dffcd50142ee4fc4beec907c85af0335200c879d7eddff759a2b644ed7	in use	11	4	transferred
94	2019-06-05 17:17:59.949	2019-06-10 16:46:44.365	c628383fea1c990d927462b0ff19840c71b471b5e89a8023a6111df3a536811c	b933ab5eaef1e1329c147314ae69981e79f761999901ab7ff0e8f705ffa090fb	in use	13	4	transferred
99	2019-06-05 17:17:59.954	2019-06-10 16:46:45.468	b5ed70b24e9a9655795cb1b1773f59f68736ca3739654dc99a57e020e6a913b3	eb4f06b1f6a36a0e6c003b0807279236f5cb6b17c254a3f7229f7d542cdf8bbd	in use	13	4	transferred
28	2019-06-05 17:17:59.866	2019-06-11 15:43:47.931	b7162bbb12c109f1467f8aa98457861052b61ebc07f3d7362b68ee80d0259343	b7162bbb12c109f1467f8aa98457861052b61ebc07f3d7362b68ee80d0259343	in use	6	21	transferring
44	2019-06-05 17:17:59.884	2019-06-11 15:49:07.745	a7771efa0046835471fb357d68b657ca5c99833cbe853efd26049ed28c2c473f	2e4241258a8e8ca8b61432ffb768432192acacd315a8d4733efd68fb0c2e7531	in use	8	21	transferring
97	2019-06-05 17:17:59.952	2019-06-09 15:30:23.038	be6058342cf7ace7ca7b2441d5501d889706e9aa22d9956b875a9eadacd5b541	5fad8cba101b956ae71d7977d493d0becaa4275b7d4ca4e06d322de8d6050523	in use	13	2	transferred
81	2019-06-05 17:17:59.935	2019-06-09 15:30:23.098	42215d2ea9aac23fc3e5d0fa3a2063452e253f238e2af7d4fbd2d57963b5e2ba	9e98cad7f20fde803216470c1bb60d2c2b7a3d0eccd176fbcac6ff58bb7bf65a	in use	12	4	transferred
51	2019-06-05 17:17:59.892	2019-06-09 15:30:23.156	800bc666d5ca3f3288835ac5e56c5e8e8a657ee05405bd96c1cd83eb57ecfb8e	78f62717046d6664f9f0e001d6c0aee88291b7f69ad815b8a9b174ab3eba997b	in use	9	2	transferred
84	2019-06-05 17:17:59.938	2019-06-09 15:30:23.214	871bd72245cf86593669593ed18179e7dec93e0d332255b74a90fa259a469583	08f4acb41f40e632d44f189c3acae4c243a7524c9c0db3a65c7d9f17a8ce26a3	in use	12	28	transferred
95	2019-06-05 17:17:59.95	2019-06-09 15:30:23.279	dc11e360034b0c74ac72027795db8c6eb023baeaaa5bbf7b5398e7ea742a0965	e6cfec08acac6b143d19ad4ab5a7cad47b298722a1e632c71d709822c7f0b09a	in use	13	28	transferred
96	2019-06-05 17:17:59.951	2019-06-09 15:30:23.345	d87226fbe4ef74689da0641f7e2855578df9760deef636394b554849de817c4f	9bad05d88097721da71bf6b12821edc9847958701d724e7a9cd2dd609a84fc65	in use	13	25	transferred
90	2019-06-05 17:17:59.944	2019-06-09 15:30:23.403	5c9513c193e0752251f8ce013df3ea8337ebf27cf6a2797175f98cd85a6902fb	be315821e436a81b809679fe0e8366324c335171ad53a5240c33cd2605448f5e	in use	12	28	transferred
92	2019-06-05 17:17:59.946	2019-06-09 15:30:23.466	d425999d9c427ed2a1cc4804b0ec15a600ea6dcb67a1f7bc8fa855e9d24fa727	3660ea84c08149dc6aa97b2b4dfe6b2f3ff600f58058e17648323eacc7a094df	in use	13	2	transferred
63	2019-06-05 17:17:59.915	2019-06-09 15:30:23.646	5e5478b966594bb8047e1202d9eeb601d4561f0736a4dc748e2997796d58c08f	59c7be87c31b67ebdad32b3e78b539b4e3fb6039ac95a659b3bf35536b55ea3f	in use	10	2	transferred
62	2019-06-05 17:17:59.913	2019-06-09 15:30:23.704	a2cfe6772ef29603230f409204211bf571d0a98bacda519ae47a6a72be8187bd	d318e9e78142f0b7f0256000560304daedf173898f588c9b09626f82dc7c48ef	in use	10	28	transferred
64	2019-06-05 17:17:59.916	2019-06-09 15:30:23.761	2054fd44ac77c264f3523e76d69e5837d398592c5e5dc5f77f5f00645ee0b27c	be202c3cd28853be75d80d2166aec677ecf00bceeadc2497b24aba1d0acf296a	in use	10	28	transferred
69	2019-06-05 17:17:59.923	2019-06-09 15:30:23.819	9086f12a16f0383ec0ed265e86be5cbd817c67020fe8b79150b327a287d482fa	961d8e1da790c0cca90276c6c6dd453a521285f330f462b57de6a30b414aec3c	in use	10	28	transferred
85	2019-06-05 17:17:59.939	2019-06-10 19:13:55.073	b8ca3c6d4fabc127cc9c10f3397bd1f7303a9802cb8ae9878996e9e4fa6392d9	8dce9286fe15adc6c701c056470ed13743bbf6a26febcf43cf534d11fc54f324	in use	12	21	transferred
23	2019-06-05 17:17:59.861	2019-06-11 15:47:58.143	42bbb04faa22466c4dccbda1006f6ff8605e995fc114708f0263b2045312e5a4	42bbb04faa22466c4dccbda1006f6ff8605e995fc114708f0263b2045312e5a4	in use	6	21	transferred
\.


--
-- Data for Name: book_author; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_author (book_id, author_id) FROM stdin;
4	2
5	3
6	3
7	2
8	1
9	3
10	3
11	2
12	1
13	1
\.


--
-- Data for Name: book_category; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_category (book_id, category_id) FROM stdin;
4	3
5	2
6	3
7	1
8	2
9	3
10	3
11	3
12	3
13	3
\.


--
-- Data for Name: book_detail; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_detail (id, created_date, update_date, name, publisher_id) FROM stdin;
4	\N	2019-06-05 17:17:59.957	XML	4
5	\N	2019-06-05 17:17:59.961	C Sharp	3
6	\N	2019-06-05 17:17:59.963	JAVA	4
7	\N	2019-06-05 17:17:59.965	JAVASCRIPT	2
8	\N	2019-06-05 17:17:59.966	SPRING	5
9	\N	2019-06-05 17:17:59.967	BigchainDB	2
10	\N	2019-06-05 17:17:59.969	Data Structure	5
11	\N	2019-06-05 17:17:59.97	Algorithm	2
12	\N	2019-06-05 17:17:59.971	Network	1
13	\N	2019-06-05 17:17:59.972	Machine Learning	3
14	2019-06-06 23:47:49.802	2019-06-06 23:47:49.803	abc	\N
15	2019-06-06 23:49:40.448	2019-06-06 23:49:40.448	abc	\N
16	2019-06-06 23:50:13.589	2019-06-06 23:50:13.589	abc	\N
17	2019-06-07 00:10:20.723	2019-06-07 00:10:20.723	abc	\N
18	2019-06-07 00:13:35.129	2019-06-07 00:13:35.129	abc	\N
19	2019-06-07 00:56:52.561	2019-06-07 00:56:52.561	abc	\N
20	2019-06-07 01:07:03.121	2019-06-07 01:07:03.121	abc	\N
21	2019-06-07 01:11:52.881	2019-06-07 01:11:52.881	abc	\N
22	2019-06-07 01:12:17.704	2019-06-07 01:12:17.704	abc	\N
23	2019-06-07 01:17:25.088	2019-06-07 01:17:25.088	abc	\N
24	2019-06-07 01:22:36.789	2019-06-07 01:22:36.789	abc	\N
25	2019-06-07 01:28:55.106	2019-06-07 01:28:55.106	abc	\N
26	2019-06-07 01:34:49.059	2019-06-07 01:34:49.059	abc	\N
\.


--
-- Data for Name: book_requests; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_requests (book_id, requests_id) FROM stdin;
\.


--
-- Data for Name: book_transactions; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_transactions (book_id, transactions_id) FROM stdin;
\.


--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.category (id, created_date, update_date, name) FROM stdin;
1	2019-06-05 17:17:07.977	2019-06-05 17:17:07.977	Computer Scienece
2	2019-06-05 17:17:07.984	2019-06-05 17:17:07.984	Software Engineering
3	2019-06-05 17:17:07.988	2019-06-05 17:17:07.988	Business Management
\.


--
-- Data for Name: image_url; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.image_url (id, created_date, update_date, url, image_id) FROM stdin;
\.


--
-- Data for Name: matching; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.matching (id, created_date, update_date, matching_start_date, pin, status, book_id, borrower_request_id, returner_request_id) FROM stdin;
93	2019-06-11 03:05:33.841	2019-06-11 03:05:33.841	2019-06-11 03:05:33.841	\N	2	98	147	168
94	2019-06-11 03:13:30.933	2019-06-11 03:14:11.191	2019-06-11 03:13:30.933	278343	3	1	170	169
95	2019-06-11 03:15:40.637	2019-06-11 03:15:40.637	2019-06-11 03:15:40.637	\N	2	9	172	166
96	2019-06-11 03:22:34.2	2019-06-11 03:22:53.038	2019-06-11 03:22:34.2	737218	3	65	174	173
100	2019-06-11 03:37:19.742	2019-06-11 03:37:46.915	2019-06-11 03:37:19.742	568591	3	52	182	181
101	2019-06-11 03:48:34.409	2019-06-11 03:49:10.365	2019-06-11 03:48:34.409	437595	3	54	185	184
102	2019-06-11 03:54:58.756	2019-06-11 03:55:16.24	2019-06-11 03:54:58.756	306941	3	45	187	186
103	2019-06-11 03:55:51.059	2019-06-11 03:58:54.046	2019-06-11 03:58:54.031	968510	2	45	149	188
104	2019-06-11 04:00:09.856	2019-06-11 04:00:09.856	2019-06-11 04:00:09.856	\N	2	65	190	175
105	2019-06-11 04:10:10.973	2019-06-11 04:10:27.461	2019-06-11 04:10:10.973	322755	3	47	192	191
106	2019-06-11 04:10:59.204	2019-06-11 04:10:59.204	2019-06-11 04:10:59.204	\N	2	47	189	193
107	2019-06-11 04:23:17.65	2019-06-11 04:23:35.182	2019-06-11 04:23:17.65	413098	3	39	195	194
79	2019-06-10 10:07:06.376	2019-06-10 10:07:22.62	2019-06-10 10:07:06.376	003447	4	61	\N	145
80	2019-06-10 10:07:30.338	2019-06-10 10:07:38.113	2019-06-10 10:07:30.338	262753	4	61	\N	146
81	2019-06-10 16:23:11.471	2019-06-10 16:23:31.183	2019-06-10 16:23:11.471	528462	3	37	152	151
82	2019-06-10 16:25:53.342	2019-06-10 16:26:12.957	2019-06-10 16:25:53.342	088927	4	2	\N	153
83	2019-06-10 16:26:19.858	2019-06-10 16:26:22.78	2019-06-10 16:26:19.858	860430	4	25	\N	154
108	2019-06-11 04:24:37.445	2019-06-11 04:26:17.462	2019-06-11 04:25:56.201	393473	3	39	197	196
84	2019-06-10 18:35:21.2	2019-06-10 18:35:31.588	2019-06-10 18:35:21.2	935760	4	4	\N	155
85	2019-06-10 19:12:30.008	2019-06-10 19:12:38.81	2019-06-10 19:12:30.008	138474	4	13	\N	156
86	2019-06-10 19:13:31.611	2019-06-10 19:13:33.219	2019-06-10 19:13:31.611	303357	4	28	\N	158
87	2019-06-10 19:13:53.689	2019-06-10 19:13:55.035	2019-06-10 19:13:53.689	648824	4	85	\N	159
89	2019-06-10 19:58:54.09	2019-06-10 19:58:56.867	2019-06-10 19:58:54.09	690218	4	21	\N	162
88	2019-06-10 19:38:59.035	2019-06-10 20:13:56.172	2019-06-10 19:38:59.035	660665	4	12	\N	161
109	2019-06-11 04:29:55.82	2019-06-11 04:32:00.377	2019-06-11 04:32:00.367	950735	2	17	199	198
91	2019-06-11 02:58:19.109	2019-06-11 02:58:32.988	2019-06-11 02:58:19.109	099517	3	98	165	164
110	2019-06-11 04:44:02.451	2019-06-11 04:44:26.033	2019-06-11 04:44:02.451	969709	3	42	201	200
92	2019-06-11 02:59:18.044	2019-06-11 03:01:01.755	2019-06-11 03:00:41.167	555961	3	10	167	150
111	2019-06-11 05:18:34.854	2019-06-11 05:18:47.924	2019-06-11 05:18:34.854	735709	4	2	\N	203
112	2019-06-11 05:25:43.734	2019-06-11 05:26:00.57	2019-06-11 05:25:43.734	811677	3	2	205	204
113	2019-06-11 05:26:54.645	2019-06-11 05:26:54.645	2019-06-11 05:26:54.645	\N	2	1	207	171
114	2019-06-11 05:34:45.78	2019-06-11 05:35:06.085	2019-06-11 05:34:45.78	317958	3	25	209	208
115	2019-06-11 05:35:57.387	2019-06-11 05:36:02.035	2019-06-11 05:35:57.387	873529	4	21	\N	210
97	2019-06-11 03:23:35.598	2019-06-11 22:37:32.016	2019-06-11 22:37:31.174	740741	2	61	176	148
117	2019-06-11 15:49:20.575	2019-06-11 15:49:20.575	2019-06-11 15:49:20.575	\N	2	42	215	202
118	2019-06-11 16:57:05.239	2019-06-12 06:05:59.418	2019-06-12 06:05:59.329	399632	2	28	216	212
\.


--
-- Data for Name: publisher; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.publisher (id, created_date, update_date, name) FROM stdin;
1	2019-06-05 17:17:08.105	2019-06-05 17:17:08.105	Springer Nature
2	2019-06-05 17:17:08.11	2019-06-05 17:17:08.11	Scholastic
3	2019-06-05 17:17:08.12	2019-06-05 17:17:08.12	McGraw-Hill Education
4	2019-06-05 17:17:08.136	2019-06-05 17:17:08.136	HarperCollins
5	2019-06-05 17:17:08.145	2019-06-05 17:17:08.145	Cengage
\.


--
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.request (id, created_date, update_date, status, type, book_id, book_detail_id, paired_user, user_id, mode) FROM stdin;
153	2019-06-10 16:25:53.33	2019-06-10 16:26:12.971	4	2	2	\N	\N	2	2
154	2019-06-10 16:26:19.849	2019-06-10 16:26:22.791	4	2	25	\N	\N	2	2
155	2019-06-10 18:35:21.152	2019-06-10 18:35:31.673	4	2	4	\N	\N	18	2
156	2019-06-10 19:12:29.972	2019-06-10 19:12:38.826	4	2	13	\N	\N	21	2
158	2019-06-10 19:13:31.599	2019-06-10 19:13:33.233	4	2	28	\N	\N	21	2
159	2019-06-10 19:13:53.67	2019-06-10 19:13:55.056	4	2	85	\N	\N	21	2
162	2019-06-10 19:58:54.08	2019-06-10 19:58:56.879	4	2	21	\N	\N	2	2
161	2019-06-10 19:38:58.99	2019-06-10 20:13:56.187	4	2	12	\N	\N	2	2
186	2019-06-11 03:54:58.748	2019-06-11 03:55:16.224	3	2	45	\N	\N	4	2
149	2019-06-10 11:25:33.768	2019-06-11 03:55:51.064	2	1	\N	8	\N	5	1
165	2019-06-11 02:58:32.954	2019-06-11 02:58:32.954	3	1	98	\N	\N	43	2
164	2019-06-11 02:58:19.098	2019-06-11 02:58:32.969	3	2	98	\N	\N	4	2
188	2019-06-11 03:55:51.048	2019-06-11 03:56:52.389	2	2	45	8	5	47	1
203	2019-06-11 05:18:34.843	2019-06-11 05:18:47.935	4	2	2	\N	\N	2	2
150	2019-06-10 13:14:15.291	2019-06-11 03:01:01.69	3	2	10	4	43	2	1
167	2019-06-11 02:59:18.031	2019-06-11 03:01:01.736	3	1	\N	4	2	43	1
168	2019-06-11 03:05:33.825	2019-06-11 03:05:33.845	2	2	98	13	\N	43	1
214	2019-06-11 15:49:07.76	2019-06-11 15:49:07.76	1	2	44	8	\N	21	1
170	2019-06-11 03:14:11.157	2019-06-11 03:14:11.157	3	1	1	\N	\N	41	2
169	2019-06-11 03:13:30.917	2019-06-11 03:14:11.168	3	2	1	\N	\N	4	2
190	2019-06-11 04:00:09.844	2019-06-11 04:00:16.765	2	1	\N	10	44	47	1
172	2019-06-11 03:15:40.625	2019-06-11 03:16:13.729	2	1	\N	4	4	4	1
147	2019-06-10 10:13:16.246	2019-06-11 03:16:33.62	2	1	\N	13	43	2	1
166	2019-06-11 02:58:59.47	2019-06-11 03:16:36.184	2	2	9	4	4	4	1
174	2019-06-11 03:22:53.011	2019-06-11 03:22:53.011	3	1	65	\N	\N	44	2
173	2019-06-11 03:22:34.191	2019-06-11 03:22:53.022	3	2	65	\N	\N	4	2
192	2019-06-11 04:10:27.434	2019-06-11 04:10:27.434	3	1	47	\N	\N	48	2
145	2019-06-10 10:07:06.34	2019-06-10 10:07:22.636	4	2	61	\N	\N	2	2
146	2019-06-10 10:07:30.323	2019-06-10 10:07:38.129	4	2	61	\N	\N	2	2
152	2019-06-10 16:23:31.149	2019-06-10 16:23:31.149	3	1	37	\N	\N	5	2
151	2019-06-10 16:23:11.43	2019-06-10 16:23:31.163	3	2	37	\N	\N	2	2
176	2019-06-11 03:23:35.589	2019-06-11 03:23:59.102	2	1	\N	10	2	4	1
191	2019-06-11 04:10:10.963	2019-06-11 04:10:27.445	3	2	47	\N	\N	4	2
182	2019-06-11 03:37:46.877	2019-06-11 03:37:46.877	3	1	52	\N	\N	45	2
181	2019-06-11 03:37:19.732	2019-06-11 03:37:46.895	3	2	52	\N	\N	4	2
183	2019-06-11 03:40:56.717	2019-06-11 03:40:56.717	1	2	52	9	\N	45	1
185	2019-06-11 03:49:10.319	2019-06-11 03:49:10.319	3	1	54	\N	\N	46	2
184	2019-06-11 03:48:34.4	2019-06-11 03:49:10.338	3	2	54	\N	\N	4	2
187	2019-06-11 03:55:16.212	2019-06-11 03:55:16.212	3	1	45	\N	\N	47	2
205	2019-06-11 05:26:00.541	2019-06-11 05:26:00.541	3	1	2	\N	\N	51	2
189	2019-06-11 03:56:07.765	2019-06-11 04:11:00.007	2	1	\N	8	48	4	1
193	2019-06-11 04:10:59.188	2019-06-11 04:11:03.245	2	2	47	8	4	48	1
195	2019-06-11 04:23:35.159	2019-06-11 04:23:35.159	3	1	39	\N	\N	49	2
194	2019-06-11 04:23:17.641	2019-06-11 04:23:35.17	3	2	39	\N	\N	4	2
204	2019-06-11 05:25:43.727	2019-06-11 05:26:00.555	3	2	2	\N	\N	2	2
206	2019-06-11 05:26:31.891	2019-06-11 05:26:31.891	1	2	2	4	\N	51	1
196	2019-06-11 04:24:07.582	2019-06-11 04:26:17.312	3	2	39	7	4	49	1
197	2019-06-11 04:24:37.437	2019-06-11 04:26:17.356	3	1	\N	7	49	4	1
171	2019-06-11 03:15:20.33	2019-06-11 05:26:54.651	2	2	1	4	\N	41	1
207	2019-06-11 05:26:54.637	2019-06-11 05:27:05.897	2	1	\N	4	41	2	1
199	2019-06-11 04:29:55.812	2019-06-11 04:30:10.365	2	1	\N	5	4	49	1
198	2019-06-11 04:29:51.393	2019-06-11 04:30:11.237	2	2	17	5	49	4	1
201	2019-06-11 04:44:26.009	2019-06-11 04:44:26.009	3	1	42	\N	\N	50	2
200	2019-06-11 04:44:02.443	2019-06-11 04:44:26.02	3	2	42	\N	\N	4	2
148	2019-06-10 10:13:55.227	2019-06-11 05:31:13.145	2	2	61	10	4	2	1
209	2019-06-11 05:35:06.058	2019-06-11 05:35:06.058	3	1	25	\N	\N	44	2
208	2019-06-11 05:34:45.769	2019-06-11 05:35:06.071	3	2	25	\N	\N	2	2
210	2019-06-11 05:35:57.347	2019-06-11 05:36:02.047	4	2	21	\N	\N	2	2
175	2019-06-11 03:23:17.118	2019-06-11 05:37:15.2	2	2	65	10	47	44	1
211	2019-06-11 22:37:45.216	2019-06-11 22:37:45.216	1	2	12	5	\N	2	1
202	2019-06-11 04:44:46.855	2019-06-11 15:49:20.58	2	2	42	8	\N	50	1
215	2019-06-11 15:49:20.564	2019-06-11 15:49:23.812	2	1	\N	8	50	17	1
216	2019-06-11 16:57:05.225	2019-06-11 16:57:09.743	2	1	\N	6	21	17	1
212	2019-06-11 15:43:47.99	2019-06-11 16:57:13.499	2	2	28	6	17	21	1
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role (id, created_date, update_date, role_name) FROM stdin;
1	\N	\N	librarian
\.


--
-- Data for Name: tbl_user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tbl_user (id, created_date, update_date, email, fullname, password, phone, is_disabled) FROM stdin;
5	2019-06-05 17:24:19.085	2019-06-05 17:24:19.085	phongdvse61654@fpt.edu.vn	\N	\N	\N	\N
2	2019-06-05 17:16:20.607	2019-06-10 10:12:48.932	tuhmse62531@fpt.edu.vn	Huỳnh Minh Tú	$2a$10$gvMeoWZ6ifAbf3WnYYoHbeDXvMUkKsk7D00joLUTpB1ev.Gn4PUIe	0796870446	f
3	2019-06-05 17:19:42.164	2019-06-05 17:19:42.164	cuongtqse62877@fpt.edu.vn	\N	\N	\N	\N
17	2019-06-05 18:14:12.81	2019-06-05 18:14:12.81	cuong@fptu.tech	Tô Quốc Cường	$2a$10$1E6kz50GQsXDa8DTHouIGuKRQpe.y9S8ePP4dfVvVyu/9YAbT.7T.	\N	\N
18	2019-06-05 18:25:58.079	2019-06-05 18:26:12.71	huynhminhtufu@gmail.com	Huỳnh Minh Tú	$2a$10$AOiHbyF0SrWRhUf4E3h67elskDsGA38l6xKtJ9B0cS2SJm0e6p7z.	0796870446	\N
41	2019-06-10 10:13:22.053	2019-06-10 10:13:22.053	thonglvse61916@fpt.edu.vn	\N	\N	\N	\N
19	2019-06-05 19:03:32.156	2019-06-05 19:05:00.871	duyhdse130491@fpt.edu.vn	Duy	\N	1234567890	\N
12	2019-06-05 17:44:01.631	2019-06-05 17:44:01.631	CuongTQSE62877@fpt.edu.vn	\N		\N	\N
4	2019-06-05 17:20:05.014	2019-06-06 04:36:17.753	linhphse62696@fpt.edu.vn	Phạm Hoàng Linh	\N	0366152394	\N
27	2019-06-08 08:58:51.901	2019-06-08 08:58:51.901	ku teo	PhongABC	$2a$10$cDz7acEvbMSx1YfLhW1kRu0Mm.XYRE.t/HCsZrVZccACd7pkIWKt.	\N	\N
22	2019-06-06 11:25:40.99	2019-06-06 11:25:40.99	hungptse63362@fpt.edu.vn	\N	\N	\N	\N
23	2019-06-06 13:15:55.83	2019-06-06 13:15:55.83	dungntse62576@fpt.edu.vn	\N	\N	\N	\N
25	2019-06-08 13:24:29.609	2019-06-08 13:24:29.609	philong@gmail.com	Le Phi Long	$2a$10$PmjyqcvhwoiO2GgAFW1DTuZ6UY2FtAXHz5rRYYZFM4XChnGd2egN6	\N	\N
43	2019-06-11 02:56:21.584	2019-06-11 02:56:21.584	ngocntse62292@fpt.edu.vn	Nguyen Thuy Ngoc - K11 FUG HCM	\N	\N	\N
28	2019-06-09 05:43:37.336	2019-06-09 05:44:55.933	student@fpt.edu.vn	Yasuo	$2a$10$NQGrUQxgjz1FNPS9PeExBu/eo6MvE/j54EohU0nMPJJZZaGcAKhfq	0123456789	\N
44	2019-06-11 03:21:02.71	2019-06-11 03:21:02.71	nhinptse62372@fpt.edu.vn	Nguyen Phan Thao Nhi - K11 FUG HCM	\N	\N	\N
45	2019-06-11 03:33:19.762	2019-06-11 03:33:19.762	haidtse130137@fpt.edu.vn	Dang Thanh Hai (K13_HCM)	\N	\N	\N
29	2019-06-09 17:10:37.54	2019-06-09 17:10:37.54	phuongkhanh0710@gmail.com	Lê Phương Khanh	$2a$10$dgxCpfWA8XTBA1MD0Jo6HOedR.EdKyng0167279nbFZaT/ZNJUpse	\N	\N
1	2019-06-05 17:16:14.885	2019-06-09 13:28:27.373	khanhkt@fe.edu.vn	Kiểu Trọng Khánh	$2a$10$d9dRuMdMI0N.Ow5.gX9y0uDcIFF4.4rY7EwtBf9eFnJejB23YBfDa	0345681294	\N
30	2019-06-09 17:34:37.368	2019-06-09 17:34:37.368	QuangHVSE61073@fpt.edu.vn	\N	\N	\N	\N
31	2019-06-09 17:40:04.885	2019-06-09 17:40:04.885	thongbhse62589@fpt.edu.vn	\N	\N	\N	\N
32	2019-06-09 22:22:14.018	2019-06-09 22:22:14.018	thanhdcse62548@fpt.edu.vn	\N	\N	\N	\N
33	2019-06-10 01:19:31.238	2019-06-10 01:19:31.238	hungbdtse62454@fpt.edu.vn	\N	\N	\N	\N
34	2019-06-10 02:12:13.573	2019-06-10 02:12:13.573	kietnase62444@fpt.edu.vn	\N	\N	\N	\N
35	2019-06-10 03:48:29.714	2019-06-10 03:50:43.75	tytnse62479@fpt.edu.vn	Ty TN	\N	0907698838	\N
36	2019-06-10 04:18:32.439	2019-06-10 04:18:32.44	tricvmse62549@fpt.edu.vn	\N	\N	\N	\N
37	2019-06-10 04:29:39.969	2019-06-10 04:29:39.969	cuongdqse62573@fpt.edu.vn	\N	\N	\N	\N
38	2019-06-10 04:45:26.487	2019-06-10 04:45:26.487	tannmse62503@fpt.edu.vn	\N	\N	\N	\N
39	2019-06-10 06:40:36.856	2019-06-10 06:40:36.856	phucdhse61768@fpt.edu.vn	\N	\N	\N	\N
46	2019-06-11 03:47:17.718	2019-06-11 03:47:17.718	tinhntse130596@fpt.edu.vn	Mouse King	\N	\N	\N
47	2019-06-11 03:53:47.134	2019-06-11 03:57:32.788	giangpttse62203@fpt.edu.vn	Tuyết Giang Phan	\N	0935152932	\N
48	2019-06-11 04:08:59.165	2019-06-11 04:08:59.165	thuyntt113@fpt.edu.vn	Nguyễn Thị Thanh Thủy (FPTU HCM)	\N	\N	\N
50	2019-06-11 04:42:21.355	2019-06-11 04:42:21.355	phuongntqsa140092@fpt.edu.vn	Nguyen Thi Que Phuong (K14 HCM)	\N	\N	\N
49	2019-06-11 04:22:14.352	2019-06-11 04:42:24.525	duongpthse62871@fpt.edu.vn	Phan Tran Hoang Duong	\N	0374484419	\N
51	2019-06-11 05:24:26.125	2019-06-11 05:24:26.125	quyenthse130364@fpt.edu.vn	Quyền Hữu	\N	\N	\N
21	2019-06-06 05:30:51.761	2019-06-06 05:30:51.762	cuongtq@fpt.edu.vn	Tô Quốc Cường	$2a$10$HeOIL3TuQXwQarSTJlEb/.KZzUQgO2cau9Q.RMtH.k/6taH9K/fVa	\N	\N
58	2019-06-11 22:15:32.318	2019-06-11 22:36:08.797	tu@mrhmt.com	Huỳnh Minh Tú	\N	0796870446	\N
\.


--
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transaction (id, created_date, update_date, bc_transaction_id, book_id, borrower_id, returner_id) FROM stdin;
1	2019-06-05 17:21:26.688	2019-06-05 17:21:26.688	\N	55	3	2
2	2019-06-05 17:24:58.406	2019-06-05 17:24:58.406	\N	97	5	2
3	2019-06-05 17:42:35.408	2019-06-05 17:42:35.408	\N	97	2	5
4	2019-06-05 18:13:19.331	2019-06-05 18:13:19.331	\N	44	5	2
5	2019-06-05 18:15:19.377	2019-06-05 18:15:19.377	\N	44	17	5
6	2019-06-05 18:15:59.26	2019-06-05 18:15:59.26	\N	44	2	17
7	2019-06-05 18:16:52.446	2019-06-05 18:16:52.446	\N	44	17	2
8	2019-06-05 18:17:44.095	2019-06-05 18:17:44.095	\N	44	5	17
9	2019-06-05 18:30:46.593	2019-06-05 18:30:46.593	\N	61	18	2
10	2019-06-05 18:33:05.223	2019-06-05 18:33:05.223	\N	51	18	2
11	2019-06-05 18:41:15.305	2019-06-05 18:41:15.305	\N	61	2	18
12	2019-06-05 18:54:22.127	2019-06-05 18:54:22.127	\N	51	2	18
13	2019-06-05 18:56:29.137	2019-06-05 18:56:29.137	\N	61	18	2
14	2019-06-05 18:58:34.196	2019-06-05 18:58:34.196	\N	61	2	18
15	2019-06-05 19:24:51.227	2019-06-05 19:24:51.227	\N	61	18	2
16	2019-06-05 19:25:45.11	2019-06-05 19:25:45.11	\N	61	2	18
17	2019-06-05 19:29:47.931	2019-06-05 19:29:47.931	\N	82	18	2
18	2019-06-05 19:31:14.335	2019-06-05 19:31:14.335	\N	82	2	18
19	2019-06-05 19:32:22.529	2019-06-05 19:32:22.529	\N	50	18	2
20	2019-06-05 19:37:31.481	2019-06-05 19:37:31.481	\N	61	18	2
21	2019-06-06 04:28:33.027	2019-06-06 04:28:33.027	\N	44	4	5
22	2019-06-06 04:30:23.23	2019-06-06 04:30:23.23	\N	44	5	4
23	2019-06-06 04:31:25.831	2019-06-06 04:31:25.831	\N	50	2	18
24	2019-06-06 04:32:00.655	2019-06-06 04:32:00.655	\N	61	2	18
25	2019-06-06 05:41:05.691	2019-06-06 05:41:05.691	\N	96	21	2
26	2019-06-06 15:18:45.888	2019-06-06 15:18:45.888	\N	61	18	2
27	2019-06-06 15:42:54.598	2019-06-06 15:42:54.598	\N	61	2	18
28	2019-06-08 07:19:50.348	2019-06-08 07:19:50.348	\N	96	25	21
29	2019-06-08 10:04:07.499	2019-06-08 10:04:07.499	\N	85	5	2
30	2019-06-08 10:13:00.113	2019-06-08 10:13:00.113	\N	85	2	5
31	2019-06-08 10:13:44.849	2019-06-08 10:13:44.849	\N	85	21	2
32	2019-06-09 06:19:19.443	2019-06-09 06:19:19.443	\N	26	2	28
33	2019-06-09 06:45:22.274	2019-06-09 06:45:22.274	\N	6	18	28
34	2019-06-09 07:00:38.454	2019-06-09 07:00:38.454	\N	16	2	2
35	2019-06-09 07:02:38.807	2019-06-09 07:02:38.807	\N	16	18	2
36	2019-06-09 07:03:53.147	2019-06-09 07:03:53.147	\N	34	18	2
37	2019-06-09 07:08:56.713	2019-06-09 07:08:56.713	\N	26	18	2
38	2019-06-09 16:42:58.315	2019-06-09 16:42:58.315	\N	4	18	2
39	2019-06-10 16:23:32.09	2019-06-10 16:23:32.09	\N	37	5	2
40	2019-06-11 02:58:33.27	2019-06-11 02:58:33.27	\N	98	43	4
41	2019-06-11 03:01:01.808	2019-06-11 03:01:01.808	\N	10	43	2
42	2019-06-11 03:14:11.436	2019-06-11 03:14:11.436	\N	1	41	4
43	2019-06-11 03:22:53.316	2019-06-11 03:22:53.316	\N	65	44	4
44	2019-06-11 03:37:47.187	2019-06-11 03:37:47.187	\N	52	45	4
45	2019-06-11 03:49:10.637	2019-06-11 03:49:10.637	\N	54	46	4
46	2019-06-11 03:55:16.508	2019-06-11 03:55:16.508	\N	45	47	4
47	2019-06-11 04:10:27.74	2019-06-11 04:10:27.74	\N	47	48	4
48	2019-06-11 04:23:35.405	2019-06-11 04:23:35.405	\N	39	49	4
49	2019-06-11 04:26:17.499	2019-06-11 04:26:17.499	\N	39	4	49
50	2019-06-11 04:44:26.339	2019-06-11 04:44:26.339	\N	42	50	4
51	2019-06-11 05:26:00.885	2019-06-11 05:26:00.885	\N	2	51	2
52	2019-06-11 05:35:06.447	2019-06-11 05:35:06.447	\N	25	44	2
\.


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_role (user_id, role_id) FROM stdin;
27	1
1	1
\.


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.category_id_seq', 3, true);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hibernate_sequence', 26, true);


--
-- Name: image_url_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.image_url_id_seq', 1, false);


--
-- Name: matching_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.matching_id_seq', 118, true);


--
-- Name: publisher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.publisher_id_seq', 5, true);


--
-- Name: request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.request_id_seq', 216, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.role_id_seq', 1, true);


--
-- Name: tbl_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tbl_user_id_seq', 58, true);


--
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.transaction_id_seq', 52, true);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- Name: book_detail book_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT book_detail_pkey PRIMARY KEY (id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: image_url image_url_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT image_url_pkey PRIMARY KEY (id);


--
-- Name: matching matching_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matching
    ADD CONSTRAINT matching_pkey PRIMARY KEY (id);


--
-- Name: publisher publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (id);


--
-- Name: request request_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT request_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: tbl_user tbl_user_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT tbl_user_pkey PRIMARY KEY (id);


--
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- Name: book_requests uk_47om0mhw8u0g70rmjnw77ncj1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT uk_47om0mhw8u0g70rmjnw77ncj1 UNIQUE (requests_id);


--
-- Name: book_transactions uk_lmabmxmxt4kowyf1bdofepkpk; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT uk_lmabmxmxt4kowyf1bdofepkpk UNIQUE (transactions_id);


--
-- Name: tbl_user uk_npn1wf1yu1g5rjohbek375pp1; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT uk_npn1wf1yu1g5rjohbek375pp1 UNIQUE (email);


--
-- Name: book fk3xigoma0tt7uvhy5693m6b7jx; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fk3xigoma0tt7uvhy5693m6b7jx FOREIGN KEY (bookdetail_id) REFERENCES public.book_detail(id);


--
-- Name: matching fk5i26fswphht7yuqcwe1l0u68m; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fk5i26fswphht7yuqcwe1l0u68m FOREIGN KEY (returner_request_id) REFERENCES public.request(id);


--
-- Name: transaction fk8hddvclv2iqa3sg1dm8295pqw; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk8hddvclv2iqa3sg1dm8295pqw FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: book_category fkam8llderp40mvbbwceqpu6l2s; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkam8llderp40mvbbwceqpu6l2s FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: book_author fkbjqhp85wjv8vpr0beygh6jsgo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo FOREIGN KEY (author_id) REFERENCES public.author(id);


--
-- Name: request fkbo8bmsdie58and5l6ddc03xef; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkbo8bmsdie58and5l6ddc03xef FOREIGN KEY (paired_user) REFERENCES public.tbl_user(id);


--
-- Name: book_transactions fkciir81r1ufgvlxa0mvu604pi2; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkciir81r1ufgvlxa0mvu604pi2 FOREIGN KEY (transactions_id) REFERENCES public.transaction(id);


--
-- Name: book_detail fkejp3y3fuwh494hor916ukyhkd; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT fkejp3y3fuwh494hor916ukyhkd FOREIGN KEY (publisher_id) REFERENCES public.publisher(id);


--
-- Name: image_url fkgy9ednxit1eycvqhko9pk9qnf; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT fkgy9ednxit1eycvqhko9pk9qnf FOREIGN KEY (image_id) REFERENCES public.transaction(id);


--
-- Name: book_transactions fkhticlf2fcsqfpbs92vkskpl2p; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkhticlf2fcsqfpbs92vkskpl2p FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book_author fkid4p9ic5tssamvk0m3f53p0b8; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkid4p9ic5tssamvk0m3f53p0b8 FOREIGN KEY (book_id) REFERENCES public.book_detail(id);


--
-- Name: transaction fkjr3i9hlgcyfid355yqrwrn1fr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkjr3i9hlgcyfid355yqrwrn1fr FOREIGN KEY (borrower_id) REFERENCES public.tbl_user(id);


--
-- Name: request fkjsjmh09dpj6tlqrf3w6e4vror; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkjsjmh09dpj6tlqrf3w6e4vror FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


--
-- Name: request fkk5wfoqkewjhptig7bdke6h2ie; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkk5wfoqkewjhptig7bdke6h2ie FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book_requests fkkgeplcw85nr006v3nlc5dsfu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkkgeplcw85nr006v3nlc5dsfu FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: request fkl44p0ukal7gxoa0rabxivw34e; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkl44p0ukal7gxoa0rabxivw34e FOREIGN KEY (book_detail_id) REFERENCES public.book_detail(id);


--
-- Name: matching fkmocchkas2lol67omhlbfybgpu; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fkmocchkas2lol67omhlbfybgpu FOREIGN KEY (borrower_request_id) REFERENCES public.request(id);


--
-- Name: book_requests fkofxcm11nyp348j1s7t5ar5c7n; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkofxcm11nyp348j1s7t5ar5c7n FOREIGN KEY (requests_id) REFERENCES public.request(id);


--
-- Name: user_role fkoqn0vpy8dpjr6u7kk4lqh15cq; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkoqn0vpy8dpjr6u7kk4lqh15cq FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


--
-- Name: transaction fkpm7o8a3dtg6dc76480k86c5ca; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkpm7o8a3dtg6dc76480k86c5ca FOREIGN KEY (returner_id) REFERENCES public.tbl_user(id);


--
-- Name: book_category fkr0ffi36oms3vuod94n27aw89h; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkr0ffi36oms3vuod94n27aw89h FOREIGN KEY (book_id) REFERENCES public.book_detail(id);


--
-- Name: matching fkrb4g7y9fmd270c8f53d34q4fb; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fkrb4g7y9fmd270c8f53d34q4fb FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book fksp3c1dqi5w5ba5gyhkkpwrpap; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fksp3c1dqi5w5ba5gyhkkpwrpap FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


--
-- PostgreSQL database dump complete
--

