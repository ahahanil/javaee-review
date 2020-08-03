package tk.deriwotua.es.last.utils;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class ElasticUtil {

    public static StringBuilder buildPath(String[] indexes, String[] types, String restPath) {
        StringBuilder path = new StringBuilder(restPath);
        if (indexes == null || indexes.length == 0) {
            path.append("/_all");
        } else {
            path.append("/").append(Arrays.stream(indexes).collect(Collectors.joining(",")));
        }

        if (types != null && types.length != 0) {
            path.append("/").append(Arrays.stream(types).collect(Collectors.joining(",")));
        }
        path.append("/_search?");
        return path;
    }

    public static ObjectNode buildSearchParam(Integer size, Integer from, Map<String, String> params, Map<String, Boolean> sorts) {
        ObjectNode req = JsonNodeFactory.instance.objectNode();

        if (size != null) {
            req.put("size", size);
        }
        if (from != null) {
            req.put("from", from);
        }
        if (params != null && !params.isEmpty()) {

            ArrayNode must = req.putObject("query").putObject("bool").putArray("must");
            for (String key : params.keySet()) {
                must.addObject().putObject("match_phrase").put(key, params.get(key));
            }
        }

        if (sorts != null && !sorts.isEmpty()) {
            ArrayNode orderBy = req.putArray("sort");
            for (String propertyName : sorts.keySet()) {
                orderBy.addObject().putObject(propertyName).put("order", sorts.get(propertyName) ? "asc" : "desc");
            }
        }
        return req;
    }
}