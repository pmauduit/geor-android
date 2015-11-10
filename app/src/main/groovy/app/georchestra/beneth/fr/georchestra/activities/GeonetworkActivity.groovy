package app.georchestra.beneth.fr.georchestra.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import app.georchestra.beneth.fr.georchestra.R

public class GeonetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_geonetwork)

        def ov = (ListView) this.findViewById(R.id.organismView)
        def tv = (ListView) this.findViewById(R.id.typeView)
        List<String> olist = new ArrayList<String>();
        for (int i = 0; i < 40 ; ++i) {
            olist << "organism${i}"
        }
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                olist)
        ov.setAdapter(arrayAdapter)

        def typeArr = [ "dataset", "series", "service" ]
        def typeAdapter = new ArrayAdapter<String>(
                this,
                android.R.layout.simple_list_item_1,
                typeArr)
        tv.setAdapter(typeAdapter)
    }
}
