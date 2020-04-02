package mid2019.graphicsLib;

import java.awt.*;
import java.io.Serializable;
import java.util.Random;

public class G {
  public static Random RANDOM = new Random();

  public static int rnd(int max) {
    return RANDOM.nextInt(max);
  }

  public static Color rndColor() {
    return new Color(rnd(256), rnd(256), rnd(256));
  }

  public static void fillBackground(Graphics g, Color c) {
    g.setColor(c);
    g.fillRect(0, 0, 5000, 5000);
  }

  // Vector
  public static class V implements Serializable{
    public static Transform T = new Transform();
    public int x, y;

    // constructor for V
    public V(int x, int y) {
      this.set(x, y);
    }
    public void add(int x, int y) { this.x += x; this.y += y; }
    public void add(V v) { this.x += v.x; this.y += v.y; }
    public void set(int x, int y) { this.x = x; this.y = y; }
    public void set(V v) {set(v.x, v.y);}

    public int tx() {return x * T.n / T.d + T.dx;}
    public int ty() {return y * T.n / T.d + T.dy;}

    public void blend(V v, int k) {set((k * x + v.x) / (k+1), (k * y + v.y) / (k+1));}


    public void setT(V v) {set(v.tx(), v.ty());}

    public static class Transform {
      public int n, d = 1, dx, dy; //numerator, denominator
      public void set(VS oVS, VS nVS) {
        setScale(oVS.size.x, oVS.size.y, nVS.size.x, nVS.size.y);
        dx = setOff(oVS.loc.x, oVS.size.x, nVS.loc.x, nVS.size.x);
        dy = setOff(oVS.loc.y, oVS.size.y, nVS.loc.y, nVS.size.y);
      }
      public void setScale(int oW, int oH, int nW, int nH) {
        n = (nW > nH) ? nW : nH;
        d = (oW > oH) ? oW : oH;
        d = (d == 0) ? 1 : d;
      }
      public int setOff(int oX, int oW, int nX, int nW) {
        return (-oX - oW/2)* n / d + (nX + nW/2);
      }
    }
  }

  // Vector space
  public static class VS implements Serializable{
    public V loc, size;

    // constructor for VS
    public VS(int x, int y, int w, int h) {
      this.loc = new V(x, y);
      this.size = new V(w, h);
    }

    public void fill(Graphics g, Color c) {
      g.setColor(c);
      g.fillRect(loc.x, loc.y, size.x, size.y);
    }

    // return true if (x, y) is in the VS area
    public boolean hit(int x, int y) {
      return x >= loc.x && x <= (loc.x + size.x) && y >= loc.y && y <= (loc.y + size.y);
    }

    public int xL() {return loc.x;}
    public int xH() {return loc.x + size.x;}
    public int xMid() {return (loc.x + loc.x + size.x) / 2;}
    public int yL() {return loc.y;}
    public int yH() {return loc.y + size.y;}
    public int yMid() {return (loc.y + loc.y + size.y) / 2;}

  }

  public static class LoHi implements Serializable{
    public int lo, hi;
    public LoHi(int lo, int hi) {this.lo = lo; this.hi = hi;}
    public void set(int v) {lo = v; hi = v;}
    public void add(int v) {if (v < lo) {lo = v;} if (v > hi) {hi = v;}}
    public int size() {return hi - lo;}
    //TODO
    public int constrain(int v) {if(v < lo) {return lo;} if(v > hi) {return hi;} return v;}
  }

  // bounding box
  public static class BBox implements Serializable{
    public LoHi h, v;  //horizontal and vertical
    public BBox() { h = new LoHi(0,0); v = new LoHi(0, 0);}
    public void set(int x, int y) {h.set(x); v.set(y);}
    public void add(int x, int y) {h.add(x); v.add(y);}
    public void add(V v) {add(v.x, v.y);}
    public void draw(Graphics g) {g.drawRect(h.lo, v.lo, h.size(), v.size());}
    public G.VS getVS() {return new VS(h.lo, v.lo, h.size(), v.size());}
  }

  //polyline
  public static class PL implements Serializable{
    public V[] points;
    public PL(int n) {
      points = new V[n];
      for(int i = 0; i < n; i++) {points[i] = new V(0,0);}
    }
    public int size(){return points.length;}
    public void drawN(Graphics g, int n){
      for(int i = 1; i < n; i++) {g.drawLine(points[i-1].x, points[i-1].y, points[i].x, points[i].y);}
      drawNDot(g, n);
    }
    public void drawNDot(Graphics g, int n) {
      g.setColor(Color.BLUE);
      for(int i = 0; i < n; i++) {
        g.drawOval(points[i].x - 3, points[i].y - 3, 6,6);
      }
    }
    public void draw(Graphics g) {drawN(g, points.length);}

    public void transform() {
      for(int i = 0; i < points.length; i++) {points[i].setT(points[i]);}
    }
  }

}

