package com.ouday.lib.conversational

import java.util.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class ConversationalFlowBuilder<DataModelType> {

    private val flow = LinkedList<LinkedList<Step<DataModelType>>>()

    fun add(vararg steps: Step<DataModelType>): ConversationalFlowBuilder<DataModelType> {
        flow.add(LinkedList(steps.toList()))
        return this
    }

    fun build(dataModel: DataModelType): ConversationFlow<DataModelType> {
        // inject data model into all steps
        flow.forEach { level ->
            level.forEach { step ->
                step.dataModel = dataModel
                step.interchangeableStepViewBinder.step = step
            }
        }
        return ConversationFlow(flow)
    }

}
