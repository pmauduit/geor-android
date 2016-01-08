package app.georchestra.beneth.fr.georchestra.activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import app.georchestra.beneth.fr.georchestra.BuildConfig
import app.georchestra.beneth.fr.georchestra.R

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)
        setTitle("About geOrchestra")

        def buildDate = (TextView) findViewById(R.id.buildDate)
        def buildUser = (TextView) findViewById(R.id.buildUser)
        def commitId  = (TextView) findViewById(R.id.commitId)

        buildDate.setText(new Date(BuildConfig.TIMESTAMP).toString())
        buildUser.setText(BuildConfig.USER)
        commitId.setText(BuildConfig.COMMIT)
    }
}
