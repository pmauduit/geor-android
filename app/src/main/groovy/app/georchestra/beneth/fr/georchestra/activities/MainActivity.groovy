package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveGeorInstancesTask
import fr.beneth.wxslib.georchestra.Instance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView

public class MainActivity extends AppCompatActivity {

    RetrieveGeorInstancesTask georInstancesTask = new RetrieveGeorInstancesTask(this)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("List of geOrchestra instances")
        ListView lv = (ListView) this.findViewById(R.id.wxsServersListView)

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                def georInstances = GeorInstanceHolder.getInstance().getGeorInstances()
                if (georInstances == null)
                    return
                Instance i = georInstances.get(position)
                Intent georInstanceIntent = new Intent(getApplicationContext(), InstanceActivity.class)
                georInstanceIntent.putExtra("GeorInstance.id", position)
                startActivityForResult(georInstanceIntent, RESULT_OK)
            }
        })

        Button sw = (Button) this.findViewById(R.id.switchMapButton)
        MapView mv = (MapView) this.findViewById(R.id.mapView)
        mv.setTileSource(TileSourceFactory.MAPQUESTOSM)

        sw.setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                if (mv.getVisibility() == View.GONE) {
                    lv.setVisibility(View.GONE)
                    mv.setVisibility(View.VISIBLE)
                    sw.setText("Switch to list view")
                } else {
                    mv.setVisibility(View.GONE)
                    lv.setVisibility(View.VISIBLE)
                    sw.setText("Switch to map view")
                }
            }
        })
        this.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE)
        georInstancesTask.execute()
    }
}

