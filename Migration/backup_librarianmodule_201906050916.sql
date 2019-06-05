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
    user_id bigint
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
    user_id bigint
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
    phone character varying(255)
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
-- Name: tbl_user_list_books; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.tbl_user_list_books (
    user_id bigint NOT NULL,
    list_books_id bigint NOT NULL
);


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
1	2019-06-04 17:21:36.332	2019-06-04 17:21:36.332	Adam Licol
2	2019-06-04 17:21:36.34	2019-06-04 17:21:36.34	John Thanos
3	2019-06-04 17:21:36.342	2019-06-04 17:21:36.342	Alex Man
\.


--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book (id, created_date, update_date, asset_id, last_tx_id, status, bookdetail_id, user_id) FROM stdin;
1	2019-06-04 17:22:27.734	2019-06-04 17:22:27.734	1137e5ac3ea2c4bb215d00d5b8ed3b6c41269cbba201288eddc0fa3a9d83ee97	1137e5ac3ea2c4bb215d00d5b8ed3b6c41269cbba201288eddc0fa3a9d83ee97	in use	4	1
2	2019-06-04 17:22:27.735	2019-06-04 17:22:27.735	4f1e74ebadf14c6c5f61df8f75f4d29d05e9cb2a8aca43e3e665580861abe993	4f1e74ebadf14c6c5f61df8f75f4d29d05e9cb2a8aca43e3e665580861abe993	in use	4	1
3	2019-06-04 17:22:27.736	2019-06-04 17:22:27.736	baaadc0e91f23da9fed55629ad41eabaa732dc882b4d4da08d4b2733b1b5ebda	baaadc0e91f23da9fed55629ad41eabaa732dc882b4d4da08d4b2733b1b5ebda	in use	4	1
4	2019-06-04 17:22:27.738	2019-06-04 17:22:27.738	fbe75084c02cc8a64e1117a1d3297ee7bd04f09eceb178564d18328253546a70	fbe75084c02cc8a64e1117a1d3297ee7bd04f09eceb178564d18328253546a70	in use	4	1
6	2019-06-04 17:22:27.74	2019-06-04 17:22:27.74	ba38abe8fdc362af6ab8e5244b314f695f8a05ba308463c99f115a5403151ff6	ba38abe8fdc362af6ab8e5244b314f695f8a05ba308463c99f115a5403151ff6	in use	4	1
7	2019-06-04 17:22:27.741	2019-06-04 17:22:27.741	6a19149466052b1e7df01b4e248d2f51adbf6d97ceb66ee6c9d3f32739bcd13e	6a19149466052b1e7df01b4e248d2f51adbf6d97ceb66ee6c9d3f32739bcd13e	in use	4	1
8	2019-06-04 17:22:27.743	2019-06-04 17:22:27.743	900d4c21c444da6c6b1e4f192256b327235b89d1675ce45c4b31c8ac656d3ce9	900d4c21c444da6c6b1e4f192256b327235b89d1675ce45c4b31c8ac656d3ce9	in use	4	1
10	2019-06-04 17:22:27.745	2019-06-04 17:22:27.745	94901e230a6e0cee5e218e367c742f0a6ba62bdaf06198cf9da847434503f212	94901e230a6e0cee5e218e367c742f0a6ba62bdaf06198cf9da847434503f212	in use	4	1
11	2019-06-04 17:22:27.746	2019-06-04 17:22:27.746	7d32545897299a5939b980873963d5c65dac788a1ebe10990d7a4a55ef4a9b0d	7d32545897299a5939b980873963d5c65dac788a1ebe10990d7a4a55ef4a9b0d	in use	5	1
12	2019-06-04 17:22:27.748	2019-06-04 17:22:27.748	756d1504dc43e5dad30c0528a5c7c86697a1a8bdc51c94fe4677d4567b8aa447	756d1504dc43e5dad30c0528a5c7c86697a1a8bdc51c94fe4677d4567b8aa447	in use	5	1
13	2019-06-04 17:22:27.749	2019-06-04 17:22:27.749	06e3e74cc0fb51aae34999a01349b208aa602a373286749add0a0bd6d1a3e8be	06e3e74cc0fb51aae34999a01349b208aa602a373286749add0a0bd6d1a3e8be	in use	5	1
14	2019-06-04 17:22:27.75	2019-06-04 17:22:27.75	805be4b4205278c690584f33c23bd6256cacf5e60a5236ef4958be121e72c453	805be4b4205278c690584f33c23bd6256cacf5e60a5236ef4958be121e72c453	in use	5	1
15	2019-06-04 17:22:27.752	2019-06-04 17:22:27.752	07683fa138ee9bed494f4c3d0b49d1c1f6e18a20604a3006d3e9219a85d0c160	07683fa138ee9bed494f4c3d0b49d1c1f6e18a20604a3006d3e9219a85d0c160	in use	5	1
16	2019-06-04 17:22:27.753	2019-06-04 17:22:27.753	eea6861a4ae0c3cef9b45ef89761faaa35f1bec67e003e5dfff9d340eb01922b	eea6861a4ae0c3cef9b45ef89761faaa35f1bec67e003e5dfff9d340eb01922b	in use	5	1
17	2019-06-04 17:22:27.754	2019-06-04 17:22:27.754	e90f83d33eacda1386d269ab9990c049f7668cfb4981894eaf57c7314ddef814	e90f83d33eacda1386d269ab9990c049f7668cfb4981894eaf57c7314ddef814	in use	5	1
18	2019-06-04 17:22:27.755	2019-06-04 17:22:27.755	7694e237e1f07f60808efd62e1e3739cbaa8aeaa306bb6cc0df783469ff82436	7694e237e1f07f60808efd62e1e3739cbaa8aeaa306bb6cc0df783469ff82436	in use	5	1
19	2019-06-04 17:22:27.756	2019-06-04 17:22:27.756	1d814b7279e83fbe0b478d140fcbde3649d4868e4dd21b4ede69c260e0dfcbb1	1d814b7279e83fbe0b478d140fcbde3649d4868e4dd21b4ede69c260e0dfcbb1	in use	5	1
20	2019-06-04 17:22:27.757	2019-06-04 17:22:27.757	d37ee6ef9fa50a37dd4a93ee23d6fac0b9b5901bada6b5a240b96a66540bb9a5	d37ee6ef9fa50a37dd4a93ee23d6fac0b9b5901bada6b5a240b96a66540bb9a5	in use	5	1
21	2019-06-04 17:22:27.759	2019-06-04 17:22:27.759	0fc6f1e66d3b0c8690bc8a744c26546d8e2523a17fa158de7ae37d0304a91be3	0fc6f1e66d3b0c8690bc8a744c26546d8e2523a17fa158de7ae37d0304a91be3	in use	6	1
22	2019-06-04 17:22:27.76	2019-06-04 17:22:27.76	a9a05f293751d78605d8951c09a5716d550e482937c4985d17c5a9ca5b72fc9f	a9a05f293751d78605d8951c09a5716d550e482937c4985d17c5a9ca5b72fc9f	in use	6	1
23	2019-06-04 17:22:27.761	2019-06-04 17:22:27.761	33f7fd441c255a3052dee81cd7a8f99894f400c077884ac5642456a961ad7030	33f7fd441c255a3052dee81cd7a8f99894f400c077884ac5642456a961ad7030	in use	6	1
24	2019-06-04 17:22:27.763	2019-06-04 17:22:27.763	c9ce416dd8bdd57e68b9378ee13b70eccd3a786359d72e73c8e5f9184d374748	c9ce416dd8bdd57e68b9378ee13b70eccd3a786359d72e73c8e5f9184d374748	in use	6	1
25	2019-06-04 17:22:27.764	2019-06-04 17:22:27.764	6089f40759c49ab02b0734a13aaf72a0c88fb37b6ee3ab309900c1bad51588c1	6089f40759c49ab02b0734a13aaf72a0c88fb37b6ee3ab309900c1bad51588c1	in use	6	1
26	2019-06-04 17:22:27.765	2019-06-04 17:22:27.765	6b6797476b1fc55ba1f4bb85b5b355f750fe822211813abbcc8c047e998382c6	6b6797476b1fc55ba1f4bb85b5b355f750fe822211813abbcc8c047e998382c6	in use	6	1
27	2019-06-04 17:22:27.766	2019-06-04 17:22:27.766	32fa96c5bbfd2a0ee1e9c98adb673e8bc68e29a43256e00a4ce951b0fbd6d758	32fa96c5bbfd2a0ee1e9c98adb673e8bc68e29a43256e00a4ce951b0fbd6d758	in use	6	1
28	2019-06-04 17:22:27.767	2019-06-04 17:22:27.767	95ea08372bc2e59e371956b6a19c8202cb6f6ffa4ee651ba7d1c61f2403b6ca0	95ea08372bc2e59e371956b6a19c8202cb6f6ffa4ee651ba7d1c61f2403b6ca0	in use	6	1
29	2019-06-04 17:22:27.768	2019-06-04 17:22:27.768	3dc021f7e6ac111658c15952b54f40d987af3f68acf8a91174fb5822c48e08bd	3dc021f7e6ac111658c15952b54f40d987af3f68acf8a91174fb5822c48e08bd	in use	6	1
30	2019-06-04 17:22:27.769	2019-06-04 17:22:27.769	15d7ffe43c52f3f1f9f2f847de6fc94fa91d8f0b0f76ed466cbcf19e2cf810b6	15d7ffe43c52f3f1f9f2f847de6fc94fa91d8f0b0f76ed466cbcf19e2cf810b6	in use	6	1
31	2019-06-04 17:22:27.77	2019-06-04 17:22:27.77	86cb3e557a69a37c7d1f609589f47337ab52d253a538349b32b1d268cf3038a2	86cb3e557a69a37c7d1f609589f47337ab52d253a538349b32b1d268cf3038a2	in use	7	1
32	2019-06-04 17:22:27.771	2019-06-04 17:22:27.771	a54de7a4b25e121215f48757bd770a1449fa6eeeb5fdf5063f2bdffe7282f247	a54de7a4b25e121215f48757bd770a1449fa6eeeb5fdf5063f2bdffe7282f247	in use	7	1
33	2019-06-04 17:22:27.772	2019-06-04 17:22:27.772	753f9fc8c7ed0dd0420fb097544457fa6d2d6f569e7f4d1ddd988c5715430738	753f9fc8c7ed0dd0420fb097544457fa6d2d6f569e7f4d1ddd988c5715430738	in use	7	1
34	2019-06-04 17:22:27.774	2019-06-04 17:22:27.774	3415648301ab8bb45e4cefd152fc5cf26e7856546a9397c80d960da0db5360be	3415648301ab8bb45e4cefd152fc5cf26e7856546a9397c80d960da0db5360be	in use	7	1
35	2019-06-04 17:22:27.775	2019-06-04 17:22:27.775	36d79d93ee02a33294401dcdb57282f34a69687ed5cb57aa93fbf16577e0986d	36d79d93ee02a33294401dcdb57282f34a69687ed5cb57aa93fbf16577e0986d	in use	7	1
36	2019-06-04 17:22:27.776	2019-06-04 17:22:27.776	898631e0b9294e8bccba8a4ab5ff77b464c178fecb1498b8caed3c30b92d26c7	898631e0b9294e8bccba8a4ab5ff77b464c178fecb1498b8caed3c30b92d26c7	in use	7	1
37	2019-06-04 17:22:27.777	2019-06-04 17:22:27.777	724350ea3036f32cff788cf2193f07d1af33c75a756ebd19e92921d5012a79a8	724350ea3036f32cff788cf2193f07d1af33c75a756ebd19e92921d5012a79a8	in use	7	1
38	2019-06-04 17:22:27.785	2019-06-04 17:22:27.785	4a15a2c49e5a67009f49a6173f2568f680b29cb0c7bb2269e985878fdf3c9df8	4a15a2c49e5a67009f49a6173f2568f680b29cb0c7bb2269e985878fdf3c9df8	in use	7	1
5	2019-06-04 17:22:27.739	2019-06-04 17:46:19.737	6c1da1626b9a070841fb06e3fa7a615de24b8e3308ac41f6307175af8a4711f9	df90ac47b9e7da9c7045b78202c5fbfb5e461762dd4436a95ddec3237c3dd156	in use	4	2
40	2019-06-04 17:22:27.788	2019-06-04 17:22:27.788	1a876d1a86ac80c2247a21ae2a17f374578d8ef6d0ea6163fcb83d482363a788	1a876d1a86ac80c2247a21ae2a17f374578d8ef6d0ea6163fcb83d482363a788	in use	7	1
41	2019-06-04 17:22:27.79	2019-06-04 17:22:27.79	b17c2390f80048dd89da95d0934cf0fd07fa9a2ad90a1f1bff4717587d82e18c	b17c2390f80048dd89da95d0934cf0fd07fa9a2ad90a1f1bff4717587d82e18c	in use	8	1
42	2019-06-04 17:22:27.792	2019-06-04 17:22:27.792	a5a369227c148dc4270807717efd1ad344d19ae70fa1c439bdaff712bb84dedf	a5a369227c148dc4270807717efd1ad344d19ae70fa1c439bdaff712bb84dedf	in use	8	1
43	2019-06-04 17:22:27.793	2019-06-04 17:22:27.793	fcbe9da3afaaa28405d5f62bed5049753548be099925aa99fa6977a8b6b27cae	fcbe9da3afaaa28405d5f62bed5049753548be099925aa99fa6977a8b6b27cae	in use	8	1
44	2019-06-04 17:22:27.795	2019-06-04 17:22:27.795	540b73e19b403f006fe3cbd3716f1215027b2f92d5ae7bfdf80f853a47130428	540b73e19b403f006fe3cbd3716f1215027b2f92d5ae7bfdf80f853a47130428	in use	8	1
45	2019-06-04 17:22:27.797	2019-06-04 17:22:27.797	75d75fa83a5ecb35471ad542a8999588d94a8eee1e0e62426bea58079566f79e	75d75fa83a5ecb35471ad542a8999588d94a8eee1e0e62426bea58079566f79e	in use	8	1
46	2019-06-04 17:22:27.798	2019-06-04 17:22:27.798	952add9352c76ebeb284e20e2f6ef2be78acf767c079fedc2e8cd77c2354178d	952add9352c76ebeb284e20e2f6ef2be78acf767c079fedc2e8cd77c2354178d	in use	8	1
47	2019-06-04 17:22:27.8	2019-06-04 17:22:27.8	609c06ff014900eb2c17630dfeca84ceadfb13e0f948747147c531f7d8924c84	609c06ff014900eb2c17630dfeca84ceadfb13e0f948747147c531f7d8924c84	in use	8	1
48	2019-06-04 17:22:27.802	2019-06-04 17:22:27.802	5d7c0bd1b90fcbb5ea816efe089482f30b6ea6468693d8a65f86b874e14ba6cf	5d7c0bd1b90fcbb5ea816efe089482f30b6ea6468693d8a65f86b874e14ba6cf	in use	8	1
49	2019-06-04 17:22:27.803	2019-06-04 17:22:27.803	4f109285a36fdb914ce0372ec3fb8059a6b89645197ba384ad836f6c07fdde76	4f109285a36fdb914ce0372ec3fb8059a6b89645197ba384ad836f6c07fdde76	in use	8	1
50	2019-06-04 17:22:27.804	2019-06-04 17:22:27.804	a551144bea7031501519a7f64f34331fa76cc2c076de0bed6997a90e20f4d17d	a551144bea7031501519a7f64f34331fa76cc2c076de0bed6997a90e20f4d17d	in use	8	1
51	2019-06-04 17:22:27.806	2019-06-04 17:22:27.806	c8dcd10c5941e8ed26e5e614f112987adc9ff5d90511b2799e8667a6a1af37fe	c8dcd10c5941e8ed26e5e614f112987adc9ff5d90511b2799e8667a6a1af37fe	in use	9	1
52	2019-06-04 17:22:27.808	2019-06-04 17:22:27.808	8bd50fffc3bd695be8abb30c3da6a0e809f2df75ac55f6766450db1ca49aacbc	8bd50fffc3bd695be8abb30c3da6a0e809f2df75ac55f6766450db1ca49aacbc	in use	9	1
53	2019-06-04 17:22:27.81	2019-06-04 17:22:27.81	d2d772ba9a16307647418db73bee2479c82ba91c40e926f5debf578652794740	d2d772ba9a16307647418db73bee2479c82ba91c40e926f5debf578652794740	in use	9	1
54	2019-06-04 17:22:27.811	2019-06-04 17:22:27.811	68e36acccc64fcb0ea0531badec8b4f6c1ca83bfa61558afe0086906df1592e7	68e36acccc64fcb0ea0531badec8b4f6c1ca83bfa61558afe0086906df1592e7	in use	9	1
55	2019-06-04 17:22:27.812	2019-06-04 17:22:27.812	8ab00a994aa29007d725834d40f09286e9b5f52632fdccfa45efcaf9ef84376d	8ab00a994aa29007d725834d40f09286e9b5f52632fdccfa45efcaf9ef84376d	in use	9	1
56	2019-06-04 17:22:27.814	2019-06-04 17:22:27.814	dce0b1c6b30d1946eab7c398239b0f338f492b13dee8a59aead0071626d9ec33	dce0b1c6b30d1946eab7c398239b0f338f492b13dee8a59aead0071626d9ec33	in use	9	1
57	2019-06-04 17:22:27.815	2019-06-04 17:22:27.815	332c9fc1f9e2ac860014af49a83efd723afd4fc05c94c4d5ee8b32cc5e16c9e8	332c9fc1f9e2ac860014af49a83efd723afd4fc05c94c4d5ee8b32cc5e16c9e8	in use	9	1
58	2019-06-04 17:22:27.816	2019-06-04 17:22:27.816	f07f3998afd0de982aafde9d994a089cd4282e5882c7013c51f0449049a2dfb5	f07f3998afd0de982aafde9d994a089cd4282e5882c7013c51f0449049a2dfb5	in use	9	1
59	2019-06-04 17:22:27.818	2019-06-04 17:22:27.818	b7af18fe559a6f8404d74cd01764ca2895c30d7fb415c8863316f96727e57050	b7af18fe559a6f8404d74cd01764ca2895c30d7fb415c8863316f96727e57050	in use	9	1
60	2019-06-04 17:22:27.819	2019-06-04 17:22:27.819	3b8bef765adc46beaec9de8862f5ce203c09bc3b7f12c776cf482f58f5137a46	3b8bef765adc46beaec9de8862f5ce203c09bc3b7f12c776cf482f58f5137a46	in use	9	1
61	2019-06-04 17:22:27.82	2019-06-04 17:22:27.82	56e814b8755ba3da487ea65c79c3df8f1323b3b4a5df6fc3a07b172288d2be80	56e814b8755ba3da487ea65c79c3df8f1323b3b4a5df6fc3a07b172288d2be80	in use	10	1
62	2019-06-04 17:22:27.821	2019-06-04 17:22:27.821	49d1958b0dbc3758a12f9c67900e0b3807407de6e665e8a117e45df2f7b54736	49d1958b0dbc3758a12f9c67900e0b3807407de6e665e8a117e45df2f7b54736	in use	10	1
63	2019-06-04 17:22:27.822	2019-06-04 17:22:27.822	12c1a5f242491fbb6d854b3dda648ccb9097e8efabcd7b2dcedc615a73639b37	12c1a5f242491fbb6d854b3dda648ccb9097e8efabcd7b2dcedc615a73639b37	in use	10	1
64	2019-06-04 17:22:27.823	2019-06-04 17:22:27.823	48af3c78c0786552f5860d154888b3ed43af02cf7bd24cdc3ecf5e26819c06a8	48af3c78c0786552f5860d154888b3ed43af02cf7bd24cdc3ecf5e26819c06a8	in use	10	1
65	2019-06-04 17:22:27.824	2019-06-04 17:22:27.824	ab995b71d02c36a36c996190fe71353e1665b0708a436b3662f9c0719e75bc52	ab995b71d02c36a36c996190fe71353e1665b0708a436b3662f9c0719e75bc52	in use	10	1
66	2019-06-04 17:22:27.826	2019-06-04 17:22:27.826	29c7a9143def0ed8778c5ddb64739123560216b4690600854e9854b3fd491a00	29c7a9143def0ed8778c5ddb64739123560216b4690600854e9854b3fd491a00	in use	10	1
67	2019-06-04 17:22:27.827	2019-06-04 17:22:27.827	2c295e3c6297caf17b052854fbd7a342420146ac0cf7ce6924bc5d91211db244	2c295e3c6297caf17b052854fbd7a342420146ac0cf7ce6924bc5d91211db244	in use	10	1
68	2019-06-04 17:22:27.828	2019-06-04 17:22:27.828	57758a850194f50dec802b660193a66f40d17799f8f8c2798530849e9ed34bbf	57758a850194f50dec802b660193a66f40d17799f8f8c2798530849e9ed34bbf	in use	10	1
69	2019-06-04 17:22:27.829	2019-06-04 17:22:27.829	746e249acccbab14bd0ccbdc2373766ca84983013213934fe34b9e8588e48c83	746e249acccbab14bd0ccbdc2373766ca84983013213934fe34b9e8588e48c83	in use	10	1
70	2019-06-04 17:22:27.83	2019-06-04 17:22:27.83	eeddc7378fac1433aeeb039486ec65863237f103d40497fc753f16ae75351554	eeddc7378fac1433aeeb039486ec65863237f103d40497fc753f16ae75351554	in use	10	1
72	2019-06-04 17:22:27.832	2019-06-04 17:22:27.832	52e10b66a7a0b3cd52639b6a0063e0a4f23ced837e1ce4e76a3b72c733393b2b	52e10b66a7a0b3cd52639b6a0063e0a4f23ced837e1ce4e76a3b72c733393b2b	in use	11	1
73	2019-06-04 17:22:27.833	2019-06-04 17:22:27.833	a110192fb513c8c2ac7a9095be361830c8c6d9733a1a61b0516b029a1436c798	a110192fb513c8c2ac7a9095be361830c8c6d9733a1a61b0516b029a1436c798	in use	11	1
74	2019-06-04 17:22:27.834	2019-06-04 17:22:27.834	82e32b58767f33fefb936513d4ea7ceffdd72b51c86ee8af33c670b20d3c0902	82e32b58767f33fefb936513d4ea7ceffdd72b51c86ee8af33c670b20d3c0902	in use	11	1
75	2019-06-04 17:22:27.835	2019-06-04 17:22:27.835	979642bc5b0a0abbcd7c62139b9af5171dc260ff4f77b78b4532e4b8ef2546f7	979642bc5b0a0abbcd7c62139b9af5171dc260ff4f77b78b4532e4b8ef2546f7	in use	11	1
76	2019-06-04 17:22:27.836	2019-06-04 17:22:27.836	ad689b1662f71944f704d2473cd55387b5409f6a7c9e1404213cca8d5feafb12	ad689b1662f71944f704d2473cd55387b5409f6a7c9e1404213cca8d5feafb12	in use	11	1
39	2019-06-04 17:22:27.787	2019-06-04 17:46:21.19	520aad7bc42f6fb68421caf248cb6b07200556a223194c4ced24072063cdd173	b391250ab88dabe76dda54726602abca17d4ab16572e02f581c4469ba4ae2f07	in use	7	2
77	2019-06-04 17:22:27.838	2019-06-04 17:22:27.838	664fbf4c435b94d62136dc3cbc5a4a3d1a23da362e8f788adecb9bcbeaa8fee0	664fbf4c435b94d62136dc3cbc5a4a3d1a23da362e8f788adecb9bcbeaa8fee0	in use	11	1
78	2019-06-04 17:22:27.839	2019-06-04 17:22:27.839	8b6080c5c796626b51c9fd637e243dfbafb1b47e98a052f4c8aa256ab1230ef1	8b6080c5c796626b51c9fd637e243dfbafb1b47e98a052f4c8aa256ab1230ef1	in use	11	1
79	2019-06-04 17:22:27.84	2019-06-04 17:22:27.84	e7960343f59cf2f2653a8a700e98c6633d938e45bef87034df810f1dfc8e367a	e7960343f59cf2f2653a8a700e98c6633d938e45bef87034df810f1dfc8e367a	in use	11	1
80	2019-06-04 17:22:27.841	2019-06-04 17:22:27.841	c3d7956970cce5501a6dd8d00ccbc16684fd6b95d839058fbac0eee2f55e9770	c3d7956970cce5501a6dd8d00ccbc16684fd6b95d839058fbac0eee2f55e9770	in use	11	1
81	2019-06-04 17:22:27.842	2019-06-04 17:22:27.842	24f4b61ed43271e159aae15286ec8c3b947289f9c33a59f55bdcb388162791bf	24f4b61ed43271e159aae15286ec8c3b947289f9c33a59f55bdcb388162791bf	in use	12	1
82	2019-06-04 17:22:27.843	2019-06-04 17:22:27.843	685e30f334a375c43daf2ae0edb80fca856cb54b6dfb9b807a2a0cb6b55bbcb2	685e30f334a375c43daf2ae0edb80fca856cb54b6dfb9b807a2a0cb6b55bbcb2	in use	12	1
83	2019-06-04 17:22:27.844	2019-06-04 17:22:27.844	16564d69fea477eab92499dca98018c5346ddd4ad0d726addb0cb4856e1d6023	16564d69fea477eab92499dca98018c5346ddd4ad0d726addb0cb4856e1d6023	in use	12	1
84	2019-06-04 17:22:27.846	2019-06-04 17:22:27.846	a6787af87f2d6e73e08291a00610cb3fdf2d25dab5410b6b2bde1db05d3cc35d	a6787af87f2d6e73e08291a00610cb3fdf2d25dab5410b6b2bde1db05d3cc35d	in use	12	1
85	2019-06-04 17:22:27.847	2019-06-04 17:22:27.847	2df9ef804a62439da6d2b8d3d4f2c2ec07454a8f14f492eb8bf62a09bcbbf42f	2df9ef804a62439da6d2b8d3d4f2c2ec07454a8f14f492eb8bf62a09bcbbf42f	in use	12	1
86	2019-06-04 17:22:27.848	2019-06-04 17:22:27.848	b8a3dba11c46897835cd06a2e1260df09576aabfe10ac0d69ccfb662a0ccd526	b8a3dba11c46897835cd06a2e1260df09576aabfe10ac0d69ccfb662a0ccd526	in use	12	1
87	2019-06-04 17:22:27.849	2019-06-04 17:22:27.849	30d7a8f661ea46c873440f962e351fedc2d2e2acc020a91978c5ce7a0b527445	30d7a8f661ea46c873440f962e351fedc2d2e2acc020a91978c5ce7a0b527445	in use	12	1
88	2019-06-04 17:22:27.85	2019-06-04 17:22:27.85	55f358d9dbe9e9bd0e8f9167af42fa7a79773f31b689dd36d09f56d3dc5cefec	55f358d9dbe9e9bd0e8f9167af42fa7a79773f31b689dd36d09f56d3dc5cefec	in use	12	1
89	2019-06-04 17:22:27.851	2019-06-04 17:22:27.851	145caef60aba704c04058f39b09761ce9f796138a8fb143611b016c72cbb8c4f	145caef60aba704c04058f39b09761ce9f796138a8fb143611b016c72cbb8c4f	in use	12	1
90	2019-06-04 17:22:27.851	2019-06-04 17:22:27.851	5f8da5c20cc484a8a7cc65fd13680325a36b1256369f6e629e698d303077f008	5f8da5c20cc484a8a7cc65fd13680325a36b1256369f6e629e698d303077f008	in use	12	1
91	2019-06-04 17:22:27.852	2019-06-04 17:22:27.852	4e3434211b64afc939d4132697646175c5ae8cc42b5e399c9b9f1361723ead65	4e3434211b64afc939d4132697646175c5ae8cc42b5e399c9b9f1361723ead65	in use	13	1
92	2019-06-04 17:22:27.859	2019-06-04 17:22:27.859	1854fc476e99d5a8c45e50c65a005a61568e34dddf8e8687cb249ee52b12f829	1854fc476e99d5a8c45e50c65a005a61568e34dddf8e8687cb249ee52b12f829	in use	13	1
93	2019-06-04 17:22:27.861	2019-06-04 17:22:27.861	96b5874cbe95f82f3535e6523230d0d5a6e069defd6a8f3059e5623f21c31d6b	96b5874cbe95f82f3535e6523230d0d5a6e069defd6a8f3059e5623f21c31d6b	in use	13	1
94	2019-06-04 17:22:27.862	2019-06-04 17:22:27.862	0c983cc51f87df16bfa94ead45a5661081b85e0e29015181856455b382faa26b	0c983cc51f87df16bfa94ead45a5661081b85e0e29015181856455b382faa26b	in use	13	1
95	2019-06-04 17:22:27.863	2019-06-04 17:22:27.863	afa3c34661ab58d24492d8359437dd80effbef63a7f222485150b5a366f73249	afa3c34661ab58d24492d8359437dd80effbef63a7f222485150b5a366f73249	in use	13	1
97	2019-06-04 17:22:27.865	2019-06-04 17:22:27.865	06363720cdc40720da6b2b090741d0877dfb7cde3fc64b1009c12ea1bb47f844	06363720cdc40720da6b2b090741d0877dfb7cde3fc64b1009c12ea1bb47f844	in use	13	1
98	2019-06-04 17:22:27.866	2019-06-04 17:22:27.866	bf60ebaa5eb205450c492b23db9c794266c476009a88a4720bda627b1799e0ae	bf60ebaa5eb205450c492b23db9c794266c476009a88a4720bda627b1799e0ae	in use	13	1
99	2019-06-04 17:22:27.867	2019-06-04 17:22:27.867	1aaf9c5ce318a420e5cf0ae35ac7b55d8b967e9a60c302999aa1e47896954369	1aaf9c5ce318a420e5cf0ae35ac7b55d8b967e9a60c302999aa1e47896954369	in use	13	1
100	2019-06-04 17:22:27.868	2019-06-04 17:22:27.868	80736d44e469e3b2be56718509c973aa3cddbb929140bc48be3b567279290b95	80736d44e469e3b2be56718509c973aa3cddbb929140bc48be3b567279290b95	in use	13	1
71	2019-06-04 17:22:27.831	2019-06-04 17:46:18.207	d25803857133dcb83328d775b3b5be179316df161a824cd6cfc39c2f3269eb63	45e0d1b6a52b07c586d209faee778553152fc35de09eaa3c93cb1cfd0bddd9be	in use	11	2
9	2019-06-04 17:22:27.744	2019-06-04 17:46:18.972	09bd064875230f05b7d184209b0cf569a2f4f063e62ae3a57db900fba99bea91	c338c3505dde835d6faf10f77704a133288bddb639f449a3dda1f72f62e210a8	in use	4	2
96	2019-06-04 17:22:27.864	2019-06-04 17:46:20.477	2f87731abfe8dd4cb6bb864740b9cf2acc74949116d7f9ee6d8687d8263aca73	3facbce7bd152c38e8fcb415144bf8948117d9db9471ac503c81811c504cad27	in use	13	2
\.


