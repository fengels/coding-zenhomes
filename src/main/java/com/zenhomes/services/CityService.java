package com.zenhomes.services;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CityService {

    // This could be any persistent backend.
    private ConcurrentHashMap<Long,String> cities;


    public CityService(){
        cities = new ConcurrentHashMap<>();
        cities.put(1l,"Villarriba");
        cities.put(2l,"Villabajo");
    }


    public void insertCity(Long id,String name){
        cities.put(id,name);
    }

    public void deleteCity(Long id){
        cities.remove(id);
    }

    public Map<Long,String> getCitites(){
        return this.cities;
    }

    public Map<Long,String> getCitites(Long id){
        Map<Long,String> result = new HashMap<>();
         result.put(id,this.cities.get(id));
         return result;
    }
    public String getCititesString(Long id){
        return this.cities.get(id);
    }
}
