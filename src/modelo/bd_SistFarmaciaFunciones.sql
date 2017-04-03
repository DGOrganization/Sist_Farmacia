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

DROP FUNCTION obtenerproducto(integer);
CREATE OR REPLACE FUNCTION public.obtenerarticulo(IN id int)
  RETURNS TABLE(cod int, prod character varying, estado boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				a.id, a.nombre, a.estado
			FROM
				articulos as a
			WHERE
				a.id = $1;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerarticulo(1);

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

CREATE OR REPLACE FUNCTION public.obtenerpreciosinv(inv int)
  RETURNS TABLE(codigo int, precio numeric, est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad, p.estado
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
	est boolean) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	i.estado
			FROM
				inventario as i;
	END
$BODY$
  LANGUAGE plpgsql;

select * from obtenerinventarios();

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


CREATE OR REPLACE FUNCTION public.obtenerprecioinv(inventario int)
  RETURNS TABLE(codigo int, cant numeric) AS
$BODY$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad
			FROM
				precios as p
			WHERE 
				p.idinventario = $1;
	END
$BODY$
  LANGUAGE plpgsql;
  
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
	imagen text) RETURNS void AS
$BODY$
	DECLARE 
		idatr int;
		idinv int;
	BEGIN
		INSERT INTO
			articulos(nombre, descripcion)
		VALUES
			($1, $2)
		RETURNING id INTO idatr;

		
		INSERT INTO
			inventario(idarticulo, idunidad, idcategoria, idbodega, stockmin, stockmax, fechavencimiento)
		VALUES
			(idatr, $3, $4, $5, $6, $7, $8)
		RETURNING id INTO idinv;

		INSERT INTO
			precios(cantidad, idinventario)
		SELECT
			*, idinv
		FROM
			json_to_recordset(invprecios) 
			as tbl(cant numeric);

		IF $10 IS NOT NULL || trim($10) <> '' THEN
			INSERT INTO
				imagenes(url, idinventario)
			VALUES
				($10, idinv);
		END IF;
	END
$BODY$
  LANGUAGE plpgsql;
  
 SELECT registrarinventario(
	'Metocarbamol',
	'Para los dolores de musculo cotidiano',
	1,
	2,
	1,
	50,
	500,
	'2018-07-13',
	'[{"cant":"2.50"}, {"cant":"3.50"}, {"cant":"4.50"}, {"cant":"5.50"}, {"cant":"6.50"}, {"cant":"7.50"}]',
	''
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