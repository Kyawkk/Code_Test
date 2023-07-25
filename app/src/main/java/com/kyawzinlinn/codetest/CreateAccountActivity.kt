package com.kyawzinlinn.codetest

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.util.Patterns
import android.widget.DatePicker
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.kyawzinlinn.codetest.databinding.ActivityCreateAccountBinding
import com.kyawzinlinn.codetest.utils.Gender

class CreateAccountActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCreateAccountBinding
    private var gender = Gender.FEMALE
    private lateinit var viewModel: FormViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCreateAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpClickListeners()

        viewModel = ViewModelProvider(this).get(FormViewModel::class.java)
        if (savedInstanceState != null) bindUi()

    }

    private fun bindUi() {
        // get data from orientation changes and show
        binding.apply {
            edFirstName.editText?.setText(viewModel.firstName)
            edLastName.editText?.setText(viewModel.lastName)
            edEmail.editText?.setText(viewModel.email)
            edDateOfBirth.editText?.setText(viewModel.dateOfBirth)
            edNationality.editText?.setText(viewModel.nationality)
            edCountry.editText?.setText(viewModel.country)
            edCountryCode.setText(viewModel.code)
            edPhoneNumber.setText(viewModel.phoneNo)
            Handler(Looper.getMainLooper()).post {
                genderSlider.setGender(viewModel.gender)
            }

            Log.d("TAG", "bindUi: $gender")
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        binding.apply {
            viewModel.firstName = edFirstName.editText?.text.toString()
            viewModel.lastName = edLastName.editText?.text.toString()
            viewModel.email = edEmail.editText?.text.toString()
            viewModel.dateOfBirth = edDateOfBirth.editText?.text.toString()
            viewModel.nationality = edNationality.editText?.text.toString()
            viewModel.country = edCountry.editText?.text.toString()
            viewModel.code = edCountryCode.text.toString()
            viewModel.gender = gender.toString()

            Log.d("TAG", "onSaved: $gender")
        }
    }

    private fun setUpClickListeners() {
        binding.apply {
            ivCreateAccountBack.setOnClickListener { onBackPressed() }
            genderSlider.setOnGenderSelectedListener {
                gender = it
            }

            edDateOfBirth.editText?.requestFocus()
            edDateOfBirth.editText?.setOnClickListener {
                showDatePickerDialog {
                    edDateOfBirth.editText?.setText(it)
                }
            }
            btnCreateNewAccount.setOnClickListener { validateForm() }
        }
    }

    private fun showDatePickerDialog(onDatePicked: (String) -> Unit){
        val datePicker = DatePickerDialog(this)
        datePicker.setOnDateSetListener { datePicker, year, month, day ->
            onDatePicked("$day/$month/$year")
        }
        datePicker.show()
    }

    private fun validateForm() {

        val isFirstNameValid = validateFirstName()
        val isLastNameValid = validateLastName()
        val isEmailValid = validateEmail()
        val isNationalityValid = validateNationality()
        val isCountryValid = validateCountryOfResidence()
        val isCountryCodeValid = validateCountryCode()
        val isPhoneNumberValid = validatePhoneNumber()
        val isDateOfBrithValid = validateDateOfBirth()

        if (isFirstNameValid && isLastNameValid && isEmailValid && isNationalityValid && isCountryValid && isCountryCodeValid && isPhoneNumberValid && isDateOfBrithValid){
            Snackbar.make(window.decorView,"Validation Successful!", Snackbar.LENGTH_SHORT).show()
        }

    }

    private fun validateFirstName(): Boolean {
        val firstName = binding.edFirstName.editText?.text.toString().trim()
        return when {
            firstName.isEmpty() -> {
                binding.edFirstName.error = "First Name can not be empty"
                false
            }
            else -> {
                binding.edFirstName.error = null
                true
            }
        }
    }

    private fun validateLastName(): Boolean {
        val lastName = binding.edLastName.editText?.text.toString().trim()
        return when {
            lastName.isEmpty() -> {
                binding.edLastName.error = "Last Name can not be empty"
                false
            }

            else -> {
                binding.edLastName.error = null
                true
            }
        }
    }

    private fun validateEmail(): Boolean {
        val email = binding.edEmail.editText?.text.toString().trim()
        if (email.isEmpty() || email.length == 0) {
            binding.edEmail.error = "Email can not be empty"
            return false
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.edEmail.error = "Incorrect format!"
            return false
        }
        else {
            binding.edEmail.error = null
            return true
        }
    }

    private fun validateDateOfBirth(): Boolean{
        val dateOfBirth = binding.edDateOfBirth.editText?.text.toString().trim()
        if (dateOfBirth.isEmpty() || dateOfBirth.length == 0) {
            binding.edDateOfBirth.error = "Date Of Birth can not be empty"
            return false
        }
        else {
            binding.edDateOfBirth.error = null
            return true
        }
    }


    private fun validateNationality(): Boolean {
        val nationality = binding.edNationality.editText?.text.toString().trim()
        return when {
            nationality.isEmpty() -> {
                binding.edNationality.error = "Nationality can not be empty"
                false
            }
            else -> {
                binding.edNationality.error = null
                true
            }
        }
    }

    private fun validateCountryOfResidence(): Boolean {
        val country = binding.edCountry.editText?.text.toString().trim()
        return when {
            country.isEmpty() -> {
                binding.edCountry.error = "Country can not be empty"
                false
            }
            else -> {
                binding.edCountry.error = null
                true
            }
        }
    }

    private fun validateCountryCode(): Boolean {
        val countryCode = binding.edCountryCode.text.toString().trim()
        return when {
            countryCode.isEmpty() -> {
                binding.edCountryCode.error = "Country code can not be empty"
                false
            }
            else -> {
                binding.edCountryCode.error = null
                true
            }
        }
    }

    private fun validatePhoneNumber(): Boolean{
        var phoneNumber = binding.edPhoneNumber.text.toString().trim()
        return when {
            phoneNumber.isEmpty() -> {
                binding.edPhoneNumber.error = "Phone number can not be empty"
                false
            }
            else -> {
                binding.edPhoneNumber.error = null
                true
            }
        }
    }
}