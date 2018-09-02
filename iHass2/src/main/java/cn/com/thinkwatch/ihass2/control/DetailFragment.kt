package cn.com.thinkwatch.ihass2.control

import android.annotation.TargetApi
import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.AbsoluteSizeSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import cn.com.thinkwatch.ihass2.R
import cn.com.thinkwatch.ihass2.model.Attribute
import cn.com.thinkwatch.ihass2.model.AttributeRender
import cn.com.thinkwatch.ihass2.model.Metadata
import com.yunsean.dynkotlins.ui.RecyclerAdapter
import kotlinx.android.synthetic.main.control_detail.view.*
import kotlinx.android.synthetic.main.listitem_sensor_attribute.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.sp

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
class DetailFragment : ControlFragment() {

    private var fragment: View? = null
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(activity)
        fragment = activity?.layoutInflater?.inflate(R.layout.control_detail, null)
        builder.setView(fragment)
        builder.setTitle(if (entity?.showName.isNullOrBlank()) entity?.friendlyName else entity?.showName)
        return builder.create()
    }
    private var adatper: RecyclerAdapter<AttributeItem>? = null
    override fun onResume() {
        super.onResume()
        fragment?.apply {
            adatper = RecyclerAdapter(R.layout.listitem_sensor_attribute, null) {
                view, index, item ->
                val spannableString = SpannableStringBuilder()
                spannableString.append(item.name);
                spannableString.append("：")
                spannableString.append(item.value)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#ff333333")), 0, item.name.length + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                spannableString.setSpan(ForegroundColorSpan(Color.parseColor("#ff777777")), item.name.length + 1, item.name.length + 1 + item.value.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                spannableString.setSpan(AbsoluteSizeSpan(sp(14), false), 0, item.name.length + 1 + item.value.length, Spannable.SPAN_EXCLUSIVE_INCLUSIVE)
                view.value.setText(spannableString)
            }
            recyclerView.adapter = adatper
            recyclerView.layoutManager = LinearLayoutManager(context)
            btnClose.onClick {
                dismiss()
            }
            refreshUi()
        }
    }
    private data class AttributeItem(val name: String,
                                    val value: String)
    private fun refreshUi() {
        val attributes = entity?.attributes
        val items = mutableListOf<AttributeItem>()
        if (attributes != null) {
            Attribute::class.java.declaredFields.forEach {
                val metadata = it.getAnnotation(Metadata::class.java)
                try {
                    if (metadata != null) {
                        it.isAccessible = true
                        var value: Any? = it.get(attributes)
                        if (value != null) {
                            if (metadata.display.isNotBlank()) {
                                val clazz = Class.forName(metadata.display)
                                if (clazz != null) {
                                    val obj = clazz.newInstance()
                                    if (obj is AttributeRender) value = obj.render(value)
                                }
                            }
                            items.add(AttributeItem(metadata.name, value.toString() + metadata.unit))
                        }
                    }
                } catch (ex: Exception) {
                    ex.printStackTrace()
                }
            }
        }
        adatper?.items = items
        fragment?.state?.text = entity?.friendlyStateRow
    }
    override fun onChange() = refreshUi()
}
