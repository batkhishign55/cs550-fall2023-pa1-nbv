from ubuntu

COPY . /cs550
RUN apt-get update && apt-get install -y ant && apt-get install -y vim && apt-get install -y python-is-python3
ENV JAVA_OPTS="-XX:PermSize=1024m -XX:MaxPermSize=2g"

ENTRYPOINT ["tail", "-f", "/dev/null"]
