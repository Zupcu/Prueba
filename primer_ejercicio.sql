#CREACIÓN DE BASE DE DATOS ClientesDB
#-----------------------------------------
CREATE DATABASE ClientesDB;

#USAR LA BASE DE DATOS CREADA ANTERIORMENTE
#------------------------------------------
USE ClientesDB;

#CREAR LA TABLA Clientes
#-------------------------
CREATE TABLE Clientes (
	ID INT AUTO_INCREMENT PRIMARY KEY, 
    nombre VARCHAR(255) NOT NULL, 
    apellido VARCHAR(255) NOT NULL, 
    email VARCHAR(255) UNIQUE NOT NULL, 
    telefono VARCHAR(20)
);

#CREAR LA TABLA Pedido
#------------------------
CREATE TABLE Pedido ( 
	ID INT AUTO_INCREMENT PRIMARY KEY,
	cliente_id INT NOT NULL,
    fecha_pedido DATE NOT NULL, 
    monto DECIMAL(10, 2) NOT NULL, 
    FOREIGN KEY (cliente_id) REFERENCES Clientes(ID)
);


#CREACIÓN DE ÍNDICES
#-------------------
#CREAR ÍNDICE POR NOMBRE DE CLIENTE
#-------------------
CREATE INDEX index_nombre ON Clientes (nombre);

#CREAR ÍNDICE ID_Cliente EN Pedido
#-----------------------------
CREATE INDEX index_pedido_cliente_id ON Pedido (cliente_id);


#CONTROL DE INTEGRIDAD REFERENCIAL
#---------------------------------
#GARANTIZAR QUE Clientes_id EN Pedido ES UN ID VÁLIDO EN Clientes
#--------------------------------------------------------------------
ALTER TABLE Pedido ADD CONSTRAINT fk_pedido_cliente FOREIGN KEY (cliente_id) REFERENCES Clientes(ID) ON DELETE CASCADE;


#POBLAR LA BASE DE DATOS
#-----------------------
#INSERTAR DATOS EN LA TABLA Clientes
#------------------------------------
INSERT INTO Clientes (nombre, apellido, email, telefono)
VALUES ('Amaia', 'Uriarte', 'auriarte@gmail.com' , '+569616322xx'),
('Iker', 'Uriarte', 'chilenito@gmail.com', '+5692250437xx'),
('Vicente', 'Medina', 'vmedinau@gmail.com', '+73756xxx56'),
('Iñigo', 'Uriarte', 'iuriartec@gmail.com', '+82586xxx59');

#INSERTAR DATOS EN LA TABLA Pedido
#------------------------------------
INSERT INTO Pedido (cliente_id, fecha_pedido, monto) 
VALUES (1, '2024-11-27', 150.00), 
(2, '2024-11-28', 200.00),
(3, '2024-11-25', 250.00),
(4, '2024-10-05', 320.00);
