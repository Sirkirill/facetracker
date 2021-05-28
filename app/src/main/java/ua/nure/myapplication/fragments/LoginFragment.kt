package ua.nure.myapplication.fragments


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
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
import ua.nure.myapplication.api.requests.LoginRequest
import ua.nure.myapplication.api.responses.LoginResponse
import ua.nure.myapplication.helpers.CustomRegex
import ua.nure.myapplication.storage.SharedPrefManager


class LoginFragment : Fragment(), View.OnClickListener {

    private lateinit var etUsername: EditText
    private lateinit var etPassword: EditText
    private lateinit var btLogin: Button
    private lateinit var lsAuth:FrameLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login, container, false)

        etUsername = view.findViewById(R.id.et_username)
        etPassword = view.findViewById(R.id.et_password)

        etPassword.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                login()
            }

            true

        }

        btLogin = view.findViewById(R.id.bt_login)
        btLogin.setOnClickListener(this)

        lsAuth = activity!!.findViewById(R.id.ls_auth)
        lsAuth.visibility = View.GONE

        return view
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.bt_login -> login()
        }
    }

    private fun login() {
        val username = etUsername.text.toString()
        val password = etPassword.text.toString()

        if (validateCredentials(username, password)) {
            lsAuth.visibility = View.VISIBLE
            val call =
                RetrofitClient.getInstance(activity!!).api.login(LoginRequest(username, password))

            call.enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    lsAuth.visibility = View.GONE
                    Toasty.error(
                        activity!!,
                        t.message!!,
                        Toasty.LENGTH_LONG
                    ).show()
                }

                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.body() != null) {
                        SharedPrefManager.getInstance(activity!!).saveToken(response.body()!!.key)
                        val intent = Intent(activity!!, MainActivity::class.java)
                        intent.putExtra(
                            "lang",
                            SharedPrefManager.getInstance(activity!!).getLanguage(null)
                        )
                        startActivity(intent)
                        activity!!.finish()
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

    private fun validateCredentials(username: String, password: String): Boolean {

        val usernameRegex = Regex(CustomRegex.USERNAME)

        when {
            username.isEmpty() -> {
                etUsername.error = getString(R.string.username_required)
                return false
            }

            !usernameRegex.matches(username) -> {
                etUsername.error = getString(R.string.wrong_username)
                return false
            }
        }

        when {
            password.isEmpty() -> {
                etPassword.error = getString(R.string.password_required)
                return false
            }
        }
        return true
    }
}
