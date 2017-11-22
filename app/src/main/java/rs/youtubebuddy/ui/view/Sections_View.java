package rs.youtubebuddy.ui.view;
//import android.widget.*;
//import android.widget.*;
//import android.view.*;

public class Sections_View
extends android.widget.LinearLayout
implements
  android.view.View.OnClickListener,
  android.widget.ListAdapter,
  android.content.DialogInterface.OnClickListener
{
  public static final int ID_DELETE=1000;
  public static final int ID_EDIT=2000;
  public static final int ID_SET=3000;
  
  public static final int COLOR_RED=0xff884444;
  public static final int COLOR_GREEN=0xff339933;
  public static final int COLOR_BLUE=0xff555599;
  public static final int COLOR_YELLOW=0xff336633;
  public static final int COLOR_PURPLE=0xff884488;
  public static final int COLOR_CYAN=0xff555577;
  public static final int COLOR_GREY=0xff444444;
  public static final int COLOR_WHITE=0xffdddddd;
  
  public Controls_View ctrls_view;
  public rs.youtubebuddy.Db db;
  public java.util.ArrayList<rs.youtubebuddy.Tag> objs;
  public android.database.DataSetObserver observer;
  public android.app.AlertDialog edit_dialog;
  public android.widget.EditText label_input;
  public android.widget.ListView list_view;
  public android.widget.ImageButton set_button;
  public int label_count;
  
  public Sections_View(android.content.Context ctx, rs.youtubebuddy.Db db)
  {
    super(ctx);
    
    this.db=db;
    this.edit_dialog=this.Build_Edit_Dialog();
    
    this.set_button=new android.widget.ImageButton(ctx);
    set_button.setId(ID_SET);
    //set_button.setText("Mark Tag");
    //set_button.setTextColor(COLOR_WHITE);
    set_button.setImageResource(rs.youtubebuddy.R.drawable.ic_add_white_24dp);
    rs.android.ui.Util.Set_Button_Colour(set_button, COLOR_YELLOW);
    set_button.setOnClickListener(this);
    
    this.list_view=new android.widget.ListView(ctx);
    this.list_view.setAdapter(this);
    this.list_view.setLayoutTransition(new android.animation.LayoutTransition());
    
    this.setOrientation(android.widget.LinearLayout.VERTICAL);
    this.addView(set_button);
    this.addView(this.list_view);
  }
  
  public android.app.AlertDialog Build_Edit_Dialog()
  {
    label_input = new android.widget.EditText(this.getContext());

    android.app.AlertDialog.Builder builder = 
      new android.app.AlertDialog.Builder(this.getContext());
    builder.setMessage("Enter a label for this tag.");
    builder.setTitle("Tag Label");
    builder.setView(label_input);
    builder.setPositiveButton("Ok", this);
    builder.setNegativeButton("Cancel", null);
    return builder.create();
  }
  
  public void Set_Tag()
  {
    rs.youtubebuddy.Tag tag;

    if (this.ctrls_view.player!=null)
    {
      this.label_count++;
      
      tag=new rs.youtubebuddy.Tag();
      tag.millis=this.ctrls_view.player.getCurrentTimeMillis();
      tag.video_id=this.ctrls_view.Get_Video_Id();
      tag.label="Tag "+this.label_count;

      this.db.Insert(tag);
      this.Notify_Dataset_Changed();
    }
  }
  
  @Override
  public void onClick(android.view.View view)
  {
    rs.youtubebuddy.Tag tag;
    int end_millis;
    
    android.util.Log.d("Tags_View.onClick", view.getClass().getName());
    if (view.getClass().equals(android.widget.TextView.class))
    {
      //android.util.Log.d("Tags_View.onClick", view.getClass().getName());
      tag=(rs.youtubebuddy.Tag)view.getTag();
      
      this.ctrls_view.Stop();
      this.ctrls_view.Set_Start(tag.millis);
      end_millis=tag.Calc_End_Millis(this.db);
      if (end_millis==0)
        end_millis=this.ctrls_view.player.getDurationMillis();
      this.ctrls_view.Set_End(end_millis);
      this.ctrls_view.Seek_Start();
      this.ctrls_view.Play();
    }
    else if (view instanceof android.widget.ImageButton && view.getId()-ID_DELETE<1000)
    {
      //android.util.Log.d("Tags_View.onClick", "del");
      tag=(rs.youtubebuddy.Tag)view.getTag();
      this.db.Delete(tag);
      this.Notify_Dataset_Changed();
    }
    else if(view instanceof android.widget.ImageButton && view.getId()-ID_EDIT<1000)
    {
      //android.util.Log.d("Tags_View.onClick", "edit");
      tag=(rs.youtubebuddy.Tag)view.getTag();
      this.label_input.setTag(tag);
      this.label_input.setText(tag.label);
      this.edit_dialog.show();
    }
    else if (view instanceof android.widget.ImageButton && view.getId()==ID_SET)
      this.Set_Tag();
  }
  
  public void onClick(android.content.DialogInterface dialog, int id) 
  {
    rs.youtubebuddy.Tag tag;
    
    tag=(rs.youtubebuddy.Tag)label_input.getTag();
    tag.label=this.label_input.getText().toString();
    this.db.Update(tag);
    this.Notify_Dataset_Changed();
  }
  
  @Override
  public int getCount()
  {
    int res=0;
    
    if (rs.android.Util.NotEmpty(this.objs))
      res = this.objs.size();
    return res;
  }

  @Override
  public boolean areAllItemsEnabled()
  {
    return true;
  }

  @Override
  public boolean isEnabled(int idx)
  {
    return true;
  }

  @Override
  public void registerDataSetObserver(android.database.DataSetObserver o)
  {
    this.observer=o;
  }

  @Override
  public void unregisterDataSetObserver(android.database.DataSetObserver o)
  {
    this.observer=null;
  }

  @Override
  public Object getItem(int idx)
  {
    return this.objs.get(idx);
  }

  @Override
  public long getItemId(int idx)
  {
    return this.objs.get(idx).id;
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
    android.widget.ImageButton edit_button, del_button;
    rs.youtubebuddy.Tag tag;
    android.widget.LinearLayout.LayoutParams lw, lw2;

    tag=(rs.youtubebuddy.Tag)this.getItem(idx);

    lw=new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 1);
    lw2=new android.widget.LinearLayout.LayoutParams(
      0, android.widget.LinearLayout.LayoutParams.MATCH_PARENT, 3);
      
    label=new android.widget.TextView(this.getContext());
    label.setTag(tag);
    label.setText(tag.label);
    label.setClickable(true);
    label.setOnClickListener(this);
    label.setPadding(20, 0, 10, 0);
    label.setTextSize(15);
    label.setTextAlignment(android.widget.TextView.TEXT_ALIGNMENT_GRAVITY);
    label.setGravity(android.view.Gravity.CENTER_VERTICAL);
    label.setTextColor(COLOR_WHITE);
    
    edit_button=new android.widget.ImageButton(this.getContext());
    edit_button.setTag(tag);
    //edit_button.setText("=");
    edit_button.setImageResource(rs.youtubebuddy.R.drawable.ic_create_white_24dp);
    edit_button.setId(idx+2000);
    edit_button.setOnClickListener(this);
    rs.android.ui.Util.Set_Button_Colour(edit_button, COLOR_BLUE);
    
    del_button=new android.widget.ImageButton(this.getContext());
    del_button.setTag(tag);
    //del_button.setText("-");
    del_button.setImageResource(rs.youtubebuddy.R.drawable.ic_clear_white_24dp);
    del_button.setId(idx+1000);
    del_button.setOnClickListener(this);
    rs.android.ui.Util.Set_Button_Colour(del_button, COLOR_RED);
    
    main_view=new android.widget.LinearLayout(this.getContext());
    main_view.setOrientation(android.widget.LinearLayout.HORIZONTAL);
    main_view.addView(label, lw2);
    main_view.addView(edit_button, lw);
    main_view.addView(del_button, lw);

    return main_view;
  }

  @Override
  public int getItemViewType(int idx)
  {
    return 1;
  }

  @Override
  public int getViewTypeCount()
  {
    return 1;
  }

  @Override
  public boolean isEmpty()
  {
    return this.objs.isEmpty();
  }
  
  public void Notify_Dataset_Changed()
  {
    if (this.ctrls_view!=null && this.ctrls_view.open_url!=null)
    {
      this.objs=(java.util.ArrayList<rs.youtubebuddy.Tag>)
        this.db.Select_Objs
          (rs.youtubebuddy.Tag.class, "select * from Tag where video_id=? order by millis asc",
            this.ctrls_view.Get_Video_Id());
      if (this.observer!=null)
        this.observer.onChanged();
    }
  }
}
