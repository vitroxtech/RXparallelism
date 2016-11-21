package squaring.vitrox.rxparallel.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import squaring.vitrox.rxparallel.Model.GithubRepository;
import squaring.vitrox.rxparallel.R;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {

    List<GithubRepository> mItems;
    Context mContext;

    public ListAdapter(Context context) {
        super();
        this.mContext=context;
        mItems = new ArrayList<>();
    }

    public void addData(GithubRepository dataObj) {
        mItems.add(dataObj);
        notifyDataSetChanged();
    }

    public void clear() {
        mItems.clear();
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_row, viewGroup, false);

        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        viewHolder.bind(mItems.get(i));
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView dateofCreation;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.itemTitle);
            dateofCreation = (TextView) itemView.findViewById(R.id.itemDate);
        }

        public void bind(final GithubRepository item) {
            title.setText(item.getName());

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("yyyy-MM-dd");
            Date d = null;
            try {
                d = sdf.parse(item.getCreated_at());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formattedTime = output.format(d);
            dateofCreation.setText(formattedTime);
        }
    }


}