# PersistentCoordinatorLayout
仿京东App首页的CoordinatorLayout方案，长列表整体是PersistentCoordinatorLayout，底部的商品列表部分是PersistentRecyclerView。

## 实现效果
点击可查看[截屏视频](http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout)：

<a href="http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout">
    <img src="https://stone225.oss-cn-hangzhou.aliyuncs.com/jingdong.jpg" width="460"/>
</a>

## 使用方法
CoordinatorLayout和RecyclerView的使用方法跟官方一样：
1. 外部的长列表容器使用PersistentCoordinator；
2. 内嵌的子列表使用PersistentRecyclerView；

仅此两点，别无其他，ViewPager和ViewPager2均已内部兼容，可任意选用；

## 实现方案
CoordinatorLayout已经实现了NestedScrollingParent3接口，底部列表上拉跟下滑时，会自动将Fling速率传递给AppBarLayout。而AppBarLayout上拉时，却无法将Fling速率传递给底部的列表View。所以，CoordinatorLayout只需要改进一点，就能让它“更像是一个长列表”，即：

AppBarLayout上拉时，Fling速率传递给下方的RecyclerView。

要实现这一点并不复杂，AppBarLayout的fling是通过behavior实现的，behavior内部也会维持一个OverScroller对象，我们只需要在特定的时间节点，将OverScroller内部的velocityY传递给底部的RecyclerView即可。

当然细节代码较多，此处不再赘述，感兴趣的同学可自行Review代码即知。

## 另一种方案
京东App首页的长列表，整体是通过RecyclerView实现的，我也实现了一份，感兴趣的同学可以去瞅瞅：[传送门](https://github.com/xmuSistone/PersistentRecyclerView)

## Demo下载
[点击下载](https://github.com/xmuSistone/PersistentCoordinatorLayout/blob/master/PersistentCoordinatorLayout.apk?raw=true)
