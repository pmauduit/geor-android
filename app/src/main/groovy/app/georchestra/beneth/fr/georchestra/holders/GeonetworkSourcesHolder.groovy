package app.georchestra.beneth.fr.georchestra.holders

import fr.beneth.cswlib.geonetwork.GeoNetworkSource;


public class GeonetworkSourcesHolder {
    private static final GeonetworkSourcesHolder holder = new GeonetworkSourcesHolder()

    private List<GeoNetworkSource> geoNetworkSources

    public void setGeonetworkSources(List<GeoNetworkSource> gnSrc) {
        geoNetworkSources = gnSrc
    }

    public List<GeoNetworkSource> getGeonetworkSources() {
        return geoNetworkSources
    }

    public static GeonetworkSourcesHolder getInstance() { return holder }
}
