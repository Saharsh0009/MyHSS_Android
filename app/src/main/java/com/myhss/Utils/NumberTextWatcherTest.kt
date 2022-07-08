//package com.uk.myhss.Utils
//
//class NumberTextWatcherTest {
//
//    @Test
//    fun phone_number_test() {
//        val phoneNumberMask = "(###) ###-####"
//        val phoneNumberTextWatcher = NumberTextWatcher(phoneNumberMask)
//
//        val input = StringBuilder()
//        val expectedResult = "(012) 345-6789"
//        var result = ""
//
//        // mimic typing 10 digits
//        for (i in 0 until 10) {
//            input.append(i)
//            result = mimicTextInput(phoneNumberTextWatcher, result, i.toString()) ?: ""
//        }
//
//        Assert.assertEquals(input.toString(), "0123456789")
//        Assert.assertEquals(result, expectedResult)
//    }
//
//    @Test
//    fun credit_card_test() {
//        val creditCardNumberMask = "#### #### #### ####"
//        val creditCardNumberTextWatcher = NumberTextWatcher(creditCardNumberMask)
//
//        val input = StringBuilder()
//        val expectedResult = "0123 4567 8901 2345"
//        var result = ""
//
//        // mimic typing 16 digits
//        for (i in 0 until 16) {
//            val value = i % 10
//            input.append(value)
//            result = mimicTextInput(creditCardNumberTextWatcher, result, value.toString()) ?: ""
//        }
//
//        Assert.assertEquals(input.toString(), "0123456789012345")
//        Assert.assertEquals(result, expectedResult)
//    }
//
//    @Test
//    fun date_test() {
//        val dateMask = "####/##/##"
//        val dateTextWatcher = NumberTextWatcher(dateMask)
//
//        val input = "20200504"
//        val expectedResult = "2020/05/04"
//        val initialInputValue = ""
//
//        val result = mimicTextInput(dateTextWatcher, initialInputValue, input)
//
//        Assert.assertEquals(result, expectedResult)
//    }
//
//    @Test
//    fun credit_card_expiration_date_test() {
//        val creditCardExpirationDateMask = "##/##"
//        val creditCardExpirationDateTextWatcher = NumberTextWatcher(creditCardExpirationDateMask)
//
//        val input = "1121"
//        val expectedResult = "11/21"
//        val initialInputValue = ""
//
//        val result = mimicTextInput(creditCardExpirationDateTextWatcher, initialInputValue, input)
//
//        Assert.assertEquals(result, expectedResult)
//    }
//
//    private fun mimicTextInput(textWatcher: TextWatcher, initialInputValue: String, input: String): String? {
//        textWatcher.beforeTextChanged(initialInputValue, initialInputValue.length, initialInputValue.length, input.length + initialInputValue.length)
//        val newText = initialInputValue + input
//
//        textWatcher.onTextChanged(newText, 1, newText.length - 1, 1)
//        val editable: Editable = SpannableStringBuilder(newText)
//
//        textWatcher.afterTextChanged(editable)
//        return editable.toString()
//    }
//}