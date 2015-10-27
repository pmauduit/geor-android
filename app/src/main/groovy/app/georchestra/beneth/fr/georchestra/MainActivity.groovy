package app.georchestra.beneth.fr.georchestra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView sv = (ListView) this.findViewById(R.id.wxsServersListView)

        String[] arr = new String[10];

        for (int i = 0 ; i < 10 ; ++i) {
           arr[i] = "Current elem " + i;
        }
        def aa = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arr)
        sv.setAdapter(aa)
    }
}
