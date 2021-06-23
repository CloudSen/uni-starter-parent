package cn.unic.starter.redis.utils;

import cn.unic.starter.redis.dto.RedisFetchDTO;
import cn.unic.starter.redis.dto.RedisFetchListDTO;
import cn.unic.starter.redis.dto.RedisFetchMapDTO;
import cn.unic.starter.redis.dto.RedisFetchSetDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.text.MessageFormat;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author younikong
 */
@Deprecated
@Service
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * items和exits 长度要一样 否则会报错
     *
     * @param items  :
     * @param exists
     * @return
     * @author soriee
     * @date 2020/8/5 15:30
     */
    public <V> List<V> getExistsOrNotExistsList(List<V> items, List<Boolean> exists, boolean findExits) {
        if (items.size() != exists.size()) {
            throw new ArrayIndexOutOfBoundsException("items和exits长度不一致");
        }

        List<V> resultList = new ArrayList<>(items.size());

        for (int i = 0; i < exists.size(); i++) {
            if (exists.get(i) && findExits) {
                resultList.add(items.get(i));
            } else if (!exists.get(i) && !findExits) {
                resultList.add(items.get(i));
            }
        }
        return resultList;
    }

    /**
     * 指定缓存失效时间
     *
     * @param key  键
     * @param time 时间(秒)
     * @return
     */
    public boolean expire(String key, long time) {
        if (key == null) {
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key 键 不能为null
     * @return 时间(秒) 返回0代表为永久有效
     */
    public long getExpire(String key) {
        return redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key是否存在
     *
     * @param key 键
     * @return true 存在 false不存在
     */
    public boolean hasKey(String key) {
        if (key == null) {
            return false;
        }
        try {
            if (redisTemplate.isExposeConnection()) {
                return redisTemplate.hasKey(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 判断redis连接状态
     *
     * @return true 连接 false没连接
     */
    public boolean isExposeConnection() {
        try {
            if (redisTemplate.isExposeConnection()) {
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    /**
     * 删除缓存
     *
     * @param key 可以传一个值 或多个
     */
    @SuppressWarnings("unchecked")
    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }
    }

    public void del(Collection<String> key) {
        if (key != null && key.size() > 0) {
            redisTemplate.delete(key);
        }
    }

    //============================String=============================

    /**
     * 普通缓存获取
     *
     * @param key 键
     * @return 值
     */
    public Object get(String key) {
        if (key == null) {
            return null;
        }
        return redisTemplate.opsForValue().get(key);
    }

    public List<Object> mget(Collection<String> key) {
        return redisTemplate.opsForValue().multiGet(key);
    }

    /**
     * 普通缓存放入
     *
     * @param key   键
     * @param value 值
     * @return true成功 false失败
     */
    public boolean set(String key, Object value) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time) {
        if (key == null) {
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 普通缓存放入并设置时间
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒) time要大于0 如果time小于等于0 将设置无限期
     * @return true成功 false 失败
     */
    public boolean set(String key, Object value, long time, TimeUnit unit) {
        if (key == null) {
            return false;
        }
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(key, value, time, unit);
            } else {
                set(key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 递增
     *
     * @param key   键
     * @param delta 要增加几(大于0)
     * @return
     */
    public long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, delta);
    }

    /**
     * 递减
     *
     * @param key   键
     * @param delta 要减少几(小于0)
     * @return
     */
    public long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(key, -delta);
    }

    //================================Map=================================

    /**
     * HashGet
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return 值o
     */
    public Object hget(String key, String item) {
        if (key == null || item == null) {
            return null;
        }
        Object result = null;
        try {
            result = redisTemplate.opsForHash().get(key, item);
        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 获取hashKey对应的所有键值
     *
     * @param key 键
     * @return 对应的多个键值
     */
    public Map<Object, Object> hgetmap(String key) {
        Map<Object, Object> resultMap = null;
        if (key == null) {
            return resultMap;
        }

        try {
            resultMap = redisTemplate.opsForHash().entries(key);
        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 获取传入的所有key的值
     *
     * @param key
     * @return
     */
    public List<Object> hmget(String key, Collection hashKeys) {
        List<Object> resultList = new ArrayList<>();
        if (key == null) {
            return resultList;
        }
        try {
            resultList = redisTemplate.opsForHash().multiGet(key, hashKeys);


        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
        }
        return resultList;
    }


    /**
     * Created by LogicArk on 2019/5/7
     *
     * @param
     * @return
     * @description 获取指定key下，所有的field
     */
    public Set<Object> hkeys(String key) {
        if (key == null) {
            return new HashSet<>();
        }
        Set<Object> resultSet = null;
        try {
            resultSet = redisTemplate.opsForHash().keys(key);
        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
        }
        if (resultSet == null) {
            resultSet = new HashSet<>();
        }
        return resultSet;
    }

    /**
     * HashSet
     *
     * @param key 键
     * @param map 对应多个键值
     * @return true 成功 false 失败
     */
    public <V> boolean hmset(String key, Map<String, V> map) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * HashSet 并设置时间
     *
     * @param key  键
     * @param map  对应多个键值
     * @param time 时间(秒)
     * @return true成功 false失败
     */
    public boolean hmset(String key, Map map, long time) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForHash().putAll(key, map);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param field 项
     * @param value 值
     * @return true 成功 false失败
     */
    public boolean hset(String key, String field, Object value) {
        if (key == null || field == null) {
            return false;
        }
        try {
            redisTemplate.opsForHash().put(key, field, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Created by LogicArk on 2019/2/18
     *
     * @param
     * @return
     * @description 只有在字段 field 不存在时，设置哈希表字段的值
     */
    public boolean hsetNx(String key, String field, Object value) {
        if (key == null || field == null) {
            return false;
        }
        try {
            redisTemplate.opsForHash().putIfAbsent(key, field, value);
            return true;
        } catch (RedisConnectionFailureException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 向一张hash表中放入数据,如果不存在将创建
     *
     * @param key   键
     * @param item  项
     * @param value 值
     * @param time  时间(秒)  注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return true 成功 false失败
     */
    public boolean hset(String key, String item, Object value, long time) {
        if (key == null || item == null) {
            return false;
        }
        try {
            redisTemplate.opsForHash().put(key, item, value);
            if (time > 0) {
                expire(key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除hash表中的值
     *
     * @param key  键 不能为null
     * @param item 项 可以使多个 不能为null
     */
    public void hdel(String key, Object... item) {
        if (key == null) {
            return;
        }
        redisTemplate.opsForHash().delete(key, item);
    }

    /**
     * 判断hash表中是否有该项的值
     *
     * @param key  键 不能为null
     * @param item 项 不能为null
     * @return true 存在 false不存在
     */
    public boolean hHasKey(String key, String item) {
        if (key == null || item == null) {
            return false;
        }
        return redisTemplate.opsForHash().hasKey(key, item);
    }

    /**
     * 判断
     *
     * @param key
     * @param item
     * @return
     */
    public boolean HasKey(String key, String item) {
        if (key == null || item == null) {
            return false;
        }
        return redisTemplate.opsForSet().isMember(key, item);
    }

    /**
     * hash递增 如果不存在,就会创建一个 并把新增后的值返回
     *
     * @param key  键
     * @param item 项
     * @param by   要增加几(大于0)
     * @return
     */
    public double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, by);
    }

    /**
     * hash递减
     *
     * @param key  键
     * @param item 项
     * @param by   要减少记(小于0)
     * @return
     */
    public double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(key, item, -by);
    }

    //============================set=============================

    /**
     * 根据key获取Set中的所有值
     *
     * @param key 键
     * @return
     */
    public Set<Object> sGet(String key) {
        if (key == null) {
            return new HashSet<>();
        }
        try {
            return redisTemplate.opsForSet().members(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public boolean sHasKey(String key, Object value) {
        if (key == null) {
            return false;
        }
        try {
            return redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将数据放入set缓存
     *
     * @param key    键
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <E> long sAdd(String key, Collection<E> values) {
        if (key == null || values == null || values.size() == 0) {
            return 0;
        }
        try {
            return redisTemplate.opsForSet().add(key, values.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <E> long sAdd(String key, E value) {
        Set<E> values = new HashSet<>();
        values.add(value);
        return this.sAdd(key, values);
    }


    /**
     * 将数据从set中删除
     *
     * @param key    :
     * @param values
     * @return
     * @author soriee
     * @date 2020/8/11 14:26
     */
    public <E> long sRemove(String key, Set<E> values) {
        if (key == null || values == null || values.size() == 0) {
            return 0;
        }
        try {
            return redisTemplate.opsForSet().remove(key, values.toArray());
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public <E> long sRemove(String key, E value) {
        if (key == null) {
            return 0;
        }
        Set<E> values = new HashSet<>();
        values.add(value);
        return this.sRemove(key, values);
    }

    /**
     * 将set数据放入缓存
     *
     * @param key    键
     * @param time   时间(秒)
     * @param values 值 可以是多个
     * @return 成功个数
     */
    public <E> long sAddAndTime(String key, long time, Set<E> values) {
        if (key == null) {
            return 0;
        }
        try {
            Long count = redisTemplate.opsForSet().add(key, values.toArray());
            if (time > 0) expire(key, time);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取set缓存的长度
     *
     * @param key 键
     * @return
     */
    public long sGetSetSize(String key) {
        if (key == null) {
            return 0;
        }
        try {
            return redisTemplate.opsForSet().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }


    //===============================list=================================

    /**
     * 获取list缓存的内容
     *
     * @param key   键
     * @param start 开始
     * @param end   结束  0 到 -1代表所有值
     * @return
     */
    public List<Object> lGet(String key, long start, long end) {
        if (key == null) {
            return null;
        }
        try {
            return redisTemplate.opsForList().range(key, start, end);
        } catch (Exception e) {
            System.out.println("错误的key:" + key);
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 获取list缓存的长度
     *
     * @param key 键
     * @return
     */
    public long lGetListSize(String key) {
        if (key == null) {
            return 0;
        }
        try {
            return redisTemplate.opsForList().size(key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 通过索引 获取list中的值
     *
     * @param key   键
     * @param index 索引  index>=0时， 0 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推
     * @return
     */
    public Object lGetIndex(String key, long index) {
        if (key == null) {
            return null;
        }
        try {
            return redisTemplate.opsForList().index(key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSet(String key, Object value) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List lGetAll(String key) {
        return lGet(key, 0, -1);
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSetSingle(String key, Object value, long time) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForList().rightPush(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @return
     */
    public boolean lSetAll(String key, List value, long time) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将list放入缓存
     *
     * @param key   键
     * @param value 值
     * @param time  时间(秒)
     * @return
     */
    public boolean lSet(String key, List value, long time) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.delete(key);
            redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0) expire(key, time);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据索引修改list中的某条数据
     *
     * @param key   键
     * @param index 索引
     * @param value 值
     * @return
     */
    public boolean lUpdateIndex(String key, long index, Object value) {
        if (key == null) {
            return false;
        }
        try {
            redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 移除N个值为value
     *
     * @param key   键
     * @param count 移除多少个
     * @param value 值
     * @return 移除的个数
     */
    public long lRemove(String key, long count, Object value) {
        if (key == null) {
            return 0;
        }
        try {
            Long remove = redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 匹配获取键值对，ScanOptions.NONE为获取全部键对
     *
     * @param key
     * @param options
     * @return
     * @author xiaoyin
     * @date 2019/5/6 16:39
     */
    public Map<String, Object> getHKeyValueMap(String key, ScanOptions options) {
        Map<String, Object> keyValueMap = new HashMap<>();
        Cursor<Map.Entry<Object, Object>> cursor = redisTemplate.opsForHash().scan(key, options);
        while (cursor.hasNext()) {
            Map.Entry<Object, Object> entry = cursor.next();
            keyValueMap.put(entry.getKey().toString(), entry.getValue());
        }
        return keyValueMap;
    }

    /**
     * 根据输入一次获取所有keys的值
     *
     * @param keys
     * @return
     */
    public List<Object> getByKeys(List<String> keys) {
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String key : keys) {
                connection.get(key.getBytes());
            }
            return null;
        });
        return list;
    }


    /**
     * redis批量设置值
     *
     * @param keys
     * @param value
     */
    public void insertByKeys(List<String> keys, String value) {
        redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String key : keys) {
                connection.set(key.getBytes(), value.getBytes());
            }
            return null;
        });
    }

    /**
     * 获取hash中的部分field
     *
     * @param key
     * @param fields
     * @return
     */
    public List<Object> hGetbyfields(String key, List<String> fields) {
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String field : fields) {
                connection.hMGet(key.getBytes(), field.getBytes());
            }
            return null;
        });
        return list;
    }


    public List<Boolean> mExists(List<String> keys) {
        List<Object> list = redisTemplate.executePipelined((RedisCallback<String>) connection -> {
            for (String key : keys) {
                connection.exists(key.getBytes());
            }
            return null;
        });
        List<Boolean> resultList = new ArrayList<>(list.size());
        for (Object per : list) {
            resultList.add((Boolean) per);
        }
        return resultList;
    }

    public Set<String> scan(String matchKey, int count) {
        return (Set<String>) redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> keysTmp = new HashSet<>();
            Cursor<byte[]> cursor = connection.scan(new ScanOptions.ScanOptionsBuilder().match(matchKey).count(count).build());
            while (cursor.hasNext()) {
                keysTmp.add(new String(cursor.next()));
            }
            return keysTmp;
        });
    }

    public void deleteKeys(String keyPattern) {
        Set<String> scan = scan(keyPattern, 1000);
        while (!CollectionUtils.isEmpty(scan)) {
            del(scan);
            scan = scan(keyPattern, 1000);
        }
    }

    public <T> List<String> getReload(List<String> inputList, Collection<T> resultList, boolean reload, String redisKey, Object... p) {
        if (reload) {
            return new ArrayList<>(inputList);
        }
        List<String> keys = inputList.stream().map(i -> MessageFormat.format(redisKey, i, p)).collect(Collectors.toList());
        List<Object> mget = this.mget(keys);
        List<String> reloadList = new ArrayList<>();
        for (int i = 0; i < mget.size(); i++) {
            Object val = mget.get(i);
            if (val == null) {
                reloadList.add(inputList.get(i));
            } else {
                resultList.add((T) val);
            }
        }
        return reloadList;
    }

    public <T> List<String> getReload(Set<String> inputSet, Collection<T> resultList, boolean reload, String redisKey, Object... p) {
        return getReload(inputSet, resultList, false, reload, redisKey, p);
    }

    public <T> List<String> getReload(Set<String> inputSet, Collection<T> resultList, boolean key2List, boolean reload, String redisKey, Object... p) {
        if (reload) {
            return new ArrayList<>(inputSet);
        }
        List<String> inputList = new ArrayList<>(inputSet);
        List<String> keys = inputList.stream().map(i -> MessageFormat.format(redisKey, i, p)).collect(Collectors.toList());
        List<Object> mget = this.mget(keys);
        List<String> reloadList = new ArrayList<>();
        for (int i = 0; i < mget.size(); i++) {
            Object val = mget.get(i);
            if (val == null) {
                reloadList.add(inputList.get(i));
            } else {
                if (key2List) {
                    resultList.addAll((List<T>) val);
                } else {
                    resultList.add((T) val);
                }
            }
        }
        return reloadList;
    }

    public <T> List<String> getReload(Set<String> inputSet, Map<String, T> resultMap, boolean reload, String redisKey, Object... p) {
        if (reload) {
            return new ArrayList<>(inputSet);
        }
        List<String> inputList = new ArrayList<>(inputSet);
        List<String> keys = inputList.stream().map(i -> MessageFormat.format(redisKey, i, p)).collect(Collectors.toList());
        List<Object> mget = this.mget(keys);
        List<String> reloadList = new ArrayList<>();
        for (int i = 0; i < mget.size(); i++) {
            Object val = mget.get(i);
            if (val == null) {
                reloadList.add(inputList.get(i));
            } else {
                resultMap.put(inputList.get(i), (T) val);
            }
        }
        return reloadList;
    }

    public <T> RedisFetchDTO<T> keyExistsAndNotNull(String key, Class<T> clazz, boolean reload) {
        if (reload) {
            return new RedisFetchDTO<>(null, true);
        }
        Object o = this.get(key);
        if (Objects.nonNull(o)) {
            return new RedisFetchDTO<>((T) o, false);
        } else {
            return new RedisFetchDTO<>(null, true);
        }
    }

    public <K, T> RedisFetchMapDTO<K, T> keyExistsAndNotNullMap(String key, Class<K> keyClazz, Class<T> valueClazz, boolean reload) {
        if (reload) {
            return new RedisFetchMapDTO<>(null, true);
        }
        Object o = this.hgetmap(key);
        if (Objects.nonNull(o)) {
            return new RedisFetchMapDTO<>((Map<K, T>) o, false);
        } else {
            return new RedisFetchMapDTO<>(null, true);
        }
    }

    public <T> RedisFetchListDTO<T> keyExistsAndListNotNull(String key, Class<T> clazz, boolean reload) {
        if (reload) {
            return new RedisFetchListDTO<>(null, true);
        }
        Object o = this.get(key);
        if (Objects.nonNull(o)) {
            return new RedisFetchListDTO<>((List<T>) o, false);
        } else {
            return new RedisFetchListDTO<>(null, true);
        }
    }

    public <T> RedisFetchSetDTO<T> keyExistsAndSetNotNull(String key, Class<T> clazz, boolean reload) {
        if (reload) {
            return new RedisFetchSetDTO<>(null, true);
        }
        Object o = this.get(key);
        if (Objects.nonNull(o)) {
            return new RedisFetchSetDTO<>((Set<T>) o, false);
        } else {
            return new RedisFetchSetDTO<>(null, true);
        }
    }
}
