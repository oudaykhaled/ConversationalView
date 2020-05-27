package com.ouday.lib.conversational.uicontract

import com.ouday.lib.conversational.Step
import java.util.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
interface ConversationUIContract<DataModelType> {

    fun setData(startFrom: Int, stepsUpdated: LinkedList<Step<DataModelType>>, allSteps: LinkedList<Step<DataModelType>>)
    fun switchToInitialState(stepIndex: Int)
    fun switchToSummaryState(stepIndex: Int)
    fun onConversationProgressUpdate(
        step: Step<DataModelType>,
        percentage: Int,
        isDone: Boolean = false
    )
}
