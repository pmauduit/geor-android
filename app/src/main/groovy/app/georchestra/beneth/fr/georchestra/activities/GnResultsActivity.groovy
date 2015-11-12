package app.georchestra.beneth.fr.georchestra.activities;

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeoNetworkQueryHolder
import fr.beneth.cswlib.geonetwork.GeoNetworkQuery
import fr.beneth.cswlib.metadata.Metadata

public class GnResultsActivity extends AppCompatActivity {

    private GeoNetworkQuery gnQuery

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gn_results)

        gnQuery = GeoNetworkQueryHolder.getInstance().getGeoNetworkQuery()
        if (gnQuery) {
            this.setTitle(this.getTitle() + " (${gnQuery.metadatas.size()} MDs)")
        }
        def lv = (ListView) this.findViewById(R.id.mdView)
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
                return view
            }
        }
        lv.setAdapter(mdAdapter)
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
