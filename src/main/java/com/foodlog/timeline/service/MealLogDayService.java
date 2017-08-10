package com.foodlog.timeline.service;


import com.foodlog.config.BatchConfigs;
import com.foodlog.timeline.entity.MealLog;
import com.foodlog.timeline.repository.MealLogRepository;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by rafael on 07/07/17.
*/
@Service
public class MealLogDayService {

    @Autowired
    private MealLogRepository mealLogRepository;

    public List<MealLog> findAll() {


        ZonedDateTime baseDate = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

        //baseDate = baseDate.minus(5, ChronoUnit.DAYS);

        Instant yesterday = baseDate.truncatedTo(ChronoUnit.DAYS).toInstant().minus(1, ChronoUnit.DAYS);
        System.out.println(yesterday);

        Instant tomorrow = baseDate.truncatedTo(ChronoUnit.DAYS).toInstant().plus(1, ChronoUnit.DAYS);
        System.out.println(tomorrow);




        List<MealLog> result = new ArrayList<>();

        MealLog lastProcessed = null;
        for (MealLog mealLog : mealLogRepository.findByMealDateTimeBetweenOrderByMealDateTimeDesc(yesterday, tomorrow)) {
            System.out.println(mealLog.getId() + " " + mealLog.getMealDateTime() + "     " + mealLog.getMealDateTime().atZone(ZoneId.of("America/Sao_Paulo")));

            if(lastProcessed == null){
                lastProcessed = mealLog;
            }

            long elapsedMealTime = Duration.between(mealLog.getMealDateTime(), lastProcessed.getMealDateTime()).getSeconds() / (60 * 60);

            System.out.println("Achando a noite. " + mealLog.getMealDateTime() + "(epalsed: " + elapsedMealTime + "): " + (elapsedMealTime < BatchConfigs.SLEEP_INTERVAL));
            // Nao achei a noite, entao computa. Isso eh pra atender casos em que come de madruga
            if (elapsedMealTime < BatchConfigs.SLEEP_INTERVAL){

                result.add(mealLog);

                lastProcessed = mealLog;
            } else {
                //achei a noite, acaba o processamento
                break;
            }
        }




        return result;
    }
}
