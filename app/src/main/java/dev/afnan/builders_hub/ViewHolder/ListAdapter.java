package dev.afnan.builders_hub.ViewHolder;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import dev.afnan.builders_hub.R;


public class ListAdapter extends BaseAdapter {

    Context context;
    String[] itemName;
    String[] listItems;
    int[] imageIDs;

    LayoutInflater inflater;

    public ListAdapter(Context context, int[] imageIDs, String[] listNames, String[] listItems) {
        this.context = context;
        this.itemName = listNames;
        this.listItems = listItems;
        this.imageIDs = imageIDs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return imageIDs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        convertView = inflater.inflate(R.layout.user_profile_single_row, null);

        ImageView icon = convertView.findViewById(R.id.user_icon);
        TextView userNameHeading = convertView.findViewById(R.id.user_txtNameHeading);
        TextView userName = convertView.findViewById(R.id.user_txtName);

        icon.setImageResource(imageIDs[position]);
        userNameHeading.setText(itemName[position]);
        userName.setText(listItems[position]);

        return convertView;
    }
}




