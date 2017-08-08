package uk.ac.ic.wlgitbridge.bridge.resource;

import org.junit.Test;
import uk.ac.ic.wlgitbridge.bridge.db.noop.NoopDbStore;
import uk.ac.ic.wlgitbridge.io.http.ning.NingHttpClientFacade;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.Mockito.mock;

public class UrlResourceCacheTest {

    private static String PROJ = "proj";

    private static String URL = "http://localhost/file.jpg";

    private static String NEW_PATH = "file1.jpg";

    private final NingHttpClientFacade http = mock(NingHttpClientFacade.class);

    private final UrlResourceCache cache
            = new UrlResourceCache(new NoopDbStore(), http);

    @Test
    public void getThrowsSizeLimitWhenContentLengthTooBig() throws Exception {
//        when(http.get(any(), any())).thenAnswer(new Answer<byte[]>() {
//            @Override
//            public byte[] answer(InvocationOnMock invocationOnMock) throws Throwable {
//                Object[] args = invocationOnMock.getArguments();
//                ((FunctionT<
//                        HttpResponseHeaders, Boolean, SizeLimitExceededException
//                >) args[1]).apply()
//            }
//        });
        cache.get(
                PROJ, URL, NEW_PATH,
                Collections.emptyMap(), Collections.emptyMap(),
                Optional.of(2L)
        );
    }
}