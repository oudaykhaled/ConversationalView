package com.ouday.lib.conversational.conversational.requirement_validator

import com.ouday.lib.conversational.StepRequirementValidator
import com.ouday.lib.conversational.conversational.OrderDataModel

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class CaptchaRequirementValidator: StepRequirementValidator<OrderDataModel>() {

    override fun areAllRequirementsValid(): Boolean {
        return dataModel?.foodType != null || dataModel?.drinkType != null
    }

    override fun clear() {
        dataModel?.isCaptchaValidated = null
    }
}