CREATE OR REPLACE FUNCTION obtenertelefonos(cod int) RETURNS TABLE (codigo int, num varchar, tip varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				id, numero, tipo, estado
			FROM
				telefonos
			WHERE
				idpersona = $1;
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenertelefonos(1);

CREATE OR REPLACE FUNCTION obtenermunicipiopersona(cod int) RETURNS TABLE (codigo int, nom varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				municipios
			WHERE
				id = (SELECT idmunicipio FROM personas WHERE id = $1);
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenermunicipiopersona(1);

CREATE OR REPLACE FUNCTION obtenermunicipiodepto(cod int) RETURNS TABLE (codigo int, nom varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				municipios
			WHERE
				iddepartamento = $1;
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenermunicipiodepto(1);

CREATE OR REPLACE FUNCTION obtenerdepartamentos() RETURNS TABLE (codigo int, nom varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				departamentos;
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenerdepartamentos();

CREATE OR REPLACE FUNCTION obtenerclientes() 
RETURNS TABLE (
		codigo int, 
		nombre varchar, 
		apellido1 varchar, 
		apellido2 varchar, 
		dui char, 
		nit char, 
		genero char, 
		nacimiento date, 
		direccion text,
		correo varchar,
		codpersona int,
		est boolean
	)
AS $BODY$
	BEGIN
		RETURN QUERY
			SELECT
				c.id as cod1, p.nombre, p.apellidopaterno, p.apellidomaterno, p.dui, p.nit, p.sexo, p.fechanacimiento, 
				p.direccion, p.email, p.id as cod2, p.estado
			FROM
				clientes as c
				inner join personas as p on c.idpersona = p.id;
		
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenerclientes();

CREATE OR REPLACE FUNCTION obtenercliente(cod int) 
RETURNS TABLE (
		codigo int, 
		nombre varchar, 
		apellido1 varchar, 
		apellido2 varchar, 
		dui char, 
		nit char, 
		genero char, 
		nacimiento date, 
		direccion text,
		correo varchar,
		codpersona int,
		est boolean
	)
AS $BODY$
	BEGIN
		RETURN QUERY
			SELECT
				c.id as cod1, p.nombre, p.apellidopaterno, p.apellidomaterno, p.dui, p.nit, p.sexo, p.fechanacimiento, 
				p.direccion, p.email, p.id as cod2, p.estado
			FROM
				clientes as c
				inner join personas as p on c.idpersona = p.id
			WHERE
				c.id = $1;
		
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenercliente(1);

DROP FUNCTION public.registrarcliente(character varying, character varying, character varying, character, character, character, date, text, character varying, boolean, integer, json);
CREATE OR REPLACE FUNCTION registrarcliente(
	nombre varchar, 
	apellido1 varchar, 
	apellido2 varchar, 
	d char, 
	n char, 
	genero char, 
	nacimiento date, 
	direccion text,
	correo varchar,	
	codmunicipio int,
	telefonos json
) RETURNS void 
AS $BODY$
	DECLARE
		codpersona int;
	BEGIN
		INSERT INTO
			personas(nombre, apellidopaterno, apellidomaterno, dui, nit, sexo, fechanacimiento, 
				 direccion, email, idmunicipio)
		VALUES
			($1, $2, $3, $4, $5, $6, $7, $8, $9, $10)
		RETURNING
			id into codpersona;
		INSERT INTO
			telefonos(numero, tipo, idpersona)
		SELECT
			*, codpersona
		FROM
			json_to_recordset(telefonos) 
			as tbl(num varchar, tip varchar);
		INSERT INTO
			clientes(idpersona)
		VALUES
			(codpersona);
	END
$BODY$ LANGUAGE plpgsql;

SELECT registrarcliente(
	'Geovany', 
	'Lopez', 'Cartagena', 
	'12345678-9', 
	'1234-123456-123-1', 
	'M', 
	'1996-04-10', 
	'Caserio Plan de las Mesas',
	'geovany@gmail.com',
	1,
	'[{"num":"1234-5679", "tip":"Casa"}, {"num":"7852-9635", "tip":"Celular"}]');

DROP FUNCTION public.editarcliente(integer, character varying, character varying, character varying, character, character, character, date, text, character varying, boolean, integer, json);
CREATE OR REPLACE FUNCTION editarcliente(
	codigo int,
	nombre varchar, 
	apellido1 varchar, 
	apellido2 varchar, 
	d char, 
	n char, 
	genero char, 
	nacimiento date, 
	direccion text,
	correo varchar,
	codmunicipio int,
	telefonos json
) RETURNS void 
AS $BODY$
	DECLARE
		codpersona int;
	BEGIN
		UPDATE
			personas
		SET
			nombre = $2, apellidopaterno = $3, apellidomaterno = $4, dui = $5, nit = $6, sexo = $7, fechanacimiento = $8, 
			direccion = $9, email = $10, idmunicipio = $11
		WHERE
			id = (SELECT idpersona FROM clientes WHERE id = $1)
		RETURNING
			id into codpersona;
			
		UPDATE
			telefonos t
		SET
			numero = tbl.num, tipo = tbl.tip
		FROM
			json_to_recordset(telefonos) as tbl(cod int, num varchar, tip varchar)
		WHERE id = tbl.cod and idpersona = codpersona;
	END
$BODY$ LANGUAGE plpgsql;

SELECT editarcliente(
	2,
	'Geovany David', 
	'Lopez', 'Cartagena', 
	'12345678-1', 
	'1234-123456-123-2', 
	'M', 
	'1980-04-10', 
	'Caserio Plan de las Mesas, Canton Upatoro',
	'geovany_david012@gmail.com',
	1,
	'[{"cod":"3", "num":"1234-5645", "tip":"Casa"}, {"cod":"4", "num":"7852-9645", "tip":"Celular"}]');
	
CREATE OR REPLACE FUNCTION eliminarcliente(codigo int) RETURNS void AS $BODY$
	DECLARE
		codpersona int;
	BEGIN
		UPDATE
			personas
		SET
			estado = false
		WHERE
			id = (SELECT idpersona FROM clientes WHERE id = $1)
		RETURNING
			id into codpersona;
			
		UPDATE
			telefonos t
		SET
			estado = false
		WHERE idpersona = codpersona;
	END
$BODY$ LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.login(
    usuario character varying,
    pass character varying)
  RETURNS boolean AS
$BODY$
	DECLARE 
		retorno boolean default false;
		contar int default 0;
	BEGIN
		SELECT 
			count(*) INTO contar 
		FROM
			admin_usuarios 
		WHERE 
			nickname = $1 AND clave = md5($2 || $1 || 'ok') AND estado = TRUE;
		IF contar > 0 THEN
			retorno = TRUE;
		ELSE
			retorno = FALSE;
		END IF;
		RETURN retorno;
	END
$BODY$
  LANGUAGE plpgsql;

DROP FUNCTION public.obtenerusuario(character varying);
CREATE OR REPLACE FUNCTION public.obtenerusuario(IN usuario character varying)
  RETURNS TABLE(nickname character varying, estado boolean, idempleado int, idnivel int) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				u.nickname, u.estado, u.idempleado, u.idnivel
			FROM
				admin_usuarios AS u
			WHERE
				u.nickname = $1;
		END
$BODY$
  LANGUAGE plpgsql;
  
select * from obtenerusuario('darkps');

CREATE OR REPLACE FUNCTION obtenerempleado(cod int) 
RETURNS TABLE (
		codigo int, 
		nombre varchar, 
		apellido1 varchar, 
		apellido2 varchar, 
		dui char, 
		nit char, 
		genero char, 
		nacimiento date, 
		direccion text,
		correo varchar,
		codpersona int,
		est boolean
	)
AS $BODY$
	BEGIN
		RETURN QUERY
			SELECT
				e.id as cod1, p.nombre, p.apellidopaterno, p.apellidomaterno, p.dui, p.nit, p.sexo, p.fechanacimiento, 
				p.direccion, p.email, p.id as cod2, p.estado
			FROM
				empleados as e
				inner join personas as p on e.idpersona = p.id
			WHERE
				e.id = $1;
		
	END
$BODY$ LANGUAGE plpgsql;

SELECT * FROM obtenerempleado(1);

CREATE OR REPLACE FUNCTION public.obtenerusuarios()
  RETURNS TABLE(nickname character varying, estado boolean, idempleado integer, idnivel integer) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				u.nickname, u.estado, u.idempleado, u.idnivel
			FROM
				admin_usuarios AS u;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerusuarios();

DROP FUNCTION obtenerarticulo(integer);
CREATE OR REPLACE FUNCTION public.obtenerarticulo(IN id int)
  RETURNS TABLE(cod int, prod character varying, descrip text, estado boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				a.id, a.nombre, a.descripcion, a.estado
			FROM
				articulos as a
			WHERE
				a.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerarticulo(3);

DROP FUNCTION public.obtenercategoria(int);

CREATE OR REPLACE FUNCTION public.obtenercategoria(IN cod int)
  RETURNS TABLE(codigo int, categoria character varying, estado boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				c.id, c.nombre, c.estado
			FROM
				categorias as c
			WHERE
				c.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenercategoria(1);

CREATE OR REPLACE FUNCTION public.obtenerunidad(IN cod int)
  RETURNS TABLE(codigo int, unidad character varying, abrev character varying) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				u.id, u.nombre, u.abreviado
			FROM
				unidades as u
			WHERE
				u.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerunidad(1);

DROP FUNCTION obtenerbodega(integer);
CREATE OR REPLACE FUNCTION public.obtenerbodega(IN cod int)
  RETURNS TABLE(codigo int, bodega character varying, dir text, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				b.id, b.nombre, b.ubicacion, b.estado
			FROM
				bodegas as b
			WHERE
				b.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerbodega(1);

DROP FUNCTION obtenerpreciosinv(integer);
CREATE OR REPLACE FUNCTION public.obtenerpreciosinv(inv int)
  RETURNS TABLE(codigo int, precio numeric, tipo varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad, p.tipo, p.estado
			FROM
				precios as p
			WHERE
				p.idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerpreciosinv(1);

DROP FUNCTION obtenerinventarios();
CREATE OR REPLACE FUNCTION public.obtenerinventarios()
  RETURNS TABLE(
	codigo int, 
	articulo integer, 
	unidad int, 
	categoria int, 
	bodega int,
	stck numeric, 
	stckmin int, 
	stckmax int, 
	vencimiento date, 
	est boolean,
	intcod varchar) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	i.estado, i.internalcod
			FROM
				inventario as i;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerinventarios();

DROP FUNCTION public.obtenerinventario(int);
CREATE OR REPLACE FUNCTION public.obtenerinventario(cod int)
  RETURNS TABLE(
	codigo int, 
	articulo integer, 
	unidad int, 
	categoria int, 
	bodega int,
	stck numeric, 
	stckmin int, 
	stckmax int, 
	vencimiento date, 
	est boolean,
	intcod varchar) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	i.estado, i.internalcod
			FROM
				inventario as i
			WHERE
				i.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerinventario(2);

CREATE OR REPLACE FUNCTION public.obtenercompatibles(cod int)
  RETURNS TABLE(
	codigo int, 
	articulo integer, 
	unidad int, 
	categoria int, 
	bodega int,
	stck numeric, 
	stckmin int, 
	stckmax int, 
	vencimiento date, 
	est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				c.idcompatible, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	c.estado
			FROM
				inventario as i inner join compatibles as c on c.idcompatible = i.id
			WHERE
				c.idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenercompatibles(7);


DROP FUNCTION public.obtenernivel(integer);
CREATE OR REPLACE FUNCTION public.obtenernivel(IN id int)
  RETURNS TABLE(codigo int, nivel character varying, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				n.id, n.nombre, n.estado
			FROM
				admin_niveles AS n
			WHERE
				n.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenernivel(1)

CREATE OR REPLACE FUNCTION public.cargarpermisos(IN nivel int)
  RETURNS TABLE(codigo int, menu character varying, permiso boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				m.id, m.nombre, p.permiso
			FROM
				admin_menus as m inner join 
				admin_permisos as p on m.id=p.idmenu
			WHERE
				p.idnivel = $1;
	END
$BODY$
  LANGUAGE plpgsql;

DROP FUNCTION obtenerprecioinv(integer);
CREATE OR REPLACE FUNCTION public.obtenerprecioinv(inventario int)
  RETURNS TABLE(codigo int, cant numeric, tipo varchar, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad, p.tipo, p.estado
			FROM
				precios as p
			WHERE 
				p.idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerprecioinv(4);

CREATE OR REPLACE FUNCTION public.obtenerimagen(inventario int)
  RETURNS TABLE(codigo int, url text) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.url
			FROM
				imagenes as i
			WHERE 
				i.idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerimagen(1);


CREATE OR REPLACE FUNCTION public.registrarinventario( 
	articulo varchar,
	decrip text, 
	unidad int, 
	categoria int, 
	bodega int,
	stckmin int, 
	stckmax int, 
	vencimiento date,
	invprecios json,
	imagen text,
	compatibles json) RETURNS void AS
$BODY$
	DECLARE 
		idatr int;
		idinv int;
		codgen1 varchar;
	BEGIN
		INSERT INTO
			articulos(nombre, descripcion)
		VALUES
			($1, $2)
		RETURNING id INTO idatr;

		SELECT 
			concat(substr(internalcod, 1, 1), LPAD((max(substr(internalcod,2, length(internalcod)))::integer + 1)::text, 4, '0')) 
			INTO codgen1
		FROM 
			inventario
		WHERE
			substr(internalcod,1,1) = substr($1, 1, 1)
		GROUP BY 
			substr(internalcod,1,1);
		
		
		INSERT INTO
			inventario(idarticulo, idunidad, idcategoria, idbodega, stockmin, stockmax, fechavencimiento, internalcod)
		VALUES
			(idatr, $3, $4, $5, $6, $7, $8, codgen1)
		RETURNING id INTO idinv;

		INSERT INTO
			precios(cantidad, idinventario)
		SELECT
			*, idinv
		FROM
			json_to_recordset(invprecios) 
			as tbl(cant numeric(20,2));

		IF $10 IS NOT NULL || trim($10) <> '' THEN
			INSERT INTO
				imagenes(url, idinventario)
			VALUES
				($10, idinv);
		END IF;

		IF $11 IS NOT NULL THEN
			INSERT INTO
				compatibles(idinventario, idcompatible)
			SELECT
				idinv, *
			FROM
				json_to_recordset($11)
				AS tb(codcomp int);
		END IF;
	END
$BODY$
  LANGUAGE plpgsql;
  
 SELECT registrarinventario(
	'Metocarbamol 2',
	'Para los dolores de musculo cotidiano',
	1,
	2,
	1,
	50,
	500,
	'2018-07-13',
	'[{"cant":"2.50"}, {"cant":"3.50"}, {"cant":"4.50"}, {"cant":"5.50"}, {"cant":"6.50"}, {"cant":"7.50"}]',
	null,
	null
 );

 CREATE OR REPLACE FUNCTION public.obtenerbodegas()
  RETURNS TABLE(codigo integer, bodega character varying, dir text, est boolean) AS
$BODY$
begin
	return QUERY
		SELECT 
			b.id, b.nombre, b.ubicacion, b.estado
		FROM
			bodegas as b;
end
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenercategorias()
  RETURNS TABLE(codigo integer, categoria character varying, estado boolean) AS
$BODY$
begin
	return QUERY
		SELECT 
			c.id, c.nombre, c.estado
		FROM
			categorias as c;
end
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenerunidades()
  RETURNS TABLE(codigo integer, unidad character varying, abrev character varying) AS
$BODY$
begin
	return QUERY
		SELECT 
			u.id, u.nombre, u.abreviado
		FROM
			unidades as u;
end
$BODY$
  LANGUAGE plpgsql;

select * from obtenerunidades();

DROP FUNCTION editarinventario(varchar,text,int,int,int,int,int,date,json,text,int);

CREATE OR REPLACE FUNCTION public.editarinventario( 
	articulo varchar,
	decrip text, 
	unidad int, 
	categoria int, 
	bodega int,
	stckmin int, 
	stckmax int, 
	vencimiento date,
	invprecios json,
	imagen text,
	codinv int,
	compatibles json) RETURNS void AS
$BODY$
	DECLARE 
		idatr int;
		imgcount int = 0;
		i json;
	BEGIN

		UPDATE
			inventario
		SET
			idunidad = $3, idcategoria = $4, idbodega = $5, stockmin = $6, stockmax = $7, fechavencimiento = $8
		WHERE
			id = $11
		RETURNING idarticulo INTO idatr;

		
		UPDATE
			articulos a
		SET
			nombre = $1, descripcion = $2
		WHERE
			id = idatr;

		UPDATE
			precios p
		SET
			cantidad = tbl.cant
		FROM
			json_to_recordset(invprecios) 
			as tbl(idp int, cant numeric(20,2))
		WHERE
			id = tbl.idp AND idinventario = $11;
			
		SELECT COUNT(*) INTO imgcount FROM imagenes WHERE idinventario = $11;
		
		IF $10 IS NOT NULL AND imgcount > 0 THEN
			UPDATE
				imagenes
			SET
				url = $10
			WHERE
				idinventario = $11;
				
		ELSIF $10 IS NOT NULL AND imgcount = 0 THEN
			INSERT INTO
				imagenes(url, idinventario)
			VALUES
				($10, $11);
		END IF;

		FOR i IN SELECT * FROM json_array_elements($12)
		LOOP
			IF (SELECT COUNT(*) FROM compatibles  where idinventario = $11 AND idcompatible = CAST(i->>'codcomp' as INT)) THEN
				UPDATE
					compatibles c
				SET
					estado = CAST(i->>'est' AS BOOLEAN)
				WHERE
					idinventario = $11 AND idcompatible = CAST(i->>'codcomp' as INT);
			ELSE
				INSERT INTO
					compatibles(idinventario, idcompatible)
				VALUES
					($11, CAST(i->>'codcomp' as INT));
			END IF;
		END LOOP;
	END
$BODY$
  LANGUAGE plpgsql;
  
 SELECT editarinventario(
	'Ibrupofeno MK II',
	'Para dolores II',
	1,3,2,10,1000,
	'2050-04-15 -06',
	'[{"idp":31,"cant":"0.25"},{"idp":32,"cant":"0.50"},{"idp":33,"cant":"0.75"},{"idp":34,"cant":"1.50"},{"idp":35,"cant":"1.25"},{"idp":36,"cant":"1.00"}]',
	'C:\Users\dakrpastiursSennin\Documents\NetBeansProjects\Sist_Farmacia\Recursos\Productos\pregunta.PNG',
	7,
	'[{"codcomp":2,"est":true},{"codcomp":8,"est":true}]');

CREATE OR REPLACE FUNCTION public.eliminarinventario( 
	codinv int) RETURNS void AS
$BODY$
	DECLARE 
		idatr int;
	BEGIN

		UPDATE
			inventario
		SET
			estado = false
		WHERE
			id = $1
		RETURNING idarticulo INTO idatr;

		
		UPDATE
			articulos a
		SET
			estado = false
		WHERE
			id = idatr;

		UPDATE
			precios p
		SET
			estado = false
		WHERE
			idinventario = $1;

		UPDATE
			compatibles c
		SET
			estado = false
		WHERE
			idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.registrarventa(
	cliente int,
	empleado int,
	tot numeric,
	camb numeric,
	comentario text,
	ltr text,
	detalles json, 
	subtot numeric, 
	iva numeric,
	nfact varchar
    )
  RETURNS void AS
$BODY$
	DECLARE
		codventa bigint;
	BEGIN
		INSERT INTO
			ventas(fecha, total, cambio, observacion, letra, idempleado, idcliente, subtotal, iva, nfactura)
		VALUES
			(NOW(), $3, $4, $5, $6, $2, $1, $8, $9, $10)
		RETURNING
			id INTO codventa;
		
		INSERT INTO
			detalleventas(idventa, idinventario, cantidad, idunidad, preciounitario, importe, descporcentaje)			
		SELECT
			codventa, *
		FROM
			json_to_recordset($7) 
			as tbl(idinv int, cant numeric, unidad int, precio numeric, imp numeric, descpor numeric);

		UPDATE
			inventario i
		SET
			stock = stock - tbl.cant
		FROM
			json_to_recordset($7) 
			as tbl(idinv int, cant numeric)
		WHERE
			id = tbl.idinv;

		INSERT INTO
			movimientos(cantidad, comentario, idinventario, total)
		SELECT
			0 - cant, 'Venta', idinv, (cant * precio)
		FROM
			json_to_recordset($7) 
			as tbl(idinv int, cant numeric, precio numeric);
			
		
	END

$BODY$
  LANGUAGE plpgsql;

DROP FUNCTION public.obtenerproveedores();
CREATE OR REPLACE FUNCTION public.obtenerproveedores()
  RETURNS TABLE(cod int, nombre character varying, represent varchar, nrc varchar, direccion text, nit varchar, telefono varchar, celular varchar, email character varying, website character varying, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				prov.id, prov.nombre, prov.representante, prov.rfc, prov.direccion,
				prov.nit, prov.telefono, prov.celular, prov.email, prov.sitioweb, prov.estado
			FROM
				proveedores as prov;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenerdepartamento(cod int)
  RETURNS TABLE(codigo integer, nom character varying, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				departamentos
			WHERE
				id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerdepartamento(1);

DROP FUNCTION public.obtenerproveedor(int);
CREATE OR REPLACE FUNCTION public.obtenerproveedor(codigo int)
  RETURNS TABLE(cod int, nombre character varying, represent varchar, nrc varchar, direccion text, nit varchar, telefono varchar, celular varchar, email character varying, website character varying, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				prov.id, prov.nombre, prov.representante, prov.rfc, prov.direccion,
				prov.nit, prov.telefono, prov.celular, prov.email, prov.sitioweb, prov.estado
			FROM
				proveedores as prov
			WHERE
				id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerproveedor(1);

CREATE OR REPLACE FUNCTION public.registrarproveedor(
	nombre character varying, 
	represent varchar, 
	nrc varchar, 
	direccion text, 
	nit varchar, 
	telefono varchar, 
	celular varchar, 
	email character varying, 
	website character varying)
  RETURNS void AS 
$BODY$
	BEGIN
		INSERT INTO
			proveedores(nombre, representante, rfc, direccion, nit, telefono, celular, email, sitioweb)
		VALUES
			($1, $2, $3, $4, $5, $6, $7, $8, $9);
	END
$BODY$
  LANGUAGE plpgsql;

select registrarproveedor(
	'Bayer 2',
	'Juan Perez ',
	'11',
	'Por ahi',
	'1234-567891-234-5',
	'1234-6789',
	'7235-7896',
	null,
	null
);

CREATE OR REPLACE FUNCTION public.editarproveedor(
	nombre character varying, 
	represent varchar, 
	nrc varchar, 
	direccion text, 
	nit varchar, 
	telefono varchar, 
	celular varchar, 
	email character varying, 
	website character varying,
	cod int)
  RETURNS void AS 
$BODY$
	BEGIN
		UPDATE
			proveedores p
		SET
			nombre = $1, representante = $2, rfc = $3, direccion = $4, nit = $5, 
			telefono = $6, celular = $7, email = $8, sitioweb = $9
		WHERE
			id = $10;
	END
$BODY$
  LANGUAGE plpgsql;

select editarproveedor(
	'Bayer',
	'Juan Perez',
	'11',
	'Por ahi',
	'1234-567891-234-6',
	'1234-6789',
	'7235-7896',
	'bayer@organizacion.com',
	'www.bayer.com',
	2
);

CREATE OR REPLACE FUNCTION public.eliminarproveedor(cod int)
  RETURNS void AS 
$BODY$
	BEGIN
		UPDATE
			proveedores
		SET
			estado = false
		WHERE
			id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select eliminarproveedor(1);

DROP FUNCTION obtenerdetallesventa(bigint);
CREATE OR REPLACE FUNCTION public.obtenerdetallesventa(IN cod bigint)
  RETURNS TABLE(codinv int, cant numeric, unidad int, precio numeric, imp numeric, descpor numeric) AS
$BODY$

	begin
		return QUERY
			select 
				dtv.idinventario, dtv.cantidad, dtv.idunidad, dtv.preciounitario, dtv.importe, dtv.descporcentaje 
			from
				detalleventas as dtv
			where 
				dtv.idventa = $1;
	end

$BODY$
  LANGUAGE plpgsql;

select * from obtenerdetallesventa(3);

CREATE OR REPLACE FUNCTION public.registrarcompra(
	numfact varchar,
	descrip text,
	tot numeric,
	empleado int,
	proveedor int,
	detalles json
    )
  RETURNS void AS
$BODY$
	DECLARE
		codcompra bigint;
	BEGIN
		INSERT INTO
			compras(nfactura, fecha, descripcion, total, idempleado, idproveedor)
		VALUES
			($1, NOW(), $2, $3, $4, $5)
		RETURNING
			id INTO codcompra;
		
		INSERT INTO
			detallecompras(idcompra, idinventario, cantidad, idunidad, preciounitario, importe)			
		SELECT
			codcompra, *
		FROM
			json_to_recordset($6) 
			as tbl(idinv int, cant numeric, unidad int, precio numeric, imp numeric);

		UPDATE
			inventario i
		SET
			stock = stock + tbl.cant
		FROM
			json_to_recordset($6) 
			as tbl(idinv int, cant numeric)
		WHERE
			id = tbl.idinv;

		INSERT INTO
			movimientos(cantidad, comentario, idinventario)
		SELECT
			cant, 'Compra', idinv
		FROM
			json_to_recordset($6) 
			as tbl(idinv int, cant numeric, precio numeric);
		
	END

$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenerdetallescompra(IN cod bigint)
  RETURNS TABLE(codinv int, cant numeric, unidad int, precio numeric, imp numeric) AS
$BODY$

	begin
		return QUERY
			select 
				dtc.idinventario, dtc.cantidad, dtc.idunidad, dtc.preciounitario, dtc.importe 
			from
				detallecompras as dtc
			where 
				dtc.idcompra = $1;
	end

$BODY$
  LANGUAGE plpgsql;

select * from obtenerdetallescompra(2);

DROP FUNCTION public.editarstockinventario(int,numeric);
CREATE OR REPLACE FUNCTION public.editarstockinventario(cod int, stck numeric, comentar varchar)
  RETURNS void AS 
$BODY$
	DECLARE
		idinv int;
	BEGIN
		UPDATE
			inventario
		SET
			stock = stock - $2
		WHERE
			id = $1;
			
		INSERT INTO
			movimientos(cantidad, comentario, idinventario)
		VALUES
			(-$2, $3, $1);
	END
$BODY$
  LANGUAGE plpgsql;

select editarstockinventario(7, 10.00, 'Se quito');

DROP FUNCTION obtenerventas();
CREATE OR REPLACE FUNCTION public.obtenerventas() RETURNS TABLE(
	cod bigint,
	cliente int,
	empleado int,
	tot numeric,
	camb numeric,
	comentario text,
	ltr text,
	subtot numeric, 
	iva numeric,
	nfact varchar,
	fecha timestamp,
	estado boolean
    )AS
$BODY$	
	BEGIN
		RETURN QUERY
			SELECT
				v.id, v.idcliente, v.idempleado, v.total, v.cambio, v.observacion,
				v.letra, v.subtotal, v.iva, v.nfactura, v.fecha, v.estado
			FROM
				ventas as v
			ORDER BY
				v.fecha DESC;
	END

$BODY$
  LANGUAGE plpgsql;

select * from obtenerventas();

select * from obtenercompatibles(7);

CREATE OR REPLACE FUNCTION public.anularventas(cod bigint) RETURNS void AS
$BODY$	
	BEGIN
		UPDATE
			ventas v
		SET
			estado = false
		WHERE
			id = $1;
	END

$BODY$
  LANGUAGE plpgsql;

select anularventas(3);

CREATE OR REPLACE FUNCTION public.registrarcategoria(nom varchar) RETURNS void AS
$BODY$
	BEGIN
		INSERT INTO
			categorias(nombre)
		VALUES
			($1);
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.editarcategoria(nom varchar, cod int) RETURNS void AS
$BODY$
	BEGIN
		UPDATE
			categorias
		SET
			nombre = $1
		WHERE
			id = $2;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.eliminarcategoria(cod int) RETURNS void AS
$BODY$
	BEGIN
		UPDATE
			categorias
		SET
			estado = false
		WHERE
			id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenerniveles()
  RETURNS TABLE(codigo int, nivel character varying, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				n.id, n.nombre, n.estado
			FROM
				admin_niveles AS n;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.obtenermenus()
  RETURNS TABLE(codigo int, menu character varying) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				m.id, m.nombre
			FROM
				admin_menus AS m;
	END
$BODY$
  LANGUAGE plpgsql;
  
DROP FUNCTION obtenermodulos();
CREATE OR REPLACE FUNCTION public.obtenermodulos()
  RETURNS TABLE(codigo int, modulo character varying) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				m.id, m.nombre
			FROM
				admin_modulos AS m;
	END
$BODY$
  LANGUAGE plpgsql;
  
DROP FUNCTION obtenermodulo(int);
CREATE OR REPLACE FUNCTION public.obtenermodulo(cod int)
  RETURNS TABLE(codigo int, modulo character varying) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				m.id, m.nombre
			FROM
				admin_modulos AS m
			WHERE
				m.id = (SELECT idmodulo FROM admin_menus WHERE id = $1);
	END
$BODY$
  LANGUAGE plpgsql;

DROP FUNCTION insertarpermisos(int, int, boolean);
 CREATE OR REPLACE FUNCTION public.insertarpermisos(codlvl int, men json) RETURNS void AS
 $BODY$
	BEGIN
		IF NOT EXISTS (Select * from admin_permisos where idnivel= $1) THEN
			INSERT INTO
				admin_permisos(idnivel, idmenu, permiso)
			SELECT
				$1, *
			FROM json_to_recordset($2) AS tbl(codmenu int, perm boolean);
		ELSE
			UPDATE
				admin_permisos ap
			SET
				permiso = tbl.perm
			FROM
				json_to_recordset($2) AS tbl(codmenu int, perm boolean)
			WHERE 
				idnivel = $1 AND idmenu = tbl.codmenu;
		END IF;
	END
 $BODY$ LANGUAGE plpgsql;

select count(*) from admin_permisos where permiso = true and idnivel = 2;

CREATE OR REPLACE FUNCTION public.registrarnivel(nom varchar) RETURNS void AS
$BODY$
	BEGIN
		INSERT INTO
			admin_niveles(nombre)
		VALUES
			($1);
	END
$BODY$
  LANGUAGE plpgsql;

 CREATE OR REPLACE FUNCTION public.editarnivel(nom varchar, cod int) RETURNS void AS
$BODY$
	BEGIN
		UPDATE
			admin_niveles an
		SET
			nombre = $1
		WHERE
			id = $2;
	END
$BODY$
  LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION public.eliminarnivel(cod int) RETURNS void AS
$BODY$
	BEGIN
		UPDATE
			admin_niveles an
		SET
			estado = false
		WHERE
			id = $1;
	END
$BODY$
  LANGUAGE plpgsql;
  
DROP FUNCTION public.obtenermovimientos();
CREATE OR REPLACE FUNCTION public.obtenermovimientos() 
RETURNS TABLE(codigo bigint, cantidad numeric(20,2), comenta varchar, fech timestamp, codinv int, est boolean)  AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT
				m.id, m.cantidad, m.comentario, m.fecha, m.idinventario, m.estado
			FROM
				movimientos as m;
	END
$BODY$ LANGUAGE plpgsql;

select * from obtenermovimientos();