package com.alvefard.yify.presentation.util

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.viewpager.widget.ViewPager
import com.alvefard.yifymovies.R
import com.google.android.material.tabs.TabLayout
import java.lang.ref.WeakReference
import java.util.ArrayList

class EnhanceTabLayout : FrameLayout {
    /**
     * retrieve TabLayout Instance
     * @return
     */
    var tabLayout: TabLayout? = null
        private set
    private var mTabList: MutableList<String>? = null
    private var mCustomViewList: MutableList<View>? = null
    private var mSelectIndicatorColor: Int = 0
    private var mSelectTextColor: Int = 0
    private var mUnSelectTextColor: Int = 0
    private var mIndicatorHeight: Int = 0
    private var mIndicatorWidth: Int = 0
    private var mTabMode: Int = 0
    private var mTabTextSize: Int = 0

    val customViewList: List<View>?
        get() = mCustomViewList

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun readAttr(context: Context, attrs: AttributeSet?) {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.EnhanceTabLayout)
        mSelectIndicatorColor = typedArray.getColor(R.styleable.EnhanceTabLayout_tabIndicatorColor, resources.getColor(R.color.solid_pink))
        mUnSelectTextColor = typedArray.getColor(R.styleable.EnhanceTabLayout_tabTextColor, resources.getColor(R.color.dove_gray))
        mSelectTextColor = typedArray.getColor(R.styleable.EnhanceTabLayout_tabSelectTextColor, resources.getColor(R.color.solid_pink))
        mIndicatorHeight = typedArray.getDimensionPixelSize(R.styleable.EnhanceTabLayout_tabIndicatorHeight, 1)
        mIndicatorWidth = typedArray.getDimensionPixelSize(R.styleable.EnhanceTabLayout_tabIndicatorWidth, 0)
        mTabTextSize = typedArray.getDimensionPixelSize(R.styleable.EnhanceTabLayout_tabTextSize, 13)
        mTabMode = typedArray.getInt(R.styleable.EnhanceTabLayout_tab_Mode, 2)
        typedArray.recycle()
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        readAttr(context, attrs)

        mTabList = ArrayList()
        mCustomViewList = ArrayList()
        val view = LayoutInflater.from(getContext()).inflate(R.layout.enhance_tab_layout, this, true)
        tabLayout = view.findViewById(R.id.enhance_tab_view)


        tabLayout!!.tabMode = if (mTabMode == 1) TabLayout.MODE_FIXED else TabLayout.MODE_SCROLLABLE
        tabLayout!!.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                // onTabItemSelected(tab.getPosition());

                for (i in 0 until tabLayout!!.tabCount) {
                    val view = tabLayout!!.getTabAt(i)!!.customView ?: return
                    val text = view.findViewById<View>(R.id.tab_item_text) as TextView
                    val indicator = view.findViewById<View>(R.id.tab_item_indicator)
                    if (i == tab.position) { // 选中状态
                        text.setTextColor(mSelectTextColor)
                        indicator.setBackgroundColor(mSelectIndicatorColor)
                        indicator.visibility = View.VISIBLE
                    } else {
                        text.setTextColor(mUnSelectTextColor)
                        indicator.visibility = View.INVISIBLE
                    }
                }

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })
    }

    fun addOnTabSelectedListener(onTabSelectedListener: TabLayout.OnTabSelectedListener) {
        tabLayout!!.addOnTabSelectedListener(onTabSelectedListener)
    }

    /**
     *
     *
     * @param viewPager
     */
    fun setupWithViewPager(viewPager: ViewPager?) {
        tabLayout!!.addOnTabSelectedListener(ViewPagerOnTabSelectedListener(viewPager!!, this))
    }

    /**
     * @param tab
     */
    fun addTab(tab: String) {
        mTabList!!.add(tab)
        val customView = getTabView(context, tab, mIndicatorWidth, mIndicatorHeight, mTabTextSize)
        mCustomViewList!!.add(customView)
        tabLayout!!.addTab(tabLayout!!.newTab().setCustomView(customView))
    }

    class ViewPagerOnTabSelectedListener(private val mViewPager: ViewPager, enhanceTabLayout: EnhanceTabLayout) : TabLayout.OnTabSelectedListener {
        private val mTabLayoutRef: WeakReference<EnhanceTabLayout>?

        init {
            mTabLayoutRef = WeakReference(enhanceTabLayout)
        }

        override fun onTabSelected(tab: TabLayout.Tab) {
            mViewPager.currentItem = tab.position
            val mTabLayout = mTabLayoutRef!!.get()
            if (mTabLayoutRef != null) {
                val customViewList = mTabLayout!!.customViewList
                if (customViewList == null || customViewList.size == 0) {
                    return
                }
                for (i in customViewList.indices) {
                    val view = customViewList[i] ?: return
                    val text = view.findViewById<View>(R.id.tab_item_text) as TextView
                    val indicator = view.findViewById<View>(R.id.tab_item_indicator)
                    if (i == tab.position) {
                        text.setTextColor(mTabLayout.mSelectTextColor)
                        indicator.setBackgroundColor(mTabLayout.mSelectIndicatorColor)
                        indicator.visibility = View.VISIBLE
                    } else {
                        text.setTextColor(mTabLayout.mUnSelectTextColor)
                        indicator.visibility = View.INVISIBLE
                    }
                }
            }

        }

        override fun onTabUnselected(tab: TabLayout.Tab) {
            // No-op
        }

        override fun onTabReselected(tab: TabLayout.Tab) {
            // No-op
        }
    }

    companion object {

        /**
         * @param context
         * @param
         * @return
         */
        fun getTabView(context: Context, text: String, indicatorWidth: Int, indicatorHeight: Int, textSize: Int): View {
            val view = LayoutInflater.from(context).inflate(R.layout.tab_item_layout, null)
            val tabText = view.findViewById<View>(R.id.tab_item_text) as TextView
            tabText.setTextColor(context.resources.getColor(R.color.dove_gray))
            if (indicatorWidth > 0) {
                val indicator = view.findViewById<View>(R.id.tab_item_indicator)
                val layoutParams = indicator.layoutParams
                layoutParams.width = indicatorWidth
                layoutParams.height = indicatorHeight
                indicator.layoutParams = layoutParams
            }
            tabText.textSize = textSize.toFloat()
            tabText.text = text
            return view
        }
    }

}