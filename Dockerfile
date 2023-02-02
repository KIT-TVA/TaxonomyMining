FROM ubuntu:20.04

RUN apt-get update \
    && DEBIAN_FRONTEND=noninteractive TZ=Etc/UTC apt install -y openjfx curl python3 python3-pip wget graphviz openjdk-17-jdk\
    && wget https://github.com/srcML/srcMLReleases/raw/main/srcml_1.0.0-1_ubuntu20.04.deb \
    && apt install -y ./srcml_1.0.0-1_ubuntu20.04.deb \
    && rm srcml_1.0.0-1_ubuntu20.04.deb \
	&& rm -rf /var/lib/apt/lists/*

ENV PATH "/usr/lib/jvm/java-17-openjdk-amd64/bin:$PATH"

ENV JAVA_HOME "/usr/lib/jvm/java-17-openjdk-amd64/"

ENV M2_HOME "/opt/apache-maven-3.8.7"

ENV PATH "/opt/apache-maven-3.8.7/bin:$PATH"

RUN wget https://dlcdn.apache.org/maven/maven-3/3.8.7/binaries/apache-maven-3.8.7-bin.tar.gz \
    && tar -xzf apache-maven-3.8.7-bin.tar.gz \
    && rm apache-maven-3.8.7-bin.tar.gz \
    && mv apache-maven-3.8.7 /opt/

RUN wget https://download2.gluonhq.com/openjfx/19.0.2.1/openjfx-19.0.2.1_linux-x64_bin-sdk.zip \
    && unzip openjfx-19.0.2.1_linux-x64_bin-sdk.zip \
    && rm openjfx-19.0.2.1_linux-x64_bin-sdk.zip  \
    && mv javafx-sdk-19.0.2.1 /opt/

RUN python3 -m pip install 'scikit-learn==1.1.3' 'numpy==1.23.4' 'jep==4.1.0' 'graphviz==0.20.1'

WORKDIR /root/taxonomymining

COPY /src src
COPY /resources resources
COPY /pom.xml pom.xml

WORKDIR /root/mining_data

COPY /examples input

WORKDIR /root/taxonomymining

RUN mvn package

ENTRYPOINT ["java", "-p", "/opt/javafx-sdk-19.0.2.1/lib", "--add-modules=javafx.controls,javafx.fxml", "-Djava.library.path=/usr/local/lib/python3.8/dist-packages/jep", "-jar", "/root/taxonomymining/target/TaxonomyMining.jar"]













