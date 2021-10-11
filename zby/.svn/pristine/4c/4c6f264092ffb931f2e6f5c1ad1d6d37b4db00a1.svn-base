package com.biaozhunyuan.tianyi.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.biaozhunyuan.tianyi.R;
import com.biaozhunyuan.tianyi.common.helper.Logger;
import com.biaozhunyuan.tianyi.common.helper.InputSoftHelper;
import com.biaozhunyuan.tianyi.common.view.CircleImageView;
import com.biaozhunyuan.tianyi.helper.JsonParser;
import com.biaozhunyuan.tianyi.common.helper.SharedPreferencesHelper;
import com.biaozhunyuan.tianyi.widget.TextEditTextView;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 声音输入控件
 *
 * @author k
 */
public class VoiceInputView extends LinearLayout {
    Timer timer = new Timer();

    //    private ImageView iv_most_left;//将光标移动到最左侧
//    private ImageView iv_most_right;//将光标移动到最右侧
    private View mRootView;
    /**
     * 顶部输入 删除/切换输入键盘
     */
//    private LinearLayout llTop;

    //按住说话语音输入区域
    private RelativeLayout llVoiceInput;
    private ImageView iv_keybord;
    private ImageView iv_speech;

    //    private ImageView ivChangeKeyBoard;
    private Context context;
    private RelativeLayout rl_keybord; //切换到键盘模式
    private RelativeLayout rl_speech; //切换到语音模式
    //    private ImageView ivClose;//关闭语音键盘
    private ImageView mIvVoice;
    private Button ivLeft;
    private Button ivRight;
    private Button ivDelete;
    private Button ivBackPress;//回车键
    private TextView mTvContent;
    private EditText mInputEditText;

    //    private EditText mEtFoucs;
    private SpeechRecognizer mIat;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case 1:   //删除文字
                    if (mInputEditText != null) {
                        int index = mInputEditText.getSelectionStart();
                        if (mInputEditText.getText().toString().trim().length() >= 0 && index >= 1) {
                            String text = mInputEditText.getText().toString();
                            StringBuilder builder = new StringBuilder(text);
                            builder.deleteCharAt(index - 1);
                            mInputEditText.setText(builder.toString());
                            Selection.setSelection(mInputEditText.getText(), index - 1);
                        }
                    }
                    break;
                case 2: // 光标左移动
                    if (mInputEditText != null) {
                        int index = mInputEditText.getSelectionStart();
                        if (mInputEditText.getText().toString().trim().length() >= 0 && index >= 1) {
                            Selection.setSelection(mInputEditText.getText(), index - 1);
                        }
                    }
                    break;

                case 3://光标右移动
                    if (mInputEditText != null) {
                        int index = mInputEditText.getSelectionStart();
                        if (mInputEditText.getText().toString().trim().length() >= 0 && index <= mInputEditText.getText().toString().trim().length() - 1) {
                            Selection.setSelection(mInputEditText.getText(), index + 1);
                        }
                    }
                    break;
            }
            return true;
        }
    });
    private long currentTouchTime;//触摸时候的时间
    private long currentDownTime;//当手指按下的时间

    private SharedPreferencesHelper preferencesHelper;
    private boolean mIsFirstHasFocus = true;
    /**
     * 是否即将打开键盘打开输入键盘，标志位：用于控制按钮切换键盘输入或语音输入，true 表示打开输入键盘，false则打开语音,默认打开语音输入
     */
    private boolean mIsWillOpentKeyBoard = true;
    /***
     * 标志：输入键盘是否已经弹出
     */
    private boolean mHasShowKeyBoard;
    private OnKeyBoardChangedListener mOnKeyBoardChangedListener;
    //    private LinearLayout llUserListParent;
    private List<CircleImageView> imgsList = new ArrayList<>();
    private LinearLayout llTop;

