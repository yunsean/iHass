package cn.com.thinkwatch.ihass2.ui

import android.os.Bundle
import cn.com.thinkwatch.ihass2.R
import cn.com.thinkwatch.ihass2.base.BaseActivity
import cn.com.thinkwatch.ihass2.bus.ChoosePanel
import cn.com.thinkwatch.ihass2.fragment.AutomationFragment
import com.dylan.common.rx.RxBus2
import com.yunsean.dynkotlins.extensions.activity


class AutomationActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentViewNoTitlebar(R.layout.activity_fragment_container)
        setTitle("自动化", true)
        setAutoHideSoftInput(AutoHideSoftInputMode.WhenClick)
        supportActionBar?.elevation = 0F
        supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, AutomationFragment().apply { showBack = true })
                .commit()
    }

    override fun doLeft() {
        RxBus2.getDefault().post(ChoosePanel())
    }

    override fun doRight() {
        activity(PanelListActivity::class.java)
    }
}

