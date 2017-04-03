--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

-- Started on 2017-04-02 11:42:02

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

SET search_path = public, pg_catalog;

--
-- TOC entry 2480 (class 0 OID 30598)
-- Dependencies: 186
-- Data for Name: admin_modulos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_modulos VALUES (1, 'Operaciones');
INSERT INTO admin_modulos VALUES (2, 'Consultas');
INSERT INTO admin_modulos VALUES (3, 'Procesos');
INSERT INTO admin_modulos VALUES (4, 'Reportes');
INSERT INTO admin_modulos VALUES (5, 'Sistema');


--
-- TOC entry 2482 (class 0 OID 30604)
-- Dependencies: 188
-- Data for Name: admin_menus; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_menus VALUES (1, 'Articulos', 1, NULL);
INSERT INTO admin_menus VALUES (2, 'Clientes', 1, NULL);
INSERT INTO admin_menus VALUES (3, 'Proveedores', 1, NULL);
INSERT INTO admin_menus VALUES (4, 'Compras', 1, NULL);
INSERT INTO admin_menus VALUES (5, 'Ventas', 1, NULL);
INSERT INTO admin_menus VALUES (6, 'Inventario Inicial', 1, NULL);
INSERT INTO admin_menus VALUES (8, 'Compras', 2, NULL);
INSERT INTO admin_menus VALUES (9, 'Ventas', 2, NULL);
INSERT INTO admin_menus VALUES (10, 'Proveedores', 2, NULL);
INSERT INTO admin_menus VALUES (11, 'Clientes', 2, NULL);
INSERT INTO admin_menus VALUES (12, 'Articulos', 2, NULL);
INSERT INTO admin_menus VALUES (13, 'Respaldos', 3, NULL);
INSERT INTO admin_menus VALUES (14, 'Importar de Excel', 3, NULL);
INSERT INTO admin_menus VALUES (15, 'Ventas', 4, NULL);
INSERT INTO admin_menus VALUES (16, 'Compras', 4, NULL);
INSERT INTO admin_menus VALUES (17, 'Proveedores', 4, NULL);
INSERT INTO admin_menus VALUES (18, 'Articulos', 4, NULL);
INSERT INTO admin_menus VALUES (19, 'Clientes', 4, NULL);
INSERT INTO admin_menus VALUES (20, 'Farmacias', 4, NULL);
INSERT INTO admin_menus VALUES (21, 'Administrar', 5, NULL);
INSERT INTO admin_menus VALUES (22, 'Empleados', 5, 21);
INSERT INTO admin_menus VALUES (23, 'Usuarios', 5, 21);
INSERT INTO admin_menus VALUES (24, 'Roles', 5, 21);
INSERT INTO admin_menus VALUES (25, 'Operatividad', 5, 21);
INSERT INTO admin_menus VALUES (26, 'Empresa', 5, NULL);
INSERT INTO admin_menus VALUES (27, 'Unidades', 5, NULL);
INSERT INTO admin_menus VALUES (28, 'Impresora', 5, NULL);
INSERT INTO admin_menus VALUES (7, 'Corte de Caja', 1, NULL);


--
-- TOC entry 2541 (class 0 OID 0)
-- Dependencies: 187
-- Name: admin_menus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_menus_id_seq', 28, true);


--
-- TOC entry 2542 (class 0 OID 0)
-- Dependencies: 185
-- Name: admin_modulos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_modulos_id_seq', 5, true);


--
-- TOC entry 2484 (class 0 OID 30610)
-- Dependencies: 190
-- Data for Name: admin_niveles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_niveles VALUES (1, 'Administrador', true);


--
-- TOC entry 2543 (class 0 OID 0)
-- Dependencies: 189
-- Name: admin_niveles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_niveles_id_seq', 1, true);


--
-- TOC entry 2485 (class 0 OID 30615)
-- Dependencies: 191
-- Data for Name: admin_permisos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_permisos VALUES (1, 1, true);
INSERT INTO admin_permisos VALUES (1, 2, true);
INSERT INTO admin_permisos VALUES (1, 3, true);
INSERT INTO admin_permisos VALUES (1, 4, true);
INSERT INTO admin_permisos VALUES (1, 5, true);
INSERT INTO admin_permisos VALUES (1, 6, true);
INSERT INTO admin_permisos VALUES (1, 7, true);
INSERT INTO admin_permisos VALUES (1, 8, true);
INSERT INTO admin_permisos VALUES (1, 9, true);
INSERT INTO admin_permisos VALUES (1, 10, true);
INSERT INTO admin_permisos VALUES (1, 11, true);
INSERT INTO admin_permisos VALUES (1, 12, true);
INSERT INTO admin_permisos VALUES (1, 13, true);
INSERT INTO admin_permisos VALUES (1, 14, true);
INSERT INTO admin_permisos VALUES (1, 15, true);
INSERT INTO admin_permisos VALUES (1, 16, true);
INSERT INTO admin_permisos VALUES (1, 17, true);
INSERT INTO admin_permisos VALUES (1, 18, true);
INSERT INTO admin_permisos VALUES (1, 19, true);
INSERT INTO admin_permisos VALUES (1, 20, true);
INSERT INTO admin_permisos VALUES (1, 21, true);
INSERT INTO admin_permisos VALUES (1, 22, true);
INSERT INTO admin_permisos VALUES (1, 23, true);
INSERT INTO admin_permisos VALUES (1, 24, true);
INSERT INTO admin_permisos VALUES (1, 25, true);
INSERT INTO admin_permisos VALUES (1, 26, true);
INSERT INTO admin_permisos VALUES (1, 27, true);
INSERT INTO admin_permisos VALUES (1, 28, true);


