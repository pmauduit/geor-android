package app.georchestra.beneth.fr.georchestra;

import android.app.Activity
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ImageView
import android.widget.TextView
import fr.beneth.wxslib.georchestra.Instance
import org.codehaus.groovy.util.StringUtil;

/**
 * Created by pmauduit on 10/28/15.
 */
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
    }
}
