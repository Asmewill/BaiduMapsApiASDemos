package baidumapsdk.demo.asmewill;import android.support.v7.widget.RecyclerView;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.widget.TextView;import com.baidu.mapapi.search.core.PoiInfo;import com.google.gson.Gson;import java.util.ArrayList;import java.util.List;import baidumapsdk.demo.R;import butterknife.BindView;import butterknife.ButterKnife;/** * Created by huangb on 2017/6/8. */public class SearchAddressAdapter extends RecyclerView.Adapter<SearchAddressAdapter.ViewHodler> {    private MapAdapter.onItemClick mOnItemClick;    private List<PoiInfo> mList = new ArrayList<>();    private Gson mGson = new Gson();    @Override    public SearchAddressAdapter.ViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {        return new SearchAddressAdapter.ViewHodler(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_map, parent, false));    }    @Override    public void onBindViewHolder(SearchAddressAdapter.ViewHodler holder, final int position) {        holder.mName.setText(mList.get(position).name);        holder.mAddress.setText(mList.get(position).address);        holder.mTitle.setVisibility(View.GONE);        holder.itemView.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                if (mOnItemClick != null) {                    mOnItemClick.onClic(position, mList.get(position));                }            }        });    }    @Override    public int getItemCount() {        return mList==null?0:mList.size();    }    public void setData(List<PoiInfo> list) {        mList = list;        notifyDataSetChanged();    }    public class ViewHodler extends RecyclerView.ViewHolder {        @BindView(R.id.tv_name)        TextView mName;        @BindView(R.id.tv_address)        TextView mAddress;        @BindView(R.id.tv_title)        TextView mTitle;        public ViewHodler(View itemView) {            super(itemView);            ButterKnife.bind(this, itemView);        }    }    public interface onItemClick {        void onClic(int postion, PoiInfo mPoiInfo);    }    public void setItemClik(MapAdapter.onItemClick mOnItemClick) {        if (mOnItemClick != null) {            this.mOnItemClick = mOnItemClick;        }    }}