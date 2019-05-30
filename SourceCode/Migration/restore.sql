--
-- NOTE:
--
-- File paths need to be edited. Search for $$PATH$$ and
-- replace it with the path to the directory containing
-- the extracted data files.
--
--
-- PostgreSQL database dump
--

-- Dumped from database version 11.3
-- Dumped by pg_dump version 11.2

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

DROP DATABASE librarianmodule;
--
-- Name: librarianmodule; Type: DATABASE; Schema: -; Owner: postgres
--

CREATE DATABASE librarianmodule WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'en_US.utf8' LC_CTYPE = 'en_US.utf8';


ALTER DATABASE librarianmodule OWNER TO postgres;

\connect librarianmodule

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- Name: author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.author (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.author OWNER TO postgres;

--
-- Name: book; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book (
    id bigint NOT NULL,
    book_id bigint
);


ALTER TABLE public.book OWNER TO postgres;

--
-- Name: book_author; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_author (
    book_id bigint NOT NULL,
    author_id bigint NOT NULL
);


ALTER TABLE public.book_author OWNER TO postgres;

--
-- Name: book_category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_category (
    book_id bigint NOT NULL,
    category_id bigint NOT NULL
);


ALTER TABLE public.book_category OWNER TO postgres;

--
-- Name: book_detail; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_detail (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    name character varying(255),
    publisher_id bigint
);


ALTER TABLE public.book_detail OWNER TO postgres;

--
-- Name: book_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.book_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.book_id_seq OWNER TO postgres;

--
-- Name: book_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.book_id_seq OWNED BY public.book.id;


--
-- Name: book_requests; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_requests (
    book_id bigint NOT NULL,
    requests_id bigint NOT NULL
);


ALTER TABLE public.book_requests OWNER TO postgres;

--
-- Name: book_transactions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.book_transactions (
    book_id bigint NOT NULL,
    transactions_id bigint NOT NULL
);


ALTER TABLE public.book_transactions OWNER TO postgres;

--
-- Name: category; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.category OWNER TO postgres;

--
-- Name: category_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.category_id_seq OWNER TO postgres;

--
-- Name: category_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;


--
-- Name: hibernate_sequence; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.hibernate_sequence OWNER TO postgres;

--
-- Name: image_url; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.image_url (
    id bigint NOT NULL,
    url character varying(255),
    image_id bigint
);


ALTER TABLE public.image_url OWNER TO postgres;

--
-- Name: image_url_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.image_url_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.image_url_id_seq OWNER TO postgres;

--
-- Name: image_url_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.image_url_id_seq OWNED BY public.image_url.id;


--
-- Name: matching; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.matching (
    id bigint NOT NULL
);


ALTER TABLE public.matching OWNER TO postgres;

--
-- Name: matching_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.matching_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.matching_id_seq OWNER TO postgres;

--
-- Name: matching_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.matching_id_seq OWNED BY public.matching.id;


--
-- Name: publisher; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.publisher (
    id bigint NOT NULL,
    name character varying(255)
);


ALTER TABLE public.publisher OWNER TO postgres;

--
-- Name: publisher_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.publisher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.publisher_id_seq OWNER TO postgres;

--
-- Name: publisher_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.publisher_id_seq OWNED BY public.publisher.id;


--
-- Name: request; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.request (
    id bigint NOT NULL,
    status integer,
    book_id bigint,
    user_id bigint
);


ALTER TABLE public.request OWNER TO postgres;

--
-- Name: request_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.request_id_seq OWNER TO postgres;

--
-- Name: request_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.request_id_seq OWNED BY public.request.id;


--
-- Name: role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role (
    id bigint NOT NULL,
    role_name character varying(255)
);


ALTER TABLE public.role OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.role_id_seq OWNER TO postgres;

--
-- Name: role_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;


--
-- Name: tbl_user; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.tbl_user (
    id bigint NOT NULL,
    password character varying(255),
    username character varying(255)
);


ALTER TABLE public.tbl_user OWNER TO postgres;

--
-- Name: tbl_user_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.tbl_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.tbl_user_id_seq OWNER TO postgres;

--
-- Name: tbl_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.tbl_user_id_seq OWNED BY public.tbl_user.id;


--
-- Name: transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.transaction (
    id bigint NOT NULL,
    book_id bigint,
    borrower_id bigint,
    returner_id bigint
);


ALTER TABLE public.transaction OWNER TO postgres;

--
-- Name: transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.transaction_id_seq OWNER TO postgres;

--
-- Name: transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.transaction_id_seq OWNED BY public.transaction.id;


--
-- Name: user_role; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);


ALTER TABLE public.user_role OWNER TO postgres;

