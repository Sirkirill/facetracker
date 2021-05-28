package ua.nure.myapplication.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import ua.nure.myapplication.fragments.LoginFragment
import ua.nure.myapplication.fragments.RegisterFragment


class AuthorizationPagerAdapter(
    fm: FragmentManager?
) :
    FragmentPagerAdapter(fm!!, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    private var loginFragment:LoginFragment = LoginFragment()
    private var registerFragment: RegisterFragment = RegisterFragment()
    override fun getItem(position: Int): Fragment {
        if(position == 0){
            return  loginFragment
        }
        return registerFragment
    }

    override fun getCount(): Int {
        return 2
    }
}