package com.ouday.lib.conversational

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.conversational.requirement_validator.*
import com.ouday.lib.conversational.conversational.view_binder.*
import com.ouday.lib.conversationalui.ConversationalViewAdapter
import com.ouday.lib.conversationalui.OnConversationProgressUpdateListener
import kotlinx.android.synthetic.main.activity_main.*

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class MainActivity : AppCompatActivity(), OnConversationProgressUpdateListener<OrderDataModel> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    private fun initViews() {

        //Initialize DataModel, Conversational Manager will take care of injecting
        //this data model into all StepViewBinders and StepRequirementValidator
        val dataModel = OrderDataModel()

        //Preparing all steps
        val nameStep = Step(NameRequirementValidator(), NameStepViewBinder())
        val orderTypeStep = Step(FoodOrDrinkRequirementValidator(), FoodOrDrinkStepViewBinder())
        val foodTypeStep = Step(PizzaOrPastaRequirementValidator(), PizzaOrPastStepViewBinder())
        val drinkTypeStep = Step(BeerOrJuiceRequirementValidator(), BeerOrJuiceStepViewBinder())
        //This is the last step, it requires an extra validator so the conversational manager
        //knows that the flow is filled successfully
        val captchaStep = Step(CaptchaRequirementValidator(), CaptchaStepViewBinder(), whereAmI = WhereAmI.LastStep {
            return@LastStep validateConversationalAtCaptchaStep(dataModel)
        })

        //Building the flow(Graph)
        val flow = ConversationalFlowBuilder<OrderDataModel>()
            .add(nameStep)
            .add(orderTypeStep)
            .add(foodTypeStep, drinkTypeStep)
            .add(captchaStep)
            .build(dataModel)
        
        rvConversationalView.layoutManager = LinearLayoutManager(this)
        val adapter = ConversationalViewAdapter(dataModel).setOnConversationProgressUpdate(this)
        rvConversationalView.adapter = adapter
        ConversationalManager(adapter, flow).notifyAllData()
    }

    private fun validateConversationalAtCaptchaStep(dataModel: OrderDataModel): LastStepReadiness {
        return if (dataModel?.isCaptchaValidated == true) LastStepReadiness.READY else LastStepReadiness.NOT_READY
    }

    override fun onConversationalProgressUpdate(
        dataModel: OrderDataModel,
        step: Step<OrderDataModel>,
        percentage: Int,
        isDone: Boolean
    ) {
        btnContinue.isEnabled = isDone
        progressBar.progress = percentage
    }


}
