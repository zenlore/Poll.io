package cecs343.pollio;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;

public class PollOptionsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EditModel> optionsList;

    public PollOptionsAdapter(Context context, ArrayList<EditModel> optionsList) {
        this.context = context;
        this.optionsList = optionsList;
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return optionsList.size();
    }

    @Override
    public Object getItem(int position) {
        return optionsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.new_poll_options_item, null, true);

            holder.editText = (EditText) convertView.findViewById(R.id.editid);
            holder.deleteBtn = (Button) convertView.findViewById(R.id.delete_btn);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }

        holder.editText.setHint("Option " + String.valueOf(position+1));
        holder.editText.setText(optionsList.get(position).getEditTextValue());
        holder.editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                optionsList.get(position).setEditTextValue(holder.editText.getText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {}
        });

        holder.deleteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                optionsList.remove(position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    private class ViewHolder {
        protected EditText editText;
        protected Button deleteBtn;
    }
}