## 前言
全局悬浮窗是项目中的一个常见需求,目前比较常见的实现是将要悬浮的`View`添加到`WindowManager`中
这种方案的主要痛点在于需要用户申请`TYPE_SYSTEM_ALERT`权限，并且需要用户去设置中手动打开,使用起来很不方便，同时需要申请权限可能会劝退用户.
针对这种情况下面介绍一种不需要权限的悬浮窗方案

## 效果图
首先看下最终的效果图
![](https://p1-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/289199fad10649ab9484955e42a2a34c~tplv-k3u1fbpfcp-watermark.image)

## 特性
- 1.不需要申请权限，可以直接打开悬浮窗，使用便捷
- 2.支持自定义布局，自定义显示样式,自定义初始显示位置
- 3.支持拖拽，可自动吸附到屏幕边缘
- 4.可过滤不需要显示悬浮窗的黑名单界面
- 5.支持自定义点击事件，可支持展开折叠等功能
- 6.`API`链式调用，使用简洁优雅

## 集成
第 1 步:在工程的 `build.gradle` 中添加：
```groovy
allprojects {
	repositories {
		...
		mavenCentral()
	}
}
```

第2步：在应用的 `build.gradle` 中添加：
```groovy
dependencies {
        implementation 'io.github.shenzhen2017:easyfloat:1.0.0'
}
```

## 使用
`API`链式调用，使用起来非常方便

### 1.初始化
```kotlin
    EasyFloat
        .layout(R.layout.layout_float_view)
        .blackList(mutableListOf(ThirdActivity::class.java))
        .layoutParams(initLayoutParams())
        .listener {
            initListener(it)
        }
        .show(this)
```

如上所示：
1.通过`layout`指定自定义布局
2.通过`blackList`指定不展示悬浮窗界面
3.通过`layoutParams`指定初始展示位置
4.通过`listener`处理自定义点击事件

### 2.销毁悬浮窗
```
	EasyFloat.dismiss(this)
```
直接调用`dismiss`销毁即可

## 主要原理
我们都知道，当我们需要设置布局的时候，是通过`setContentView`设置的
而`setContentView`实际上是将我们的布局添加到了`DecoreView`上,布局层级如下所示：

![](https://p3-juejin.byteimg.com/tos-cn-i-k3u1fbpfcp/a78fc9a822ce4edaab449d7c5a401843~tplv-k3u1fbpfcp-watermark.image)

1.`Activity` 类似于一个框架，负责容器生命周期及活动，窗口通过 `Window` 来管理；
2.`Window` 负责窗口管理（实际是子类 `PhoneWindow`），窗口的绘制和渲染交给 `DecorView`完成；
3.`DecorView` 是 `View` 树的根，开发人员为 `Activity` 定义的 `layout` 将成为 `DecorView` 的子视图 `ContentParent` 的子视图；
4.`layout.xml` 是开发人员定义的布局文件，最终 `inflate` 为 `DecorView` 的子组件；

由上我们可以想到一个方案：
**我们在`Activity onStart`时，将要悬浮的`View`添加到`ContentParent`上就可以实现不需要权限的悬浮窗了**

当然我们还需要注意以下几点
1.因为我们需要在多个页面展示悬浮窗，可以通过`ActivityLifecycleCallbacks`监听所有`Activity`的生命周期,`onStart`时添加，`onStop`时移除
2.因为要在多个页面共享状态，所以应该有一个单例类管理`View`，做到只创建一个`View`，页面切换时只做添加与移除
3.因为要添加到`ContentParent`中，持有了`Activity`的引用，所以要注意处理内存泄漏的问题，在项目中我们使用了弱引用来防止内存泄漏

部分代码如下：
```kotlin
object EasyFloat : Application.ActivityLifecycleCallbacks {
    override fun onActivityStarted(activity: Activity) {
         FloatingView.get().attach(it)
    }

    override fun onActivityStopped(activity: Activity) {
        FloatingView.get().detach(activity)
    }

    fun show(activity: Activity) {
        initShow(activity)
        activity.application.registerActivityLifecycleCallbacks(this)
    }

    fun dismiss(activity: Activity) {
        FloatingView.get().remove()
        FloatingView.get().detach(activity)
        activity.application.unregisterActivityLifecycleCallbacks(this)
    }
}
```

## 总结
### 特别鸣谢
在实现这个开源框架的过程中，主要借鉴了[EnFloatingView](https://github.com/leotyndale/EnFloatingView)的一些思路
并在其基础上进行了一定的封装，优化了`API`调用并解决了滑动冲突等一些问题

### 项目地址
[EasyFloat](https://github.com/shenzhen2017/EasyFloat)
开源不易，如果项目对你有所帮助，欢迎点赞,`Star`,收藏~