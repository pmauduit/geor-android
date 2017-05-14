package app.georchestra.beneth.fr.georchestra.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ListView
import android.widget.Toast
import app.georchestra.beneth.fr.georchestra.R
import app.georchestra.beneth.fr.georchestra.holders.GeorInstanceHolder
import app.georchestra.beneth.fr.georchestra.tasks.RetrieveGeorInstancesTask
import app.georchestra.beneth.fr.georchestra.utils.StamenTileSource
import fr.beneth.wxslib.georchestra.Instance
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.ItemizedIconOverlay
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus
import org.osmdroid.views.overlay.OverlayItem

public class MainActivity extends AppCompatActivity {
    RetrieveGeorInstancesTask georInstancesTask = new RetrieveGeorInstancesTask(this)

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setTitle("geOrchestra instances")
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
        MapView mv = (MapView) this.findViewById(R.id.mapView)
        mv.setTileSource(TileSourceFactory.MAPNIK)
        mv.setMultiTouchControls(true)
        mv.getController().setZoom(3)
        mv.getController().setCenter(new GeoPoint(0.0, 0.0))
        this.findViewById(R.id.ProgressBar).setVisibility(View.VISIBLE)
        georInstancesTask.execute()
    }

    public void updateMapData(ArrayList<Instance> georInstances) {
        def mv = (MapView) this.findViewById(R.id.mapView)
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
        def georInstancesOverlay = new ItemizedIconOverlay<OverlayItem>(
                items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        Toast.makeText(getApplicationContext(), "tapped on ${item.title}, " +
                                "long-tap to select this geOrchestra instance",
                                Toast.LENGTH_SHORT).show()
                        return true
                    }
                    @Override
                    boolean onItemLongPress(int index, OverlayItem item) {
                        // the index should be the same in the map list as in the georInstances
                        // holder
                        Intent georInstanceIntent = new Intent(getApplicationContext(),
                                InstanceActivity.class)
                        georInstanceIntent.putExtra("GeorInstance.id", index)
                        startActivityForResult(georInstanceIntent, RESULT_OK)
                    }
                }, this)

        mv.getOverlays().add(georInstancesOverlay)
        mv.invalidate()
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu)
        return true
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_toggle_mode:
                toggleMode(item)
                return true
            case R.id.action_help:
                showHelp()
                return true
            default:
                return super.onOptionsItemSelected(item)
        }
    }

    private void showHelp() {
        Intent about = new Intent(getApplicationContext(),
                AboutActivity.class)
        startActivityForResult(about, RESULT_OK)
    }

    private void toggleMode(MenuItem item) {
        def mv = (MapView) this.findViewById(R.id.mapView)
        ListView lv = (ListView) this.findViewById(R.id.wxsServersListView)
        if (mv.getVisibility() == View.GONE) {
            lv.setVisibility(View.GONE)
            mv.setVisibility(View.VISIBLE)
            item.setIcon(ContextCompat.getDrawable(getApplicationContext(),
                    android.R.drawable.ic_menu_sort_by_size))
        } else {
            mv.setVisibility(View.GONE)
            lv.setVisibility(View.VISIBLE)
            item.setIcon(ContextCompat.getDrawable(getApplicationContext(),
                    android.R.drawable.ic_menu_mapmode))
        }
    }
}


