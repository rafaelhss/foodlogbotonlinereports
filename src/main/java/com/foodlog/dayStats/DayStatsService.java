package com.foodlog.dayStats;

import com.foodlog.entity.MealLog;
import com.foodlog.entity.ScheduledMeal;
import com.foodlog.entity.user.User;
import com.foodlog.timeline.repository.MealLogRepository;
import com.foodlog.timeline.repository.ScheduledMealRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;

/**
 * Created by rafa on 13/08/17.
 */
@Service
public class DayStatsService {

    @Autowired
    MealLogRepository mealLogRepository;

    @Autowired
    ScheduledMealRepository scheduledMealRepository;

    private final int BASIC_PENALIZATION = 5;
    private final int LOW_PENALIZATION = BASIC_PENALIZATION * 1;
    private final int MEDIUM_PENALIZATION = LOW_PENALIZATION * 2;
    private final int HIGH_PENALIZATION = MEDIUM_PENALIZATION * 3;



    public DayStats generateStats(User currentUser) {
        DayStats dayStats = calculateMealIntervals(new DayStats(), currentUser);

       int score = 100;

        // penalizacao por cada refeicao fora de hora,
        // independentemente de quantidade
        int penalty = (LOW_PENALIZATION * dayStats.getOffTimeMeals());
        score -= penalty;
        if(penalty > 0)
            dayStats.getDayScore().getPenalties().add("Refeicao fora de hora: -" +penalty);

        //penalizacao por refeicao extra alem do agendado
        //Ou por refeicao que ficou devendo
        penalty = (MEDIUM_PENALIZATION * (Math.abs(dayStats.getScheduledMeals() - dayStats.getLoggedMeals())));
        score -= penalty;
        if(penalty > 0)
            dayStats.getDayScore().getPenalties().add("Refeicao extra ou faltante: -" +penalty);

        //penalizacao por cada intervalo maior
        // que 3 horas sem comer
        penalty = (HIGH_PENALIZATION * dayStats.getTreePlusHourIntervals());
        score -= penalty;
        if(penalty > 0)
            dayStats.getDayScore().getPenalties().add("Intervalo maior que 3 horas: -" +penalty);

        //penalizacao por diferenca grande entre intervalo
        //medio previsto e realizado. 30 min de tolerancia
        float diff = dayStats.getAvgSecondsBetweenMeals() - dayStats.getScheduledAvgSecondsBetweenMeals();
        if(Math.abs(diff) > (60 * 30)){
            penalty = HIGH_PENALIZATION;
            score -= penalty;
            dayStats.getDayScore().getPenalties().add("Interv. medio diff do esperado: -" +penalty);
        }

        dayStats.getDayScore().setScore(score);

        return dayStats;
    }


    private DayStats calculateMealIntervals(DayStats dayStats, User currentUser) {

        Instant now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant();
        System.out.println("ZonedDateTime.now(ZoneId.of(\"America/Sao_Paulo\")):" + ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")));
        System.out.println("ZonedDateTime.now(ZoneId.of(\"America/Sao_Paulo\")).toInstant():" + ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant());

        List<MealLog> mealLogs2Days = mealLogRepository.findByUserAndMealDateTimeAfterOrderByMealDateTimeDesc(currentUser, now.truncatedTo(ChronoUnit.DAYS).minus(2, ChronoUnit.DAYS));
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");

        float secondsSum = 0;
        float count = 0;

        int count3hourintervals = 0;
        int countOnTime = 0;
        int countOffTime = 0;

        Instant lastMealTime = null;
        for (MealLog mealLog: mealLogs2Days){
            if(lastMealTime != null) {

                //ZonedDateTime brTime = mealLog.getMealDateTime().atZone(ZoneId.of("America/Sao_Paulo"));
                Instant current = mealLog.getMealDateTime();

                float seconds = Duration.between(current, lastMealTime).getSeconds();

                //if (!brTime.truncatedTo(ChronoUnit.DAYS).isBefore(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).truncatedTo(ChronoUnit.DAYS))) { // passou um dia. ignora
                if(seconds > (60 * 60 * 5)) { //se for mais que 5 horas chegou no dia anterior. ai para
                    break;
                } else {

                    if(seconds > (60 * 60 * 3)){
                        count3hourintervals++;
                    }
                    if(mealLog.getScheduledMeal() != null){
                        countOnTime++;
                    } else {
                        countOffTime++;
                    }

                    secondsSum += seconds;
                    count += 1F;
                }
                System.out.println(mealLog.getId() + "----> " + mealLog.getMealDateTime() + " ---> " + Duration.between(mealLog.getMealDateTime(), lastMealTime).getSeconds());
            }

            lastMealTime =  mealLog.getMealDateTime();
        }

        float avgSeconds = (secondsSum/count);

        if(avgSeconds > 1) {
            dayStats.setAvgSecondsBetweenMeals(avgSeconds);
            dayStats.setLoggedMeals((int) ++count);
            dayStats.setScheduledMeals(getScheduledMeals(currentUser));
            dayStats.setScheduledAvgSecondsBetweenMeals(calcScheduledAvgIntervals(currentUser));
            dayStats.setTreePlusHourIntervals(count3hourintervals);
            dayStats.setOnTimeMeals(countOnTime);
            dayStats.setOffTimeMeals(countOffTime);
            return dayStats;
        } else {
            return null;
        }
    }

    private float calcScheduledAvgIntervals(User currentUser) {

        ZonedDateTime lastMealTime = null;
        float secondsSum = 0;
        float count = 0;

        for(ScheduledMeal scheduledMeal:scheduledMealRepository.findByUserOrderByTargetTimeDesc(currentUser)){
            ZonedDateTime current = getZonedTargetTime(scheduledMeal);
            if(lastMealTime != null && !current.equals(lastMealTime)) {
                float seconds = Duration.between(current, lastMealTime).getSeconds();
                secondsSum += seconds;
                count += 1F;
            }
            lastMealTime = current;
        }
        float avgSeconds = (secondsSum/count);

        return avgSeconds;
    }

    private ZonedDateTime getZonedTargetTime(ScheduledMeal scheduledMeal) {
        String time[] = scheduledMeal.getTargetTime().split(":");

        ZonedDateTime now = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));

        int hour = Integer.parseInt(time[0]);
        int minute = Integer.parseInt(time[1]);

        return now.with(LocalTime.of(hour, minute));
    }

    private int getScheduledMeals(User currentUser) {

        HashMap<String,String> aux = new HashMap<>();
        int count = 0;
        for(ScheduledMeal scheduledMeal:scheduledMealRepository.findByUserOrderByTargetTimeDesc(currentUser)){
            if (aux.get(scheduledMeal.getTargetTime()) == null){
                aux.put(scheduledMeal.getTargetTime(),scheduledMeal.getTargetTime());
                count++;
            }
        }
        return count;
    }

}
