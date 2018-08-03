package cn.com.thinkwatch.ihass2.bean

import android.content.Context
import android.view.View
import cn.com.thinkwatch.ihass2.R
import cn.com.thinkwatch.ihass2.bus.EntityLongClicked
import cn.com.thinkwatch.ihass2.dto.ServiceRequest
import cn.com.thinkwatch.ihass2.model.JsonEntity
import com.dylan.common.rx.RxBus2
import kotlinx.android.synthetic.main.tile_switch.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SwitchBean(entity: JsonEntity): BaseBean(entity) {
    override fun layoutResId(): Int = R.layout.tile_switch
    override fun bindToView(itemView: View, context: Context) {
        if (entity.isStateful) {
            itemView.stateful.visibility = View.VISIBLE
            itemView.stateless.visibility = View.GONE
            itemView.friendlyName.text = if (entity.showName.isNullOrBlank()) entity.friendlyName else entity.showName
            itemView.group.text = entity.groupName
            itemView.state.text = entity.iconState
            itemView.isActivated = entity.isActivated
            itemView.state.isActivated = entity.isActivated
            itemView.group.isActivated = entity.isActivated
            itemView.friendlyName.isActivated = entity.isActivated
            itemView.stateful.onClick { RxBus2.getDefault().post(ServiceRequest(entity.domain, "toggle", entity.entityId))}
            itemView.stateful.setOnLongClickListener {
                RxBus2.getDefault().post(EntityLongClicked(entity))
                true
            }
        } else {
            itemView.stateful.visibility = View.GONE
            itemView.stateless.visibility = View.VISIBLE
            itemView.friendlyName.isActivated = false
            itemView.friendlyName1.text = if (entity.showName.isNullOrBlank()) entity.friendlyName else entity.showName
            itemView.turnOn.onClick { RxBus2.getDefault().post(ServiceRequest(entity.domain, "turn_on", entity.entityId)) }
            itemView.turnOff.onClick { RxBus2.getDefault().post(ServiceRequest(entity.domain, "turn_off", entity.entityId)) }
            itemView.turnOn.setOnLongClickListener {
                RxBus2.getDefault().post(EntityLongClicked(entity))
                true
            }
            itemView.turnOff.setOnLongClickListener {
                RxBus2.getDefault().post(EntityLongClicked(entity))
                true
            }
            itemView.stateless.setOnLongClickListener {
                RxBus2.getDefault().post(EntityLongClicked(entity))
                true
            }
        }
        itemView.contentView.setOnTouchListener(getTouchListener(itemView.contentView))
    }
}