package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveGeorInstancesTask
import fr.beneth.wxslib.georchestra.Instance
import org.osmdroid.DefaultResourceProxyImpl
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.OverlayItem

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
        mv.setTileSource(TileSourceFactory.MAPNIK)
        mv.setMultiTouchControls(true)

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
    public void updateMapData(ArrayList<Instance> georInstances) {
        def mv = (MapView) this.findViewById(R.id.mapView)
        def mctrl = mv.getController()
        def items = new ArrayList<OverlayItem>()
        georInstances.each {
            if (it.lat == 0.0 && it.lon == 0.0) {
                return
            }
            def pos = new GeoPoint((int) (it.lat * 1E6), (int) (it.lon * 1E6))
            def olItem = new OverlayItem(it.title, it._abstract, pos)
            def gMarker = ResourcesCompat.getDrawable(getResources(), R.drawable.marker, null)
            olItem.setMarker(gMarker)
            items << olItem
        }
        def mrp = new DefaultResourceProxyImpl(getApplicationContext())
        def georInstancesOverlay = new ItemizedIconOverlay<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getApplicationContext(),
                                "tapped on ${item.title}", Toast.LENGTH_SHORT).show()
                        return true
                    }
                    @Override
                    boolean onItemLongPress(int index, OverlayItem item) {
                        return false
                    }
                }, mrp)

        mv.getOverlays().add(georInstancesOverlay)
        mv.invalidate()
    }

}


