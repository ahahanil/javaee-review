package tk.deriwotua.es.last.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import tk.deriwotua.es.last.enums.Sort;

import java.io.Serializable;

@ApiModel
public class BaseQueryDto implements Serializable {

    @ApiModelProperty(value = "显示数目",example = "10")
    private int pageSize=10;
    @ApiModelProperty(value = "当前索引",example = "0")
    @Deprecated
    private int pageNo=1;
    @ApiModelProperty("开始页数")
    private int pageIndex=1;
    @ApiModelProperty("排序字段")
    private String sortField;
    @ApiModelProperty(value = "排序方式",example = "")
    private Sort sortOrder= Sort.ASC;
    @ApiModelProperty(value="是否关闭分页,如果为false 不分页",example = "true")
    private boolean page=true;


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getPageNo() {
        return pageNo;
    }

    public void setPageNo(int pageNo) {
        this.pageNo = pageNo;
    }

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }


    public boolean isPage() {
        return page;
    }

    public void setPage(boolean page) {
        this.page = page;
    }


    public Sort getSortOrder() {
        return sortOrder;
    }

    public void setSortOrder(Sort sortOrder) {
        this.sortOrder = sortOrder;
    }

    public int getPageIndex() {
        return getPageNo();
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
        setPageNo(pageIndex);
    }
}