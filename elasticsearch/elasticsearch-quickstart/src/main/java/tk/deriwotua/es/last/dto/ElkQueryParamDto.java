package tk.deriwotua.es.last.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.util.StringUtils;

import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class ElkQueryParamDto extends BaseQueryDto {

    public static final String OPERATE_LOG = "operateLog";
    public static final String AUDIT_TRAIL = "auditTrail";

    public final static int	DEFAULT_PAGE_INDEX	= 1;	// 基准
    public final static int	DEFAULT_PAGE_SIZE	= 20;	// pageSize:20

    @ApiModelProperty("Id（内部生成的编号，创建时请勿传入）")
    String id;

    @ApiModelProperty("软件Id")
    String operSoftId;
    @ApiModelProperty("软件Name")
    String operSoftName;
    @ApiModelProperty("模块Id")
    String operModuleId;
    @ApiModelProperty("模块Name")
    String operModuleName;

    String auditType = AUDIT_TRAIL;

    @ApiModelProperty("动作的Id，如：create|update|delete|…")
    String operActId;
    @ApiModelProperty("动作的Name，如：创建|修改|删除|…")
    String operActName;
    @ApiModelProperty("租户Id")
    String operTenantId /*= SystemContext.getTenantId()*/;
    @ApiModelProperty("租户Name")
    String operTenantName;
    @ApiModelProperty("项目id")
    String operProjectId;
    @ApiModelProperty("项目Name")
    String operProjectName;
    @ApiModelProperty("中心id")
    String operSiteId;
    @ApiModelProperty("中心Name")
    String operSiteName;
    @ApiModelProperty("用户Id")
    String operUserId;
    @ApiModelProperty("用户Name")
    String operUserName;
    @ApiModelProperty("关联Id")
    String operRefId;
    @ApiModelProperty("关联Name")
    String operRefName;
    @ApiModelProperty("动作的时间，时间戳")
    String operTime;
    @ApiModelProperty("Ip地址")
    String operIpAddr;
    @ApiModelProperty("检索字段")
    String code;
    @ApiModelProperty("检索字段显示名")
    String codeName;

    @ApiModelProperty("扩展数据Map")
    Map<String, String> extMap;

    @ApiModelProperty("动作的时间起始范围，yyyy-MM-dd hh:mi:ss")
    String operTimeFrom;
    @ApiModelProperty("动作的时间结束范围，yyyy-MM-dd hh:mi:ss")
    String operTimeTo;

    @ApiModelProperty("操作对象")
    String operTarget;

    public int getRealIndex(){
        if(this.getPageNo() < DEFAULT_PAGE_INDEX) {this.setPageNo(DEFAULT_PAGE_INDEX);}
        if(this.getPageSize() < DEFAULT_PAGE_INDEX) {this.setPageSize(DEFAULT_PAGE_SIZE);}

        return this.getPageNo() - 1;
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

    public String getOperTime() {
        return operTime;
    }

    public void setOperTime(String operTime) {
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

    public String getOperTimeFrom() {
        String from = operTimeFrom;
        if(!StringUtils.isEmpty(operTimeFrom)) {
            from = operTimeFrom.replaceAll("-|\\s|:", "");
        }
        return from;
    }

    public void setOperTimeFrom(String operTimeFrom) {
        this.operTimeFrom = operTimeFrom;
    }

    public String getOperTimeTo() {

        String to = operTimeTo;
        if(!StringUtils.isEmpty(operTimeTo)) {
            to = operTimeTo.replaceAll("-|\\s|:", "");
        }
        return to;
    }

    public void setOperTimeTo(String operTimeTo) {
        this.operTimeTo = operTimeTo;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public String getAuditType() {
        return auditType;
    }

    public void setAuditType(String auditType) {
        this.auditType = auditType;
    }

    public String getOperTarget() {
        return operTarget;
    }

    public void setOperTarget(String operTarget) {
        this.operTarget = operTarget;
    }
}