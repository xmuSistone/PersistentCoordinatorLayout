# PersistentCoordinatorLayout
persistent fling with coordinatorLayout

### 前因
淘宝（京东）首页里的“长列表”，可以左右滑动切换频道，能够看到的是长列表内部有一个ViewPager。这样的效果看起来简单，实现起来却不简单。此库本来没打算开源，后来看到[JasonGaoH](https://github.com/JasonGaoH)同学的尝试[NestedRecyclerView](https://github.com/JasonGaoH/NestedRecyclerView)，在文章中提及CoordinatorLayout无法解决AppBarLayout的fling惯性问题。由于先前阅读源码并通过hook钻空子的方式解决了这个问题，故将代码开源，供有需要的同学参考。

### 原理
诚然，CoordinatorLayout是距离淘宝、京东首页效果最近的官方容器控件。但是，它需要解决一个问题：AppBarLayout往下fling时，View的滑动速率如何传导给ViewPager内部的RecyclerView（或者NestedSrollView）？这也意味着，需要处理好这三处逻辑：

1. 手指松开fling多长时间可以结束(duration)？
2. fling结束之后的Y轴速率几何(velocity)？
3. fling瞬间结束那一刻的Y轴速率，如何传递给下方ViewPager中的RecyclerView？

如果能够猜一把的话，AppBarLayout触摸滑动或fling，一定会跟OverScroller搭上关系。事实上，确实有这样一个OverScroller，它隐藏在behavior里，即AppBarLayout.Behavior的爷爷类（父类的父类）里。OverScroller内部有一个匿名内部类叫SplineOverScroller，看一眼这个内部类的源码，会发现我们想要的所有信息它都有！所以，我们自己实现一个OverScroller，强行塞到Behavior里：

```kotlin
/**
 * 反射behavior塞入hookScroller
 */
 private fun hookScroller() {
    val lp = appBarLayout.layoutParams as LayoutParams
    val behavior = lp.behavior as AppBarLayout.Behavior

    val scrollerField = AppBarLayout.Behavior::class.java.superclass.superclass.getDeclaredField("scroller")
    scrollerField.isAccessible = true
    if (scrollerField.get(behavior) == null) {
        overScroller = HookedScroller(context)
        scrollerField.set(behavior, overScroller)
    }
}
```
是的，就是这么任性！我们自己实现一个HookedScroller，然后我们可以随心所欲，为所欲为！


