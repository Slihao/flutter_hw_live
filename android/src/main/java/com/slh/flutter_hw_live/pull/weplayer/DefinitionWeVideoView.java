//package com.slh.flutter_hw_live.pull.weplayer;
//
//import android.content.Context;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.Paint;
//import android.graphics.RectF;
//import android.graphics.drawable.Drawable;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.text.Spannable;
//import android.text.SpannableStringBuilder;
//import android.text.TextPaint;
//import android.util.AttributeSet;
//import android.util.Log;
//
//import com.huawei.weplayer.listener.DefinitionMediaPlayerControl;
//import com.huawei.weplayer.util.Data;
//import com.huawei.weplayer.util.LogUtils;
//import com.huawei.weplayer.util.PlayerUtils;
//import com.huawei.weplayer.widget.CenteredImageSpan;
//
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//
//import master.flame.danmaku.controller.IDanmakuView;
//import master.flame.danmaku.danmaku.model.BaseDanmaku;
//import master.flame.danmaku.danmaku.model.DanmakuTimer;
//import master.flame.danmaku.danmaku.model.IDanmakus;
//import master.flame.danmaku.danmaku.model.IDisplayer;
//import master.flame.danmaku.danmaku.model.android.DanmakuContext;
//import master.flame.danmaku.danmaku.model.android.Danmakus;
//import master.flame.danmaku.danmaku.model.android.SpannedCacheStuffer;
//import master.flame.danmaku.danmaku.parser.BaseDanmakuParser;
//import master.flame.danmaku.ui.widget.DanmakuView;
//
//
///**
// * 清晰度切换
// * Created by anber on 2019/4/16.
// */
//
//public class DefinitionWeVideoView extends WeVideoView implements DefinitionMediaPlayerControl {
//    private LinkedHashMap<String, String> mDefinitionMap;
//    private DanmakuView mDanmakuView;
//    private DanmakuContext mDanmaContext;
//    private Context mContext;
//    private BaseDanmakuParser mParser;
//    private boolean mIsShow = false;
//
//    private String mCurrentDefinition;
//
//    public DefinitionWeVideoView(@NonNull Context context) {
//        super(context);
//        this.mContext =context;
//    }
//
//    public DefinitionWeVideoView(@NonNull Context context, @Nullable AttributeSet attrs) {
//        super(context, attrs);
//        this.mContext =context;
//    }
//
//    public DefinitionWeVideoView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        this.mContext =context;
//    }
//
//    @Override
//    public LinkedHashMap<String, String> getDefinitionData() {
//        return mDefinitionMap;
//    }
//
//    @Override
//    public void switchDefinition(String definition) {
//        LogUtils.d("switchDefinition");
//        Data.setUuidShouldChange(false);
//        String url = mDefinitionMap.get(definition);
//        LogUtils.v("---xxb--- :" + url + "   definition:" + definition);
////        if (definition.equals(mCurrentDefinition)) return;
//        if (url.equals(mCurrentUrl)) return;
//        mCurrentUrl = url;
//        stopPlayback();
//        release();
//        startPlay(true);
////        addDisplay();
////        getCurrentPosition();
////        startPrepare(true);
//        mCurrentDefinition = definition;
//    }
//
//    public void setDefinitionVideos(LinkedHashMap<String, String> videos) {
//        this.mDefinitionMap = videos;
//        this.mCurrentUrl = getValueFromLinkedMap(videos, 0);
//    }
//
//    public static String getValueFromLinkedMap(LinkedHashMap<String, String> map, int index) {
//        int currentIndex = 0;
//        for (String key : map.keySet()) {
//            if (currentIndex == index) {
//                return map.get(key);
//            }
//            currentIndex++;
//        }
//        return null;
//    }
//    @Override
//    protected void initPlayer(boolean islive) {
//        super.initPlayer(islive);
//        if (mDanmakuView != null) {
//            mPlayerContainer.removeView(mDanmakuView);
//            mPlayerContainer.addView(mDanmakuView, 1);
//        }
//    }
//
//
//    @Override
//    protected void startPrepare(boolean needReset) {
//        super.startPrepare(needReset);
//        if (mDanmakuView != null) {
//            mDanmakuView.prepare(mParser, mDanmaContext);
//        }
//    }
//
//    @Override
//    protected void startInPlaybackState() {
//        super.startInPlaybackState();
//        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
//            mDanmakuView.resume();
//        }
//    }
//
//    @Override
//    public void pause() {
//        super.pause();
//        if (isInPlaybackState()) {
//            if (mDanmakuView != null && mDanmakuView.isPrepared()) {
//                mDanmakuView.pause();
//            }
//        }
//    }
//
//    @Override
//    public void resume() {
//        super.resume();
//        if (mDanmakuView != null && mDanmakuView.isPrepared() && mDanmakuView.isPaused()) {
//            mDanmakuView.resume();
//        }
//    }
//
//    @Override
//    public void release() {
//        super.release();
//        if (mDanmakuView != null) {
//            // dont forget release!
//            mDanmakuView.release();
//            mDanmakuView = null;
//        }
//    }
//
//    @Override
//    public void seekTo(long pos) {
//        super.seekTo(pos);
//        if (isInPlaybackState()) {
//            if (mDanmakuView != null) mDanmakuView.seekTo(pos);
//        }
//    }
//
//    @Override
//    public void setDanmuku(boolean isShow) {
//        super.setDanmuku(isShow);
//        Log.e("---xxb---","setDanmuku");
//        if(mDanmakuView == null){
//            initDanMuView();
//        }else if(isShow){
//            mIsShow = isShow;
//            mDanmakuView.show();
//        }else {
//            mIsShow = isShow;
//            mDanmakuView.hide();
//        }
//    }
//
//    /**
//     * 添加弹幕
//     */
//    public void openDanmukuView(boolean isOpenDanmaku) {
//        if(mDanmakuView == null && isOpenDanmaku){
//            initDanMuView();
//            if(!mIsShow){
//                mDanmakuView.hide();
//            }
//        }
//    }
//
//
//    //弹幕
//    private void initDanMuView() {
//// 设置最大显示行数
//        HashMap<Integer, Integer> maxLinesPair = new HashMap<>();
//        maxLinesPair.put(BaseDanmaku.TYPE_SCROLL_RL, 5); // 滚动弹幕最大显示5行
//        // 设置是否禁止重叠
//        HashMap<Integer, Boolean> overlappingEnablePair = new HashMap<>();
//        overlappingEnablePair.put(BaseDanmaku.TYPE_SCROLL_RL, true);
//        overlappingEnablePair.put(BaseDanmaku.TYPE_FIX_TOP, true);
//
//        mDanmakuView = new DanmakuView(mContext);
//        mDanmaContext = DanmakuContext.create();
//        mDanmaContext.setDanmakuStyle(IDisplayer.DANMAKU_STYLE_STROKEN, 3).setDuplicateMergingEnabled(false).setScrollSpeedFactor(1.2f).setScaleTextSize(1.2f)
////                .setCacheStuffer(new SpannedCacheStuffer(), null) // 图文混排使用SpannedCacheStuffer
//                .setCacheStuffer(new BackgroundCacheStuffer(), null)  // 绘制背景使用BackgroundCacheStuffer
//                .setMaximumLines(maxLinesPair)
//                .preventOverlapping(overlappingEnablePair).setDanmakuMargin(40);
//        if (mDanmakuView != null) {
//            mParser = new BaseDanmakuParser() {
//                @Override
//                protected IDanmakus parse() {
//                    return new Danmakus();
//                }
//            };
//            mDanmakuView.setCallback(new master.flame.danmaku.controller.DrawHandler.Callback() {
//                @Override
//                public void updateTimer(DanmakuTimer timer) {
//                }
//
//                @Override
//                public void drawingFinished() {
//
//                }
//
//                @Override
//                public void danmakuShown(BaseDanmaku danmaku) {
////                    Log.d("DFM", "danmakuShown(): text=" + danmaku.text);
//                }
//
//                @Override
//                public void prepared() {
//                    mDanmakuView.start();
//                }
//            });
//            mDanmakuView.setOnDanmakuClickListener(new IDanmakuView.OnDanmakuClickListener() {
//
//                @Override
//                public boolean onDanmakuClick(IDanmakus danmakus) {
//                    Log.d("DFM", "onDanmakuClick: danmakus size:" + danmakus.size());
//                    BaseDanmaku latest = danmakus.last();
//                    if (null != latest) {
//                        Log.d("DFM", "onDanmakuClick: text of latest danmaku:" + latest.text);
//                        return true;
//                    }
//                    return false;
//                }
//
//                @Override
//                public boolean onDanmakuLongClick(IDanmakus danmakus) {
//                    return false;
//                }
//
//                @Override
//                public boolean onViewClick(IDanmakuView view) {
//                    return false;
//                }
//            });
//            mDanmakuView.showFPS(true);
//            mDanmakuView.enableDanmakuDrawingCache(true);
//        }
//    }
//
//    /**
//     * 绘制背景(自定义弹幕样式)
//     */
//    private class BackgroundCacheStuffer extends SpannedCacheStuffer {
//
//
//        // 通过扩展SimpleTextCacheStuffer或SpannedCacheStuffer个性化你的弹幕样式
//        final Paint paint = new Paint();
//
//        @Override
//        public void measure(BaseDanmaku danmaku, TextPaint paint, boolean fromWorkerThread) {
////            danmaku.padding = 5;  // 在背景绘制模式下增加padding
//            super.measure(danmaku, paint, fromWorkerThread);
//        }
//
//        @Override
//        public void drawBackground(BaseDanmaku danmaku, Canvas canvas, float left, float top) {
//            paint.setAntiAlias(true);
//            paint.setColor(Color.parseColor("#65777777"));//黑色 普通
//            int radius = PlayerUtils.dp2px(mContext, 10);
//            canvas.drawRoundRect(new RectF(left, top, left + danmaku.paintWidth, top + danmaku.paintHeight), radius, radius, paint);
//        }
//
//        @Override
//        public void drawStroke(BaseDanmaku danmaku, String lineText, Canvas canvas, float left, float top, Paint paint) {
//            // 禁用描边绘制
//        }
//    }
//
//    public void addDanmakuWithDrawable(String text, Drawable drawable) {
//        if (mDanmakuView == null) {
//            return;
//        }
//        mDanmaContext.setCacheStuffer(new BackgroundCacheStuffer(), null);
//        BaseDanmaku danmaku = mDanmaContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
//        if (danmaku == null) {
//            return;
//        }
//        // for(int i=0;i<100;i++){
//        // }
//        int size = PlayerUtils.dp2px(mContext, 20);
//        drawable.setBounds(0, 0, size, size);
//
////        danmaku.text = "这是一条弹幕";
//        danmaku.text = createSpannable(drawable, text);
////        danmaku.padding = 5;
//        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
//        danmaku.isLive = false;
//        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
//        danmaku.textSize = PlayerUtils.sp2px(mContext, 12);
//        danmaku.textColor = Color.RED;
//        danmaku.textShadowColor = Color.WHITE;
//        // danmaku.underlineColor = Color.GREEN;
////        danmaku.borderColor = Color.GREEN;
//        mDanmakuView.addDanmaku(danmaku);
//
//    }
//    public void addDanmaku(String text) {
//        if (mDanmakuView == null) {
//            return;
//        }
//        mDanmaContext.setCacheStuffer(new SpannedCacheStuffer(), null);
//        BaseDanmaku danmaku = mDanmaContext.mDanmakuFactory.createDanmaku(BaseDanmaku.TYPE_SCROLL_RL);
//        if (danmaku == null) {
//            return;
//        }
//
//        danmaku.text = text;
//        danmaku.priority = 0;  // 可能会被各种过滤器过滤并隐藏显示
//        danmaku.isLive = false;
//        danmaku.setTime(mDanmakuView.getCurrentTime() + 1200);
//        danmaku.textSize = PlayerUtils.sp2px(mContext, 12);
//        danmaku.textColor = Color.WHITE;
//        danmaku.textShadowColor = Color.RED;
//        // danmaku.underlineColor = Color.GREEN;
//        danmaku.borderColor = Color.GREEN;
//        mDanmakuView.addDanmaku(danmaku);
//
//    }
//
//
//    private SpannableStringBuilder createSpannable(Drawable drawable, String mText) {
//        String text = "bitmap";
//        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text);
//        CenteredImageSpan span = new CenteredImageSpan(drawable);//ImageSpan.ALIGN_BOTTOM);
//        spannableStringBuilder.setSpan(span, 0, text.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
//        spannableStringBuilder.append(mText);
//        return spannableStringBuilder;
//    }
//
//}
