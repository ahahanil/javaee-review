package tk.deriwotua.es.last.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.commons.collections.map.HashedMap;
import org.apache.commons.lang.StringUtils;
import tk.deriwotua.es.last.SearchParameterDto;
import tk.deriwotua.es.last.enums.Sort;
import tk.deriwotua.es.last.utils.ElkUtils;

import java.util.Iterator;
import java.util.Map;

@JsonInclude(value = JsonInclude.Include.NON_NULL)
public class SearchParamDto extends SearchParameterDto<ElkAuditDto> {

    ElkQueryParamDto elkQueryParamDto;

    public SearchParamDto(ElkQueryParamDto elkQueryParamDto, String appId, String propertiesEnv) {
        this.elkQueryParamDto = elkQueryParamDto;
        super.setClazz(ElkAuditDto.class);
        init(appId, propertiesEnv);
    }

    private void init(String appId, String propertiesEnv) {
        if (null != this.elkQueryParamDto) {
            if(this.elkQueryParamDto.isPage()){
                super.setFrom(this.elkQueryParamDto.getRealIndex() * this.elkQueryParamDto.getPageSize());
                super.setSize(this.elkQueryParamDto.getPageSize());
            } else {
                super.setFrom(this.elkQueryParamDto.getRealIndex() * this.elkQueryParamDto.getPageSize());
                super.setSize(9999);
            }

            super.setIndexes(ElkUtils.getIndex(appId, propertiesEnv).split(","));
            super.setTypes(ElkUtils.getType(appId, propertiesEnv).split(","));
            if (StringUtils.isNotBlank(this.getElkQueryParamDto().getSortField())) {
                Map<String, Boolean> sortMap = new HashedMap();
                sortMap.put(this.elkQueryParamDto.getSortField(), 0 == this.elkQueryParamDto.getSortOrder().compareTo(Sort.ASC) ? true : false);
                super.setSorts(sortMap);
            }
        }
    }

    /**
     * 重写实现对range支持
     *
     * @return
     */
    @Override
    public ObjectNode buildRequestNode() {
        ObjectNode req = this.baseBuild();
        if (null != this.elkQueryParamDto) {
            ArrayNode must = req.putObject("query").putObject("bool").putArray("must");

            /**
             * range
             *{
             *    "from": 0,
             *    "query": {
             *        "query_string": {
             *            "query": {
             *                "bool": {
             *                    "must": [
             *                        {
             *                            "match_phrase": {
             *                               "operProjectName": "BPI-15086"
             *                            }
             *                        },
             *                        {
             *                            "range": {
             *                                "operTime": {
             *                                    "gte": "20180319143404",
             *                                    "lte": "20180402180000"
             *                                }
             *                            }
             *                        }
             *                    ]
             *                }
             *            }
             *        }
             *    },
             *    "size": 100
             *}
             */
            if (StringUtils.isNotBlank(this.elkQueryParamDto.getOperTimeFrom()) && StringUtils.isNotBlank(this.elkQueryParamDto.getOperTimeTo())) {
                must.addObject().putObject("range").putObject("operTime").put("gte", this.elkQueryParamDto.getOperTimeFrom()).put("lte", this.elkQueryParamDto.getOperTimeTo());
            }
            Map<String, String> param = ElkUtils.bean2Map(this.elkQueryParamDto);
            param.putAll(ElkUtils.extMap2Param(this.elkQueryParamDto.getExtMap()));
            Iterator<Map.Entry<String, String>> var3 = param.entrySet().iterator();

            while (var3.hasNext()) {
                Map.Entry<String, String> next = var3.next();
                must.addObject().putObject("match_phrase").put(next.getKey(), next.getValue());
            }
        }

        return req;
    }

    public ElkQueryParamDto getElkQueryParamDto() {
        return elkQueryParamDto;
    }

    public void setElkQueryParamDto(ElkQueryParamDto elkQueryParamDto) {
        this.elkQueryParamDto = elkQueryParamDto;
    }
}