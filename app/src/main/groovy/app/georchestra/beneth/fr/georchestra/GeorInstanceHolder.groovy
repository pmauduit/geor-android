package app.georchestra.beneth.fr.georchestra

import fr.beneth.wxslib.georchestra.Instance;

public class GeorInstanceHolder {

    private static final GeorInstanceHolder holder = new GeorInstanceHolder()
    ArrayList<Instance> georInstances = new ArrayList<Instance>()
    public void setGeorInstances(ArrayList<Instance> insts) {
        georInstances = insts
    }

    public ArrayList<Instance> getGeorInstances() {
        return georInstances
    }

    public static GeorInstanceHolder getInstance() { return holder }
}
