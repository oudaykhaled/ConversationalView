package com.ouday.lib.conversational.conversational.requirement_validator

import com.ouday.lib.conversational.StepRequirementValidator
import com.ouday.lib.conversational.conversational.OrderDataModel

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class FoodOrDrinkRequirementValidator: StepRequirementValidator<OrderDataModel>() {

    override fun areAllRequirementsValid(): Boolean {
        return !dataModel?.name.isNullOrEmpty()
    }

    override fun clear() {
        dataModel?.orderType = null
    }
}