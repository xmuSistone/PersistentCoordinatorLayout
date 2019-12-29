package com.stone.persistent


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.stone.persistent.library.PersistentProvider
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: FragmentPagerAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initAdapter()
        viewpager.adapter = adapter

        coordinator_layout.setPersistentProvider(object : PersistentProvider {
            override fun getViewPager(): ViewPager {
                return viewpager
            }

            override fun getRecyclerView(position: Int): RecyclerView? {
                val listFragment = adapter.getItem(position) as ListFragment
                return listFragment.getRecyclerView()
            }
        })

        main_webview.loadUrl("https://github.com/xmuSistone")
    }


    private fun initAdapter() {
        val fragmentList = ArrayList<Fragment>()
        fragmentList.add(ListFragment(R.layout.fragment_list))
        fragmentList.add(ListFragment(R.layout.fragment_list))
        fragmentList.add(ListFragment(R.layout.fragment_list))

        adapter = object : FragmentPagerAdapter(supportFragmentManager, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
            override fun getCount(): Int {
                return fragmentList.size
            }

            override fun getItem(position: Int): Fragment {
                return fragmentList[position]
            }

        }
    }
}