--
-- Data for Name: book_author; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_author (book_id, author_id) FROM stdin;
4	3
5	2
6	3
7	2
8	1
9	2
10	3
11	3
12	1
13	2
\.


--
-- Data for Name: book_category; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_category (book_id, category_id) FROM stdin;
4	1
5	3
6	2
7	1
8	3
9	3
10	3
11	3
12	3
13	2
\.


--
-- Data for Name: book_detail; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.book_detail (id, created_date, update_date, name, publisher_id) FROM stdin;
4	\N	2019-06-04 17:22:27.87	XML	2
5	\N	2019-06-04 17:22:27.873	C Sharp	1
6	\N	2019-06-04 17:22:27.874	JAVA	3
7	\N	2019-06-04 17:22:27.876	JAVASCRIPT	2
8	\N	2019-06-04 17:22:27.877	SPRING	4
9	\N	2019-06-04 17:22:27.879	BigchainDB	3
10	\N	2019-06-04 17:22:27.88	Data Structure	2
11	\N	2019-06-04 17:22:27.881	Algorithm	1
12	\N	2019-06-04 17:22:27.882	Network	4
13	\N	2019-06-04 17:22:27.883	Machine Learning	2
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
1	2019-06-04 17:21:36.295	2019-06-04 17:21:36.295	Computer Scienece
2	2019-06-04 17:21:36.301	2019-06-04 17:21:36.301	Software Engineering
3	2019-06-04 17:21:36.306	2019-06-04 17:21:36.306	Business Management
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
1	\N	\N	\N	\N	1	9	12	13
\.


