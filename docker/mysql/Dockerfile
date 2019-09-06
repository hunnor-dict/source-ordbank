FROM mysql

ENV TZ=Europe/Oslo

ENV MYSQL_DATABASE=ordbank \
    MYSQL_PASSWORD=ordbank \
    MYSQL_ROOT_PASSWORD=ordbank \
    MYSQL_USER=ordbank

COPY grant-file.sql /docker-entrypoint-initdb.d

COPY load-data.cnf /etc/mysql/conf.d

COPY sql /sql

COPY my.cnf /root/.my.cnf
RUN chmod 600 /root/.my.cnf
