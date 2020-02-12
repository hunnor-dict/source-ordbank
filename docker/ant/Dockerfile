FROM openjdk:13-jdk

RUN cd /opt && \
    curl -s -S -o apache-ant-1.10.7-bin.tar.gz http://apache.uib.no/ant/binaries/apache-ant-1.10.7-bin.tar.gz && \
    tar -xzf apache-ant-1.10.7-bin.tar.gz && \
    rm apache-ant-1.10.7-bin.tar.gz

ENV PATH /opt/apache-ant-1.10.7/bin:${PATH}

WORKDIR /ant

ENTRYPOINT ["ant"]
