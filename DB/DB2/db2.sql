CREATE TABLE Groups
(
	GID int NOT NULL AUTO_INCREMENT PRIMARY KEY,
	GName varchar(30)
);

CREATE TABLE Person
(
	Username	varchar(20)	NOT NULL,
	PName	varchar(30),
	Email	varchar(320),
	Password	varchar(30),
	LastLoggedIn	DATETIME,
	PRIMARY KEY	(Username)
);

CREATE TABLE Appointment
(
	AID	int	NOT NULL AUTO_INCREMENT,
	AName varchar(30),
	Description varchar(200),
	Start	DATETIME,
	End	DATETIME,
	Priority varchar(1),
	DateCreated	DATETIME,
	DateChanged	DATETIME,
	PRIMARY KEY (AID)
);

CREATE TABLE Room
(
	RID int NOT NULL AUTO_INCREMENT,
	RName varchar(30),
	Capacity varchar(4),
	Primary key (RID)
);

CREATE TABLE MemberOf
(
	moID INT NOT NULL,
	GID	int NOT NULL,
	Username varchar(20),
	PRIMARY KEY(moID),
	FOREIGN KEY (GID) REFERENCES Groups(GID),
	FOREIGN KEY (Username) REFERENCES Person(Username)
);

CREATE TABLE InvitedTo
(
	itID INT NOT NULL AUTO_INCREMENT,
	Username varchar(20) NOT NULL,
	AID	int	NOT NULL,
	Attends	BOOLEAN DEFAULT NULL,
	hasAlarm BOOLEAN,
	AlarmTime DATETIME,
	PRIMARY KEY (itID),
	FOREIGN KEY (AID) REFERENCES Appointment(AID),
	FOREIGN KEY (Username) REFERENCES Person(Username)
);

CREATE TABLE IsLeader
(
	ilID INT NOT NULL AUTO_INCREMENT,
	Username	varchar(20)	NOT NULL,
	AID	int	NOT NULL,
	PRIMARY KEY (ilID),
	FOREIGN KEY (AID) REFERENCES Appointment(AID),
	FOREIGN KEY (Username) REFERENCES Person(Username)
);

CREATE TABLE TakesPlace
(
	tpID INT NOT NULL AUTO_INCREMENT,
	AID	int	NOT NULL,
	RID int NOT NULL,
	PRIMARY KEY (tpID),
	FOREIGN KEY (AID) REFERENCES Appointment(AID),
	FOREIGN KEY (RID) REFERENCES Room(RID)
);