package rs.youtubebuddy;

public class Tag
implements java.io.Serializable
{
  public Long id;
  public Integer millis;
  public String video_id;
  public String label;
  
  public int Calc_End_Millis(Db db)
  {
    int res=java.lang.Integer.MAX_VALUE;
    java.util.ArrayList<Tag> objs;
    
    objs=(java.util.ArrayList<Tag>)
      db.Select_Objs
        (Tag.class, "select * from Tag where video_id=?", this.video_id);
    if (rs.android.Util.NotEmpty(objs))
    {
      for (Tag t: objs)
      {
        if (t.millis>this.millis && t.millis<res)
          res=t.millis;
      }
    }
    
    if (res==java.lang.Integer.MAX_VALUE)
      res=0;
      
    return res;
  }
}
