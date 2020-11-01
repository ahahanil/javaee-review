package tk.deriwotua.kafka.wechat.service;


import com.alibaba.fastjson.JSONObject;
import tk.deriwotua.kafka.wechat.conf.WechatTemplateProperties;

public interface WechatTemplateService {

    /**
     * 获取微信调查问卷模板 - 获取目前active为true的模板就可以了
     * @return
     */
    WechatTemplateProperties.WechatTemplate getWechatTemplate();

    /**
     * 上报调查问卷填写结果
     * @param reportInfo
     */
    void templateReported(JSONObject reportInfo);

    /**
     * 获取调查问卷的统计结果
     * @param templateId
     * @return
     */
    JSONObject templateStatistics(String templateId);

}
