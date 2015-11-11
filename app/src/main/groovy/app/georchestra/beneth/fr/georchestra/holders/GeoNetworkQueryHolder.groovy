package app.georchestra.beneth.fr.georchestra.holders

import fr.beneth.cswlib.geonetwork.GeoNetworkQuery

public class GeoNetworkQueryHolder {
    private static GeoNetworkQueryHolder ourInstance = new GeoNetworkQueryHolder()

    private GeoNetworkQuery geoNetworkQuery

    public GeoNetworkQuery getGeoNetworkQuery() {
        return geoNetworkQuery
    }

    public void setGeoNetworkQuery(GeoNetworkQuery gnq) {
        geoNetworkQuery = gnq
    }

    public static GeoNetworkQueryHolder getInstance() { return ourInstance }

    private GeoNetworkQueryHolder() { }
}