--
-- Name: book id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book ALTER COLUMN id SET DEFAULT nextval('public.book_id_seq'::regclass);


--
-- Name: category id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);


--
-- Name: image_url id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image_url ALTER COLUMN id SET DEFAULT nextval('public.image_url_id_seq'::regclass);


--
-- Name: matching id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matching ALTER COLUMN id SET DEFAULT nextval('public.matching_id_seq'::regclass);


--
-- Name: publisher id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher ALTER COLUMN id SET DEFAULT nextval('public.publisher_id_seq'::regclass);


--
-- Name: request id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request ALTER COLUMN id SET DEFAULT nextval('public.request_id_seq'::regclass);


--
-- Name: role id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);


--
-- Name: tbl_user id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_user ALTER COLUMN id SET DEFAULT nextval('public.tbl_user_id_seq'::regclass);


--
-- Name: transaction id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction ALTER COLUMN id SET DEFAULT nextval('public.transaction_id_seq'::regclass);


--
-- Data for Name: author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.author (id, name) FROM stdin;
\.
COPY public.author (id, name) FROM '$$PATH$$/3018.dat';

--
-- Data for Name: book; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book (id, book_id) FROM stdin;
\.
COPY public.book (id, book_id) FROM '$$PATH$$/3020.dat';

--
-- Data for Name: book_author; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_author (book_id, author_id) FROM stdin;
\.
COPY public.book_author (book_id, author_id) FROM '$$PATH$$/3021.dat';

--
-- Data for Name: book_category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_category (book_id, category_id) FROM stdin;
\.
COPY public.book_category (book_id, category_id) FROM '$$PATH$$/3022.dat';

--
-- Data for Name: book_detail; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_detail (id, created_at, name, publisher_id) FROM stdin;
\.
COPY public.book_detail (id, created_at, name, publisher_id) FROM '$$PATH$$/3023.dat';

--
-- Data for Name: book_requests; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_requests (book_id, requests_id) FROM stdin;
\.
COPY public.book_requests (book_id, requests_id) FROM '$$PATH$$/3024.dat';

--
-- Data for Name: book_transactions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.book_transactions (book_id, transactions_id) FROM stdin;
\.
COPY public.book_transactions (book_id, transactions_id) FROM '$$PATH$$/3025.dat';

--
-- Data for Name: category; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.category (id, name) FROM stdin;
\.
COPY public.category (id, name) FROM '$$PATH$$/3027.dat';

--
-- Data for Name: image_url; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.image_url (id, url, image_id) FROM stdin;
\.
COPY public.image_url (id, url, image_id) FROM '$$PATH$$/3029.dat';

--
-- Data for Name: matching; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.matching (id) FROM stdin;
\.
COPY public.matching (id) FROM '$$PATH$$/3031.dat';

--
-- Data for Name: publisher; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.publisher (id, name) FROM stdin;
\.
COPY public.publisher (id, name) FROM '$$PATH$$/3033.dat';

--
-- Data for Name: request; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.request (id, status, book_id, user_id) FROM stdin;
\.
COPY public.request (id, status, book_id, user_id) FROM '$$PATH$$/3035.dat';

--
-- Data for Name: role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role (id, role_name) FROM stdin;
\.
COPY public.role (id, role_name) FROM '$$PATH$$/3037.dat';

--
-- Data for Name: tbl_user; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.tbl_user (id, password, username) FROM stdin;
\.
COPY public.tbl_user (id, password, username) FROM '$$PATH$$/3039.dat';

--
-- Data for Name: transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.transaction (id, book_id, borrower_id, returner_id) FROM stdin;
\.
COPY public.transaction (id, book_id, borrower_id, returner_id) FROM '$$PATH$$/3041.dat';

--
-- Data for Name: user_role; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_role (user_id, role_id) FROM stdin;
\.
COPY public.user_role (user_id, role_id) FROM '$$PATH$$/3042.dat';

--
-- Name: book_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.book_id_seq', 100, true);


--
-- Name: category_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.category_id_seq', 3, true);


--
-- Name: hibernate_sequence; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.hibernate_sequence', 13, true);


--
-- Name: image_url_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.image_url_id_seq', 1, false);


--
-- Name: matching_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.matching_id_seq', 1, false);


--
-- Name: publisher_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.publisher_id_seq', 5, true);


--
-- Name: request_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.request_id_seq', 1, false);


--
-- Name: role_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_id_seq', 1, false);


--
-- Name: tbl_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.tbl_user_id_seq', 1, false);


--
-- Name: transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.transaction_id_seq', 1, false);


--
-- Name: author author_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);


