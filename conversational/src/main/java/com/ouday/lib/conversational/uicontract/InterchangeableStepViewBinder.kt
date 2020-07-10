package com.ouday.lib.conversational.uicontract

import android.view.View
import android.view.ViewGroup
import com.ouday.lib.conversational.ConversationManagerNotifier
import com.ouday.lib.conversational.Step
import com.ouday.lib.conversational.ViewState


/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 * Abstract class to manage UI Step creation and interactions
 * Manage the step UI in both states Initial and Summary
 * Initial State: equivalent to the edit mode view
 * Summary State: equivalent to the Locked mode view (After editing)
 */
abstract class InterchangeableStepViewBinder<DataModelType> {
    var state = ViewState.INITIAL_STATE
    lateinit var conversationManagerNotifier: ConversationManagerNotifier<DataModelType>

    private var step : Step<DataModelType>? = null


    /**
     * Retrieve the parent object (Enclosing Step)
     * @see Step
     */
    fun getStep(): Step<DataModelType>? {
        return step
    }

    /**
     * Retrieve the parent object (Enclosing Step)
     * @see Step
     */
    fun setStep(step: Step<DataModelType>?) {
        this.step = step
    }

    /**
     * Notify conversational manager about data model changde
     */
    fun notifyDataChange(){
        conversationManagerNotifier.notifyManagerAtStep(this)
    }

    /**
     * Setup the ConversationManagerNotifier
     * For more information see ConversationManagerNotifier docs
     * @see ConversationManagerNotifier
     */
    fun setConversationalManagerNotifier(conversationManagerNotifier: ConversationManagerNotifier<DataModelType>){
        this.conversationManagerNotifier = conversationManagerNotifier
    }

    /**
     * Switch to initial state without affecting data model
     * This should be called when switching and a state to edit mode
     */
    protected fun switchToInitialState() {
        conversationManagerNotifier.switchToInitialState(this)
    }

    open fun hasSummaryState(): Boolean {return true}

    /**
     * Return the initial view of this step
     * Initial step means the Edit View of the step
     */
    abstract fun onCreateViewForInitialState(parent: ViewGroup): View


    /**
     * Bind data model the edit mode view
     */
    abstract fun onBindViewForInitialState(dataModel: DataModelType, itemView: View)

    /**
     * Return the summary view of this step
     * Initial step means the View created just after finish editing of the step
     */
    abstract fun onCreateViewForSummaryState(parent: ViewGroup): View

    /**
     * Bind data model to the View created just after finish editing of the step
     */
    abstract fun onBindViewForSummaryState(dataModel: DataModelType, itemView: View)

    abstract fun onUnBindInitialState(dataModel: DataModelType, itemView: View)

    abstract fun onUnBindSummaryState(dataModel: DataModelType, itemView: View)


}
