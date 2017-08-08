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
        return weightRepository.findTop30ByOrderByWeightDateTimeDesc();
    }
}
