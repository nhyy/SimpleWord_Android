package com.bubble.simpleword.db;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.bubble.simpleword.MainActivity;
import com.bubble.simpleword.wordbook.WordsClass;


/**
 * <p>Title: WordsManager</p>
 * <p>Description: 管理单词库</p>
 * <p>Company: </p> 
 * @version 1.0   
 * @since JDK 1.8.0_45
 * @author bubble
 * @date 2015-8-6 下午11:35:11
 */
public class WordsDB {
	private static Context mContext;
	private static SQLiteDatabase db;
    private static WordsDbHelper wordsDbHelper;
    
    public static int counts;
    public static Cursor cursor;
    
    public static WordsClass wordClass; //the current word
    
    public final static String TABLE_NAME = "Words";
    public final static String COLUMN_WORD = "word";
    public final static String COLUMN_PHONETIC = "phonetic_symbol";
    public final static String COLUMN_DEFINITION = "definition";
    public static String wordWord;  
    public static String wordPhonetic;  
    public static String wordDefinition;  
    
    //"db.query"'s parameter: "orderby"
    private final static String ORDERBY_RANDOM = "RANDOM()"; //整体随机重新排序，未改变原数据库数据顺序
    private final static String ORDERBY_IN_ORDER = "word"; 
    private final static String ORDERBY_REVERSE_ORDER = "word desc"; 
    
    public static int MODE_GET_WORD ;
    private final static int MODE_GET_WORD_RANDOM = 0 ;
    private final static int MODE_GET_WORD_IN_ORDER = 1;
    private final static int MODE_GET_WORD_REVERSE_ORDER = 2;
    
    static boolean isRandom = false;
    static boolean isInOrder = false;
    static boolean isReverseOrder = false;
    
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: 对表执行操作前先初始化</p>
     * @param context
     * @author bubble
     * @date 2015-8-7
     */
    public static void initWordsDB(Context context){
    	mContext = context;
		wordsDbHelper = new WordsDbHelper(mContext, MainActivity.DB_NAME, null, 1);
		db = wordsDbHelper.getWritableDatabase();
		cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
		cursor.moveToFirst();
		counts = cursor.getCount();
//		Log.i("Words表的行数counts", Integer.toString(counts));
    }
    /**
     * <p>Title: initWordsDB</p>
     * <p>Description: </p>
     * @param context
     * @param position
     * @author bubble
     * @date 2015-8-20 上午12:32:56
     */
    public static void initWordsDB(Context context, int position){
    	mContext = context;
    	wordsDbHelper = new WordsDbHelper(mContext, MainActivity.DB_NAME, null, 1);
    	db = wordsDbHelper.getWritableDatabase();
    	cursor = db.query(TABLE_NAME, null, null, null, null, null, null);
    	cursor.moveToPosition(position);
    }

	public static void addWord(){
		
	}
	
	public static void deleteWord(){
		
	}
	
	public static void editWord(){
		
	}
	
	/**
	 * <p>Title: getAllWords</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-7
	 */
	public static ArrayList<WordsClass> getAllWords() {
		cursor.moveToFirst();
		ArrayList<WordsClass> allWords = new ArrayList<WordsClass>();
		if (cursor != null && cursor.moveToFirst()) {  
		    do {  
		        setWordClass(cursor);
		        allWords.add(wordClass);  
		    } while (cursor.moveToNext());  
		    db.close();
		    return allWords;
		} else {
			db.close();
			return null;
		}
    }
	
	/**
	 * <p>Title: getWord</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-18 上午11:17:59
	 */
	public static WordsClass getWord(){
		getWordOrderBy(cursor);
		cursor.moveToNext();
		return wordClass;
	}
	
