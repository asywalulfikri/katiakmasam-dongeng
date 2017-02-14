package ceritadongeng.dongenganakindonesia.dongeng.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ceritadongeng.dongenganakindonesia.dongeng.R;

public class LoadingLayout extends RelativeLayout {

	private TextView mLoadingTv;
	private TextView mActionEmptyTv;
	private ProgressBar mLoadingPb;
	private Button mRetryBtn;
	private ImageView mActionEmptyIv;

	public LoadingLayout(Context context) {
		super(context);

		init(context);
	}

	public LoadingLayout(Context context, AttributeSet attrs) {
		super(context, attrs);

		init(context);
	}

	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context);
	}

	private void init(Context context) {
		LayoutInflater layoutInflater = LayoutInflater.from(context);

		View view = layoutInflater.inflate(R.layout.layout_loading, this);

		mLoadingTv = (TextView) view.findViewById(R.id.tv_loading);
		mActionEmptyTv = (TextView) view.findViewById(R.id.tv_action_empty);
		mLoadingPb = (ProgressBar) view.findViewById(R.id.pb_loading);
		mRetryBtn = (Button) view.findViewById(R.id.btn_retry);
		mActionEmptyIv = (ImageView) view.findViewById(R.id.iv_action_empty);
	}
	
	public void setActionEmptyImageResource(int resId) {
		mActionEmptyIv.setImageResource(resId);
	}
	
	public void setListener(final LoadingLayoutListener listener) {
		mRetryBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onRetryClick();
			}
		});
		
		mActionEmptyIv.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				listener.onActionEmptyClick();
			}
		});
	}
	
	public void show(String text) {
		mActionEmptyTv.setVisibility(View.GONE);
        mActionEmptyIv.setVisibility(View.GONE);
        mRetryBtn.setVisibility(View.GONE);
        
        mLoadingTv.setVisibility(View.VISIBLE);
        mLoadingPb.setVisibility(View.VISIBLE);
        
        if (!text.equals("")) {
        	mLoadingTv.setText(text);
        }
	}
	
	public void show() {
		show("");
	}
	
	public void hide() {
		setVisibility(View.GONE);
	}
	
	public void hideAndRetry(String btnText, String message) {
		mLoadingTv.setVisibility(View.VISIBLE);
		mLoadingPb.setVisibility(View.GONE);
		mRetryBtn.setVisibility(View.VISIBLE);
		
		if (!btnText.equals("")) {
			mRetryBtn.setText(btnText);
		}
		
		mLoadingTv.setText(message);
	}
	
	public void hideAndRetry(String message) {
		hideAndRetry("", message);
	}
	
	public void hideAndEmpty(String message, boolean action, int icon) {
		if (!action) {
			mLoadingTv.setVisibility(View.VISIBLE);
			mLoadingPb.setVisibility(View.GONE);
			
			mLoadingTv.setText(message);
		} else {
			mLoadingTv.setVisibility(View.GONE);
	        mLoadingPb.setVisibility(View.GONE);
			
			mActionEmptyTv.setVisibility(View.VISIBLE);
	        mActionEmptyIv.setVisibility(View.VISIBLE);
	        
	        mActionEmptyTv.setText(message);
	        
	        if (icon != 0) {
	        	mActionEmptyIv.setImageResource(icon);
	        }
		}
	}
	
	public void hideAndEmpty(String message, boolean action) {
		hideAndEmpty(message, action, 0);
	}
	
	public interface LoadingLayoutListener {
		public abstract void onRetryClick();
		public abstract void onActionEmptyClick();
	}

}
