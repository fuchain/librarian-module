PGDMP         3                w            mydb    11.3    11.3 p    �           0    0    ENCODING    ENCODING        SET client_encoding = 'UTF8';
                       false            �           0    0 
   STDSTRINGS 
   STDSTRINGS     (   SET standard_conforming_strings = 'on';
                       false            �           0    0 
   SEARCHPATH 
   SEARCHPATH     8   SELECT pg_catalog.set_config('search_path', '', false);
                       false            �           1262    16393    mydb    DATABASE     �   CREATE DATABASE mydb WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';
    DROP DATABASE mydb;
             postgres    false            �            1259    28427    author    TABLE     X   CREATE TABLE public.author (
    id bigint NOT NULL,
    name character varying(255)
);
    DROP TABLE public.author;
       public         postgres    false            �            1259    28432    book    TABLE     �   CREATE TABLE public.book (
    id bigint NOT NULL,
    asset_id character varying(255),
    previous_tx_id character varying(255),
    status character varying(255),
    bookdetail_id bigint,
    user_id bigint
);
    DROP TABLE public.book;
       public         postgres    false            �            1259    28440    book_author    TABLE     `   CREATE TABLE public.book_author (
    book_id bigint NOT NULL,
    author_id bigint NOT NULL
);
    DROP TABLE public.book_author;
       public         postgres    false            �            1259    28443    book_category    TABLE     d   CREATE TABLE public.book_category (
    book_id bigint NOT NULL,
    category_id bigint NOT NULL
);
 !   DROP TABLE public.book_category;
       public         postgres    false            �            1259    28446    book_detail    TABLE     �   CREATE TABLE public.book_detail (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    name character varying(255),
    publisher_id bigint
);
    DROP TABLE public.book_detail;
       public         postgres    false            �            1259    28451    book_requests    TABLE     d   CREATE TABLE public.book_requests (
    book_id bigint NOT NULL,
    requests_id bigint NOT NULL
);
 !   DROP TABLE public.book_requests;
       public         postgres    false            �            1259    28454    book_transactions    TABLE     l   CREATE TABLE public.book_transactions (
    book_id bigint NOT NULL,
    transactions_id bigint NOT NULL
);
 %   DROP TABLE public.book_transactions;
       public         postgres    false            �            1259    28459    category    TABLE     Z   CREATE TABLE public.category (
    id bigint NOT NULL,
    name character varying(255)
);
    DROP TABLE public.category;
       public         postgres    false            �            1259    28457    category_id_seq    SEQUENCE     x   CREATE SEQUENCE public.category_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.category_id_seq;
       public       postgres    false    205            �           0    0    category_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.category_id_seq OWNED BY public.category.id;
            public       postgres    false    204            �            1259    28425    hibernate_sequence    SEQUENCE     {   CREATE SEQUENCE public.hibernate_sequence
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.hibernate_sequence;
       public       postgres    false            �            1259    28467 	   image_url    TABLE     o   CREATE TABLE public.image_url (
    id bigint NOT NULL,
    url character varying(255),
    image_id bigint
);
    DROP TABLE public.image_url;
       public         postgres    false            �            1259    28465    image_url_id_seq    SEQUENCE     y   CREATE SEQUENCE public.image_url_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.image_url_id_seq;
       public       postgres    false    207            �           0    0    image_url_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.image_url_id_seq OWNED BY public.image_url.id;
            public       postgres    false    206            �            1259    28475    matching    TABLE     �   CREATE TABLE public.matching (
    id bigint NOT NULL,
    created_at timestamp without time zone,
    pin character varying(255),
    status integer,
    book_id bigint,
    borrower_request_id bigint,
    returner_request_id bigint
);
    DROP TABLE public.matching;
       public         postgres    false            �            1259    28473    matching_id_seq    SEQUENCE     x   CREATE SEQUENCE public.matching_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.matching_id_seq;
       public       postgres    false    209            �           0    0    matching_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.matching_id_seq OWNED BY public.matching.id;
            public       postgres    false    208            �            1259    28483 	   publisher    TABLE     [   CREATE TABLE public.publisher (
    id bigint NOT NULL,
    name character varying(255)
);
    DROP TABLE public.publisher;
       public         postgres    false            �            1259    28481    publisher_id_seq    SEQUENCE     y   CREATE SEQUENCE public.publisher_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 '   DROP SEQUENCE public.publisher_id_seq;
       public       postgres    false    211            �           0    0    publisher_id_seq    SEQUENCE OWNED BY     E   ALTER SEQUENCE public.publisher_id_seq OWNED BY public.publisher.id;
            public       postgres    false    210            �            1259    28491    request    TABLE     �   CREATE TABLE public.request (
    id bigint NOT NULL,
    created_date timestamp without time zone,
    status integer,
    type integer,
    update_date timestamp without time zone,
    book_id bigint,
    book_detail_id bigint,
    user_id bigint
);
    DROP TABLE public.request;
       public         postgres    false            �            1259    28489    request_id_seq    SEQUENCE     w   CREATE SEQUENCE public.request_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 %   DROP SEQUENCE public.request_id_seq;
       public       postgres    false    213            �           0    0    request_id_seq    SEQUENCE OWNED BY     A   ALTER SEQUENCE public.request_id_seq OWNED BY public.request.id;
            public       postgres    false    212            �            1259    28499    role    TABLE     [   CREATE TABLE public.role (
    id bigint NOT NULL,
    role_name character varying(255)
);
    DROP TABLE public.role;
       public         postgres    false            �            1259    28497    role_id_seq    SEQUENCE     t   CREATE SEQUENCE public.role_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 "   DROP SEQUENCE public.role_id_seq;
       public       postgres    false    215            �           0    0    role_id_seq    SEQUENCE OWNED BY     ;   ALTER SEQUENCE public.role_id_seq OWNED BY public.role.id;
            public       postgres    false    214            �            1259    28507    tbl_user    TABLE     �   CREATE TABLE public.tbl_user (
    id bigint NOT NULL,
    email character varying(255),
    fullname character varying(255),
    password character varying(255)
);
    DROP TABLE public.tbl_user;
       public         postgres    false            �            1259    28505    tbl_user_id_seq    SEQUENCE     x   CREATE SEQUENCE public.tbl_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 &   DROP SEQUENCE public.tbl_user_id_seq;
       public       postgres    false    217            �           0    0    tbl_user_id_seq    SEQUENCE OWNED BY     C   ALTER SEQUENCE public.tbl_user_id_seq OWNED BY public.tbl_user.id;
            public       postgres    false    216            �            1259    28516    tbl_user_list_books    TABLE     l   CREATE TABLE public.tbl_user_list_books (
    user_id bigint NOT NULL,
    list_books_id bigint NOT NULL
);
 '   DROP TABLE public.tbl_user_list_books;
       public         postgres    false            �            1259    28521    transaction    TABLE     �   CREATE TABLE public.transaction (
    id bigint NOT NULL,
    book_id bigint,
    borrower_id bigint,
    returner_id bigint
);
    DROP TABLE public.transaction;
       public         postgres    false            �            1259    28519    transaction_id_seq    SEQUENCE     {   CREATE SEQUENCE public.transaction_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;
 )   DROP SEQUENCE public.transaction_id_seq;
       public       postgres    false    220            �           0    0    transaction_id_seq    SEQUENCE OWNED BY     I   ALTER SEQUENCE public.transaction_id_seq OWNED BY public.transaction.id;
            public       postgres    false    219            �            1259    28527 	   user_role    TABLE     \   CREATE TABLE public.user_role (
    user_id bigint NOT NULL,
    role_id bigint NOT NULL
);
    DROP TABLE public.user_role;
       public         postgres    false            �
           2604    28462    category id    DEFAULT     j   ALTER TABLE ONLY public.category ALTER COLUMN id SET DEFAULT nextval('public.category_id_seq'::regclass);
 :   ALTER TABLE public.category ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    205    204    205            �
           2604    28470    image_url id    DEFAULT     l   ALTER TABLE ONLY public.image_url ALTER COLUMN id SET DEFAULT nextval('public.image_url_id_seq'::regclass);
 ;   ALTER TABLE public.image_url ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    206    207    207            �
           2604    28478    matching id    DEFAULT     j   ALTER TABLE ONLY public.matching ALTER COLUMN id SET DEFAULT nextval('public.matching_id_seq'::regclass);
 :   ALTER TABLE public.matching ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    208    209    209            �
           2604    28486    publisher id    DEFAULT     l   ALTER TABLE ONLY public.publisher ALTER COLUMN id SET DEFAULT nextval('public.publisher_id_seq'::regclass);
 ;   ALTER TABLE public.publisher ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    210    211    211            �
           2604    28494 
   request id    DEFAULT     h   ALTER TABLE ONLY public.request ALTER COLUMN id SET DEFAULT nextval('public.request_id_seq'::regclass);
 9   ALTER TABLE public.request ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    213    212    213            �
           2604    28502    role id    DEFAULT     b   ALTER TABLE ONLY public.role ALTER COLUMN id SET DEFAULT nextval('public.role_id_seq'::regclass);
 6   ALTER TABLE public.role ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    214    215    215            �
           2604    28510    tbl_user id    DEFAULT     j   ALTER TABLE ONLY public.tbl_user ALTER COLUMN id SET DEFAULT nextval('public.tbl_user_id_seq'::regclass);
 :   ALTER TABLE public.tbl_user ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    217    216    217            �
           2604    28524    transaction id    DEFAULT     p   ALTER TABLE ONLY public.transaction ALTER COLUMN id SET DEFAULT nextval('public.transaction_id_seq'::regclass);
 =   ALTER TABLE public.transaction ALTER COLUMN id DROP DEFAULT;
       public       postgres    false    220    219    220            �          0    28427    author 
   TABLE DATA               *   COPY public.author (id, name) FROM stdin;
    public       postgres    false    197   ��       �          0    28432    book 
   TABLE DATA               \   COPY public.book (id, asset_id, previous_tx_id, status, bookdetail_id, user_id) FROM stdin;
    public       postgres    false    198   À       �          0    28440    book_author 
   TABLE DATA               9   COPY public.book_author (book_id, author_id) FROM stdin;
    public       postgres    false    199   J�       �          0    28443    book_category 
   TABLE DATA               =   COPY public.book_category (book_id, category_id) FROM stdin;
    public       postgres    false    200   k�       �          0    28446    book_detail 
   TABLE DATA               K   COPY public.book_detail (id, created_date, name, publisher_id) FROM stdin;
    public       postgres    false    201   ��       �          0    28451    book_requests 
   TABLE DATA               =   COPY public.book_requests (book_id, requests_id) FROM stdin;
    public       postgres    false    202   Ɂ       �          0    28454    book_transactions 
   TABLE DATA               E   COPY public.book_transactions (book_id, transactions_id) FROM stdin;
    public       postgres    false    203   �       �          0    28459    category 
   TABLE DATA               ,   COPY public.category (id, name) FROM stdin;
    public       postgres    false    205   �       �          0    28467 	   image_url 
   TABLE DATA               6   COPY public.image_url (id, url, image_id) FROM stdin;
    public       postgres    false    207   4�       �          0    28475    matching 
   TABLE DATA               r   COPY public.matching (id, created_at, pin, status, book_id, borrower_request_id, returner_request_id) FROM stdin;
    public       postgres    false    209   Q�       �          0    28483 	   publisher 
   TABLE DATA               -   COPY public.publisher (id, name) FROM stdin;
    public       postgres    false    211   ��       �          0    28491    request 
   TABLE DATA               p   COPY public.request (id, created_date, status, type, update_date, book_id, book_detail_id, user_id) FROM stdin;
    public       postgres    false    213   Ă       �          0    28499    role 
   TABLE DATA               -   COPY public.role (id, role_name) FROM stdin;
    public       postgres    false    215    �       �          0    28507    tbl_user 
   TABLE DATA               A   COPY public.tbl_user (id, email, fullname, password) FROM stdin;
    public       postgres    false    217   =�       �          0    28516    tbl_user_list_books 
   TABLE DATA               E   COPY public.tbl_user_list_books (user_id, list_books_id) FROM stdin;
    public       postgres    false    218   ݃       �          0    28521    transaction 
   TABLE DATA               L   COPY public.transaction (id, book_id, borrower_id, returner_id) FROM stdin;
    public       postgres    false    220   ��       �          0    28527 	   user_role 
   TABLE DATA               5   COPY public.user_role (user_id, role_id) FROM stdin;
    public       postgres    false    221   -�       �           0    0    category_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.category_id_seq', 1, true);
            public       postgres    false    204            �           0    0    hibernate_sequence    SEQUENCE SET     @   SELECT pg_catalog.setval('public.hibernate_sequence', 2, true);
            public       postgres    false    196            �           0    0    image_url_id_seq    SEQUENCE SET     ?   SELECT pg_catalog.setval('public.image_url_id_seq', 1, false);
            public       postgres    false    206            �           0    0    matching_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.matching_id_seq', 1, false);
            public       postgres    false    208            �           0    0    publisher_id_seq    SEQUENCE SET     >   SELECT pg_catalog.setval('public.publisher_id_seq', 1, true);
            public       postgres    false    210            �           0    0    request_id_seq    SEQUENCE SET     <   SELECT pg_catalog.setval('public.request_id_seq', 8, true);
            public       postgres    false    212            �           0    0    role_id_seq    SEQUENCE SET     :   SELECT pg_catalog.setval('public.role_id_seq', 1, false);
            public       postgres    false    214            �           0    0    tbl_user_id_seq    SEQUENCE SET     =   SELECT pg_catalog.setval('public.tbl_user_id_seq', 2, true);
            public       postgres    false    216            �           0    0    transaction_id_seq    SEQUENCE SET     @   SELECT pg_catalog.setval('public.transaction_id_seq', 4, true);
            public       postgres    false    219            �
           2606    28431    author author_pkey 
   CONSTRAINT     P   ALTER TABLE ONLY public.author
    ADD CONSTRAINT author_pkey PRIMARY KEY (id);
 <   ALTER TABLE ONLY public.author DROP CONSTRAINT author_pkey;
       public         postgres    false    197            �
           2606    28450    book_detail book_detail_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT book_detail_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.book_detail DROP CONSTRAINT book_detail_pkey;
       public         postgres    false    201            �
           2606    28439    book book_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.book
    ADD CONSTRAINT book_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.book DROP CONSTRAINT book_pkey;
       public         postgres    false    198            �
           2606    28464    category category_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.category
    ADD CONSTRAINT category_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.category DROP CONSTRAINT category_pkey;
       public         postgres    false    205            �
           2606    28472    image_url image_url_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT image_url_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.image_url DROP CONSTRAINT image_url_pkey;
       public         postgres    false    207            �
           2606    28480    matching matching_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.matching
    ADD CONSTRAINT matching_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.matching DROP CONSTRAINT matching_pkey;
       public         postgres    false    209            �
           2606    28488    publisher publisher_pkey 
   CONSTRAINT     V   ALTER TABLE ONLY public.publisher
    ADD CONSTRAINT publisher_pkey PRIMARY KEY (id);
 B   ALTER TABLE ONLY public.publisher DROP CONSTRAINT publisher_pkey;
       public         postgres    false    211            �
           2606    28496    request request_pkey 
   CONSTRAINT     R   ALTER TABLE ONLY public.request
    ADD CONSTRAINT request_pkey PRIMARY KEY (id);
 >   ALTER TABLE ONLY public.request DROP CONSTRAINT request_pkey;
       public         postgres    false    213            �
           2606    28504    role role_pkey 
   CONSTRAINT     L   ALTER TABLE ONLY public.role
    ADD CONSTRAINT role_pkey PRIMARY KEY (id);
 8   ALTER TABLE ONLY public.role DROP CONSTRAINT role_pkey;
       public         postgres    false    215            �
           2606    28515    tbl_user tbl_user_pkey 
   CONSTRAINT     T   ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT tbl_user_pkey PRIMARY KEY (id);
 @   ALTER TABLE ONLY public.tbl_user DROP CONSTRAINT tbl_user_pkey;
       public         postgres    false    217            �
           2606    28526    transaction transaction_pkey 
   CONSTRAINT     Z   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT transaction_pkey PRIMARY KEY (id);
 F   ALTER TABLE ONLY public.transaction DROP CONSTRAINT transaction_pkey;
       public         postgres    false    220            �
           2606    28531 *   book_requests uk_47om0mhw8u0g70rmjnw77ncj1 
   CONSTRAINT     l   ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT uk_47om0mhw8u0g70rmjnw77ncj1 UNIQUE (requests_id);
 T   ALTER TABLE ONLY public.book_requests DROP CONSTRAINT uk_47om0mhw8u0g70rmjnw77ncj1;
       public         postgres    false    202            �
           2606    28533 .   book_transactions uk_lmabmxmxt4kowyf1bdofepkpk 
   CONSTRAINT     t   ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT uk_lmabmxmxt4kowyf1bdofepkpk UNIQUE (transactions_id);
 X   ALTER TABLE ONLY public.book_transactions DROP CONSTRAINT uk_lmabmxmxt4kowyf1bdofepkpk;
       public         postgres    false    203            �
           2606    28535 %   tbl_user uk_npn1wf1yu1g5rjohbek375pp1 
   CONSTRAINT     a   ALTER TABLE ONLY public.tbl_user
    ADD CONSTRAINT uk_npn1wf1yu1g5rjohbek375pp1 UNIQUE (email);
 O   ALTER TABLE ONLY public.tbl_user DROP CONSTRAINT uk_npn1wf1yu1g5rjohbek375pp1;
       public         postgres    false    217            �
           2606    28537 0   tbl_user_list_books uk_p9mwbjxf78tqeke6idggt8kyu 
   CONSTRAINT     t   ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT uk_p9mwbjxf78tqeke6idggt8kyu UNIQUE (list_books_id);
 Z   ALTER TABLE ONLY public.tbl_user_list_books DROP CONSTRAINT uk_p9mwbjxf78tqeke6idggt8kyu;
       public         postgres    false    218            �
           2606    28538     book fk3xigoma0tt7uvhy5693m6b7jx    FK CONSTRAINT     �   ALTER TABLE ONLY public.book
    ADD CONSTRAINT fk3xigoma0tt7uvhy5693m6b7jx FOREIGN KEY (bookdetail_id) REFERENCES public.book_detail(id);
 J   ALTER TABLE ONLY public.book DROP CONSTRAINT fk3xigoma0tt7uvhy5693m6b7jx;
       public       postgres    false    198    201    2780                       2606    28608 $   matching fk5i26fswphht7yuqcwe1l0u68m    FK CONSTRAINT     �   ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fk5i26fswphht7yuqcwe1l0u68m FOREIGN KEY (returner_request_id) REFERENCES public.request(id);
 N   ALTER TABLE ONLY public.matching DROP CONSTRAINT fk5i26fswphht7yuqcwe1l0u68m;
       public       postgres    false    213    2794    209            	           2606    28638 '   transaction fk8hddvclv2iqa3sg1dm8295pqw    FK CONSTRAINT     �   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fk8hddvclv2iqa3sg1dm8295pqw FOREIGN KEY (book_id) REFERENCES public.book(id);
 Q   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fk8hddvclv2iqa3sg1dm8295pqw;
       public       postgres    false    198    220    2778                       2606    28653 %   user_role fka68196081fvovjhkek5m97n3y    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fka68196081fvovjhkek5m97n3y FOREIGN KEY (role_id) REFERENCES public.role(id);
 O   ALTER TABLE ONLY public.user_role DROP CONSTRAINT fka68196081fvovjhkek5m97n3y;
       public       postgres    false    2796    221    215            �
           2606    28558 )   book_category fkam8llderp40mvbbwceqpu6l2s    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkam8llderp40mvbbwceqpu6l2s FOREIGN KEY (category_id) REFERENCES public.category(id);
 S   ALTER TABLE ONLY public.book_category DROP CONSTRAINT fkam8llderp40mvbbwceqpu6l2s;
       public       postgres    false    200    205    2786                       2606    28628 /   tbl_user_list_books fkapnc4sgw9i96oelca5uiuisnj    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT fkapnc4sgw9i96oelca5uiuisnj FOREIGN KEY (list_books_id) REFERENCES public.book(id);
 Y   ALTER TABLE ONLY public.tbl_user_list_books DROP CONSTRAINT fkapnc4sgw9i96oelca5uiuisnj;
       public       postgres    false    218    198    2778            �
           2606    28548 '   book_author fkbjqhp85wjv8vpr0beygh6jsgo    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo FOREIGN KEY (author_id) REFERENCES public.author(id);
 Q   ALTER TABLE ONLY public.book_author DROP CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo;
       public       postgres    false    197    2776    199            �
           2606    28583 -   book_transactions fkciir81r1ufgvlxa0mvu604pi2    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkciir81r1ufgvlxa0mvu604pi2 FOREIGN KEY (transactions_id) REFERENCES public.transaction(id);
 W   ALTER TABLE ONLY public.book_transactions DROP CONSTRAINT fkciir81r1ufgvlxa0mvu604pi2;
       public       postgres    false    220    203    2804            �
           2606    28568 '   book_detail fkejp3y3fuwh494hor916ukyhkd    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_detail
    ADD CONSTRAINT fkejp3y3fuwh494hor916ukyhkd FOREIGN KEY (publisher_id) REFERENCES public.publisher(id);
 Q   ALTER TABLE ONLY public.book_detail DROP CONSTRAINT fkejp3y3fuwh494hor916ukyhkd;
       public       postgres    false    201    211    2792                        2606    28593 %   image_url fkgy9ednxit1eycvqhko9pk9qnf    FK CONSTRAINT     �   ALTER TABLE ONLY public.image_url
    ADD CONSTRAINT fkgy9ednxit1eycvqhko9pk9qnf FOREIGN KEY (image_id) REFERENCES public.transaction(id);
 O   ALTER TABLE ONLY public.image_url DROP CONSTRAINT fkgy9ednxit1eycvqhko9pk9qnf;
       public       postgres    false    2804    220    207            �
           2606    28588 -   book_transactions fkhticlf2fcsqfpbs92vkskpl2p    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_transactions
    ADD CONSTRAINT fkhticlf2fcsqfpbs92vkskpl2p FOREIGN KEY (book_id) REFERENCES public.book(id);
 W   ALTER TABLE ONLY public.book_transactions DROP CONSTRAINT fkhticlf2fcsqfpbs92vkskpl2p;
       public       postgres    false    203    2778    198            �
           2606    28553 '   book_author fkid4p9ic5tssamvk0m3f53p0b8    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_author
    ADD CONSTRAINT fkid4p9ic5tssamvk0m3f53p0b8 FOREIGN KEY (book_id) REFERENCES public.book_detail(id);
 Q   ALTER TABLE ONLY public.book_author DROP CONSTRAINT fkid4p9ic5tssamvk0m3f53p0b8;
       public       postgres    false    199    201    2780                       2606    28633 /   tbl_user_list_books fkjcohem7sn5xixprwln6c8yptr    FK CONSTRAINT     �   ALTER TABLE ONLY public.tbl_user_list_books
    ADD CONSTRAINT fkjcohem7sn5xixprwln6c8yptr FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);
 Y   ALTER TABLE ONLY public.tbl_user_list_books DROP CONSTRAINT fkjcohem7sn5xixprwln6c8yptr;
       public       postgres    false    217    2798    218            
           2606    28643 '   transaction fkjr3i9hlgcyfid355yqrwrn1fr    FK CONSTRAINT     �   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkjr3i9hlgcyfid355yqrwrn1fr FOREIGN KEY (borrower_id) REFERENCES public.tbl_user(id);
 Q   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkjr3i9hlgcyfid355yqrwrn1fr;
       public       postgres    false    220    217    2798                       2606    28623 #   request fkjsjmh09dpj6tlqrf3w6e4vror    FK CONSTRAINT     �   ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkjsjmh09dpj6tlqrf3w6e4vror FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);
 M   ALTER TABLE ONLY public.request DROP CONSTRAINT fkjsjmh09dpj6tlqrf3w6e4vror;
       public       postgres    false    217    2798    213                       2606    28613 #   request fkk5wfoqkewjhptig7bdke6h2ie    FK CONSTRAINT     �   ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkk5wfoqkewjhptig7bdke6h2ie FOREIGN KEY (book_id) REFERENCES public.book(id);
 M   ALTER TABLE ONLY public.request DROP CONSTRAINT fkk5wfoqkewjhptig7bdke6h2ie;
       public       postgres    false    198    2778    213            �
           2606    28578 (   book_requests fkkgeplcw85nr006v3nlc5dsfu    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkkgeplcw85nr006v3nlc5dsfu FOREIGN KEY (book_id) REFERENCES public.book(id);
 R   ALTER TABLE ONLY public.book_requests DROP CONSTRAINT fkkgeplcw85nr006v3nlc5dsfu;
       public       postgres    false    202    2778    198                       2606    28618 #   request fkl44p0ukal7gxoa0rabxivw34e    FK CONSTRAINT     �   ALTER TABLE ONLY public.request
    ADD CONSTRAINT fkl44p0ukal7gxoa0rabxivw34e FOREIGN KEY (book_detail_id) REFERENCES public.book_detail(id);
 M   ALTER TABLE ONLY public.request DROP CONSTRAINT fkl44p0ukal7gxoa0rabxivw34e;
       public       postgres    false    201    2780    213                       2606    28603 $   matching fkmocchkas2lol67omhlbfybgpu    FK CONSTRAINT     �   ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fkmocchkas2lol67omhlbfybgpu FOREIGN KEY (borrower_request_id) REFERENCES public.request(id);
 N   ALTER TABLE ONLY public.matching DROP CONSTRAINT fkmocchkas2lol67omhlbfybgpu;
       public       postgres    false    2794    213    209            �
           2606    28573 )   book_requests fkofxcm11nyp348j1s7t5ar5c7n    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_requests
    ADD CONSTRAINT fkofxcm11nyp348j1s7t5ar5c7n FOREIGN KEY (requests_id) REFERENCES public.request(id);
 S   ALTER TABLE ONLY public.book_requests DROP CONSTRAINT fkofxcm11nyp348j1s7t5ar5c7n;
       public       postgres    false    202    2794    213                       2606    28658 %   user_role fkoqn0vpy8dpjr6u7kk4lqh15cq    FK CONSTRAINT     �   ALTER TABLE ONLY public.user_role
    ADD CONSTRAINT fkoqn0vpy8dpjr6u7kk4lqh15cq FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);
 O   ALTER TABLE ONLY public.user_role DROP CONSTRAINT fkoqn0vpy8dpjr6u7kk4lqh15cq;
       public       postgres    false    221    217    2798                       2606    28648 '   transaction fkpm7o8a3dtg6dc76480k86c5ca    FK CONSTRAINT     �   ALTER TABLE ONLY public.transaction
    ADD CONSTRAINT fkpm7o8a3dtg6dc76480k86c5ca FOREIGN KEY (returner_id) REFERENCES public.tbl_user(id);
 Q   ALTER TABLE ONLY public.transaction DROP CONSTRAINT fkpm7o8a3dtg6dc76480k86c5ca;
       public       postgres    false    220    2798    217            �
           2606    28563 )   book_category fkr0ffi36oms3vuod94n27aw89h    FK CONSTRAINT     �   ALTER TABLE ONLY public.book_category
    ADD CONSTRAINT fkr0ffi36oms3vuod94n27aw89h FOREIGN KEY (book_id) REFERENCES public.book_detail(id);
 S   ALTER TABLE ONLY public.book_category DROP CONSTRAINT fkr0ffi36oms3vuod94n27aw89h;
       public       postgres    false    200    201    2780                       2606    28598 $   matching fkrb4g7y9fmd270c8f53d34q4fb    FK CONSTRAINT     �   ALTER TABLE ONLY public.matching
    ADD CONSTRAINT fkrb4g7y9fmd270c8f53d34q4fb FOREIGN KEY (book_id) REFERENCES public.book(id);
 N   ALTER TABLE ONLY public.matching DROP CONSTRAINT fkrb4g7y9fmd270c8f53d34q4fb;
       public       postgres    false    2778    209    198            �
           2606    28543     book fksp3c1dqi5w5ba5gyhkkpwrpap    FK CONSTRAINT     �   ALTER TABLE ONLY public.book
    ADD CONSTRAINT fksp3c1dqi5w5ba5gyhkkpwrpap FOREIGN KEY (user_id) REFERENCES public.tbl_user(id);
 J   ALTER TABLE ONLY public.book DROP CONSTRAINT fksp3c1dqi5w5ba5gyhkkpwrpap;
       public       postgres    false    217    2798    198            �      x�3�tLI�U��L������� .�s      �   w   x�̻�0 �ڞ"D`0�]��&M��߻A���;��ι�I��$���d���"�n�1D������W@T�2���A	�2������R�W��P�a�0G��������s���'�      �      x�3�4����� f      �      x�3�4����� f      �   -   x�3�420��50�50V02�2��20�3�0����4����� ��      �      x������ � �      �      x������ � �      �   !   x�3�t��-(-I-RN�L�KMN����� k
{      �      x������ � �      �   4   x���  �w���=A����0I0�x��u�79����!������}��      �      x�3�.(��KO-R�K,)-J����� Y��      �   L   x�m���0��Q��� 1IV�����w��G�j�ǌ��f"�oꓻ�%h�u¥��vw�oϊ���TD.�+�      �      x������ � �      �   �   x�=���   �g���]��M7��:�^ZFP����������U/�� �'��2*3Y�2M`�C���:���ݢVTE[���l[�����Z��S�E\U�Zo�ׁ�.gVJ��o�T�7�D%vg�t�!���+�      �      x������ � �      �   #   x�3�440�4�4�2�9����b&p�=... ���      �      x������ � �     