//    private HorizontalScrollView horizontalScrollView;
//    private HorizontalListView horizontalListview;
//    private DictionaryHelper dictionaryHelper;
//    private ORMDataHelper dataHelper;
//    private List<User> AllStaffList = new ArrayList<>();
//    private int lastPosition = -1;
//    private CommanAdapter<User> adapter;
//    private ImageView iv_user_add;

    public VoiceInputView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        mRootView = LayoutInflater.from(context).inflate(
                R.layout.view_input_voice, this, true);
        preferencesHelper = new SharedPreferencesHelper(context);
        initViews();
        setOnEvent();

        initVoice();
    }

    public VoiceInputView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VoiceInputView(Context context) {
        this(context, null);
    }

    private void initViews() {
        if (mRootView != null) {
            llVoiceInput = (RelativeLayout) mRootView.findViewById(R.id.ll_speek_voice);
            mIvVoice = (ImageView) mRootView.findViewById(R.id.iv_voice_value);
            llTop = mRootView.findViewById(R.id.ll_voice_keboard);
            mTvContent = (TextView) mRootView.findViewById(R.id.tv_content_value);
            rl_keybord = (RelativeLayout) findViewById(R.id.rl_speech_view_keybord);
            rl_speech = (RelativeLayout) findViewById(R.id.rl_speech_view_mac);
            iv_keybord = (ImageView) findViewById(R.id.iv_speech_view_keybord);
            iv_speech = (ImageView) findViewById(R.id.iv_speech_view_mac);
            ivLeft = (Button) mRootView.findViewById(R.id.iv_left_remove_voice);
            ivRight = (Button) mRootView.findViewById(R.id.iv_right_remove_voice);
            ivDelete = (Button) mRootView.findViewById(R.id.iv_delete_remove_voice);
            ivBackPress = (Button) mRootView.findViewById(R.id.iv_back_press);


        }
    }

    private void setOnEvent() {

        ivDelete.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                currentTouchTime = System.currentTimeMillis();
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timer = new Timer();
                    timer.schedule(new Task1(), 100, 100);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer.cancel();
                    timer.purge();
                }
                return false;
            }
        });

        ivDelete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputEditText != null) {
                    int index = mInputEditText.getSelectionStart();
                    if (mInputEditText.getText().toString().trim().length() >= 0 && index >= 1) {
                        String text = mInputEditText.getText().toString();
                        StringBuilder builder = new StringBuilder(text);
                        builder.deleteCharAt(index - 1);
                        mInputEditText.setText(builder.toString());
                        Selection.setSelection(mInputEditText.getText(), index - 1);
                    }
                }
            }
        });

        ivLeft.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timer = new Timer();
                    timer.schedule(new Task2(), 100, 100);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer.cancel();
                    timer.purge();
                }
                return false;
            }
        });


        //光标左移动
        ivLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputEditText != null) {
                    int index = mInputEditText.getSelectionStart();
                    if (mInputEditText.getText().toString().trim().length() >= 0 && index >= 1) {
                        Selection.setSelection(mInputEditText.getText(), index - 1);
                    }
                }
            }
        });


        ivRight.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    timer = new Timer();
                    timer.schedule(new Task3(), 100, 100);
                }

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    timer.cancel();
                    timer.purge();
                }
                return false;
            }
        });

        //光标右移动
        ivRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputEditText != null) {
                    int index = mInputEditText.getSelectionStart();
                    if (mInputEditText.getText().toString().trim().length() >= 0 && index <= mInputEditText.getText().toString().trim().length() - 1) {
                        Selection.setSelection(mInputEditText.getText(), index + 1);
                    }
                }
            }
        });
//
//        //光标移动到最左侧
//        iv_most_left.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInputEditText != null) {
//                    Selection.setSelection(mInputEditText.getText(), 0);
//                }
//            }
//        });
//
//        //光标移动到最右侧
//        iv_most_right.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInputEditText != null) {
//                    Selection.setSelection(mInputEditText.getText(), mInputEditText.getText().length());
//                }
//            }
//        });
//
        //回车键
        ivBackPress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mInputEditText != null) {
                    int index = mInputEditText.getSelectionStart();
                    String text = mInputEditText.getText().toString();
                    StringBuffer buffer = new StringBuffer(text);
                    text = buffer.insert(index, "\n").toString();
                    mInputEditText.setText(text);
                    Selection.setSelection(mInputEditText.getText(), index + 1);
                }
            }
        });

//        ivChangeKeyBoard.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (mInputEditText != null) {
//                    changeKeyBoard();
//                }
//            }
//        });

//        ivClose.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                closeAllKeyBoard();
//            }
//        });

        /**
         * 切换到语音模式
         */
        rl_speech.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToSpeech();
            }
        });


        /**
         * 切换到键盘模式
         */
        rl_keybord.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                changeToKeybord();
            }
        });

        mIvVoice.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mTvContent.setText("开始录音");
                        mIvVoice.setImageResource(R.drawable.icon_voice_value1);
                        int i = mIat.startListening(mRecoListener);
                        if (i != ErrorCode.SUCCESS) {
                            Logger.d("识别失败,错误码: " + i);
                        }
                        break;
                    case MotionEvent.ACTION_UP:
                        mIat.stopListening();
                        mTvContent.setText("按下录音");
                        mIvVoice.setImageResource(R.drawable.ico_voice_value_default);
                        break;
                }
                return true;
            }
        });

