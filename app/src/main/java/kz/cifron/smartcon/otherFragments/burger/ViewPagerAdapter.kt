package kz.cifron.smartcon.otherFragments.burger

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

internal class ViewPagerAdapter(
    var context : Context,
    fm : FragmentManager,
    var totalTabs : Int
) : FragmentPagerAdapter(fm){
    override fun getCount(): Int {
        return totalTabs
    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 ->{
                AnonimFragment()
            }
            1->{
                ActFragment()
            }
            else->getItem(position)
        }
    }

}