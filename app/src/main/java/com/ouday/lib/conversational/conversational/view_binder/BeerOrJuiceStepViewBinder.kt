package com.ouday.lib.conversational.conversational.view_binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ouday.lib.conversational.R
import com.ouday.lib.conversational.TwoButtonsView
import com.ouday.lib.conversational.conversational.DrinkType
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import kotlinx.android.synthetic.main.step_beer_or_juice.view.*
import kotlinx.android.synthetic.main.step_summary.view.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class BeerOrJuiceStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {

    override fun onCreateViewForInitialState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.step_beer_or_juice, parent, false)
    }

    override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            btnOption.setOnOptionSelectedListener { _, option ->
                dataModel.drinkType =
                    if (option == TwoButtonsView.Option.OPTION_1) DrinkType.BEER else DrinkType.JUICE
                notifyDataChange()
            }
        }
    }

    override fun onCreateViewForSummaryState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)
    }

    override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            tvTitleLabel.text = "Drink selected"
            tvSummary.text = if (dataModel.drinkType == DrinkType.BEER) "Beer" else "Juice"
            root.setOnClickListener {
                switchToInitialState()
            }
        }
    }

}