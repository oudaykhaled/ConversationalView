package com.ouday.lib.conversational.conversational

/**
 * @author Ouday Khaled
 * Last update on 27-05-2020
 */
data class OrderDataModel (
    var name: String? = null,               // null => not answered yet
    var orderType: OrderType? = null,       // null => not answered yet
    var foodType: FoodType? = null,         // null => not answered yet
    var drinkType: DrinkType? = null,       // null => not answered yet
    var isCaptchaValidated: Boolean? = null // null => not answered yet
)

enum class OrderType{ FOOD, DRINK }
enum class FoodType{ PIZZA, PASTA }
enum class DrinkType{ BEER, JUICE }
