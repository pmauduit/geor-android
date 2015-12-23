package app.georchestra.beneth.fr.georchestra

import app.georchestra.beneth.fr.georchestra.utils.GnUtils;
import org.junit.Test
import static org.junit.Assert.*

public class GnUtilsTest {

    @Test
    public void testGuessMetadataUuid() throws Exception {
        def urlToTest [ "http://example.com/geonetwork/uuid/not/set",
        "http://example.com/geonetwork/xml.metadata.get?uuid=123456" ]
        def expected [ null, "123456" ]

        urlToTest.each { it, idx ->
            assertEquals(GnUtils.guessMetadataUuid(it), expected[idx])
        }
    }
}
