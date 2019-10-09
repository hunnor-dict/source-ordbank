# Norsk ordbank: A user's journey

This is the first article in a series about Norsk ordbank.

Norsk ordbank is a lexical database for Norwegian (bokmål and nynorsk), and reflects the current, official standard orthography. The database is available for download from [Språkbanken](https://www.nb.no/sprakbanken/show?serial=oai%3Anb.no%3Asbr-5&lang=nb).

In simple terms, Norsk ordbank contains Norwegian words, along with information about their inflection and composition. It can be a useful tool in many scenarios, including authoring and publishing language related works, such as dictionaries or encyclopedias, or natural language processing applications.

This article shows how to set up a local copy of the database.

> To run the commands in the article, you will need a PC or Mac with Docker installed, and a copy of the repository at `https://github.com/adamzkover/ordbank.git`. Some knowledge of Ant and Docker is useful, but not required.

# TL;DR

The following commands start a local database instance and import data from Norsk ordbank:

```
git clone https://github.com/adamzkover/ordbank.git
cd ordbank/docker
docker-compose up -d
```

After container `docker_ant_1` stops with code 0, and the database is initialized in container `docker_mysql_1`, run the following commands:

```
docker exec docker_mysql_1 sh -c 'mysql ordbank < /sql/create.sql'
docker exec docker_mysql_1 sh -c 'mysql -v -v -v --local-infile ordbank < /sql/import.sql'
docker exec docker_mysql_1 sh -c 'mysql ordbank < /sql/constraints.sql'
```

Connect to the datanase container with any MySQL client, with username `ordbank` and password `ordbank` and you should see the tables of Norsk ordbank.

# 1 How to build the database

Norsk ordbank is stored in a relational database, and is available for download as exports of the database tables. The database structure is documented in a separate PDF file.

We will have to download two files, and at least extract the archive. We will use Apache Ant to automate this process.

## 1.1 Prepare the tools

Running any of the commands should not require installation of additional tools. This means that Ant will have to be run with Docker. First, we create and test a docker image with Ant installed.

Note: if you tried the `docker-compose` command in the TL;DR section, make sure to delete the containers and images that `docker-compose`.

The `docker/ant` directory contains a `Dockerfile` that is based on OpenJDK 11, and installs Ant. The `Dockerfile` also shows that `WORKDIR` is set to `/ant`, and that `ENTRYPOINT` is the Ant executable. With this setup, we can simply run the default target of a build file by mounting its parent directory as a volume to `/ant`.

Run the following command to build the image:

```
cd docker/ant
docker build -t docker_ant .
```

To test the image, start a container with the current directory mounted to `/ant`:

`docker run --rm --volume $(pwd):/ant docker_ant`

The container should print the "Hello Ant!" message and exit.

## 1.2 Download the data

The same Ant build file contains targets for downloading the archive that contains the database export to the `data` directory, and the documentation to the `docs` directory. Run the following commands to download both files:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant download.pdf download.tar`

The next step is to extract the data files, this is done with the `extract` target:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant extract`

The archive contains 9 text files. 8 of these are exports of database tables. `norsk_ordbank_argstr.txt` is part of the documentation, and can for the moment be moved to the `docs` directory. The `sort` target does exactly that:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant sort`

The text files use ISO-8859-1 encoding. We will convert them to UTF-8 before loading them to the database, using the `recode` target:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant recode`

This target creates copies of the text file directly under the data directory. Finally, to clean up the original files and the downloaded archive, we can run the `cleanup` target:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant cleanup`

The steps above can also be run with a single command, using the `ordbank` target:

`docker run --rm --volume $(pwd):/ant --volume $(pwd)/../../data:/data --volume $(pwd)/../../docs:/docs docker_ant ordbank`

## 1.3 Create the database

The `docker/mysql` directory contains the Dockerfile and configuration files for the MySQL server. The configuration files make it possible to load data from text files using the command line.

Run the following commands to build the MySQL server image:

```
cd ../mysql
docker build -t ordbank-mysql .
```

After the images is built, run the MySQL server:

`docker run --detach --name ordbank-mysql --publish 3306:3306 --volume $(pwd)/../../data:/data ordbank-mysql`

Adding data to the database is split to 3 steps. Execute the following commands in sequence to create the database, load data and apply primary and foreign key constraints.

1. Create the tables:

  `docker exec ordbank-mysql sh -c 'mysql ordbank < /sql/create.sql'`

2. Import data:

  `docker exec ordbank-mysql sh -c 'mysql -v -v -v --local-infile ordbank < /sql/import.sql'`

3. Apply constraints:

  `docker exec ordbank-mysql sh -c 'mysql ordbank < /sql/constraints.sql'`

In case you want to start again, you can use `drop.sql` to drop all tables :

`docker exec ordbank-mysql sh -c 'mysql ordbank < /sql/drop.sql'`

# Conclusion

By running the commands in this article, you can quickly have a local copy of Norsk ordbank up and running.

The next article is going to explore the contents of the database, look at what the data is and give some use case examples.
