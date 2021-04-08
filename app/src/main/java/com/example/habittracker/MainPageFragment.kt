package com.example.habittracker

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.databinding.FragmentMainPageBinding
import com.google.android.material.tabs.TabLayoutMediator
import java.util.*

class MainPageFragment : Fragment() {
    companion object {
        private const val HABITS_LIST = "habits list"

        @JvmStatic
        fun newInstance(habits: ArrayList<Habit>) =
            MainPageFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(HABITS_LIST, habits)
                }
            }
    }

    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    private lateinit var habits: ArrayList<Habit>

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.getSerializable(HABITS_LIST).let {
            habits = it as ArrayList<Habit>
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = HabitsListFragmentAdapter(activity as AppCompatActivity, habits)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0)
                tab.text = "Полезные"
            else tab.text = "Вредные"
        }.attach()
        binding.floatingActionButton.setOnClickListener {
            callback?.addHabit()
        }
        return binding.root
    }
}