--
-- Data for Name: publisher; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.publisher (id, created_date, update_date, name) FROM stdin;
1	2019-06-04 17:21:36.345	2019-06-04 17:21:36.345	Springer Nature
2	2019-06-04 17:21:36.351	2019-06-04 17:21:36.351	Scholastic
3	2019-06-04 17:21:36.356	2019-06-04 17:21:36.356	McGraw-Hill Education
4	2019-06-04 17:21:36.36	2019-06-04 17:21:36.36	HarperCollins
5	2019-06-04 17:21:36.365	2019-06-04 17:21:36.365	Cengage
\.


--
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.request (id, created_date, update_date, status, type, book_id, book_detail_id, user_id) FROM stdin;
1	2019-06-04 19:23:15.09	2019-06-04 19:23:15.09	1	2	5	\N	2
2	2019-06-04 19:45:29.163	2019-06-04 19:45:29.163	1	1	\N	7	2
3	2019-06-04 19:48:21.089	2019-06-04 19:48:21.089	1	1	\N	6	2
4	2019-06-04 19:51:30.432	2019-06-04 19:51:30.432	1	1	\N	11	2
5	2019-06-04 20:11:07.888	2019-06-04 20:11:07.888	1	1	\N	6	4
6	2019-06-05 03:24:58.442	2019-06-05 03:24:58.442	1	2	39	7	2
7	2019-06-05 03:39:42.753	2019-06-05 03:39:42.753	1	1	\N	4	2
8	2019-06-05 04:03:38.898	2019-06-05 04:03:38.898	1	2	96	13	2
9	2019-06-05 04:03:57.965	2019-06-05 04:03:57.965	1	2	71	11	2
10	2019-06-05 04:04:41.366	2019-06-05 04:04:41.366	1	1	\N	9	2
11	2019-06-05 04:05:29.642	2019-06-05 04:05:29.642	1	1	\N	10	6
12	2019-06-05 04:26:36.998	2019-06-05 04:26:36.998	1	1	\N	7	6
13	2019-06-04 21:45:34.607	2019-06-04 21:45:34.607	1	2	9	4	2
14	2019-06-05 16:07:55.971	2019-06-05 16:07:56.147	2	1	\N	13	4
\.


