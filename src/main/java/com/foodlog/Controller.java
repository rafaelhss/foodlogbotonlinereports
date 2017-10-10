package com.foodlog;

import com.foodlog.bodylog.BodyLogService;
import com.foodlog.dayStats.DayStats;
import com.foodlog.dayStats.DayStatsService;
import com.foodlog.entity.BodyLogImage;
import com.foodlog.entity.MealLog;
import com.foodlog.entity.ScheduledMeal;
import com.foodlog.entity.user.User;
import com.foodlog.entity.user.UserRepository;
import com.foodlog.jaca.Jaca;
import com.foodlog.jaca.JacaRepository;
import com.foodlog.timeline.repository.ScheduledMealRepository;
import com.foodlog.timeline.service.MealLogDayService;
import com.foodlog.weight.Weight;
import com.foodlog.weight.WeightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class Controller {

    @Autowired
    private WeightRepository weightRepository;

    @Autowired
    private MealLogDayService mealLogDayService;

    @Autowired
    private ScheduledMealRepository scheduledMealRepository;

    @Autowired
    private DayStatsService dayStatsService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BodyLogService bodyLogService;

    @Autowired
    private JacaRepository jacaRepository;

    @CrossOrigin(origins = "*")
    @RequestMapping("/body-log")
    public BodyLogImage getBodyPanel(@RequestParam(value="userid") Long userid,
    @RequestParam(defaultValue = "panel", value="image-type") String type) {
        User user = userRepository.findOne(userid);

        if(type != null && type.trim().equals("gif")){
            return bodyLogService.getBodyGif(user);
        }
        return bodyLogService.getBodyPanel(user);
    }



    @CrossOrigin(origins = "*")
    @RequestMapping("/jaca")
    public List<Jaca> listJacass(@RequestParam(value="userid") Long userid) {
        User user = userRepository.findOne(userid);
        return jacaRepository.findTop30ByUserOrderByJacaDateTime(user);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/weight")
    public List<Weight> listWeights(@RequestParam(value="userid") Long userid) {
        User user = userRepository.findOne(userid);
        return weightRepository.findTop30ByUserOrderByWeightDateTimeDesc(user);
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/meal-log")
    public List<MealLog> getAllMealLogDays(@RequestParam(value="userid") Long userid) {
        return mealLogDayService.findAll(userRepository.findOne(userid));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/scheduled-meals")
    public List<ScheduledMeal> getAllScheduledMeals(@RequestParam(value="userid") Long userid) {
        return scheduledMealRepository.findByUserOrderByTargetTime(userRepository.findOne(userid));
    }

    @CrossOrigin(origins = "*")
    @RequestMapping("/day-stats")
    public DayStats getDayStats(@RequestParam(value="userid") Long userid){

        return dayStatsService.generateStats(userRepository.findOne(userid));
    }


}
