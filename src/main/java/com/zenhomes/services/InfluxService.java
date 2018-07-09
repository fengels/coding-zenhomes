package com.zenhomes.services;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;
import org.influxdb.impl.InfluxDBResultMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
public class InfluxService {

    @Value("${databaseURL}")
    private String databaseURL;

    @Value("${userName}")
    private String userName;

    @Value("${password}")
    private String password;

    private InfluxDB influxDB;

    @PostConstruct
    public void init(){
        influxDB = InfluxDBFactory.connect(databaseURL, userName, password);
        influxDB.createDatabase("energy");
        influxDB.enableBatch(100, 100, TimeUnit.MILLISECONDS);
        influxDB.setDatabase("energy");
        influxDB.setRetentionPolicy("defaultPolicy");
        influxDB.setConsistency(InfluxDB.ConsistencyLevel.QUORUM);
        influxDB.createRetentionPolicy("defaultPolicy", "energy", "30d", 1, true);
    }

    /**
     * {
     *     "counter_id": "1",
     *     "amount": 10000.123
     * }
     */
    public void count(Long counter_id, Double amount) {
        Point point = Point.measurement("energy")
                .time(System.currentTimeMillis(), TimeUnit.MILLISECONDS)
                .addField("counter_id", counter_id)
                .addField("amount", amount)
                .build();

        influxDB.write(point);
    }

    /**
     * {
     *     "villages": [
     *         {
     *             "village_name": "Villarriba",
     *             "consumption": 12345.123
     *         },
     *         {
     *             "village_name": "Villabajo",
     *             "consumption": 23456.123
     *         }
     *     ]
     * }
     */
    public List<EnergyPoint> consumptionReport(String duration){
        QueryResult result = influxDB.query(new Query("SELECT counter_id,amount FROM \"energy\" WHERE time >= now() -" + duration,"energy"));

        InfluxDBResultMapper resultMapper = new InfluxDBResultMapper();
        List<EnergyPoint> list = resultMapper.toPOJO(result,EnergyPoint.class);
        return list;
    }
}
