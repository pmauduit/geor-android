package app.georchestra.beneth.fr.georchestra.activities;

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import app.georchestra.beneth.fr.georchestra.R

public class GnResultsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gn_results)



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
