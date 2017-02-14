package ceritadongeng.dongenganakindonesia.dongeng.ui.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;

public class CeritaAdapter3 extends BaseAdapter {

    private LayoutInflater mInflater;
    private Typeface mFont;
    private Context mContext;

    private LikeQuestion mQuestionLike;
    private MoreCerita mMoreCerita;
    private ViewHolder holder;
    private ArrayList<Cerita> mWisataList = new ArrayList<Cerita>();
    private PrettyTime mPrettyTime;

    private DataValueOpen mDataValueOpen;
    private ArrayList<Cerita> mDataList;
    String  useridd;

    public CeritaAdapter3(Context context) {
        init(context);
    }

    private void init(Context context) {
        mContext = context;

        if (context != null) {

            mInflater = LayoutInflater.from(context);
            mPrettyTime = new PrettyTime();


        }

    }

    public void setOpenData(DataValueOpen open) {
        mDataValueOpen = open;
    }

    public void setData(ArrayList<Cerita> list) {
        mDataList = list;
    }

    public void likeQuestion(LikeQuestion question) {
        mQuestionLike = question;
    }
    public void moreCerita(MoreCerita morecerita) {
        mMoreCerita = morecerita;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
//		return (mDataList == null) ? 0 : (mDataList.size() + 1);
        return (mDataList == null) ? 0 : mDataList.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @SuppressLint("InflateParams")
    @Override
    public View getView(final int position, View itemView, ViewGroup parent) {

        if (itemView == null) {
            itemView = mInflater.inflate(R.layout.list_item, null);

            holder = new ViewHolder();
            holder.ivCerita = (ImageView) itemView.findViewById(R.id.iv_cerita);
            holder.tvTitle  = (TextView) itemView.findViewById(R.id.tv_title_cerita);
            holder.cardView = (CardView) itemView.findViewById(R.id.card);
            holder.ivMore   = (ImageView)itemView.findViewById(R.id.iv_more);

            itemView.setTag(holder);
        } else {
            holder = (ViewHolder) itemView.getTag();
        }

        final Cerita cerita= mDataList.get(position);

        holder.tvTitle.setText(cerita.title);
        Picasso.with(mContext) //
                .load((cerita.image1.equals("")) ? null : cerita.image1) //
                .placeholder(R.drawable.no_image) //
                .error(R.drawable.no_image)
                .into(holder.ivCerita);



        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mQuestionLike.OnLikeClickQuestion(v, position);
            }
        });

        holder.ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mMoreCerita.OnMoreClickCerita(v, position);
            }
        });


        return itemView;
    }


    public interface DataValueOpen {
        public void setValueOpen(String id, String value);
    }

    public interface LikeQuestion {
        public abstract void OnLikeClickQuestion(View view, int position);
    }

    public interface MoreCerita {
        public abstract void OnMoreClickCerita(View view, int position);
    }

    static class ViewHolder {

        private TextView  tvTitle;
        private ImageView ivCerita;
        private CardView cardView;
        private ImageView ivMore;
    }
    private static final Drawable getDrawableModify(Context context, int id) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= 21) {
            return ContextCompat.getDrawable(context, id);
        } else {
            return context.getResources().getDrawable(id);
        }
    }
}