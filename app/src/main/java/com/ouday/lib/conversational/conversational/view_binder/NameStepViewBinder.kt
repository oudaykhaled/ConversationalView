package com.ouday.lib.conversational.conversational.view_binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ouday.lib.conversational.R
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import kotlinx.android.synthetic.main.step_enter_name.view.*
import kotlinx.android.synthetic.main.step_summary.view.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class NameStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {

    override fun onCreateViewForInitialState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_enter_name, parent, false)
    }

    override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            btnDone.setOnClickListener {
                dataModel.name = etName.text.toString()
                notifyDataChange()
            }
        }
    }

    override fun onCreateViewForSummaryState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)
    }

    override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            tvTitleLabel.text = "Welcome to our Restaurant"
            tvSummary.text = dataModel.name
            root.setOnClickListener {
                switchToInitialState()
            }
        }
    }

}
