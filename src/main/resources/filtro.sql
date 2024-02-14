CREATE TABLE impuestos (
    id_impuesto INT AUTO_INCREMENT,
    anho YEAR NOT NULL,
    porcentaje FLOAT NOT NULL,
    PRIMARY KEY (id_impuesto)
);
DROP TABLE descuentos;
CREATE TABLE descuentos (
    id_descuento INT AUTO_INCREMENT,
    tipo ENUM("Porcentaje", "Fijo") NOT NULL,
    condicion VARCHAR(50) NOT NULL,
    porcentaje FLOAT NOT NULL,
    producto INT,
    activo BOOLEAN NOT NULL,
    PRIMARY KEY (id_descuento)
);
INSERT INTO descuentos(tipo, condicion, porcentaje, producto, activo) VALUES 
(1, "Monto minimo de compara: $1000", 10, NULL, 1),
(2, "Compra de al menos 5 unidades del producto 5", 5, 5, 1),
(1, "Cliente Gold con mas de 10 compras", 15, NULL, 1),
(2, "Valido solo los viernes", 3, NULL, 1),
(1, "Compra durante la temporada navideña", 5, NULL, 1);

INSERT INTO impuestos(anho, porcentaje) VALUES 
('2023', 12),
('2024', 19);

INSERT INTO impuestos(anho, porcentaje) VALUES (2022, 10);
ALTER TABLE factura ADD COLUMN descuento DOUBLE NOT NULL;
ALTER TABLE factura ADD COLUMN impuesto DOUBLE NOT NULL;
SELECT c.id, c.nombre, c.apellido, SUM(itf.importe) AS total FROM cliente c INNER JOIN factura ON c.id = factura.cliente_id INNER JOIN item_factura itf ON factura.numeroFactura = itf.factura_numeroFactura GROUP BY c.id ORDER BY total DESC;
SELECT P.nombre, SUM(itf.cantidad) AS total FROM producto p INNER JOIN item_factura itf ON p.codigo = itf.producto_codigo GROUP BY p.codigo ORDER BY total DESC;
SELECT sum(itf.importe) AS ventas, sum(f.descuento) AS descuentos, sum(f.impuesto) AS impuestos, sum(itf.importe) - sum(f.descuento) + sum(f.impuesto) AS total FROM factura f INNER JOIN item_factura itf ON f.numeroFactura = itf.factura_numeroFactura;