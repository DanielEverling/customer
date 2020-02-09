package com.customer.commands

import com.cross.commands.BaseCommand

data class CreateCustomerCommand (val completeName : String, val nickName : String, val email: String) : BaseCommand()