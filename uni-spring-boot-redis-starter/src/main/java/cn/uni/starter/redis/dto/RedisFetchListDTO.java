package cn.uni.starter.redis.dto;

import java.util.List;

public class RedisFetchListDTO<T> {
    private List<T> result;
    private boolean goOnFetch;

    public List<T> getResult() {
        return result;
    }

    public void setResult(List<T> result) {
        this.result = result;
    }

    public boolean isGoOnFetch() {
        return goOnFetch;
    }

    public void setGoOnFetch(boolean goOnFetch) {
        this.goOnFetch = goOnFetch;
    }

    public RedisFetchListDTO(List<T> result, boolean goOnFetch) {
        this.result = result;
        this.goOnFetch = goOnFetch;
    }
}
