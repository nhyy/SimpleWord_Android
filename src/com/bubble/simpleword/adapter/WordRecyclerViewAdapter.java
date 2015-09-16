package com.bubble.simpleword.adapter;

import java.util.List;

import android.app.ActionBar.LayoutParams;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.util.Util;

/**
 * <p>Title: MyAdapter</p>
 * <p>Description: </p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-11 下午5:44:53
 */
public class WordRecyclerViewAdapter extends RecyclerView.Adapter<WordRecyclerViewAdapter.BaseViewHolder> {  

	private Context mContext;  
	private List<WordCls> wordsList;  
	private String tableName;
	private OnRecyclerViewItemClickListener onItemClickListener = null;
	private OnRecyclerViewItemLongClickListener onItemLongClickListener = null;
	
	private int viewType = 0;
	public static final int TYPE_VIEW_VERTICAL = 0;
    public static final int TYPE_VIEW_HORIZON = 1;
    
    private int currentPosition;
	
	public WordRecyclerViewAdapter( Context context , String tableName, List<WordCls> wordsList) {  
	    this.mContext = context;  
	    this.tableName = tableName;
	    this.wordsList = wordsList;  
	}  
	
	/**
	 * @date 2015-9-9 下午12:42:41
	 */
	public static interface OnRecyclerViewItemClickListener {
	    void onItemClick(View view , int position, WordCls wordCls);
	}
	
	/**
	 * <p>Title: setOnItemClickListener</p>
	 * <p>Description: </p>
	 * @param listener
	 * @author bubble
	 * @date 2015-9-9 下午12:43:02
	 */
	public void setOnItemClickListener(OnRecyclerViewItemClickListener listener) {
		this.onItemClickListener = listener;
	}
	
	/**@date 2015-9-9 下午7:42:22
	 *
	 */
	public static interface OnRecyclerViewItemLongClickListener {
		void onItemLongClick(View view , int position, WordCls wordCls);
	}
	
	/**
	 * <p>Title: setOnItemLongClickListener</p>
	 * <p>Description: </p>
	 * @param listener
	 * @author bubble
	 * @date 2015-9-9 下午7:45:53
	 */
	public void setOnItemLongClickListener(OnRecyclerViewItemLongClickListener listener) {
		this.onItemLongClickListener = listener;
	}
	
	/**
	 * @date 2015-9-10 下午12:51:34
	 */
	public static interface OnRecyclerViewItemTouchListener {
		void onItemTouch(View view , MotionEvent event, int position, WordCls wordCls);
	}
	
	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-10 下午5:34:48
	 */
	@Override
	public int getItemViewType(int position) {
		return viewType;
	}
	
	/**
	 * <p>Title: setItemViewType</p>
	 * <p>Description: </p>
	 * @param viewType
	 * @author bubble
	 * @date 2015-9-10 下午5:39:29
	 */
	public void setItemViewType(int viewType) {
		this.viewType = viewType;
	}
	
	/**
	 * <p>Title: getCurrentItemPosition</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-9-11 上午12:54:45
	 */
	public int getCurrentItemPosition() {
		return currentPosition;
	}
	
	/**
	 * <p>Title: setCurrentItemPosition</p>
	 * <p>Description: </p>
	 * @param position
	 * @author bubble
	 * @date 2015-9-11 上午12:54:47
	 */
	public void setCurrentItemPosition(int position) {
		this.currentPosition = position;
	}
	
	@Override  
	public BaseViewHolder onCreateViewHolder( ViewGroup parent, int viewType ) {  
		final BaseViewHolder viewHolder;
	    View v ;
	    
	    switch (viewType) {
		case TYPE_VIEW_HORIZON:
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordbook_item_horizontal_cardview, parent, false);  
			viewHolder = new HorizonViewHolder(v);
			break;
		case TYPE_VIEW_VERTICAL:
		default:
			v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wordbook_item_vertical_cardview, parent, false);  
			
			viewHolder = new VerticalViewHolder(v);
			
			v.setOnLongClickListener(new OnLongClickListener() {
				
				@Override
				public boolean onLongClick(View v) {
					if (onItemLongClickListener != null) {
			            onItemLongClickListener.onItemLongClick(v, viewHolder.getLayoutPosition(), (WordCls)v.getTag());
			        }
					return false;
				}
			});
			
