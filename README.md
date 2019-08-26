# WaveProgressView_Support
支持android support版本的一个可以更换不同背景的水波进度条

1、导入仓库
allprojects {
repositories {
...
maven { url 'https://jitpack.io' }
}
}

2、导入依赖
dependencies {
implementation 'com.github.android-work:WaveProgressView_Support:v1.0.1'
}

相关的动画调用方法
  ##### 1、添加布局
      <com.work.android.waveprogressview.WaveProgress
        android:layout_width="100dp"
        android:layout_height="300dp"/>
        
   ##### 2、初始化
        调用开启动画：startAnimation()
        停止动画：stopAnimator()
        实时调用该方法，维护进度：setProgress(float progress)传入的是一个（0-1）之间的一个进度值
        设置水波移动速度：setWaveSpeed(long speed)默认100毫秒
        设置水波颜色：setWaveColor(int color)
        设置文字颜色：setTextColor(int color)
        设置水波高度：setWaveHeight(int height)
        设置在背景宽度中显示多少个波浪周期：setWaveCount(int count)默认1个
        设置文字大小：setTextSize(float textSize)
        设置进度条背景：setBG(int background)
