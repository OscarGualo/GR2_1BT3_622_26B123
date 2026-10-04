USE PoliDinner;
GO
INSERT INTO PersonalComedor (nombres, apellidos, rol, cedula, clave)
VALUES ('Ana', 'Pérez', 'Administrador', '1700000001', '1234');
DECLARE @personal INT = SCOPE_IDENTITY();

INSERT INTO Menu (fecha, estado, personal_id)
VALUES (CAST(GETDATE() AS DATE), 'Publicado', @personal);
DECLARE @menu INT = SCOPE_IDENTITY();

INSERT INTO ItemMenu (nombre, precio, estado, descripcion, version, menu_id)
VALUES ('Seco de Pollo', 3.00, 'Disponible', 'Pollo guisado con arroz amarillo', 0, @menu);
INSERT INTO Plato (id) VALUES (SCOPE_IDENTITY());

INSERT INTO ItemMenu (nombre, precio, estado, descripcion, version, menu_id)
VALUES ('Agua sin gas 500 ml', 0.60, 'Disponible', 'Botella personal', 0, @menu);
INSERT INTO Producto (id, stock, fechaCaducidad) VALUES (SCOPE_IDENTITY(), 20, '2027-06-30');
GO