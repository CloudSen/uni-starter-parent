package cn.uni.starter.redis.dto;

import java.util.Set;

public class RedisFetchSetDTO<T> {
    private Set<T> result;
    private boolean goOnFetch;

    public Set<T> getResult() {
        return result;
    }

    public void setResult(Set<T> result) {
        this.result = result;
    }

    public boolean isGoOnFetch() {
        return goOnFetch;
    }

    public void setGoOnFetch(boolean goOnFetch) {
        this.goOnFetch = goOnFetch;
    }

    public RedisFetchSetDTO(Set<T> result, boolean goOnFetch) {
        this.result = result;
        this.goOnFetch = goOnFetch;
    }
}
