package com.ouday.lib.conversational.conversational.view_binder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.jakewharton.rxbinding4.widget.textChanges
import com.ouday.lib.conversational.R
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder
import io.reactivex.rxjava3.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.step_enter_captcha.view.*
import kotlinx.android.synthetic.main.step_summary.view.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
private const val CAPTCHA = "1122"
class CaptchaStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {

    private var compositeDisposable = CompositeDisposable()

    override fun onCreateViewForInitialState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context)
            .inflate(R.layout.step_enter_captcha, parent, false)
    }

    override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            var isValidated = CAPTCHA.contentEquals(etCaptcha.text.toString())
            compositeDisposable.add(
                etCaptcha.textChanges().subscribe { charSequence ->
                    val newStatus = CAPTCHA.contentEquals(charSequence.toString())
                    if (newStatus xor isValidated) {
                        dataModel.isCaptchaValidated = newStatus
                        notifyDataChange()
                    }
                    isValidated = newStatus
                })
        }
    }

    override fun onCreateViewForSummaryState(parent: ViewGroup): View {
        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)
    }

    override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {
        itemView.apply {
            tvTitleLabel.text = "Additional Description"
            tvSummary.text = "Verified"
            root.setOnClickListener {
                switchToInitialState()
            }
        }
    }

    override fun onUnBindInitialState(dataModel: OrderDataModel, itemView: View) {
        compositeDisposable.dispose()
        compositeDisposable = CompositeDisposable()
    }


}