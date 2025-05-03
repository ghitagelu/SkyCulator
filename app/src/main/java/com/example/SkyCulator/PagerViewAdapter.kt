import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.SkyCulator.Fragments.*


class PagerViewAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2 // Number of fragments
    override fun createFragment(position: Int): Fragment {
        return when (position) {            
            0 -> OverviewFragment()
            1 -> ListFragment()
	    else -> OverviewFragment()
       }
    }
    
}

