FROM openjdk:15-jdk-alpine
COPY target/*SNAPSHOT.jar app.jar
EXPOSE 8080
ENV TZ=Asia/Seoul
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone
ENTRYPOINT ["java","-Xmx400M","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar","--spring.profiles.active=docker"]

FROM gitpod/workspace-full

# Helm 설치
RUN curl https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3 | bash

# kubectl 설치 (안정적 바이너리 방식)
RUN curl -LO "https://dl.k8s.io/release/$(curl -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl" && \
    install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl && \
    rm kubectl


# Azure CLI 설치
RUN curl -sL https://aka.ms/InstallAzureCLIDeb | bash

# Java, Maven, Docker 등 추가 설치
RUN sudo apt-get install -y openjdk-17-jdk maven docker.io

COPY init.sh /init.sh
RUN chmod +x /init.sh
