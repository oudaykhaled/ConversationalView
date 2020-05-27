package com.ouday.lib.conversationalui

import com.ouday.lib.conversational.Step

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
interface OnConversationProgressUpdateListener<DataModelType> {
    fun onConversationalProgressUpdate(
        dataModel: DataModelType,
        step: Step<DataModelType>,
        percentage: Int,
        done: Boolean
    )
}
