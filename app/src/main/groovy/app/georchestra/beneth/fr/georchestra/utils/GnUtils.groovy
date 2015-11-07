package app.georchestra.beneth.fr.georchestra.utils

import fr.beneth.wxslib.MetadataUrl

/**
 * Created by pmauduit on 11/7/15.
 */
public class GnUtils {
    public static String guessMetadataUuid(ArrayList<MetadataUrl> mdUrls) {
        for (def mdUrl : mdUrls) {
            def pat = mdUrl.url =~ /[uuid|id]=([^&]*)/
            if (pat != null) {
                return pat[0][1]
            }
        }
        return null
    }
}
