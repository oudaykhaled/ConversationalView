package com.ouday.lib.conversational.conversational.view_binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ouday.lib.conversational.R
import com.ouday.lib.conversational.TwoButtonsView
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.conversational.OrderType
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import kotlinx.android.synthetic.main.step_food_or_drink.view.*
import kotlinx.android.synthetic.main.step_summary.view.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class FoodOrDrinkStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {

    override fun onCreateViewForInitialState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_food_or_drink, parent, false)
    }

    override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            btnOption.setOnOptionSelectedListener { _, option ->
                dataModel.orderType = if (option == TwoButtonsView.Option.OPTION_1) OrderType.FOOD else OrderType.DRINK
                notifyDataChange()
            }
        }
    }

    override fun onCreateViewForSummaryState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)
    }

    override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            tvTitleLabel.text = "You selected"
            tvSummary.text = if (dataModel.orderType == OrderType.FOOD) "Food" else "Drink"
            root.setOnClickListener {
                switchToInitialState()
            }
        }
    }

    override fun onUnBindInitialState(dataModel: OrderDataModel, itemView: View) {
    }

    override fun onUnBindSummaryState(dataModel: OrderDataModel, itemView: View) {
    }
}