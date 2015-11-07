package app.georchestra.beneth.fr.georchestra.utils;

public class GsUtils {
    public static String getGeoserverWmsUrl(String instanceUrl, String operation) {
        def ret = "${instanceUrl}geoserver/wms?service=wms&request=${operation}"
        // Hack to fix data from the georchestra instance WFS sources:
        // Sometimes the url to the geOr instance points to the viewer.
        ret = ret -~ /(mapfishapp|carto)\//
        return ret
    }
}
