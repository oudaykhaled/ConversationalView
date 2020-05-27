package com.ouday.lib.conversational

import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
interface ConversationManagerNotifier<DataModelType> {
    fun notifyManagerAtStep(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>?)
    fun switchToInitialState(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>)
    fun switchToSummaryState(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>)
}
