CREATE DATABASE database;

CREATE TABLE Group
(
	GID	int NOT NULL AUTO_INCREMENT,
	Name varchar(30),
	Primary key (GID)
)

CREATE TABLE MemberOf
(
	GID	int NOT NULL AUTO_INCREMENT,
	Username varchar(20),
	Foreign key ()
)

CREATE TABLE Person
(
	Username varchar(20)	NOT NULL,
	Name varchar(30),
	Password varchar(30),
	LastLoggedIn DATETIME,
	Primary key (Username)
)

CREATE TABLE InvitedTo
(

)

CREATE TABLE IsLeader
(

)

CREATE TABLE Appointment
(
	AID	int NOT NULL AUTO_INCREMENT,
	Name varchar(30),
	Start	DATETIME,
	End	DATETIME,
	Priority varchar(1),
	DateCreated	DATETIME,
	DateChanged	DATETIME,
	Primary key (AID)
)

CREATE TABLE TakesPlace
(

)

CREATE TABLE Room
(
	RID int NOT NULL AUTO_INCREMENT,
	Name varchar(30),
	Capacity varchar(4),
	Primary key (RID)
)



