package rs.android.ui.adapter;

public class Tree_Strings_Adapter
extends android.widget.BaseExpandableListAdapter
{
  public static int TAG_KEY_CLASS=1;
  public static int TAG_VALUE_LABEL=1;
  
	public String[] groups;
	public String[][] children;
  public int base_padding=20;
  public android.content.res.Resources.Theme theme;

	public Tree_Strings_Adapter(String[] groups, String[][] children, android.content.res.Resources.Theme theme)
	{
		this.groups=groups;
		this.children=children;
		this.theme = theme;
  }

  public android.graphics.drawable.Drawable Get_Def_Grp_Ind(android.content.res.Resources.Theme theme, boolean isExpanded, int children_count)
  {
    android.graphics.drawable.Drawable res;
    android.graphics.drawable.StateListDrawable grp_ind_drawable;
    
    android.content.res.TypedArray expandableListViewStyle =
            theme.obtainStyledAttributes(new int[]{android.R.attr.expandableListViewStyle});
    android.content.res.TypedArray groupIndicator =
            theme.obtainStyledAttributes(expandableListViewStyle.getResourceId(0,0),new int[]{android.R.attr.groupIndicator});
    grp_ind_drawable = (android.graphics.drawable.StateListDrawable)groupIndicator.getDrawable(0);
    expandableListViewStyle.recycle();
    groupIndicator.recycle();
  
    if (children_count>0)
    {
      if (isExpanded)
        grp_ind_drawable.selectDrawable(0);
      else
        grp_ind_drawable.selectDrawable(1);
      res = grp_ind_drawable.getCurrent();
    }
    else
    {
      res = new android.graphics.drawable.ColorDrawable(0x00000000);
    }
    return res;
  }

  public int getGroupCount()
  {
    int res=0;

    if (rs.android.Util.NotEmpty(this.groups))
      res = this.groups.length;
    return res;
  }

  public int getChildrenCount (int groupPosition)
  {
    int res=0;

    if (rs.android.Util.NotEmpty(this.children) && rs.android.Util.NotEmpty(this.children[groupPosition]))
      res = this.children[groupPosition].length;
    return res;
  }

  public Object getGroup(int groupPosition)
  {
    Object res=null;

    if (rs.android.Util.NotEmpty(this.groups))
      res = this.groups[groupPosition];
    return res;
  }

  public Object getChild(int groupPosition, int childPosition)
  {
    Object res=null;

    if (rs.android.Util.NotEmpty(this.children) && rs.android.Util.NotEmpty(this.children[groupPosition]))
      res = this.children[groupPosition][childPosition];
    return res;
  }

  public long getGroupId (int groupPosition)
  {
    return groupPosition;
  }

  public long getChildId (int groupPosition, int childPosition)
  {
    return childPosition;
  }

	public boolean hasStableIds()
	{
		return false;
	}

  public android.view.View getGroupView (int groupPosition, boolean isExpanded, android.view.View convertView, android.view.ViewGroup parent)
  {
    String label;
    android.widget.TextView label_view=null;
    android.widget.ImageView ind_view;
    android.widget.LinearLayout layout;
    android.graphics.drawable.Drawable grp_ind_drawable;
    android.widget.LinearLayout.LayoutParams params;

    layout=new android.widget.LinearLayout(parent.getContext());
    layout.setPadding(base_padding, base_padding, base_padding, base_padding);

    params = new android.widget.LinearLayout.LayoutParams(60, 60);
    grp_ind_drawable = this.Get_Def_Grp_Ind(this.theme, isExpanded, getChildrenCount(groupPosition));
    ind_view=new android.widget.ImageView(parent.getContext());
    ind_view.setImageDrawable(grp_ind_drawable);
    layout.addView(ind_view, params);

    label=this.groups[groupPosition];
    label_view = rs.android.ui.Util.New_Label(parent.getContext(), label, null);
    label_view.setPadding(base_padding, 0, 0, 0);
    label_view.setTag(TAG_VALUE_LABEL);
    layout.addView(label_view);

    return layout;
  }

	public android.view.View getChildView(int groupPosition, int childPosition, boolean isLastChild, android.view.View convertView, android.view.ViewGroup parent)
	{
	  String label;
    android.widget.TextView res=null;

    label=this.children[groupPosition][childPosition];
    res = rs.android.ui.Util.New_Label(parent.getContext(), label, null);
    res.setPadding(base_padding*2+60, base_padding, base_padding, base_padding);
    res.setBackgroundColor(0xfff36a09);
    
    return res;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition)
  {
    return true;
  }
}
