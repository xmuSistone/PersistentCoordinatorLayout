# PersistentCoordinatorLayout

仿京东首页，整体是个长列表，内嵌子列表-商品feeds流(PersistentRecyclerView)，且商品流可以左右滑动。

## 实现效果
点击可查看[截屏视频](http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout)：

<a href="http://sistone.top/capture/video.html?content=PersistentCoordinatorLayout">
    <img src="https://stone225.oss-cn-hangzhou.aliyuncs.com/jingdong.jpg" width="460"/>
</a>

## 使用方法
1. 外部的长列表容器使用PersistentCoordinatorLayout；
2. 内嵌的子列表使用PersistentRecyclerView；

CoordinatorLayout和RecyclerView的使用方法跟官方一样，ViewPager和ViewPager2可随意选用，均已内部兼容；

## 实现方案
CoordinatorLayout已经实现了NestedScrollingParent3接口，当底部列表上拉或下拉时，会自动将Fling的速率传递给AppBarLayout。而AppBarLayout上拉触底时，却无法将Fling速率传递给底部的RecyclerView。所以，我们只要能改造好这一点，就能让CoordinatorLayout“<b>更像是一个长列表</b>”。<br/><br/>

要实现这一点并不复杂，AppBarLayout的fling是通过behavior实现的，behavior内部会维护一个OverScroller对象，OverScroller保存了我们想要的一切，包括Fling速率。<br/><br/>

当然细节代码较多，此处不再赘述，感兴趣的同学可自行Review代码即知。

## 另一种方案
京东App首页的长列表，整体是通过RecyclerView实现的，我也实现了一份，感兴趣的同学可以去瞅瞅：[传送门](https://github.com/xmuSistone/PersistentRecyclerView)

## Demo下载
[点击下载](https://github.com/xmuSistone/PersistentCoordinatorLayout/blob/master/PersistentCoordinatorLayout.apk?raw=true)
