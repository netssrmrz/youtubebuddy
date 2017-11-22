package rs.android;

public class Cache
{
  public android.support.v4.util.LruCache<String, Object> mem_cache;
  
  public Cache()
  {
    final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
  
    // Use 1/8th of the available memory for this memory cache.
    final int cacheSize = maxMemory / 8;
  
    mem_cache = new android.support.v4.util.LruCache<String, Object>(cacheSize);
  }
}
