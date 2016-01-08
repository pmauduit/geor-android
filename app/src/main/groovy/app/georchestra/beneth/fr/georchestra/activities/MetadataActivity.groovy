package app.georchestra.beneth.fr.georchestra.activities

import android.graphics.Typeface
import android.opengl.Visibility
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveMetadataTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import fr.beneth.cswlib.metadata.Metadata
import fr.beneth.cswlib.metadata.OnlineResource
import fr.beneth.wxslib.Layer
import fr.beneth.wxslib.georchestra.Instance
import fr.beneth.wxslib.operations.Capabilities

public class MetadataActivity extends AppCompatActivity {
    private Layer layer
    public MetadataActivity() {}

    private RetrieveMetadataTask mdrt = null

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_metadata)
        Bundle extras = getIntent().getExtras()
        if (! extras) {
            finish()
        }
        int georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)

        Capabilities wmsCap = WmsCapabilitiesHolder.getInstance().getWmsCapabilities()
        String layerName = extras.getString("GeorInstance.layer_name")

        def mdUuid = null
        if (layerName == null)  {
            String mdUuidIntent = extras.getString("md.uuid")
            if (mdUuidIntent != null) {
                mdUuid = mdUuidIntent
            } else {
                finish()
            }
        }
        else {
            layer = wmsCap.findLayerByName(layerName)
            mdUuid = GnUtils.guessMetadataUuid(layer.metadataUrls)
        }

        if (mdUuid != null) {
            mdrt = new RetrieveMetadataTask(this)
            mdrt.execute(ist.url - ~/(mapfishapp|carto)\// +
                    "/geonetwork/srv/eng/xml.metadata.get?uuid=${mdUuid}")
        } else {
            Toast.makeText(this, "Unable to find the metadata UUID !",Toast.LENGTH_LONG).show()
            finish()
        }
    }

    public void updateInterface(Metadata m) {
        if ((m == null) || (mdrt != null) && (mdrt.error != null)) {
            Toast.makeText(this, "Error occured while loading metadata",Toast.LENGTH_LONG).show()
            finish()
            return
        }
        this.findViewById(R.id.progressBar).setVisibility(View.GONE)
        ((TextView) this.findViewById(R.id.title)).setText(m.title)
        ((TextView) this.findViewById(R.id._abstract)).setText(m._abstract)

        ((TextView) this.findViewById(R.id.keywords)).setText("Keywords: " + m.keywords.join(", "))
        if(m.graphicOverviewUrls.size() > 0) {
            new RetrieveImageTask([
                    (ImageView) this.findViewById(R.id.overview)
            ]).execute([m.graphicOverviewUrls.first()])
        }
        if (m.responsibleParty) {
            def indivName = (TextView) this.findViewById(R.id.individualName)
            def position = (TextView) this.findViewById(R.id.position)
            def orgname = (TextView) this.findViewById(R.id.orgname)
            ((TextView) this.findViewById(R.id.responsibleparty)).setText("Responsible party")

            if (m.responsibleParty.individualName) {
                indivName.setText(m.responsibleParty.individualName)
            } else {
                indivName.setVisibility(View.GONE)
            }

            if (m.responsibleParty.positionName) {
                position.setText(m.responsibleParty.positionName)
            } else {
                position.setVisibility(View.GONE)
            }

            if (m.responsibleParty.organisationName) {
                orgname.setText(m.responsibleParty.organisationName)
            } else {
                orgname.setVisibility(View.GONE)
            }
            if (m.responsibleParty.address) {
                def a = m.responsibleParty.address
                def dp = (TextView) this.findViewById(R.id.addr_deliverypoint)
                def pc = (TextView) this.findViewById(R.id.addr_postalcode)
                def city = (TextView) this.findViewById(R.id.addr_city)
                def mail = (TextView) this.findViewById(R.id.addr_email)
                if (a.deliveryPoint) {
                    dp.setText(a.deliveryPoint)
                } else {
                    dp.setVisibility(View.GONE)
                }
                if (a.postalCode) {
                    pc.setText(a.postalCode)
                } else {
                    pc.setVisibility(View.GONE)
                }
                if (a.city) {
                    city.setText(a.city)
                } else {
                    city.setVisibility(View.GONE)
                }
                if (a.electronicMailAddress) {
                    mail.setText(a.electronicMailAddress)
                } else {
                    mail.setVisibility(View.GONE)
                }
            }
        } else {
            ((TextView) this.findViewById(R.id.responsibleparty)).setVisibility(View.GONE)
        }
        if (m.onlineResources.size() > 0) {
            ((TextView) this.findViewById(R.id.onlineresourcetitle)).setText("Online resources")

            m.onlineResources.each {
                ((LinearLayout) this.findViewById(R.id.onlineresourcecontainer)).
                        addView(createOnlineResourceElement(it))
            }
        } else {
            ((TextView) this.findViewById(R.id.onlineresourcetitle)).setVisibility(View.GONE)

        }
    }

    private View createOnlineResourceElement(OnlineResource res) {
        def olres = new LinearLayout(this)
        def lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        )
        olres.setLayoutParams(lp)
        olres.setOrientation(LinearLayout.VERTICAL)

        def nameView = new TextView(this)
        nameView.setText(res.name)
        nameView.setTextAppearance(this, android.R.attr.textAppearanceLarge)
        nameView.setTypeface(null, Typeface.BOLD)

        def descView = new TextView(this)
        descView.setText(res.description)
        descView.setTextAppearance(this, android.R.attr.textAppearanceSmall)
        descView.setTypeface(null, Typeface.ITALIC)

        def protocolView = new TextView(this)
        protocolView.setText(res.protocol)
        protocolView.setTypeface(null, Typeface.BOLD)

        def urlView = new TextView(this)
        urlView.setText(res.url)
        urlView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 10)
        urlView.setTypeface(null, Typeface.ITALIC)

        olres.addView(nameView, lp)
        olres.addView(descView, lp)
        olres.addView(protocolView, lp)
        olres.addView(urlView, lp)
        return olres
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
