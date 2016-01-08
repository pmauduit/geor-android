package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeoNetworkQueryHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveImageTask
import fr.beneth.cswlib.geonetwork.GeoNetworkQuery
import fr.beneth.cswlib.metadata.Metadata

public class GnResultsActivity extends AppCompatActivity {

    private GeoNetworkQuery gnQuery
    private RetrieveImageTask retrieveImageTask

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gn_results)

        gnQuery = GeoNetworkQueryHolder.getInstance().getGeoNetworkQuery()
        if (gnQuery) {
            this.setTitle(this.getTitle() + " (${gnQuery.metadatas.size()} MDs)")
        }

        def lv = (ListView) this.findViewById(R.id.mdView)
        retrieveImageTask = new RetrieveImageTask(lv).execute(
            gnQuery.metadatas.collect { if (it.graphicOverviewUrls.size() > 0)
                it.graphicOverviewUrls[0]
            }
        )

        def mdAdapter = new ArrayAdapter<Metadata>(this, R.layout.metadata_list_item,
                R.id.mdTitle, gnQuery.metadatas) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {

                View view = super.getView(position, convertView, parent)
                def md = gnQuery.metadatas.get(position)

                def title = (TextView) view.findViewById(R.id.mdTitle)
                title.setText(md.title)

                def _abstract = (TextView) view.findViewById(R.id.mdAbstract)
                _abstract.setText(md._abstract)

                def keywords = (TextView) view.findViewById(R.id.mdKeywords)
                keywords.setText(md.keywords.join(", "))

                def scopecode = (TextView) view.findViewById(R.id.mdScopeCode)
                scopecode.setText(md.scopeCode)

                def uuid = (TextView) view.findViewById(R.id.mdUuid)
                uuid.setText(md.fileIdentifier)

                def mdOverView = (ImageView) view.findViewById(R.id.mdOverView)
                if (md.graphicOverviewUrls.size() > 0) {
                    def ovUrl = md.graphicOverviewUrls[0]
                    if (retrieveImageTask.hmImages[ovUrl])
                        mdOverView.setImageBitmap(retrieveImageTask.hmImages[ovUrl])
                }
                return view
            }
        }
        lv.setAdapter(mdAdapter)
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle extras = getIntent().getExtras()
                def georInstanceId = extras.getInt("GeorInstance.id")
                def md = gnQuery.metadatas.get(position)
                Intent mdIntent = new Intent(getApplicationContext(), MetadataActivity.class)
                mdIntent.putExtra("md.uuid", md.fileIdentifier)
                mdIntent.putExtra("GeorInstance.id", georInstanceId)
                startActivityForResult(mdIntent, RESULT_OK)
            }
        })
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