//        mInputEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
//            @Override
//            public void onGlobalLayout() {
//
//                Log.d("KeyboardHSize", "onGlobalLayout: ");
//                Rect r = new Rect();
//                //获取当前界面可视部分
//                ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
//                //获取屏幕的高度
//                int screenHeight = ((Activity) getContext()).getWindow().getDecorView().getRootView().getHeight();
//                //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
//                int heightDifference = screenHeight - r.bottom;
//                Log.d("KeyboardHSize", "Size: " + heightDifference);
//
//                if (heightDifference <= 0) {
//                    //键盘隐藏,显示语音输入
//                    mHasShowKeyBoard = false;
//                } else {
//                    mHasShowKeyBoard = true;
//                }
//
//            }
//        });
    }

    /**
     * 设置不可点击切换键盘和语音
     */
    public void setSwitvhKeyBoardandVoiceVisible(boolean isVisible){
        llTop.setVisibility(isVisible? VISIBLE : GONE);
    }

    /***
     * 关闭所有键盘，包括语音键盘和系统键盘
     */
    public void closeAllKeyBoard() {
        if (mHasShowKeyBoard) {
            //如果文字键盘是打开的 则关闭
            InputMethodManager imm = (InputMethodManager) ((Activity) getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        }

        if (llVoiceInput.getVisibility() != GONE) {
            llVoiceInput.setVisibility(GONE);
        }
        //关闭语音键盘
        VoiceInputView.this.setVisibility(GONE);

        //默认初次打开语音键盘
        mIsWillOpentKeyBoard = false;

        addKeyBoardChangeListener(false);
    }

    /***
     * 切换键盘
     */
    private void changeKeyBoard() {
        InputMethodManager imm = (InputMethodManager) ((Activity) getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (mIsWillOpentKeyBoard) {
            if (mHasShowKeyBoard) {
                //如果键盘已经打开，不做处理
                return;
            }

            boolean isDirectOpen = false;
            if (llVoiceInput.getVisibility() == VISIBLE) {
                preferencesHelper.putBooleanValue("isVoiceOpen", false);
                //关闭语音
                iv_keybord.setImageResource(R.drawable.ico_speech_keybord_selected);
                iv_speech.setImageResource(R.drawable.ico_speech_mac_unselected);
                llVoiceInput.setVisibility(GONE);
                mIsWillOpentKeyBoard = !mIsWillOpentKeyBoard;
            } else {
                //直接打开键盘
                isDirectOpen = true;
            }

            //打开输入键盘
            imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);

            if (isDirectOpen) {
                //直接打开键盘
                addKeyBoardChangeListener(true);
            }

        } else {
            boolean isOpenVoice = false;
            if (mHasShowKeyBoard) {
                //如果已经打开键盘先关闭
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            } else {
                isOpenVoice = true;
            }

            if (llVoiceInput.getVisibility() == GONE) {
                preferencesHelper.putBooleanValue("isVoiceOpen", true);
                //打开语音键盘
                iv_keybord.setImageResource(R.drawable.ico_speech_keybord_unselected);
                iv_speech.setImageResource(R.drawable.ico_speech_mac_selected);
                llVoiceInput.setVisibility(VISIBLE);
                mIsWillOpentKeyBoard = !mIsWillOpentKeyBoard;

                if (isOpenVoice) {
                    addKeyBoardChangeListener(true);
                } else {
                    addKeyBoardChangeListener(false);
                }


            }
        }
    }

    /**
     * 切换到键盘模式
     */
    private void changeToKeybord() {
        InputMethodManager imm = (InputMethodManager) ((Activity) getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        VoiceInputView.this.setVisibility(VISIBLE);
        boolean isDirectOpen = false;
        if (llVoiceInput.getVisibility() == VISIBLE) {
            preferencesHelper.putBooleanValue("isVoiceOpen", false);
            //关闭语音
            iv_keybord.setImageResource(R.drawable.ico_speech_keybord_selected);
            iv_speech.setImageResource(R.drawable.ico_speech_mac_unselected);
            llVoiceInput.setVisibility(GONE);
            mIsWillOpentKeyBoard = !mIsWillOpentKeyBoard;
        } else {
            //直接打开键盘
            isDirectOpen = true;
        }
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        if (isDirectOpen) {
            //直接打开键盘
            addKeyBoardChangeListener(true);
        } else {
            addKeyBoardChangeListener(false);
        }

//        }
    }

    /**
     * 切换到语音模式
     */
    private void changeToSpeech() {
        InputMethodManager imm = (InputMethodManager) ((Activity) getContext()).getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(mInputEditText.getWindowToken(),
                    0);
        }
        boolean isOpenVoice = true;

        if (llVoiceInput.getVisibility() == GONE) {
            preferencesHelper.putBooleanValue("isVoiceOpen", true);
            //打开语音键盘
            iv_keybord.setImageResource(R.drawable.ico_speech_keybord_unselected);
            iv_speech.setImageResource(R.drawable.ico_speech_mac_selected);
            llVoiceInput.setVisibility(VISIBLE);
            mIsWillOpentKeyBoard = !mIsWillOpentKeyBoard;

            if (isOpenVoice) {
                addKeyBoardChangeListener(true);
            }
        }
//        }
    }

    private void addKeyBoardChangeListener(boolean isOpen) {
        if (mOnKeyBoardChangedListener != null) {
            mOnKeyBoardChangedListener.onChanged(isOpen);
        }
    }



    private void initVoice() {
        //1.创建SpeechRecognizer对象，第二个参数：本地识别时传InitListener
        mIat = SpeechRecognizer.createRecognizer(getContext(), null);
        //2.设置听写参数，详见《MSC Reference Manual》SpeechConstant类
        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");

    }

    RecognizerListener mRecoListener = new RecognizerListener() {

        @Override
        public void onVolumeChanged(int i) {
            if (i == 0) {
                mIvVoice.setImageResource(R.drawable.icon_voice_value1);
            } else {
                int value = i / 5;
                switch (value) {
                    case 0:
                    case 1:
                        mIvVoice.setImageResource(R.drawable.icon_voice_value1);
                        break;
                    case 2:
                    case 3:
                        mIvVoice.setImageResource(R.drawable.icon_voice_value2);
                        break;
                    case 4:
                    case 5:
                        mIvVoice.setImageResource(R.drawable.icon_voice_value3);
                        break;
                    default:
                        mIvVoice.setImageResource(R.drawable.icon_voice_value4);
                        break;
                }
            }
        }

        @Override
        public void onBeginOfSpeech() {
        }

        @Override
        public void onEndOfSpeech() {
            Logger.i("onResult" + "onEndOfSpeech");
//            mTvContent.setText("录音完成");
//            mIvVoice.setImageResource(R.drawable.ico_voice_value_default);
        }

        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
            String results = recognizerResult.getResultString();
            Logger.i("onResult" + results + "");
            String text = JsonParser.parseIatResult(results);

            if (mInputEditText != null) {
                int index = mInputEditText.getSelectionStart();
                Editable editable = mInputEditText.getText();
                editable.insert(index, text); // 在光标处插入
                mInputEditText.setSelection(mInputEditText.length());
            }
        }

        @Override
        public void onError(SpeechError speechError) {
            Logger.i(speechError.toString());
        }

        @Override
        public void onEvent(int i, int i1, int i2, String s) {

        }
    };

    /**
     * 设置关联输入的Edittext
     *
     * @param etText
     */
    public void setRelativeInputView(EditText etText) {
        this.mInputEditText = etText;
        setInputKeyBoardListener();
    }

    /**
     * 设置输入框的集合，判断如果哪个输入框获取焦点就绑定输入框。
     */
    public void setRelativeListInputView(List<EditText> editList) {
        for (final EditText et : editList) {
            et.setOnFocusChangeListener(new OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        setRelativeInputView(et);
                    }
                }
            });
        }

    }

    private void setInputKeyBoardListener() {
        if (mInputEditText != null) {
            InputSoftHelper.hideSoftInputMethod(mInputEditText);
//            mInputEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
//                @Override
//                public void onFocusChange(View v, boolean hasFocus) {
//                    LogUtils.i("VoiceInputView", "onFocusChange");
//                    if (hasFocus && !mIsFirstHasFocus) {
//                        mIsFirstHasFocus = false;
//                        llVoiceInput.setVisibility(VISIBLE);
//                    }
//                    if(hasFocus)
//                    {
//                        changeKeyBoard();
//                    }
//                }
//            });

            mInputEditText.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    Logger.i("VoiceInputView" + "onClick:" + getVisibility() + " this:" + VoiceInputView.this.getVisibility());
                    boolean isVoiceOpen = preferencesHelper.getBooleanValue("isVoiceOpen", true); //判断上一次关闭时候是打开的语音界面还是键盘,默认打开的是语音界面

                    if (isVoiceOpen) {  //如果打开的是语音界面
                        mIsWillOpentKeyBoard = true;
                        openVoice();
                    } else {
                        mIsWillOpentKeyBoard = false;
                        changeToKeybord();
                    }


                }
            });


            mInputEditText.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    Rect r = new Rect();
                    //获取当前界面可视部分
                    ((Activity) getContext()).getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
                    //获取屏幕的高度
                    int screenHeight = ((Activity) getContext()).getWindow().getDecorView().getRootView().getHeight();
                    //此处就是用来获取键盘的高度的， 在键盘没有弹出的时候 此高度为0 键盘弹出的时候为一个正数
                    int heightDifference = screenHeight - r.bottom;
                    Logger.d("boardgetViewTreeObserver" + "mInputEditTextSize: " + heightDifference);
