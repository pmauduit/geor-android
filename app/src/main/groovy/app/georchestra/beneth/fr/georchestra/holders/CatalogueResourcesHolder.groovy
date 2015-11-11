package app.georchestra.beneth.fr.georchestra.holders

import fr.beneth.cswlib.geonetwork.GeoNetworkSource;


public class CatalogueResourcesHolder {
    private static final CatalogueResourcesHolder holder = new CatalogueResourcesHolder()

    private List<GeoNetworkSource> geoNetworkSources
    private List<String> typeOfResources

    public void setGeonetworkSources(List<GeoNetworkSource> gnSrc) {
        geoNetworkSources = gnSrc
    }

    public List<GeoNetworkSource> getGeonetworkSources() {
        return geoNetworkSources
    }

    public List<String> getTypeOfResources() {
        return typeOfResources
    }
    public void setTypeOfResources(List<String> typeOfResources) {
        this.typeOfResources = typeOfResources
    }

    public static CatalogueResourcesHolder getInstance() { return holder }
}
