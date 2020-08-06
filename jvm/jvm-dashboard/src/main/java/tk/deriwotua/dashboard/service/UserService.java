package tk.deriwotua.dashboard.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.deriwotua.dashboard.mapper.UserMapper;
import tk.deriwotua.dashboard.utils.DateUtils;

import java.util.*;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper;

    public UserService() {
    }

    public List<Map<String, Object>> queryUserCityGroupByCity() {
        Date date = new Date();
        String start = DateUtils.getDateBegin(DateUtils.formatDate(DateUtils.dateAddDays(date, -6)));
        String end = DateUtils.getDateEnd(DateUtils.formatDate(date));
        List<Map<String, Object>> list = this.userMapper.queryUserCityGroupByCity(start, end);
        List<Map<String, Object>> result = new ArrayList();
        Iterator var6 = list.iterator();

        while(var6.hasNext()) {
            Map<String, Object> map = (Map)var6.next();
            Map<String, Object> h = new HashMap();
            h.put("name", StringUtils.replace((String)map.get("city"), "市", ""));
            h.put("value", map.get("num"));
            result.add(h);
        }

        return result;
    }
}