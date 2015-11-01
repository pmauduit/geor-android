package app.georchestra.beneth.fr.georchestra.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import fr.beneth.wxslib.Layer;

public class LayerInfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layer_info)

        Bundle extras = getIntent().getExtras()
        String layerName = extras.getString("GeorInstance.layer_name")
        def wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        Layer l = wmsCap.findLayerByName(layerName)


        TextView tv = (TextView) findViewById(R.id.LayerInfoView)
        tv.setText(layerAsText(l))
    }

    private String layerAsText(Layer layer) {
        def ret = "name: ${layer.name}\r\n" +
                "title: ${layer.title}\r\n" +
                "abstract: \r\n\r\n${layer._abstract}\r\n\r\n"
        if (layer.attributionTitle)
            ret += "attribution: \t${layer.attributionTitle}\r\n"
        ret += "is opaque: \t${layer.opaque}\r\n" +
                "is queryable: \t${layer.queryable}\r\n"
        if (layer.keywords.size() > 0) {
            ret += "keywords: \t${layer.keywords.join(",")}\r\n"
        }
        if (layer.styles.size() > 0) {
            ret += "styles: \t${layer.styles.collect{ it.name }.join(",")}\r\n"
        }
        if (layer.layers.size() > 0) {
            ret += "Children layers count (recursive): \t${layer.getLayersCount()}\r\n"
        }
        if (layer.metadataUrls.size() > 0) {
            ret += "Metadadata urls:\r\n"
            layer.metadataUrls.each {ret += "\t- ${it.url} - ${it.format}\r\n"}
        }
        ret
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish()
                return true
        }
        return super.onOptionsItemSelected(item)
    }
}
