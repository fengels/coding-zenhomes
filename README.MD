# Energy Measure App


### How to Run

`./gradlew bootRun`


### API Documentation

http://localhost:8080/swagger-ui.html

### Articles
http://www.baeldung.com/java-influxdb

### Test dependencies
```
docker run -p 8086:8086 -v influxdb:/var/lib/influxdb -e INFLUXDB_DB=energy -e INFLUXDB_USER_PASSWORD=help -e INFLUXDB_USER=help  influxdb
```   
https://docs.docker.com/samples/library/influxdb/#using-this-image


### Checking the DB manually
```
curl -sl -I localhost:8086/ping
```

```
$ curl -G 'http://localhost:8086/query?db=energy' --data-urlencode 'q=SELECT * FROM "energy"'
```

```
$ curl -G 'http://localhost:8086/query?db=energy' --data-urlencode 'q=SELECT * FROM "energy" WHERE time >= now() - 24h
```