package ua.nure.myapplication.fragments


import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.FrameLayout
import es.dmoral.toasty.Toasty
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

import ua.nure.myapplication.R
import ua.nure.myapplication.activities.MainActivity
import ua.nure.myapplication.api.RetrofitClient
import ua.nure.myapplication.api.requests.RegisterRequest
import ua.nure.myapplication.api.responses.DetailResponse
import ua.nure.myapplication.helpers.CustomRegex
import ua.nure.myapplication.storage.SharedPrefManager
import java.time.LocalDate
import java.util.*
import java.util.regex.Pattern

class RegisterFragment : Fragment(), View.OnClickListener {

    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var etPasswordConfirm: EditText
    private lateinit var btRegister: Button
    private lateinit var lsAuth: FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_register, container, false)

        etUsername = view.findViewById(R.id.et_register_username)
        etEmail = view.findViewById(R.id.et_register_email)
        etPassword = view.findViewById(R.id.et_register_password)
        etPasswordConfirm = view.findViewById(R.id.et_register_password_confirm)

        btRegister = view.findViewById(R.id.bt_register)
        btRegister.setOnClickListener(this)

        lsAuth = activity!!.findViewById(R.id.ls_auth)

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_register -> register()
        }
    }

    private fun register() {
        val username = etUsername.text.toString()
        val email = etEmail.text.toString()
        val password1 = etPassword.text.toString()
        val password2 = etPasswordConfirm.text.toString()

        val request = RegisterRequest(username, email, password1, password2)

        if (validateRegisterRequest(request)) {
            lsAuth.visibility = View.VISIBLE
            val call = RetrofitClient.getInstance(activity!!).api.register(request)

            call.enqueue(object : Callback<DetailResponse> {
                override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                    lsAuth.visibility = View.GONE
                    Toasty.error(
                        activity!!,
                        t.message!!,
                        Toasty.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<DetailResponse>,
                    response: Response<DetailResponse>
                ) {
                    if (response.body() != null) {
                        Toasty.info(activity!!, response.body()!!.detail, Toasty.LENGTH_LONG).show()
                    } else {
                        Toasty.error(
                            activity!!,
                            getString(R.string.something_wrong),
                            Toasty.LENGTH_LONG
                        ).show()
                    }
                    lsAuth.visibility = View.GONE
                }

            })
        }
    }

    private fun validateRegisterRequest(registerRequest: RegisterRequest): Boolean {
        val usernameRegex = Regex(CustomRegex.USERNAME)


        when {

            registerRequest.username.isEmpty() -> {
                etUsername.error = getString(R.string.username_required)
                return false
            }

            !usernameRegex.matches(registerRequest.username) -> {
                etUsername.error = getString(R.string.wrong_username)
                return false
            }

            registerRequest.password1.isEmpty() -> {
                etPassword.error = getString(R.string.password_required)
                return false
            }


            registerRequest.password1 != registerRequest.password2 -> {
                etPasswordConfirm.error = getString(R.string.passwords_not_match)
                return false
            }

            registerRequest.email.isEmpty() -> {
                etEmail.error = getString(R.string.email_required)
                return false
            }

            !Pattern.matches(Patterns.EMAIL_ADDRESS.pattern(), registerRequest.email) -> {
                etEmail.error = getString(R.string.wrong_email)
                return false
            }
        }
        return true
    }
}
