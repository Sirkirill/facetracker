package ua.nure.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ua.nure.myapplication.fragments.LoginFragment


class AuthorizationPagerAdapter(
    fm: FragmentManager?
) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var loginFragment:LoginFragment = LoginFragment()
    override fun getItem(position: Int): Fragment {
        return  loginFragment
    }

    override fun getCount(): Int {
        return 1
    }
}