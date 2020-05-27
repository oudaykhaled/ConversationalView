package com.ouday.lib.conversational.uicontract

import android.view.View
import android.view.ViewGroup

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
abstract class SimpleStepViewBinder<DataModelType>: InterchangeableStepViewBinder<DataModelType>() {

    override fun hasSummaryState(): Boolean{return false}

    override fun onCreateViewForSummaryState(parent: ViewGroup): View  = View(null)

    override fun onBindViewForSummaryState(dataModel: DataModelType, itemView: View) {}

    override fun onUnBindSummaryState(dataModel: DataModelType, itemView: View){}

}
