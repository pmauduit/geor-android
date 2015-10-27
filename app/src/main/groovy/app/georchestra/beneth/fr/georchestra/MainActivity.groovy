package app.georchestra.beneth.fr.georchestra

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ListView
import android.widget.TextView

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView sv = (ListView) this.findViewById(R.id.wxsServersListView)
        for (int i = 0 ; i < 10 ; ++i) {
            TextView tv = new TextView(this)
            tv.setText("Current elem " + i)
            sv.addFooterView(tv)
        }
    }
}
