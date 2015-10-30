package app.georchestra.beneth.fr.georchestra

import fr.beneth.wxslib.operations.Capabilities;


public class WmsCapabilitiesHolder {
    private static final WmsCapabilitiesHolder holder = new WmsCapabilitiesHolder()

    private Capabilities wmsCapabilities

    public void setWmsCapabilities(Capabilities cap) {
        wmsCapabilities = cap
    }

    public Capabilities getWmsCapabilities() {
        return wmsCapabilities
    }

    public static WmsCapabilitiesHolder getInstance() { return holder }
}