--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.role (id, created_date, update_date, role_name) FROM stdin;
\.


--
-- Data for Name: tbl_user; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tbl_user (id, created_date, update_date, email, fullname, password, phone) FROM stdin;
3	2019-06-04 18:05:20.338	2019-06-04 18:05:20.338	phongdv@fptu.tech	Đoàn Vũ Phong	$2a$10$SOJbGQK51Uwzbe.7xeARne6M/4NQFOXBNjWOOyXoLhWypp8HgQJsO	\N
6	2019-06-04 18:46:39.613	2019-06-04 18:46:39.613	huynhminhtufu@gmail.com	Huỳnh Minh Tú	$2a$10$bzDlOGl30gIhHdRC3Msk8.KBJa4BqZqJwz/zCKyt1oXQZVmYkh5a.	\N
4	2019-06-04 18:09:45.093	2019-06-04 18:09:45.093	phongdvse61654@fpt.edu.vn	Đoàn Vũ phong	$2a$10$1nlK0VzRQRuniSeA2BPR1ejrpSBJBIxuUxnghi0A7/0sPVj7/hbt2	\N
1	2019-06-04 17:21:23.957	2019-06-04 17:21:23.957	khanhkt@fe.edu.vn	Kiều Trọng Khánh	$2a$10$IY0ToCvnzYpHQB9YWhP.uu4QF9NEvrE4OQ87AVJ7kHZHqB.1U59AS	0345681294
5	2019-06-04 18:12:30.8	2019-06-04 18:12:30.8	nguoiMuonABC	Đoàn Vũ phong	$2a$10$WlbSx26VRqNgL2w6wgVNFu9NBfP0fJj5rC2oGdJKsRFXgtjS6WtCa	\N
2	2019-06-04 17:21:28.172	2019-06-04 17:21:28.172	tuhmse62531@fpt.edu.vn	Huỳnh Minh Tú	$2a$10$Od5qV9QjZRVqDpuecR2wgurrFAc1RSLiOBs79N2R5Ltu5j1gwDxwu	0345681294
\.


