package mid2019.music;

import java.awt.Graphics;
import java.util.ArrayList;
import mid2019.UC;
import mid2019.music.Time.List;
import mid2019.reaction.Mass;

public class Sys extends Mass {

  public ArrayList<Staff> staffs = new ArrayList<>();
  public Page page = Page.PAGE;
  public int iSys;
  public Sys.Fmt fmt;
  public Time.List times;

  public Sys(int iSys, Sys.Fmt sysFmt) {
    super("BACK");
    this.iSys = iSys;
    this.fmt = sysFmt;
    this.times = new Time.List(this);
    for (int i = 0; i < fmt.size(); i++) {
      addStaff(new Staff(i, fmt.get(i)));
    }
  }

  public int yTop() {
    return page.sysTop(iSys); //...
  }

  public int yBot() { return staffs.get(staffs.size() - 1).yBottom(); }

  public Time getTime(int x) { return times.getTime(x); }

  public void show(Graphics g) {
    int y = yTop(); // get Top coordinates
    int x = Page.PAGE.margins.left;
    g.drawLine(x, y, x, y + fmt.height()); //draw vertical line to add system
  }

  //helper function to add staff to system and list
  public void addStaff(Staff s) {
    staffs.add(s);
    s.sys = this;
  }

  //---------------------System.Format---------------------
  public static class Fmt extends ArrayList<Staff.Fmt> {

    public int maxH = UC.defaultStaffH;
    public ArrayList<Integer> staffOffsets = new ArrayList<>();

    public int height() {
      int last = size() - 1;
      return staffOffsets.get(last) + get(last).height();
    }

    public void showAt(Graphics g, int y) {
      for (int i = 0; i < size(); i++) {
        get(i).showAt(g, y + staffOffsets.get(i));
      }
    }

  }
}