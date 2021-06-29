package cn.uni.starter.redis.dto;


public class RedisFetchDTO<T> {
    private T result;
    private boolean goOnFetch;

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public boolean isGoOnFetch() {
        return goOnFetch;
    }

    public void setGoOnFetch(boolean goOnFetch) {
        this.goOnFetch = goOnFetch;
    }

    public RedisFetchDTO(T result, boolean goOnFetch) {
        this.result = result;
        this.goOnFetch = goOnFetch;
    }
}
