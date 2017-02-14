package ceritadongeng.dongenganakindonesia.dongeng.ui.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ceritadongeng.dongenganakindonesia.dongeng.R;
import ceritadongeng.dongenganakindonesia.dongeng.model.Cerita;

/**
 * Created by asywalulfikri on 12/9/16.
 */

public class CeritaAdapter extends RecyclerView.Adapter<CeritaAdapter.ItemViewHolderCategoryList> {

    private ArrayList<Cerita> mCeritaList ;
    private Context mContext;
    private int lastPosition = -1;
    private MoreCerita mMoreCerita;
    private LikeQuestion mQuestionLike;

    public CeritaAdapter(ArrayList<Cerita> mCeritaListt, Context context) {
        mCeritaList = mCeritaListt;
        mContext = context;
    }

    public void likeQuestion(LikeQuestion question) {
        mQuestionLike = question;
    }
    public void moreCerita(MoreCerita morecerita) {
        mMoreCerita = morecerita;
    }


    @Override
    public void onBindViewHolder(ItemViewHolderCategoryList holder, final int position) {
        Cerita cerita = mCeritaList.get(position);

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


        //setAnimation(holder.cardView, position);
    }

    public void setData(ArrayList<Cerita> data) {
        this.mCeritaList = data;
    }

    @Override
    public int getItemCount() {
       return (mCeritaList.size());
    }

    class ItemViewHolderCategoryList extends RecyclerView.ViewHolder{


        private TextView  tvTitle;
        private ImageView ivCerita;
        private CardView cardView;
        private ImageView ivMore;

        public ItemViewHolderCategoryList(View itemView) {
            super(itemView);

            ivCerita = (ImageView) itemView.findViewById(R.id.iv_cerita);
            tvTitle  = (TextView) itemView.findViewById(R.id.tv_title_cerita);
            cardView = (CardView) itemView.findViewById(R.id.card);
            ivMore   = (ImageView)itemView.findViewById(R.id.iv_more);

        }
    }

    @Override
    public ItemViewHolderCategoryList onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).
                inflate(R.layout.list_item, parent, false);

        return new ItemViewHolderCategoryList(itemView);
    }


    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_up_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }




    public interface OnItemClickListener {
        public abstract void onItemClick(View view, int position);
    }

    public interface LikeQuestion {
        public abstract void OnLikeClickQuestion(View view, int position);
    }
    public interface MoreCerita {
        public abstract void OnMoreClickCerita(View view, int position);
    }
}
