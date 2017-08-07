package com.foodlog;

import com.foodlog.weight.Weight;
import com.foodlog.weight.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private WeightRepository weightRepository;

    @RequestMapping("/weight")
    public List<Weight> greeting(@RequestParam(value="patient") String patient) {
        List<Weight> result = weightRepository.findTop15ByOrderByWeightDateTimeDesc();
        System.out.println("########################   result.size:  " + result.size());
        return weightRepository.findTop15ByOrderByWeightDateTimeDesc();
    }
}
