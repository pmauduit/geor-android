package app.georchestra.beneth.fr.georchestra.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.holders.WmsCapabilitiesHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveOrganismsTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import fr.beneth.cswlib.geonetwork.GeoNetworkSource
import fr.beneth.wxslib.georchestra.Instance

public class GeonetworkActivity extends AppCompatActivity {

    private List<GeoNetworkSource> gnSources = new ArrayList<GeoNetworkSource>()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geonetwork)

        def dateFrom = (DatePicker) this.findViewById(R.id.dateFromPicker)
        def dateTo = (DatePicker) this.findViewById(R.id.dateToPicker)
        dateFrom.setEnabled(false)
        dateTo.setEnabled(false)

        // Organism view

        def ov = (ListView) this.findViewById(R.id.organismView)
        ArrayAdapter<GeoNetworkSource> arrayAdapter = new ArrayAdapter<GeoNetworkSource>(
                this,
                android.R.layout.simple_list_item_1,
                gnSources) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                def gns = gnSources.get(position)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                text1.setText(gns.name)
                return view
            }

        }
        ov.setAdapter(arrayAdapter)

        def rotask = new RetrieveOrganismsTask(this)
        Bundle extras = getIntent().getExtras()
        def georInstanceId = extras.getInt("GeorInstance.id")
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)
        rotask.execute(GnUtils.getGeonetworkUrl(ist.url))

        // "When ?" part

        def whenCheck = (CheckBox) this.findViewById(R.id.whenActivated)
        whenCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dateFrom.setEnabled(isChecked)
                dateTo.setEnabled(isChecked)
            }
        })

        // Type of data
        def tv = (ListView) this.findViewById(R.id.typeView)
        def typeArr = [ "dataset", "series", "service" ]
        def typeAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                typeArr)
        tv.setAdapter(typeAdapter)
    }

    void updateOrganismsList(List<GeoNetworkSource> geoNetworkSources) {
        gnSources = geoNetworkSources.clone()
        gnSources.each {
            Log.d("GnActivity", it.name)
        }
        def ov = (ListView) this.findViewById(R.id.organismView)
        def ovAdapter = (ArrayAdapter) ov.getAdapter()
        ovAdapter.clear()
        ovAdapter.addAll(gnSources)
        ovAdapter.notifyDataSetChanged()
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    this.finish()
                    return true
                }
    }
}