	/**
	 * <p>Title: setWordRandom</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordRandom(){
		if ( ! isRandom ) {
			MODE_GET_WORD = MODE_GET_WORD_RANDOM;
			cursor = setWordOrderBy(cursor, ORDERBY_RANDOM);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordInOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordInOrder(){
		if ( ! isInOrder) {
			MODE_GET_WORD = MODE_GET_WORD_IN_ORDER;
			cursor = setWordOrderBy(cursor, ORDERBY_IN_ORDER);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordReverseOrder</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordReverseOrder(){
		if ( ! isReverseOrder ) {
			MODE_GET_WORD = MODE_GET_WORD_REVERSE_ORDER;
			cursor = setWordOrderBy(cursor,ORDERBY_REVERSE_ORDER);
			cursor.moveToFirst();
			setWordModeFlag(MODE_GET_WORD);
		}
	}
	
	/**
	 * <p>Title: setWordModeFlag</p>
	 * <p>Description: change the state of get_word_mode's flags </p>
	 * @author bubble
	 * @date 2015-8-19 下午9:52:19
	 */
	public static void setWordModeFlag(int i) {
		switch (i) {
		case MODE_GET_WORD_RANDOM:
			isRandom = true;
			isInOrder = false;
			isReverseOrder = false;
			break;
		case MODE_GET_WORD_IN_ORDER:
			isInOrder = true;
			isRandom = false;
			isReverseOrder = false;
			break;
		case MODE_GET_WORD_REVERSE_ORDER:
			isReverseOrder = true;
			isInOrder = false;
			isRandom = false;
			break;
		default:
			break;
		}
	}
	
	/**
	 * <p>Title: setWordOrderBy</p>
	 * <p>Description: </p>
	 * @param cur
	 * @param orderby
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
	private static Cursor setWordOrderBy(Cursor cur,String orderby){
		cur = db.query(TABLE_NAME, null, null, null, null, null, orderby);
		return cur;
	}
	/**
	 * <p>Title: getWordOrderBy</p>
	 * <p>Description: </p>
	 * @param cur
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
	private static WordsClass getWordOrderBy(Cursor cur){
		if ( ! isCursorValid(cur) )
			return null;
		
		setWordClass(cur);
		return wordClass;
	}
	
	/**
	 * <p>Title: setWordClass</p>
	 * <p>Description: </p>
	 * @param cur
	 * @author bubble
	 * @date 2015-8-8
	 */
	public static void setWordClass(Cursor cur){
		wordClass = new WordsClass();  
		wordWord = cur.getString(cur.getColumnIndex(COLUMN_WORD));  
		wordPhonetic = cur.getString(cur.getColumnIndex(COLUMN_PHONETIC));  
		wordDefinition = cur.getString(cur.getColumnIndex(COLUMN_DEFINITION));  
		wordClass.setWord(wordWord);  
		wordClass.setPhonetic_symbol(wordPhonetic);  
		wordClass.setDefinition(wordDefinition);
	}
	 
	/**
	 * <p>Title: isCursorValid</p>
	 * <p>Description: </p>
	 * @param cursor
	 * @return
	 * @author bubble
	 * @date 2015-8-9
	 */
	public static boolean isCursorValid(Cursor cursor){
		if ( cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst())
			return false;
		else
			return true;
	}
	/**
	 * <p>Title: isCursorValid</p>
	 * <p>Description: </p>
	 * @return
	 * @author bubble
	 * @date 2015-8-20 上午1:12:13
	 */
	public static boolean isCursorValid(){
		if ( cursor == null || cursor.isAfterLast() || cursor.isBeforeFirst())
			return false;
		else
			return true;
	}
	
	/**
	 * <p>Title: getCursorPosition</p>
	 * <p>Description: </p>
	 * @author bubble
	 * @date 2015-8-19 下午10:41:21
	 */
	public static int getCursorPosition() {
		return cursor.getPosition();
	}
	
	/**
	 * <p>Title: setCursorPosition</p>
	 * <p>Description: </p>
	 * @param position
	 * @author bubble
	 * @date 2015-8-19 下午10:42:32
	 */
	public static void setCursorPosition(int position) {
		cursor.moveToPosition(position);
	}
	
}