--
-- TOC entry 2508 (class 0 OID 30694)
-- Dependencies: 214
-- Data for Name: departamentos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO departamentos VALUES (1, 'Chalatenango', true);
INSERT INTO departamentos VALUES (2, 'Santa Ana', true);


--
-- TOC entry 2510 (class 0 OID 30700)
-- Dependencies: 216
-- Data for Name: municipios; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO municipios VALUES (1, 'Chalatenango', 1, true);
INSERT INTO municipios VALUES (2, 'Agua Caliente', 1, true);
INSERT INTO municipios VALUES (3, 'Concepcion Quezaltepeque', 1, true);
INSERT INTO municipios VALUES (4, 'Nueva Concepcion', 1, true);
INSERT INTO municipios VALUES (5, 'Candelaria de la Frontera', 2, true);
INSERT INTO municipios VALUES (6, 'Chalchuapa', 2, true);


--
-- TOC entry 2504 (class 0 OID 30679)
-- Dependencies: 210
-- Data for Name: personas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO personas VALUES (1, 'Manuel de Jesus', '12345678-9', '1234-123456-123-1', '1996-04-05', 'Caserio Plan de las Mesas', 'manuel@gmail.com', 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (4, 'Glenda Esmeralda', '21321321-5', '3213-212313-232-1', '1989-10-01', 'Barrio San Antonio', 'glenda_lopez15@gmail.com', 5, true, 'F', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (2, 'Geovany David', '12345678-1', '1234-123456-123-2', '1980-04-10', 'Caserio Plan de las Mesas, Canton Upatoro', 'geovany_david012@gmail.com', 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (8, 'Fredy Pastor', '74125896-3', '1235-789445-369-4', '1992-09-11', 'Canton Upatoro, Caserio Plan de las Mesas', NULL, 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (6, 'Fatima Ernestina', '68764564-6', '5456-132165-498-4', '1996-07-20', 'Barrio El Calvario', NULL, 1, false, 'F', 'Lopez', 'Cartagena');


--
-- TOC entry 2532 (class 0 OID 30779)
-- Dependencies: 238
-- Data for Name: empleados; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO empleados VALUES (1, 8, true);


--
-- TOC entry 2486 (class 0 OID 30618)
-- Dependencies: 192
-- Data for Name: admin_usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_usuarios VALUES ('darkps', 'efe237b5fe14b08a47f698cbb80d5aab', true, 1, 1);


--
-- TOC entry 2488 (class 0 OID 30624)
-- Dependencies: 194
-- Data for Name: articulos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2544 (class 0 OID 0)
-- Dependencies: 193
-- Name: articulos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('articulos_id_seq', 1, false);


--
-- TOC entry 2490 (class 0 OID 30631)
-- Dependencies: 196
-- Data for Name: bodegas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO bodegas VALUES (1, 'Farmacia 1', 'Barrio San Antonio', true);
INSERT INTO bodegas VALUES (2, 'Farmacia 2', 'Barrio El Calvario', true);


--
-- TOC entry 2545 (class 0 OID 0)
-- Dependencies: 195
-- Name: bodegas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('bodegas_id_seq', 2, true);


--
-- TOC entry 2500 (class 0 OID 30667)
-- Dependencies: 206
-- Data for Name: cajas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2546 (class 0 OID 0)
-- Dependencies: 205
-- Name: cajas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cajas_id_seq', 1, false);


--
-- TOC entry 2502 (class 0 OID 30673)
-- Dependencies: 208
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2547 (class 0 OID 0)
-- Dependencies: 207
-- Name: categorias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('categorias_id_seq', 1, false);


--
-- TOC entry 2512 (class 0 OID 30706)
-- Dependencies: 218
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO clientes VALUES (1, 1);
INSERT INTO clientes VALUES (2, 2);
INSERT INTO clientes VALUES (3, 4);
INSERT INTO clientes VALUES (4, 6);


--
-- TOC entry 2548 (class 0 OID 0)
-- Dependencies: 217
-- Name: clientes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('clientes_id_seq', 4, true);


--
-- TOC entry 2492 (class 0 OID 30640)
-- Dependencies: 198
-- Data for Name: unidades; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2495 (class 0 OID 30649)
-- Dependencies: 201
-- Data for Name: inventario; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2513 (class 0 OID 30710)
-- Dependencies: 219
-- Data for Name: compatibles; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2528 (class 0 OID 30764)
-- Dependencies: 234
-- Data for Name: proveedores; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2515 (class 0 OID 30715)
-- Dependencies: 221
-- Data for Name: compras; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2549 (class 0 OID 0)
-- Dependencies: 220
-- Name: compras_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('compras_id_seq', 1, false);


--
-- TOC entry 2493 (class 0 OID 30644)
-- Dependencies: 199
-- Data for Name: conversiones; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2517 (class 0 OID 30725)
-- Dependencies: 223
-- Data for Name: cortecajas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2550 (class 0 OID 0)
-- Dependencies: 222
-- Name: cortecajas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cortecajas_id_seq', 1, false);


--
-- TOC entry 2551 (class 0 OID 0)
-- Dependencies: 213
-- Name: departamentos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('departamentos_id_seq', 2, true);


--
-- TOC entry 2518 (class 0 OID 30729)
-- Dependencies: 224
-- Data for Name: detallecompras; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2534 (class 0 OID 30785)
-- Dependencies: 240
-- Data for Name: ventas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2519 (class 0 OID 30732)
-- Dependencies: 225
-- Data for Name: detalleventas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2521 (class 0 OID 30739)
-- Dependencies: 227
-- Data for Name: impuestos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2522 (class 0 OID 30743)
-- Dependencies: 228
-- Data for Name: detallevimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2552 (class 0 OID 0)
-- Dependencies: 237
-- Name: empleados_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('empleados_id_seq', 1, false);


--
-- TOC entry 2524 (class 0 OID 30748)
-- Dependencies: 230
-- Data for Name: imagenes; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2553 (class 0 OID 0)
-- Dependencies: 229
-- Name: imagenes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('imagenes_id_seq', 1, false);


--
-- TOC entry 2554 (class 0 OID 0)
-- Dependencies: 226
-- Name: impuestos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('impuestos_id_seq', 1, false);


--
-- TOC entry 2555 (class 0 OID 0)
-- Dependencies: 200
-- Name: inventario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('inventario_id_seq', 1, false);


--
-- TOC entry 2526 (class 0 OID 30757)
-- Dependencies: 232
-- Data for Name: movimientos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2556 (class 0 OID 0)
-- Dependencies: 231
-- Name: movimientos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('movimientos_id_seq', 1, false);


--
-- TOC entry 2557 (class 0 OID 0)
-- Dependencies: 215
-- Name: municipios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('municipios_id_seq', 6, true);


--
-- TOC entry 2558 (class 0 OID 0)
-- Dependencies: 209
-- Name: personas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('personas_id_seq', 8, true);


--
-- TOC entry 2497 (class 0 OID 30656)
-- Dependencies: 203
-- Data for Name: precios; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2498 (class 0 OID 30662)
-- Dependencies: 204
-- Data for Name: precioimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2559 (class 0 OID 0)
-- Dependencies: 202
-- Name: precios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('precios_id_seq', 1, false);


--
-- TOC entry 2560 (class 0 OID 0)
-- Dependencies: 233
-- Name: proveedores_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('proveedores_id_seq', 1, false);


--
-- TOC entry 2530 (class 0 OID 30773)
-- Dependencies: 236
-- Data for Name: resumencortecaja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2561 (class 0 OID 0)
-- Dependencies: 235
-- Name: resumencortecaja_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('resumencortecaja_id_seq', 1, false);


--
-- TOC entry 2506 (class 0 OID 30688)
-- Dependencies: 212
-- Data for Name: telefonos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO telefonos VALUES (1, '1234-5679', 'Casa', 1, true);
INSERT INTO telefonos VALUES (5, '1234-5679', 'Casa', 4, true);
INSERT INTO telefonos VALUES (2, '1234-5679', 'Celular', 1, true);
INSERT INTO telefonos VALUES (6, '1234-5679', 'Celular', 4, true);
INSERT INTO telefonos VALUES (3, '1234-5645', 'Casa', 2, true);
INSERT INTO telefonos VALUES (4, '7852-9645', 'Celular', 2, true);
INSERT INTO telefonos VALUES (7, '5646-5465', 'Casa', 6, false);
INSERT INTO telefonos VALUES (8, '5646-5465', 'Movil', 6, false);


--
-- TOC entry 2562 (class 0 OID 0)
-- Dependencies: 211
-- Name: telefonos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('telefonos_id_seq', 8, true);


--
-- TOC entry 2563 (class 0 OID 0)
-- Dependencies: 197
-- Name: unidades_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unidades_id_seq', 1, false);


--
-- TOC entry 2536 (class 0 OID 30795)
-- Dependencies: 242
-- Data for Name: ventaimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2564 (class 0 OID 0)
-- Dependencies: 241
-- Name: ventaimpuesto_idventa_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ventaimpuesto_idventa_seq', 1, false);


--
-- TOC entry 2565 (class 0 OID 0)
-- Dependencies: 239
-- Name: ventas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ventas_id_seq', 1, false);


-- Completed on 2017-04-02 11:42:03

--
-- PostgreSQL database dump complete
--

