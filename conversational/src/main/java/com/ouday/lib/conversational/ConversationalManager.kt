package com.ouday.lib.conversational

import com.ouday.lib.conversational.uicontract.ConversationUIContract
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import java.util.*
import kotlin.math.roundToInt

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class ConversationalManager<DataModelType>(
    private var uiContact: ConversationUIContract<DataModelType>,
    private var flow: ConversationFlow<DataModelType>
) : ConversationManagerNotifier<DataModelType> {

    val steps = LinkedList<Step<DataModelType>>()

    init {
        // Inject ConversationalManagerNotifier contract into all steps
        flow?.iterateThroughAllSteps { step ->
            step.interchangeableStepViewBinder.setConversationalManagerNotifier(
                this
            )
        }
        steps.add(flow.getStep(0, 0))
    }

    private fun findFirstValidStepAtLevel(levelIndex: Int): Step<DataModelType>? {
        return flow.getStepsAtLevel(levelIndex)
            .find { it.stepRequirementValidator.areAllRequirementsValid() }
    }

    fun notifyAllData(): ConversationalManager<DataModelType> {
        if (steps.count() < 1) return this
        notifyManagerAtStep(null)
        return this
    }

    override fun notifyManagerAtStep(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>?) {
        var levelIndex: Int? = 0
        var firstValidStep: Step<DataModelType>? = null
        interchangeableStepViewBinder?.let { it ->

            it.getStep()?.let { step ->
                val whereAmI = it.getStep()?.whereAmI
                if (whereAmI is WhereAmI.LastStep) {
                    if (whereAmI.checkIfReady?.invoke() == LastStepReadiness.READY) {
                        uiContact.onConversationProgressUpdate(
                            step,
                            percentage = 100,
                            isDone = true
                        )
                    } else {
                        uiContact.onConversationProgressUpdate(
                            step,
                            percentage = computePercentage(step)
                        )
                    }
                    return
                }
            }

            levelIndex = flow.findStepLevel(it)
            levelIndex?.let { index -> levelIndex = index + 1 }
        }
        firstValidStep = levelIndex?.let { findFirstValidStepAtLevel(it) }
        if (firstValidStep == null) {
            println("No valid step")
            if (steps.last != interchangeableStepViewBinder){
                //Clear/Remove non-eligiable steps
                removeNonEliigiableSteps(steps)
                //Notify the ui
                levelIndex?.let { uiContact.setData(it, LinkedList<Step<DataModelType>>(), steps) }!!
                uiContact.onConversationProgressUpdate(interchangeableStepViewBinder!!.getStep()!!, percentage = computePercentage(interchangeableStepViewBinder!!.getStep()!!))
            }
            return
        }
        uiContact.onConversationProgressUpdate(
            firstValidStep!!,
            percentage = computePercentage(firstValidStep)
        )
        firstValidStep?.let { step ->
            if (interchangeableStepViewBinder?.hasSummaryState() == true) {
                interchangeableStepViewBinder?.let { switchToSummaryState(it) }
            }
            steps.forEach { it.status = StepStatus.SUMMARY_STATUS }
            step.status = StepStatus.INITIAL_STATE
            steps.add(step)
            val stepsUpdated = LinkedList<Step<DataModelType>>()
            stepsUpdated.add(step)


            //Clear/Remove non-eligiable steps
            removeNonEliigiableSteps(stepsUpdated)


            //Notify the ui
            levelIndex?.let { uiContact.setData(it, stepsUpdated, steps) }!!
            uiContact.onConversationProgressUpdate(step, percentage = computePercentage(step))
        }

        //If there is a valid step pending,
        //Notify UI
        firstValidStep?.let {
            notifyManagerAtStep(firstValidStep.interchangeableStepViewBinder)
        }

    }

    private fun removeNonEliigiableSteps(stepsUpdated: LinkedList<Step<DataModelType>>) {
        val level = flow.findStepLevel(stepsUpdated.last)
        val lstToRemove = LinkedList<Step<DataModelType>>()
        steps.forEach {
            if (flow.findStepLevel(it) > level) {
                lstToRemove.add(it)
                it.stepRequirementValidator.clear()
            }
        }
        steps.removeAll(lstToRemove)
    }

    fun computePercentage(step: Step<DataModelType>): Int {
        return (
                (flow.findStepLevel(step).toDouble() / flow.getLevelsCount().toDouble())
                        * 100.toDouble())
            .roundToInt()
    }

    override fun switchToInitialState(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>) {
        interchangeableStepViewBinder.getStep()?.status = StepStatus.INITIAL_STATE
        val levelIndex = flow.findStepLevel(interchangeableStepViewBinder)
        levelIndex.let { uiContact.switchToInitialState(it) }
    }

    override fun switchToSummaryState(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>) {
        interchangeableStepViewBinder.getStep()?.status = StepStatus.SUMMARY_STATUS
        val levelIndex = flow.findStepLevel(interchangeableStepViewBinder)
        levelIndex.let { uiContact.switchToSummaryState(it) }
    }


}