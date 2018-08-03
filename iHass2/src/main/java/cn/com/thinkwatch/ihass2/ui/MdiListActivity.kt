package cn.com.thinkwatch.ihass2.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.inputmethod.EditorInfo
import cn.com.thinkwatch.ihass2.R
import cn.com.thinkwatch.ihass2.base.BaseActivity
import cn.com.thinkwatch.ihass2.model.MDIFont
import com.dylan.common.utils.Utility
import com.yunsean.dynkotlins.extensions.onChanged
import com.yunsean.dynkotlins.extensions.text
import com.yunsean.dynkotlins.ui.RecyclerAdapter
import kotlinx.android.synthetic.main.activity_hass_mdi_list.*
import kotlinx.android.synthetic.main.listitem_hass_mdi.view.*
import org.jetbrains.anko.act
import org.jetbrains.anko.ctx
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk25.coroutines.onClick


class MdiListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hass_mdi_list)
        setTitle("图标选择", true)
        setAutoHideSoftInput(AutoHideSoftInputMode.WhenClick)

        mdis.add("")
        mdis.addAll(MDIFont.getIcons().keys)
        ui()
    }

    private var filterRunnable: Runnable = object: Runnable {
        override fun run() {
            filter()
        }
    }
    private var adapter: RecyclerAdapter<String>? = null
    private fun ui() {
        val colCount = Utility.getScreenWidth(ctx) / dip(64)
        this.adapter = RecyclerAdapter(R.layout.listitem_hass_mdi, mdis) {
            view, index, key ->
            view.icon.text = if (key.isNotBlank()) MDIFont.getIcon(key) else key
            view.name.text = key
            view.onClick {
                setResult(Activity.RESULT_OK, Intent().putExtra("icon", if (key.isNotBlank())"mdi:${key}" else ""))
                finish()
            }
        }
        this.recyclerView.adapter = adapter
        this.recyclerView.layoutManager = GridLayoutManager(ctx, colCount)

        this.keyword.onChanged {
            act.keyword.removeCallbacks(filterRunnable)
            act.keyword.postDelayed(filterRunnable, 1000)
        }
        this.keyword.setOnEditorActionListener() { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                act.keyword.removeCallbacks(filterRunnable)
                Utility.hideSoftKeyboard(act)
                filter()
            }
            false
        }
    }
    private var mdis = mutableListOf<String>()
    private fun filter() {
        val keyword = act.keyword.text()
        this.adapter?.items = mdis.filter { it.contains(keyword) }.toList()
        this.adapter?.notifyDataSetChanged()
    }
}
