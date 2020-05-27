package com.ouday.lib.conversational

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
abstract class StepRequirementValidator<DataModelType> {
    var dataModel: DataModelType? = null
    abstract fun areAllRequirementsValid(): Boolean
    abstract fun clear()
}
