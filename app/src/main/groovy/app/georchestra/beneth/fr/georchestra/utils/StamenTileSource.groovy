package app.georchestra.beneth.fr.georchestra.utils

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource

/**
 * Created by pmauduit on 5/14/17.
 */

public class StamenTileSource {
public static final OnlineTileSourceBase StamenTonerTileSource =  new XYTileSource("Stamen Toner",
    0, 19, 256, ".png", [
                "http://a.tile.openstreetmap.org/",
                "http://b.tile.openstreetmap.org/",
                "http://c.tile.openstreetmap.org/" ],"Map tiles by Stamen Design, CC BY 3.0, " +
        "Map data © OpenStreetMap contributors");
}
