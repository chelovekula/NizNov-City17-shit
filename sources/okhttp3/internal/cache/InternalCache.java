package okhttp3.internal.cache;

import java.io.IOException;
import okhttp3.C1069Response;
import okhttp3.Request;

public interface InternalCache {
    C1069Response get(Request request) throws IOException;

    CacheRequest put(C1069Response response) throws IOException;

    void remove(Request request) throws IOException;

    void trackConditionalCacheHit();

    void trackResponse(CacheStrategy cacheStrategy);

    void update(C1069Response response, C1069Response response2);
}
