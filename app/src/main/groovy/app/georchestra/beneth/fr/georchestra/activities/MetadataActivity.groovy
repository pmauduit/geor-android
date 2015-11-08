package app.georchestra.beneth.fr.georchestra.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveMetadataTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import fr.beneth.cswlib.metadata.Metadata
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities

public class MetadataActivity extends AppCompatActivity {
    private Layer layer
    public MetadataActivity() {}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metadata)
        Bundle extras = getIntent().getExtras()
        int georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)

        Capabilities wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        String layerName = extras.getString("GeorInstance.layer_name")
        layer = wmsCap.findLayerByName(layerName)

        def mdUuid = GnUtils.guessMetadataUuid(layer.metadataUrls)
        if (mdUuid != null) {
            def mdrt = new RetrieveMetadataTask(this)
            mdrt.execute(ist.url - ~/(mapfishapp|carto)\// +
                    "/geonetwork/srv/eng/xml.metadata.get?uuid=${mdUuid}")
        } else {
            Toast.makeText(this, "Unable to find the metadata UUID !",Toast.LENGTH_LONG).show()
        }
    }

    public void updateInterface(Metadata m) {
        if (m == null)
            return
        ((TextView) this.findViewById(R.id.title)).setText(m.title)
        ((TextView) this.findViewById(R.id._abstract)).setText(m._abstract)

        if(m.graphicOverviewUrls.first() != null) {
            new RetrieveImageTask(
                    (ImageView) this.findViewById(R.id.overview)
            ).execute(m.graphicOverviewUrls.first())
        }
        if (m.responsibleParty) {
            ((TextView) this.findViewById(R.id.individualName)).setText(m.responsibleParty.individualName)
            ((TextView) this.findViewById(R.id.position)).setText(m.responsibleParty.positionName)
            ((TextView) this.findViewById(R.id.orgname)).setText(m.responsibleParty.organisationName)
            if (m.responsibleParty.address) {
                def a = m.responsibleParty.address
                ((TextView) this.findViewById(R.id.addr_deliverypoint)).setText(a.deliveryPoint)
                ((TextView) this.findViewById(R.id.addr_postalcode)).setText(a.postalCode)
                ((TextView) this.findViewById(R.id.addr_city)).setText(a.city)
                ((TextView) this.findViewById(R.id.addr_email)).setText(a.electronicMailAddress)
            }
        }
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
