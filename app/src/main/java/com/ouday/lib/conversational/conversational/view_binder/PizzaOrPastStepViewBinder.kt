package com.ouday.lib.conversational.conversational.view_binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ouday.lib.conversational.R
import com.ouday.lib.conversational.TwoButtonsView
import com.ouday.lib.conversational.conversational.FoodType
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import kotlinx.android.synthetic.main.step_food_or_drink.view.*
import kotlinx.android.synthetic.main.step_summary.view.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class PizzaOrPastStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {

    override fun onCreateViewForInitialState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.step_pizza_or_pasta, parent, false)
    }

    override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            btnOption.setOnOptionSelectedListener { _, option ->
                dataModel.foodType =
                    if (option == TwoButtonsView.Option.OPTION_1) FoodType.PIZZA else FoodType.PASTA
                notifyDataChange()
            }
        }
    }

    override fun onCreateViewForSummaryState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)
    }

    override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            tvTitleLabel.text = "Food selected"
            tvSummary.text = if (dataModel.foodType == FoodType.PIZZA) "Pizza" else "Pasta"
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