package cn.uni.starter.jpa.utils;

import cn.uni.common.util.CamelUtils;
import com.alibaba.fastjson.JSON;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.ListUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.text.CaseUtils;
import org.hibernate.SQLQuery;
import org.hibernate.transform.Transformers;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.Query;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * JPA native query 查询工具
 *
 * @author zhanhaoyang
 */
@Slf4j
@SuppressWarnings({"rawtypes", "unchecked", "deprecation"})
public class QueryUtil {

    public static final String OBJECT = "object";

    public static <T> List<T> queryResult(Query query, Class<T> clazz) {
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List queryList = query.getResultList();
        List<T> resultList = new ArrayList<>();
        queryList
            .forEach(r -> {
                try {
                    resultList.add(JSON.parseObject(JSON.toJSONString(r), clazz));
                } catch (Exception e) {
                    log.error("序列化异常\n{}" + ExceptionUtils.getStackTrace(e));
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

    /**
     * 反射包含父类
     *
     * @param query jpa query对象
     * @param clazz 要映射的类类型
     * @param <T>   类型T
     * @return 类型T列表
     */
    public static <T> List<T> queryResultList(Query query, Class<T> clazz) {
        query.unwrap(SQLQuery.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
        List<T> result = new ArrayList<>();
        List list = query.getResultList();
        List<Field> fields = getFields(clazz);

        list.forEach(item -> {

            T t = null;
            try {
                t = clazz.getDeclaredConstructor().newInstance();
                Map<String, Object> objectMap = (Map<String, Object>) item;
                for (Field field : fields) {
                    String key = CamelUtils.underline(field.getName()).toUpperCase();
                    if (objectMap.get(key) != null) {
                        field.setAccessible(true);
                        setValue(field, t, objectMap.get(key));
                    }
                }

                result.add(t);

            } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
                log.warn(e.getMessage());
            }
        });
        return result;
    }

    private static List<Field> getFields(Class<?> clazz) {
        List<Field> list = new ArrayList<>();
        Set<String> fields = new HashSet<>();
        if (OBJECT.equals(clazz.getSimpleName())) {
            return list;
        } else {
            for (Field field : clazz.getDeclaredFields()) {
                if (!fields.contains(field.getName())) {
                    list.add(field);
                    fields.add(field.getName());
                }
            }
            Class<?> superClass = clazz.getSuperclass();
            while (superClass != null && !OBJECT.equals(superClass.getSimpleName())) {
                for (Field field : superClass.getDeclaredFields()) {
                    if (!fields.contains(field.getName())) {
                        list.add(field);
                        fields.add(field.getName());
                    }
                }
                superClass = superClass.getSuperclass();
            }
        }
        return list;
    }
}
