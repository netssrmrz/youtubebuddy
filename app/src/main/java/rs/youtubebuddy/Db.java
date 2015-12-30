package rs.youtubebuddy;

public class Db
{
  public java.util.List<Tag> objs;
  
  public Db()
  {
    this.objs=new java.util.ArrayList<Tag>();
  }
  
  public void Insert(Tag tag)
  {
    tag.id=System.nanoTime();
    this.objs.add(tag);
  }
  
  public long[] Select_All()
  {
    return null;
  }
  
  public Tag Select_Obj(long id)
  {
    return null;
  }
  
  public void Update(Tag tag)
  {
    
  }
  
  public void Delete(long id)
  {
    
  }
}
