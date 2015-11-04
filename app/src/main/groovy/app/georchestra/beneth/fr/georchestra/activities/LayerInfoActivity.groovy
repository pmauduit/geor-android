package app.georchestra.beneth.fr.georchestra.activities

import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Parcel
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.*
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import fr.beneth.wxslib.Layer

public class LayerInfoActivity extends AppCompatActivity {
    int LAYER_TRESHOLD = 5

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_layer_info)

        Bundle extras = getIntent().getExtras()
        String layerName = extras.getString("GeorInstance.layer_name")
        def wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        Layer l = wmsCap.findLayerByName(layerName)

        if (l.name)
            this.setTitle(l.name)
        else
            this.setTitle(l.title)

        TextView ltt = (TextView) this.findViewById(R.id.layerTitleText)
        TextView lat = (TextView) this.findViewById(R.id.layerAbstractText)
        TextView lattrt = (TextView) this.findViewById(R.id.layerAttributionText)
        TextView lkt = (TextView) this.findViewById(R.id.layerKeywordsText)
        TextView lst =  (TextView) this.findViewById(R.id.layerStylesText)
        CheckBox qchk = (CheckBox) this.findViewById(R.id.layerQueryableCheck)
        CheckBox qopk = (CheckBox) this.findViewById(R.id.layerOpaqueCheck)

        ltt.setText(l.title)
        lat.setText(l._abstract)
        if (l.attributionTitle) {
            lattrt.setText("Attribution: ${l.attributionTitle}")
        } else {
            lattrt.setText("")
        }
        if (l.keywords.size()) {
            lkt.setText("Keywords: ${l.keywords.join(", ")}")
        } else {
            lkt.setText("")
        }
        if (l.styles.size()) {
            def layersTitles = l.styles.collect { it.name }
            lst.setText("Styles: ${layersTitles.join(", ")}")
        }
        else {
            lst.setText("")
        }
        qchk.setChecked(l.queryable)
        qopk.setChecked(l.opaque)

        def capReqPng = wmsCap.capabilityRequests.find {
            it.name.toLowerCase() == "getmap" && it.formats.contains("image/png")
        } != null



        ImageView layerOverView = new ImageView(this) {
            boolean loaded = false
            @Override
            protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec)
                int width = getMeasuredWidth()
                int height = getMeasuredHeight()

                if (capReqPng && l.getLayersCount() > LAYER_TRESHOLD && ! loaded) {
                    loaded = true
                    // http://sdi.georchestra.org                    -> instance URL needed
                    // /geoserver/wms?                               -> hardcoded
                    // LAYERS=pmauduit_test:armoires-fo              -> l.name
                    // &FORMAT=image%2Fpng                           -> png || jpg
                    // &SERVICE=WMS                                  -> hardcoded
                    // &VERSION=1.1.1&                               -> hardcoded
                    // REQUEST=GetMap&                               -> hardcoded
                    // SRS=EPSG%3A4326&BBOX=5.8794184477356,45.556558165252,
                    //                5.9788649672668,45.58826575119 -> getcapabilities (wxslib)
                    // &WIDTH=1035                                   -> width
                    // &HEIGHT=330                                   -> height
                }
            }
        }
        LinearLayout.LayoutParams owLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
        )
        layerOverView.setBackgroundColor(Color.BLACK)
        LinearLayout owLayout = (LinearLayout) findViewById(R.id.ll2)
        owLayout.addView(layerOverView, owLayoutParams)
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
