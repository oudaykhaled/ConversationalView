package com.ouday.lib.conversational.conversational.requirement_validator

import com.ouday.lib.conversational.StepRequirementValidator
import com.ouday.lib.conversational.conversational.OrderDataModel
import com.ouday.lib.conversational.conversational.OrderType

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class PizzaOrPastaRequirementValidator: StepRequirementValidator<OrderDataModel>() {

    override fun areAllRequirementsValid(): Boolean {
        return dataModel?.orderType == OrderType.FOOD
    }

    override fun clear() {
        dataModel?.foodType = null
    }

}