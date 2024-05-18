package main.system.resetableCache;

public interface ResetableCache {

    public String cacheName();

    public String cacheDescription();

    public void rebuildCache();
}
