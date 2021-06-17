package cn.unic.starter.jpa.utils;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.CaseUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Query;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

/**
 * JPA native query 查询工具
 *
 * @author zhanhaoyang
 */
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked"})
public class QueryUtil {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.setDateFormat(dateFormat);
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> List<T> queryResult(Query query, Class<T> clazz) {
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List queryList = query.getResultList();
        List<T> resultList = new ArrayList<>();
        queryList
            .forEach(r -> {
                try {
                    resultList.add(OBJECT_MAPPER.readValue(OBJECT_MAPPER.writeValueAsString(r), clazz));
                } catch (IOException e) {
                    log.warn(e.getMessage());
                }
            });
        return resultList;
    }

    public static <T> T querySingleResult(Query query, Class<T> clazz) {
        List<T> resultList = queryResult(query, clazz);
        return resultList.isEmpty() ? null : resultList.get(0);
    }

    public static <T> List<T> queryResultList(Query query, Class<T> clazz, boolean underLineToCamel) {
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<T> result = new ArrayList<>();
        List list = query.getResultList();
        list.forEach(item -> {
            T t;
            try {
                t = clazz.newInstance();
                Map<String, Object> objectMap = (Map<String, Object>) item;
                for (Map.Entry<String, Object> entry : objectMap.entrySet()) {
                    String attribute = underLineToCamel ? CaseUtils.toCamelCase(StringUtils.lowerCase(entry.getKey()), false, '_') :
                        entry.getKey();
                    try {
                        Field field = t.getClass().getDeclaredField(attribute);
                        field.setAccessible(true);
                        if (entry.getValue() != null) {
                            setValue(field, t, entry.getValue());
                        }
                    } catch (NoSuchFieldException e) {
                        log.warn(e.getMessage());
                    }
                }
                result.add(t);
            } catch (InstantiationException | IllegalAccessException e) {
                log.warn(e.getMessage());
            }
        });
        return result;
    }

    private static void setValue(Field field, Object target, Object data) {
        Class type = field.getType();
        try {
            if (type == String.class) {
                field.set(target, data.toString());
                return;
            }
            if (type == Integer.class || type == int.class) {
                field.setInt(target, (Integer) data);
                return;
            }
            if (type == boolean.class || type == Boolean.class) {
                field.set(target, "Y".equals(data));
            }
        } catch (IllegalAccessException e) {
            log.warn(e.getMessage());
        }
    }

    public static <T, M extends JpaRepository> List<T> findLarge(M repository, List<String> ids) {
        List<T> result = new ArrayList<>();
        List<List<String>> partition = ListUtils.partition(ids, 1000);
        partition.forEach(l -> result.addAll(repository.findAllById(l)));
        return result;
    }
}
