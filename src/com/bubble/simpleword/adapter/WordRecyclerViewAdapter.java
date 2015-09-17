package com.bubble.simpleword.adapter;

import java.net.URL;
import java.util.List;

import net.htmlparser.jericho.Source;

import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.bubble.simpleword.R;
import com.bubble.simpleword.db.WordCls;
import com.bubble.simpleword.db.WordsManager;
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
	public static final int TYPE_VIEW_VERTICAL = LinearLayoutManager.HORIZONTAL;
    public static final int TYPE_VIEW_HORIZON = LinearLayoutManager.VERTICAL;
    
    private int currentPosition;
    
    private WordCls wordCls;
    
    boolean isJsonSucceed;
    boolean isLoadedSucceed = false;
	
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
	 * @param viewType LinearLayoutManager.HORIZONTAL or LinearLayoutManager.VERTICAL
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
	    
	    
	    return viewHolder;   
	}

	/**
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-9-10 下午9:04:07
	 */
	@Override  
	public void onBindViewHolder( BaseViewHolder baseViewHolder, final int position ) {  
		setCurrentItemPosition(position);
		
		wordCls = wordsList.get(position);  
		setWordCls(wordCls);
		
		baseViewHolder.itemView.setTag(wordCls);
		
		baseViewHolder.tvWord.setText(wordCls.getWord());  
		baseViewHolder.tvPhonetic.setText(wordCls.getPhonetic());  
		baseViewHolder.tvDefinition.setText(wordCls.getDefinition());  
		
		switch (baseViewHolder.getItemViewType()) {
		case TYPE_VIEW_HORIZON:
			final HorizonViewHolder horizonViewHolder = (HorizonViewHolder) baseViewHolder;
			
			if ( wordCls.isLoaded() ) {
				horizonViewHolder.tvHint.setVisibility(View.INVISIBLE);
				horizonViewHolder.tvDefEN.setText(wordCls.getDefinitionEN());
				horizonViewHolder.tvDefCN.setText(wordCls.getDefinitionCN());
			} else {
				horizonViewHolder.tvHint.setVisibility(View.VISIBLE);
			}
			
			horizonViewHolder.tvHint.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					switch (v.getId()) {
					case R.id.wordbook_horizon_tv_hint:
						if ( ! wordCls.isLoaded() ) {
							Handler handler = new Handler(){
								
								@Override
								public void handleMessage(Message msg) {
									switch (msg.what) {
									case 0:
										horizonViewHolder.tvHint.setText("数据获取失败，请重试");
									case 1:
										horizonViewHolder.tvHint.setVisibility(View.INVISIBLE);
										break;
									default:
										break;
									}
								}
								
							};
							ParseJsonTask parseJsonTask = new ParseJsonTask(horizonViewHolder, wordCls, position, handler);
							parseJsonTask.execute();
						} 
						break;

					default:
						break;
					}
				}
			});
			
			
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
	
	/**
	 * @date 2015-9-17 上午12:37:04
	 * @author bubble
	 */
	class ParseJsonTask extends AsyncTask<String, Void, Boolean> {
		HorizonViewHolder horizonViewHolder;
		WordCls wordCls;
		int position;
		
		String definitionEN;
		String definitionCN;
		String audioUrlUS;
		
		Handler handler;
		
        public ParseJsonTask(HorizonViewHolder horizonViewHolder,
				WordCls wordCls, int position, Handler handler) {
			super();
			this.horizonViewHolder = horizonViewHolder;
			this.wordCls = wordCls;
			this.position = position;
			this.handler = handler;
		}

		@Override
        protected void onPreExecute() {
        	horizonViewHolder.progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean result) {
        	Message msg = handler.obtainMessage();
        	
        	horizonViewHolder.progressBar.setVisibility(View.INVISIBLE);
            if (result) {
            	wordCls.setDefinitionEN(definitionEN);
            	wordCls.setDefinitionCN(definitionCN);
            	wordCls.setAudioUrlUS(audioUrlUS);
            	wordCls.setLoaded(1);
            	WordsManager.addWordLoadInfo(tableName, wordCls);
            	updateItem(position, wordCls);
            	
            	msg.what = 1;
            }else {
            	msg.what = 0;
            	Log.i(wordCls.getWord(), "获取失败");
            }
            
            handler.sendMessage(msg);
            
            super.onPostExecute(result);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            String path = "https://api.shanbay.com/bdc/search/?word=" + wordCls.getWord();
            try {
            	URL url = new URL(path);
                Source source = new Source(url.openConnection());	//jericho-html-3.1.jar
                String jsonstr = source.toString();
                 
                JSONObject jsonObj = new JSONObject(jsonstr);
                 
                JSONObject data = jsonObj.getJSONObject("data");
                 
                JSONObject defEN = data.getJSONObject("en_definition");
                definitionEN = defEN.getString("pos") + "." + defEN.getString("defn"); 
                 
                JSONObject defCN = data.getJSONObject("cn_definition");
                definitionCN = defCN.getString("pos") + defCN.getString("defn"); 
                 
                audioUrlUS = data.getString("us_audio");
                 
                return true;
            } catch (Exception e) {
            	Toast.makeText(mContext, "获取数据失败", Toast.LENGTH_SHORT).show();
                return false;
            }
    	}
	}
	
	/**
	 * <p>Title: getWordCls</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-9-16 下午4:39:01
	 */
	public WordCls getWordCls() {
		return this.wordCls;
	}

	/**
	 * <p>Title: setWordCls</p>
	 * <p>Description: </p>
	 * @param wordCls
	 * @author bubble
	 * @date 2015-9-16 下午4:39:03
	 */
	public void setWordCls(WordCls wordCls) {
		this.wordCls = wordCls;
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
		TextView tvHint;
		LinearLayout progressBar;
		
		TextView tvDefEN;
		TextView tvDefCN;
		LinearLayout llCompleteWord;
		
		public HorizonViewHolder( View v) {  
			super(v); 
			
			tvWord = (TextView) v.findViewById(R.id.wordcard_horizon_tv_word);  
			tvPhonetic = (TextView) v.findViewById(R.id.wordcard_horizon_tv_phonetic);  
			tvDefinition = (TextView) v.findViewById(R.id.wordcard_horizon_tv_definition);  
			
			tvHint = (TextView) v.findViewById(R.id.wordbook_horizon_tv_hint);
			progressBar = (LinearLayout) v.findViewById(R.id.wordbook_horizon_progressbar);
			
			tvDefEN = (TextView) v.findViewById(R.id.wordbook_horizon_tv_def_en);
			tvDefCN = (TextView) v.findViewById(R.id.wordbook_horizon_tv_def_cn);
			llCompleteWord=(LinearLayout)v.findViewById(R.id.wordbook_horizon_ll_complete_word);
		}
	}
}  