package com.ouday.lib.conversationalui

import com.ouday.lib.conversational.StepStatus
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import java.util.*
import kotlin.collections.HashMap

private const val VIEW_TYPE_OFFSET = 1000

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class StepViewBinderMap<DataModelType> {

    private val mapItemIds = HashMap<InterchangeableStepViewBinder<DataModelType>, Long>()
    private val mapViewBinders = HashMap<Long, InterchangeableStepViewBinder<DataModelType>>()
    private val lstBinders = LinkedList<InterchangeableStepViewBinder<DataModelType>>()
    private val viewTypeCounter = ViewTypeCounter()

    fun getViewBinderAtIndex(index: Int): InterchangeableStepViewBinder<DataModelType> {
        return lstBinders[index]
    }

    fun getIdAtIndex(index: Int): Long? {
        return if (lstBinders[index].getStep()?.status == StepStatus.INITIAL_STATE || !lstBinders[index].hasSummaryState()) {
            mapItemIds[getViewBinderAtIndex(index)]
        } else {
            mapItemIds[getViewBinderAtIndex(index)]?.times(VIEW_TYPE_OFFSET)
        }
    }

    fun getViewBinderByViewType(viewType: Long): InterchangeableStepViewBinder<DataModelType>? {
        return mapViewBinders[if (viewType >= VIEW_TYPE_OFFSET) viewType / VIEW_TYPE_OFFSET else viewType]
    }

    fun count(): Int {
        return lstBinders.count()
    }

    @Synchronized
    fun setViewBinders(startFrom: Int, stepsUpdated: LinkedList<InterchangeableStepViewBinder<DataModelType>>) {
        var counter = 1L
        var lstToRemove = LinkedList(lstBinders.subList(startFrom, lstBinders.size))

        lstToRemove.forEach {
            mapViewBinders.remove(mapItemIds[it])
            mapItemIds.remove(it)
            lstBinders.remove(it)
        }

        for (it in stepsUpdated) {
            mapItemIds[it] = viewTypeCounter.getValue() + counter
            mapViewBinders[viewTypeCounter.getValue() + counter] = it
            lstBinders.add(it)
            counter++
        }

        viewTypeCounter.incrementBy(counter)


    }

    class ViewTypeCounter {

        private var viewTypeCounter = 1L

        @Synchronized
        fun incrementBy(num: Long) {
            viewTypeCounter += num
        }

        @Synchronized
        fun getValue() = viewTypeCounter

    }

}


