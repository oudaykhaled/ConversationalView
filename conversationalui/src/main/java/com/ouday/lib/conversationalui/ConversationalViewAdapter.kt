package com.ouday.lib.conversationalui

import android.os.Handler
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_IDLE
import com.ouday.lib.conversational.Step
import com.ouday.lib.conversational.ViewState
import com.ouday.lib.conversational.uicontract.ConversationUIContract
import java.util.*

private const val DELAY_UNTIL_RECYCLER_VIEW_READY = 30L

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class ConversationalViewAdapter<DataModelType>(val dataModel: DataModelType) :
    RecyclerView.Adapter<ConversationalViewAdapter.GenericViewHolder>(),
    ConversationUIContract<DataModelType> {

    private var recyclerView: RecyclerView? = null

    private val stepViewBinderMap = StepViewBinderMap<DataModelType>()
    private var onConversationProgressUpdateListener: OnConversationProgressUpdateListener<DataModelType>? = null

    override fun onConversationProgressUpdate(
        step: Step<DataModelType>,
        percentage: Int,
        isDone: Boolean
    ) {
        this.onConversationProgressUpdateListener?.onConversationalProgressUpdate(dataModel, step, percentage, isDone)
    }

    fun setOnConversationProgressUpdate(onConversationProgressUpdateListener: OnConversationProgressUpdateListener<DataModelType>)
        : ConversationalViewAdapter<DataModelType> {
        this.onConversationProgressUpdateListener = onConversationProgressUpdateListener
        return this
    }

    override fun getItemViewType(position: Int): Int {
        return stepViewBinderMap.getIdAtIndex(position)!!.toInt()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder {
        if (recyclerView == null) recyclerView = parent as RecyclerView
        val viewBinder = stepViewBinderMap.getViewBinderByViewType(viewType.toLong())!!
        return if (viewBinder.state == ViewState.INITIAL_STATE) GenericViewHolder(
            viewBinder.onCreateViewForInitialState(
                parent
            )
        )
        else GenericViewHolder(viewBinder.onCreateViewForSummaryState(parent))
    }

    override fun getItemCount(): Int {
        return stepViewBinderMap.count()
    }

    override fun onBindViewHolder(holder: GenericViewHolder, position: Int) {
        val viewBinder = stepViewBinderMap.getViewBinderAtIndex(position)
        if (viewBinder.state == ViewState.INITIAL_STATE) {
            viewBinder.onUnBindInitialState(dataModel, holder.itemView)
            viewBinder.onBindViewForInitialState(dataModel, holder.itemView)
        } else {
            viewBinder.onUnBindSummaryState(dataModel, holder.itemView)
            viewBinder.onBindViewForSummaryState(dataModel, holder.itemView)
        }
    }

    class GenericViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    @Synchronized
    override fun setData(
        startFrom: Int,
        stepsUpdated: LinkedList<Step<DataModelType>>,
        allSteps: LinkedList<Step<DataModelType>>
    ) {
        stepViewBinderMap.setViewBinders(
            startFrom,
            LinkedList(stepsUpdated.map { it.interchangeableStepViewBinder }.toList())
        )
        safeNotifyDataSetChanged()
    }

    @Synchronized
    override fun switchToInitialState(stepIndex: Int) {
        stepViewBinderMap.getViewBinderAtIndex(stepIndex).state = ViewState.INITIAL_STATE
        safeNotifyItemChanged(stepIndex)
    }

    @Synchronized
    override fun switchToSummaryState(stepIndex: Int) {
        stepViewBinderMap.getViewBinderAtIndex(stepIndex).state = ViewState.SUMMARY_STATE
        safeNotifyItemChanged(stepIndex)
    }

    @Synchronized
    fun safeNotifyDataSetChanged() {
        if (recyclerView == null) {
            notifyDataSetChanged()
            return
        }
        recyclerView?.let {
            if (!it.isComputingLayout && it.scrollState == SCROLL_STATE_IDLE) {
                notifyDataSetChanged()
            } else {
                Handler().postDelayed(
                    { safeNotifyDataSetChanged() },
                    DELAY_UNTIL_RECYCLER_VIEW_READY
                )
            }
        }
    }

    @Synchronized
    fun safeNotifyItemChanged(index: Int) {
        if (recyclerView == null) {
            notifyItemChanged(index)
            return
        }
        recyclerView?.let {
            if (!(it.isComputingLayout || it.scrollState != SCROLL_STATE_IDLE)) {
                notifyItemChanged(index)
            } else {
                Handler().postDelayed({ notifyItemChanged(index) }, DELAY_UNTIL_RECYCLER_VIEW_READY)
            }
        }
    }

}
