# PersistentCoordinatorLayout
persistent fling with coordinatorLayout

### 前因
淘宝（京东）首页里的“长列表”，可以左右滑动切换频道，能够看到的是长列表内部有一个ViewPager。
此库本来没打算开源，后来看到[JasonGaoH](https://github.com/JasonGaoH)同学的尝试[NestedRecyclerView](https://github.com/JasonGaoH/NestedRecyclerView)，在博客中提及CoordinatorLayout无法解决AppBarLayout的fling惯性问题。由于之前阅读过源码，并通过hook解决了这个问题。所以，此处将代码开源，供有需要的同学参考。

### 原理
其实，CoordinatorLayout距离淘宝、京东首页的效果是最近的，它只需要解决一个问题：AppBarLayout往下fling时，View的滑动速率如何传递给ViewPager内部的RecyclerView（或者NestedSrollView）？这也就意味着，我们需要处理好这三点：
1. 手指松开fling多长时间可以结束(duration)？
2. fling结束之后的Y轴速率如何？
3. fling瞬间结束那一刻的Y轴速率，如何传递给下方ViewPager中的RecyclerView？