--
-- Name: book_detail book_detail_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT book_detail_pkey PRIMARY KEY (id);


--
-- Name: book book_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);


--
-- Name: category category_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);


--
-- Name: image_url image_url_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT image_url_pkey PRIMARY KEY (id);


--
-- Name: matching matching_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.matching
    ADD CONSTRAINT matching_pkey PRIMARY KEY (id);


--
-- Name: publisher publisher_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (id);


--
-- Name: request request_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT request_pkey PRIMARY KEY (id);


--
-- Name: role role_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);


--
-- Name: tbl_user tbl_user_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT tbl_user_pkey PRIMARY KEY (id);


--
-- Name: transaction transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);


--
-- Name: book_requests uk_47om0mhw8u0g70rmjnw77ncj1; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT uk_47om0mhw8u0g70rmjnw77ncj1 UNIQUE (requests_id);


--
-- Name: tbl_user uk_k0bty7tbcye41jpxam88q5kj2; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT uk_k0bty7tbcye41jpxam88q5kj2 UNIQUE (username);


--
-- Name: book_transactions uk_lmabmxmxt4kowyf1bdofepkpk; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT uk_lmabmxmxt4kowyf1bdofepkpk UNIQUE (transactions_id);


--
-- Name: book fk2qb5qwg79ico56fxeryoqnoh8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book
    ADD CONSTRAINT fk2qb5qwg79ico56fxeryoqnoh8 FOREIGN KEY (book_id) REFERENCES public.book_detail(id);


--
-- Name: transaction fk8hddvclv2iqa3sg1dm8295pqw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk8hddvclv2iqa3sg1dm8295pqw FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: user_role fka68196081fvovjhkek5m97n3y; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);


--
-- Name: book_category fkam8llderp40mvbbwceqpu6l2s; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkam8llderp40mvbbwceqpu6l2s FOREIGN KEY (category_id) REFERENCES public.category(id);


--
-- Name: book_author fkbjqhp85wjv8vpr0beygh6jsgo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo FOREIGN KEY (author_id) REFERENCES public.author(id);


--
-- Name: book_transactions fkciir81r1ufgvlxa0mvu604pi2; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkciir81r1ufgvlxa0mvu604pi2 FOREIGN KEY (transactions_id) REFERENCES public.transaction(id);


--
-- Name: book_detail fkejp3y3fuwh494hor916ukyhkd; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT fkejp3y3fuwh494hor916ukyhkd FOREIGN KEY (publisher_id) REFERENCES public.publisher(id);


--
-- Name: image_url fkgy9ednxit1eycvqhko9pk9qnf; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT fkgy9ednxit1eycvqhko9pk9qnf FOREIGN KEY (image_id) REFERENCES public.transaction(id);


--
-- Name: book_transactions fkhticlf2fcsqfpbs92vkskpl2p; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkhticlf2fcsqfpbs92vkskpl2p FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book_author fkid4p9ic5tssamvk0m3f53p0b8; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkid4p9ic5tssamvk0m3f53p0b8 FOREIGN KEY (book_id) REFERENCES public.book_detail(id);


--
-- Name: transaction fkjr3i9hlgcyfid355yqrwrn1fr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkjr3i9hlgcyfid355yqrwrn1fr FOREIGN KEY (borrower_id) REFERENCES public.tbl_user(id);


--
-- Name: request fkjsjmh09dpj6tlqrf3w6e4vror; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkjsjmh09dpj6tlqrf3w6e4vror FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


--
-- Name: request fkk5wfoqkewjhptig7bdke6h2ie; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkk5wfoqkewjhptig7bdke6h2ie FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book_requests fkkgeplcw85nr006v3nlc5dsfu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkkgeplcw85nr006v3nlc5dsfu FOREIGN KEY (book_id) REFERENCES public.book(id);


--
-- Name: book_requests fkofxcm11nyp348j1s7t5ar5c7n; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkofxcm11nyp348j1s7t5ar5c7n FOREIGN KEY (requests_id) REFERENCES public.request(id);


--
-- Name: user_role fkoqn0vpy8dpjr6u7kk4lqh15cq; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkoqn0vpy8dpjr6u7kk4lqh15cq FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);


--
-- Name: transaction fkpm7o8a3dtg6dc76480k86c5ca; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkpm7o8a3dtg6dc76480k86c5ca FOREIGN KEY (returner_id) REFERENCES public.tbl_user(id);


--
-- Name: book_category fkr0ffi36oms3vuod94n27aw89h; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkr0ffi36oms3vuod94n27aw89h FOREIGN KEY (book_id) REFERENCES public.book_detail(id);


--
-- PostgreSQL database dump complete
--

