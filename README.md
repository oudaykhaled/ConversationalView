Soon it will be published to artifactory

# Android Conversational View

It's a library that help you build and manage custom conversational view. All what is needed is to build the steps and the graph. conversational view will manage showing the steps, validating the steps and and editing the steps.

# How It Works

[![Conversational Flow](http://img.youtube.com/vi/lNGIEwkNW2g/0.jpg)](http://www.youtube.com/watch?v=lNGIEwkNW2g "Conversational View")

## Concept

**1- DataModel** class needed to manage all data in the conversational process. Created by you, no need for any inheritance or implementation.

**2- InterchangeableStepViewBinder** Or **SimpleStepViewBinder**: Abstract class responsible for binding a single step to the UI. This class provides what layout should be inflated, how it's binded to DataModel. For each step you would like to add, you have to create for it a corresponding StepViewBinder. **SimpleStepViewBinder** has no Summary State (Will be explained later).
**InterchangeableStepViewBinder** has Summary State (Will be explained later).

**3- StepRequirementValidator**, Interface responsible for checking if all requirement needed to show the step are valid. In this class you should implement how to Conversational Manage can clear step **DataModel**

**4- ConversationFlow**, Concrete class represent the conversational flow.

**5- ConversationalFlowBuilder**, Builder class helps you build conversational flow.

**6- ConversationalManager**, Control the conversational view.


## Implementation

**Step 1: Create Data Model**

	data class OrderDataModel (  
	    var name: String? = null, // null => not answered yet  
	  var orderType: OrderType? = null, // null => not answered yet  
	  var foodType: FoodType? = null, // null => not answered yet  
	  var drinkType: DrinkType? = null, // null => not answered yet  
	  var isCaptchaValidated: Boolean? = null // null => not answered yet  
	)  
	  
	enum class OrderType{ FOOD, DRINK }  
	enum class FoodType{ PIZZA, PASTA }  
	enum class DrinkType{ BEER, JUICE }
    
 **Step 2: Create StepViewBinders for each step**
	
	    class FoodOrDrinkStepViewBinder : InterchangeableStepViewBinder<OrderDataModel>() {  
	  
	    /**  
	 * Called when manager as StepViewBinder to inflate the initial state view */  override fun onCreateViewForInitialState(parent: ViewGroup): View {  
	        return LayoutInflater.from(parent.context).inflate(R.layout.step_food_or_drink, parent, false)  
	    }  
	  
	    /**  
	 * Called when manager as StepViewBinder to bind the initial state view */  override fun onBindViewForInitialState(dataModel: OrderDataModel, itemView: View) {  
	        itemView.apply {  
	  btnOption.setOnOptionSelectedListener { _, option ->  
	  dataModel.orderType = if (option == TwoButtonsView.Option.OPTION_1) OrderType.FOOD else OrderType.DRINK  
	  notifyDataChange()  
	            }  
	 }  }  
	  
	    /**  
	 * Called when manager as StepViewBinder to inflate the summary state view */  override fun onCreateViewForSummaryState(parent: ViewGroup): View {  
	        return LayoutInflater.from(parent.context).inflate(R.layout.step_summary, parent, false)  
	    }  
	  
	    /**  
	 * Called when manager as StepViewBinder to inflate the summary state view */  override fun onBindViewForSummaryState(dataModel: OrderDataModel, itemView: View) {  
	        itemView.apply {  
	  tvTitleLabel.text = "You selected"  
	  tvSummary.text = if (dataModel.orderType == OrderType.FOOD) "Food" else "Drink"  
	  root.setOnClickListener {  
	  switchToInitialState()  
	            }  
	 }  }  
	}

 **Step 3: Create StepRequirementValidator for each step**

	class FoodOrDrinkRequirementValidator: StepRequirementValidator<OrderDataModel>() {  
  
    override fun areAllRequirementsValid(): Boolean {  
        return !dataModel?.name.isNullOrEmpty()  
    }  
  
    override fun clear() {  
        dataModel?.orderType = null  
	  }  
	}

**Step5: Create an instance of your DataModel**

	//Initialize DataModel, Conversational Manager will take care of injecting  
	//this data model into all StepViewBinders and StepRequirementValidator  
	val dataModel = OrderDataModel()

 **Step 6: Combine StepViewBinders with corresponding StepRequirementValidator in one class called Step**

In your Activity or Fragment

	//Preparing all steps  
	val nameStep = Step(NameRequirementValidator(), NameStepViewBinder())  
	val orderTypeStep = Step(FoodOrDrinkRequirementValidator(), FoodOrDrinkStepViewBinder())  
	val foodTypeStep = Step(PizzaOrPastaRequirementValidator(), PizzaOrPastStepViewBinder())  
	val drinkTypeStep = Step(BeerOrJuiceRequirementValidator(), BeerOrJuiceStepViewBinder())  


**Step 7: Last step require to be validate**

	//This is the last step, it requires an extra validator so the conversational manager  
	//knows that the flow is filled successfully  
	val captchaStep = Step(CaptchaRequirementValidator(), CaptchaStepViewBinder(), whereAmI = WhereAmI.LastStep {  
	  return@LastStep validateConversationalAtCaptchaStep(dataModel)  
	})

Create validation function

	private fun validateConversationalAtCaptchaStep(dataModel: OrderDataModel): LastStepReadiness {  
	    return if (dataModel?.isCaptchaValidated == true) LastStepReadiness.READY else LastStepReadiness.NOT_READY  
	}


>**Note: You can have multiple "Last Steps"**

**Step8: Build your flow**

	//Building the flow(Graph)  
	val flow = ConversationalFlowBuilder<OrderDataModel>()  
	    .add(nameStep)  
	    .add(orderTypeStep)  
	    .add(foodTypeStep, drinkTypeStep)  
	    .add(captchaStep)  
	    .build(dataModel)

**Step 9: Implement OnConversationProgressUpdateListener interface**

This callback interface will be called each time the conversational View updated

	class MainActivity : AppCompatActivity(), OnConversationProgressUpdateListener<OrderDataModel>

	override fun onConversationalProgressUpdate(  
	    dataModel: OrderDataModel,  
	  step: Step<OrderDataModel>,  
	  percentage: Int,  
	  isDone: Boolean  
	) {  
	    btnContinue.isEnabled = isDone  
	    progressBar.progress = percentage  
	}

**Step 10: Get the Adapter and attach it to your RecyclerView**

	rvConversationalView.layoutManager = LinearLayoutManager(this)  
	val adapter = ConversationalViewAdapter(dataModel).setOnConversationProgressUpdate(this)  
	rvConversationalView.adapter = adapter  
	ConversationalManager(adapter, flow).notifyAllData()

>**Note: You can your own UI implementation, all what you need is to create your own View that implement **ConversationUIContract** interface**
