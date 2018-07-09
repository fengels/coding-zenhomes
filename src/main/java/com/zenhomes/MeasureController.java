package com.zenhomes;

import com.zenhomes.services.CityService;
import com.zenhomes.services.EnergyPoint;
import com.zenhomes.services.InfluxService;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@Validated
public class MeasureController {

    @Autowired
    private InfluxService influxService;

    @Autowired
    private CityService cityService;

    /**
     * <pre>
     *  POST /counter_callback
     *  {
     *      "counter_id": "1",
     *      "amount": 10000.123
     *  }
     * </pre>
     * @param json
     * @return
     */
    @PostMapping("/counter_callback")
    public HttpStatus counter(@RequestBody String json) {
        JSONObject jsonObject = new JSONObject(json);
        // TODO handle json exception
        // TODO validation

        Long counter_id = Long.valueOf(jsonObject.get("counter_id").toString());
        Double  amount = Double.valueOf(jsonObject.get("amount").toString());
        influxService.count(counter_id, amount);
        return HttpStatus.OK;
    }

    /**
     *
     * <pre>
     *    GET /counter?id=1
     *    {
     *        "id": "1",
     *        "village_name": "Villarriba"
     *    }
     * </pre>
     * @param id
     * @param model
     * @return
     */
    @GetMapping("/counter")
    public ResponseEntity<Object> counter(@Pattern(regexp = "^\\d*$") @RequestParam(name="id", required=false) String id, Model model) {
        Map<Long,String> mapping;
        if(id==null){
            mapping = cityService.getCitites();
        }else {
            mapping = cityService.getCitites(Long.valueOf(id));
        }
        if(mapping.isEmpty()||mapping.get(id)==null){
            return new ResponseEntity<Object>(mapping, HttpStatus.NOT_FOUND);
        }
        // TODO handle json exception
        return new ResponseEntity<Object>(mapping, HttpStatus.OK);
    }

    /**
     * GET /consumption_report
     *        "villages": [
     *         {
     *             "village_name": "Villarriba",
     *                 "consumption": 12345.123
     *         },
     *         {
     *             "village_name": "Villabajo",
     *                 "consumption": 23456.123
     *         }
     *     ]
     *     }
     * @param duration
     * @param model
     * @return
     */
    @GetMapping("/consumption_report")
    public String consumptionReport(@Pattern(regexp = "^\\d+[u,,ms,s,m,h,d,w]$") @RequestParam(name="duration", required=false, defaultValue="24h") String duration, Model model) {
        List<EnergyPoint> pointList = influxService.consumptionReport(duration);

        Set<Map.Entry<Long,Long>> res = pointList.stream()
                .collect(Collectors.groupingBy(energyPoint -> energyPoint.getCounterId(),
                        Collectors.summingLong(energyPoint -> energyPoint.getAmount())))
                .entrySet();

        JSONObject result = new JSONObject();
        JSONArray townList = new JSONArray();
        res.forEach(e -> townList.put(
                new JSONObject().put("village_name",cityService.getCititesString(e.getKey()))
                .put("consumption",e.getValue())));
        result.put("villages",townList);

        return result.toString();
    }
}
