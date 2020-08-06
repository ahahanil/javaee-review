package tk.deriwotua.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.deriwotua.dashboard.mapper.DAUMapper;
import tk.deriwotua.dashboard.utils.DateUtils;

import java.util.Date;

@Service
public class DAUService {
    @Autowired
    private DAUMapper dauMapper;

    public DAUService() {
    }

    public Integer queryCountByDate(int type) {
        String start = null;
        String end = null;
        Date date = new Date();
        if (type == -1) {
            date = DateUtils.dateAddDays(date, -1);
        } else if (type == -30) {
            try {
                date = DateUtils.parseDate(DateUtils.getDateStrOfDayLastMonth(), "yyyy-MM-dd");
            } catch (Exception var6) {
                var6.printStackTrace();
                return -1;
            }
        }

        start = DateUtils.getDateBegin(DateUtils.formatDate(date));
        end = DateUtils.getDateEnd(DateUtils.formatDate(date));
        return this.dauMapper.queryCountByDate(start, end);
    }
}