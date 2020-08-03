package tk.deriwotua.es.last.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import org.apache.commons.collections.map.HashedMap;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class ElkAuditDto extends AuditDto {
    private String id;

    public static final String OPERATE_LOG = "operateLog";
    public static final String AUDIT_TRAIL = "auditTrail";

    @ApiModelProperty("软件Id")
    @JsonProperty
    String operSoftId = "eTime";
    @ApiModelProperty("软件Name")
    @JsonProperty
    String operSoftName = "工时管理系统";
    @ApiModelProperty("痕迹 auditTrail (默认)||操作日志 operateLog")
    @JsonProperty
    String auditType = AUDIT_TRAIL;
    @ApiModelProperty("请求来源")
    @JsonProperty
    String requestSource;
    @ApiModelProperty("模块Id")
    @JsonProperty
    String operModuleId;
    @ApiModelProperty("模块Name")
    @JsonProperty
    String operModuleName;
    @ApiModelProperty("动作的Id，如：create|update|delete|…")
    @JsonProperty
    String operActId;
    @ApiModelProperty("动作的Name，如：创建|修改|删除|…")
    @JsonProperty
    String operActName;
    @ApiModelProperty("租户Id")
    @JsonProperty
    String operTenantId;
    @ApiModelProperty("租户Name")
    @JsonProperty
    String operTenantName;
    @ApiModelProperty("中心Id")
    @JsonProperty
    String operSiteId;
    @ApiModelProperty("中心Name")
    @JsonProperty
    String operSiteName;
    @ApiModelProperty("项目Id")
    @JsonProperty
    String operProjectId;
    @ApiModelProperty("项目Name")
    @JsonProperty
    String operProjectName;
    @ApiModelProperty("用户Id")
    @JsonProperty
    String operUserId;
    @ApiModelProperty("用户Name")
    @JsonProperty
    String operUserName;
    @ApiModelProperty("关联Id")
    @JsonProperty
    String operRefId;
    @ApiModelProperty("关联Name")
    @JsonProperty
    String operRefName;
    @ApiModelProperty("动作的时间，yyyy-MM-dd hh:mi:ss elk中转时间戳存储")
    @JsonProperty
    Date operTime;
    @ApiModelProperty("Ip地址")
    @JsonProperty
    String operIpAddr;

    @ApiModelProperty("操作对象")
    @JsonProperty
    String operTarget;

    @ApiModelProperty("扩展数据Map")
    @JsonProperty
    Map<String, String> extMap = new HashedMap();

    public ElkAuditDto() {
        Map<String, String> contextMap = new HashedMap();
        if(null != contextMap && !"config-trace".equals(contextMap.get("TM-ACTION-PATH"))){
            this.setOperModuleId(contextMap.get("TM-ACTION-PATH"));
            this.setOperModuleName(contextMap.get("moduleName"));

            this.requestSource = contextMap.get("TM-Header-Request-Source");
        }

        /*this.operTenantId = SystemContext.getTenantId();
        this.operUserId = SystemContext.getUserId();
        this.operUserName = SystemContext.getUserName();
        this.operIpAddr = SystemContext.getClientIp();*/
        this.operTime = new Date();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperSoftId() {
        return operSoftId;
    }

    public void setOperSoftId(String operSoftId) {
        this.operSoftId = operSoftId;
    }

    public String getOperSoftName() {
        return operSoftName;
    }

    public void setOperSoftName(String operSoftName) {
        this.operSoftName = operSoftName;
    }

    public String getOperModuleId() {
        return operModuleId;
    }

    public void setOperModuleId(String operModuleId) {
        this.operModuleId = operModuleId;
    }

    public String getOperModuleName() {
        return operModuleName;
    }

    public void setOperModuleName(String operModuleName) {
        this.operModuleName = operModuleName;
    }

    public String getOperActId() {
        return operActId;
    }

    public void setOperActId(String operActId) {
        this.operActId = operActId;
    }

    public String getOperActName() {
        return operActName;
    }

    public void setOperActName(String operActName) {
        this.operActName = operActName;
    }

    public String getOperTenantId() {
        return operTenantId;
    }

    public void setOperTenantId(String operTenantId) {
        this.operTenantId = operTenantId;
    }

    public String getOperTenantName() {
        return operTenantName;
    }

    public void setOperTenantName(String operTenantName) {
        this.operTenantName = operTenantName;
    }

    public String getOperUserId() {
        return operUserId;
    }

    public void setOperUserId(String operUserId) {
        this.operUserId = operUserId;
    }

    public String getOperUserName() {
        return operUserName;
    }

    public void setOperUserName(String operUserName) {
        this.operUserName = operUserName;
    }

    public String getOperRefId() {
        return operRefId;
    }

    public void setOperRefId(String operRefId) {
        this.operRefId = operRefId;
    }

    public String getOperRefName() {
        return operRefName;
    }

    public void setOperRefName(String operRefName) {
        this.operRefName = operRefName;
    }

    public Date getOperTime() {
        return operTime;
    }

    public void setOperTime(Date operTime) {
        this.operTime = operTime;
    }

    public String getOperIpAddr() {
        return operIpAddr;
    }

    public void setOperIpAddr(String operIpAddr) {
        this.operIpAddr = operIpAddr;
    }

    public Map<String, String> getExtMap() {
        return extMap;
    }

    public void setExtMap(Map<String, String> extMap) {
        this.extMap = extMap;
    }

    public String getOperSiteId() {
        return operSiteId;
    }

    public void setOperSiteId(String operSiteId) {
        this.operSiteId = operSiteId;
    }

    public String getOperSiteName() {
        return operSiteName;
    }

    public void setOperSiteName(String operSiteName) {
        this.operSiteName = operSiteName;
    }

    public String getOperProjectId() {
        return operProjectId;
    }

    public void setOperProjectId(String operProjectId) {
        this.operProjectId = operProjectId;
    }

    public String getOperProjectName() {
        return operProjectName;
    }

    public void setOperProjectName(String operProjectName) {
        this.operProjectName = operProjectName;
    }

    public String getOperTarget() {
        return operTarget;
    }

    public void setOperTarget(String operTarget) {
        this.operTarget = operTarget;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getRequestSource() {
        return requestSource;
    }

    public void setRequestSource(String requestSource) {
        this.requestSource = requestSource;
    }

    /**
     * 便于查询
     * @return
     */
    private String dateToString(){
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DD hh:mm:ss");
        Date date = new Date();
        return format.format(date).replaceAll("-|\\s|:", "");
    }
}