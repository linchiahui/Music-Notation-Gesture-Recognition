package mid2019.music;

import java.awt.Graphics;
import java.util.ArrayList;

import mid2019.UC;
import mid2019.reaction.Gesture;
import mid2019.reaction.Mass;
import mid2019.reaction.Reaction;

public class Head extends Mass implements Comparable<Head>{
  public Staff staff;
  public int line;
  public Time time;
  public Stem stem = null;  // could be null
  public boolean wrongSide = false;
  public  Glyph forcedGlyph = null;  // if not null, use another system of notation


  public Head(Staff staff, int x, int y) {
    super("NOTE");
    this.staff = staff;
    this.time = staff.sys.getTime(x);
    this.line = staff.lineOfY(y);
    time.heads.add(this);
    addReaction(new Reaction( "S-S") {
      @Override
      public int bid(Gesture g) {
        int x= g.vs.xMid(), y1 = g.vs.yL(), y2 = g.vs.yH();
        int w = Head.this.w(), hy = Head.this.y();
        if(y1 > y || y2< y){ return UC.nobid; }
        int hl = Head.this.time.x, hr = hl + w;
        if(x < hl - w || x> hr + w){return UC.nobid;}
        if(x < hl + w/2){
          return hl - x;
        }else if (x > hr - w/2){
          return x - hr;
        }else{
          return UC.nobid;
        }
      }

      @Override
      public void act(Gesture g) {
        int x = g.vs.xMid(), y1 = g.vs.yL(), y2 = g.vs.yH();
        Staff staff = Head.this.staff;
        Time t = Head.this.time;
        int w = Head.this.w();
        boolean up = x > (t.x + w/2);
        if(Head.this.stem == null){
          t.stemHeads(staff, up, y1, y2);
        }else{
          t.unstemHeads(y1, y2);
        }

      }
    });
  }
  public int w(){return 24*staff.H() / 10;}

  public void show(Graphics g) {
    int H = staff.H();
    (forcedGlyph != null ? forcedGlyph: normalGlyph()).showAt(g, H, x(), y());
  }

  public int y() {return staff.yLine(line);}
  public int x() {
    int res = time.x;
    if(wrongSide){ res += (stem != null && stem.isUp) ? w() : -w(); }
    return res;
  }
  public Glyph normalGlyph(){
    if (stem == null) {return Glyph.HEAD_Q;}
    if (stem.nFlag == -1) {return  Glyph.HEAD_HALF;}
    if (stem.nFlag == -2) {return  Glyph.HEAD_W;}
    return Glyph.HEAD_Q;
  }
  public  void deleteHead(){ time.heads.remove(this); }  //TODO
  public void joinStem(Stem s){
    if (stem != null){unstem();}
    s.heads.add(this);
    stem = s;
  }

  public void unstem(){
    if(stem != null){
      System.out.println("head unstem:" + stem.id);
      if (stem.heads.size() == 0){
        stem.deleteStem();
      }
      stem.heads.remove(this);
      stem = null;
      wrongSide = false;
    }
  }

  @Override
  public int compareTo(Head h) {
    return (staff.iStaff != h.staff.iStaff)? staff.iStaff - h.staff.iStaff : line - h.line;
  }

  // -----------------LIST--------------------
  public static class  List extends ArrayList<Head>{}

}
