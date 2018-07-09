package com.zenhomes.services;

import com.fasterxml.jackson.annotation.JsonGetter;
import org.influxdb.annotation.Column;
import org.influxdb.annotation.Measurement;

import java.time.Instant;

@Measurement(name="energy")
public class EnergyPoint {

    @Column(name="time")
    private Instant time;

    @Column(name="counter_id")
    private Long counterId;

    @Column(name="amount")
    private Long amount;

    @JsonGetter("amount")
    public Long getAmount() {
        return amount;
    }

    @JsonGetter("counter_id")
    public Long getCounterId() {
        return counterId;
    }

    @JsonGetter("time")
    public Instant getTime() {
        return time;
    }
}
