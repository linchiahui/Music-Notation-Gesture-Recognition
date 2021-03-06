package mid2019.music;

import mid2019.UC;
import mid2019.reaction.Gesture;
import mid2019.reaction.Mass;
import mid2019.reaction.Reaction;

import java.awt.*;

public class Rest extends Duration {
    public Staff staff;
    public Time time;
    public int line = 4;    // middle line
    public Rest(Staff staff, Time time){
        this.staff = staff;
        this.time = time;
        addReaction(new Reaction("E-E") { // add a flag to the rest
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yMid(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if(x1 > x || x2 < x){return UC.nobid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            @Override
            public void act(Gesture g) { Rest.this.incFlag();}
        });
        addReaction(new Reaction("W-W") { // decrement flags
            @Override
            public int bid(Gesture g) {
                int y = g.vs.yMid(), x1 = g.vs.xL(), x2 = g.vs.xH(), x = Rest.this.time.x;
                if(x1 > x || x2 < x){return UC.nobid;}
                return Math.abs(y - Rest.this.staff.yLine(4));
            }

            @Override
            public void act(Gesture g) { Rest.this.decFlag();}
        });
        addReaction(new Reaction("DOT") {
            @Override
            public int bid(Gesture g) {
                int xR = Rest.this.time.x, yR = Rest.this.y();
                int xD = g.vs.xMid(), yD = g.vs.yMid();
                if(xD < xR || xD > xR + 40 || yD < yR - 40 || yD > yR + 40){return UC.nobid;}
                return Math.abs(xD - xR) + Math.abs(yD - yR);
            }

            @Override
            public void act(Gesture g) {
                Rest.this.cycleDot();
            }
        });
    }

    public int y(){return staff.yLine(line);}
    public void show(Graphics g){
        int h = staff.H(), y = y();
        if(nFlag == -2){ Glyph.REST_W.showAt(g, h, time.x, y);}
        if(nFlag == -1){ Glyph.REST_H.showAt(g, h, time.x, y);}
        if(nFlag == 0){ Glyph.REST_Q.showAt(g, h, time.x, y);}
        if(nFlag == 1){ Glyph.REST_1F.showAt(g, h, time.x, y);}
        if(nFlag == 2){ Glyph.REST_2F.showAt(g, h, time.x, y);}
        if(nFlag == 3){ Glyph.REST_3F.showAt(g, h, time.x, y);}
        if(nFlag == 4){ Glyph.REST_4F.showAt(g, h, time.x, y);}
        int off = 30;
        int sp = 10;
        for(int i = 0; i< nDot; i++){
            g.fillOval(time.x + off + i*sp, y - 3*h/2, h*2/3, h*2/3);
        }
    }
}
