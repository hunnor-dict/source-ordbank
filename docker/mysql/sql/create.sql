CREATE TABLE BOYING_GRUPPER (
  BOY_GRUPPE VARCHAR(20) NOT NULL
);

CREATE TABLE BOYING (
  BOY_NUMMER INT(2) NOT NULL,
  BOY_GRUPPE VARCHAR(20) NOT NULL,
  BOY_TEKST VARCHAR(100) NOT NULL,
  ORDBOK_TEKST VARCHAR(200)
);

CREATE TABLE PARADIGME (
  PARADIGME_ID VARCHAR(5) NOT NULL,
  BOY_GRUPPE VARCHAR(20) NOT NULL,
  ORDKLASSE VARCHAR(50) NOT NULL,
  ORDKLASSE_UTDYPING VARCHAR(100) DEFAULT NULL,
  FORKLARING VARCHAR(100) DEFAULT NULL,
  DOEME VARCHAR(100) DEFAULT NULL,
  ID INT(5) NOT NULL
);

CREATE TABLE PARADIGME_BOYING (
  PARADIGME_ID VARCHAR(5) NOT NULL,
  BOY_NUMMER INT(2) NOT NULL,
  BOY_GRUPPE VARCHAR(20) NOT NULL,
  BOY_UTTRYKK VARCHAR(50) DEFAULT NULL
);

CREATE TABLE LEMMA (
  LEMMA_ID INT(8) NOT NULL,
  GRUNNFORM VARCHAR(100) NOT NULL
);

CREATE TABLE LEMMA_PARADIGME (
  LEMMA_ID INT(8) NOT NULL,
  PARADIGME_ID VARCHAR(5) NOT NULL,
  NORMERING VARCHAR(20) DEFAULT 'normert' NOT NULL,
  FRADATO VARCHAR(10) NOT NULL,
  TILDATO VARCHAR(10) NOT NULL,
  KOMMENTAR VARCHAR(1000)
);

CREATE TABLE FULLFORMSLISTE (
  LEMMA_ID INT(8) NOT NULL,
  OPPSLAG VARCHAR(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  TAG VARCHAR(625) NOT NULL,
  PARADIGME_ID VARCHAR(5) NOT NULL,
  BOY_NUMMER INT(2) NOT NULL,
  FRADATO VARCHAR(10) NOT NULL,
  TILDATO VARCHAR(10) NOT NULL,
  NORMERING VARCHAR(100) NOT NULL
);

CREATE TABLE LEDDANALYSE (
  LEDDANALYSE_ID INT(8) NOT NULL,
  OPPSLAG VARCHAR(100) NOT NULL,
  LEDDANALYSE VARCHAR(250) NOT NULL,
  FORLEDD VARCHAR(100) NOT NULL,
  FORLEDD_GRAM VARCHAR(50) NOT NULL,
  FUGE VARCHAR(5) NOT NULL,
  ETTERLEDD VARCHAR(100) NOT NULL,
  ETTERLEDD_GRAM VARCHAR(50) NOT NULL,
  LEDDMARKERT_BOB VARCHAR(10) NOT NULL,
  NEG_FUGE VARCHAR(2) NOT NULL,
  OPPSLAG_LEDD_MARKERT VARCHAR(100) NOT NULL,
  BINDESTREKSFORM_FOR_LEMMA VARCHAR(100) NOT NULL,
  LEMMA_ID INT(8) NOT NULL
);
