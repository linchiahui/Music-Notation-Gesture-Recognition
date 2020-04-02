package mid2019.reaction;

import mid2019.I;

import java.awt.*;

public abstract class Mass extends Reaction.List implements I.Show{
    public Layer layer;
    public Mass (String layerName){
        this.layer = Layer.byName.get(layerName);
        if(layer != null){
            layer.add(this);
        }else{
            System.out.println("Bad layer name: " + layerName);
        }
    }
    public void deleteMass(){
        clearAll();
//        System.out.println("remove from layer: " + this);
//        System.out.println("Layer now" + layer);
//        layer.remove(this);
        layer.betterRemove(this);
//        System.out.println("Layer after" + layer);
    }
    // do nothing
    public void show(Graphics g){}
}
