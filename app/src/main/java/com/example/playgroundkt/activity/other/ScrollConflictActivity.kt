package com.example.playgroundkt.activity.other

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.jetpackmvvm.base.activity.BaseDbActivity
import com.example.playgroundkt.R
import com.example.playgroundkt.RouterPath
import com.example.playgroundkt.databinding.ActivityScrollConflictBinding
import com.example.playgroundkt.fragment.ConflictScrollFragment


@Route(path = RouterPath.ScrollConflictActivity)
class ScrollConflictActivity : BaseDbActivity<ActivityScrollConflictBinding>() {
    private val mFragments = ArrayList<ConflictScrollFragment>()

    /**
     * 0 内部拦截
     * 1 外部拦截
     */
    private var mInterceptType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //建一个存放fragment的集合，并且把新的fragment放到集合中
        mFragments.add(getFragment());
        mFragments.add(getFragment());
        mFragments.add(getFragment());

        //初始化adapter
        val adapter = MyFragmentPagerAdapter(supportFragmentManager, mFragments);
        //将适配器和ViewPager结合
        mDatabind.viewPager.adapter = adapter;

        mDatabind.viewPager.setIsOuter(mInterceptType == 0)

    }

    private fun getFragment(): ConflictScrollFragment {
        val fragment = ConflictScrollFragment()
        val bundle = Bundle()
        bundle.putBoolean("isInner", mInterceptType == 0)
        fragment.arguments = bundle
        return fragment
    }

    override fun layoutId(): Int {
        return R.layout.activity_scroll_conflict
    }

    override fun initView(savedInstanceState: Bundle?) {
    }

    override fun showLoading(message: String) {
    }

    override fun dismissLoading() {
    }

    override fun createObserver() {
    }


    class MyFragmentPagerAdapter(
        fm: FragmentManager?,
        fragments: List<ConflictScrollFragment>
    ) :
        FragmentPagerAdapter(fm!!) {
        private val mFragments: List<ConflictScrollFragment> = fragments
        override fun getItem(position: Int): Fragment {
            return mFragments[position]
        }

        override fun getCount(): Int {
            return mFragments.size
        }
    }
}