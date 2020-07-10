package com.ouday.lib.conversational

import com.ouday.lib.conversational.uicontract.InterchangeableStepViewBinder

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class Step<DataModelType>(
    internal val stepRequirementValidator: StepRequirementValidator<DataModelType>,
    val interchangeableStepViewBinder: InterchangeableStepViewBinder<DataModelType>,
    val whereAmI: WhereAmI = WhereAmI.NotLastStep
) {

    private var _dataModel: DataModelType? = null
    internal var dataModel: DataModelType?
        get() {
            return _dataModel
        }
        internal set(value){
            _dataModel = value
            stepRequirementValidator.dataModel = value
        }

    private var _status: StepStatus? = null
    var status: StepStatus?
        set(value) {_status = value}
        get() = _status

}

enum class StepStatus{ INITIAL_STATE, SUMMARY_STATUS }
enum class LastStepReadiness { READY, NOT_READY }

sealed class WhereAmI(val checkIfReady: (() -> LastStepReadiness)? = null) {
    object NotLastStep: WhereAmI()
    class  LastStep(checkIfReady: () -> LastStepReadiness): WhereAmI(checkIfReady)
}


