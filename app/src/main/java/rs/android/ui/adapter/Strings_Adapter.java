package rs.android.ui.adapter;

public class Strings_Adapter
implements android.widget.ListAdapter
{
	public String[] items;

	public Strings_Adapter(String[] items)
	{
		this.items=items;
	}

	public void registerDataSetObserver(android.database.DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	public void unregisterDataSetObserver(android.database.DataSetObserver p1)
	{
		// TODO: Implement this method
	}

	public int getCount()
	{
		int res=0;

		if (rs.android.Util.NotEmpty(this.items))
			res = this.items.length;
		return res;
	}

	public Object getItem(int i)
	{
		Object res=null;

		if (rs.android.Util.NotEmpty(this.items))
			res = this.items[i];
		return res;
	}

	public long getItemId(int i)
	{
		return i;
	}

	public boolean hasStableIds()
	{
		return true;
	}

	public android.view.View getView(int i, android.view.View v, android.view.ViewGroup p)
	{
		android.widget.TextView res=null;
    int base_padding=30;

		res = rs.android.ui.Util.New_Label(p.getContext(), items[i], null);
    res.setPadding(base_padding, base_padding, base_padding, base_padding);

		return res;
	}

	public int getItemViewType(int p1)
	{
		return 0;
	}

	public int getViewTypeCount()
	{
		return 1;
	}

	public boolean isEmpty()
	{
		return !rs.android.Util.NotEmpty(this.items);
	}

	public boolean isEnabled(int i)
	{
		return true;
	}

	public boolean areAllItemsEnabled()
	{
		return true;
	}
}
