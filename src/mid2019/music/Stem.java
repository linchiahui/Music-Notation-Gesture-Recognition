package mid2019.music;

import java.awt.*;
import java.util.Collections;

public class Stem extends Duration {
    public Staff staff;
    public Head.List heads = new Head.List();
    public boolean isUp = true;

    public int id;
    public static int ID = 3;

    @Override
    public String toString() {
        String res = "stem id: " + id + "- ";
        for (Head h : heads) {
            res += h.line + " ";
        }
        return res;
    }

    public Stem(Staff staff, boolean up){
        this.staff = staff;
        this.isUp = up;
        this.id = ID++;
    }

    public void show(Graphics g){
//        System.out.println("show() stem: " + this);
        if(nFlag >= -1 && heads.size() > 0){
            int x = this.x(), h = staff.H(), yh = yFirstHead(), yb = yBeamEnd();
            g.drawLine(x, yh, x, yb);
        }
    }

    public void deleteStem(){
        deleteMass();
//        System.out.println("deleting stem: " + this + "Layers:" + AaMusic.NOTE.size());
    }
    public void setWrongSides(){
        Collections.sort(heads);
        int i, last, next;
        if(isUp) {
            i = heads.size() - 1;
            last = 0;
            next = -1;
        } else{
            i = 0;
            last = heads.size() - 1;
            next = 1;
        }
        Head pH = heads.get(i); // previous head
        pH.wrongSide = false;
        while (i != last){
            i += next;
            Head nH = heads.get(i); // next head
            nH.wrongSide = Math.abs(nH.line - pH.line) <= 1 && !pH.wrongSide;
        }
    }    // stub

    public int yFirstHead(){
        Head h = firstHead();
        return h.staff.yLine(h.line);
    }
    public Head firstHead(){ return heads.get(isUp? heads.size()-1 : 0); }
    public Head lastHead(){return heads.get(isUp? 0: heads.size() - 1);}
    public int x(){Head h = firstHead(); return h.time.x + (isUp? h.w():0);}
    public int yBeamEnd(){  // based on music theory requirement
        Head h = lastHead();
        int line = h.line;
        line += isUp? -7 : 7;
        int flagInk = (nFlag > 2)? 2* nFlag - 2: 0;
        line += isUp? -flagInk : flagInk;
        if((isUp && line > 4) || (isUp && line < 4)){line = 4; }  // 4 is the center line
        return h.staff.yLine(line);
    }
}
