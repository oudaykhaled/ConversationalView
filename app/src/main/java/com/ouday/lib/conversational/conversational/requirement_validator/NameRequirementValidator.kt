package com.ouday.lib.conversational.conversational.requirement_validator

import com.ouday.lib.conversational.StepRequirementValidator
import com.ouday.lib.conversational.conversational.OrderDataModel

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
class NameRequirementValidator: StepRequirementValidator<OrderDataModel>() {

    override fun areAllRequirementsValid(): Boolean {
        // First step should return always true
        return true
    }

    override fun clear() {
        dataModel?.name = null
    }

}