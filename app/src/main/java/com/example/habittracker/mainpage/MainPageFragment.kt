package com.example.habittracker.mainpage

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.example.habittracker.ApplicationWithDaggerComponent
import com.example.habittracker.MainActivityCallback
import com.example.habittracker.R
import com.example.habittracker.databinding.FragmentMainPageBinding
import com.google.android.material.tabs.TabLayoutMediator
import javax.inject.Inject

class MainPageFragment : Fragment() {
    private var _binding: FragmentMainPageBinding? = null
    private val binding get() = _binding!!
    private var callback: MainActivityCallback? = null

    @Inject
    lateinit var viewModel: HabitsListViewModel

    override fun onAttach(context: Context) {
        super.onAttach(context)
        callback = activity as MainActivityCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val habitsListComponent =
            (requireActivity().application as ApplicationWithDaggerComponent).applicationComponent
                .getViewModelSubcomponent()
                .create()
        habitsListComponent.inject(this)
        _binding = FragmentMainPageBinding.inflate(inflater, container, false)
        binding.viewPager.adapter = HabitsListFragmentAdapter(this)
        binding.viewPager.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            if (position == 0)
                tab.text = resources.getText(R.string.good_habits_tab_title)
            else tab.text = resources.getText(R.string.bad_habits_tab_title)
        }.attach()
        binding.addHabitButton.setOnClickListener {
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