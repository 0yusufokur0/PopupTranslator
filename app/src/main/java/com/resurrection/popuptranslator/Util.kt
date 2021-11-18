package com.resurrection.popuptranslator

fun  getModelFieldValue(model:Any,fieldName:String):Any?{
    val clazz = model.javaClass
    val field = clazz.getDeclaredField(fieldName)
    field.isAccessible = true
    return field.get(model)
}

fun getModelFieldsValues(model:Any):Map<String,Any>{
    val clazz = model.javaClass
    val fields = clazz.declaredFields
    val map = mutableMapOf<String,Any>()
    for (field in fields){
        field.isAccessible = true
        //key             //value
        map[field.name] = field.get(model)
    }
    return map
}