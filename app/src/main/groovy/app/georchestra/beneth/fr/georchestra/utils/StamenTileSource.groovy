package app.georchestra.beneth.fr.georchestra.utils

import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource

/**
 * Created by pmauduit on 5/14/17.
 */

public class StamenTileSource {

    public static final OnlineTileSourceBase StamenTonerTileSource =  new XYTileSource(
            "Stamen Toner",
            0, 19, 256,
            ".png",
            [
                "http://stamen-tiles-a.a.ssl.fastly.net/toner-lite/",
                "http://stamen-tiles-b.a.ssl.fastly.net/toner-lite/",
                "http://stamen-tiles-c.a.ssl.fastly.net/toner-lite/",
                "http://stamen-tiles-d.a.ssl.fastly.net/toner-lite/"
            ] as String[],
            "Map tiles by Stamen Design, CC BY 3.0, Map data © OpenStreetMap contributors")
}
