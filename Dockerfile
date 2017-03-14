FROM java
LABEL maintainer "AmrAbed@vt.edu"

WORKDIR /tmp/rhids

COPY rhids /usr/bin/rhids
COPY src src
COPY gradle gradle
COPY build.gradle gradlew ./

RUN ./gradlew createJar && cp build/libs/*.jar /usr/bin/rhids/rhids.jar

ENV PATH $PATH:/usr/bin/rhids

VOLUME /usr/log/strace-docker
VOLUME /var/log/rhids

ENTRYPOINT ["rhids"]

CMD ["-h"]