--
-- Data for Name: tbl_user_list_books; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.tbl_user_list_books (user_id, list_books_id) FROM stdin;
\.


--
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.transaction (id, created_date, update_date, bc_transaction_id, book_id, borrower_id, returner_id) FROM stdin;
\.


--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: -
--

COPY public.user_role (user_id, role_id) FROM stdin;
\.


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.category_id_seq', 3, true);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.hibernate_sequence', 13, true);


--
-- Name: image_url_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.image_url_id_seq', 1, false);


--
-- Name: matching_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.matching_id_seq', 1, false);


--
-- Name: publisher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.publisher_id_seq', 5, true);


--
-- Name: request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.request_id_seq', 15, true);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- Name: tbl_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.tbl_user_id_seq', 7, true);


--
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: -
--

SELECT pg_catalog.setval('public.transaction_id_seq', 1, false);


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
-- Name: tbl_user_list_books uk_p9mwbjxf78tqeke6idggt8kyu; Type: CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT uk_p9mwbjxf78tqeke6idggt8kyu UNIQUE (list_books_id);


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
-- Name: tbl_user_list_books fkapnc4sgw9i96oelca5uiuisnj; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT fkapnc4sgw9i96oelca5uiuisnj FOREIGN KEY (list_books_id) REFERENCES public.book(id);


--
-- Name: book_author fkbjqhp85wjv8vpr0beygh6jsgo; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo FOREIGN KEY (author_id) REFERENCES public.author(id);


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
-- Name: tbl_user_list_books fkjcohem7sn5xixprwln6c8yptr; Type: FK CONSTRAINT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT fkjcohem7sn5xixprwln6c8yptr FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


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