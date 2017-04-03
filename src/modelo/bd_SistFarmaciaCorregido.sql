--Autor Gerardo Tejada
--Revision Pastor Lopez
--Inicio Tablas
--En general se cambio los id en las tablas principales porque escencialmente estando en la tabla no es necesario indicarlo
--En general se cambio los nombre de las llaves foraneas a indicar id<nombreTabla>
--Se quito menu, escencialmente porque pueden haber varios menus en el modulo e igual submenu
DROP TABLE IF EXISTS "public"."admin_modulos";
CREATE TABLE "public"."admin_modulos" (
"id" serial NOT NULL,
"nombre" varchar(45) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;

--Se creo la tabla de menu con una relacion recursiva para indicar que menu le pertenece los submenus
DROP TABLE IF EXISTS "public"."admin_menus";
CREATE TABLE "public"."admin_menus"(
"id" serial NOT NULL,
"nombre" varchar(45) COLLATE "default" NOT NULL,
"idmodulo" int,
"idmenu" int
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."admin_niveles";
CREATE TABLE "public"."admin_niveles" (
"id" serial NOT NULL,
"nombre" varchar(50) COLLATE "default",
"estado" boolean DEFAULT true
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."admin_permisos";
CREATE TABLE "public"."admin_permisos" (
"idnivel" int NOT NULL,
"idmenu" int NOT NULL,
"permiso" boolean
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."admin_usuarios";
CREATE TABLE "public"."admin_usuarios" (
"nickname" varchar(20) COLLATE "default" NOT NULL,
"clave" varchar(32) COLLATE "default",
"estado" boolean DEFAULT true,
"idempleado" int,
"idnivel" int
)
WITH (OIDS=FALSE)

;

--La tabla iba a generar redundancia de datos asi que se subdividieron en inventario, bodega, unidades y precio
DROP TABLE IF EXISTS "public"."articulos";
CREATE TABLE "public"."articulos" (
"id" serial NOT NULL,
"nombre" varchar(20) COLLATE "default" NOT NULL,
"descripcion" text,
"estado" boolean DEFAULT true
)
WITH (OIDS=FALSE)

;
--Esto es para las multiples bodegas que tenga la farmcia por ejemplo Casa Matriz, Bodega Sucursal 1, etc...
DROP TABLE IF EXISTS "public"."bodegas";
CREATE TABLE "public"."bodegas"(
"id" serial NOT NULL,
"nombre" varchar(20) COLLATE "default" NOT NULL,
"ubicacion" text NOT NULL,
"estado" boolean
)
WITH (OIDS=FALSE)

;
--Para gestionar las unidades de los productos
DROP TABLE IF EXISTS "public"."unidades";
CREATE TABLE "public"."unidades"(
"id" serial NOT NULL,
"nombre" varchar(20) COLLATE "default" NOT NULL,
"abreviado" varchar(20) COLLATE "default" NOT NULL
)
WITH (OIDS=FALSE)

;
--Para gestionar conversiones de unidades por ejemplo 1 caja = 12 pastillas, 1 caja = 8 pastillas, etc...
DROP TABLE IF EXISTS "public"."conversiones";
CREATE TABLE "public"."conversiones"(
"idunidadp" int NOT NULL,
"idunidads" int,
"equivalencia" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;
--La clasica de inventario para gestionar el stock de los productos las unidades, etc...
DROP TABLE IF EXISTS "public"."inventario";
CREATE TABLE "public"."inventario"(
"id" serial NOT NULL,
"idarticulo" int NOT NULL,
"idcategoria" int NOT NULL,
"idunidad" int NOT NULL,
"estado" boolean DEFAULT true,
"stock" numeric(20,2) NOT NULL,
"stockmin" int NOT NULL,
"stockmax" int NOT NULL,
"fechavencimiento" date NOT NULL,
"idbodega" integer
)
WITH (OIDS=FALSE)

;

--Para gestionar los multiples precios de un producto
DROP TABLE IF EXISTS "public"."precios";
CREATE TABLE "public"."precios"(
"id" serial NOT NULL,
"cantidad" numeric(20,2) DEFAULT 0.00 NOT NULL,
"idinventario" int NOT NULL,
"estado" boolean DEFAULT true
)
WITH (OIDS=FALSE)

;
--Para gestionar los precios con impuestos
DROP TABLE IF EXISTS "public"."precioimpuesto";
CREATE TABLE "public"."precioimpuesto" (
"idprecio" int NOT NULL,
"idimpuesto" int NOT NULL,
"importe" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."cajas";
CREATE TABLE "public"."cajas" (
"id" serial NOT NULL,
"nombre" varchar(45) COLLATE "default" NOT NULL,
"total" numeric(20,2) NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."categorias";
CREATE TABLE "public"."categorias" (
"id" serial NOT NULL,
"nombre" varchar(45) COLLATE "default" NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;

--Para gestionar a los clientes y empleados ya que tiene los mismos datos
DROP TABLE IF EXISTS "public"."personas";
CREATE TABLE "public"."personas" (
"id" serial NOT NULL,
"nombre" varchar(120) COLLATE "default" NOT NULL,
"apellidopaterno" varchar(60) COLLATE "default" NOT NULL,
"apellidomaterno" character varying(60) COLLATE "default",
"dui" char(10) COLLATE "default",
"nit" char(17) COLLATE "default",
"sexo" char(1) NOT NULL,
"fechanacimiento" date,
"direccion" text NOT NULL,
"email" varchar(255) COLLATE "default" NOT NULL,
"idmunicipio" int NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;

--Para gestionar numeros y celulares ya que posee los mismos formatos
DROP TABLE IF EXISTS "public"."telefonos";
CREATE TABLE "public"."telefonos" (
"id" serial NOT NULL,
"numero" varchar(10) COLLATE "default" NOT NULL,
"tipo" varchar(10) COLLATE "default" NOT NULL,
"idpersona" int,
"estado" boolean NOT NULL DEFAULT true;
)
WITH (OIDS=FALSE)

;

--Departamentos para guardar los municipios (porque asi aparece en el form de cliente)
DROP TABLE IF EXISTS "public"."departamentos";
CREATE TABLE "public"."departamentos" (
"id" serial NOT NULL,
"nombre" varchar(80) COLLATE "default" NOT NULL,
"estado" boolean NOT NULL DEFAULT true
)
WITH (OIDS=FALSE)

;
--Los municipios o ciudad de la persona
DROP TABLE IF EXISTS "public"."municipios";
CREATE TABLE "public"."municipios" (
"id" serial NOT NULL,
"nombre" varchar(80) COLLATE "default" NOT NULL,
"iddepartamento" int NOT NULL,
"estado" boolean NOT NULL DEFAULT true
)
WITH (OIDS=FALSE)

;

--Para diferencia a un cliente de la tabla persona (herencia)
DROP TABLE IF EXISTS "public"."clientes";
CREATE TABLE "public"."clientes"(
"id" serial NOT NULL,
"idpersona" int NOT NULL
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."compatibles";
CREATE TABLE "public"."compatibles" (
"idinventario" int NOT NULL,
"idcompatible" int NOT NULL,
"referencia" int NOT NULL
)
WITH (OIDS=FALSE)

;

--Se movio para aca el artibuto de descripcion de la compra, tambien se quito el id resumen ya que si esta enlazado a caja se puede deducir 
--de dicha caja
DROP TABLE IF EXISTS "public"."compras";
CREATE TABLE "public"."compras" (
"id" bigserial NOT NULL,
"nfactura" varchar(15) COLLATE "default" NOT NULL,
"fecha" timestamp(6) NOT NULL,
"descripcion" text,
"subtotal" numeric(20,2) NOT NULL,
"total" numeric(20,2) NOT NULL,
"idcaja" int NOT NULL,
"idproveedor" int NOT NULL,
"idempleado" int NOT NULL,
"estado" boolean DEFAULT true
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."cortecajas";
CREATE TABLE "public"."cortecajas" (
"id" serial NOT NULL,
"fecha" timestamp(6) NOT NULL,
"contado" numeric(20,2) NOT NULL,
"calculado" numeric(20,2) NOT NULL,
"diferencia" numeric(20,2) NOT NULL,
"retiro" numeric(20,2) NOT NULL,
"idcaja" int NOT NULL
)
WITH (OIDS=FALSE)

;

--Se quitaron los precios e importes con impuestos y sin impuestos, se cambio id_articulo por idinventario
DROP TABLE IF EXISTS "public"."detallecompras";
CREATE TABLE "public"."detallecompras" (
"idcompra" bigint NOT NULL,
"idinventario" int NOT NULL,
"cantidad" numeric(20,4) NOT NULL,
"idunidad" int,
"preciounitario" numeric(20,6) NOT NULL,
"importe" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;

--Similar al detalle compra
DROP TABLE IF EXISTS "public"."detalleventas";
CREATE TABLE "public"."detalleventas" (
"idventa" BIGINT NOT NULL,
"idinventario" int NOT NULL,
"cantidad" numeric(20,4) DEFAULT 0.000 NOT NULL,
"idunidad" int,
"preciounitario" numeric(20,6) DEFAULT 0.000 NOT NULL,
"importe" numeric(20,2) NOT NULL,
"descporcentaje" numeric(20,2) NOT NULL,
"desctotal" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;

--Se cambia el nombre impuesto a nombre y se agrego el porcentaje
DROP TABLE IF EXISTS "public"."impuestos";
CREATE TABLE "public"."impuestos" (
"id" serial NOT NULL,
"nombre" varchar(20) COLLATE "default" NOT NULL,
"porcentaje" numeric(20,6) NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;
--Similar a detalle compra y venta
DROP TABLE IF EXISTS "public"."detallevimpuesto";
CREATE TABLE "public"."detallevimpuesto" (
"idventa" int NOT NULL,
"idinventario" int NOT NULL,
"idimpuesto" int NOT NULL,
"importe" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;

--Se cambio el campo a url por text para las imagenes del producto
DROP TABLE IF EXISTS "public"."imagenes";
CREATE TABLE "public"."imagenes" (
"id" serial NOT NULL,
"url" text NOT NULL,
"idinventario" int NOT NULL
)
WITH (OIDS=FALSE)

;

--Se quitaron los campos de comp_id y vent_id y se cambia por el dato de tipo de movimiento pero aun creo que hay que revalorizar los datos del movimiento
DROP TABLE IF EXISTS "public"."movimientos";
CREATE TABLE "public"."movimientos" (
"id" serial NOT NULL,
"total" numeric(20,2) NOT NULL,
"comentario" varchar(255) COLLATE "default" NOT NULL,
"tipoMov" int NOT NULL,
"idcaja" int NOT NULL,
"idinventario" int NOT NULL,
"idcorte" int,
"estado" boolean default true
)
WITH (OIDS=FALSE)

;

DROP TABLE IF EXISTS "public"."proveedores";
CREATE TABLE "public"."proveedores" (
"id" serial NOT NULL,
"nombre" varchar(120) COLLATE "default" NOT NULL,
"representante" varchar(120) COLLATE "default" NOT NULL,
"direccion" varchar(120) COLLATE "default" NOT NULL,
"ciudad" varchar(120) COLLATE "default" NOT NULL,
"rfc" varchar(45) COLLATE "default" NOT NULL,
"telefono" varchar(45) COLLATE "default" NOT NULL,
"celular" varchar(45) COLLATE "default" NOT NULL,
"email" varchar(255) COLLATE "default" NOT NULL,
"sitioweb" varchar(300) COLLATE "default" NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;

--Estoy pensado que esto se puede hacer con una vista o una funcion que una los movimientos de compra venta inventario
DROP TABLE IF EXISTS "public"."resumencortecaja";
CREATE TABLE "public"."resumencortecaja" (
"id" serial NOT NULL,
"ventcontado" numeric(20,2) NOT NULL,
"comcontado" numeric(20,2) NOT NULL,
"entramovi" numeric(20,2) NOT NULL,
"salicompra" numeric(20,2) NOT NULL,
"salimovi" numeric(20,2) NOT NULL,
"idcorte" int
)
WITH (OIDS=FALSE)

;

--Me imagine que vendedor es la tabla de empleado le cambie el nombre, el se va a ver en el nivel de acceso y la comision se puede calcular en una funcion o vista
DROP TABLE IF EXISTS "public"."empleados";
CREATE TABLE "public"."empleados" (
"id" serial NOT NULL,
"idpersona" int NOT NULL,
"estado" boolean NOT NULL
)
WITH (OIDS=FALSE)

;

--Se quito letra porque bueno no creo que tengan por pagos los medicamentos, se cambio el nombre de comentario a observacion, se quito el descuento porque se le coloca al
--producto o medicamento el descuento y no a la venta total
--Al igual que en compra el resumen de corte de caja se puede determinar por medio de la caja
DROP TABLE IF EXISTS "public"."ventas";
CREATE TABLE "public"."ventas" (
"id" bigserial NOT NULL,
"fecha" timestamp(6) NOT NULL,
"subtotal" numeric(20,2) NOT NULL,"total" numeric(20,2) NOT NULL,
"cambio" numeric(20,2) NOT NULL,
"observacion" text NOT NULL,
"idcaja" int NOT NULL,
"idempleado" int,
"idcliente" int,
"estado" boolean default true
)
WITH (OIDS=FALSE)

;

--Me imagine que el total se refleja en la venta aunque esta se parece mucho a la de detallevimp
DROP TABLE IF EXISTS "public"."ventaimpuesto";
CREATE TABLE "public"."ventaimpuesto" (
"idventa" serial NOT NULL,
"idimpuesto" int NOT NULL,
"importe" numeric(20,2) NOT NULL
)
WITH (OIDS=FALSE)

;

--Fin Tablas

--Inicio llaves
----Primarias
ALTER TABLE "public"."admin_modulos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."admin_menus" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."admin_niveles" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."admin_permisos" ADD PRIMARY KEY ("idnivel", "idmenu");
ALTER TABLE "public"."admin_usuarios" ADD PRIMARY KEY ("nickname");
ALTER TABLE "public"."articulos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."bodegas" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."unidades" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."conversiones" ADD PRIMARY KEY ("idunidadp", "idunidads");
ALTER TABLE "public"."inventario" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."precios" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."precioimpuesto" ADD UNIQUE ("idprecio", "idimpuesto");
ALTER TABLE "public"."cajas" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."categorias" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."personas" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."telefonos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."departamentos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."municipios" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."clientes" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."compatibles" ADD PRIMARY KEY ("idinventario", "idcompatible");
ALTER TABLE "public"."compras" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."cortecajas" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."detallecompras" ADD PRIMARY KEY ("idcompra", "idinventario");
ALTER TABLE "public"."detalleventas" ADD PRIMARY KEY ("idventa", "idinventario");
ALTER TABLE "public"."impuestos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."detallevimpuesto" ADD PRIMARY KEY ("idventa", "idimpuesto");
ALTER TABLE "public"."imagenes" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."movimientos" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."proveedores" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."resumencortecaja" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."empleados" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."ventas" ADD PRIMARY KEY ("id");
ALTER TABLE "public"."ventaimpuesto" ADD PRIMARY KEY ("idventa", "idimpuesto");

---Unicas
ALTER TABLE "public"."admin_usuarios" ADD UNIQUE ("idempleado");
ALTER TABLE "public"."clientes" ADD UNIQUE ("idpersona");
ALTER TABLE "public"."empleados" ADD UNIQUE ("idpersona");

--Foraneas
ALTER TABLE "public"."admin_menus" ADD FOREIGN KEY ("idmodulo") REFERENCES "public"."admin_modulos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."admin_menus" ADD FOREIGN KEY ("idmenu") REFERENCES "public"."admin_menus" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."admin_permisos" ADD FOREIGN KEY ("idnivel") REFERENCES "public"."admin_niveles" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."admin_permisos" ADD FOREIGN KEY ("idmenu") REFERENCES "public"."admin_menus" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."admin_usuarios" ADD FOREIGN KEY ("idnivel") REFERENCES "public"."admin_niveles" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."admin_usuarios" ADD FOREIGN KEY ("idempleado") REFERENCES "public"."empleados" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."conversiones" ADD FOREIGN KEY ("idunidadp") REFERENCES "public"."unidades" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."conversiones" ADD FOREIGN KEY ("idunidads") REFERENCES "public"."unidades" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inventario" ADD FOREIGN KEY ("idarticulo") REFERENCES "public"."articulos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inventario" ADD FOREIGN KEY ("idcategoria") REFERENCES "public"."categorias" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inventario" ADD FOREIGN KEY ("idunidad") REFERENCES "public"."unidades" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."precios" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."precioimpuesto" ADD FOREIGN KEY ("idprecio") REFERENCES "public"."precios" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."precioimpuesto" ADD FOREIGN KEY ("idimpuesto") REFERENCES "public"."impuestos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."personas" ADD FOREIGN KEY ("idmunicipio") REFERENCES "public"."municipios" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."telefonos" ADD FOREIGN KEY ("idpersona") REFERENCES "public"."personas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."municipios" ADD FOREIGN KEY ("iddepartamento") REFERENCES "public"."departamentos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."clientes" ADD FOREIGN KEY ("idpersona") REFERENCES "public"."personas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."compatibles" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."compatibles" ADD FOREIGN KEY ("idcompatible") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."compras" ADD FOREIGN KEY ("idcaja") REFERENCES "public"."cajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."compras" ADD FOREIGN KEY ("idproveedor") REFERENCES "public"."proveedores" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."compras" ADD FOREIGN KEY ("idempleado") REFERENCES "public"."empleados" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."cortecajas" ADD FOREIGN KEY ("idcaja") REFERENCES "public"."cajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallecompras" ADD FOREIGN KEY ("idcompra") REFERENCES "public"."compras" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallecompras" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallecompras" ADD FOREIGN KEY ("idunidad") REFERENCES "public"."unidades" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detalleventas" ADD FOREIGN KEY ("idventa") REFERENCES "public"."ventas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detalleventas" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detalleventas" ADD FOREIGN KEY ("idunidad") REFERENCES "public"."unidades" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallevimpuesto" ADD FOREIGN KEY ("idventa") REFERENCES "public"."ventas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallevimpuesto" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."detallevimpuesto" ADD FOREIGN KEY ("idimpuesto") REFERENCES "public"."impuestos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."imagenes" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."movimientos" ADD FOREIGN KEY ("idinventario") REFERENCES "public"."inventario" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."movimientos" ADD FOREIGN KEY ("idcaja") REFERENCES "public"."cajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."movimientos" ADD FOREIGN KEY ("idcorte") REFERENCES "public"."cortecajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."resumencortecaja" ADD FOREIGN KEY ("idcorte") REFERENCES "public"."cortecajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."empleados" ADD FOREIGN KEY ("idpersona") REFERENCES "public"."personas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ventas" ADD FOREIGN KEY ("idcaja") REFERENCES "public"."cajas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ventas" ADD FOREIGN KEY ("idcliente") REFERENCES "public"."clientes" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ventas" ADD FOREIGN KEY ("idempleado") REFERENCES "public"."empleados" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ventaimpuesto" ADD FOREIGN KEY ("idventa") REFERENCES "public"."ventas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."ventaimpuesto" ADD FOREIGN KEY ("idimpuesto") REFERENCES "public"."impuestos" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;
ALTER TABLE "public"."inventario" ADD FOREIGN KEY ("idbodega") REFERENCES "public"."bodegas" ("id") ON DELETE NO ACTION ON UPDATE NO ACTION;

--Registros
INSERT INTO 
	departamentos(nombre) 
VALUES
	('Chalatenango');
	
INSERT INTO 
	municipios(nombre, iddepartamento) 
VALUES
	('Chalatenango',1),
	('Agua Caliente',1),
	('Concepcion Quezaltepeque',1),
	('Nueva Concepcion', 1);

INSERT INTO
	admin_modulos(nombre)
VALUES
	('Operaciones'),
	('Consultas'),
	('Procesos'),
	('Reportes'),
	('Sistema');

INSERT INTO
	admin_menus(nombre, idmodulo, idmenu)
VALUES
	('Articulos', 1, null),
	('Clientes', 1, null) ,
	('Proveedores', 1, null),
	('Compras', 1, null),
	('Ventas', 1, null),
	('Inventario Inicial', 1, null),
	('Corte Inicial', 1, null),
	('Compras', 2, null),
	('Ventas', 2, null),
	('Proveedores', 2, null),
	('Clientes', 2, null),
	('Articulos', 2, null),
	('Respaldos', 3, null),
	('Importar de Excel', 3, null),
	('Ventas', 4, null),
	('Compras', 4, null),
	('Proveedores', 4, null),
	('Articulos', 4, null),
	('Clientes', 4, null),
	('Farmacias', 4, null),
	('Administrar', 5, null),
	('Empleados', 5, 21),
	('Usuarios', 5, 21),
	('Roles', 5, 21),
	('Operatividad', 5, 21),
	('Empresa', 5, null),
	('Unidades', 5, null);

with sub AS (select * from admin_menus)
select * from admin_menus inner join sub on admin_menus.id = sub.idmenu;

INSERT INTO
	admin_permisos(idnivel, idmenu, permiso)
VALUES
	(1, 1, true),
	(1, 2, true),
	(1, 3, true),
	(1, 4, true),
	(1, 5, true),
	(1, 6, true),
	(1, 7, true),
	(1, 8, true),
	(1, 9, true),
	(1, 10, true),
	(1, 11, true),
	(1, 12, true),
	(1, 13, true),
	(1, 14, true),
	(1, 15, true),
	(1, 16, true),
	(1, 17, true),
	(1, 18, true),
	(1, 19, true),
	(1, 20, true),
	(1, 21, true),
	(1, 22, true),
	(1, 23, true),
	(1, 24, true),
	(1, 25, true),
	(1, 26, true),
	(1, 27, true);
