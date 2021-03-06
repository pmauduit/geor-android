package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.*
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveCatalogueResourcesTask
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveGeonetworkResultsTask
import app.georchestra.beneth.fr.georchestra.utils.GnUtils
import fr.beneth.cswlib.geonetwork.GeoNetworkQuery
import fr.beneth.cswlib.geonetwork.GeoNetworkSource
import fr.beneth.wxslib.georchestra.Instance

public class GeonetworkActivity extends AppCompatActivity {

    private List<GeoNetworkSource> gnSources = new ArrayList<GeoNetworkSource>()
    private List<String> selectedSources = new ArrayList<String>()
    private List<String>  selectedTypes = new ArrayList<String>()
    private List<String> typeOfResources = new ArrayList<String>()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geonetwork)
        this.findViewById(R.id.progressBar).setVisibility(View.GONE)
        def dateFromRow = (TableRow) this.findViewById(R.id.dateFromRow)
        def dateToRow   = (TableRow) this.findViewById(R.id.dateToRow)
        dateFromRow.setVisibility(View.GONE)
        dateToRow.setVisibility(View.GONE)

        // Organism view
        def ov = (ListView) this.findViewById(R.id.organisationsView)
        ArrayAdapter<GeoNetworkSource> arrayAdapter = new ArrayAdapter<GeoNetworkSource>(
                this, android.R.layout.simple_list_item_1, gnSources) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                def gns = gnSources.get(position)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                if (selectedSources.contains(gns.name)) {
                    view.setBackgroundResource(R.color.itemWithMd)
                } else {
                    view.setBackgroundResource(android.R.color.transparent)
                }
                text1.setText(gns.name)
                return view
            }

        }
        ov.setAdapter(arrayAdapter)
        ov.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def source = gnSources.get(position)
                if (selectedSources.contains(source.name)) {
                    selectedSources.remove(source.name)
                    view.setBackgroundResource(android.R.color.transparent)
                } else {
                    selectedSources.add(source.name)
                    view.setBackgroundResource(R.color.itemWithMd)
                }
            }
        })

        // "When ?" part

        def whenCheck = (CheckBox) this.findViewById(R.id.whenActivated)
        whenCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                dateFromRow.setVisibility(isChecked ? View.VISIBLE : View.GONE)
                dateToRow.setVisibility(isChecked ? View.VISIBLE : View.GONE)
            }
        })

        // Type of data
        def tv = (ListView) this.findViewById(R.id.typeView)
        def typeAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, typeOfResources) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent)
                def typeres = typeOfResources.get(position)
                TextView text1 = (TextView) view.findViewById(android.R.id.text1)
                if (selectedTypes.contains(typeres)) {
                    view.setBackgroundResource(R.color.itemWithMd)
                } else {
                    view.setBackgroundResource(android.R.color.transparent)
                }
                text1.setText(typeres)
                return view
            }
        }
        tv.setAdapter(typeAdapter)
        tv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def typeofres = typeOfResources.get(position)
                if (selectedTypes.contains(typeofres)) {
                    selectedTypes.remove(typeofres)
                    view.setBackgroundResource(android.R.color.transparent)
                } else {
                    selectedTypes.add(typeofres)
                    view.setBackgroundResource(R.color.itemWithMd)
                }
            }
        })
        def rotask = new RetrieveCatalogueResourcesTask(this)
        Bundle extras = getIntent().getExtras()
        def georInstanceId = extras.getInt("GeorInstance.id")
        if (! extras) {
            finish()
        }
        Instance ist = GeorInstanceHolder.getInstance().getGeorInstances().get(georInstanceId)
        rotask.execute(GnUtils.getGeonetworkUrl(ist.url))

        // Click on  "search"

        def that = this
        def searchBtn = (Button) this.findViewById(R.id.searchButton)
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                def selSources = that.selectedSources.inject([]) { acc, src ->
                    def currentSource = gnSources.find { it.name == src }
                    acc << currentSource.uuid
                }
                selSources = selSources.join("%20or%20")

                def whenActivated = whenCheck.checked
                def dfp = (DatePicker) that.findViewById(R.id.dateFromPicker)
                def dtp = (DatePicker) that.findViewById(R.id.dateToPicker)
                def dateFrom = "${dfp.getYear()}-" +
                        "${String.format("%02d", dfp.getMonth() + 1)}-" +
                        "${String.format("%02d", dfp.getDayOfMonth())}"
                def dateTo = "${dtp.getYear()}-" +
                        "${String.format("%02d", dtp.getMonth() + 1)}-" +
                        "${String.format("%02d", dtp.getDayOfMonth())}"
                def selTypes = that.selectedTypes.join("%20or%20")
                def freeTxt = (SearchView) that.findViewById(R.id.freeTextSearch)

                def url = GnUtils.getGeonetworkUrl(ist.url)
                url += "srv/eng/q?fast=index&any=${freeTxt.getQuery()}"
                if (selTypes)
                    url += "&type=${selTypes}"
                if ((whenActivated) && (dateFrom))
                    url += "&extFrom=${dateFrom}"
                if ((whenActivated) && (dateTo))
                    url += "&extTo=${dateTo}"
                if (selSources) {
                    url += "&siteId=${selSources}"
                }
                url += "&from=1&to=30&hitsperpage=30"
                def rgnrt = new RetrieveGeonetworkResultsTask(that)
                that.findViewById(R.id.progressBar).setVisibility(View.VISIBLE)
                Log.d(this.getClass().toString(), url)
                rgnrt.execute(url)
            }
        })

    }

    void discard() {
        Toast.makeText(this, "Error occured while loading current catalogue",
                Toast.LENGTH_LONG).show()
        finish()
    }

    void updateView(List<GeoNetworkSource> geoNetworkSources,
                    List<String> resources) {
        if (geoNetworkSources != null) {
            gnSources = geoNetworkSources.clone()
        }
        if (resources != null) {
            typeOfResources = resources.clone()
        }
        def ov = (ListView) this.findViewById(R.id.organisationsView)
        def tv = (ListView) this.findViewById(R.id.typeView)

        def ovAdapter = (ArrayAdapter) ov.getAdapter()
        ovAdapter.clear()
        ovAdapter.addAll(gnSources)
        ovAdapter.notifyDataSetChanged()

        def tvAdapter = (ArrayAdapter) tv.getAdapter()
        tvAdapter.clear()
        tvAdapter.addAll(typeOfResources)
        tvAdapter.notifyDataSetChanged()
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                    this.finish()
                    return true
                }
    }

    void notifyGeonetworkResults(GeoNetworkQuery geoNetworkQuery) {
        this.findViewById(R.id.progressBar).setVisibility(View.GONE)
        if (geoNetworkQuery) {
            def extras = getIntent().getExtras()
            Intent gnra = new Intent(getApplicationContext(), GnResultsActivity.class)
            gnra.putExtra("GeorInstance.id", extras.getInt("GeorInstance.id"))
            startActivityForResult(gnra, RESULT_OK)
        }

    }
}
