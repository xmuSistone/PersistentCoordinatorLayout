# PersistentCoordinatorLayout
仿京东App首页的CoordinatorLayout方案，思路：长列表整体是PersistentCoordinator，底部的商品列表部分(PersistentRecyclerView)可以左右滑动。

## 实现效果
点击可查看[截屏视频](http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout)：

<a href="http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout">
    <img src="capturedImage.jpg" width="460"/>
</a>

## 使用方法
CoordinatorLayout和RecyclerView的使用方法跟官方一样，只是需要注意这2点：
1. 外部的长列表容器使用PersistentCoordinator；
2. 内嵌的子列表使用PersistentRecyclerView；

仅此两点，别无其他，ViewPager和ViewPager2均已内部兼容，可任意选用；

## 实现方案
熟悉CoordinatorLayout的同学会知道，官方控件只需要改进一点，就能让它“更像是一个长列表”，即：

AppBarLayout快速向上拖动时，Fling速率能够传递给下方的RecyclerView。

要实现这一点并不复杂，AppBarLayout的fling是通过behavior实现的，behavior内部也会维持一个OverScroller对象，我们只需要在特定的时间点，将OverScroller内部的velocityY传递给RecyclerView，就能实现我们期待的长列表效果。

当然细节代码较多，此处不再赘述，感兴趣的同学可自行Review代码即知。

## 另一种方案
京东App首页的长列表，整体是通过RecyclerView实现的，我也实现了一份，感兴趣的同学可以去瞅瞅：[传送门](https://github.com/xmuSistone/PersistentCoordinatorLayout)

## Demo下载
[点击下载](https://github.com/xmuSistone/PersistentRecyclerView/blob/master/app-release.apk?raw=true)
