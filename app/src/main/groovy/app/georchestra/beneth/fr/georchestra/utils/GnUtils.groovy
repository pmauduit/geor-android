package app.georchestra.beneth.fr.georchestra.utils

import fr.beneth.wxslib.MetadataUrl

/**
 * Created by pmauduit on 11/7/15.
 */
public class GnUtils {

    public static String guessMetadataUuid(ArrayList<MetadataUrl> mdUrls) {
        for (def mdUrl : mdUrls) {
            def guessed = GnUtils.guessMetadataUuid(mdUrl.url)
            if (guessed != null) {
                return guessed
            }
        }
        return null
    }
    public static String guessMetadataUuid(String mdUrl) {
        def pat = mdUrl =~ /.*geonetwork.*[uuid|id]=([^&]*)/
        if (pat.matches()) {
            return pat[0][1]
        }
        return null
    }

    public static isEligible(ArrayList<MetadataUrl> mdUrls) {
        for (def mdUrl : mdUrls) {
            if (mdUrl.url ==~ /.*geonetwork.*[uuid|id]=.*/)
                return true
        }
        return false
    }
    public static getGeonetworkUrl(String instanceUrl) {
        def ret = "${instanceUrl}geonetwork/"
        // Hack to fix data from the georchestra instance WFS sources:
        // Sometimes the url to the geOr instance points to the viewer.
        ret = ret -~ /(mapfishapp|carto)\//
        return ret
    }
    public static getCswUrl(String instanceUrl) {
        return "${GnUtils.getGeonetworkUrl(instanceUrl)}srv/eng/csw"
    }
}

