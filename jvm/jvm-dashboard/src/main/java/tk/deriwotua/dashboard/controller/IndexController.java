package tk.deriwotua.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import tk.deriwotua.dashboard.service.DAUService;
import tk.deriwotua.dashboard.service.UserService;
import tk.deriwotua.dashboard.utils.DateUtils;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
public class IndexController {
    @Autowired
    private DAUService dauService;
    @Autowired
    private UserService userService;

    public IndexController() {
    }

    @RequestMapping({"index"})
    public ModelAndView index() {
        ModelAndView mv = new ModelAndView("index");
        mv.addObject("dau_day", this.dauService.queryCountByDate(0));
        mv.addObject("dau_day_before", this.dauService.queryCountByDate(-1));
        mv.addObject("dau_day_month", this.dauService.queryCountByDate(-30));
        mv.addObject("dau_day_str", DateUtils.formatDate(2));
        mv.addObject("dau_day_before_str", DateUtils.formatDate(DateUtils.dateAddDays(new Date(), -1)));
        mv.addObject("dau_day_month_str", DateUtils.getDateStrOfDayLastMonth());
        return mv;
    }

    @GetMapping({"userCity"})
    @ResponseBody
    public List<Map<String, Object>> userCity() {
        return this.userService.queryUserCityGroupByCity();
    }
}