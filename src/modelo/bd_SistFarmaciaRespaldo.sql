--
-- PostgreSQL database dump
--

-- Dumped from database version 9.6.2
-- Dumped by pg_dump version 9.6.2

-- Started on 2017-04-18 16:49:44

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;
SET row_security = off;

--
-- TOC entry 1 (class 3079 OID 12387)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2565 (class 0 OID 0)
-- Dependencies: 1
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

--
-- TOC entry 269 (class 1255 OID 33037)
-- Name: cargarpermisos(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION cargarpermisos(nivel integer) RETURNS TABLE(codigo integer, menu character varying, permiso boolean)
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.cargarpermisos(nivel integer) OWNER TO postgres;

--
-- TOC entry 274 (class 1255 OID 31172)
-- Name: editarcliente(integer, character varying, character varying, character varying, character, character, character, date, text, character varying, integer, json); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION editarcliente(codigo integer, nombre character varying, apellido1 character varying, apellido2 character varying, d character, n character, genero character, nacimiento date, direccion text, correo character varying, codmunicipio integer, telefonos json) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.editarcliente(codigo integer, nombre character varying, apellido1 character varying, apellido2 character varying, d character, n character, genero character, nacimiento date, direccion text, correo character varying, codmunicipio integer, telefonos json) OWNER TO postgres;

--
-- TOC entry 288 (class 1255 OID 33096)
-- Name: editarinventario(character varying, text, integer, integer, integer, integer, integer, date, json, text, integer, json); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION editarinventario(articulo character varying, decrip text, unidad integer, categoria integer, bodega integer, stckmin integer, stckmax integer, vencimiento date, invprecios json, imagen text, codinv integer, compatibles json) RETURNS void
    LANGUAGE plpgsql
    AS $_$
	DECLARE 
		idatr int;
		imgcount int = 0;
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

		IF $12 IS NOT NULL THEN
			UPDATE
				compatibles c
			SET
				idcompatible = tb.codcomp,
				estado = tb.est
			FROM
				json_to_recordset($12)
				AS tb(codcomp int, est boolean)
			WHERE
				idinventario = $11;
		ELSE
			INSERT INTO
				compatibles(idinventario, idcompatible)
			SELECT
				$11, *
			FROM
				json_to_recordset($12)
				AS tb(codcomp int);
		END IF;
	END
$_$;


ALTER FUNCTION public.editarinventario(articulo character varying, decrip text, unidad integer, categoria integer, bodega integer, stckmin integer, stckmax integer, vencimiento date, invprecios json, imagen text, codinv integer, compatibles json) OWNER TO postgres;

--
-- TOC entry 292 (class 1255 OID 33101)
-- Name: editarproveedor(character varying, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying, integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION editarproveedor(nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying, cod integer) RETURNS void
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		UPDATE
			proveedores p
		SET
			nombre = $1, representante = $2, rfc = $3, direccion = $4, nit = $5, 
			telefono = $6, celular = $7, email = $8, sitioweb = $9
		WHERE
			id = $10;
	END
$_$;


ALTER FUNCTION public.editarproveedor(nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying, cod integer) OWNER TO postgres;

--
-- TOC entry 287 (class 1255 OID 33097)
-- Name: editarstockinventario(integer, numeric); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION editarstockinventario(cod integer, stck numeric) RETURNS void
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		UPDATE
			inventario
		SET
			stock = $2
		WHERE
			id = $1;
	END
$_$;


ALTER FUNCTION public.editarstockinventario(cod integer, stck numeric) OWNER TO postgres;

--
-- TOC entry 275 (class 1255 OID 31173)
-- Name: eliminarcliente(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION eliminarcliente(codigo integer) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.eliminarcliente(codigo integer) OWNER TO postgres;

--
-- TOC entry 286 (class 1255 OID 33050)
-- Name: eliminarinventario(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION eliminarinventario(codinv integer) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.eliminarinventario(codinv integer) OWNER TO postgres;

--
-- TOC entry 283 (class 1255 OID 33076)
-- Name: eliminarproveedor(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION eliminarproveedor(cod integer) RETURNS void
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		UPDATE
			proveedores
		SET
			estado = false
		WHERE
			id = $1;
	END
$_$;


ALTER FUNCTION public.eliminarproveedor(cod integer) OWNER TO postgres;

--
-- TOC entry 263 (class 1255 OID 31190)
-- Name: login(character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION login(usuario character varying, pass character varying) RETURNS boolean
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.login(usuario character varying, pass character varying) OWNER TO postgres;

--
-- TOC entry 271 (class 1255 OID 33048)
-- Name: obtenerarticulo(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerarticulo(id integer) RETURNS TABLE(cod integer, prod character varying, descrip text, estado boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT 
				a.id, a.nombre, a.descripcion, a.estado
			FROM
				articulos as a
			WHERE
				a.id = $1;
	END
$_$;


ALTER FUNCTION public.obtenerarticulo(id integer) OWNER TO postgres;

--
-- TOC entry 268 (class 1255 OID 33031)
-- Name: obtenerbodega(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerbodega(cod integer) RETURNS TABLE(codigo integer, bodega character varying, dir text, est boolean)
    LANGUAGE plpgsql
    AS $_$
begin
	return QUERY
		SELECT 
			b.id, b.nombre, b.ubicacion, b.estado
		FROM
			bodegas as b
		WHERE
			b.id = $1;
end
$_$;


ALTER FUNCTION public.obtenerbodega(cod integer) OWNER TO postgres;

--
-- TOC entry 276 (class 1255 OID 33045)
-- Name: obtenerbodegas(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerbodegas() RETURNS TABLE(codigo integer, bodega character varying, dir text, est boolean)
    LANGUAGE plpgsql
    AS $$
begin
	return QUERY
		SELECT 
			b.id, b.nombre, b.ubicacion, b.estado
		FROM
			bodegas as b;
end
$$;


ALTER FUNCTION public.obtenerbodegas() OWNER TO postgres;

--
-- TOC entry 244 (class 1255 OID 33026)
-- Name: obtenercategoria(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenercategoria(cod integer) RETURNS TABLE(codigo integer, categoria character varying, estado boolean)
    LANGUAGE plpgsql
    AS $_$
begin
	return QUERY
		SELECT 
			c.id, c.nombre, c.estado
		FROM
			categorias as c
		WHERE
			c.id = $1;
end
$_$;


ALTER FUNCTION public.obtenercategoria(cod integer) OWNER TO postgres;

--
-- TOC entry 277 (class 1255 OID 33046)
-- Name: obtenercategorias(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenercategorias() RETURNS TABLE(codigo integer, categoria character varying, estado boolean)
    LANGUAGE plpgsql
    AS $$
begin
	return QUERY
		SELECT 
			c.id, c.nombre, c.estado
		FROM
			categorias as c;
end
$$;


ALTER FUNCTION public.obtenercategorias() OWNER TO postgres;

--
-- TOC entry 259 (class 1255 OID 31089)
-- Name: obtenercliente(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenercliente(cod integer) RETURNS TABLE(codigo integer, nombre character varying, apellido1 character varying, apellido2 character varying, dui character, nit character, genero character, nacimiento date, direccion text, correo character varying, codpersona integer, est boolean)
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.obtenercliente(cod integer) OWNER TO postgres;

--
-- TOC entry 258 (class 1255 OID 31088)
-- Name: obtenerclientes(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerclientes() RETURNS TABLE(codigo integer, nombre character varying, apellido1 character varying, apellido2 character varying, dui character, nit character, genero character, nacimiento date, direccion text, correo character varying, codpersona integer, est boolean)
    LANGUAGE plpgsql
    AS $$
	BEGIN
		RETURN QUERY
			SELECT
				c.id as cod1, p.nombre, p.apellidopaterno, p.apellidomaterno, p.dui, p.nit, p.sexo, p.fechanacimiento, 
				p.direccion, p.email, p.id as cod2, p.estado
			FROM
				clientes as c
				inner join personas as p on c.idpersona = p.id;
		
	END
$$;


ALTER FUNCTION public.obtenerclientes() OWNER TO postgres;

--
-- TOC entry 285 (class 1255 OID 33086)
-- Name: obtenercompatibles(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenercompatibles(cod integer) RETURNS TABLE(codigo integer, articulo integer, unidad integer, categoria integer, bodega integer, stck numeric, stckmin integer, stckmax integer, vencimiento date, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	c.estado
			FROM
				inventario as i inner join compatibles as c on c.idinventario = i.id
			WHERE
				c.idinventario = $1;
	END
$_$;


ALTER FUNCTION public.obtenercompatibles(cod integer) OWNER TO postgres;

--
-- TOC entry 282 (class 1255 OID 33071)
-- Name: obtenerdepartamento(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerdepartamento(cod integer) RETURNS TABLE(codigo integer, nom character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				departamentos
			WHERE
				id = $1;
	END
$_$;


ALTER FUNCTION public.obtenerdepartamento(cod integer) OWNER TO postgres;

--
-- TOC entry 262 (class 1255 OID 31169)
-- Name: obtenerdepartamentos(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerdepartamentos() RETURNS TABLE(codigo integer, nom character varying, est boolean)
    LANGUAGE plpgsql
    AS $$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				departamentos;
	END
$$;


ALTER FUNCTION public.obtenerdepartamentos() OWNER TO postgres;

--
-- TOC entry 280 (class 1255 OID 33083)
-- Name: obtenerdetallescompra(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerdetallescompra(cod bigint) RETURNS TABLE(codinv integer, cant numeric, unidad integer, precio numeric, imp numeric)
    LANGUAGE plpgsql
    AS $_$

	begin
		return QUERY
			select 
				dtc.idinventario, dtc.cantidad, dtc.idunidad, dtc.preciounitario, dtc.importe 
			from
				detallecompras as dtc
			where 
				dtc.idcompra = $1;
	end

$_$;


ALTER FUNCTION public.obtenerdetallescompra(cod bigint) OWNER TO postgres;

--
-- TOC entry 279 (class 1255 OID 33079)
-- Name: obtenerdetallesventa(bigint); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerdetallesventa(cod bigint) RETURNS TABLE(codinv integer, cant numeric, unidad integer, precio numeric, imp numeric, descpor numeric)
    LANGUAGE plpgsql
    AS $_$

	begin
		return QUERY
			select 
				dtv.idinventario, dtv.cantidad, dtv.idunidad, dtv.preciounitario, dtv.importe, dtv.descporcentaje 
			from
				detalleventas as dtv
			where 
				dtv.idventa = $1;
	end

$_$;


ALTER FUNCTION public.obtenerdetallesventa(cod bigint) OWNER TO postgres;

--
-- TOC entry 267 (class 1255 OID 31192)
-- Name: obtenerempleado(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerempleado(cod integer) RETURNS TABLE(codigo integer, nombre character varying, apellido1 character varying, apellido2 character varying, dui character, nit character, genero character, nacimiento date, direccion text, correo character varying, codpersona integer, est boolean)
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.obtenerempleado(cod integer) OWNER TO postgres;

--
-- TOC entry 270 (class 1255 OID 33041)
-- Name: obtenerimagen(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerimagen(inventario integer) RETURNS TABLE(codigo integer, url text)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.url
			FROM
				imagenes as i
			WHERE 
				i.idinventario = $1;
	END
$_$;


ALTER FUNCTION public.obtenerimagen(inventario integer) OWNER TO postgres;

--
-- TOC entry 295 (class 1255 OID 33110)
-- Name: obtenerinventario(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerinventario(cod integer) RETURNS TABLE(codigo integer, articulo integer, unidad integer, categoria integer, bodega integer, stck numeric, stckmin integer, stckmax integer, vencimiento date, est boolean, intcod character varying)
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.obtenerinventario(cod integer) OWNER TO postgres;

--
-- TOC entry 296 (class 1255 OID 33111)
-- Name: obtenerinventarios(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerinventarios() RETURNS TABLE(codigo integer, articulo integer, unidad integer, categoria integer, bodega integer, stck numeric, stckmin integer, stckmax integer, vencimiento date, est boolean, intcod character varying)
    LANGUAGE plpgsql
    AS $$
	BEGIN
		RETURN QUERY
			SELECT 
				i.id, i.idarticulo, i.idunidad, i.idcategoria, i.idbodega, i.stock, i.stockmin, 
				i.stockmax, i.fechavencimiento,	i.estado, i.internalcod
			FROM
				inventario as i;
	END
$$;


ALTER FUNCTION public.obtenerinventarios() OWNER TO postgres;

--
-- TOC entry 261 (class 1255 OID 31168)
-- Name: obtenermunicipiodepto(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenermunicipiodepto(cod integer) RETURNS TABLE(codigo integer, nom character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				municipios
			WHERE
				iddepartamento = $1;
	END
$_$;


ALTER FUNCTION public.obtenermunicipiodepto(cod integer) OWNER TO postgres;

--
-- TOC entry 260 (class 1255 OID 31166)
-- Name: obtenermunicipiopersona(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenermunicipiopersona(cod integer) RETURNS TABLE(codigo integer, nom character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT
				id, nombre, estado
			FROM
				municipios
			WHERE
				id = (SELECT idmunicipio FROM personas WHERE id = $1);
	END
$_$;


ALTER FUNCTION public.obtenermunicipiopersona(cod integer) OWNER TO postgres;

--
-- TOC entry 265 (class 1255 OID 33036)
-- Name: obtenernivel(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenernivel(id integer) RETURNS TABLE(codigo integer, nivel character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT
				n.id, n.nombre, n.estado
			FROM
				admin_niveles AS n
			WHERE
				n.id = $1;
	END
$_$;


ALTER FUNCTION public.obtenernivel(id integer) OWNER TO postgres;

--
-- TOC entry 284 (class 1255 OID 33088)
-- Name: obtenerprecioinv(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerprecioinv(inventario integer) RETURNS TABLE(codigo integer, cant numeric, tipo character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad, p.tipo, p.estado
			FROM
				precios as p
			WHERE 
				p.idinventario = $1;
	END
$_$;


ALTER FUNCTION public.obtenerprecioinv(inventario integer) OWNER TO postgres;

--
-- TOC entry 272 (class 1255 OID 33084)
-- Name: obtenerpreciosinv(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerpreciosinv(inv integer) RETURNS TABLE(codigo integer, precio numeric, tipo character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT 
				p.id, p.cantidad, p.tipo, p.estado
			FROM
				precios as p
			WHERE
				p.idinventario = $1;
	END
$_$;


ALTER FUNCTION public.obtenerpreciosinv(inv integer) OWNER TO postgres;

--
-- TOC entry 290 (class 1255 OID 33099)
-- Name: obtenerproveedor(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerproveedor(codigo integer) RETURNS TABLE(cod integer, nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.obtenerproveedor(codigo integer) OWNER TO postgres;

--
-- TOC entry 289 (class 1255 OID 33098)
-- Name: obtenerproveedores(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerproveedores() RETURNS TABLE(cod integer, nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying, est boolean)
    LANGUAGE plpgsql
    AS $$
	BEGIN
		RETURN QUERY
			SELECT
				prov.id, prov.nombre, prov.representante, prov.rfc, prov.direccion,
				prov.nit, prov.telefono, prov.celular, prov.email, prov.sitioweb, prov.estado
			FROM
				proveedores as prov;
	END
$$;


ALTER FUNCTION public.obtenerproveedores() OWNER TO postgres;

--
-- TOC entry 243 (class 1255 OID 31084)
-- Name: obtenertelefonos(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenertelefonos(cod integer) RETURNS TABLE(codigo integer, num character varying, tip character varying, est boolean)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		RETURN QUERY
			SELECT
				id, numero, tipo, estado
			FROM
				telefonos
			WHERE
				idpersona = $1;
	END
$_$;


ALTER FUNCTION public.obtenertelefonos(cod integer) OWNER TO postgres;

--
-- TOC entry 245 (class 1255 OID 33027)
-- Name: obtenerunidad(integer); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerunidad(cod integer) RETURNS TABLE(codigo integer, unidad character varying, abrev character varying)
    LANGUAGE plpgsql
    AS $_$
begin
	return QUERY
		SELECT 
			u.id, u.nombre, u.abreviado
		FROM
			unidades as u
		WHERE
			u.id = $1;
end
$_$;


ALTER FUNCTION public.obtenerunidad(cod integer) OWNER TO postgres;

--
-- TOC entry 278 (class 1255 OID 33047)
-- Name: obtenerunidades(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerunidades() RETURNS TABLE(codigo integer, unidad character varying, abrev character varying)
    LANGUAGE plpgsql
    AS $$
begin
	return QUERY
		SELECT 
			u.id, u.nombre, u.abreviado
		FROM
			unidades as u;
end
$$;


ALTER FUNCTION public.obtenerunidades() OWNER TO postgres;

--
-- TOC entry 266 (class 1255 OID 31193)
-- Name: obtenerusuario(character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerusuario(usuario character varying) RETURNS TABLE(nickname character varying, estado boolean, idempleado integer, idnivel integer)
    LANGUAGE plpgsql
    AS $_$
	BEGIN
	RETURN QUERY
		SELECT
			u.nickname, u.estado, u.idempleado, u.idnivel
		FROM
			admin_usuarios AS u
		WHERE
			u.nickname = $1;
	END
$_$;


ALTER FUNCTION public.obtenerusuario(usuario character varying) OWNER TO postgres;

--
-- TOC entry 264 (class 1255 OID 33017)
-- Name: obtenerusuarios(); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION obtenerusuarios() RETURNS TABLE(nickname character varying, estado boolean, idempleado integer, idnivel integer)
    LANGUAGE plpgsql
    AS $$
	BEGIN
	RETURN QUERY
		SELECT
			u.nickname, u.estado, u.idempleado, u.idnivel
		FROM
			admin_usuarios AS u;
	END
$$;


ALTER FUNCTION public.obtenerusuarios() OWNER TO postgres;

--
-- TOC entry 273 (class 1255 OID 31171)
-- Name: registrarcliente(character varying, character varying, character varying, character, character, character, date, text, character varying, integer, json); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION registrarcliente(nombre character varying, apellido1 character varying, apellido2 character varying, d character, n character, genero character, nacimiento date, direccion text, correo character varying, codmunicipio integer, telefonos json) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.registrarcliente(nombre character varying, apellido1 character varying, apellido2 character varying, d character, n character, genero character, nacimiento date, direccion text, correo character varying, codmunicipio integer, telefonos json) OWNER TO postgres;

--
-- TOC entry 281 (class 1255 OID 33082)
-- Name: registrarcompra(character varying, text, numeric, integer, integer, json); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION registrarcompra(numfact character varying, descrip text, tot numeric, empleado integer, proveedor integer, detalles json) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
		
	END

$_$;


ALTER FUNCTION public.registrarcompra(numfact character varying, descrip text, tot numeric, empleado integer, proveedor integer, detalles json) OWNER TO postgres;

--
-- TOC entry 294 (class 1255 OID 33089)
-- Name: registrarinventario(character varying, text, integer, integer, integer, integer, integer, date, json, text, json); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION registrarinventario(articulo character varying, decrip text, unidad integer, categoria integer, bodega integer, stckmin integer, stckmax integer, vencimiento date, invprecios json, imagen text, compatibles json) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
$_$;


ALTER FUNCTION public.registrarinventario(articulo character varying, decrip text, unidad integer, categoria integer, bodega integer, stckmin integer, stckmax integer, vencimiento date, invprecios json, imagen text, compatibles json) OWNER TO postgres;

--
-- TOC entry 291 (class 1255 OID 33100)
-- Name: registrarproveedor(character varying, character varying, character varying, text, character varying, character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION registrarproveedor(nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying) RETURNS void
    LANGUAGE plpgsql
    AS $_$
	BEGIN
		INSERT INTO
			proveedores(nombre, representante, rfc, direccion, nit, telefono, celular, email, sitioweb)
		VALUES
			($1, $2, $3, $4, $5, $6, $7, $8, $9);
	END
$_$;


ALTER FUNCTION public.registrarproveedor(nombre character varying, represent character varying, nrc character varying, direccion text, nit character varying, telefono character varying, celular character varying, email character varying, website character varying) OWNER TO postgres;

--
-- TOC entry 293 (class 1255 OID 33103)
-- Name: registrarventa(integer, integer, numeric, numeric, text, text, json, numeric, numeric, character varying); Type: FUNCTION; Schema: public; Owner: postgres
--

CREATE FUNCTION registrarventa(cliente integer, empleado integer, tot numeric, camb numeric, comentario text, ltr text, detalles json, subtot numeric, iva numeric, nfact character varying) RETURNS void
    LANGUAGE plpgsql
    AS $_$
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
		
	END

$_$;


ALTER FUNCTION public.registrarventa(cliente integer, empleado integer, tot numeric, camb numeric, comentario text, ltr text, detalles json, subtot numeric, iva numeric, nfact character varying) OWNER TO postgres;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 188 (class 1259 OID 30604)
-- Name: admin_menus; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE admin_menus (
    id integer NOT NULL,
    nombre character varying(45) NOT NULL,
    idmodulo integer,
    idmenu integer
);


ALTER TABLE admin_menus OWNER TO postgres;

--
-- TOC entry 187 (class 1259 OID 30602)
-- Name: admin_menus_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE admin_menus_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE admin_menus_id_seq OWNER TO postgres;

--
-- TOC entry 2566 (class 0 OID 0)
-- Dependencies: 187
-- Name: admin_menus_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE admin_menus_id_seq OWNED BY admin_menus.id;


--
-- TOC entry 186 (class 1259 OID 30598)
-- Name: admin_modulos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE admin_modulos (
    id integer NOT NULL,
    nombre character varying(45) NOT NULL
);


ALTER TABLE admin_modulos OWNER TO postgres;

--
-- TOC entry 185 (class 1259 OID 30596)
-- Name: admin_modulos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE admin_modulos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE admin_modulos_id_seq OWNER TO postgres;

--
-- TOC entry 2567 (class 0 OID 0)
-- Dependencies: 185
-- Name: admin_modulos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE admin_modulos_id_seq OWNED BY admin_modulos.id;


--
-- TOC entry 190 (class 1259 OID 30610)
-- Name: admin_niveles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE admin_niveles (
    id integer NOT NULL,
    nombre character varying(50),
    estado boolean DEFAULT true
);


ALTER TABLE admin_niveles OWNER TO postgres;

--
-- TOC entry 189 (class 1259 OID 30608)
-- Name: admin_niveles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE admin_niveles_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE admin_niveles_id_seq OWNER TO postgres;

--
-- TOC entry 2568 (class 0 OID 0)
-- Dependencies: 189
-- Name: admin_niveles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE admin_niveles_id_seq OWNED BY admin_niveles.id;


--
-- TOC entry 191 (class 1259 OID 30615)
-- Name: admin_permisos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE admin_permisos (
    idnivel integer NOT NULL,
    idmenu integer NOT NULL,
    permiso boolean
);


ALTER TABLE admin_permisos OWNER TO postgres;

--
-- TOC entry 192 (class 1259 OID 30618)
-- Name: admin_usuarios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE admin_usuarios (
    nickname character varying(20) NOT NULL,
    clave character varying(32),
    estado boolean DEFAULT true,
    idempleado integer,
    idnivel integer
);


ALTER TABLE admin_usuarios OWNER TO postgres;

--
-- TOC entry 194 (class 1259 OID 30624)
-- Name: articulos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE articulos (
    id integer NOT NULL,
    nombre character varying(80) NOT NULL,
    estado boolean DEFAULT true,
    descripcion text
);


ALTER TABLE articulos OWNER TO postgres;

--
-- TOC entry 193 (class 1259 OID 30622)
-- Name: articulos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE articulos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE articulos_id_seq OWNER TO postgres;

--
-- TOC entry 2569 (class 0 OID 0)
-- Dependencies: 193
-- Name: articulos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE articulos_id_seq OWNED BY articulos.id;


--
-- TOC entry 196 (class 1259 OID 30631)
-- Name: bodegas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE bodegas (
    id integer NOT NULL,
    nombre character varying(20) NOT NULL,
    ubicacion text NOT NULL,
    estado boolean
);


ALTER TABLE bodegas OWNER TO postgres;

--
-- TOC entry 195 (class 1259 OID 30629)
-- Name: bodegas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE bodegas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE bodegas_id_seq OWNER TO postgres;

--
-- TOC entry 2570 (class 0 OID 0)
-- Dependencies: 195
-- Name: bodegas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE bodegas_id_seq OWNED BY bodegas.id;


--
-- TOC entry 206 (class 1259 OID 30667)
-- Name: cajas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cajas (
    id integer NOT NULL,
    nombre character varying(45) NOT NULL,
    total numeric(20,2) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE cajas OWNER TO postgres;

--
-- TOC entry 205 (class 1259 OID 30665)
-- Name: cajas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cajas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cajas_id_seq OWNER TO postgres;

--
-- TOC entry 2571 (class 0 OID 0)
-- Dependencies: 205
-- Name: cajas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cajas_id_seq OWNED BY cajas.id;


--
-- TOC entry 208 (class 1259 OID 30673)
-- Name: categorias; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE categorias (
    id integer NOT NULL,
    nombre character varying(45) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE categorias OWNER TO postgres;

--
-- TOC entry 207 (class 1259 OID 30671)
-- Name: categorias_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE categorias_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE categorias_id_seq OWNER TO postgres;

--
-- TOC entry 2572 (class 0 OID 0)
-- Dependencies: 207
-- Name: categorias_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE categorias_id_seq OWNED BY categorias.id;


--
-- TOC entry 218 (class 1259 OID 30706)
-- Name: clientes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE clientes (
    id integer NOT NULL,
    idpersona integer NOT NULL
);


ALTER TABLE clientes OWNER TO postgres;

--
-- TOC entry 217 (class 1259 OID 30704)
-- Name: clientes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE clientes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE clientes_id_seq OWNER TO postgres;

--
-- TOC entry 2573 (class 0 OID 0)
-- Dependencies: 217
-- Name: clientes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE clientes_id_seq OWNED BY clientes.id;


--
-- TOC entry 219 (class 1259 OID 30710)
-- Name: compatibles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE compatibles (
    idinventario integer NOT NULL,
    idcompatible integer NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE compatibles OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 30715)
-- Name: compras; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE compras (
    id bigint NOT NULL,
    nfactura character varying(15) NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    descripcion text,
    total numeric(20,2) NOT NULL,
    idproveedor integer NOT NULL,
    idempleado integer NOT NULL,
    estado boolean DEFAULT true
);


ALTER TABLE compras OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 30713)
-- Name: compras_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE compras_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE compras_id_seq OWNER TO postgres;

--
-- TOC entry 2574 (class 0 OID 0)
-- Dependencies: 220
-- Name: compras_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE compras_id_seq OWNED BY compras.id;


--
-- TOC entry 199 (class 1259 OID 30644)
-- Name: conversiones; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE conversiones (
    idunidadp integer NOT NULL,
    idunidads integer NOT NULL,
    equivalencia numeric(20,2) NOT NULL
);


ALTER TABLE conversiones OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 30725)
-- Name: cortecajas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE cortecajas (
    id integer NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    contado numeric(20,2) NOT NULL,
    calculado numeric(20,2) NOT NULL,
    diferencia numeric(20,2) NOT NULL,
    retiro numeric(20,2) NOT NULL,
    idcaja integer NOT NULL
);


ALTER TABLE cortecajas OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 30723)
-- Name: cortecajas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE cortecajas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE cortecajas_id_seq OWNER TO postgres;

--
-- TOC entry 2575 (class 0 OID 0)
-- Dependencies: 222
-- Name: cortecajas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE cortecajas_id_seq OWNED BY cortecajas.id;


--
-- TOC entry 214 (class 1259 OID 30694)
-- Name: departamentos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE departamentos (
    id integer NOT NULL,
    nombre character varying(80) NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE departamentos OWNER TO postgres;

--
-- TOC entry 213 (class 1259 OID 30692)
-- Name: departamentos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE departamentos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE departamentos_id_seq OWNER TO postgres;

--
-- TOC entry 2576 (class 0 OID 0)
-- Dependencies: 213
-- Name: departamentos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE departamentos_id_seq OWNED BY departamentos.id;


--
-- TOC entry 224 (class 1259 OID 30729)
-- Name: detallecompras; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE detallecompras (
    idcompra bigint NOT NULL,
    idinventario integer NOT NULL,
    cantidad numeric(20,4) NOT NULL,
    idunidad integer,
    preciounitario numeric(20,6) NOT NULL,
    importe numeric(20,2) NOT NULL
);


ALTER TABLE detallecompras OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 30732)
-- Name: detalleventas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE detalleventas (
    idventa bigint NOT NULL,
    idinventario integer NOT NULL,
    cantidad numeric(20,2) DEFAULT 0.000 NOT NULL,
    idunidad integer,
    preciounitario numeric(20,2) DEFAULT 0.000 NOT NULL,
    importe numeric(20,2) NOT NULL,
    descporcentaje numeric(20,2),
    desctotal numeric(20,2)
);


ALTER TABLE detalleventas OWNER TO postgres;

--
-- TOC entry 228 (class 1259 OID 30743)
-- Name: detallevimpuesto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE detallevimpuesto (
    idventa integer NOT NULL,
    idinventario integer NOT NULL,
    idimpuesto integer NOT NULL,
    importe numeric(20,2) NOT NULL
);


ALTER TABLE detallevimpuesto OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 30779)
-- Name: empleados; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE empleados (
    id integer NOT NULL,
    idpersona integer NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE empleados OWNER TO postgres;

--
-- TOC entry 237 (class 1259 OID 30777)
-- Name: empleados_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE empleados_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE empleados_id_seq OWNER TO postgres;

--
-- TOC entry 2577 (class 0 OID 0)
-- Dependencies: 237
-- Name: empleados_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE empleados_id_seq OWNED BY empleados.id;


--
-- TOC entry 230 (class 1259 OID 30748)
-- Name: imagenes; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE imagenes (
    id integer NOT NULL,
    url text NOT NULL,
    idinventario integer NOT NULL
);


ALTER TABLE imagenes OWNER TO postgres;

--
-- TOC entry 229 (class 1259 OID 30746)
-- Name: imagenes_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE imagenes_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE imagenes_id_seq OWNER TO postgres;

--
-- TOC entry 2578 (class 0 OID 0)
-- Dependencies: 229
-- Name: imagenes_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE imagenes_id_seq OWNED BY imagenes.id;


--
-- TOC entry 227 (class 1259 OID 30739)
-- Name: impuestos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE impuestos (
    id integer NOT NULL,
    nombre character varying(20) NOT NULL,
    porcentaje numeric(20,6) NOT NULL,
    estado boolean NOT NULL
);


ALTER TABLE impuestos OWNER TO postgres;

--
-- TOC entry 226 (class 1259 OID 30737)
-- Name: impuestos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE impuestos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE impuestos_id_seq OWNER TO postgres;

--
-- TOC entry 2579 (class 0 OID 0)
-- Dependencies: 226
-- Name: impuestos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE impuestos_id_seq OWNED BY impuestos.id;


--
-- TOC entry 201 (class 1259 OID 30649)
-- Name: inventario; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE inventario (
    id integer NOT NULL,
    idarticulo integer NOT NULL,
    idcategoria integer NOT NULL,
    idunidad integer NOT NULL,
    estado boolean DEFAULT true,
    stock numeric(20,2) DEFAULT 0 NOT NULL,
    stockmin integer NOT NULL,
    stockmax integer NOT NULL,
    fechavencimiento date NOT NULL,
    idbodega integer,
    internalcod character varying(15)
);


ALTER TABLE inventario OWNER TO postgres;

--
-- TOC entry 200 (class 1259 OID 30647)
-- Name: inventario_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE inventario_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE inventario_id_seq OWNER TO postgres;

--
-- TOC entry 2580 (class 0 OID 0)
-- Dependencies: 200
-- Name: inventario_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE inventario_id_seq OWNED BY inventario.id;


--
-- TOC entry 232 (class 1259 OID 30757)
-- Name: movimientos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE movimientos (
    id integer NOT NULL,
    total numeric(20,2) NOT NULL,
    comentario character varying(255) NOT NULL,
    "tipoMov" integer NOT NULL,
    idcaja integer NOT NULL,
    idinventario integer NOT NULL,
    idcorte integer,
    estado boolean DEFAULT true
);


ALTER TABLE movimientos OWNER TO postgres;

--
-- TOC entry 231 (class 1259 OID 30755)
-- Name: movimientos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE movimientos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE movimientos_id_seq OWNER TO postgres;

--
-- TOC entry 2581 (class 0 OID 0)
-- Dependencies: 231
-- Name: movimientos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE movimientos_id_seq OWNED BY movimientos.id;


--
-- TOC entry 216 (class 1259 OID 30700)
-- Name: municipios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE municipios (
    id integer NOT NULL,
    nombre character varying(80) NOT NULL,
    iddepartamento integer NOT NULL,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE municipios OWNER TO postgres;

--
-- TOC entry 215 (class 1259 OID 30698)
-- Name: municipios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE municipios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE municipios_id_seq OWNER TO postgres;

--
-- TOC entry 2582 (class 0 OID 0)
-- Dependencies: 215
-- Name: municipios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE municipios_id_seq OWNED BY municipios.id;


--
-- TOC entry 210 (class 1259 OID 30679)
-- Name: personas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE personas (
    id integer NOT NULL,
    nombre character varying(120) NOT NULL,
    dui character(10),
    nit character(17),
    fechanacimiento date,
    direccion text NOT NULL,
    email character varying(255),
    idmunicipio integer NOT NULL,
    estado boolean DEFAULT true NOT NULL,
    sexo character(1) NOT NULL,
    apellidopaterno character varying(60),
    apellidomaterno character varying(60)
);


ALTER TABLE personas OWNER TO postgres;

--
-- TOC entry 209 (class 1259 OID 30677)
-- Name: personas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE personas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE personas_id_seq OWNER TO postgres;

--
-- TOC entry 2583 (class 0 OID 0)
-- Dependencies: 209
-- Name: personas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE personas_id_seq OWNED BY personas.id;


--
-- TOC entry 204 (class 1259 OID 30662)
-- Name: precioimpuesto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE precioimpuesto (
    idprecio integer NOT NULL,
    idimpuesto integer NOT NULL,
    importe numeric(20,2) NOT NULL
);


ALTER TABLE precioimpuesto OWNER TO postgres;

--
-- TOC entry 203 (class 1259 OID 30656)
-- Name: precios; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE precios (
    id integer NOT NULL,
    cantidad numeric(20,2) DEFAULT 0.00 NOT NULL,
    idinventario integer NOT NULL,
    estado boolean DEFAULT true,
    tipo character varying(30)
);


ALTER TABLE precios OWNER TO postgres;

--
-- TOC entry 202 (class 1259 OID 30654)
-- Name: precios_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE precios_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE precios_id_seq OWNER TO postgres;

--
-- TOC entry 2584 (class 0 OID 0)
-- Dependencies: 202
-- Name: precios_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE precios_id_seq OWNED BY precios.id;


--
-- TOC entry 234 (class 1259 OID 30764)
-- Name: proveedores; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE proveedores (
    id integer NOT NULL,
    nombre character varying(120) NOT NULL,
    representante character varying(120) NOT NULL,
    direccion text NOT NULL,
    rfc character varying(45) NOT NULL,
    telefono character varying(45) NOT NULL,
    celular character varying(45) NOT NULL,
    email character varying(255),
    sitioweb character varying(300),
    estado boolean DEFAULT true NOT NULL,
    nit character varying(20) NOT NULL
);


ALTER TABLE proveedores OWNER TO postgres;

--
-- TOC entry 233 (class 1259 OID 30762)
-- Name: proveedores_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE proveedores_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE proveedores_id_seq OWNER TO postgres;

--
-- TOC entry 2585 (class 0 OID 0)
-- Dependencies: 233
-- Name: proveedores_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE proveedores_id_seq OWNED BY proveedores.id;


--
-- TOC entry 236 (class 1259 OID 30773)
-- Name: resumencortecaja; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE resumencortecaja (
    id integer NOT NULL,
    ventcontado numeric(20,2) NOT NULL,
    comcontado numeric(20,2) NOT NULL,
    entramovi numeric(20,2) NOT NULL,
    salicompra numeric(20,2) NOT NULL,
    salimovi numeric(20,2) NOT NULL,
    idcorte integer
);


ALTER TABLE resumencortecaja OWNER TO postgres;

--
-- TOC entry 235 (class 1259 OID 30771)
-- Name: resumencortecaja_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE resumencortecaja_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE resumencortecaja_id_seq OWNER TO postgres;

--
-- TOC entry 2586 (class 0 OID 0)
-- Dependencies: 235
-- Name: resumencortecaja_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE resumencortecaja_id_seq OWNED BY resumencortecaja.id;


--
-- TOC entry 212 (class 1259 OID 30688)
-- Name: telefonos; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE telefonos (
    id integer NOT NULL,
    numero character varying(10) NOT NULL,
    tipo character varying(10) NOT NULL,
    idpersona integer,
    estado boolean DEFAULT true NOT NULL
);


ALTER TABLE telefonos OWNER TO postgres;

--
-- TOC entry 211 (class 1259 OID 30686)
-- Name: telefonos_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE telefonos_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE telefonos_id_seq OWNER TO postgres;

--
-- TOC entry 2587 (class 0 OID 0)
-- Dependencies: 211
-- Name: telefonos_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE telefonos_id_seq OWNED BY telefonos.id;


--
-- TOC entry 198 (class 1259 OID 30640)
-- Name: unidades; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE unidades (
    id integer NOT NULL,
    nombre character varying(20) NOT NULL,
    abreviado character varying(20) NOT NULL
);


ALTER TABLE unidades OWNER TO postgres;

--
-- TOC entry 197 (class 1259 OID 30638)
-- Name: unidades_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE unidades_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE unidades_id_seq OWNER TO postgres;

--
-- TOC entry 2588 (class 0 OID 0)
-- Dependencies: 197
-- Name: unidades_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE unidades_id_seq OWNED BY unidades.id;


--
-- TOC entry 242 (class 1259 OID 30795)
-- Name: ventaimpuesto; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE ventaimpuesto (
    idventa integer NOT NULL,
    idimpuesto integer NOT NULL,
    importe numeric(20,2) NOT NULL
);


ALTER TABLE ventaimpuesto OWNER TO postgres;

--
-- TOC entry 241 (class 1259 OID 30793)
-- Name: ventaimpuesto_idventa_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ventaimpuesto_idventa_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ventaimpuesto_idventa_seq OWNER TO postgres;

--
-- TOC entry 2589 (class 0 OID 0)
-- Dependencies: 241
-- Name: ventaimpuesto_idventa_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ventaimpuesto_idventa_seq OWNED BY ventaimpuesto.idventa;


--
-- TOC entry 240 (class 1259 OID 30785)
-- Name: ventas; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE ventas (
    id bigint NOT NULL,
    fecha timestamp(6) without time zone NOT NULL,
    subtotal numeric(20,2),
    total numeric(20,2) NOT NULL,
    cambio numeric(20,2) NOT NULL,
    observacion text NOT NULL,
    idempleado integer,
    idcliente integer,
    estado boolean DEFAULT true,
    letra text,
    iva numeric(20,2),
    nfactura character varying(20)
);


ALTER TABLE ventas OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 30783)
-- Name: ventas_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE ventas_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE ventas_id_seq OWNER TO postgres;

--
-- TOC entry 2590 (class 0 OID 0)
-- Dependencies: 239
-- Name: ventas_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE ventas_id_seq OWNED BY ventas.id;


--
-- TOC entry 2227 (class 2604 OID 30607)
-- Name: admin_menus id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_menus ALTER COLUMN id SET DEFAULT nextval('admin_menus_id_seq'::regclass);


--
-- TOC entry 2226 (class 2604 OID 30601)
-- Name: admin_modulos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_modulos ALTER COLUMN id SET DEFAULT nextval('admin_modulos_id_seq'::regclass);


--
-- TOC entry 2228 (class 2604 OID 30613)
-- Name: admin_niveles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_niveles ALTER COLUMN id SET DEFAULT nextval('admin_niveles_id_seq'::regclass);


--
-- TOC entry 2231 (class 2604 OID 30627)
-- Name: articulos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY articulos ALTER COLUMN id SET DEFAULT nextval('articulos_id_seq'::regclass);


--
-- TOC entry 2233 (class 2604 OID 30634)
-- Name: bodegas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bodegas ALTER COLUMN id SET DEFAULT nextval('bodegas_id_seq'::regclass);


--
-- TOC entry 2241 (class 2604 OID 30670)
-- Name: cajas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cajas ALTER COLUMN id SET DEFAULT nextval('cajas_id_seq'::regclass);


--
-- TOC entry 2242 (class 2604 OID 30676)
-- Name: categorias id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categorias ALTER COLUMN id SET DEFAULT nextval('categorias_id_seq'::regclass);


--
-- TOC entry 2251 (class 2604 OID 30709)
-- Name: clientes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY clientes ALTER COLUMN id SET DEFAULT nextval('clientes_id_seq'::regclass);


--
-- TOC entry 2253 (class 2604 OID 30718)
-- Name: compras id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compras ALTER COLUMN id SET DEFAULT nextval('compras_id_seq'::regclass);


--
-- TOC entry 2255 (class 2604 OID 30728)
-- Name: cortecajas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cortecajas ALTER COLUMN id SET DEFAULT nextval('cortecajas_id_seq'::regclass);


--
-- TOC entry 2247 (class 2604 OID 30697)
-- Name: departamentos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY departamentos ALTER COLUMN id SET DEFAULT nextval('departamentos_id_seq'::regclass);


--
-- TOC entry 2265 (class 2604 OID 30782)
-- Name: empleados id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY empleados ALTER COLUMN id SET DEFAULT nextval('empleados_id_seq'::regclass);


--
-- TOC entry 2259 (class 2604 OID 30751)
-- Name: imagenes id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagenes ALTER COLUMN id SET DEFAULT nextval('imagenes_id_seq'::regclass);


--
-- TOC entry 2258 (class 2604 OID 30742)
-- Name: impuestos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY impuestos ALTER COLUMN id SET DEFAULT nextval('impuestos_id_seq'::regclass);


--
-- TOC entry 2235 (class 2604 OID 30652)
-- Name: inventario id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario ALTER COLUMN id SET DEFAULT nextval('inventario_id_seq'::regclass);


--
-- TOC entry 2260 (class 2604 OID 30760)
-- Name: movimientos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimientos ALTER COLUMN id SET DEFAULT nextval('movimientos_id_seq'::regclass);


--
-- TOC entry 2249 (class 2604 OID 30703)
-- Name: municipios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY municipios ALTER COLUMN id SET DEFAULT nextval('municipios_id_seq'::regclass);


--
-- TOC entry 2243 (class 2604 OID 30682)
-- Name: personas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY personas ALTER COLUMN id SET DEFAULT nextval('personas_id_seq'::regclass);


--
-- TOC entry 2238 (class 2604 OID 30659)
-- Name: precios id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precios ALTER COLUMN id SET DEFAULT nextval('precios_id_seq'::regclass);


--
-- TOC entry 2262 (class 2604 OID 30767)
-- Name: proveedores id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedores ALTER COLUMN id SET DEFAULT nextval('proveedores_id_seq'::regclass);


--
-- TOC entry 2264 (class 2604 OID 30776)
-- Name: resumencortecaja id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resumencortecaja ALTER COLUMN id SET DEFAULT nextval('resumencortecaja_id_seq'::regclass);


--
-- TOC entry 2245 (class 2604 OID 30691)
-- Name: telefonos id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telefonos ALTER COLUMN id SET DEFAULT nextval('telefonos_id_seq'::regclass);


--
-- TOC entry 2234 (class 2604 OID 30643)
-- Name: unidades id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unidades ALTER COLUMN id SET DEFAULT nextval('unidades_id_seq'::regclass);


--
-- TOC entry 2268 (class 2604 OID 30798)
-- Name: ventaimpuesto idventa; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventaimpuesto ALTER COLUMN idventa SET DEFAULT nextval('ventaimpuesto_idventa_seq'::regclass);


--
-- TOC entry 2266 (class 2604 OID 30788)
-- Name: ventas id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventas ALTER COLUMN id SET DEFAULT nextval('ventas_id_seq'::regclass);


--
-- TOC entry 2504 (class 0 OID 30604)
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
-- TOC entry 2591 (class 0 OID 0)
-- Dependencies: 187
-- Name: admin_menus_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_menus_id_seq', 28, true);


--
-- TOC entry 2502 (class 0 OID 30598)
-- Dependencies: 186
-- Data for Name: admin_modulos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_modulos VALUES (1, 'Operaciones');
INSERT INTO admin_modulos VALUES (2, 'Consultas');
INSERT INTO admin_modulos VALUES (3, 'Procesos');
INSERT INTO admin_modulos VALUES (4, 'Reportes');
INSERT INTO admin_modulos VALUES (5, 'Sistema');


--
-- TOC entry 2592 (class 0 OID 0)
-- Dependencies: 185
-- Name: admin_modulos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_modulos_id_seq', 5, true);


--
-- TOC entry 2506 (class 0 OID 30610)
-- Dependencies: 190
-- Data for Name: admin_niveles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_niveles VALUES (1, 'Administrador', true);


--
-- TOC entry 2593 (class 0 OID 0)
-- Dependencies: 189
-- Name: admin_niveles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('admin_niveles_id_seq', 1, true);


--
-- TOC entry 2507 (class 0 OID 30615)
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
-- TOC entry 2508 (class 0 OID 30618)
-- Dependencies: 192
-- Data for Name: admin_usuarios; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO admin_usuarios VALUES ('darkps', 'efe237b5fe14b08a47f698cbb80d5aab', true, 1, 1);


--
-- TOC entry 2510 (class 0 OID 30624)
-- Dependencies: 194
-- Data for Name: articulos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO articulos VALUES (4, 'Aspirina Forte de Bayer', true, 'Para la migraña');
INSERT INTO articulos VALUES (2, 'Ibuprofeno', true, 'Para los dolores de cabeza o jaqueca cotidianos');
INSERT INTO articulos VALUES (3, 'Metocarbamol 500mg', false, 'Para los dolores muculares recurrentes');
INSERT INTO articulos VALUES (7, 'Ibrupofeno MK', true, 'Para dolores');
INSERT INTO articulos VALUES (8, 'Metocarbamol 2', true, 'Para los dolores de musculo cotidiano');


--
-- TOC entry 2594 (class 0 OID 0)
-- Dependencies: 193
-- Name: articulos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('articulos_id_seq', 8, true);


--
-- TOC entry 2512 (class 0 OID 30631)
-- Dependencies: 196
-- Data for Name: bodegas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO bodegas VALUES (1, 'Farmacia 1', 'Barrio San Antonio', true);
INSERT INTO bodegas VALUES (2, 'Farmacia 2', 'Barrio El Calvario', true);


--
-- TOC entry 2595 (class 0 OID 0)
-- Dependencies: 195
-- Name: bodegas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('bodegas_id_seq', 2, true);


--
-- TOC entry 2522 (class 0 OID 30667)
-- Dependencies: 206
-- Data for Name: cajas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2596 (class 0 OID 0)
-- Dependencies: 205
-- Name: cajas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cajas_id_seq', 1, false);


--
-- TOC entry 2524 (class 0 OID 30673)
-- Dependencies: 208
-- Data for Name: categorias; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO categorias VALUES (1, 'Agentes inmunosupresores', true);
INSERT INTO categorias VALUES (2, 'Antibióticos', true);
INSERT INTO categorias VALUES (3, 'Ansiolítico', true);
INSERT INTO categorias VALUES (4, 'Fármaco', true);
INSERT INTO categorias VALUES (5, 'Suplemento dietético', true);
INSERT INTO categorias VALUES (6, 'Suplemento vitamínico', true);
INSERT INTO categorias VALUES (7, 'Sustancia placebo', true);


--
-- TOC entry 2597 (class 0 OID 0)
-- Dependencies: 207
-- Name: categorias_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('categorias_id_seq', 1, false);


--
-- TOC entry 2534 (class 0 OID 30706)
-- Dependencies: 218
-- Data for Name: clientes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO clientes VALUES (1, 1);
INSERT INTO clientes VALUES (2, 2);
INSERT INTO clientes VALUES (3, 4);
INSERT INTO clientes VALUES (4, 6);


--
-- TOC entry 2598 (class 0 OID 0)
-- Dependencies: 217
-- Name: clientes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('clientes_id_seq', 4, true);


--
-- TOC entry 2535 (class 0 OID 30710)
-- Dependencies: 219
-- Data for Name: compatibles; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO compatibles VALUES (7, 7, false);


--
-- TOC entry 2537 (class 0 OID 30715)
-- Dependencies: 221
-- Data for Name: compras; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO compras VALUES (1, '123456789', '2017-04-14 08:08:31.494526', 'Nada', 152.50, 1, 1, true);
INSERT INTO compras VALUES (2, '', '2017-04-14 08:09:22.480362', 'Nada', 0.50, 1, 1, true);


--
-- TOC entry 2599 (class 0 OID 0)
-- Dependencies: 220
-- Name: compras_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('compras_id_seq', 2, true);


--
-- TOC entry 2515 (class 0 OID 30644)
-- Dependencies: 199
-- Data for Name: conversiones; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2539 (class 0 OID 30725)
-- Dependencies: 223
-- Data for Name: cortecajas; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2600 (class 0 OID 0)
-- Dependencies: 222
-- Name: cortecajas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('cortecajas_id_seq', 1, false);


--
-- TOC entry 2530 (class 0 OID 30694)
-- Dependencies: 214
-- Data for Name: departamentos; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO departamentos VALUES (1, 'Chalatenango', true);
INSERT INTO departamentos VALUES (2, 'Santa Ana', true);


--
-- TOC entry 2601 (class 0 OID 0)
-- Dependencies: 213
-- Name: departamentos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('departamentos_id_seq', 2, true);


--
-- TOC entry 2540 (class 0 OID 30729)
-- Dependencies: 224
-- Data for Name: detallecompras; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO detallecompras VALUES (1, 4, 50.0000, 1, 0.500000, 25.00);
INSERT INTO detallecompras VALUES (1, 2, 50.0000, 1, 2.550000, 127.50);
INSERT INTO detallecompras VALUES (2, 4, 1.0000, 1, 0.500000, 0.50);


--
-- TOC entry 2541 (class 0 OID 30732)
-- Dependencies: 225
-- Data for Name: detalleventas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO detalleventas VALUES (3, 4, 10.00, 1, 0.50, 4.25, 15.00, NULL);
INSERT INTO detalleventas VALUES (4, 4, 5.00, 1, 1.50, 6.75, 10.00, NULL);
INSERT INTO detalleventas VALUES (5, 4, 10.00, 1, 0.50, 2.50, 50.00, NULL);
INSERT INTO detalleventas VALUES (5, 2, 10.00, 1, 2.55, 22.95, 10.00, NULL);
INSERT INTO detalleventas VALUES (6, 4, 3.00, 1, 0.50, 0.98, 35.00, NULL);
INSERT INTO detalleventas VALUES (6, 2, 4.00, 1, 2.55, 8.87, 13.00, NULL);
INSERT INTO detalleventas VALUES (7, 4, 50.00, 1, 0.50, 25.00, 0.00, NULL);
INSERT INTO detalleventas VALUES (8, 2, 15.00, 1, 2.55, 38.25, 0.00, NULL);
INSERT INTO detalleventas VALUES (10, 4, 10.00, 1, 1.50, 11.25, 25.00, NULL);
INSERT INTO detalleventas VALUES (10, 2, 10.00, 1, 2.55, 25.50, 0.00, NULL);
INSERT INTO detalleventas VALUES (11, 4, 1.00, 1, 0.50, 0.50, 0.00, NULL);


--
-- TOC entry 2544 (class 0 OID 30743)
-- Dependencies: 228
-- Data for Name: detallevimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2554 (class 0 OID 30779)
-- Dependencies: 238
-- Data for Name: empleados; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO empleados VALUES (1, 8, true);


--
-- TOC entry 2602 (class 0 OID 0)
-- Dependencies: 237
-- Name: empleados_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('empleados_id_seq', 1, false);


--
-- TOC entry 2546 (class 0 OID 30748)
-- Dependencies: 230
-- Data for Name: imagenes; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO imagenes VALUES (3, 'C:\Users\dakrpastiursSennin\Documents\NetBeansProjects\Sist_Farmacia\Recursos\Productos\141fe_aspirina_thumb.jpg', 4);
INSERT INTO imagenes VALUES (1, 'C:\Users\dakrpastiursSennin\Documents\NetBeansProjects\Sist_Farmacia\Recursos\Productos\travel_journey-14_icon-icons.com_56002.png', 2);
INSERT INTO imagenes VALUES (6, 'C:\Users\dakrpastiursSennin\Documents\NetBeansProjects\Sist_Farmacia\Recursos\Productos\metocarbamol_750_mg_mk_tabs_-_caja_x_20_-_mckesson.jpg', 3);
INSERT INTO imagenes VALUES (9, 'C:\Users\dakrpastiursSennin\Documents\NetBeansProjects\Sist_Farmacia\Recursos\Productos\7BF.jpg', 7);


--
-- TOC entry 2603 (class 0 OID 0)
-- Dependencies: 229
-- Name: imagenes_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('imagenes_id_seq', 9, true);


--
-- TOC entry 2543 (class 0 OID 30739)
-- Dependencies: 227
-- Data for Name: impuestos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2604 (class 0 OID 0)
-- Dependencies: 226
-- Name: impuestos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('impuestos_id_seq', 1, false);


--
-- TOC entry 2517 (class 0 OID 30649)
-- Dependencies: 201
-- Data for Name: inventario; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO inventario VALUES (2, 2, 2, 1, true, 63.00, 50, 500, '2018-07-31', 2, 'I0001');
INSERT INTO inventario VALUES (3, 3, 7, 1, false, 52.00, 52, 5000, '2018-07-27', 2, 'M0001');
INSERT INTO inventario VALUES (4, 4, 2, 1, true, 24.00, 50, 5000, '2017-04-30', 2, 'A0001');
INSERT INTO inventario VALUES (7, 7, 1, 1, true, 0.00, 10, 1000, '2050-04-15', 1, 'I0002');
INSERT INTO inventario VALUES (8, 8, 2, 1, true, 0.00, 50, 500, '2018-07-13', 1, 'M0002');


--
-- TOC entry 2605 (class 0 OID 0)
-- Dependencies: 200
-- Name: inventario_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('inventario_id_seq', 8, true);


--
-- TOC entry 2548 (class 0 OID 30757)
-- Dependencies: 232
-- Data for Name: movimientos; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2606 (class 0 OID 0)
-- Dependencies: 231
-- Name: movimientos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('movimientos_id_seq', 1, false);


--
-- TOC entry 2532 (class 0 OID 30700)
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
-- TOC entry 2607 (class 0 OID 0)
-- Dependencies: 215
-- Name: municipios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('municipios_id_seq', 6, true);


--
-- TOC entry 2526 (class 0 OID 30679)
-- Dependencies: 210
-- Data for Name: personas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO personas VALUES (1, 'Manuel de Jesus', '12345678-9', '1234-123456-123-1', '1996-04-05', 'Caserio Plan de las Mesas', 'manuel@gmail.com', 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (4, 'Glenda Esmeralda', '21321321-5', '3213-212313-232-1', '1989-10-01', 'Barrio San Antonio', 'glenda_lopez15@gmail.com', 5, true, 'F', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (2, 'Geovany David', '12345678-1', '1234-123456-123-2', '1980-04-10', 'Caserio Plan de las Mesas, Canton Upatoro', 'geovany_david012@gmail.com', 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (8, 'Fredy Pastor', '74125896-3', '1235-789445-369-4', '1992-09-11', 'Canton Upatoro, Caserio Plan de las Mesas', NULL, 1, true, 'M', 'Lopez', 'Cartagena');
INSERT INTO personas VALUES (6, 'Fatima Ernestina', '68764564-6', '5456-132165-498-4', '1996-07-20', 'Barrio El Calvario', NULL, 1, false, 'F', 'Lopez', 'Cartagena');


--
-- TOC entry 2608 (class 0 OID 0)
-- Dependencies: 209
-- Name: personas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('personas_id_seq', 8, true);


--
-- TOC entry 2520 (class 0 OID 30662)
-- Dependencies: 204
-- Data for Name: precioimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2519 (class 0 OID 30656)
-- Dependencies: 203
-- Data for Name: precios; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO precios VALUES (31, 0.25, 7, true, NULL);
INSERT INTO precios VALUES (32, 0.50, 7, true, NULL);
INSERT INTO precios VALUES (33, 0.75, 7, true, NULL);
INSERT INTO precios VALUES (34, 1.50, 7, true, NULL);
INSERT INTO precios VALUES (35, 1.25, 7, true, NULL);
INSERT INTO precios VALUES (36, 1.00, 7, true, NULL);
INSERT INTO precios VALUES (37, 2.50, 8, true, NULL);
INSERT INTO precios VALUES (38, 3.50, 8, true, NULL);
INSERT INTO precios VALUES (39, 4.50, 8, true, NULL);
INSERT INTO precios VALUES (40, 5.50, 8, true, NULL);
INSERT INTO precios VALUES (41, 6.50, 8, true, NULL);
INSERT INTO precios VALUES (42, 7.50, 8, true, NULL);
INSERT INTO precios VALUES (1, 2.55, 2, true, 'Venta 1');
INSERT INTO precios VALUES (2, 3.55, 2, true, 'Venta 2');
INSERT INTO precios VALUES (3, 4.55, 2, true, 'Venta 3');
INSERT INTO precios VALUES (4, 5.55, 2, true, 'Mayoreo 1');
INSERT INTO precios VALUES (5, 6.55, 2, true, 'Mayoreo 2');
INSERT INTO precios VALUES (6, 7.55, 2, true, 'Mayoreo 3');
INSERT INTO precios VALUES (13, 0.50, 4, true, 'Venta 1');
INSERT INTO precios VALUES (7, 2.52, 3, false, 'Venta 1');
INSERT INTO precios VALUES (14, 1.50, 4, true, 'Venta 2');
INSERT INTO precios VALUES (8, 3.52, 3, false, 'Venta 2');
INSERT INTO precios VALUES (15, 2.50, 4, true, 'Venta 3');
INSERT INTO precios VALUES (9, 4.52, 3, false, 'Venta 3');
INSERT INTO precios VALUES (16, 3.50, 4, true, 'Mayoreo 1');
INSERT INTO precios VALUES (10, 5.52, 3, false, 'Mayoreo 1');
INSERT INTO precios VALUES (17, 4.50, 4, true, 'Mayoreo 2');
INSERT INTO precios VALUES (11, 6.52, 3, false, 'Mayoreo 2');
INSERT INTO precios VALUES (18, 5.50, 4, true, 'Mayoreo 3');
INSERT INTO precios VALUES (12, 7.52, 3, false, 'Mayoreo 3');


--
-- TOC entry 2609 (class 0 OID 0)
-- Dependencies: 202
-- Name: precios_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('precios_id_seq', 42, true);


--
-- TOC entry 2550 (class 0 OID 30764)
-- Dependencies: 234
-- Data for Name: proveedores; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO proveedores VALUES (1, 'Bayer', 'Juan Pere', 'Por ahi', '11', '1234-6789', '7235-7896', 'bayer@organizacion.com', 'www.bayer.com', true, '1234-123456-123-1');


--
-- TOC entry 2610 (class 0 OID 0)
-- Dependencies: 233
-- Name: proveedores_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('proveedores_id_seq', 2, true);


--
-- TOC entry 2552 (class 0 OID 30773)
-- Dependencies: 236
-- Data for Name: resumencortecaja; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2611 (class 0 OID 0)
-- Dependencies: 235
-- Name: resumencortecaja_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('resumencortecaja_id_seq', 1, false);


--
-- TOC entry 2528 (class 0 OID 30688)
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
-- TOC entry 2612 (class 0 OID 0)
-- Dependencies: 211
-- Name: telefonos_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('telefonos_id_seq', 8, true);


--
-- TOC entry 2514 (class 0 OID 30640)
-- Dependencies: 198
-- Data for Name: unidades; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO unidades VALUES (1, 'Caja', 'Caj');


--
-- TOC entry 2613 (class 0 OID 0)
-- Dependencies: 197
-- Name: unidades_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('unidades_id_seq', 1, true);


--
-- TOC entry 2558 (class 0 OID 30795)
-- Dependencies: 242
-- Data for Name: ventaimpuesto; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 2614 (class 0 OID 0)
-- Dependencies: 241
-- Name: ventaimpuesto_idventa_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ventaimpuesto_idventa_seq', 1, false);


--
-- TOC entry 2556 (class 0 OID 30785)
-- Dependencies: 240
-- Data for Name: ventas; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO ventas VALUES (4, '2017-04-08 09:19:09.2735', NULL, 6.75, 3.25, 'No hay ninguno', 1, 3, true, 'SEIS 75/100 DOLARES', NULL, NULL);
INSERT INTO ventas VALUES (5, '2017-04-08 09:52:02.498792', NULL, 25.45, 4.55, '', 1, 1, true, 'VEINTE Y CINCO 45/100 DOLARES', NULL, NULL);
INSERT INTO ventas VALUES (6, '2017-04-08 10:16:32.833319', NULL, 9.85, 0.15, '', 1, 1, true, 'NUEVE 85/100 DOLARES', NULL, NULL);
INSERT INTO ventas VALUES (7, '2017-04-17 15:14:51.461071', 25.00, 28.25, 1.75, '', 1, 2, true, 'VEINTE Y OCHO 25/100 DOLARES', 3.25, NULL);
INSERT INTO ventas VALUES (8, '2017-04-17 15:30:02.651933', 38.25, 38.25, 1.75, '', 1, 1, true, 'TREINTA Y OCHO 25/100 DOLARES', NULL, '1');
INSERT INTO ventas VALUES (10, '2017-04-17 16:10:15.038686', 36.75, 41.53, 3.47, '', 1, 3, true, 'CUARENTA Y UN 53/100 DOLARES', 4.78, '2');
INSERT INTO ventas VALUES (11, '2017-04-17 16:11:39.024597', 0.50, 0.57, 0.00, '', 1, 3, true, 'CERO 57/100 DOLARES', 0.07, '3');
INSERT INTO ventas VALUES (3, '2017-04-08 09:13:19.503009', NULL, 4.25, 0.75, ' ', 1, 2, true, 'CUATRO 25/100 DOLARES', NULL, NULL);


--
-- TOC entry 2615 (class 0 OID 0)
-- Dependencies: 239
-- Name: ventas_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('ventas_id_seq', 11, true);


--
-- TOC entry 2272 (class 2606 OID 30802)
-- Name: admin_menus admin_menus_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_menus
    ADD CONSTRAINT admin_menus_pkey PRIMARY KEY (id);


--
-- TOC entry 2270 (class 2606 OID 30800)
-- Name: admin_modulos admin_modulos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_modulos
    ADD CONSTRAINT admin_modulos_pkey PRIMARY KEY (id);


--
-- TOC entry 2274 (class 2606 OID 30804)
-- Name: admin_niveles admin_niveles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_niveles
    ADD CONSTRAINT admin_niveles_pkey PRIMARY KEY (id);


--
-- TOC entry 2276 (class 2606 OID 30806)
-- Name: admin_permisos admin_permisos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_permisos
    ADD CONSTRAINT admin_permisos_pkey PRIMARY KEY (idnivel, idmenu);


--
-- TOC entry 2278 (class 2606 OID 30866)
-- Name: admin_usuarios admin_usuarios_idempleado_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_usuarios
    ADD CONSTRAINT admin_usuarios_idempleado_key UNIQUE (idempleado);


--
-- TOC entry 2280 (class 2606 OID 30808)
-- Name: admin_usuarios admin_usuarios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_usuarios
    ADD CONSTRAINT admin_usuarios_pkey PRIMARY KEY (nickname);


--
-- TOC entry 2282 (class 2606 OID 30810)
-- Name: articulos articulos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY articulos
    ADD CONSTRAINT articulos_pkey PRIMARY KEY (id);


--
-- TOC entry 2284 (class 2606 OID 30812)
-- Name: bodegas bodegas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY bodegas
    ADD CONSTRAINT bodegas_pkey PRIMARY KEY (id);


--
-- TOC entry 2296 (class 2606 OID 30824)
-- Name: cajas cajas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cajas
    ADD CONSTRAINT cajas_pkey PRIMARY KEY (id);


--
-- TOC entry 2298 (class 2606 OID 30826)
-- Name: categorias categorias_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY categorias
    ADD CONSTRAINT categorias_pkey PRIMARY KEY (id);


--
-- TOC entry 2308 (class 2606 OID 30868)
-- Name: clientes clientes_idpersona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY clientes
    ADD CONSTRAINT clientes_idpersona_key UNIQUE (idpersona);


--
-- TOC entry 2310 (class 2606 OID 30836)
-- Name: clientes clientes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY clientes
    ADD CONSTRAINT clientes_pkey PRIMARY KEY (id);


--
-- TOC entry 2312 (class 2606 OID 30838)
-- Name: compatibles compatibles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compatibles
    ADD CONSTRAINT compatibles_pkey PRIMARY KEY (idinventario, idcompatible);


--
-- TOC entry 2314 (class 2606 OID 30840)
-- Name: compras compras_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compras
    ADD CONSTRAINT compras_pkey PRIMARY KEY (id);


--
-- TOC entry 2288 (class 2606 OID 30816)
-- Name: conversiones conversiones_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversiones
    ADD CONSTRAINT conversiones_pkey PRIMARY KEY (idunidadp, idunidads);


--
-- TOC entry 2316 (class 2606 OID 30842)
-- Name: cortecajas cortecajas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cortecajas
    ADD CONSTRAINT cortecajas_pkey PRIMARY KEY (id);


--
-- TOC entry 2304 (class 2606 OID 30832)
-- Name: departamentos departamentos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY departamentos
    ADD CONSTRAINT departamentos_pkey PRIMARY KEY (id);


--
-- TOC entry 2318 (class 2606 OID 30844)
-- Name: detallecompras detallecompras_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallecompras
    ADD CONSTRAINT detallecompras_pkey PRIMARY KEY (idcompra, idinventario);


--
-- TOC entry 2320 (class 2606 OID 30846)
-- Name: detalleventas detalleventas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalleventas
    ADD CONSTRAINT detalleventas_pkey PRIMARY KEY (idventa, idinventario);


--
-- TOC entry 2324 (class 2606 OID 30850)
-- Name: detallevimpuesto detallevimpuesto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallevimpuesto
    ADD CONSTRAINT detallevimpuesto_pkey PRIMARY KEY (idventa, idimpuesto);


--
-- TOC entry 2334 (class 2606 OID 30870)
-- Name: empleados empleados_idpersona_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY empleados
    ADD CONSTRAINT empleados_idpersona_key UNIQUE (idpersona);


--
-- TOC entry 2336 (class 2606 OID 30860)
-- Name: empleados empleados_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY empleados
    ADD CONSTRAINT empleados_pkey PRIMARY KEY (id);


--
-- TOC entry 2326 (class 2606 OID 30852)
-- Name: imagenes imagenes_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagenes
    ADD CONSTRAINT imagenes_pkey PRIMARY KEY (id);


--
-- TOC entry 2322 (class 2606 OID 30848)
-- Name: impuestos impuestos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY impuestos
    ADD CONSTRAINT impuestos_pkey PRIMARY KEY (id);


--
-- TOC entry 2290 (class 2606 OID 30818)
-- Name: inventario inventario_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario
    ADD CONSTRAINT inventario_pkey PRIMARY KEY (id);


--
-- TOC entry 2328 (class 2606 OID 30854)
-- Name: movimientos movimientos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimientos
    ADD CONSTRAINT movimientos_pkey PRIMARY KEY (id);


--
-- TOC entry 2306 (class 2606 OID 30834)
-- Name: municipios municipios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY municipios
    ADD CONSTRAINT municipios_pkey PRIMARY KEY (id);


--
-- TOC entry 2300 (class 2606 OID 30828)
-- Name: personas personas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY personas
    ADD CONSTRAINT personas_pkey PRIMARY KEY (id);


--
-- TOC entry 2294 (class 2606 OID 30822)
-- Name: precioimpuesto precioimpuesto_idprecio_idimpuesto_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precioimpuesto
    ADD CONSTRAINT precioimpuesto_idprecio_idimpuesto_key UNIQUE (idprecio, idimpuesto);


--
-- TOC entry 2292 (class 2606 OID 30820)
-- Name: precios precios_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precios
    ADD CONSTRAINT precios_pkey PRIMARY KEY (id);


--
-- TOC entry 2330 (class 2606 OID 30856)
-- Name: proveedores proveedores_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY proveedores
    ADD CONSTRAINT proveedores_pkey PRIMARY KEY (id);


--
-- TOC entry 2332 (class 2606 OID 30858)
-- Name: resumencortecaja resumencortecaja_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resumencortecaja
    ADD CONSTRAINT resumencortecaja_pkey PRIMARY KEY (id);


--
-- TOC entry 2302 (class 2606 OID 30830)
-- Name: telefonos telefonos_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telefonos
    ADD CONSTRAINT telefonos_pkey PRIMARY KEY (id);


--
-- TOC entry 2286 (class 2606 OID 30814)
-- Name: unidades unidades_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY unidades
    ADD CONSTRAINT unidades_pkey PRIMARY KEY (id);


--
-- TOC entry 2340 (class 2606 OID 30864)
-- Name: ventaimpuesto ventaimpuesto_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventaimpuesto
    ADD CONSTRAINT ventaimpuesto_pkey PRIMARY KEY (idventa, idimpuesto);


--
-- TOC entry 2338 (class 2606 OID 30862)
-- Name: ventas ventas_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventas
    ADD CONSTRAINT ventas_pkey PRIMARY KEY (id);


--
-- TOC entry 2342 (class 2606 OID 31185)
-- Name: admin_menus admin_menus_idmenu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_menus
    ADD CONSTRAINT admin_menus_idmenu_fkey FOREIGN KEY (idmenu) REFERENCES admin_menus(id);


--
-- TOC entry 2341 (class 2606 OID 31180)
-- Name: admin_menus admin_menus_idmodulo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_menus
    ADD CONSTRAINT admin_menus_idmodulo_fkey FOREIGN KEY (idmodulo) REFERENCES admin_modulos(id);


--
-- TOC entry 2344 (class 2606 OID 30876)
-- Name: admin_permisos admin_permisos_idmenu_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_permisos
    ADD CONSTRAINT admin_permisos_idmenu_fkey FOREIGN KEY (idmenu) REFERENCES admin_menus(id);


--
-- TOC entry 2343 (class 2606 OID 30871)
-- Name: admin_permisos admin_permisos_idnivel_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_permisos
    ADD CONSTRAINT admin_permisos_idnivel_fkey FOREIGN KEY (idnivel) REFERENCES admin_niveles(id);


--
-- TOC entry 2346 (class 2606 OID 30886)
-- Name: admin_usuarios admin_usuarios_idempleado_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_usuarios
    ADD CONSTRAINT admin_usuarios_idempleado_fkey FOREIGN KEY (idempleado) REFERENCES empleados(id);


--
-- TOC entry 2345 (class 2606 OID 30881)
-- Name: admin_usuarios admin_usuarios_idnivel_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY admin_usuarios
    ADD CONSTRAINT admin_usuarios_idnivel_fkey FOREIGN KEY (idnivel) REFERENCES admin_niveles(id);


--
-- TOC entry 2359 (class 2606 OID 30946)
-- Name: clientes clientes_idpersona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY clientes
    ADD CONSTRAINT clientes_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES personas(id);


--
-- TOC entry 2361 (class 2606 OID 30956)
-- Name: compatibles compatibles_idcompatible_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compatibles
    ADD CONSTRAINT compatibles_idcompatible_fkey FOREIGN KEY (idcompatible) REFERENCES inventario(id);


--
-- TOC entry 2360 (class 2606 OID 30951)
-- Name: compatibles compatibles_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compatibles
    ADD CONSTRAINT compatibles_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2363 (class 2606 OID 30971)
-- Name: compras compras_idempleado_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compras
    ADD CONSTRAINT compras_idempleado_fkey FOREIGN KEY (idempleado) REFERENCES empleados(id);


--
-- TOC entry 2362 (class 2606 OID 30966)
-- Name: compras compras_idproveedor_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY compras
    ADD CONSTRAINT compras_idproveedor_fkey FOREIGN KEY (idproveedor) REFERENCES proveedores(id);


--
-- TOC entry 2347 (class 2606 OID 30891)
-- Name: conversiones conversiones_idunidadp_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversiones
    ADD CONSTRAINT conversiones_idunidadp_fkey FOREIGN KEY (idunidadp) REFERENCES unidades(id);


--
-- TOC entry 2348 (class 2606 OID 30896)
-- Name: conversiones conversiones_idunidads_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY conversiones
    ADD CONSTRAINT conversiones_idunidads_fkey FOREIGN KEY (idunidads) REFERENCES unidades(id);


--
-- TOC entry 2364 (class 2606 OID 30976)
-- Name: cortecajas cortecajas_idcaja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY cortecajas
    ADD CONSTRAINT cortecajas_idcaja_fkey FOREIGN KEY (idcaja) REFERENCES cajas(id);


--
-- TOC entry 2365 (class 2606 OID 30981)
-- Name: detallecompras detallecompras_idcompra_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallecompras
    ADD CONSTRAINT detallecompras_idcompra_fkey FOREIGN KEY (idcompra) REFERENCES compras(id);


--
-- TOC entry 2366 (class 2606 OID 30986)
-- Name: detallecompras detallecompras_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallecompras
    ADD CONSTRAINT detallecompras_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2367 (class 2606 OID 30991)
-- Name: detallecompras detallecompras_idunidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallecompras
    ADD CONSTRAINT detallecompras_idunidad_fkey FOREIGN KEY (idunidad) REFERENCES unidades(id);


--
-- TOC entry 2369 (class 2606 OID 31001)
-- Name: detalleventas detalleventas_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalleventas
    ADD CONSTRAINT detalleventas_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2370 (class 2606 OID 31006)
-- Name: detalleventas detalleventas_idunidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalleventas
    ADD CONSTRAINT detalleventas_idunidad_fkey FOREIGN KEY (idunidad) REFERENCES unidades(id);


--
-- TOC entry 2368 (class 2606 OID 30996)
-- Name: detalleventas detalleventas_idventa_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detalleventas
    ADD CONSTRAINT detalleventas_idventa_fkey FOREIGN KEY (idventa) REFERENCES ventas(id);


--
-- TOC entry 2373 (class 2606 OID 31021)
-- Name: detallevimpuesto detallevimpuesto_idimpuesto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallevimpuesto
    ADD CONSTRAINT detallevimpuesto_idimpuesto_fkey FOREIGN KEY (idimpuesto) REFERENCES impuestos(id);


--
-- TOC entry 2372 (class 2606 OID 31016)
-- Name: detallevimpuesto detallevimpuesto_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallevimpuesto
    ADD CONSTRAINT detallevimpuesto_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2371 (class 2606 OID 31011)
-- Name: detallevimpuesto detallevimpuesto_idventa_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY detallevimpuesto
    ADD CONSTRAINT detallevimpuesto_idventa_fkey FOREIGN KEY (idventa) REFERENCES ventas(id);


--
-- TOC entry 2379 (class 2606 OID 31051)
-- Name: empleados empleados_idpersona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY empleados
    ADD CONSTRAINT empleados_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES personas(id);


--
-- TOC entry 2374 (class 2606 OID 31026)
-- Name: imagenes imagenes_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY imagenes
    ADD CONSTRAINT imagenes_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2349 (class 2606 OID 30901)
-- Name: inventario inventario_idarticulo_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario
    ADD CONSTRAINT inventario_idarticulo_fkey FOREIGN KEY (idarticulo) REFERENCES articulos(id);


--
-- TOC entry 2352 (class 2606 OID 33020)
-- Name: inventario inventario_idbodega_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario
    ADD CONSTRAINT inventario_idbodega_fkey FOREIGN KEY (idbodega) REFERENCES bodegas(id);


--
-- TOC entry 2350 (class 2606 OID 30906)
-- Name: inventario inventario_idcategoria_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario
    ADD CONSTRAINT inventario_idcategoria_fkey FOREIGN KEY (idcategoria) REFERENCES categorias(id);


--
-- TOC entry 2351 (class 2606 OID 30911)
-- Name: inventario inventario_idunidad_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY inventario
    ADD CONSTRAINT inventario_idunidad_fkey FOREIGN KEY (idunidad) REFERENCES unidades(id);


--
-- TOC entry 2376 (class 2606 OID 31036)
-- Name: movimientos movimientos_idcaja_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimientos
    ADD CONSTRAINT movimientos_idcaja_fkey FOREIGN KEY (idcaja) REFERENCES cajas(id);


--
-- TOC entry 2377 (class 2606 OID 31041)
-- Name: movimientos movimientos_idcorte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimientos
    ADD CONSTRAINT movimientos_idcorte_fkey FOREIGN KEY (idcorte) REFERENCES cortecajas(id);


--
-- TOC entry 2375 (class 2606 OID 31031)
-- Name: movimientos movimientos_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY movimientos
    ADD CONSTRAINT movimientos_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2358 (class 2606 OID 30941)
-- Name: municipios municipios_iddepartamento_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY municipios
    ADD CONSTRAINT municipios_iddepartamento_fkey FOREIGN KEY (iddepartamento) REFERENCES departamentos(id);


--
-- TOC entry 2356 (class 2606 OID 30931)
-- Name: personas personas_idmunicipio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY personas
    ADD CONSTRAINT personas_idmunicipio_fkey FOREIGN KEY (idmunicipio) REFERENCES municipios(id);


--
-- TOC entry 2355 (class 2606 OID 30926)
-- Name: precioimpuesto precioimpuesto_idimpuesto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precioimpuesto
    ADD CONSTRAINT precioimpuesto_idimpuesto_fkey FOREIGN KEY (idimpuesto) REFERENCES impuestos(id);


--
-- TOC entry 2354 (class 2606 OID 30921)
-- Name: precioimpuesto precioimpuesto_idprecio_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precioimpuesto
    ADD CONSTRAINT precioimpuesto_idprecio_fkey FOREIGN KEY (idprecio) REFERENCES precios(id);


--
-- TOC entry 2353 (class 2606 OID 30916)
-- Name: precios precios_idinventario_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY precios
    ADD CONSTRAINT precios_idinventario_fkey FOREIGN KEY (idinventario) REFERENCES inventario(id);


--
-- TOC entry 2378 (class 2606 OID 31046)
-- Name: resumencortecaja resumencortecaja_idcorte_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY resumencortecaja
    ADD CONSTRAINT resumencortecaja_idcorte_fkey FOREIGN KEY (idcorte) REFERENCES cortecajas(id);


--
-- TOC entry 2357 (class 2606 OID 30936)
-- Name: telefonos telefonos_idpersona_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY telefonos
    ADD CONSTRAINT telefonos_idpersona_fkey FOREIGN KEY (idpersona) REFERENCES personas(id);


--
-- TOC entry 2383 (class 2606 OID 31076)
-- Name: ventaimpuesto ventaimpuesto_idimpuesto_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventaimpuesto
    ADD CONSTRAINT ventaimpuesto_idimpuesto_fkey FOREIGN KEY (idimpuesto) REFERENCES impuestos(id);


--
-- TOC entry 2382 (class 2606 OID 31071)
-- Name: ventaimpuesto ventaimpuesto_idventa_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventaimpuesto
    ADD CONSTRAINT ventaimpuesto_idventa_fkey FOREIGN KEY (idventa) REFERENCES ventas(id);


--
-- TOC entry 2380 (class 2606 OID 31061)
-- Name: ventas ventas_idcliente_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventas
    ADD CONSTRAINT ventas_idcliente_fkey FOREIGN KEY (idcliente) REFERENCES clientes(id);


--
-- TOC entry 2381 (class 2606 OID 31066)
-- Name: ventas ventas_idempleado_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY ventas
    ADD CONSTRAINT ventas_idempleado_fkey FOREIGN KEY (idempleado) REFERENCES empleados(id);


-- Completed on 2017-04-18 16:49:44

--
-- PostgreSQL database dump complete
--