//                    Toast.makeText(getContext(), "addOnGlobalLayoutListener=" + heightDifference, Toast.LENGTH_SHORT).show();
                    if (heightDifference <= 0) {
                        //键盘隐藏,显示语音输入
                        mHasShowKeyBoard = false;
                    } else {
                        mHasShowKeyBoard = true;
                    }
                }
            });

//            new KeyboardChangeListener((Activity)getContext()).setKeyBoardListener(new KeyboardChangeListener.KeyBoardListener() {
//                @Override
//                public void onKeyboardChange(boolean isShow, int keyboardHeight) {
//                    Log.d("KeyboardHSize", "Size: " + keyboardHeight+"---"+isShow);
//                }
//            });
        }
    }

    public void openVoice() {
        if (VoiceInputView.this.getVisibility() == GONE) {
            VoiceInputView.this.setVisibility(VISIBLE);
            if (onOpenListener != null) {
                onOpenListener.onOpen();
            }
        }

        if (!mHasShowKeyBoard && llVoiceInput.getVisibility() == GONE) {
            //当语音和文字键盘都关闭时才调用键盘事件
            changeKeyBoard();
        }
    }

    /**
     * 打开键盘
     * @param isVoiceOpen true打开语音界面 false打开软键盘
     */
    public void openLastStatus(boolean isVoiceOpen) {

        if (isVoiceOpen) {  //如果打开的是语音界面
            mIsWillOpentKeyBoard = true;
            changeToSpeech();
        } else {
            mIsWillOpentKeyBoard = false;
            changeToKeybord();
        }
    }


    /**
     * 记录上一次状态，打开上一次状态的键盘
     */
    public void openLastStatus() {
        Logger.i("VoiceInputView" + "onClick:" + getVisibility() + " this:" + VoiceInputView.this.getVisibility());
        boolean isVoiceOpen = preferencesHelper.getBooleanValue("isVoiceOpen", true); //判断上一次关闭时候是打开的语音界面还是键盘,默认打开的是语音界面

        if (isVoiceOpen) {  //如果打开的是语音界面
            mIsWillOpentKeyBoard = true;
            changeToSpeech();
        } else {
            mIsWillOpentKeyBoard = false;
            changeToKeybord();
        }
    }

    /***
     * 监听键盘状态
     *
     * @param onKeyBoardChangedListener
     */
    public void setOnKeyBoardChangedListener(OnKeyBoardChangedListener onKeyBoardChangedListener) {
        this.mOnKeyBoardChangedListener = onKeyBoardChangedListener;
    }


    public interface OnKeyBoardChangedListener {
        void onChanged(boolean isOpen);
    }


    private class Task1 extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }
    }

    private class Task2 extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 2;
            handler.sendMessage(message);
        }
    }

    private class Task3 extends TimerTask {
        @Override
        public void run() {
            Message message = new Message();
            message.what = 3;
            handler.sendMessage(message);
        }
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == 1) {
            super.onKeyPreIme(keyCode, event);
            if (onKeyBoardHideListener != null) {
                onKeyBoardHideListener.onKeyHide(keyCode, event);
            }
            return false;
        }
        return super.onKeyPreIme(keyCode, event);
    }

    /**
     * 键盘监听接口
     */
    TextEditTextView.OnKeyBoardHideListener onKeyBoardHideListener;

    public void setOnKeyBoardHideListener(TextEditTextView.OnKeyBoardHideListener onKeyBoardHideListener) {
        this.onKeyBoardHideListener = onKeyBoardHideListener;
    }

    public interface OnKeyBoardHideListener {
        void onKeyHide(int keyCode, KeyEvent event);
    }

    private onOpenListener onOpenListener;

    public void setOnOpenListener(onOpenListener listener) {
        onOpenListener = listener;
    }

    public interface onOpenListener {
        void onOpen();
    }
}


