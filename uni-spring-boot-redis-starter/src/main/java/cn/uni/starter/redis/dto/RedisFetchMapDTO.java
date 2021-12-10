package cn.uni.starter.redis.dto;

import java.util.Map;

public class RedisFetchMapDTO<K, T> {
    private Map<K, T> result;
    private boolean goOnFetch;

    public RedisFetchMapDTO(Map<K, T> result, boolean goOnFetch) {
        this.result = result;
        this.goOnFetch = goOnFetch;
    }

    public Map<K, T> getResult() {
        return result;
    }

    public void setResult(Map<K, T> result) {
        this.result = result;
    }

    public boolean isGoOnFetch() {
        return goOnFetch;
    }

    public void setGoOnFetch(boolean goOnFetch) {
        this.goOnFetch = goOnFetch;
    }
}
