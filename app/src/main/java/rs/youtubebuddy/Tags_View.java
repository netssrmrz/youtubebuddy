package rs.youtubebuddy;
//import android.view.*;

public class Tags_View
extends android.widget.ListView
implements android.widget.Adapter, android.view.View.OnClickListener
{

  @Override
  public void onClick(android.view.View view)
  {
    // TODO: Implement this method
  }

  public Db db;
  
  @Override
  public void registerDataSetObserver(android.database.DataSetObserver p1)
  {
    // TODO: Implement this method
  }

  @Override
  public void unregisterDataSetObserver(android.database.DataSetObserver p1)
  {
    // TODO: Implement this method
  }

  @Override
  public Object getItem(int idx)
  {
    return this.db.objs.get(idx);
  }

  @Override
  public long getItemId(int idx)
  {
    return this.db.objs.get(idx).id;
  }

  @Override
  public boolean hasStableIds()
  {
    return false;
  }

  @Override
  public android.view.View getView
    (int idx, android.view.View old_view, android.view.ViewGroup parent)
  {
    android.widget.LinearLayout main_view;
    android.widget.TextView label;
    
    main_view=new android.widget.LinearLayout(this.getContext());
    main_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    
    label=new android.widget.TextView(this.getContext());
    label.setText(((Tag)this.getItem(idx)).label);
    label.setClickable(true);
    label.setOnClickListener(this);
    
    return null;
  }

  @Override
  public int getItemViewType(int p1)
  {
    return 0;
  }

  @Override
  public int getViewTypeCount()
  {
    return 0;
  }

  @Override
  public boolean isEmpty()
  {
    return this.db.objs.isEmpty();
  }

  public Tags_View(android.content.Context ctx)
  {
    super(ctx);
    
    //this.setAdapter(this);
    this.setVisibility(android.view.View.GONE);
  }
}
