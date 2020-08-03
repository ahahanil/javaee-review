package tk.deriwotua.es.last.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import tk.deriwotua.es.last.annotation.FieldTag;

import java.io.Serializable;

public class AuditDto implements Serializable {
    private static final long serialVersionUID = 1L;
    @JsonProperty
    @FieldTag(fieldData = true, type = "string")
    private String code;
    @JsonProperty
    @FieldTag(fieldData = true, type = "string")
    private String codeName;
    @JsonProperty
    @FieldTag(fieldData = true, type = "string")
    private Object oldData;
    @JsonProperty
    @FieldTag(fieldData = true, type = "string")
    private Object newData;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCodeName() {
        return codeName;
    }

    public void setCodeName(String codeName) {
        this.codeName = codeName;
    }

    public Object getOldData() {
        return oldData;
    }

    public void setOldData(Object oldData) {
        this.oldData = oldData;
    }

    public Object getNewData() {
        return newData;
    }

    public void setNewData(Object newData) {
        this.newData = newData;
    }
}