			break;
		}
	    
	    v.setLayoutParams(new LayoutParams(Util.getScreenWidth(), LayoutParams.MATCH_PARENT));
	    
	    v.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (onItemClickListener != null) {
		            onItemClickListener.onItemClick(v, viewHolder.getLayoutPosition(), (WordCls)v.getTag());
		        }
			}
		});
	    
	    setCurrentItemPosition(viewHolder.getLayoutPosition());
	    
	    return viewHolder;   
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-10 下午9:04:07
	 */
	@Override  
	public void onBindViewHolder( BaseViewHolder baseViewHolder, int position ) {  
		WordCls wordCls = wordsList.get(position);  
		
		baseViewHolder.itemView.setTag(wordCls);
		
		baseViewHolder.tvWord.setText(wordCls.getWord());  
		baseViewHolder.tvPhonetic.setText(wordCls.getPhonetic());  
		baseViewHolder.tvDefinition.setText(wordCls.getDefinition());  
		
		switch (baseViewHolder.getItemViewType()) {
		case TYPE_VIEW_HORIZON:
			HorizonViewHolder horizonViewHolder = (HorizonViewHolder) baseViewHolder;
			break;
		case TYPE_VIEW_VERTICAL:
		default:
			VerticalViewHolder verticalViewHolder = (VerticalViewHolder) baseViewHolder;
			verticalViewHolder.imgBtnAddDel.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
				}
			});
			break;
		}
	}  

	@Override  
	public int getItemCount()  
	{  
	    // 返回数据总数  
	    return wordsList == null ? 0 : wordsList.size();  
	}  

	
	/**
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param position
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:21:47
	 */
	public void addItem(int position, WordCls wordCls) {
		wordsList.add(position, wordCls);
        notifyItemInserted(position);
    }
	/**
	 * <p>Title: addItem</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:24:11
	 */
	public void addItem(WordCls wordCls) {
		wordsList.add(wordCls);
		int position = wordsList.indexOf(wordCls);
		notifyItemInserted(position);
	}
	
	/**
	 * <p>Title: removeItem</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-9 下午2:20:55
	 */
	public void deleteItem(WordCls wordCls) {
	    int position = wordsList.indexOf(wordCls);
	    wordsList.remove(position);
	    notifyItemRemoved(position);
	}
	
	/**
	 * <p>Title: updateItem</p>
	 * <p>Description: </p>
	 * @param position
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-10 上午12:13:31
	 */
	public void updateItem(int position, WordCls wordCls) {
		wordsList.set(position, wordCls);
		notifyItemChanged(position);
	}
	
	/**
	 * <p>Title: updateList</p>
	 * <p>Description: </p>
	 * @param wordsDataset
	 * @author bubble
	 * @date 2015-9-9 下午2:20:58
	 */
	public void updateDataset(List<WordCls> newList) {
		wordsList = newList;
		notifyDataSetChanged();
	}
	
	/**
	 * @author bubble
	 * @date 2015-9-10 下午8:33:49
	 */
	public class BaseViewHolder extends RecyclerView.ViewHolder {
		public TextView tvWord;  
		public TextView tvPhonetic;  
		public TextView tvDefinition;  
		public BaseViewHolder(View v) {
			super(v);
//			Log.i("BaseViewHolder——getLayoutPosition", String.valueOf(getLayoutPosition()));
		}
	}
	
	/**
	 * @author bubble
	 * @date 2015-9-10
	 */
	public class VerticalViewHolder extends BaseViewHolder {
		public ImageButton imgBtnAddDel;
		
	    public VerticalViewHolder( View v) {  
	        super(v); 
            
			tvWord = (TextView) v.findViewById(R.id.wordcard_vertical_tv_word);  
			tvPhonetic = (TextView) v.findViewById(R.id.wordcard_vertical_tv_phonetic);  
			tvDefinition = (TextView) v.findViewById(R.id.wordcard_vertical_tv_definition);  
			imgBtnAddDel = (ImageButton) v.findViewById(R.id.wordcard_vertical_imgbtn_add_delete);  
	    }
	}  
	
	/**
	 * @author bubble
	 * @date 2015-9-10 下午8:10:28
	 */
	public class HorizonViewHolder extends BaseViewHolder {
		
		public HorizonViewHolder( View v) {  
			super(v); 
			
			tvWord = (TextView) v.findViewById(R.id.wordcard_horizon_tv_word);  
			tvPhonetic = (TextView) v.findViewById(R.id.wordcard_horizon_tv_phonetic);  
			tvDefinition = (TextView) v.findViewById(R.id.wordcard_horizon_tv_definition);  
		}
	}
}  