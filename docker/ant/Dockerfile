FROM openjdk:11-jdk

RUN cd /opt && \
    wget -q http://apache.uib.no/ant/binaries/apache-ant-1.10.6-bin.tar.gz && \
    tar -xzf apache-ant-1.10.6-bin.tar.gz && \
    rm apache-ant-1.10.6-bin.tar.gz

ENV PATH /opt/apache-ant-1.10.6/bin:${PATH}

WORKDIR /ant

ENTRYPOINT ["ant"]
