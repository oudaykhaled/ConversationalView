package com.ouday.lib.conversational

import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import java.util.*
import kotlin.collections.HashMap

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class ConversationFlow<DataModelType>(
    private val flow: LinkedList<LinkedList<Step<DataModelType>>>
) {

    private val mapStepLevels = HashMap<Step<DataModelType>, Int>()

    init {
        flow.forEach {
            val level = flow.indexOf(it)
            it.forEach { step ->
                mapStepLevels[step] = level
            }
        }
    }

    fun iterateThroughAllSteps(iterator: ((Step<DataModelType>) -> Unit)){
        flow.forEach {
            it.forEach { step ->
                iterator.invoke(step)
            }
        }
    }

    fun getLevelsCount() = flow.size

    fun getStepCountAtLevel(levelIndex: Int) = flow[levelIndex].size

    fun getStep(levelIndex: Int, stepIndex: Int) = flow[levelIndex][stepIndex]

    fun getStepsAtLevel(levelIndex: Int) = flow[levelIndex]

    fun findStepLevel(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>): Int {
        interchangeableStepViewBinder.getStep()?.let { return findStepLevel(it) }
        return 0
    }

    fun findStepLevel(step: Step<DataModelType>): Int {
        mapStepLevels[step]?.let { return it }
        return 0
    }
}

