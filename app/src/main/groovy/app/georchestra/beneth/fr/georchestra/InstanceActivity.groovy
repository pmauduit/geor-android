package app.georchestra.beneth.fr.georchestra;

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import fr.beneth.wxslib.georchestra.Instance
import org.codehaus.groovy.util.StringUtil;

public class InstanceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instance);
        Bundle extras = getIntent().getExtras()
        int pos = extras.getInt("GeorInstance.id")
        Instance currentInst = GeorInstanceHolder.getInstance().getGeorInstances().get(pos)

        this.setTitle(currentInst.title)
        if (currentInst.logo_url) {
            ImageView logoView = this.findViewById(R.id.LogoView)
            def rit = new RetrieveImageTask(logoView)
            rit.execute(currentInst.logo_url)
        }

        // Hook for GeoServer button
        this.findViewById(R.id.gsButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Intent gsActivity = new Intent(getApplicationContext(), gsActivity.class)
                gsActivity.putExtra("GeorInstance.id", pos)
                startActivity(gsActivity)
            }
        })
        // Hook for GeoNetwork button
        this.findViewById(R.id.gnButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Toast.makeText(getApplicationContext(), "unimplemented",
                        Toast.LENGTH_LONG).show();
            }
        })
        // Hook for viewer button
        this.findViewById(R.id.viewerButton).setOnClickListener(new View.OnClickListener() {
            @Override
            void onClick(View v) {
                Toast.makeText(getApplicationContext(), "unimplemented",
                        Toast.LENGTH_LONG).show();
            }
        })
    }
}
