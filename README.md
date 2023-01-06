# jsonexporter

JSON-Prometheus-Exporter in Java.

## Building

You will have to supply a working `settings.xml` for the maven build process in the container.

Then, on the command line, you can execute a docker build by executing:

````bash
docker build \ 
# --add-host=<maven-repository.host.name>:<IP-Address> \
# --add-host=<docker-registry.host.name>:<IP-Address>
 --build-arg DOCKER_REGISTRY=docker-registry.host.name:18080 \
   -f src/main/docker/Dockerfile.jvm \
   -t weigo/quarkus_json_exporter .
````

If your maven repository or docker registry cannot be resolved from inside the container, add DNS resolution by removing the hash bangs
above.

You can then execute the container using the supplied docker composition:

````bash
docker-compose up -d
````

## Configuration
