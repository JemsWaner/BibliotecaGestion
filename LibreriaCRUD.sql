create database Libreria;

use Libreria;

create table Generos(
id int auto_increment primary key,
genero varchar(50)
);

create table Libros(
id int auto_increment primary key,
titulo varchar(100),
autor varchar(100),
descripcion varchar(1000)
);

CREATE TABLE LibrosGeneros (
    id int auto_increment primary key,
    libro_id int,
    genero_id int,
    foreign key(libro_id) references Libros(id),
    foreign key(genero_id) references Generos(id)
);

insert into Generos(genero) 
value("Terror"),("Romance"),("Cuentos de hadas"),("ciencia ficción"),("gótica"),("policíaca"),
("paranormal"),("distópica"),("fantástica"),("gore/violencia");

select * from generos;

insert into Libros (titulo, autor, descripcion) values 
('1984', 'George Orwell', 'Distopía clásica que describe un futuro totalitario'),
('To Kill a Mockingbird', 'Harper Lee', 'Novela sobre la injusticia racial en el sur de Estados Unidos'),
('One Hundred Years of Solitude', 'Gabriel García Márquez', 'Realismo mágico en Macondo'),
('The Great Gatsby', 'F. Scott Fitzgerald', 'Crítica a la decadencia de la sociedad americana en los años 1920');

select * from LibrosGeneros;

insert into LibrosGeneros(libro_id,genero_id)
values
(1, 8),  -- 1984: Distópica
(1, 4),  -- 1984: Ciencia ficción
(2, 6),  -- To Kill a Mockingbird: Policíaca
(2, 9),  -- To Kill a Mockingbird: Fantástica
(3, 9),  -- One Hundred Years of Solitude: Fantástica
(3, 10), -- One Hundred Years of Solitude: Gore/violencia
(4, 3),  -- The Great Gatsby: Cuentos de hadas
(4, 7);  -- The Great Gatsby: Paranormal

DELIMITER //
create procedure tablaCompleta()
begin
	select L.id, L.titulo, L.autor, L.descripcion, G.genero
	from Libros L 
	left join LibrosGeneros LG on L.id= LG.libro_id
	left join Generos G on G.id= LG.genero_id;
end //
DELIMITER ;

DELIMITER //
create procedure agregarLibro(in titulo varchar(100),in autor varchar(100),in descripcion varchar(1000))
begin
	insert into Libros(titulo, autor, descripcion)
    value(titulo,autor,descripcion);
end //
DELIMITER ;

call tablaCompleta();

call agregarLibro('Luna de Pluton','Dross','Se que te va a gustar');
