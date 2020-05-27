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
        interchangeableStepViewBinder?.let { it ->

            it.step?.let { step ->
                val whereAmI = it.step?.whereAmI
                if (whereAmI is WhereAmI.LastStep) {
                    if (whereAmI.checkIfReady?.invoke() == LastStepReadiness.READY) {
                        uiContact.onConversationProgressUpdate(it.step!!, percentage = 100, isDone = true)
                    } else {
                        uiContact.onConversationProgressUpdate(it.step!!, percentage = 100)
                    }
                    return
                }
            }

            levelIndex = flow.findStepLevel(it)!!
            levelIndex?.let { index -> levelIndex = index + 1 }
        }
        val firstValidStep = levelIndex?.let { findFirstValidStepAtLevel(it) }
        if (firstValidStep == null) println("No valid step")
        uiContact.onConversationProgressUpdate(firstValidStep!!, percentage = computePercentage(firstValidStep))
        firstValidStep?.let { step ->
            if (interchangeableStepViewBinder?.hasSummaryState() == true) {
                interchangeableStepViewBinder?.let { switchToSummaryState(it) }
            }
            steps.forEach { it.status = StepStatus.SUMMARY_STATUS }
            step.status = StepStatus.INITIAL_STATE
            steps.add(step)
            val stepsUpdated = LinkedList<Step<DataModelType>>()
            stepsUpdated.add(step)
            //Notify the ui
            levelIndex?.let { uiContact.setData(it, stepsUpdated, steps) }!!
        }
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
        levelIndex?.let { uiContact.switchToInitialState(it) }
    }

    override fun switchToSummaryState(interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>) {
        interchangeableStepViewBinder.getStep()?.status = StepStatus.SUMMARY_STATUS
        val levelIndex = flow.findStepLevel(interchangeableStepViewBinder)
        levelIndex?.let { uiContact.switchToSummaryState(it) }
    }


}
