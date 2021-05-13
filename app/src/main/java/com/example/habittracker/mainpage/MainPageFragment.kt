package com.example.habittracker.mainpage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.R
import com.example.habittracker.RetrofitClient
import com.example.habittracker.databinding.FragmentMainPageBinding
import com.example.habittracker.model.HabitDatabase
import com.example.habittracker.model.HabitRepository
import com.google.android.material.tabs.TabLayoutMediator

class MainPageFragment : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null
    private lateinit var viewModel: HabitsListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(
            requireActivity(),
            HabitsListViewModelFactory(
                HabitRepository(
                    HabitDatabase.getDatabase(requireContext()).habitDao()
                )
            )
        ).get(HabitsListViewModel::class.java)

        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = HabitsListFragmentAdapter(activity as AppCompatActivity)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0)
                tab.text = resources.getText(R.string.good_habits_tab_title)
            else tab.text = resources.getText(R.string.bad_habits_tab_title)
        }.attach()
        binding.floatingActionButton.setOnClickListener {
            viewModel.clickOnFab()
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.mediatorLiveData.observe(viewLifecycleOwner, { })

        viewModel.startToCreateHabit.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                callback?.addHabit()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.mediatorLiveData.removeObservers(viewLifecycleOwner)
    }
}
